package source.moteur.grahique.pane;

import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import source.Niveau;
import source.moteur.grahique.Controler;
import source.moteur.grahique.vue.LogVue;

/**
 * Created by louzw on 10/01/2016.
 */
public class ZoneTextePane extends GridPane {

    public ZoneTextePane(Pane pane, Niveau niveau){
        this.setStyle("-fx-background-color: red;");
        this.prefWidthProperty().bind(pane.prefWidthProperty());
        this.prefHeightProperty().bind(pane.prefHeightProperty());

        LogVue ztp = new LogVue(pane);
        niveau.addObserver(ztp);

        TextField ta2 = new TextField();
        ta2.setPrefWidth(this.getPrefWidth());
        ta2.setOnAction(new Controler(niveau));

        this.add(ztp,0,0);
        this.add(ta2,0,1);

    }
}
