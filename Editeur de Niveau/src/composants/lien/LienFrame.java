package composants.lien;

import composants.ParentFrame;
import javafx.scene.layout.BorderPane;
import util.UtilEditor;

/**
 * Created by louzw on 21/12/2015.
 */
public class LienFrame extends ParentFrame {

    public LienFrame(){
        BorderPane pane = new BorderPane();
        super.stage = UtilEditor.createStage("Création Lien",450,100,pane,false,null);
    }

}
