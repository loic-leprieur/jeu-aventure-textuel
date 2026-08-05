package source.util;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * Classe du composant GroupBox
 */
public class GroupBox extends StackPane {

    /**
     * Constructeur du Groupbox
     * @param titre Titre du GroupBox
     * @param content Contenu du GroupBox
     */
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
