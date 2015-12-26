package composants.association;

import composants.GridPaneEditeur;
import action.table.AssociationTable;

/**
 * Classe AssociationPane
 */
public class AssociationPane extends GridPaneEditeur {

    /**
     * Construceur de AssociationPane
     * Créer un pane pour association, il sera utiliser dans le stage
     */
    public AssociationPane(){
        this.createComponent(new AssociationTable(),new AssociationFrame());
    }

}
