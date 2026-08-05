package source.editeur.action.table;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import source.editeur.composants.objet.Objet;
import source.editeur.composants.variable.Variable;

/**
 * Classe VariableTable
 */
public class VariableTable extends TableView {

    public static ObservableList<Variable> variable;

    public VariableTable(){
        variable = FXCollections.observableArrayList();

        TableColumn nom = new TableColumn("Nom");
        nom.setCellValueFactory(new PropertyValueFactory<Objet,String>("nom"));


        TableColumn valeur = new TableColumn("Valeur");
        valeur.setCellValueFactory(new PropertyValueFactory<Objet,String>("valeur"));


        this.getColumns().addAll(nom,valeur);
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.setItems(variable);
    }

    /**
     * Ajouter une Variable a la table
     * @param o Variable
     */
    public static void addItem(Variable o){
        variable.add(o);
    }

}
