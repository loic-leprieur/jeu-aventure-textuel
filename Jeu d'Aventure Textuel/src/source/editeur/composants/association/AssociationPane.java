package source.editeur.composants.association;


import source.editeur.action.table.AssociationTable;
import source.editeur.composants.GridPaneEditeur;

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
