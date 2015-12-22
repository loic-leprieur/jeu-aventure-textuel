package composants.association;

import composants.ParentFrame;
import javafx.scene.layout.BorderPane;
import util.UtilEditor;

/**
 * Created by louzw on 21/12/2015.
 */
public class AssociationFrame extends ParentFrame {

    public AssociationFrame(){
        BorderPane pane = new BorderPane();
        super.stage = UtilEditor.createStage("Création Association",450,100,pane,false,null);
    }

}
