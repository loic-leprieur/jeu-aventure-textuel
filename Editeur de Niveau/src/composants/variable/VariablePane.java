package composants.variable;

import composants.GridPaneEditeur;
import action.table.VariableTable;

/**
 * Classe VariablePane
 */
public class VariablePane extends GridPaneEditeur {

    /**
     * Constructeur de VariablePane
     * Créer un pane pour variable, il sera utiliser dans le stage
     */
    public VariablePane(){
        this.createComponent(new VariableTable(),new VariableFrame());
    }
}
