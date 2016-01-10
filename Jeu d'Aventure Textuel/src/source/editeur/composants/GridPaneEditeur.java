package source.editeur.composants;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import source.util.UtilEditor;

/**
 * Classe GridPaneEditeur
 */
public abstract class GridPaneEditeur extends GridPane {

    /**
     * Constructeur de GridPaneEditeur
     */
    public GridPaneEditeur(){
        UtilEditor.configGridPane(this,6,6,new Insets(2,6,0,0),true);
    }

    /**
     * Créer les composant dans le GridPane
     * @param table TableView correspondant à une Vue
     * @param pf ParentFrame pour les boutons
     */
    protected void createComponent(TableView table, ParentFrame pf){
        Button ajouter = UtilEditor.createButton(this,"Ajouter",true,false);
        ajouter.setOnAction(actionEvent -> pf.show());
        this.add(ajouter,1,0);

        Button supprimer = UtilEditor.createButton(this,"Supprimer",true,false);
        this.add(supprimer,2,0);

        this.add(table ,1,1,2,1);
    }

}
