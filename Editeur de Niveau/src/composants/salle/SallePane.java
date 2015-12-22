package composants.salle;

import composants.GridPaneEditor;
import vue.VueListViewSalle;

/**
 * Created by louzw on 21/12/2015.
 */
public class SallePane extends GridPaneEditor{

    public SallePane(){
        this.createComponent(new VueListViewSalle(),new SalleFrame());
    }




}
