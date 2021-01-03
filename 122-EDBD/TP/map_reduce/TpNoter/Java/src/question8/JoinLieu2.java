package question8;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
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


public class JoinLieu2 {
	private static final String INPUT_PATH = "input-output-8-1/";
	private static final String OUTPUT_PATH = "input-output-8-2/";
	private static final Logger LOG = Logger.getLogger(JoinLieu2.class.getName());
	


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

	public static class Map extends Mapper<LongWritable, Text, Text, Text> {
		
		private final static String emptyWords[] = { "" };

		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString().trim();
			String[] columns = line.split("\\|");
			
			// La ligne est la premiere de Lieux : on s'arrête
			if (Arrays.equals(columns, emptyWords) || columns[0].equals("id") ){
				return;
			}
			
			//on est dans une ligne de Lieux on transmet le continent
			if (columns.length == 15) {
				context.write(new Text(columns[0]), new Text(columns[8]));
				return;
			}
			
			context.write(new Text(columns[0]), new Text(columns[1]+","+ columns[2]));
		}
	}

	public static class Reduce extends Reducer<Text, Text, Text, Text> {

		@Override
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			String continent = "ERROOOOR NO MATCHING PLACE IN LIEUX";
			ArrayList<Text> valeurs = new ArrayList<>();
			String[] val = null;
			for (Text value :values) {
				val = value.toString().split(",");
				if (val.length == 1){
					continent = value.toString();
				}
				else {
					valeurs.add(new Text(value));
				}
			}
			for (Text value: valeurs) {
				context.write(new Text(continent), value);
			}
	
		}
	}

	public static void main(String[] args) throws Exception {
		
		FileUtil.fullyDelete(new File(OUTPUT_PATH));
		
		Configuration conf = new Configuration();
		
		conf.set("mapred.textoutputformat.separator", ",");
		
		Job job = new Job(conf, "Group by");

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);

		job.setOutputValueClass(Text.class); 

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(INPUT_PATH));
		FileOutputFormat.setOutputPath(job, new Path(OUTPUT_PATH));

		job.waitForCompletion(true);
	}
}