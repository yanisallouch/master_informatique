package jeu.de.lettre.product;

public interface IGestPhraseMystere {
	public double getPourcentageLettresCodes();
	public void setPourcentageLettresCodes(double pourcentageLettresCodes);
	public IAlphabet getAlphabetEncode();
	public void setAlphabetEncode(IAlphabet alphabetEncode);
	public String getMotMystere();
	public void setMotMystere(String motMystere);
	public boolean estToujoursMystere();
	public void initMotMystere();
	public boolean contientLaLettre(String lettre);
	public void demasquerLettre(String lettre);
	public int resteADecouvrir();
	public void startGestion();
}