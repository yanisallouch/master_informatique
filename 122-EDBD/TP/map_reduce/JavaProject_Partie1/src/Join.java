
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
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;


public class Join {
	private static final String INPUT_PATH = "input-join/";
	private static final String OUTPUT_PATH = "output/join-";
	private static final Logger LOG = Logger.getLogger(Join.class.getName());

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
			int keyIndex = -1;
			int valueIndex = -1;
			String tableId;

			// La ligne est premiere : on s'arrête
			if (key.equals(new LongWritable(0)) || Arrays.equals(columns, emptyWords)){
				return;
			}
			if (columns.length  == 9) {
				keyIndex = 1;
				valueIndex = 8;
				tableId = "";
				}
			else if (columns.length == 8) {
				keyIndex = 0;
				valueIndex = 1;
				tableId = "c,&";
			}
			else {
				System.out.println("Column length is "+ columns.length);
				return;
			}
			context.write(new Text(columns[keyIndex]), new Text(tableId +columns[valueIndex]));
		}
	}

	public static class Reduce extends Reducer<Text, Text, Text, Text> {

		@Override
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			ArrayList<Text> customers = new ArrayList<Text>();
			ArrayList<Text> orders = new ArrayList<Text>();
			for (Text val : values){
				String[] texte = val.toString().split(",&");
				if (texte.length == 2) {
					customers.add(new Text(texte[1]));
				}
				else orders.add(val);
			}
			for (Text customer: customers) {
				for (Text order : orders) {
					context.write(customer, order);
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		conf.set("mapred.textoutputformat.separator", "\t||\t");

		// Pour windows-only
		//conf.set("fs.file.impl", "com.conga.services.hadoop.patch.HADOOP_7682.WinLocalFileSystem");

		Job job = new Job(conf, "Group by");

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);

		job.setOutputValueClass(Text.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(INPUT_PATH));
		FileOutputFormat.setOutputPath(job, new Path(OUTPUT_PATH + Instant.now().getEpochSecond()));

		//System.exit(job.waitForCompletion(true)?0:1); // Bonne pratique pour fermer "cluster" des threads
		job.waitForCompletion(true);
	}
}