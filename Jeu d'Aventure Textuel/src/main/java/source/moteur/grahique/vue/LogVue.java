package source.moteur.grahique.vue;

import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import source.Niveau;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by louzw on 10/01/2016.
 */
public class LogVue extends TextArea implements Observer {

    public LogVue(Pane pane){
        this.setEditable(false);
        this.setPrefWidth(pane.getPrefWidth());
    }

    @Override
    public void update(Observable o, Object arg) {
        String txt = "";
        for(String s : ((Niveau)o).getLog()){
            txt += s + "\n";
        }
        this.setText(txt);
    }
}
