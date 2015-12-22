
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import util.UtilEditor;


/**
 * Fenêtre principale
 */
public class PrincipalFrame {

    public static Model model;
    public static BorderPane root;

    /**
     * Création de la fenêtre principale
     */
    public PrincipalFrame(){
        root = new BorderPane();
        model = new Model();

        Stage stage = UtilEditor.createStage("",1000,700,root,true,null);

        root.setTop(new PaneTop(stage));
        root.setCenter(new PaneCenter(stage));

        stage.show();
    }


}
