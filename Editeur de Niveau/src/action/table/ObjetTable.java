package action.table;

import composants.objet.Objet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

/**
 * Classe ObjetTable
 */
public class ObjetTable extends TableView<Objet>  {

    //Liste des objets
    public static ObservableList<Objet> objet;

    /**
     * Constructeur de ObjetTable
     */
    public ObjetTable(){
        objet = FXCollections.observableArrayList();
        this.setEditable(true);

        //table Nom
        TableColumn nom = new TableColumn("Nom");
        nom.setCellValueFactory(new PropertyValueFactory<Objet,String>("nom"));
        nom.setCellFactory(TextFieldTableCell.<Objet>forTableColumn());
        nom.setOnEditCommit(new EventHandler<CellEditEvent<Objet,String>>() {
            @Override
            public void handle(CellEditEvent<Objet,String> t) {
                t.getTableView().getItems().get(t.getTablePosition().getRow()).setNom(t.getNewValue());
            }
        });

        //table Description
        TableColumn description = new TableColumn("Description");
        description.setCellValueFactory(new PropertyValueFactory<Objet,String>("description"));
        description.setCellFactory(TextFieldTableCell.<Objet>forTableColumn());
        description.setOnEditCommit(new EventHandler<CellEditEvent<Objet,String>>() {
            @Override
            public void handle(CellEditEvent<Objet,String> t) {
                t.getTableView().getItems().get(t.getTablePosition().getRow()).setDescription(t.getNewValue());
            }
        });

        //table Prenable
        TableColumn prenable = new TableColumn("Prenable");
        prenable.setCellValueFactory(new PropertyValueFactory<Objet,String>("prenable"));
        prenable.setCellFactory(TextFieldTableCell.<Objet>forTableColumn());
        prenable.setOnEditCommit(new EventHandler<CellEditEvent<Objet,String>>() {
            @Override
            public void handle(CellEditEvent<Objet,String> t) {
                t.getTableView().getItems().get(t.getTablePosition().getRow()).setPrenable(t.getNewValue());
            }
        });

        //table Image
        TableColumn image = new TableColumn("Image");
        image.setCellValueFactory(new PropertyValueFactory<Objet,String>("image"));
        image.setCellFactory(TextFieldTableCell.<Objet>forTableColumn());
        image.setOnEditCommit(new EventHandler<CellEditEvent<Objet,String>>() {
            @Override
            public void handle(CellEditEvent<Objet,String> t) {
                t.getTableView().getItems().get(t.getTablePosition().getRow()).setImage(t.getNewValue());
            }
        });


        this.getColumns().addAll(nom,description,prenable,image);
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.setItems(objet);

    }

    /**
     * Ajouter un Objet a la table
     * @param o Objet
     */
    public static void addItem(Objet o){
        objet.add(o);
    }

}
