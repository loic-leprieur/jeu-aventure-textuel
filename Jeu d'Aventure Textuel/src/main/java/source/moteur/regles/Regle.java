package source.moteur.regles;

import source.moteur.analyseur.Phrase;

public class Regle {

    public static String analyser(Phrase phrase){
        String res = "Phrase Incorrecte";
        switch (phrase.getVerbe()){
            case "manger":
                switch (phrase.getComplement()){
                    case "pomme":
                        res = "Miam cette pomme est délicieuse";
                        break;
                    case "frite":
                        res = "Une frite en moins";
                        break;
                }
                break;
        }

        return res;
    }
}
