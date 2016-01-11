/**
 * 
 */
package source.moteur.analyseur.analyse;


import source.moteur.analyseur.Phrase;
import source.moteur.analyseur.dictionnaire.Dictionnaire;
import source.moteur.regles.Regle;

/**
 * @author Laura
 *
 */
public class Analyseur {

	private static Dictionnaire dico;
	private static String aAnalyser;
	/**
	 * constructeur 
	 */
	public Analyseur(Dictionnaire dico, String ph){
		this.dico = dico;
		aAnalyser = ph;
	}

	/**
	 * methode qui va analyser la phrase et faire les appels sur dictionnaire qu il faut 
	 */
	public static Phrase analyserPhrase(){
		String[] tab = aAnalyser.split(" ");
		Phrase phrase = null;
		//nb : pour ce qui suit on a besoin d'une decomposition du dico

		switch(tab.length){
		//note sur les longueurs : correspond aux regles de syntaxe
		//première itération : juste nom + verbe

		case 2:
			//le premier est un verbe le deuxieme est un nom 
			//comparaison du premier mot avec les VERBES du dico etc 
			//si non present : phrase non comprise
			//if(comparerVerbe(tab[0])!=null && comparerNom(tab[1])!=null){
			phrase = new Phrase(tab[0],tab[1]);
			//}else{
				//faire en sorte dafficher message d erreur
				//System.out.println("Mots inconnus");
			//}
			break;
		case 3: 
			//le premier est un verbe, le deuxieme est une preposition, le troisieme est un nom 
			//comparaison du premier mot avec les VERBES du dico etc 
			//si non present : phrase non comprise
			if(comparerVerbe(tab[0])!=null && comparerPrepo(tab[1])!=null && comparerNom(tab[2])!=null ){

			}else{
				//faire en sorte dafficher message d erreur 
			}

			break;
		case 4:
			//le premier est un verbe, le deuxieme est un nom, le troisieme une preposition, le quatrieme un nom
			//comparaison du premier mot avec les VERBES du dico etc 
			//si non present : phrase non comprise
			if(comparerVerbe(tab[0])!=null && comparerNom(tab[1])!=null && comparerPrepo(tab[2])!=null && comparerNom(tab[3])!=null ){

			}else{
				//faire en sorte dafficher message d erreur 
			}

			break;

		default :
			//leve le cas de phrase incorrecte
			phrase = new Phrase("null","null");
			break;
		}
		return phrase;
	}

	//TODO : implémenter, voir pour reagencer sous la forme d'une classe abstraite comparaison 
	/**
	 * méthode pour comparer les verbes avec le dictionnaire 
	 */
	public static String comparerVerbe(String s){
		String res = null;
		if(dico.getVerbe().getLibelle() == s){
			res = s;
		}
		return res;
	}

	/**
	 * méthode pour comparer les prepositions avec le dictionnaire 
	 */
	public static String comparerPrepo(String s){
		String res = null;
		//si on trouve on met le mot 
		return res;

	}


	/**
	 * méthode pour comparer les noms avec le dictionnaire
	 */
	public static String comparerNom(String s){
		String res = null;
		if(dico.getNom().getLibelle() == s){
			res = s;
		}
		return res;

	}
}