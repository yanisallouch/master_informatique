package question1;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
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

class CompositeKey implements WritableComparable<CompositeKey>{
	public String idProduit;
	public String idLieuDepart;
//	public String idlieuarrivee;
//	public String idprestataire;
//	public String iddatedebut;
//	public String iddatefin;

	public CompositeKey() {
		super();
	}

	public CompositeKey(String idProduit, String idLieuDepart
			/*, String idlieuarrivee, String idprestataire,
			String iddatedebut, String iddatefin*/) {
		super();
		this.idProduit = idProduit;
		this.idLieuDepart = idLieuDepart;
//		this.idlieuarrivee = idlieuarrivee;
//		this.idprestataire = idprestataire;
//		this.iddatedebut = iddatedebut;
//		this.iddatefin = iddatefin;
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		this.idProduit =  arg0.readUTF();
		this.idLieuDepart =  arg0.readUTF();
//		this.idlieuarrivee =  arg0.readUTF();
//		this.idprestataire =  arg0.readUTF();
//		this.iddatedebut =  arg0.readUTF();
//		this.iddatefin =  arg0.readUTF();
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		arg0.writeUTF(idProduit);
		arg0.writeUTF(idLieuDepart);
//		arg0.writeUTF(idlieuarrivee);
//		arg0.writeUTF(idprestataire);
//		arg0.writeUTF(iddatedebut);
//		arg0.writeUTF(iddatefin);
	}

	@Override
	public int compareTo(CompositeKey arg0) {
		int compare = idProduit.compareTo(arg0.idProduit);
		if(compare == 0) {
			return idLieuDepart.compareTo(arg0.idLieuDepart);
		}
		return compare;
	}

	public String toString() {
		return this.idProduit + "," + this.idLieuDepart ;
	}
}

// =========================================================================
// MAPPER
// =========================================================================

class Map extends Mapper<LongWritable, Text, CompositeKey, Text> {

	private final static String emptyWords[] = { "" };

	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		String line = value.toString().trim();
		String[] columns = line.split(",");
		CompositeKey cleComposite;
		int valueIndex = -1;

		if (key.equals(new LongWritable(0)) || Arrays.equals(columns, emptyWords)) {
			return;
		}
		if (columns.length  <= 9) {
			if (columns[6].equals("vente") && columns[1].equals("10")) {
				// 10 ==> id d'un lieu géographique
				valueIndex = 8;
				cleComposite = new CompositeKey(
						columns[0],
						columns[1]/*,
						columns[2],
						columns[3],
						columns[4],
						columns[5]*/);
			}
			else {
				return;
			}
		}
		else {
//			System.out.println("Column length is "+ columns.length);
			return;
		}

		context.write(cleComposite, new Text(columns[valueIndex])); //
	}
}

// =========================================================================
// REDUCER
// =========================================================================

class Reduce extends Reducer<CompositeKey, Text, CompositeKey, IntWritable> {

	@Override
	public void reduce(CompositeKey key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {

		ArrayList<Text> coutsProduit = new ArrayList<Text>();
		int sum = 0;

		for (Text val : values){
			coutsProduit.add(val);
		}

		for (Text text : coutsProduit) {
			sum += Integer.parseInt(text.toString());
		}
		context.write(key, new IntWritable(sum));
	}
}

public class GroupByProduits {
	private static final String INPUT_PATH = "input-productions-lieux/"; // dimensions
	private static final String OUTPUT_PATH = "output/productions-lieux-";
	private static final Logger LOG = Logger.getLogger(GroupByProduits.class.getName());

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

		Job job = new Job(conf, "Requete-1-DW");

		job.setOutputKeyClass(CompositeKey.class);
		job.setOutputValueClass(IntWritable.class);

		job.setMapOutputValueClass(Text.class);

		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(INPUT_PATH));
		FileOutputFormat.setOutputPath(job, new Path(OUTPUT_PATH + Instant.now().getEpochSecond()));

		job.waitForCompletion(true);
	}

	// conf.set("fs.file.impl", "com.conga.services.hadoop.patch.HADOOP_7682.WinLocalFileSystem");
}