package composants.variable;

import composants.GridPaneEditeur;
import javafx.stage.Stage;
import vue.VariableVueList;

/**
 * Classe VariablePane
 */
public class VariablePane extends GridPaneEditeur {

    /**
     * Constructeur de VariablePane
     * Créer un pane pour variable, il sera utiliser dans le stage
     */
    public VariablePane(Stage stage){
        this.createComponent(stage,new VariableVueList(),new VariableFrame());
    }
}
