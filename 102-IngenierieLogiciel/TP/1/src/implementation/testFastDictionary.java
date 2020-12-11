package implementation;
import dico.AbstractDictionary;

public class testFastDictionary {
	public static void main(String[] args) {
		AbstractDictionary d1 = new FastDictionary(10);
		System.out.println(d1); // should print total = 10 real = 0
		d1.put("probity", "integrity and uprightness; honesty.");
		d1.put("mellifluous", "flowing with honey; sweetened with or as if with honey.");
		d1.put("festinate", "to hurry; hasten.");
		d1.put("quondam", "former; onetime: his quondam partner.");
		d1.put("spang", "directly, exactly.");
		d1.put("blatherskite", "a person given to voluble, empty talk.");
		d1.put("lapidify", "to turn into stone.");
		d1.put("threequel", "the third in a series of literary works, movies, etc.;");
		d1.put("ineffable", "incapable of being expressed or described in words; inexpressible.");
		//should grow because nbElements is over 3/4*size
		System.out.println(d1);
		d1.put("vale", "the world, or mortal or earthly life: this vale of tears.");
		d1.put("somnambulism", "sleepwalking.");
		d1.put("doddle", "something easily done, fixed, etc.");
		// try to put something already existing, nothing should change
		d1.put("probity", "integrity and uprightness; honesty.");
		// mixing stuff
		d1.put(660066, 561600);
		d1.put(769435, 727849);
		AbstractDictionary d2, d3, d4;
		d2 = new OrderedDictionary();
		d3 = new FastDictionary(1);
		d4 = new OrderedDictionary(1);
		d1.put(d2, d2.hashCode());
		d1.put(d3, d3.hashCode());
		d1.put(d4, d4.hashCode());
		System.out.println(d1);
		int n = 8;
		System.out.println("the key of ' " + d1.keyArray[n] + "' is assocatied to the value '" + d1.get(d1.keyArray[n]) + "'");

	}
}