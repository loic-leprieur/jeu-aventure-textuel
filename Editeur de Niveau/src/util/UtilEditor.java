package util;

import composants.ParentFrame;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
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
     * @param pane pane du TabPane
     * @param map Map contenant le nom et la Pane
     * @return TabPane
     */
    public static TabPane createTabPane(Pane pane, Map<String,Pane> map){
        TabPane tp = new TabPane();
        tp.prefWidthProperty().bind(pane.widthProperty());
        tp.prefHeightProperty().bind(pane.heightProperty());

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
     * @param titre Titre du GroupBox
     * @param pane Pane du GroupBox
     * @param parent Pane parent du GroupBox
     * @return GroupBox
     */
    public static GroupBox createGroupBox(Pane pane,String titre,Pane parent){
        GroupBox gb = new GroupBox(titre,pane);
        gb.prefWidthProperty().bind(parent.widthProperty());
        gb.prefHeightProperty().bind(parent.heightProperty());
        return gb;
    }

    /**
     * Créer un Bouton
     * @param pane pane du Bouton, si null la taille du bouton sera la taille du titre
     * @param titre Titre du Bouton
     * @param pf Parent frame a ouvrir si non null
     * @param widthProperty Parametre de longeur
     * @param heightProperty Parametre de hauteur
     * @return Button
     */
    public static Button createButton(Pane pane, String titre, ParentFrame pf,boolean widthProperty,boolean heightProperty){
        Button b = new Button(titre);
        if(pane != null){
            if(widthProperty)b.prefWidthProperty().bind(pane.widthProperty());
            if(heightProperty)b.prefHeightProperty().bind(pane.heightProperty());
        }
        if(pf != null) b.setOnAction(actionEvent -> pf.show());
        return b;
    }

    /**
     * Créer un Label
     * @param pane Panel du Label, si null la taille du label sera la taille du titre
     * @param titre Titre du label
     * @param center Texte au centre
     * @return Label
     */
    public static Label createLabel(Pane pane, String titre,boolean center){
        Label l = new Label(titre);
        if(pane != null) l.prefWidthProperty().bind(pane.widthProperty());
        if(center)l.setAlignment(Pos.CENTER);
        return l;
    }

    /**
     * Créer une TextArea
     * @param pane Panel de la TextArea, si null la taille de la TextAre sera la taille par defaut
     * @param texte Texte pas defaut, si null aucun texte
     * @param widthProperty Parametre pour la longueur
     * @param heightProperty Parametre pour la hauteur
     * @return TextArea
     */
    public static TextArea createTextArea(Pane pane,String texte, boolean widthProperty, boolean heightProperty){
        TextArea t = new TextArea();
        if(texte!=null)t.setText(texte);
        if(pane!=null){
            if(widthProperty){
                t.prefWidthProperty().bind(pane.widthProperty());
            }
            if(heightProperty){
                t.prefHeightProperty().bind(pane.heightProperty());
            }else{
                t.setMaxHeight(15);
            }
        }
        return t;
    }

    /**
     * Créer une ComboBox de String
     * @param pane Panel de la ComboBox, si null la taille de la ComboBox sera la taille par defaut
     * @param contenu Contenu de la ComboBox
     * @return Combobox
     */
    public static ComboBox<String> createComboBox(Pane pane, ObservableList<String> contenu){
        ComboBox<String> cb = new ComboBox<>(contenu);
        if(pane != null)cb.prefWidthProperty().bind(pane.widthProperty());
        cb.setEditable(false);
        cb.setValue(contenu.get(0));
        return cb;
    }

    /**
     * Créer une CheckBox
     * @param pane Panel de la CheckBox, si null la taille de la CheckBox sera la taille par defaut
     * @param titre Titre de la CheckBox
     * @param selected CheckBox coché ou non
     * @param center CheckBox centrer ou non
     * @return CheckBox
     */
    public static CheckBox createCheckBox(Pane pane, String titre,boolean selected,boolean center){
        CheckBox c = new CheckBox(titre);
        if(pane!=null)c.prefWidthProperty().bind(pane.widthProperty());
        c.setSelected(selected);
        if(center)c.setAlignment(Pos.CENTER);
        return c;
    }

    /**
     * Configuration d'un GridPane
     * @param pane GridPane
     * @param hgap parametre hgap du gridpane
     * @param vgap parametre vgap du gridpane
     * @param i Inset du gripdpane
     * @param center alignement centrer dans gridpane
     */
    public static void configGridPane(GridPane pane, int hgap, int vgap, Insets i,boolean center){
        pane.setHgap(hgap);
        pane.setVgap(vgap);
        pane.setPadding(i);
        if(center)pane.setAlignment(Pos.CENTER);
    }

}
