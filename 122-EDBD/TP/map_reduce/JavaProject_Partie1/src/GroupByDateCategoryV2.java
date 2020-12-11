
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
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
	public String date;
	public String category;

	public CompositeKey() {
		super();
	}
	public CompositeKey(String d, String cat) {
		date = d;
		category = cat;
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		date= arg0.readUTF();
		category = arg0.readUTF();
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		arg0.writeUTF(date);
		arg0.writeUTF(category);
	}

	@Override
	public int compareTo(CompositeKey arg0) {
		int dateCompare = date.compareTo(arg0.date);
		if (dateCompare == 0) {
			return category.compareTo(arg0.category);
		}
		return dateCompare;
	}
	public String toString() {
		return date + "\t" + category;
	}
}


public class GroupByDateCategoryV2 {
	private static final String INPUT_PATH = "input-groupBy/";
	private static final String OUTPUT_PATH = "output/groupBy-";
	private static final Logger LOG = Logger.getLogger(GroupByDateCategoryV2.class.getName());


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

	public static class Map extends Mapper<LongWritable, Text, CompositeKey, DoubleWritable> {

		private final static String emptyWords[] = { "" };

		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString().trim();
			String[] columns = line.split(",");

			// La ligne est premiere : on s'arrête
			if (key.equals(new LongWritable(0)) || Arrays.equals(columns, emptyWords)){
				return;
			}

			Double sales;
			try {
				try {
					sales = Double.parseDouble(columns[17]);
				}
				catch (NumberFormatException e) {
					sales = Double.parseDouble(columns[columns.length - 4]);
				}
			}
			catch (Exception e) {
				return;
			}
			context.write(new CompositeKey(columns[2], columns[14]), new DoubleWritable(sales));
		}
	}

	public static class Reduce extends Reducer<CompositeKey, DoubleWritable, CompositeKey, DoubleWritable> {

		@Override
		public void reduce(CompositeKey key, Iterable<DoubleWritable> values, Context context)
				throws IOException, InterruptedException {
			double sum = 0;

			for (DoubleWritable val : values){

				sum += val.get();
			}
			context.write(key, new DoubleWritable(sum));
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();

		// Pour windows-only
		//conf.set("fs.file.impl", "com.conga.services.hadoop.patch.HADOOP_7682.WinLocalFileSystem");

		Job job = new Job(conf, "Group by");

		job.setOutputKeyClass(CompositeKey.class);
		job.setOutputValueClass(Text.class);

		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);

		job.setOutputValueClass(DoubleWritable.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(INPUT_PATH));
		FileOutputFormat.setOutputPath(job, new Path(OUTPUT_PATH + Instant.now().getEpochSecond()));

		//System.exit(job.waitForCompletion(true)?0:1); // Bonne pratique pour fermer "cluster" des threads
		job.waitForCompletion(true);
	}
}