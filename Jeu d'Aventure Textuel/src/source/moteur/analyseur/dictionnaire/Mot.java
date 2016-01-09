package source.moteur.analyseur.dictionnaire;

public abstract class Mot {
	// nom du mot
	protected String libelle;
	// type du mot = verbe, préposition ou complément
	protected String type;
	
	/**
	 * un mot est une chaine de
	 * caractères appartenant à un des 
	 * types verbe, préposition ou complément.
	 * L'analyseur utilise ces types pour
	 * trouver la méthode à exécuter dans la 
	 * phrase rentrée par l'utilisateur
	 * @param nom
	 */
	public Mot(String nom){
		this.libelle = nom;
	}
	
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	
	/**
	 * retourne le type du mot
	 * (verbe, préposition, complément)
	 */
	public abstract String getType();
	
}
