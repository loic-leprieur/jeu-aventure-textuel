package source.editeur.composants.lien;

import javafx.scene.layout.BorderPane;
import source.editeur.composants.ParentFrame;
import source.editeur.util.UtilEditor;

/**
 * Classe LienFrame
 */
public class LienFrame extends ParentFrame {

    /**
     * Constructeur de LienFrame
     * Créer un stage pour lien permettant de modifier ou d'ajouter un lien
     */
    public LienFrame(){
        BorderPane pane = new BorderPane();
        super.stage = UtilEditor.createStage("Création Lien",450,100,pane,false);
    }

}
