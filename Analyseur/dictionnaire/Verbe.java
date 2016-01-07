package dictionnaire;

public class Verbe extends Mot {

	public Verbe(String nom) {
		super(nom);
	}

	@Override
	public String getType() {
		return "verbe";
	}

}
