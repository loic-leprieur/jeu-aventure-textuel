package source.editeur.composants.lien;

import source.editeur.action.table.LienTable;
import source.editeur.composants.GridPaneEditeur;

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
