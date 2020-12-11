
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
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class GroupByTam2 {
	private static final String INPUT_PATH = "input-tam/";
	private static final String OUTPUT_PATH = "output/tam-";
	private static final Logger LOG = Logger.getLogger(GroupByTam2.class.getName());
	


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

	public static class Map extends Mapper<LongWritable, Text, Text, IntWritable > {
		
		@SuppressWarnings("unused")
		private final static IntWritable one = new IntWritable(1);
		
		@SuppressWarnings("unused")
		private final static String emptyWords[] = { "" };

		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString().trim();
			String[] columns = line.split(";");
			
			if (key.get() == 0 || columns.length != 9) { return; }
			
			context.write(new Text(columns[3]), new IntWritable(Integer.parseInt(columns[4])));
		}
	}

	public static class Reduce extends Reducer<Text, IntWritable, Text, Text> {

		@Override
		public void reduce(Text key, Iterable<IntWritable> values, Context context)
				throws IOException, InterruptedException {
			int cptBus = 0;
			int cptTram = 0;

			for(IntWritable ittVal : values){
						 
				int val = ittVal.get();
				switch (val){
				case 1:
				case 2:
				case 3:
				case 4:
					cptTram++;
					break;
				
				default:
					cptBus++;
					break;
				}
			}
			context.write(key, new Text(cptBus + "\t" + cptTram));
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		
		conf.set("mapred.textoutputformat.separator",  "\t");

		
		Job job = new Job(conf, "Group by");

		job.setOutputKeyClass(Text.class);
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