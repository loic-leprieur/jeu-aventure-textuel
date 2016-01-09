package source.editeur.composants;

import javafx.stage.Stage;

/**
 * Classe ParentFrame
 */
public abstract class ParentFrame {

    //Stage de la ParentFrame
    protected Stage stage;

    /**
     * Affiche le Stage
     */
    public void show(){
        stage.show();
    }

    /**
     * Retourne le stage
     * @return Stage
     */
    public Stage getStage(){
        return stage;
    }
}
