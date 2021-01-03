package question8hash;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;


public class HashJoinContinentSemaine2et3et4 {
	private static final String INPUT_PATH = "input-output-8hash-1/";
	private static final String OUTPUT_PATH = "input-output-8hash-3/";
	private static final Logger LOG = Logger.getLogger(HashJoinContinentSemaine2et3et4.class.getName());
	


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
		
		private int k1;
		private int k2;
		
		public void setup(Context context) {
			k1 = context.getConfiguration().getInt("k1", 4);
			k2 = context.getConfiguration().getInt("k2", 4);
			
		}

		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString().trim();
			String[] columns = line.split("\\|");
			if (columns.length==1) columns = line.split(",");
			
			// La ligne es vide ou est la premiere de Lieux ou Dates : on s'arrête
			if (Arrays.equals(columns, emptyWords) || columns[0].equals("idDdate") || columns[0].equals("id") ){
				return;
			}
			
			//ligne de Lieux
			if (columns.length == 15) {
				int classeLieu = Integer.parseInt(columns[0]) % k1;
				Text continent = new Text("continent," + columns[0]+ "," +columns[8]+",dud");
				for (int i = 0; i<k2; i++) {
					context.write(new Text(classeLieu+","+i), continent);
				}
				
			}
			
			//ligne de Dates
			else if (columns.length == 19) {
				int classeDate = columns[0].hashCode() % k2;
				Text semaine = new Text("semaine," + columns[0]+"," +columns[4]+ ",dud");
				for (int i=0; i<k1; i++) {
					context.write(new Text(i+","+classeDate), semaine);
				}
				
			}
			
			//ligne de output du job 1
			else if (columns.length == 3) {
				int classeLieu = Integer.parseInt(columns[0]) % k1;
				int classeDate = columns[1].hashCode() % k2;
				int nbLivraisons = Integer.parseInt(columns[2]);
				context.write(new Text(classeLieu+","+ classeDate), new Text(columns[0] + "," + columns[1] + "," + nbLivraisons));
				
				
			}
			
			else {
				System.out.println("Length not matching");
			}
			
			
			
			
			
		}
	}

	public static class Reduce extends Reducer<Text, Text, Text, Text> {

		
		private HashMap<String, String> continents = new HashMap<>();
		private HashMap<String, String> semaines = new HashMap<>();
		
		private ArrayList<Pair<Pair<String,String>,String>> couples = new ArrayList<>();
		@Override
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			
			couples = new ArrayList<>();
			
			for (Text value: values) {
				String[] columns = value.toString().split(",");
				
				//ligne de output du job
				if (columns.length == 3) {
					Pair<String,String> id = new Pair(columns[0], columns[1]);
					// si les valeurs sont déjà présentes on peut les traiter tout de suite
					if (continents.containsKey(id.x) && semaines.containsKey(id.y)) {
						String semaine = semaines.get(id.y);
						String continent = continents.get(id.x);
						context.write(new Text(semaine), new Text(continent +"," + columns[2]));
					}
					//sinon on doit stocker et traiter le couple a la fin
					else {
						couples.add(new Pair(id, columns[2]));
					}
				}
				else if (columns[0].equals("continent")) {
					continents.put(columns[1], columns[2]);
					
				}
				
				else if (columns[0].equals("semaine")) {
					semaines.put(columns[1], columns[2]);
					
				}
				
				else {System.out.println("no match in reduce");}
			}
			
			for (Pair<Pair<String,String>, String> couple : couples) {
				String semaine = semaines.get(couple.x.y);
				String continent = continents.get(couple.x.x);
				String nombre = couple.y;
				context.write(new Text(semaine+","+ continent), new Text(nombre));
			}
	
			
		}

	}

	public static void main(String[] args) throws Exception {
		
		FileUtil.fullyDelete(new File(OUTPUT_PATH));
		
		Configuration conf = new Configuration();
		
		conf.set("mapred.textoutputformat.separator", ",");
		
		Job job = new Job(conf, "Group by");

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);

		job.setOutputValueClass(Text.class); 

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(INPUT_PATH));
		FileOutputFormat.setOutputPath(job, new Path(OUTPUT_PATH));

		job.waitForCompletion(true);
	}
}