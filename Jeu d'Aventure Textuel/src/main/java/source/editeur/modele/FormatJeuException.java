package source.editeur.modele;

/**
 * Erreur de lecture d'un fichier de jeu
 */
public class FormatJeuException extends Exception {

    private final int ligne;

    public FormatJeuException(String message, int ligne) {
        super(ligne > 0 ? "Ligne " + ligne + " : " + message : message);
        this.ligne = ligne;
    }

    /**
     * @return Numéro de la ligne en erreur (0 si inconnue)
     */
    public int getLigne() {
        return ligne;
    }
}
