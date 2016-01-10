package source.moteur.regles;

import source.moteur.analyseur.Phrase;

public class Regle {

    public static String analyser(Phrase phrase){
        String res = "Phrase Incorrecte";
        if(phrase.getVerbe().equals("manger")){
            if(phrase.getComplement().equals("pomme")){
                res = "Miam cette pomme est délicieuse";
            }
        }
        return res;
    }
}
