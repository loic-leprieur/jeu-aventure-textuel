package vue;

import javafx.scene.control.ListView;

import java.util.Observable;
import java.util.Observer;

/**
 * Vue de la ListView pour Variable
 */
public class VariableVueList extends ListView<String> implements Observer {
    @Override
    public void update(Observable o, Object arg) {

    }
}
