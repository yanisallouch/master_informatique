package question6;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Pattern;

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


public class Join3 {
	static class Pair<T1, T2> {

		T1 first;
		T2 second;

		public Pair(T1 string, T2 string2) {
			this.first = string;
			this.second = string2;
		}

		public String toString() {
			return first + ", " + second;
		}

	}

	// =========================================================================
	// MAPPER
	// =========================================================================

	static class Map extends Mapper<LongWritable, Text, Text, Text> {

		private final static String emptyWords[] = { "" };

		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

			String line = value.toString().trim();
			String[] columns = line.split(Pattern.quote("|"));
			int keyIndex = 0;

			if (key.equals(new LongWritable(0)) || Arrays.equals(columns, emptyWords)) {
				return;
			}

			context.write(new Text(columns[keyIndex]), new Text(line));

		}
	}

	// =========================================================================
	// REDUCER
	// =========================================================================

	static class Reduce extends Reducer<Text, Text, Text, Text> {

		@Override
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {

			String nomContinent = "";
			ArrayList<Pair<Integer, Double>> produitsMoyennes = new ArrayList<>();
			int sumPertes = 0;
			int sumCouts = 0;

			for (Text text : values) {
				String line = text.toString().trim();
				String[] columns = line.split(Pattern.quote("|"));
				String[] columns2 = columns[1].trim().split(",");
				nomContinent = columns[0];
				if (columns2.length == 3) {
					sumPertes += Integer.parseInt(columns2[1].trim());
					sumCouts += Integer.parseInt(columns2[2].trim());
					produitsMoyennes.add(new Pair<Integer, Double>(Integer.parseInt(columns2[0].trim()),(sumCouts/(sumPertes+0.0))));
				}
			}
			for (Pair<Integer, Double> pair : produitsMoyennes) {
				if (Double.parseDouble(String.valueOf(pair.second)) >= (sumCouts/(sumPertes+0.0)) ) {
					context.write(new Text(nomContinent), new Text("|" + pair.first + ", " + (sumPertes/(sumCouts+0.0))));
				}
			}
		}
	}
	private static final String INPUT_PATH = "output/input-output-10-2"; // dimensions
	private static final String OUTPUT_PATH = "output/input-output-10-3-";
	private static final Logger LOG = Logger.getLogger(Join3.class.getName());

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

		Job job = new Job(conf, "Join Input-10-2 Continent Lieux");

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

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