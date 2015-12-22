package vue;

import javafx.scene.control.ListView;

import java.util.Observable;
import java.util.Observer;

/**
 * Vue de la ListView pour Objet
 */
public class ObjetVueList extends ListView<String> implements Observer {
    @Override
    public void update(Observable o, Object arg) {

    }
}
