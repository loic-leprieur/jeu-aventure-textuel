package composants.objet;

import composants.GridPaneEditeur;
import vue.ObjetVueList;

/**
 * Classe ObjetPane
 */
public class ObjetPane extends GridPaneEditeur {

    /**
     * Constructeur de ObjetPane
     * Créer un pane pour objet, il sera utiliser dans le stage
     */
    public ObjetPane(){
        this.createComponent(new ObjetVueList(),new ObjetFrame());
    }

}
