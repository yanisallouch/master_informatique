package mockito;

import java.util.Random;

public class Dice {
	private final int nbFaces;
	private Random randomGen;
	public Dice(int nbFaces, Random randomGen) {
		super();
		this.nbFaces = nbFaces;
		this.randomGen = randomGen;
	}
	
	public int roll() throws DiceException {
		int result = randomGen.nextInt(nbFaces) + 1; // 0 is not a possible value but nbFaces is.
		if(result > nbFaces || result <= 0) {
			throw new DiceException("Incorrect value");
		}
		return result;
	}
	// assertThrows(DiceExecption.class, ()->{dice.roll();});
}
