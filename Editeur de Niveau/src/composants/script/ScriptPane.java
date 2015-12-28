package composants.script;

import composants.GridPaneEditeur;
import javafx.scene.control.TextArea;
import action.table.Script;

/**
 * Classe ScriptPane
 */
public class ScriptPane extends GridPaneEditeur {

    /**
     * Constructeur de ScriptPane
     */
    public ScriptPane(){
        TextArea ta = new Script();
        ta.prefWidthProperty().bind(this.widthProperty());
        ta.prefHeightProperty().bind(this.heightProperty());
        this.add(ta,1,0);
    }
}
