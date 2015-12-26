package composants.objet;

import composants.GridPaneEditeur;
import action.table.ObjetTable;

/**
 * Classe ObjetPane
 */
public class ObjetPane extends GridPaneEditeur {

    /**
     * Constructeur de ObjetPane
     * Créer un pane pour objet, il sera utiliser dans le stage
     */
    public ObjetPane(){
        this.createComponent(new ObjetTable(),new ObjetFrame());
    }

}
