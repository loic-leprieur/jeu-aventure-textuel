package composants;

import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import util.UtilEditor;

/**
 * Classe GridPaneEditeur
 */
public abstract class GridPaneEditeur extends GridPane {

    /**
     * Constructeur de GridPaneEditeur
     */
    public GridPaneEditeur(){
        this.setHgap(6);
        this.setVgap(6);
        this.setPadding(new Insets(0, 6, 0, 0));
    }

    /**
     * Créer les composant dans le GridPane
     * @param lv ListView correspondant à une Vue
     * @param pf ParentFrame pour les boutons
     */
    protected void createComponent(Stage stage, ListView<String> lv, ParentFrame pf){
        this.add(UtilEditor.createButton(stage,"Ajouter",pf),1,0);
        this.add(UtilEditor.createButton(stage,"Modifier",null),2,0);
        this.add(UtilEditor.createButton(stage,"Supprimer",null),3,0);

        this.add(lv ,1,1,3,1);
       // this.prefWidthProperty().bind(pf.getStage().widthProperty());
    }

}
