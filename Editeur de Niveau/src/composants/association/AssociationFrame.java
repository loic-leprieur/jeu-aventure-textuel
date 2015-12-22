package composants.association;

import composants.ParentFrame;
import javafx.scene.layout.BorderPane;
import util.UtilEditor;

/**
 * Classe AssociationFrame
 */
public class AssociationFrame extends ParentFrame {

    /**
     * Constructeur de Association Frame
     * Créer un stage pour association permettant de modifier ou d'ajouter une association
     */
    public AssociationFrame(){
        BorderPane pane = new BorderPane();
        super.stage = UtilEditor.createStage("Création Association",450,100,pane,false);
    }

}
