package question4;

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


public class GroupBy3 {
	private static final String INPUT_PATH = "input-output-4-2/";
	private static final String OUTPUT_PATH = "output/question4-";
	private static final Logger LOG = Logger.getLogger(GroupBy3.class.getName());
	


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
		
		
			IntWritable montant = new IntWritable(Integer.parseInt(columns[1]));
			context.write(new Text(columns[0]), montant);

		}
	}

	public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
		
		private TreeMap<IntWritable, List<Text>> sortedContinents = new TreeMap<>();
		private int nbSortedContinents = 0;
		private int k;
		
		@Override
		public void setup(Context context) {
			// On charge k
			k = context.getConfiguration().getInt("k", 1);
		}
		
		@Override
		public void reduce(Text key, Iterable<IntWritable> values, Context context)
				throws IOException, InterruptedException {
			
			int  s = 0;
			
			Text keyCopy = new Text(key);
			for (IntWritable val: values ) {
				s+= val.get();
				
			}
			
			IntWritable sum = new IntWritable(s);
			
			// Profit déjà présent
			if (sortedContinents.containsKey(sum)) {
				sortedContinents.get(sum).add(keyCopy);
			}
			
			else {
				List<Text> continents = new ArrayList<>();
				continents.add(keyCopy);
				sortedContinents.put(sum, continents);
			}

			// Nombre de lignes enregistrées atteint : on supprime la ligne avec le plus petit profit (la première dans sortedWords)
			if (nbSortedContinents == k) {
				IntWritable firstKey = sortedContinents.firstKey();

				List<Text> continents = sortedContinents.get(firstKey);
				
				int sizeBefore = continents.size();
				
				continents.remove(continents.size() - 1);
				assert(continents.size() == sizeBefore -1);
				if (continents.isEmpty())
					sortedContinents.remove(firstKey);
			} else
				nbSortedContinents++;
	
		}
		
		public void cleanup(Context context) throws IOException, InterruptedException{
			for (IntWritable sum : sortedContinents.descendingKeySet()) {
				for (Text continent: sortedContinents.get(sum)) {
					context.write(continent, sum);
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		FileUtil.fullyDelete(new File(OUTPUT_PATH));
		
		Configuration conf = new Configuration();
		conf.set("mapred.textoutputformat.separator", ",");
		conf.setInt("k", 10);
		
		
		Job job = new Job(conf, "Group by");

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

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