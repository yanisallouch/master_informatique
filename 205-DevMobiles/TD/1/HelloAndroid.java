import android.widget.TextView;
public class HelloAndroid extends Activity {

	@Override
	public void onCreate(Bundle savedinstanceState) {
		super.onCreate(savedinstanceState);

		LinearLayout layout = new LinearLayout(this);
		TextView tv = new TextView/(this);
		tv.setText("Hello, Android");
		setContentView(tv);
	}
}
