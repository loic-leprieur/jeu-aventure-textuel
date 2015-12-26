package composants.lien;

import composants.GridPaneEditeur;
import action.table.LienTable;

/**
 * Classe LienPane
 */
public class LienPane extends GridPaneEditeur {

    /**
     * Constructeur LienPane
     * Créer un pane pour lien, il sera utiliser dans le stage
     */
    public LienPane(){
        this.createComponent(new LienTable(),new LienFrame());
    }

}
