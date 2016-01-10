package source.moteur.exception;

/**
 * Exception levée si un objet n'est pas dans la salle
 */
public class ObjetPasDansSalleException extends Exception {
    public ObjetPasDansSalleException(String e){
        super(e);
    }
}
