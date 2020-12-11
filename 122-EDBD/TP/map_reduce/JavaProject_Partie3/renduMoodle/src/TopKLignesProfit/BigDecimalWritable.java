package TopKLignesProfit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;

import org.apache.hadoop.io.WritableComparable;


public class BigDecimalWritable implements WritableComparable<BigDecimalWritable> {
	private BigDecimal value;
	
	public BigDecimalWritable() {
		super();
		value = new BigDecimal("0.0");
	}
	
	public BigDecimalWritable(BigDecimal d) {
		super();
		value = d;
	}
	
    @Override
    public void readFields(DataInput dataInput) throws IOException {
//      int n = dataInput.readInt();
//      if (n < 0 || n > 1000) {
//        throw new IllegalArgumentException("Invalid representation for BigDecimal ... length is " + n);
//      }
//      byte[] bytes = new byte[n];
//      dataInput.readFully(bytes);
//      try {
//        value = (BigDecimal) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
//      } catch (ClassNotFoundException e) {
//        throw new RuntimeException("Unable to read serialized BigDecimal value, can't happen", e);
//      }
    	value = new BigDecimal(dataInput.readUTF());
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
//      ByteArrayOutputStream bos = new ByteArrayOutputStream();
//      ObjectOutputStream out = new ObjectOutputStream(bos);
//      out.writeObject(value);
//      out.close();
//      byte[] bytes = bos.toByteArray();
//      dataOutput.writeInt(bytes.length);
//      dataOutput.write(bytes);
    	dataOutput.writeUTF(value.toString());
    }
    

	@Override
	public int compareTo(BigDecimalWritable arg0) {
		return value.compareTo(arg0.get());
	}
	
	public BigDecimal get() {
		return value;
	}
	
	public String toString() {
		return value.toString();
	}
	
	public boolean equals(BigDecimalWritable  arg0) {
		return value.equals(arg0.get());
	}
	
}
