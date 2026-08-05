package source.editeur.composants.objet;


import source.editeur.action.table.ObjetTable;
import source.editeur.composants.GridPaneEditeur;

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
