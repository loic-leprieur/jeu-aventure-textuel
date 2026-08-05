package source.moteur.exception;

/**
 * Exception levée si la salle est inexistante
 */
public class SalleInexistanteException extends Exception {
    public SalleInexistanteException(String e){
        super(e);
    }
}
