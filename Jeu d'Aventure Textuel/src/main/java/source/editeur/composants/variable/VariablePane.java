package source.editeur.composants.variable;


import source.editeur.action.table.VariableTable;
import source.editeur.composants.GridPaneEditeur;

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
