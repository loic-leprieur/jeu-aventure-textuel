package source.moteur.grahique;

import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import source.util.UtilEditor;

/**
 * Created by louzw on 09/01/2016.
 */
public class PrincipalFrame {

    public PrincipalFrame(){
        GridPane pane = new GridPane();

        Stage stage = UtilEditor.createStage("Moteur Graphique",1000,700, pane,true);


        stage.show();
    }
}
