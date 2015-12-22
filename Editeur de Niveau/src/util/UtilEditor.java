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
 * Created by louzw on 21/12/2015.
 */
public class UtilEditor {


    public static Stage createStage(String titre, int x, int y, Pane pane, boolean resizible,Color color){
        Stage stage = new Stage();
        stage.setTitle("4LEditeur " + titre);
        if(color==null) color = Color.BEIGE;
        stage.setResizable(resizible);
        pane.getStylesheets().add("util/style/style.css");
        stage.setScene(new Scene(pane,x,y, color));
        return stage;
    }

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

    public static GroupBox createGroupBox(Stage stage,String titre, Pane pane){
        GroupBox gb = new GroupBox(titre,pane);
        gb.prefWidthProperty().bind(stage.widthProperty());
        gb.prefHeightProperty().bind(stage.heightProperty());
        return gb;
    }

    public static Button createButton(Stage stage, String titre, ParentFrame pf){
        Button b = new Button(titre);
        if(stage != null) b.prefWidthProperty().bind(stage.widthProperty());
        if(pf != null) b.setOnAction(actionEvent -> pf.show());
        return b;
    }


}
