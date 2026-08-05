package source.moteur.grahique.vue;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import source.Niveau;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by louzw on 10/01/2016.
 */
public class SalleImageViewVue extends ImageView implements Observer {

    Pane pane;

    public SalleImageViewVue(Pane pane){
        this.pane = pane;
        this.setStyle("-fx-background-color: green;");
    }


    @Override
    public void update(Observable o, Object arg) {
        Image img = ((Niveau)o).getSalleActuel().getImage();
        this.setImage(img);
    }
}
