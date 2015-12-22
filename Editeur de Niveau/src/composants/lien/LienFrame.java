package composants.lien;

import composants.ParentFrame;
import javafx.scene.layout.BorderPane;
import util.UtilEditor;

/**
 * Classe LienFrame
 */
public class LienFrame extends ParentFrame {

    /**
     * Constructeur de LienFrame
     * Créer un stage pour lien permettant de modifier ou d'ajouter un lien
     */
    public LienFrame(){
        BorderPane pane = new BorderPane();
        super.stage = UtilEditor.createStage("Création Lien",450,100,pane,false,null);
    }

}
