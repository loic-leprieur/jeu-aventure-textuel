package composants.association;

import composants.GridPaneEditor;
import vue.VueListAssociation;

/**
 * Created by louzw on 21/12/2015.
 */
public class AssociationPane extends GridPaneEditor {

    public AssociationPane(){
        this.createComponent(new VueListAssociation(),new AssociationFrame());
    }

}
