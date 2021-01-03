package question8hash;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class GroupByLieuEtDate1 {
	private static final String INPUT_PATH = "input-productions/";
	private static final String OUTPUT_PATH = "input-output-8hash-1/";
	private static final Logger LOG = Logger.getLogger(GroupByLieuEtDate1.class.getName());

	/*
	 * Ce bloc initialise le logger 'LOG'. Celui-ci permet d'afficher des messages dans la console, le classique System.out.print() ne fonctionnant pas dans le e d'exécution normal de Hadoop.
	 * En plus d'être affiché dans la console, les messages seront aussi présents dans le fichier 'out.log' qui apparaitra à la racine du projet.
	 */
	static {
		/*
		 * La chaine %5$s%n%6$s permet de spécifier au logger le format de la sortie qu'il renverra.
		 * '%' est le caractère spécifiant qu'une substitution va avoir lieu à son emplacement, les caractères suivants définissent le type de la substitution.
		 * '5$' dénote que la valeur du 5ème argument de l'objet interne représentant le message de log sera récupéré et utilisé pour la substitution. Cette valeur correspond à la String du message.
		 * 's' spécifie que la valeur sera formattée comme une chaine de caractère (String).
		 * '%n' représente un saut de ligne.
		 * '%6$s' affiche la pile d'exécution en cas d'exception.
		 */
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
		private final static IntWritable one = new IntWritable(1);
		private final static String emptyWords[] = { "" };

		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();

			String[] columns= line.split(",");

			// La ligne est vide : on s'arrête
			if (Arrays.equals(columns, emptyWords) || !columns[6].equals("livraison") )
				return;
			
			Text lieuDate = new Text(columns[1]+"|"+columns[4]);
			context.write(lieuDate, one);

		}
	}

	public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {

		public void reduce(Text key, Iterable<IntWritable> values, Context context)
				throws IOException, InterruptedException {
			int sum = 0;
			
			for (IntWritable val : values){
				sum ++;
			}
			
			context.write(key, new IntWritable(sum));
		}
	}

	public static void main(String[] args) throws Exception {
		FileUtil.fullyDelete(new File(OUTPUT_PATH));
		
		Configuration conf = new Configuration();
		conf.set("mapred.textoutputformat.separator", "|");

		Job job = new Job(conf, "wordcount");
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(INPUT_PATH));
		
		/*
		 * Génère un nouveau dossier de sortie à chaque exécution du programme.
		 * Cette stratégie est utilisée dans le cadre du TP car pour Hadoop, si le dossier de sortie existe déjà lors d'une exécution, celui-ci renvoie une erreur.
		 * Le fait de conserver les sorties précédentes permet, éventuellement, de comparer les nouvelles sorties lors de l'écriture du programme.
		 * Il conviendra de supprimer de temps en temps les trop vieux dossiers de sortie.
		 */
		FileOutputFormat.setOutputPath(job, new Path(OUTPUT_PATH));

		job.waitForCompletion(true);
	}
}