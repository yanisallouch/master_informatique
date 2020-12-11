
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.time.Instant;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.hadoop.conf.Configuration;
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

class CompositeKey3 implements WritableComparable<CompositeKey3>{
	
	public String station;
	public String heure;
	
	public CompositeKey3() {
		super();
	}
	
	public CompositeKey3(String d, String cat) {
		station = d;
		heure = cat;
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		station= arg0.readUTF();
		heure = arg0.readUTF();
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		arg0.writeUTF(station);
		arg0.writeUTF(heure);
		
	}

	@Override
	public int compareTo(CompositeKey3 arg0) {
		int dateCompare = station.compareTo(arg0.station);
		if (dateCompare == 0) {
			return heure.compareTo(arg0.heure);
		}
		return dateCompare;
	}
	
	public String toString() {
		return station + "\t" + heure;
	}
	
}

class CompositeValue implements WritableComparable<CompositeValue>{
	
	public int type;
	public int direction;
	
	public CompositeValue() {
		super();
	}
	
	public CompositeValue(int l, int d) {
		type = l;
		direction = d;
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		type = arg0.readInt();
		direction = arg0.readInt();
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		arg0.writeInt(type);
		arg0.writeInt(direction);
		
	}

	@Override
	public int compareTo(CompositeValue arg0) {
		if (type == arg0.type) {
			return (direction<arg0.direction) ? -1: (direction == arg0.direction ? 0: 1);
		}
		return type<arg0.type ? -1: (type == arg0.type ? 0: 1);
	}
	
}



public class GroupByTam3 {
	private static final String INPUT_PATH = "input-tam/";
	private static final String OUTPUT_PATH = "output/tam-";
	private static final Logger LOG = Logger.getLogger(GroupByTam3.class.getName());
	


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

	public static class Map extends Mapper<LongWritable, Text, CompositeKey3, CompositeValue > {
		
		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString().trim();
			String[] columns = line.split(";");
			int type = -1;
			
			if (key.get() == 0|| columns.length != 9) { return; }
			switch (Integer.parseInt(columns[4])) {
			case 1:
			case 2:
			case 3:
			case 4:
				type = 0;
			break;
			default:
				type = 1;
				
			}
			
			CompositeKey3 ckey = new CompositeKey3(columns[3],columns[7].substring(0,2));
			context.write(ckey, new CompositeValue(type, Integer.parseInt(columns[6])));
		}
	}

	public static class Reduce extends Reducer<CompositeKey3, CompositeValue, CompositeKey3, Text> {

		@Override
		public void reduce(CompositeKey3 key, Iterable<CompositeValue> values, Context context)
				throws IOException, InterruptedException {
			
			int cptTramAller = 0;
			int cptTramRetour = 0;
			int cptBusAller = 0;
			int cptBusRetour = 0;
			
			for(CompositeValue ittVal : values){
				if (ittVal.direction == 0) {
					if (ittVal.type == 0) cptTramAller++;
					else cptBusAller++;
				}
				else {
					if (ittVal.type == 0) {
						cptTramRetour++;
					}
					else cptBusRetour++;
				}
			}
			String freqTramAller = cptTramAller < 4 ? "faible" : cptTramAller < 10 ? "moyen" : "fort";
			String freqTramRetour = cptTramRetour < 4 ? "faible" : cptTramRetour < 10 ? "moyen" : "fort";
			String freqBusAller = cptBusAller < 4 ? "faible" : cptBusAller < 10 ? "moyen" : "fort";
			String freqBusRetour = cptBusRetour < 4 ? "faible" : cptBusRetour < 10 ? "moyen" : "fort";
			
			String freqStart = "";
			String freqTot = "";
			if (cptTramAller+cptTramRetour != 0) {
				freqStart = "Fréquence\n";
				freqTot =  freqTot +"trams:\taller=" + freqTramAller+ "\tretour="+freqTramRetour+"\n";
			}
			if (cptBusAller+cptBusRetour != 0) {
				freqStart = "Fréquence\n";
				freqTot =  freqTot +"bus:\taller=" + freqBusAller+ "\tretour="+freqBusRetour+ "\n";
			}
			String freqFinal = freqStart + freqTot;
			
			context.write(key, new Text(freqFinal));
	

		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		
		conf.set("mapred.textoutputformat.separator",  " ");

		
		Job job = new Job(conf, "Group by");

		job.setOutputKeyClass(CompositeKey3.class);
		job.setOutputValueClass(Text.class);
		job.setMapOutputValueClass(CompositeValue.class);

		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(INPUT_PATH));
		FileOutputFormat.setOutputPath(job, new Path(OUTPUT_PATH + Instant.now().getEpochSecond()));

		job.waitForCompletion(true);
	}
}