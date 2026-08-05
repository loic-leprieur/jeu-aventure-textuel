package source.moteur.exception;

/**
 * Exception levée si un objet est déjà présent dans la salle
 */
public class ObjetDejaExistantDansSalleException extends Exception {
    public ObjetDejaExistantDansSalleException(String e){
        super(e);
    }
}
