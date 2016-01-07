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
	
	public Dictionnaire(){
		this.synonymes = new HashMap<Mot, List<String>>();
	}
	

	public void ajouterNouvelleAction(Mot action, List<String> lSyn){
		this.synonymes.put(action, lSyn); 
	}
	
	/**
	 * cherche l'action associée à un synonyme donnée. 
	 * Si une des actions (clé de la map)
	 * contient ce synonyme alors l'action est retournée.
	 * 
	 * @param synonymeAaction
	 * @return le verbe recherché si il existe
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
	
	public Map<Mot, List<String>> getActionsPossibles() {
		return synonymes;
	}

	public void setActionsPossibles(HashMap< Mot, List<String> > syn) {
		this.synonymes = syn;
	}
}
