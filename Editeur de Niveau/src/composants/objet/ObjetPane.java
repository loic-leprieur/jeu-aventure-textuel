package composants.objet;

import composants.GridPaneEditeur;
import javafx.stage.Stage;
import vue.ObjetVueList;

/**
 * Classe ObjetPane
 */
public class ObjetPane extends GridPaneEditeur {

    /**
     * Constructeur de ObjetPane
     * Créer un pane pour objet, il sera utiliser dans le stage
     */
    public ObjetPane(Stage stage){
        this.createComponent(stage,new ObjetVueList(),new ObjetFrame());
    }

}
