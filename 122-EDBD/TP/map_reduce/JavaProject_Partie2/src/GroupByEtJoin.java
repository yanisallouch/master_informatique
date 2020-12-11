
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;


public class GroupByEtJoin {
	private static final String INPUT_PATH = "input-join/";
	private static final String OUTPUT_PATH = "output/groupby-join-";
	private static final Logger LOG = Logger.getLogger(GroupByEtJoin.class.getName());
	


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

			//ORDERS
			if (columns.length  == 9) {
				keyIndex = 1;
				valueIndex = 3;
				tableId = "";
				}

			//CUSTOMERS
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

	public static class Reduce extends Reducer<Text, Text, Text, DoubleWritable> {

		@Override
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			
			ArrayList<Text> customers = new ArrayList<Text>();
			Double sumTotalprice  = 0.;

			for (Text val : values){
				String[] texte = val.toString().split(",&");
				if (texte.length == 2) {
					customers.add(new Text(texte[1]));
				}
				else {
					try {
						sumTotalprice += Double.parseDouble(val.toString());
					}
					catch (Exception e) {
					}
				}
			}
			
			for (Text customer: customers) {
				context.write(customer, new DoubleWritable(sumTotalprice));
			}
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		
		conf.set("mapred.textoutputformat.separator", "\t||\t");
		
		Job job = new Job(conf, "Group by");

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(DoubleWritable.class);
		
		job.setMapOutputValueClass(Text.class);

		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);



		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(INPUT_PATH));
		FileOutputFormat.setOutputPath(job, new Path(OUTPUT_PATH + Instant.now().getEpochSecond()));

		job.waitForCompletion(true);
	}
}