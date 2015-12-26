
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import util.UtilEditor;


/**
 * Fenêtre principale
 */
public class PrincipalFrame {

    /**
     * Création de la fenêtre principale
     */
    public PrincipalFrame(){
        BorderPane pane = new BorderPane();

        Stage stage = UtilEditor.createStage("",1000,700, pane,true);

        pane.setTop(new PaneTop(stage));
        pane.setCenter(new PaneCenter());

        stage.show();
    }


}
