package source.editeur.composants.script;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import source.editeur.modele.FormatJeu;
import source.editeur.modele.ModeleJeu;

/**
 * Classe ScriptPane
 * Aperçu du script du jeu, tel qu'il sera enregistré dans le fichier (lecture seule)
 */
public class ScriptPane extends BorderPane {

    private final TextArea texte = new TextArea();

    public ScriptPane() {
        texte.setEditable(false);
        texte.getStyleClass().add("script");
        setCenter(texte);

        Label aide = new Label("Aperçu du script enregistré dans le fichier .aventure (mis à jour automatiquement).");
        aide.getStyleClass().add("texte-aide");
        aide.setPadding(new Insets(0, 0, 4, 0));
        setTop(aide);

        ModeleJeu.get().versionProperty().addListener(o -> actualiser());
        actualiser();
    }

    private void actualiser() {
        double defilement = texte.getScrollTop();
        texte.setText(FormatJeu.ecrire(ModeleJeu.get()));
        texte.setScrollTop(defilement);
    }
}
