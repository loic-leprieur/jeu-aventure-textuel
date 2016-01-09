package source.editeur.composants.objet;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import source.editeur.action.bouton.ObjetBouton;
import source.editeur.composants.ParentFrame;
import source.editeur.images.ObservableListImage;
import source.editeur.util.UtilEditor;

/**
 * Classe ObjetFrame
 */
public class ObjetFrame extends ParentFrame {

    /**
     * Constructeur ObjetFrame
     * Créer un stage pour objet permettant de modifier ou d'ajouter un objet
     */
    public ObjetFrame(){

        GridPane pane = new GridPane();
        UtilEditor.configGridPane(pane,6,6,new Insets(0,6,0,5),true);

        pane.add(UtilEditor.createLabel(pane,"Nom",true),0,0);
        pane.add(UtilEditor.createLabel(pane,"Description",true),1,0);
        pane.add(UtilEditor.createLabel(pane,"Chemin Image",true),3,0);

        final TextArea nom = UtilEditor.createTextArea(pane,null,true,false);
        pane.add(nom,0,1);

        final TextArea description = UtilEditor.createTextArea(pane,null,true,false);
        pane.add(description,1,1);

        final CheckBox prenable = UtilEditor.createCheckBox(pane,"Objet prenable",false,true);
        pane.add(prenable,2,0,1,2);

        final ComboBox<String> image = UtilEditor.createComboBox(pane, ObservableListImage.imageObjetList);
        pane.add(image,3,1);

        final Button b = UtilEditor.createButton(pane,"Ajouter",true,false);
        b.setOnAction(new ObjetBouton(nom,description,prenable,image));
        pane.add(b,4,0,1,2);

        super.stage = UtilEditor.createStage("Création Objet",600,100,pane,false);
    }
}
