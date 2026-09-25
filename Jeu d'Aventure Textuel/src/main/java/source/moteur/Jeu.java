package source.moteur;

import javafx.application.Application;
import javafx.stage.Stage;
import source.editeur.composants.Dialogues;
import source.editeur.modele.FormatJeuException;
import source.moteur.grahique.PrincipalFrame;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Lancement du moteur de jeu.
 * Argument facultatif : fichier de jeu (.aventure ou script .txt) ; sans argument, le jeu de démonstration.
 */
public class Jeu extends Application{

    public static void main(String[] args){
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setUserAgentStylesheet(STYLESHEET_MODENA);

        List<String> arguments = getParameters().getRaw();
        ChargeurJeu.JeuCharge jeu = null;
        if(!arguments.isEmpty()){
            File f = new File(arguments.get(0));
            try {
                jeu = ChargeurJeu.ouvrir(f.toPath());
            } catch (FormatJeuException e) {
                Dialogues.erreur(null, "Le fichier « " + f.getName() + " » contient une erreur.", e.getMessage());
            } catch (IOException e) {
                Dialogues.erreur(null, "Impossible d'ouvrir « " + f.getName() + " ».", String.valueOf(e.getMessage()));
            }
        }
        new PrincipalFrame(jeu != null ? jeu : ChargeurJeu.demo());
    }
}
