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
        final TextArea ta = new Script();
        ta.prefWidthProperty().bind(this.widthProperty());
        ta.prefHeightProperty().bind(this.heightProperty());
        ta.setEditable(false);
        ta.textProperty().bindBidirectional(Script.script);
        Script.script.addListener((observable, oldValue, newValue) -> {
            ta.setText(newValue);
        });
        this.add(ta,1,0);
        Script.refresh();
    }
}
