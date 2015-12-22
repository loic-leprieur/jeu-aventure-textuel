package composants.lien;

import composants.GridPaneEditor;
import vue.VueListViewLien;

/**
 * Created by louzw on 21/12/2015.
 */
public class LienPane extends GridPaneEditor {

    public LienPane(){
        this.createComponent(new VueListViewLien(),new LienFrame());
    }

}
