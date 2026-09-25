package source.editeur;

import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import source.util.UtilEditor;


/**
 * Fenêtre principale
 */
public class PrincipalFrame {

    /**
     * Création de la fenêtre principale
     */
    public PrincipalFrame(){
        BorderPane pane = new BorderPane();

        Stage stage = UtilEditor.createStage("4LEditeur",1150,760, pane,true);
        ActionsFichier actions = new ActionsFichier(stage);

        pane.setTop(new PaneTop(stage, actions));
        pane.setCenter(new PaneCenter());

        //Proposer d'enregistrer avant de fermer la fenêtre
        stage.setOnCloseRequest(e -> {
            if(!actions.confirmerAbandon()){
                e.consume();
            }
        });
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();
    }


}
