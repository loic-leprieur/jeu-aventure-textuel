package composants.objet;

import composants.ParentFrame;
import javafx.scene.layout.BorderPane;
import util.UtilEditor;

/**
 * Created by louzw on 21/12/2015.
 */
public class ObjetFrame extends ParentFrame {

    public ObjetFrame(){
        BorderPane pane = new BorderPane();
        super.stage = UtilEditor.createStage("Création Objet",450,100,pane,false,null);
    }
}
