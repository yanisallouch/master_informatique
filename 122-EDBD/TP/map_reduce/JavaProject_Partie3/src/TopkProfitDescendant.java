
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
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


/*
 * Jusqu'à présent nous avons défini nos mappers et reducers comme des classes internes à notre classe principale.
 * Dans des applications réelles de map-reduce cela ne sera généralement pas le cas, les classes seront probablement localisées dans d'autres fichiers.
 * Dans cet exemple, nous avons défini Map et Reduce en dehors de notre classe principale.
 * Il se pose alors le problème du passage du paramètre 'k' dans notre reducer, car il n'est en effet plus possible de déclarer un paramètre k dans notre classe principale qui serait partagé avec ses classes internes ; c'est la que la Configuration du Job entre en jeu.
 */

// =========================================================================
// MAPPER
// =========================================================================



public class TopkProfitDescendant {
	class Map extends Mapper<LongWritable, Text, Text, Text> {

		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();
			String[] words = line.split(",");
			
			if(!words[20].equals("Profit")) {
				context.write(new Text(words[0]), value);
			}
		}
	}

	// =========================================================================
	// REDUCER
	// =========================================================================

	class Reduce extends Reducer<Text, Text, Text, DoubleWritable> {
		/**
		 * Map avec tri suivant l'ordre naturel de la clé (la clé représentant la fréquence d'un ou plusieurs mots).
		 * Utilisé pour conserver les k mots les plus fréquents.
		 * 
		 * Il associe une fréquence à une liste de mots.
		 */
		
		private TreeMap<Double, List<Text>> sortedValues = new TreeMap<>();
		private int nbsortedValue = 0;
		private int k;

		/**
		 * Méthode appelée avant le début de la phase reduce.
		 */
		
		@Override
		public void setup(Context context) {
			// On charge k
			k = context.getConfiguration().getInt("k", 1);
		}

		@Override
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			
			String line = "";
			String[] columns;;
			Double sum = Double.valueOf(0);
			for (Text text : values) {
				line = text.toString();
				columns = line.split(",");
				sum = Double.parseDouble(columns[20]);

				// On copie car l'objet key reste le même entre chaque appel du reducer
				Text keyCopy = new Text(key);

				// Fréquence déjà présente
				if (sortedValues.containsKey(sum)) {
					sortedValues.get(sum).add(keyCopy);
				}
				else {
					List<Text> words = new ArrayList<>();
					words.add(new Text(keyCopy));
					sortedValues.put(sum, words);
				}

				// Nombre de mots enregistrés atteint : on supprime le mot le moins fréquent (le premier dans sortedWords)
				if (nbsortedValue == k) {
					Double firstKey = sortedValues.firstKey();
					List<Text> words = sortedValues.get(firstKey);
					words.remove(words.size() - 1);
					if (words.isEmpty()) {
						sortedValues.remove(firstKey);
					}
				} else {
					nbsortedValue++;
				}
			}
		}

		/**
		 * Méthode appelée à la fin de l'étape de reduce.
		 * 
		 * Ici on envoie les mots dans la sortie, triés par ordre descendant.
		 */
		@Override
		public void cleanup(Context context) throws IOException, InterruptedException {

			Double[] nbofs = sortedValues.keySet().toArray(new Double[0]);
			// Parcours en sens inverse pour obtenir un ordre descendant
			int i = nbofs.length;
			
			while (i-- != 0) {
				Double nbof = nbofs[i];
				for (Text words : sortedValues.get(nbof))
					context.write(words, new DoubleWritable(nbofs[i]));
			}
		}
	}
	
	private static final String INPUT_PATH = "input-groupBy/";
	private static final String OUTPUT_PATH = "output/TopkProfitDesc-";
	private static final Logger LOG = Logger.getLogger(TopkProfitDescendant.class.getName());

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
	 * Ce programme permet le passage d'une valeur k en argument de la ligne de commande.
	 */
	public static void main(String[] args) throws Exception {
		// Borne 'k' du topk
		int k = 10;

		try {
			// Passage du k en argument ?
			if (args.length > 0) {
				k = Integer.parseInt(args[0]);

				// On contraint k à valoir au moins 1
				if (k <= 0) {
					LOG.warning("k must be at least 1, " + k + " given");
					k = 1;
				}
			}
		} catch (NumberFormatException e) {
			LOG.severe("Error for the k argument: " + e.getMessage());
			System.exit(1);
		}

		Configuration conf = new Configuration();
		conf.setInt("k", k);
		conf.set("fs.file.impl", "com.conga.services.hadoop.patch.HADOOP_7682.WinLocalFileSystem");
		
		Job job = new Job(conf, "wordcount");

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