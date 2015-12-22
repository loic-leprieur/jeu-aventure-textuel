package composants.lien;

import composants.GridPaneEditeur;
import javafx.stage.Stage;
import vue.LienVueList;

/**
 * Classe LienPane
 */
public class LienPane extends GridPaneEditeur {

    /**
     * Constructeur LienPane
     * Créer un pane pour lien, il sera utiliser dans le stage
     */
    public LienPane(Stage stage){
        this.createComponent(stage,new LienVueList(),new LienFrame());
    }

}
