package source.moteur.analyseur.dictionnaire;

import source.moteur.analyseur.analyse.Normaliseur;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Laura
 * classe contenant la map des synonymes
 */

public class Dictionnaire {

	/** Longueur minimale d'un mot pour accepter une faute de frappe */
	private static final int LONGUEUR_MIN_FAUTE = 4;

	// ensemble des mots interprétés
	private Map<Mot, List<String>> synonymes;
	private List<Mot> mots;

	public Dictionnaire(List<Mot> mots){
		this.synonymes = new HashMap<>();
		this.mots = new ArrayList<>(mots);
	}


	public void ajouterNouvelleAction(Mot action, List<String> lSyn){
		if(!this.mots.contains(action)){
			this.mots.add(action);
		}
		this.synonymes.put(action, new ArrayList<>(lSyn));
	}

	/**
	 * Ajoute un mot et ses synonymes (qui peuvent être des expressions : "jeter un oeil")
	 * @param mot Mot
	 * @param lSyn Synonymes
	 */
	public void ajouterMot(Mot mot, String... lSyn){
		ajouterNouvelleAction(mot, Arrays.asList(lSyn));
	}


	/**
	 * Retourne le libellé du mot correspondant à un synonyme
	 * @param syn Mot ou synonyme
	 * @param t Type du mot
	 * @return Libellé, null si inconnu
	 */
	public String getAction(String syn,Type t){
		Mot mot = rechercher(t, syn, true);
		return mot == null ? null : mot.getLibelle();
	}

	/**
	 * Verifie si un mot est présent
	 * @param t Type du mot
	 * @param mot Mot a vérifier
     * @return Trouvé ou non
     */
	public boolean estPresent(Type t,String mot){
		return rechercher(t, mot, true) != null;
	}

	/**
	 * Recherche le mot correspondant à une expression, en essayant successivement :
	 * la forme exacte (sans tenir compte des accents), la forme conjuguée / au pluriel,
	 * puis, si tolererFautes, une faute de frappe (une lettre de différence).
	 * @param t Type du mot recherché
	 * @param expression Expression saisie (un ou plusieurs mots)
	 * @param tolererFautes Accepter une faute de frappe
	 * @return Mot trouvé, null sinon
	 */
	public Mot rechercher(Type t, String expression, boolean tolererFautes){
		String cible = Normaliseur.normaliser(expression);
		if(cible.isEmpty()){
			return null;
		}
		for(Mot m : getMot(t)){
			for(String forme : getFormes(m)){
				if(forme.equals(cible)){
					return m;
				}
			}
		}
		if(cible.contains(" ")){
			return null;
		}
		for(Mot m : getMot(t)){
			for(String forme : getFormes(m)){
				if(!forme.contains(" ") && memeMot(t, forme, cible)){
					return m;
				}
			}
		}
		if(tolererFautes && cible.length() >= LONGUEUR_MIN_FAUTE){
			for(Mot m : getMot(t)){
				for(String forme : getFormes(m)){
					if(!forme.contains(" ") && Normaliseur.distance(forme, cible) <= 1){
						return m;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Nombre maximal de mots d'une expression du dictionnaire ("porte d'entrée" = 3)
	 */
	public int getLongueurMaxExpression(){
		int res = 1;
		for(Mot m : mots){
			for(String forme : getFormes(m)){
				res = Math.max(res, forme.split(" ").length);
			}
		}
		return res;
	}

	private boolean memeMot(Type t, String forme, String cible){
		switch (t){
			case verbe:
				return Normaliseur.memeVerbe(forme, cible);
			case complement:
				return Normaliseur.singulier(forme).equals(Normaliseur.singulier(cible));
			default:
				return false;
		}
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
	 * Récupère le libelle du mot plus la liste des synonymes, normalisés
	 * @param m Mot
	 * @return Liste de String
     */
	private List<String> getFormes(Mot m){
		List<String> res = new ArrayList<>();
		res.add(Normaliseur.normaliser(m.getLibelle()));
		for(String s : synonymes.getOrDefault(m, List.of())){
			res.add(Normaliseur.normaliser(s));
		}
		return res;
	}

}
