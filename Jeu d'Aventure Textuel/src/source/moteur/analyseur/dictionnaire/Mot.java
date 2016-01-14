package source.moteur.analyseur.dictionnaire;

public abstract class Mot {
	// nom du mot
	protected String libelle;
	// type du mot = verbe, préposition ou complément
	protected Type type;
	
	/**
	 * un mot est une chaine de
	 * caractères appartenant à un des 
	 * types verbe, préposition ou complément.
	 * L'analyseur utilise ces types pour
	 * trouver la méthode à exécuter dans la 
	 * phrase rentrée par l'utilisateur
	 * @param nom Nom
	 * @param t Type du mot
	 */
	public Mot(String nom,Type t){
		this.libelle = nom;
		this.type = t;
	}
	
	public String getLibelle() {
		return libelle;
	}

	/**
	 * retourne le type du mot
	 * (verbe, préposition, complément)
	 */
	public String getType(){
		return this.type.toString();
	}
	
}
