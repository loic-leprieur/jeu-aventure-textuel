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
	private String aAnalyser;
	/**
	 * constructeur 
	 */
	public Analyseur(Dictionnaire dico, String ph){
		this.dico = dico;
		aAnalyser = ph;
		
	}
	
	/**
	 * methode qui va analyser la phrase et faire les appels à dictionnaire qu il faut 
	 */
	public static void analyserPhrase(){
		String[] tab = explode(aAnalyser," ");
		//nb : pour ce qui suit on a besoin d'une décomposition du dico 
		
		if(tab.length==2){
			//le premier est un verbe le deuxieme est un nom 
		}
		if(tab.length==3){
			//le prmier est un verbe, le deuxieme est une preposition, le troisieme est un nom 
		}
		if(tab.length==4){
			//le premier est un verble, le deuxieme est un nom, le troisieme une preposition, le quatrieme un nom
		}
	}

}
