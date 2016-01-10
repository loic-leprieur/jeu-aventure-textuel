package source.moteur.regles;

import source.moteur.analyseur.dictionnaire.Mot;


public class Regle {

    public static String manger(String nom){
        String res = "";
        if(nom.equals("pomme")){
            //TODO:Action a effectuer appel aux regles
            res = "Miam cette pomme est délicieuse";
        }
        return res;
    }
}
