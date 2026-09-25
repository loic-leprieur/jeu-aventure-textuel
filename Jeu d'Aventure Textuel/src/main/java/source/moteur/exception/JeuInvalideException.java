package source.moteur.exception;

/**
 * Exception levée si un jeu ne peut pas être lancé (ex : aucune salle)
 */
public class JeuInvalideException extends Exception {
    public JeuInvalideException(String e){
        super(e);
    }
}
