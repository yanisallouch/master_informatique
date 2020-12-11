
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.time.Instant;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
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

class CompositeKey implements WritableComparable<CompositeKey>{
	public String identifiant;
	public String nom;

	public CompositeKey() {
		super();
	}
	public CompositeKey(String id, String cat) {
		identifiant = id;
		nom = cat;
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		identifiant= arg0.readUTF();
		nom = arg0.readUTF();
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		arg0.writeUTF(identifiant);
		arg0.writeUTF(nom);
	}

	public int compareTo(CompositeKey arg0) {
		if (identifiant !=  arg0.identifiant) {
			return nom.compareTo(arg0.nom);
		}
		return identifiant.compareTo(arg0.identifiant);
	}
	public String toString() {
		return identifiant + "\t" + nom;
	}
}

class CompositeKey2 implements WritableComparable<CompositeKey2>{
	public String identifiant;
	public Double profit;

	public CompositeKey2() {
		super();
	}
	public CompositeKey2(String id, Double p) {
		identifiant = id;
		profit = p;
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		identifiant= arg0.readUTF();
		profit = arg0.readDouble();
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		arg0.writeUTF(identifiant);
		arg0.writeDouble(profit);
	}

	public int compareTo(CompositeKey2 arg0) {
		if (profit != arg0.profit){
			return profit.compareTo(arg0.profit);
		}
		return identifiant.compareTo(arg0.identifiant);
	}
	public String toString() {
		return profit.toString();
	}

}


// =========================================================================
// CLASSE MAIN
// =========================================================================

public class TriClientProfit {
	private static final String INPUT_PATH = "input-groupBy/";
	private static final String OUTPUT_PATH = "output/input-9-ClientProfit-";
	private static final String OUTPUT_PATH_2 = "output/9-TriClientProfit-";
	private static final Logger LOG = Logger.getLogger(TriAvecComparaison.class.getName());

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


	// =========================================================================
	// MAPPER
	// =========================================================================

		public static class Map extends Mapper<LongWritable, Text, CompositeKey, DoubleWritable> {

			@SuppressWarnings("unused")
			private final static String emptyWords[] = { "" };

			@Override
			public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
				String line = value.toString().trim();
				String[] columns = line.split(",");


				if(!columns[20].equals("Profit")) {
					DoubleWritable profit = new DoubleWritable(Double.parseDouble(columns[20]));
					context.write(new CompositeKey(columns[5], columns[6]), profit);
				}
			}
		}

	// =========================================================================
	// REDUCER
	// =========================================================================

	public static class Reduce extends Reducer<CompositeKey, DoubleWritable, CompositeKey, DoubleWritable> {

		@Override
		public void reduce(CompositeKey key, Iterable<DoubleWritable> values, Context context)
				throws IOException, InterruptedException {
			Double sum = 0.0;
			for(DoubleWritable val : values) {
				sum+= val.get();
			}
			context.write(key, new DoubleWritable(sum));
		}
	}

	// =========================================================================
	// MAPPER2
	// =========================================================================

	public static class Map2 extends Mapper<LongWritable, Text, CompositeKey2, Text> {

		@SuppressWarnings("unused")
		private final static String emptyWords[] = { "" };

		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString().trim();
			String[] columns = line.split("\t");

			Double profit = Double.parseDouble(columns[2]);
			CompositeKey2 outKey = new CompositeKey2(columns[0],profit);
			context.write(outKey, new Text(columns[0]+"\t" + columns[1]));
		}
	}

	// =========================================================================
	// REDUCER2
	// =========================================================================

	public static class Reduce2 extends Reducer<CompositeKey2, Text, Text, CompositeKey2> {

		@Override
		public void reduce(CompositeKey2 key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			for (Text value : values){
				context.write(value, key);
			}

		}
	}


	// =========================================================================
	// MAIN
	// =========================================================================

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		conf.set("fs.file.impl", "com.conga.services.hadoop.patch.HADOOP_7682.WinLocalFileSystem");
		Job job = new Job(conf, "9-SortP1");

		/*
		 * Affectation de la classe du comparateur au job.
		 * Celui-ci sera appelé durant la phase de shuffle.
		 */

		job.setOutputKeyClass(CompositeKey.class);
		job.setOutputValueClass(DoubleWritable.class);

		job.setMapperClass(Map.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(INPUT_PATH));

		long time = Instant.now().getEpochSecond() ;

		FileOutputFormat.setOutputPath(job, new Path(OUTPUT_PATH + time));

		job.waitForCompletion(true);

		Job job2 = new Job(conf, "9-SortP2");

		/*
		 * Affectation de la classe du comparateur au job.
		 * Celui-ci sera appelé durant la phase de shuffle.
		 */

		job2.setOutputKeyClass(Text.class);
		job2.setOutputValueClass(CompositeKey2.class);

		job2.setMapOutputKeyClass(CompositeKey2.class);
		job2.setMapOutputValueClass(Text.class);

		job2.setMapperClass(Map2.class);
		job2.setReducerClass(Reduce2.class);

		job2.setInputFormatClass(TextInputFormat.class);
		job2.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job2, new Path(OUTPUT_PATH + time));
		FileOutputFormat.setOutputPath(job2, new Path(OUTPUT_PATH_2 + time ));

		job2.waitForCompletion(true);
	}
}