package question2;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.hadoop.conf.Configuration;
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

public class Join2 {
	static class Pair<T1, T2>{

		T1 first;
		T2 second;

		public Pair(T1 string, T2 string2) {
			this.first = string;
			this.second = string2;
		}

		public String toString() {
			return first + "\t" + second;
		}
	}

	static class ProductionKey implements WritableComparable<ProductionKey>{
		public String idProduit;
		public Integer anneeDebut;
		public Integer moisDebut;
		public Integer jourDebut;
		public Integer anneeFin;
		public Integer moisFin;
		public Integer jourFin;

		public ProductionKey() {
			super();
		}

		public ProductionKey(String idProduit, String columns, String columns2, String columns3, String columns4,
				String columns5, String columns6) {
			super();
			this.idProduit = idProduit;
			this.anneeDebut = Integer.parseInt(columns);
			this.moisDebut = Integer.parseInt(columns2);
			this.jourDebut = Integer.parseInt(columns3);
			this.anneeFin = Integer.parseInt(columns4);
			this.moisFin = Integer.parseInt(columns5);
			this.jourFin = Integer.parseInt(columns6);
		}



		@Override
		public void readFields(DataInput arg0) throws IOException {
			this.idProduit =  arg0.readUTF();
			this.anneeDebut =  arg0.readInt();
			this.moisDebut = arg0.readInt();
			this.jourDebut = arg0.readInt();
			this.anneeFin =  arg0.readInt();
			this.moisFin = arg0.readInt();
			this.jourFin = arg0.readInt();
		}

		@Override
		public void write(DataOutput arg0) throws IOException {
			arg0.writeUTF(idProduit);
			arg0.writeInt(anneeDebut);
			arg0.writeInt(moisDebut);
			arg0.writeInt(jourDebut);
			arg0.writeInt(anneeFin);
			arg0.writeInt(moisFin);
			arg0.writeInt(jourFin);
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
				return jourDebut.compareTo(arg0.jourDebut);
			}
			return compare;
		}

		public String toString() {
			return this.idProduit + "," + String.join(",", String.format("%02d",anneeDebut), String.format("%02d",moisDebut), String.format("%02d",jourDebut)) + "," + String.join(",", String.format("%02d",anneeFin), String.format("%02d",moisFin), String.format("%02d",jourFin)) ;
		}
	}

	//=========================================================================
	//MAPPER
	//=========================================================================

	static class Map extends Mapper<LongWritable, Text, Text, Text> {

		private final static String emptyWords[] = { "" };

		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

			String line = value.toString().trim();
			String[] columns = line.split(",");
			int keyIndex = -1;
			int valueIndex = -1;

			if (key.equals(new LongWritable(0)) || Arrays.equals(columns, emptyWords)) {
				return;
			}
			if (columns.length < 7) {
				// output du job 1
				keyIndex = 2;
				valueIndex = 5; // CA du produit
			}
			else if (columns.length < 20) {
				// dates.csv
				keyIndex = 5;
				valueIndex = 13; // nom du mois associé a <keyIndex>
			}
			else {
//				System.out.println("Column length is "+ columns.length);
				return;
			}
			context.write(new Text(columns[keyIndex]), (keyIndex==5?new Text(columns[valueIndex]):new Text(columns[0] + "," + columns[valueIndex].substring(2)))); // 
		}
	}

	//=========================================================================
	//REDUCER
	//=========================================================================

	class Reduce extends Reducer<Text, Text, Text, Text> {

		@Override
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			String month = "";
			ArrayList<Pair<String, String>> produits = new ArrayList<Pair<String, String>>();

			for (Text val: values) {
				String[] texte = val.toString().split(",");
				System.out.println(val);
				if (texte.length == 1 && !texte[0].equals("")) {
					month = texte[0];
				} else if (texte.length <= 2 ) {
					produits.add(new Pair<String, String>(texte[0], texte[1]));
				} else {
					System.out.println("reduce undefined");
				}
			}

			for (Pair<String, String> pair : produits) {
				context.write(new Text(month), new Text(pair.toString()));
			}
		}
	}

	private static final String INPUT_PATH = "output/input-output-1-1/"; // dimensions
	private static final String OUTPUT_PATH = "output/input-output-1-2-";
	private static final Logger LOG = Logger.getLogger(Join2.class.getName());

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

		Configuration conf = new Configuration();
		conf.set("fs.file.impl", "com.conga.services.hadoop.patch.HADOOP_7682.WinLocalFileSystem");

		Job job = new Job(conf, "CA / Produits / Mois / Join");

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(INPUT_PATH));
		FileOutputFormat.setOutputPath(job, new Path(OUTPUT_PATH + Instant.now().getEpochSecond()));

		job.waitForCompletion(true);
	}
}
