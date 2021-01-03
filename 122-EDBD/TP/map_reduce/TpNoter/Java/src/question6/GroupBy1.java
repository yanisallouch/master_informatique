package question6;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
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

class StockKey implements WritableComparable<StockKey>{
	public Integer anneeDebut;
	public Integer moisDebut;
	public Integer jourDebut;
	public Integer idEntrepot;
	public Integer idProduit;

	public StockKey() {
		super();
	}

	public StockKey(String date, String date2, String date3, String columns, String columns2) {
		super();
		this.anneeDebut = Integer.parseInt(date);
		this.moisDebut = Integer.parseInt(date2);
		this.jourDebut = Integer.parseInt(date3);
		this.idEntrepot = Integer.parseInt(columns);
		this.idProduit = Integer.parseInt(columns2);
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		this.anneeDebut =  arg0.readInt();
		this.moisDebut = arg0.readInt();
		this.jourDebut =  arg0.readInt();
		this.idEntrepot = arg0.readInt();
		this.idProduit =  arg0.readInt();
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		arg0.writeInt(anneeDebut);
		arg0.writeInt(moisDebut);
		arg0.writeInt(jourDebut);
		arg0.writeInt(idEntrepot);
		arg0.writeInt(idProduit);
	}

	@Override
	public int compareTo(StockKey arg0) {
		if (anneeDebut.compareTo(arg0.anneeDebut) != 0){
			return anneeDebut.compareTo(arg0.anneeDebut);
		}
		if (moisDebut.compareTo(arg0.moisDebut) != 0){
			return moisDebut.compareTo(arg0.moisDebut);
		}
		if (jourDebut.compareTo(arg0.jourDebut) != 0){
			return jourDebut.compareTo(arg0.jourDebut);
		}
		if (idEntrepot.compareTo(arg0.idEntrepot) != 0){
			return idEntrepot.compareTo(arg0.idEntrepot);
		}

		return idProduit.compareTo(arg0.idProduit);
	}

	public String toString() {
		return String.join("|", String.format("%02d",anneeDebut), String.format("%02d",moisDebut), String.format("%02d", jourDebut)) + "|" + this.idEntrepot + "|" + this.idProduit + "|";
	}
}

class StockValue implements WritableComparable<StockValue>{
	public Integer stock;
	public Integer qteSortie;

	public StockValue() {
		super();
	}

	public StockValue(String columns, String columns2) {
		super();
		this.stock = Integer.parseInt(columns);
		this.qteSortie = Integer.parseInt(columns2);
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		this.stock =  arg0.readInt();
		this.qteSortie = arg0.readInt();
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		arg0.writeInt(stock);
		arg0.writeInt(qteSortie);
	}

	@Override
	public int compareTo(StockValue arg0) {
		int compare = stock.compareTo(arg0.stock);

		if (qteSortie.compareTo(arg0.qteSortie) != 0){
			return qteSortie.compareTo(arg0.qteSortie);
		}
		return compare;
	}

	public String toString() {
		return stock + "|" + qteSortie ;
	}
}

// =========================================================================
// MAPPER
// =========================================================================

class Map extends Mapper<LongWritable, Text, StockKey, StockValue> {

	private final static String emptyWords[] = { "" };

	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		String line = value.toString().trim();
		String[] columns = line.split(",");
		StockKey cleComposite = null;
		StockValue valComposite = null;

		if (key.equals(new LongWritable(0)) || Arrays.equals(columns, emptyWords)) {
			return;
		}

		String[] date = columns[0].split("-");

		cleComposite = new StockKey(date[0],date[1],date[2],columns[1],	columns[2]);
		valComposite = new StockValue(columns[3], columns[4]);
		context.write(cleComposite, valComposite);
	}
}

// =========================================================================
// REDUCER
// =========================================================================

class Reduce extends Reducer<StockKey, StockValue, StockKey, Text> {

	@Override
	public void reduce(StockKey key, Iterable<StockValue> values, Context context)
			throws IOException, InterruptedException {

		ArrayList<Integer> qteSortie = new ArrayList<>();
		double moyenneQte = 0.0;

		for (StockValue val : values) {
			qteSortie.add(val.qteSortie);
		}


		for (Integer qte : qteSortie) {
			moyenneQte += qte;
		}

		moyenneQte = moyenneQte / qteSortie.size();

		context.write(key, new Text(moyenneQte + "|" + qteSortie.size()));
	}
}

public class GroupBy1 {
	private static final String INPUT_PATH = "input-stocks/";
	private static final String OUTPUT_PATH = "output/input-output-6-1";
	private static final Logger LOG = Logger.getLogger(GroupBy1.class.getName());

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

		Job job = new Job(conf, "GroupBy Produit & Lieux, Pénurie");

		job.setOutputKeyClass(StockKey.class);
		job.setOutputValueClass(Text.class);

		job.setMapOutputValueClass(StockValue.class);

		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(INPUT_PATH));
		FileOutputFormat.setOutputPath(job, new Path(OUTPUT_PATH));

		job.waitForCompletion(true);
	}
}
