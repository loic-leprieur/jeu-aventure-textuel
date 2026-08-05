package source.moteur.analyseur.dictionnaire;

import java.util.ArrayList;
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
	private List<Mot> mots;

	public Dictionnaire(List<Mot> mots){
		this.synonymes = new HashMap<>();
		this.mots = mots;
	}


	public void ajouterNouvelleAction(Mot action, List<String> lSyn){
		this.synonymes.put(action, lSyn);
	}


	public String getAction(String syn,Type t){
		String res = null;
		for(Mot mot : this.getMot(t)){
			if(mot.getLibelle().equalsIgnoreCase(syn)){
				res = mot.getLibelle();
				break;
			}
			for(String s : synonymes.get(mot)){
				if(s.equalsIgnoreCase(syn)){
					res = mot.getLibelle();
					break;
				}
			}
		}
		return res;
	}

	/**
	 * Verifie si un mot est présent
	 * @param t Type du mot
	 * @param mot Mot a vérifier
     * @return Trouvé ou non
     */
	public boolean estPresent(Type t,String mot){
		boolean res = false;
		for(Mot m : getMot(t)){
			for(String s : getSynonyme(m)){
				if(s.equalsIgnoreCase(mot)){
					res = true;
					break;
				}
			}
		}
		return res;
	}

	/**
	 * 0btient tout les mot du type t
	 * @param t Type
	 * @return List de mot
     */
	private ArrayList<Mot> getMot(Type t){
		ArrayList<Mot> res = new ArrayList<>();
		for (Mot m : mots){
			if(m.getType().equalsIgnoreCase(t.toString())){
				res.add(m);
			}
		}
		return res;
	}

	/**
	 * Récupère la liste des synonyme plus le libelle du mot
	 * @param m Mot
	 * @return Liste de String
     */
	private List<String> getSynonyme(Mot m){
		List<String> res = synonymes.get(m);
		if(res == null){
			res = new ArrayList<>();
		}
		res.add(m.getLibelle());
		return res;
	}

}
