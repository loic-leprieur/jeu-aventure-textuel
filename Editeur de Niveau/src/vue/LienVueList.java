package vue;

import javafx.scene.control.ListView;
import java.util.Observable;
import java.util.Observer;

/**
 * Vue de la ListView pour Lien
 */
public class LienVueList extends ListView<String> implements Observer {

    public LienVueList(){
        //ObservableList<String> items = FXCollections.observableArrayList ("Item1","Item2","Item3");
        //items.add("test");
        // this.setItems(items);
    }

    @Override
    public void update(Observable o, Object arg) {

    }

}
