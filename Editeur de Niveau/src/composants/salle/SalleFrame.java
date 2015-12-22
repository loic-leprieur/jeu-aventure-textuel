package composants.salle;

import composants.ParentFrame;
import javafx.scene.layout.BorderPane;
import util.UtilEditor;

public class SalleFrame extends ParentFrame{

    public SalleFrame(){
        BorderPane pane = new BorderPane();
        super.stage = UtilEditor.createStage("Création Salle",450,100,pane,false,null);
    }
}
