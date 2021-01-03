package question10;

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
			String[] partGauche = columns[0].trim().split(",");
			int keyIndex = -1;
			int valueIndex = -1;

			if (key.equals(new LongWritable(0)) || Arrays.equals(columns, emptyWords)) {
				return;
			}

			if (columns.length == 2) {
				// output-10-1
				keyIndex = 1;
			} else if (columns.length == 15) {
				// lieux.csv
				keyIndex = 0;
				valueIndex = 8;
			}

			context.write(new Text(partGauche[keyIndex]), (keyIndex==0?new Text(columns[valueIndex] + "|" + columns[keyIndex]):new Text(line)));

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
			ArrayList<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> idContinentPairPerteCouts = new ArrayList<>();

			for(Text text : values) {
				String line = text.toString().trim();
				String[] columns = line.split(Pattern.quote("|"));
				line = columns[0].trim();
				String[] partGauche = line.split(",");
				line = columns[1].trim();
				String[] partDroit = line.split(",");

				if (partGauche.length == 1) {
					Pair<String, Integer> continent = new Pair<String, Integer>(partGauche[0], Integer.parseInt(partDroit[0]));
					// columns[0] == Continent | columns[1] == idEntrepot rattaché au Continent
					continents.add(continent);
				}
				else if (partGauche.length == 3) {
					Pair<Integer, Integer> PairSumPerteCout = new Pair<Integer, Integer>(Integer.parseInt(partDroit[0]), Integer.parseInt(partDroit[1]));
//					// columns[5] == SumPerte | columns[6] == SumCout
					Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> idContinentPairPerteCout = new Pair<Pair<Integer, Integer>, Pair<Integer,Integer>>(new Pair<Integer, Integer>(Integer.parseInt(partGauche[1]), Integer.parseInt(partGauche[0])), PairSumPerteCout);
//					// columns[3] == idEntrepot | columsn[4] == idProduit
					idContinentPairPerteCouts.add(idContinentPairPerteCout);
				}
			}

			for (Pair<String, Integer> pair : continents) {
				for (Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> pair2 : idContinentPairPerteCouts) {
					if (pair2.first.first == pair.second) {
						context.write(new Text(pair.first), new Text("|" + String.valueOf(pair2.first.second) + ", "+ String.valueOf(pair2.second.first) + ", " + String.valueOf(pair2.second.second)));
						// key == NomContinent
						// value == idProduit, moyenne, qteMoyenne
					}
				}
			}
		}
	}
	private static final String INPUT_PATH = "output/input-output-10-1"; // dimensions
	private static final String OUTPUT_PATH = "output/input-output-10-2";
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

		Job job = new Job(conf, "Join Input-10-1 Lieu");

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