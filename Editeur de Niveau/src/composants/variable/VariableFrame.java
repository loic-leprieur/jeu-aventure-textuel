package composants.variable;

import composants.ParentFrame;
import javafx.scene.layout.BorderPane;
import util.UtilEditor;

/**
 * Created by louzw on 21/12/2015.
 */
public class VariableFrame extends ParentFrame {

    public VariableFrame(){
        BorderPane pane = new BorderPane();
        super.stage = UtilEditor.createStage("Création Variable",450,100,pane,false,null);
    }

}
