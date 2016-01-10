package source.moteur.grahique.pane;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Created by louzw on 10/01/2016.
 */
public class ZoneTextePane extends Pane {

    public ZoneTextePane(Pane pane){
        this.setStyle("-fx-background-color: red;");
        this.prefWidthProperty().bind(pane.prefWidthProperty());
        this.prefHeightProperty().bind(pane.prefHeightProperty());
    }
}
