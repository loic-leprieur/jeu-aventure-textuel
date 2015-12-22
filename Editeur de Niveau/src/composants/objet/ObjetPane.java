package composants.objet;

import composants.GridPaneEditor;
import vue.VueListObjet;

/**
 * Created by louzw on 21/12/2015.
 */
public class ObjetPane extends GridPaneEditor {

    public ObjetPane(){
        this.createComponent(new VueListObjet(),new ObjetFrame());
    }

}
