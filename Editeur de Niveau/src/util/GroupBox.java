package util;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * Created by louzw on 21/12/2015.
 */
public class GroupBox extends StackPane {

    public GroupBox(String titre, Node content){
        Label title = new Label(" " + titre + " ");
        title.getStyleClass().add("groupbox-title");
        StackPane.setAlignment(title, Pos.TOP_CENTER);

        StackPane contentPane = new StackPane();
        content.getStyleClass().add("groupbox-content");
        contentPane.getChildren().add(content);

        getStyleClass().add("groupbox-border");
        getChildren().addAll(title, contentPane);
    }

}
