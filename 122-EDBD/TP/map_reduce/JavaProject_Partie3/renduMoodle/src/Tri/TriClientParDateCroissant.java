package Tri;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.time.Instant;
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

// =========================================================================
// COMPARATEURS
// =========================================================================


class DateWritable  implements WritableComparable<DateWritable>{
	public Integer annee;
	public Integer mois;
	public Integer jour;
	
	public DateWritable() {
		super();
	}

	public DateWritable(String d) {
		String[] tokens = d.trim().split("/");
		annee =  Integer.parseInt(tokens[2]);
		mois =  Integer.parseInt(tokens[0]); 
		jour = Integer.parseInt(tokens[1]);
	}

	public void readFields(DataInput arg0) throws IOException {
		annee = arg0.readInt();
		mois = arg0.readInt();
		jour = arg0.readInt();
	}

	public void write(DataOutput arg0) throws IOException {
		arg0.writeInt((int)annee);
		arg0.writeInt((int)mois);
		arg0.writeInt((int)jour);
	}

	public String toString() {
		//String s = annee + "-" + mois + "-" + jour;
		return String.join("-", String.format("%02d",annee), String.format("%02d",mois), String.format("%02d",jour));
	}

	public int compareTo(DateWritable arg0) {
		if (annee.compareTo(arg0.annee) != 0){
			return annee.compareTo(arg0.annee);
		}
		if (mois.compareTo(arg0.mois) != 0){
			return mois.compareTo(arg0.mois);
		}
		return jour.compareTo(arg0.jour);

	}
}

// =========================================================================
// CLASSE MAIN
// =========================================================================

public class TriClientParDateCroissant {
	private static final String INPUT_PATH = "input-groupBy/";
	private static final String OUTPUT_PATH = "output/9-TriAvecComparaisonCroissant-";
	private static final Logger LOG = Logger.getLogger(TriClientParDateCroissant.class.getName());

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

	public static class Map extends Mapper<LongWritable, Text, DateWritable, Text> {

		@SuppressWarnings("unused")
		private final static String emptyWords[] = { "" };

		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString().trim();
			String[] columns = line.split(",");

			if(!columns[20].equals("Profit")) {
				context.write(new DateWritable(columns[3]), new Text(columns[1]));
			}
		}
	}

	// =========================================================================
	// REDUCER
	// =========================================================================

	public static class Reduce extends Reducer<DateWritable, Text, DateWritable, Text> {

		@Override
		public void reduce(DateWritable key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			for(Text val : values) {
				context.write(key, val);
			}
		}
	}

	// =========================================================================
	// MAIN
	// =========================================================================

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		conf.set("fs.file.impl", "com.conga.services.hadoop.patch.HADOOP_7682.WinLocalFileSystem");
		Job job = new Job(conf, "9-Sort");

		/*
		 * Affectation de la classe du comparateur au job.
		 * Celui-ci sera appelé durant la phase de shuffle.
		 */
		
		job.setOutputKeyClass(DateWritable.class);
		job.setOutputValueClass(Text.class);

		job.setMapperClass(Map.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(INPUT_PATH));
		FileOutputFormat.setOutputPath(job, new Path(OUTPUT_PATH + Instant.now().getEpochSecond()));

		job.waitForCompletion(true);
	}
}