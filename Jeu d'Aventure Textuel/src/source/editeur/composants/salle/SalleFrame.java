package source.editeur.composants.salle;

import javafx.scene.layout.BorderPane;
import source.editeur.composants.ParentFrame;
import source.editeur.util.UtilEditor;

/**
 * Classe SalleFrame
 */
public class SalleFrame extends ParentFrame {

    /**
     * Constructeur de SalleFrame
     * Créer un stage pour salle permettant de modifier ou d'ajouter une salle
     */
    public SalleFrame(){
        BorderPane pane = new BorderPane();
        super.stage = UtilEditor.createStage("Création Salle",450,100,pane,false);
    }
}
