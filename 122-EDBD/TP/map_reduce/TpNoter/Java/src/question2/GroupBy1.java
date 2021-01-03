package question2;

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
import org.apache.hadoop.io.IntWritable;
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

class ProductionKey implements WritableComparable<ProductionKey>{
	public Integer idProduit;
	public Integer anneeDebut;
	public Integer moisDebut;
	public Integer anneeFin;
	public Integer moisFin;

	public ProductionKey() {
		super();
	}

	public ProductionKey(String idProduit, String columns, String columns2, String columns3, String columns4,
			String columns5, String columns6) {
		super();
		this.idProduit = Integer.parseInt(idProduit);
		this.anneeDebut = Integer.parseInt(columns);
		this.moisDebut = Integer.parseInt(columns2);
		this.anneeFin = Integer.parseInt(columns4);
		this.moisFin = Integer.parseInt(columns5);
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		this.idProduit =  arg0.readInt();
		this.anneeDebut =  arg0.readInt();
		this.moisDebut = arg0.readInt();
		this.anneeFin =  arg0.readInt();
		this.moisFin = arg0.readInt();
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		arg0.writeInt(idProduit);
		arg0.writeInt(anneeDebut);
		arg0.writeInt(moisDebut);
		arg0.writeInt(anneeFin);
		arg0.writeInt(moisFin);
	}

	@Override
	public int compareTo(ProductionKey arg0) {
		int compare = idProduit.compareTo(arg0.idProduit);
		if(compare == 0) {
			if (anneeDebut.compareTo(arg0.anneeDebut) != 0){
				return anneeDebut.compareTo(arg0.anneeDebut);
			}
			if (moisDebut.compareTo(arg0.moisDebut) != 0){
				return moisDebut.compareTo(arg0.moisDebut);
			}
		}
		return compare;
	}

	public String toString() {
		return this.idProduit + "," + String.join(",", String.format("%02d",anneeDebut), String.format("%02d",moisDebut)) + "," + String.join(",", String.format("%02d",anneeFin), String.format("%02d",moisFin)) ;
	}
}

//=========================================================================
//MAPPER
//=========================================================================

class Map extends Mapper<LongWritable, Text, ProductionKey, Text> {

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
			valueIndex = 8;
			String[] dateDebut = columns[4].split("-");
			String[] dateFin = columns[5].split("-");
			cleComposite = new ProductionKey(
					columns[0],
					dateDebut[0],
					dateDebut[1],
					dateDebut[2],
					dateFin[0],
					dateFin[1],
					dateFin[2]);
		}
		else {
//			System.out.println("Column length is "+ columns.length);
			return;
		}
		context.write(cleComposite, new Text(columns[valueIndex])); // 
	}
}

// =========================================================================
// REDUCER
// =========================================================================

class Reduce extends Reducer<ProductionKey, Text, ProductionKey, IntWritable> {

	@Override
	public void reduce(ProductionKey key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {

		ArrayList<Text> coutsProduit = new ArrayList<Text>();
		int sum = 0;

		for (Text val : values){
			coutsProduit.add(val);
		}

		for (Text text : coutsProduit) {
			sum += Integer.parseInt(text.toString());
		}
		context.write(key, new IntWritable(sum));
	}
}

public class GroupBy1 {
	private static final String INPUT_PATH = "input-productions/"; // dimensions
	private static final String OUTPUT_PATH = "output/input-output-1-1";
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

		Job job = new Job(conf, "CA / Produits / Mois / GroupBy");

		job.setOutputKeyClass(ProductionKey.class);
		job.setOutputValueClass(IntWritable.class);

		job.setMapOutputValueClass(Text.class);

		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(INPUT_PATH));
		FileOutputFormat.setOutputPath(job, new Path(OUTPUT_PATH));

		job.waitForCompletion(true);
	}
}
