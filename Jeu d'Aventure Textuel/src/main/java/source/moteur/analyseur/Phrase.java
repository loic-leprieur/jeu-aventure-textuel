package source.moteur.analyseur;

/**
 * Phrase crée avec l'analyseur
 */
public class Phrase {

    private String verbe;
    private String complement;

    public Phrase(String verbe,String complement){
        this.verbe = verbe;
        this.complement = complement;
    }

    public String getVerbe() {
        return verbe;
    }

    public String getComplement() {
        return complement;
    }
}
