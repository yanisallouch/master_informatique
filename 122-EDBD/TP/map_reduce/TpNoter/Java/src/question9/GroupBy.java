package question9;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
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
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;


public class GroupBy {
	private static final String INPUT_PATH = "input-productions";
	private static final String OUTPUT_PATH = "output/question9-";
	private static final Logger LOG = Logger.getLogger(GroupBy.class.getName());
	


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

	public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
		
		private final static String emptyWords[] = { "" };

		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString().trim();
			String[] columns = line.split(",");
			
			//ligne vide ou premier ligne, ou pas une vente ou une perte
			if (Arrays.equals(columns, emptyWords) || columns[0].equals("idproduit") || (!columns[6].equals("vente") && !columns[6].equals("perte")) ){
				return;
			}
			
			int quantite;
			
			if (columns[6].equals("vente"))
				quantite = Integer.parseInt(columns[7]);
			else quantite = - Integer.parseInt(columns[7]);
	
			context.write(new Text(columns[0]), new IntWritable(quantite));

		}
	}

	public static class Reduce extends Reducer<Text, IntWritable, Text, DoubleWritable> {
		
		
		@Override
		public void reduce(Text key, Iterable<IntWritable> values, Context context)
				throws IOException, InterruptedException {
			
			int quantiteVendue = 0;
			int quantitePerdue = 0;
			for (IntWritable value: values) {
				int quantite = value.get();
				if (quantite >0) quantiteVendue += quantite;
				else quantitePerdue += - quantite;
				}
			
			Double taux = (double)quantitePerdue/ (double) quantiteVendue;
			
			context.write(key, new DoubleWritable(taux));
			
	
		}
	
	
	}

	public static void main(String[] args) throws Exception {
		FileUtil.fullyDelete(new File(OUTPUT_PATH));
		
		Configuration conf = new Configuration();
		conf.set("mapred.textoutputformat.separator", ",");
		
		
		Job job = new Job(conf, "Group by");

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Double.class);
		job.setMapOutputValueClass(IntWritable.class);

		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);

		job.setOutputValueClass(IntWritable.class); 

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(INPUT_PATH));
		FileOutputFormat.setOutputPath(job, new Path(OUTPUT_PATH+Instant.now().getEpochSecond()));

		job.waitForCompletion(true);
	}
}