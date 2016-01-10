package source.moteur.grahique;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import source.moteur.grahique.composant.MenuBarTop;
import source.moteur.grahique.pane.InformationPane;
import source.moteur.grahique.pane.InventairePane;
import source.moteur.grahique.pane.SallePane;
import source.moteur.grahique.pane.ZoneTextePane;
import source.util.UtilEditor;

/**
 * Created by louzw on 09/01/2016.
 */
public class PrincipalFrame {

    public PrincipalFrame(){
        BorderPane paneRoot = new BorderPane();
        Stage stage = UtilEditor.createStage("Moteur Graphique",1000,700, paneRoot,true);


        GridPane pane = new GridPane();

        pane.prefWidthProperty().bind(paneRoot.widthProperty());
        pane.prefHeightProperty().bind(paneRoot.heightProperty());

        pane.setGridLinesVisible(true);
        pane.add(new SallePane(pane),0,0,2,2);
        pane.add(new InventairePane(pane),2,0,1,2);
        pane.add(new ZoneTextePane(pane),0,2,2,1);
        pane.add(new InformationPane(pane),2,2,1,1);


        ColumnConstraints col1Constraints = new ColumnConstraints();
        col1Constraints.setPercentWidth(75);
        pane.getColumnConstraints().addAll(col1Constraints);

        RowConstraints row1Constraints = new RowConstraints();
        row1Constraints.setPercentHeight(70);
        pane.getRowConstraints().addAll(row1Constraints);

        paneRoot.setCenter(pane);
        paneRoot.setTop(new MenuBarTop(paneRoot));


        stage.show();
    }
}
