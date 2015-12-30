/**
 * 
 */
package analyse;

import dictionnaire.*;

/**
 * @author Laura
 *
 */
public class Analyseur {

	private Dictionnaire dico;
	private static String aAnalyser;
	/**
	 * constructeur 
	 */
	public Analyseur(Dictionnaire dico, String ph){
		this.dico = dico;
		aAnalyser = ph;

	}

	/**
	 * methode qui va analyser la phrase et faire les appels � dictionnaire qu il faut 
	 */
	public static void analyserPhrase(){
		//TODO pas assez d'éléments pour finir vraiment 
		String[] tab = aAnalyser.split(" ");
		//nb : pour ce qui suit on a besoin d'une d�composition du dico 

		//note sur les longueurs : correspond aux regles de syntaxe
		if(tab.length==2){
			//le premier est un verbe le deuxieme est un nom 
			//comparaison du premier mot avec les VERBES du dico etc 
			//si non pr�sent : phrase non comprise
			if(comparerVerbe(tab[0]) && comparerNom(tab[1])){

			}else{
				//faire en sorte dafficher message d erreur 
			}
		}
		if(tab.length==3){
			//le prmier est un verbe, le deuxieme est une preposition, le troisieme est un nom 
			//comparaison du premier mot avec les VERBES du dico etc 
			//si non pr�sent : phrase non comprise
			if(comparerVerbe(tab[0]) && comparerPrepo(tab[1]) && comparerNom(tab[2]) ){

			}else{
				//faire en sorte dafficher message d erreur 
			}

		}
		if(tab.length==4){
			//le premier est un verbe, le deuxieme est un nom, le troisieme une preposition, le quatrieme un nom
			//comparaison du premier mot avec les VERBES du dico etc 
			//si non pr�sent : phrase non comprise
			if(comparerVerbe(tab[0]) && comparerNom(tab[1]) && comparerPrepo(tab[2]) && comparerNom(tab[3]) ){

			}else{
				//faire en sorte dafficher message d erreur 
			}

		}
		if(tab.length<=1 || tab.length>=5){
			//leve le cas de phrase incorrecte 
		}
	}

	//TODO : implémenter, voir pour réagencer sous la forme d'une classe abstraite comparaison 
	/**
	 * méthode pour comparer les verbes avec le dictionnaire 
	 */
	public static boolean comparerVerbe(String s){
		boolean res = false;
		//si on trouve on met vrai 
		return res;

	}

	/**
	 * méthode pour comparer les prepositions avec le dictionnaire 
	 */
	public static boolean comparerPrepo(String s){
		boolean res = false;
		//si on trouve on met vrai 
		return res;

	}


	/**
	 * méthode pour comparer les nom avec le dictionnaire 
	 */
	public static boolean comparerNom(String s){
		boolean res = false;
		//si on trouve on met vrai 
		return res;

	}
}
