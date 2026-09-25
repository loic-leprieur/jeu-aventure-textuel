package source.moteur.analyseur;

/**
 * Phrase crée avec l'analyseur
 * <p>
 * Structure : verbe [complément] [préposition complément secondaire].
 * <ul>
 *     <li>"ouvre la porte avec la clé" : ouvrir / porte / avec / clé</li>
 *     <li>"va au nord" : aller / nord / à (pas de complément secondaire)</li>
 * </ul>
 */
public class Phrase {

    private String verbe;
    private String complement;
    private String preposition;
    private String complementSecondaire;
    private boolean negative;
    private String erreur;

    public Phrase(String verbe,String complement){
        this(verbe, complement, null, null, false, null);
    }

    public Phrase(String verbe, String complement, String preposition, String complementSecondaire,
                  boolean negative, String erreur){
        this.verbe = verbe;
        this.complement = complement;
        this.preposition = preposition;
        this.complementSecondaire = complementSecondaire;
        this.negative = negative;
        this.erreur = erreur;
    }

    /**
     * Crée une phrase non comprise
     * @param erreur Message expliquant ce qui n'a pas été compris
     * @return Phrase
     */
    public static Phrase incomprise(String erreur){
        return new Phrase(null, null, null, null, false, erreur);
    }

    public String getVerbe() {
        return verbe;
    }

    public String getComplement() {
        return complement;
    }

    public String getPreposition() {
        return preposition;
    }

    public String getComplementSecondaire() {
        return complementSecondaire;
    }

    /**
     * @return Vrai si la phrase est à la forme négative ("ne mange pas la pomme")
     */
    public boolean estNegative() {
        return negative;
    }

    /**
     * @return Vrai si la phrase a été comprise
     */
    public boolean estComprise() {
        return erreur == null;
    }

    /**
     * @return Message expliquant ce qui n'a pas été compris, null si la phrase est comprise
     */
    public String getErreur() {
        return erreur;
    }

    @Override
    public String toString() {
        if (!estComprise()) {
            return "Phrase incomprise : " + erreur;
        }
        StringBuilder sb = new StringBuilder();
        if (negative) {
            sb.append("NE PAS ");
        }
        sb.append(verbe);
        if (complement != null) {
            sb.append(' ').append(complement);
        }
        if (preposition != null) {
            sb.append(" [").append(preposition).append(']');
        }
        if (complementSecondaire != null) {
            sb.append(' ').append(complementSecondaire);
        }
        return sb.toString();
    }
}
