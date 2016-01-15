/**
 * 
 */
package source.moteur.analyseur.analyse;


import source.moteur.analyseur.Phrase;
import source.moteur.analyseur.dictionnaire.Dictionnaire;
import source.moteur.analyseur.dictionnaire.Type;

/**
 * @author Laura
 *
 */
public class Analyseur {

	private Dictionnaire dico;
	/**
	 * constructeur 
	 */
	public Analyseur(Dictionnaire dico){
		this.dico = dico;
	}

	/**
	 * methode qui va analyser la phrase et faire les appels sur dictionnaire qu il faut 
	 */
	public  Phrase analyserPhrase(String aAnalyser){
		String[] tab = aAnalyser.split(" ");
		Phrase phrase = new Phrase("null","null");
		//nb : pour ce qui suit on a besoin d'une decomposition du dico

		switch(tab.length){
		//note sur les longueurs : correspond aux regles de syntaxe
		//première itération : juste nom + verbe

		case 2:
			//le premier est un verbe le deuxieme est un nom 
			//comparaison du premier mot avec les VERBES du dico etc 
			//si non present : phrase non comprise
			if(dico.estPresent(Type.verbe,tab[0]) && dico.estPresent(Type.complement,tab[1])) {
				phrase = new Phrase(dico.getAction(tab[0],Type.verbe),dico.getAction(tab[1],Type.complement));
			}
			break;
		case 3:
			/*
			le premier est un verbe, le deuxieme est une preposition, le troisieme est un nom
			comparaison du premier mot avec les VERBES du dico etc
			si non present : phrase non comprise
			if(comparerVerbe(tab[0])!=null && comparerPrepo(tab[1])!=null && comparerNom(tab[2])!=null ){

			}
			break;*/
		case 4:
			/*
			le premier est un verbe, le deuxieme est un nom, le troisieme une preposition, le quatrieme un nom
			comparaison du premier mot avec les VERBES du dico etc
			si non present : phrase non comprise
			if(comparerVerbe(tab[0])!=null && comparerNom(tab[1])!=null && comparerPrepo(tab[2])!=null && comparerNom(tab[3])!=null ){

			}

			break;*/
		}
		return phrase;
	}

}