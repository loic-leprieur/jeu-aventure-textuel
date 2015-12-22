package composants.objet;

import composants.ParentFrame;
import javafx.scene.layout.BorderPane;
import util.UtilEditor;

/**
 * Classe ObjetFrame
 */
public class ObjetFrame extends ParentFrame {

    /**
     * Constructeur ObjetFrame
     * Créer un stage pour objet permettant de modifier ou d'ajouter un objet
     */
    public ObjetFrame(){
        BorderPane pane = new BorderPane();
        super.stage = UtilEditor.createStage("Création Objet",450,100,pane,false,null);
    }
}
