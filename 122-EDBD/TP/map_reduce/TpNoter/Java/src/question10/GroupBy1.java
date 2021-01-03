package question10;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;


public class GroupBy1 {
	static class ProductionKey implements WritableComparable<ProductionKey>{
		public Integer idProduit;
		public Integer idLieu;
		public Integer typeOperation;

		public ProductionKey() {
			super();
		}

		public ProductionKey(String idProduit, String columns, String typeOperation) {
			super();
			this.idProduit = Integer.parseInt(idProduit);
			this.idLieu = Integer.parseInt(columns);
			this.typeOperation = Integer.parseInt(typeOperation);
		}

		@Override
		public void readFields(DataInput arg0) throws IOException {
			this.idProduit =  arg0.readInt();
			this.idLieu =  arg0.readInt();
			this.typeOperation = arg0.readInt();
		}

		@Override
		public void write(DataOutput arg0) throws IOException {
			arg0.writeInt(idProduit);
			arg0.writeInt(idLieu);
			arg0.writeInt(typeOperation);
		}

		@Override
		public int compareTo(ProductionKey arg0) {
			int compare = idProduit.compareTo(arg0.idProduit);
			if(compare == 0) {
				if (idLieu.compareTo(arg0.idLieu) != 0){
					return idLieu.compareTo(arg0.idLieu);
				}
				if (typeOperation.compareTo(arg0.typeOperation) != 0) {
					return typeOperation.compareTo(arg0.typeOperation);
				}
			}
			return compare;
		}

		public String toString() {
			return this.idProduit + "," + this.idLieu + "," + this.typeOperation;
		}
	}

	//=========================================================================
	//MAPPER
	//=========================================================================

	static class Map extends Mapper<LongWritable, Text, ProductionKey, Text> {

		private final static String emptyWords[] = { "" };

		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

			String line = value.toString().trim();
			String[] columns = line.split(",");
			ProductionKey cleComposite;
			int valueIndex = -1;

			if (key.equals(new LongWritable(0)) || Arrays.equals(columns, emptyWords)) {
				return;
			}
			if (columns.length  <= 9) {
				// 10 ==> id d'un lieu géographique
				cleComposite = new ProductionKey(
						columns[0],
						columns[1],
						(columns[6].equals("perte")?"1":"0"));
				valueIndex = 8;
			}
			else {
//				System.out.println("Column length is "+ columns.length);
				return;
			}
			context.write(cleComposite, new Text(columns[valueIndex])); // 
		}
	}

	// =========================================================================
	// REDUCER
	// =========================================================================

	static class Reduce extends Reducer<ProductionKey, Text, ProductionKey, Text> {

		@Override
		public void reduce(ProductionKey key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {

			ArrayList<Text> coutsProduit = new ArrayList<Text>();
			int sumPertes = 0;
			int sumCout = 0;

			for (Text val : values){
				coutsProduit.add(val);
			}

			String line = key.toString().trim();
			String[] columns = line.split(",");

			for (Text text : coutsProduit) {
				if (columns[2].equals("1") ) {
					sumPertes += Integer.parseInt(text.toString());
				}
				else {
					sumCout += Integer.parseInt(text.toString());
				}
			}

			context.write(key, new Text("|" + sumPertes + "," + sumCout));
		}
	}
	
	private static final String INPUT_PATH = "input-productions/"; // dimensions
	private static final String OUTPUT_PATH = "output/input-output-10-1";
	private static final Logger LOG = Logger.getLogger(GroupBy1.class.getName());

	static {
		System.setProperty("java.util.logging.SimpleFormatter.format", "%5$s%n%6$s");

		try {
			FileHandler fh = new FileHandler("out.log");
			fh.setFormatter(new SimpleFormatter());
			LOG.addHandler(fh);
		} catch (SecurityException | IOException e) {
			System.exit(1);
		}
	}

	/**
	 * Quels sont les chiffres d’affaires liés à chacun des produits vendus dans une zone
géographique ?
	 */
	public static void main(String[] args) throws Exception {

		FileUtil.fullyDelete(new File(OUTPUT_PATH));

		Configuration conf = new Configuration();
		conf.set("fs.file.impl", "com.conga.services.hadoop.patch.HADOOP_7682.WinLocalFileSystem");

		Job job = new Job(conf, "GroupBy Produit & Lieux, ");

		job.setOutputKeyClass(ProductionKey.class);
		job.setOutputValueClass(Text.class);

		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(INPUT_PATH));
		FileOutputFormat.setOutputPath(job, new Path(OUTPUT_PATH));

		job.waitForCompletion(true);
	}
}
