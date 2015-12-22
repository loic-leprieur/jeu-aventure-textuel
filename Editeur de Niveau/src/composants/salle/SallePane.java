package composants.salle;

import composants.GridPaneEditeur;
import vue.SalleVueList;

/**
 * Classe SallePane
 */
public class SallePane extends GridPaneEditeur {

    /**
     * Constructeur de SallePane
     * Créer un pane pour salle, il sera utiliser dans le stage
     */
    public SallePane(){
        this.createComponent(new SalleVueList(),new SalleFrame());
    }




}
