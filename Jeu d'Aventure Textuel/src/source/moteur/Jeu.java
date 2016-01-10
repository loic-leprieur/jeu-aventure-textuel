package source.moteur;

import javafx.application.Application;
import javafx.stage.Stage;
import source.moteur.grahique.PrincipalFrame;
import source.moteur.analyseur.analyse.Analyseur;
import source.moteur.analyseur.dictionnaire.Complement;
import source.moteur.analyseur.dictionnaire.Dictionnaire;
import source.moteur.analyseur.dictionnaire.Mot;
import source.moteur.analyseur.dictionnaire.Verbe;

public class Jeu extends Application{
    public static void main(String[] args){
        // instancier IG
        // ajouter un controleur dans le formulaire de l'IG

        // analyse de la phrase du formulaire
        Mot[] mots = {new Verbe("manger"), new Complement("pomme")};
        Dictionnaire dico = new Dictionnaire(mots);
        Analyseur analyseur = new Analyseur(dico, "manger pomme");
        analyseur.analyserPhrase();
        // màj IG

        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setUserAgentStylesheet(STYLESHEET_MODENA);
        new PrincipalFrame();
    }
}