package composants;

import javafx.geometry.Insets;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import util.UtilEditor;

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
        this.add(UtilEditor.createButton(this,"Ajouter",pf,true,false),1,0);
        this.add(UtilEditor.createButton(this,"Modifier",null,true,false),2,0);
        this.add(UtilEditor.createButton(this,"Supprimer",null,true,false),3,0);
        this.add(table ,1,1,3,1);
    }

}
