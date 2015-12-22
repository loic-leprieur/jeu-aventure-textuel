package composants.variable;

import composants.GridPaneEditor;
import vue.VueListVariable;

/**
 * Created by louzw on 21/12/2015.
 */
public class VariablePane extends GridPaneEditor {

    public VariablePane(){
        this.createComponent(new VueListVariable(),new VariableFrame());
    }
}
