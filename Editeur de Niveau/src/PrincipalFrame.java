
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import util.UtilEditor;


/**
 * Created by louzw on 21/12/2015.
 */
public class PrincipalFrame {

    public static Model model;
    public static BorderPane root;

    public PrincipalFrame(){
        root = new BorderPane();
        model = new Model();

        Stage stage = UtilEditor.createStage("",1000,700,root,true,null);

        root.setTop(new PaneTop(stage));
        root.setCenter(new PaneCenter(stage));

        stage.show();
    }


}
