package composants.variable;

import composants.ParentFrame;
import javafx.scene.layout.BorderPane;
import util.UtilEditor;

/**
 * Classe VariableFrame
 */
public class VariableFrame extends ParentFrame {

    /**
     * Constructeur de VariableFrame
     * Créer un stage pour variable permettant de modifier ou d'ajouter une variable
     */
    public VariableFrame(){
        BorderPane pane = new BorderPane();
        super.stage = UtilEditor.createStage("Création Variable",450,100,pane,false);
    }

}
