package composants.script;

import composants.GridPaneEditeur;
import javafx.scene.control.TextArea;
import action.table.ScriptTextVue;

/**
 * Classe ScriptPane
 */
public class ScriptPane extends GridPaneEditeur {

    /**
     * Constructeur de ScriptPane
     */
    public ScriptPane(){
        TextArea ta = new ScriptTextVue();
        ta.prefWidthProperty().bind(this.widthProperty());
        ta.prefHeightProperty().bind(this.heightProperty());
        this.add(ta,1,0);
    }
}
