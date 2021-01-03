package question6;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileUtil;
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

public class Join2 {
	static class Pair<T1, T2> {

		T1 first;
		T2 second;

		public Pair(T1 string, T2 string2) {
			this.first = string;
			this.second = string2;
		}

		public String toString() {
			return first + "," + second;
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
			int keyIndex = -1;
			int valueIndex = -1;

			if (key.equals(new LongWritable(0)) || Arrays.equals(columns, emptyWords)) {
				return;
			}

			if (columns.length == 7) {
				// output-6-1
				keyIndex = 3;
			} else if (columns.length == 15) {
				// lieux.csv
				keyIndex = 0;
				valueIndex = 8;
			}

			context.write(new Text(columns[keyIndex]), (keyIndex==0?new Text(columns[valueIndex] + "|" + columns[0]):new Text(line)));

		}
	}

	// =========================================================================
	// REDUCER
	// =========================================================================

	static class Reduce extends Reducer<Text, Text, Text, Text> {

		@Override
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {

			ArrayList<Pair<String, Integer>> continents = new ArrayList<>();
			ArrayList<Pair<Pair<Integer, Integer>, Pair<Double, Integer>>> idContinentMoyennes = new ArrayList<>();

			for (Text text : values) {
				String line = text.toString().trim();
				String[] columns = line.split(Pattern.quote("|"));

				if (columns.length == 2) {
					Pair<String, Integer> continent = new Pair<String, Integer>(columns[0], Integer.parseInt(columns[1]));
					// columns[0] == Continent | columns[1] == idEntrepot rattaché au Continent
					continents.add(continent);
				} else if (columns.length == 7) {
					Pair<Double, Integer> moyenne = new Pair<Double, Integer>(Double.parseDouble(columns[5]), Integer.parseInt(columns[6]));
					// columns[5] == moyenne | columns[6] == qte utilisé pour la moyenne
					Pair<Pair<Integer, Integer>, Pair<Double, Integer>> idContinentMoyenne = new Pair<Pair<Integer, Integer>, Pair<Double,Integer>>(new Pair<Integer, Integer>(Integer.parseInt(columns[3]), Integer.parseInt(columns[4])), moyenne);
					// columns[3] == idEntrepot | columsn[4] == idProduit
					idContinentMoyennes.add(idContinentMoyenne);
				}
			}

//			for (Pair<Pair<Integer, Integer>, Pair<Double, Integer>> pair : idContinentMoyennes) {
//				context.write(new Text(String.valueOf(pair.first.first) +"|"), new Text(String.valueOf(pair.first.second) + ", "+ String.valueOf(pair.second.first) + ", " + String.valueOf(pair.second.second)));
//			}
//			for (Pair<String, Integer> pair : continents) {
//				context.write(new Text(String.valueOf(pair.second) + "|"), new Text(pair.first));
//			}
			for (Pair<String, Integer> pair : continents) {
				for (Pair<Pair<Integer, Integer>, Pair<Double, Integer>> pair2 : idContinentMoyennes) {
					if (pair2.first.first == pair.second) {
						context.write(new Text(pair.first), new Text("|" + String.valueOf(pair2.first.second) + ", "+ String.valueOf(pair2.second.first) + ", " + String.valueOf(pair2.second.second)));
						// key == NomContinent
						// value == idProduit, moyenne, qteMoyenne
					}
				}
			}
		}
	}

	private static final String INPUT_PATH = "output/input-output-6-1"; // dimensions
	private static final String OUTPUT_PATH = "output/input-output-6-2";
	private static final Logger LOG = Logger.getLogger(Join2.class.getName());

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

		FileUtil.fullyDelete(new File(OUTPUT_PATH));

		Configuration conf = new Configuration();
		conf.set("fs.file.impl", "com.conga.services.hadoop.patch.HADOOP_7682.WinLocalFileSystem");

		Job job = new Job(conf, "Join Input-6-1 Lieu");

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setMapOutputValueClass(Text.class);

		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(INPUT_PATH));
		FileOutputFormat.setOutputPath(job, new Path(OUTPUT_PATH));

		job.waitForCompletion(true);
	}
}
