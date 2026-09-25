package source.editeur;

import javafx.application.Application;

/**
 * Point d'entrée de l'éditeur à utiliser depuis un IDE (Run As > Java Application).
 * Le lanceur Java refuse de démarrer directement une classe héritant de
 * javafx.application.Application quand JavaFX est sur le classpath ;
 * cette classe, qui n'en hérite pas, contourne le problème.
 */
public class LanceurEditeur {

    public static void main(String[] args){
        Application.launch(Principal.class, args);
    }
}
