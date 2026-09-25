package source.moteur.grahique.pane;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import source.Niveau;
import source.moteur.MoteurJeu;

import java.util.Observable;
import java.util.Observer;

/**
 * Informations sur la partie : nom du jeu, salle actuelle et sorties
 */
public class InformationPane extends VBox implements Observer {

    private final MoteurJeu moteur;
    private final Label salle = new Label();
    private final Label sorties = new Label();

    public InformationPane(MoteurJeu moteur){
        this.moteur = moteur;
        setSpacing(4);
        Label jeu = new Label(moteur.getNiveau().getNom().isBlank() ? "Jeu d'aventure" : moteur.getNiveau().getNom());
        jeu.getStyleClass().add("titre-jeu");
        jeu.setWrapText(true);
        salle.getStyleClass().add("titre-panneau");
        sorties.setWrapText(true);
        getChildren().addAll(jeu, salle, sorties);
    }

    @Override
    public void update(Observable o, Object arg) {
        salle.setText("Lieu : " + ((Niveau) o).getSalleActuel().getNom());
        sorties.setText(moteur.texteSorties());
    }
}
