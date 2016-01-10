package source.moteur.grahique.pane;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Created by louzw on 10/01/2016.
 */
public class SallePane extends Pane {

    public SallePane(Pane pane){
        this.setStyle("-fx-background-color: yellow;");
        this.prefWidthProperty().bind(pane.prefWidthProperty());
        this.prefHeightProperty().bind(pane.prefHeightProperty());
    }
}
