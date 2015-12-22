package vue;

import javafx.scene.control.ListView;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by louzw on 22/12/2015.
 */
public class VueListObjet extends ListView<String> implements Observer {
    @Override
    public void update(Observable o, Object arg) {

    }
}
