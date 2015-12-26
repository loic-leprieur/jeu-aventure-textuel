package action.table;

import composants.Objet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Classe ObjetTable
 */
public class ObjetTable extends TableView<Objet>  {

    //Liste des objets
    public static ObservableList<Objet> itemListViewObjet;

    /**
     * Constructeur de ObjetTable
     */
    public ObjetTable(){
        itemListViewObjet = FXCollections.observableArrayList();

        TableColumn nom = new TableColumn("Nom");
        nom.setCellValueFactory(new PropertyValueFactory<Objet,String>("nom"));
        TableColumn description = new TableColumn("Description");
        description.setCellValueFactory(new PropertyValueFactory<Objet,String>("description"));
        TableColumn prenable = new TableColumn("Prenable");
        prenable.setCellValueFactory(new PropertyValueFactory<Objet,Boolean>("prenable"));
        TableColumn image = new TableColumn("Image");
        image.setCellValueFactory(new PropertyValueFactory<Objet,String>("image"));

        this.getColumns().addAll(nom,description,prenable,image);
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.setItems(itemListViewObjet);
    }

    /**
     * Ajouter un Objet a la table
     * @param o Objet
     */
    public static void addItem(Objet o){
        itemListViewObjet.add(o);
    }

}
