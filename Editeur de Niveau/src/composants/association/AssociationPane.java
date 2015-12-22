package composants.association;

import composants.GridPaneEditeur;
import javafx.stage.Stage;
import vue.AssociationVueList;

/**
 * Classe AssociationPane
 */
public class AssociationPane extends GridPaneEditeur {

    /**
     * Construceur de AssociationPane
     * Créer un pane pour association, il sera utiliser dans le stage
     */
    public AssociationPane(Stage stage){
        this.createComponent(stage,new AssociationVueList(),new AssociationFrame());
    }

}
