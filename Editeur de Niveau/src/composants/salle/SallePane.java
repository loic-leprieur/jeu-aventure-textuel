package composants.salle;

import composants.GridPaneEditeur;
import javafx.stage.Stage;
import vue.SalleVueList;

/**
 * Classe SallePane
 */
public class SallePane extends GridPaneEditeur {

    /**
     * Constructeur de SallePane
     * Créer un pane pour salle, il sera utiliser dans le stage
     */
    public SallePane(Stage stage){
        this.createComponent(stage,new SalleVueList(),new SalleFrame());
    }




}
