package question10_HashJoin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
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
		
		private int k;
		
		private final static String emptyWords[] = { "" };
		
		@Override
		public void setup(Context context) {
			k = context.getConfiguration().getInt("k", 1);
		}
		
		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

			String line = value.toString().trim();
			String[] columns = line.split(Pattern.quote("|"));
			//lieux.csv
			String[] columns2 = line.split(",");
			//produits.csv
			String[] partGauche = columns[0].trim().split(",");
			String tmpKey;
			int valueIndex = -1;
			
			if (key.equals(new LongWritable(0)) || Arrays.equals(columns, emptyWords)) {
				return;
			}

			if (columns.length == 2) {
				// output-10-1
				tmpKey = (Integer.parseInt(partGauche[0])%k)+","+(Integer.parseInt(partGauche[1])%k)+","+(Integer.parseInt(partGauche[2])%k);
				context.write(new Text(tmpKey), new Text(line));			
			} else if (columns2.length == 12) {
				// produits
				valueIndex = 2;
				for (int i = 0; i < k; i++) {
					tmpKey = (Integer.parseInt(partGauche[0])%k)+","+(i%k)+","+(Math.abs(partGauche[2].hashCode())%k);
					context.write(new Text(tmpKey), new Text("produits|" + partGauche[valueIndex] + "|" + Integer.parseInt(partGauche[0])));
				}
			} else if (columns.length == 15) {
				// lieux.csv
				valueIndex = 8;
				for (int i = 0; i < k; i++) {
					tmpKey = (i%k)+","+(Integer.parseInt(columns[0])%k)+","+(Math.abs(columns[2].hashCode())%k);
					context.write(new Text(tmpKey), new Text("lieux|" + columns[valueIndex] + "|" + Integer.parseInt(columns[0])));
				}
			}
//			context.write(new Text(partGauche[keyIndex]), (keyIndex==0?new Text(columns[valueIndex] + "|" + columns[keyIndex]):new Text(line)));			
		}
	}

	// =========================================================================
	// REDUCER
	// =========================================================================

	static class Reduce extends Reducer<Text, Text, Text, Text> {

		@Override
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {

			
			HashMap<Integer,String> continents = new HashMap<>();
			HashMap<Integer,String> produits = new HashMap<>();

			HashMap<String, String> faits = new HashMap<>();
			
			for (Text text : values) {
				
				String line = text.toString().trim();
				String[] columns = line.split(Pattern.quote("|"));
				if (columns[0].equals("lieux")) {
					continents.put(Integer.parseInt(columns[2]),columns[1]);
				}else if (columns[0].equals("produits")) {
					produits.put(Integer.parseInt(columns[2]), columns[1]);
				}else if (columns[1].length() == 2){
					String[] columns2 = columns[0].trim().split(",");
					if(faits.containsKey(columns2[1] + "," + columns2[0])) {
						faits.put(columns2[1] + "," + columns2[0], faits.get(columns2[1] + "|" + columns2[0]) + "," + columns[1]);
					}else {
						faits.put(columns2[1] + "," + columns2[0], columns[0] + "," + columns[1]);
					}
				}
			}
			
			if(faits.size() != 0) {
				for (Entry<String, String> fact: faits.entrySet()) {
					String[] factValues = fact.getKey().trim().split(",");
					context.write(new Text(produits.get(factValues[0])), new Text(continents.get(factValues[1]) + "," + fact.getValue()));
				}
			}
		}
	}
	private static final String INPUT_PATH = "output/input-output-hashjoin-10-1"; // dimensions
	private static final String OUTPUT_PATH = "output/input-output-hashjoin-10-2";
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

		int k = 4;

		try {
			// Passage du k en argument ?
			if (args.length > 0) {
				k = Integer.parseInt(args[0]);

				// On contraint k Ã  valoir au moins 1
				if (k <= 0) {
					LOG.warning("k must be at least 1, " + k + " given");
					k = 1;
				}
			}
		} catch (NumberFormatException e) {
			LOG.severe("Error for the k argument: " + e.getMessage());
			System.exit(1);
		}
		
		FileUtil.fullyDelete(new File(OUTPUT_PATH));
		
		Configuration conf = new Configuration();
		conf.setInt("k", k);
		conf.set("fs.file.impl", "com.conga.services.hadoop.patch.HADOOP_7682.WinLocalFileSystem");
		
		Job job = new Job(conf, "Join HashJoin Input-10-1 Lieu");
		
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