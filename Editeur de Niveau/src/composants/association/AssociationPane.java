package composants.association;

import composants.GridPaneEditeur;
import vue.AssociationVueList;

/**
 * Classe AssociationPane
 */
public class AssociationPane extends GridPaneEditeur {

    /**
     * Construceur de AssociationPane
     * Créer un pane pour association, il sera utiliser dans le stage
     */
    public AssociationPane(){
        this.createComponent(new AssociationVueList(),new AssociationFrame());
    }

}
