package source.moteur.grahique.pane;

import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import source.moteur.MoteurJeu;
import source.moteur.grahique.Controler;
import source.moteur.grahique.vue.LogVue;

/**
 * Journal de la partie et zone de saisie des commandes
 */
public class ZoneTextePane extends BorderPane {

    private final TextField saisie = new TextField();

    public ZoneTextePane(MoteurJeu moteur){
        LogVue journal = new LogVue();
        moteur.getNiveau().addObserver(journal);

        saisie.setPromptText("Que veux-tu faire ? (ex : prends la clé puis va au nord)");
        saisie.getStyleClass().add("saisie");
        new Controler(moteur, saisie);

        setCenter(journal);
        setBottom(saisie);
    }

    /**
     * Place le curseur dans la zone de saisie
     */
    public void activerSaisie(){
        saisie.requestFocus();
    }
}
