package source.editeur.composants.salle;

import source.editeur.action.table.SalleTable;
import source.editeur.composants.GridPaneEditeur;

/**
 * Classe SallePane
 */
public class SallePane extends GridPaneEditeur {

    /**
     * Constructeur de SallePane
     * Créer un pane pour salle, il sera utiliser dans le stage
     */
    public SallePane(){
        this.createComponent(new SalleTable(),new SalleFrame());
    }




}
