package composants.variable;

import action.bouton.VariableBouton;
import composants.ParentFrame;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import util.UtilEditor;

/**
 * Classe VariableFrame
 */
public class VariableFrame extends ParentFrame {

    /**
     * Constructeur de VariableFrame
     * Créer un stage pour variable permettant de modifier ou d'ajouter une variable
     */
    public VariableFrame(){
        GridPane pane = new GridPane();
        UtilEditor.configGridPane(pane,6,6,new Insets(0,6,0,5),true);

        pane.add(UtilEditor.createLabel(pane,"Nom",true),0,0);
        pane.add(UtilEditor.createLabel(pane,"Valeur",true),1,0);

        final TextArea nom = UtilEditor.createTextArea(pane,null,true,false);
        pane.add(nom,0,1);

        final TextArea valeur = UtilEditor.createTextArea(pane,null,true,false);
        pane.add(valeur,1,1);

        final Button b = UtilEditor.createButton(pane,"Ajouter",null,true,false);
        b.setOnAction(new VariableBouton(nom,valeur));
        pane.add(b,2,0,1,2);

        super.stage = UtilEditor.createStage("Création Variable",450,100,pane,false);
    }

}
