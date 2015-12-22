
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Observable;

/**
 * Created by louzw on 21/12/2015.
 */
public class Model extends Observable {

    private ObservableList<String> itemListViewSalle;
    private ObservableList<String> itemListViewJoin;
    private ObservableList<String> itemListViewObjet;
    private ObservableList<String> itemListViewAssociation;
    private ObservableList<String> itemListViewVariable;

    public Model(){
        this.itemListViewSalle = FXCollections.observableArrayList();
        this.itemListViewJoin = FXCollections.observableArrayList();
        this.itemListViewObjet = FXCollections.observableArrayList();
        this.itemListViewAssociation = FXCollections.observableArrayList();
        this.itemListViewVariable = FXCollections.observableArrayList();
    }

    public ObservableList<String> getItemListViewSalle() {
        return itemListViewSalle;
    }

    public ObservableList<String> getItemListViewJoin() {
        return itemListViewJoin;
    }

    public ObservableList<String> getItemListViewObjet() {
        return itemListViewObjet;
    }

    public ObservableList<String> getItemListViewAssociation() {
        return itemListViewAssociation;
    }

    public ObservableList<String> getItemListViewVariable() {
        return itemListViewVariable;
    }

}
