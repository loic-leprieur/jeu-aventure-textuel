/**
 *
 */
package source.moteur.analyseur.analyse;


import source.moteur.analyseur.Phrase;
import source.moteur.analyseur.dictionnaire.Dictionnaire;
import source.moteur.analyseur.dictionnaire.Mot;
import source.moteur.analyseur.dictionnaire.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Analyseur de phrases en langage naturel.
 * <p>
 * Étapes :
 * <ol>
 *     <li>découpage du texte en commandes ("." ";" "puis" "ensuite", "et" suivi d'un verbe)</li>
 *     <li>normalisation (accents, apostrophes, tirets, contractions "au", "du"...)</li>
 *     <li>reconnaissance des mots du dictionnaire (expressions de plusieurs mots,
 *     formes conjuguées, pluriels, fautes de frappe)</li>
 *     <li>les mots vides (articles, pronoms, politesse) sont ignorés</li>
 * </ol>
 * Exemples : "Je voudrais prendre la clé puis ouvrir la porte avec",
 * "prends la pomme et la frite", "s'il te plaît, va au nord".
 *
 * @author Laura
 *
 */
public class Analyseur {

	/** Mots ignorés s'ils ne sont pas dans le dictionnaire */
	private static final Set<String> MOTS_VIDES = Set.of(
			// articles et déterminants
			"le", "la", "les", "l", "un", "une", "des", "de", "d", "a",
			"ce", "cet", "cette", "ces", "mon", "ma", "mes", "ton", "ta", "tes",
			"son", "sa", "ses", "notre", "votre", "nos", "vos", "leur", "leurs",
			// pronoms
			"je", "j", "tu", "il", "elle", "on", "nous", "vous", "moi", "toi",
			"me", "m", "te", "t", "se", "s", "y", "en", "lui",
			// modaux et politesse
			"veux", "voudrais", "veut", "voulons", "voulez", "aimerais",
			"peux", "pourrais", "peut", "pouvez", "pourriez", "dois", "vais", "faut",
			"essaie", "essaye", "essayer", "essayons", "essayez",
			"svp", "stp", "plait", "merci", "alors", "donc", "bon", "maintenant", "vite",
			// fin de négation
			"pas", "jamais");

	private static final Set<String> NEGATIONS = Set.of("ne", "n");

	private static final String COORDINATION = "et";

	private Dictionnaire dico;
	/**
	 * constructeur
	 */
	public Analyseur(Dictionnaire dico){
		this.dico = dico;
	}

	/**
	 * Analyse un texte pouvant contenir plusieurs commandes
	 * @param texte Texte saisi par le joueur
	 * @return Liste des phrases reconnues, dans l'ordre (vide si le texte est vide)
	 */
	public List<Phrase> analyser(String texte){
		List<Phrase> res = new ArrayList<>();
		for(String segment : decouperCommandes(texte)){
			List<String> mots = developperContractions(Normaliseur.decouper(segment));
			if(!mots.isEmpty()){
				res.addAll(analyserMots(mots));
			}
		}
		return res;
	}

	/**
	 * methode qui va analyser la phrase et faire les appels sur dictionnaire qu il faut
	 * @return Première commande du texte
	 */
	public  Phrase analyserPhrase(String aAnalyser){
		List<Phrase> phrases = analyser(aAnalyser);
		return phrases.isEmpty() ? Phrase.incomprise("Tu n'as rien dit.") : phrases.get(0);
	}

	/**
	 * Découpe le texte sur la ponctuation forte et les mots "puis" / "ensuite".
	 * La virgule est traitée comme un "et" : "prends la clé, la feuille".
	 */
	private List<String> decouperCommandes(String texte){
		String t = Normaliseur.sansAccents(texte).replace(",", " et ");
		List<String> res = new ArrayList<>();
		for(String segment : t.split("[.;!?:]+|\\b(?:et\\s+)?(?:puis|ensuite)\\b")){
			if(!segment.isBlank()){
				res.add(segment);
			}
		}
		return res;
	}

	/**
	 * "au" -> "a le", "aux" -> "a les", "du" -> "de le"
	 */
	private List<String> developperContractions(List<String> mots){
		List<String> res = new ArrayList<>();
		for(String mot : mots){
			switch (mot){
				case "au" -> { res.add("a"); res.add("le"); }
				case "aux" -> { res.add("a"); res.add("les"); }
				case "du" -> { res.add("de"); res.add("le"); }
				default -> res.add(mot);
			}
		}
		return res;
	}

	/**
	 * Analyse une commande déjà découpée en mots.
	 * Grammaire : verbe [complément] [préposition complément] ("et" complément | "et" verbe ...)*
	 */
	private List<Phrase> analyserMots(List<String> mots){
		List<Phrase> res = new ArrayList<>();
		PhraseEnCours courante = new PhraseEnCours();
		int i = 0;
		while(i < mots.size()){
			String mot = mots.get(i);

			if(mot.equals(COORDINATION)){
				int suivant = sauterMotsVides(mots, i + 1);
				if(suivant < mots.size() && trouver(Type.verbe, mots, suivant, false) != null){
					// "prends la clé et ouvre la porte" : nouvelle commande
					res.add(courante.construire());
					courante = new PhraseEnCours();
				}else if(suivant < mots.size() && courante.verbe != null && courante.complement != null
						&& trouver(Type.complement, mots, suivant, false) != null){
					// "prends la clé et la feuille" : même verbe, autre complément
					res.add(courante.construire());
					PhraseEnCours nouvelle = new PhraseEnCours();
					nouvelle.verbe = courante.verbe;
					nouvelle.negative = courante.negative;
					courante = nouvelle;
				}
				i = suivant;
				continue;
			}

			if(NEGATIONS.contains(mot) && !estDansDictionnaire(mots, i)){
				courante.negative = true;
				i++;
				continue;
			}

			Correspondance c = reconnaitre(courante, mots, i, false);
			if(c == null && !MOTS_VIDES.contains(mot)){
				c = reconnaitre(courante, mots, i, true);
			}
			if(c != null){
				courante.ajouter(c.mot());
				i += c.longueur();
			}else{
				if(!MOTS_VIDES.contains(mot)){
					courante.inconnus.add(mot);
				}
				i++;
			}
		}
		if(!courante.estVide()){
			res.add(courante.construire());
		}
		return res;
	}

	/**
	 * Cherche le mot attendu à la position i : un verbe tant qu'il n'y en a pas,
	 * puis un complément ou une préposition
	 */
	private Correspondance reconnaitre(PhraseEnCours courante, List<String> mots, int i, boolean tolererFautes){
		Correspondance c = null;
		if(courante.verbe == null){
			c = trouver(Type.verbe, mots, i, tolererFautes);
		}
		if(c == null){
			c = trouver(Type.complement, mots, i, tolererFautes);
		}
		if(c == null){
			c = trouver(Type.preposition, mots, i, tolererFautes);
		}
		return c;
	}

	/**
	 * Recherche la plus longue expression du type t commençant à la position debut
	 */
	private Correspondance trouver(Type t, List<String> mots, int debut, boolean tolererFautes){
		int max = Math.min(dico.getLongueurMaxExpression(), mots.size() - debut);
		for(int longueur = max; longueur >= 1; longueur--){
			String expression = String.join(" ", mots.subList(debut, debut + longueur));
			Mot m = dico.rechercher(t, expression, tolererFautes && longueur == 1);
			if(m != null){
				return new Correspondance(m, longueur);
			}
		}
		return null;
	}

	private boolean estDansDictionnaire(List<String> mots, int i){
		for(Type t : Type.values()){
			if(trouver(t, mots, i, false) != null){
				return true;
			}
		}
		return false;
	}

	private int sauterMotsVides(List<String> mots, int i){
		while(i < mots.size() && MOTS_VIDES.contains(mots.get(i)) && !estDansDictionnaire(mots, i)){
			i++;
		}
		return i;
	}

	private record Correspondance(Mot mot, int longueur) {
	}

	/**
	 * Phrase en cours de construction
	 */
	private static class PhraseEnCours {
		private String verbe;
		private String complement;
		private String preposition;
		private String complementSecondaire;
		private boolean negative;
		private final List<String> inconnus = new ArrayList<>();

		void ajouter(Mot mot){
			switch (Type.valueOf(mot.getType())){
				case verbe -> verbe = mot.getLibelle();
				case preposition -> {
					if(preposition == null){
						preposition = mot.getLibelle();
					}else{
						inconnus.add(mot.getLibelle());
					}
				}
				case complement -> {
					if(complement == null){
						complement = mot.getLibelle();
					}else if(complementSecondaire == null){
						complementSecondaire = mot.getLibelle();
					}else{
						inconnus.add(mot.getLibelle());
					}
				}
			}
		}

		boolean estVide(){
			return verbe == null && complement == null && preposition == null && inconnus.isEmpty();
		}

		Phrase construire(){
			if(!inconnus.isEmpty()){
				String liste = String.join("\", \"", inconnus);
				return Phrase.incomprise(inconnus.size() == 1
						? "Je ne comprends pas le mot \"" + liste + "\"."
						: "Je ne comprends pas les mots \"" + liste + "\".");
			}
			if(verbe == null){
				return Phrase.incomprise(complement != null
						? "Que veux-tu faire avec " + complement + " ?"
						: "Je n'ai pas compris ce que tu veux faire.");
			}
			return new Phrase(verbe, complement, preposition, complementSecondaire, negative, null);
		}
	}

}
