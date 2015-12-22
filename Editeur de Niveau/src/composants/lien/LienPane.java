package composants.lien;

import composants.GridPaneEditeur;
import vue.LienVueList;

/**
 * Classe LienPane
 */
public class LienPane extends GridPaneEditeur {

    /**
     * Constructeur LienPane
     * Créer un pane pour lien, il sera utiliser dans le stage
     */
    public LienPane(){
        this.createComponent(new LienVueList(),new LienFrame());
    }

}
