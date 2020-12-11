package Top10BusEtTram;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
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



/*
 * Jusqu'à présent nous avons défini nos mappers et reducers comme des classes internes à notre classe principale.
 * Dans des applications réelles de map-reduce cela ne sera généralement pas le cas, les classes seront probablement localisées dans d'autres fichiers.
 * Dans cet exemple, nous avons défini Map et Reduce en dehors de notre classe principale.
 * Il se pose alors le problème du passage du paramètre 'k' dans notre reducer, car il n'est en effet plus possible de déclarer un paramètre k dans notre classe principale qui serait partagé avec ses classes internes ; c'est la que la Configuration du Job entre en jeu.
 */


// =========================================================================
// MAPPER
// =========================================================================

class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
	private static IntWritable one = new IntWritable(1);

	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String line = value.toString();

		String[] columns = line.split(";");
		
		if(columns[0].equals("course")) {
			return;
		}
		
		
		
		context.write(new Text(columns[3]), one);

	}
}

// =========================================================================
// REDUCER
// =========================================================================

class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
	/**
	 * Map avec tri suivant l'ordre naturel de la clé (la clé représentant la fréquence d'un ou plusieurs mots).
	 * Utilisé pour conserver les k mots les plus fréquents.
	 * 
	 * Il associe une fréquence à une liste de mots.
	 */
	private TreeMap<Integer, List<Text>> sortedStops = new TreeMap<>();
	private int nbSortedStops = 0;
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
	public void reduce(Text key, Iterable<IntWritable> values, Context context)
			throws IOException, InterruptedException {
		
		int  sum = 0;
		
		Text keyCopy = new Text(key);
		for (IntWritable val: values ) {
			sum+= 1;
			
		
		}
		
		
		// Profit déjà présent
		if (sortedStops.containsKey(sum)) {
			sortedStops.get(sum).add(keyCopy);
		}
		
		else {
			List<Text> stops = new ArrayList<>();
			stops.add(keyCopy);
			sortedStops.put(sum, stops);
		}

		// Nombre de lignes enregistrées atteint : on supprime la ligne avec le plus petit profit (la première dans sortedWords)
		if (nbSortedStops == k) {
			int firstKey = sortedStops.firstKey();

			List<Text> stops = sortedStops.get(firstKey);
			
			int sizeBefore = stops.size();
			
			stops.remove(stops.size() - 1);
			assert(stops.size() == sizeBefore -1);
			if (stops.isEmpty())
				sortedStops.remove(firstKey);
		} else
			nbSortedStops++;
			

		
		
	}

	/**
	 * Méthode appelée à la fin de l'étape de reduce.
	 * 
	 * Ici on envoie les mots dans la sortie, triés par ordre descendant.
	 */
	@Override
	public void cleanup(Context context) throws IOException, InterruptedException {
		int i = 0;
		for (int d : sortedStops.descendingKeySet()) {
			if (i == k) break;
			for (Text stop: sortedStops.get(d)) {
				if (i == k) break;
				context.write(stop, new IntWritable(d));
				i++;
			}
		}
	}

}

public class Top10BusEtTram {
	private static final String INPUT_PATH = "input-tam";
	private static final String OUTPUT_PATH = "output/Top10BusEtTram-";
	private static final Logger LOG = Logger.getLogger(Top10BusEtTram.class.getName());

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

		Job job = new Job(conf, "Top station bus et tram");

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(INPUT_PATH));
		FileOutputFormat.setOutputPath(job, new Path(OUTPUT_PATH + Instant.now().getEpochSecond()));

		job.waitForCompletion(true);
	}
}