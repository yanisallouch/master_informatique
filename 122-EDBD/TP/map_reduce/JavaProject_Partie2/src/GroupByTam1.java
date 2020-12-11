
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.time.Instant;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.hadoop.conf.Configuration;
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

class CompositeKey2 implements WritableComparable<CompositeKey2>{
	
	public String ligne;
	public String heure;
	
	public CompositeKey2() {
		super();
	}
	
	public CompositeKey2(String d, String cat) {
		ligne = d;
		heure = cat;
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		ligne= arg0.readUTF();
		heure = arg0.readUTF();
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		arg0.writeUTF(ligne);
		arg0.writeUTF(heure);
		
	}

	@Override
	public int compareTo(CompositeKey2 arg0) {
		int dateCompare = ligne.compareTo(arg0.ligne);
		if (dateCompare == 0) {
			return heure.compareTo(arg0.heure);
		}
		return dateCompare;
	}
	
	public String toString() {
		return "< "+ligne + ", " + heure +",";
	}
	
}



public class GroupByTam1 {
	private static final String INPUT_PATH = "input-tam/";
	private static final String OUTPUT_PATH = "output/tam-";
	private static final Logger LOG = Logger.getLogger(GroupByTam1.class.getName());
	


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

	public static class Map extends Mapper<LongWritable, Text, CompositeKey2, IntWritable > {
		private final static IntWritable one = new IntWritable(1);
		
		@SuppressWarnings("unused")
		private final static String emptyWords[] = { "" };

		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString().trim();
			String[] columns = line.split(";");
			
			if (!columns[3].equals("OCCITANIE") || columns.length != 9) { return; }
			
			CompositeKey2 ckey = new CompositeKey2(columns[4],columns[7].substring(0,2));
			context.write(ckey, one );
		}
	}

	public static class Reduce extends Reducer<CompositeKey2, IntWritable, CompositeKey2, Text> {

		@Override
		public void reduce(CompositeKey2 key, Iterable<IntWritable> values, Context context)
				throws IOException, InterruptedException {
			int cpt = 0;
			Text res; // = values.iterator().next();
			
			for(@SuppressWarnings("unused") IntWritable value : values){
				cpt++;
			}

			res = new Text(cpt + " >");

			context.write(key, res);
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		conf.set("fs.file.impl", "com.conga.services.hadoop.patch.HADOOP_7682.WinLocalFileSystem");

		conf.set("mapred.textoutputformat.separator",  " ");

		
		Job job = new Job(conf, "Group by");

		job.setOutputKeyClass(CompositeKey2.class);
		job.setOutputValueClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);

		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(INPUT_PATH));
		FileOutputFormat.setOutputPath(job, new Path(OUTPUT_PATH + Instant.now().getEpochSecond()));

		job.waitForCompletion(true);
	}
}