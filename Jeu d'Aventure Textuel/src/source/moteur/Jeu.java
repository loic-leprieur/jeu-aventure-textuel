package source.moteur;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import source.Niveau;
import source.Objet;
import source.Salle;
import source.moteur.grahique.PrincipalFrame;
import source.moteur.analyseur.analyse.Analyseur;

import java.util.ArrayList;
import java.util.HashMap;

public class Jeu extends Application{

    private Niveau niveau;

    public static void main(String[] args){
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setUserAgentStylesheet(STYLESHEET_MODENA);

        Objet obj1 = new Objet("clé","une clé",new Image("source/editeur/images/objets/obj_cle.png"));
        ArrayList<Objet> objs = new ArrayList<>();
        objs.add(obj1);
        HashMap<Direction,Salle> map = new HashMap<>();
        Salle s1 = new Salle("salle1","descsalle1",new Image("source/editeur/images/salles/sal_bureau.jpg"),objs,map);
        ArrayList<Salle> salles = new ArrayList<>();
        salles.add(s1);
        niveau = new Niveau("niv1",salles);

        new PrincipalFrame(niveau);


    }
}