package composants.salle;

import composants.GridPaneEditeur;
import vue.VueListViewSalle;

/**
 * Classe SallePane
 */
public class SallePane extends GridPaneEditeur {

    /**
     * Constructeur de SallePane
     * Créer un pane pour salle, il sera utiliser dans le stage
     */
    public SallePane(){
        this.createComponent(new VueListViewSalle(),new SalleFrame());
    }




}
