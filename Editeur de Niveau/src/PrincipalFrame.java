
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import util.UtilEditor;


/**
 * Fenêtre principale
 */
public class PrincipalFrame {

    public static Model model;
    public BorderPane pane;

    /**
     * Création de la fenêtre principale
     */
    public PrincipalFrame(){
        pane = new BorderPane();
        model = new Model();

        Stage stage = UtilEditor.createStage("",1000,700, pane,true);

        pane.setTop(new PaneTop(stage));
        pane.setCenter(new PaneCenter(stage));

        stage.show();
    }


}
