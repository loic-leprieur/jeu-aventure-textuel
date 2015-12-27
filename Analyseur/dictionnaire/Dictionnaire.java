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
	private Map<String, List<String>> synonymes;
	
	public Dictionnaire(){
		this.synonymes = new HashMap<String, List<String>>();
	}
	

	public void ajouterNouvelleAction(String action, List<String> synonymes){
		((Map<String, List<String>>) synonymes).put(action, synonymes); 
	}
	
	/**
	 * cherche l'action associée à un synonyme donnée. 
	 * Si une des actions (clé de la map)
	 * contient ce synonyme alors l'action est retournée.
	 * 
	 * 
	 * @param actionRentree
	 * @return action recherché si trouvée
	 */
	public String getAction(String actionRentree){
		String res = "";
		
		for(String clé : synonymes.keySet()){
			for(String valeur : synonymes.get(clé)){
				if(valeur.equalsIgnoreCase(actionRentree)){
					res = clé;
				}
			}
		}
		return res;
	}
	
	public Map<String, List<String>> getActionsPossibles() {
		return synonymes;
	}

	public void setActionsPossibles(Map< String, List<String> > syn) {
		this.synonymes = syn;
	}
}
