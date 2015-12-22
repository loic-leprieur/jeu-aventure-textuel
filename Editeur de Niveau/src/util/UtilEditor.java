package util;

import composants.ParentFrame;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Map;

/**
 * Classe des méthode pour créer des composants
 */
public class UtilEditor {

    /**
     * Créer un Stage
     * @param titre Titre du Stage
     * @param width Longueur du Stage
     * @param height Hauteur du Stage
     * @param pane Pane du Stage
     * @param resizible Stage redimmensionnable
     * @return Stage
     */
    public static Stage createStage(String titre, int width, int height, Pane pane, boolean resizible){
        Stage stage = new Stage();
        stage.setTitle("4LEditeur " + titre);
        stage.setResizable(resizible);
        pane.getStylesheets().add("util/style/style.css");
        pane.setStyle("-fx-background-color: white");
        stage.setScene(new Scene(pane,width,height));
        return stage;
    }

    /**
     * Créer un TabPane
     * @param stage Stage du TabPane
     * @param map Map contenant le nom et la Pane
     * @return TabPane
     */
    public static TabPane createTabPane(Stage stage, Map<String,Pane> map){
        TabPane tp = new TabPane();
        tp.prefWidthProperty().bind(stage.widthProperty());
        tp.prefHeightProperty().bind(stage.heightProperty());

        for(Map.Entry<String,Pane> entry : map.entrySet()){
            Tab tab = new Tab();
            tab.setText(entry.getKey());
            tab.setClosable(false);
            tab.setContent(entry.getValue());
            tp.getTabs().addAll(tab);
        }

        return tp;
    }

    /**
     * Créer une GroupBox
     * @param stage Stage du GroupBox
     * @param titre Titre du GroupBox
     * @param pane Pane du GroupBox
     * @return GroupBox
     */
    public static GroupBox createGroupBox(Stage stage,String titre, Pane pane){
        GroupBox gb = new GroupBox(titre,pane);
        gb.prefWidthProperty().bind(stage.widthProperty());
        gb.prefHeightProperty().bind(stage.heightProperty());
        return gb;
    }

    /**
     * Créer un Bouton
     * @param stage Stage du Bouton, si null la taille du bouton sera la taille du titre
     * @param titre Titre du Bouton
     * @param pf Parent frame a ouvrir si non null
     * @return Button
     */
    public static Button createButton(Stage stage, String titre, ParentFrame pf){
        Button b = new Button(titre);
        if(stage != null) b.prefWidthProperty().bind(stage.widthProperty());
        if(pf != null) b.setOnAction(actionEvent -> pf.show());
        return b;
    }


}
