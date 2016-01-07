package dictionnaire;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Laura
 * classe contenant la map des synonymes 
 */

public class Dictionnaire {
	// ensemble des mots interprétés 
	private HashMap<Mot, List<String>> synonymes;
	
	public Dictionnaire(HashMap<Mot, List<String>> dico){
		this.synonymes = dico;
	}
	

	public void ajouterNouvelleAction(Mot action, List<String> lSyn){
		this.synonymes.put(action, lSyn); 
	}
	
	/**
	 * cherche le verbe associé à un synonyme donné.
	 * Si une des actions (clé de la map de type verbe)
	 * contient ce synonyme alors le mot est retourné.
	 * 
	 * @param synonymeAaction
	 * @return le verbe associé au synonyme
	 * 
	 * TODO : lever une exception si le verbe n'existe pas
	 */
	public Mot getAction(String synonymeAaction){
		Mot verbeTrouve = new Verbe("");
		for(Mot cle : synonymes.keySet()){
			for(String valeur : synonymes.get(cle)){
				if(valeur.equalsIgnoreCase(synonymeAaction) && cle.getType() == "verbe"){
					verbeTrouve = cle;
				}
			}
		}
		return verbeTrouve;
	}
	
	/**
	 * retourne la liste de tous les
	 * synonymes des verbes existant
	 * @return List<String>
	 */
	public List<String> getActionsPossibles() {
		List<String> tousLesVerbesPossibles = null;
		for (Mot mot : synonymes.keySet()) {
			for (String syn : synonymes.get(mot)) {
				if(mot.type == "verbe")
					tousLesVerbesPossibles.add(syn);
;			}
		}
		return tousLesVerbesPossibles;
	}

	public void setActionsPossibles(HashMap< Mot, List<String> > syn) {
		this.synonymes = syn;
	}
}
