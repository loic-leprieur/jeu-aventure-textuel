package dictionnaire;

/**
 * un complement est un nom ou
 * un ensemble de noms qui 
 * @author Loic
 *
 */
public class Complement extends Mot {

	public Complement(String nom) {
		super(nom);
	}

	@Override
	public String getType() {
		return "complément";
	}


}
