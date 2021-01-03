package question7;

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

class Map extends Mapper<LongWritable, Text, IntWritable, IntWritable> {
	private static IntWritable one = new IntWritable(1);
	
	private static String dateVoulue = "2020-12-13";

	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String line = value.toString();

		String[] columns = line.split(",");
		
		if(columns[0].equals("iddatesnapshot") || !columns[0].equals(dateVoulue)) {
			return;
		}
		
		IntWritable idEntrepot = new IntWritable(Integer.parseInt(columns[1]));
		
		context.write(idEntrepot, one);

	}
}

// =========================================================================
// REDUCER
// =========================================================================

class Reduce extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {
	/**
	 * Map avec tri suivant l'ordre naturel de la clé (la clé représentant la fréquence d'un ou plusieurs mots).
	 * Utilisé pour conserver les k mots les plus fréquents.
	 * 
	 * Il associe une fréquence à une liste de mots.
	 */
	private TreeMap<Integer, List<IntWritable>> sortedEntrepots = new TreeMap<>();
	private int nbSortedEntrepots = 0;
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
	public void reduce(IntWritable key, Iterable<IntWritable> values, Context context)
			throws IOException, InterruptedException {
		
		int  sum = 0;
		IntWritable keyCopy = new IntWritable(key.get());
		

		for (IntWritable val: values ) {
			sum++;	
		}
		
		if (sortedEntrepots.containsKey(sum)) {
			sortedEntrepots.get(sum).add(keyCopy);
		}
		else {
			List<IntWritable> entrepots = new ArrayList<>();
			entrepots.add(keyCopy);
			sortedEntrepots.put(sum,entrepots);
			
		}

		// Nombre de lignes enregistrées atteint : on supprime la ligne avec le plus petit profit (la première dans sortedWords)
		if (nbSortedEntrepots == k) {
			int firstKey = sortedEntrepots.firstKey();

			List<IntWritable> entrepots = sortedEntrepots.get(firstKey);
			
			int sizeBefore = entrepots.size();
			
			entrepots.remove(entrepots.size() - 1);
			assert(entrepots.size() == sizeBefore -1);
			if (entrepots.isEmpty())
				sortedEntrepots.remove(firstKey);
		} else
			nbSortedEntrepots++;
	}

	/**
	 * Méthode appelée à la fin de l'étape de reduce.
	 * 
	 * Ici on envoie les mots dans la sortie, triés par ordre descendant.
	 */
	@Override
	public void cleanup(Context context) throws IOException, InterruptedException {
		for (int sum : sortedEntrepots.descendingKeySet()) {
			for (IntWritable entrepot: sortedEntrepots.get(sum))
				context.write(entrepot, new IntWritable(sum));
				}
	}

}

public class PlusDeProduitsEnStock {
	private static final String INPUT_PATH = "input-stocks";
	private static final String OUTPUT_PATH = "output/question7-";
	private static final Logger LOG = Logger.getLogger(PlusDeProduitsEnStock.class.getName());

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

		job.setOutputKeyClass(IntWritable.class);
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