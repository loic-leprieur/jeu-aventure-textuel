package source.moteur.grahique.vue;

import javafx.scene.control.TextArea;
import source.Niveau;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Journal de la partie : commandes du joueur et réponses du jeu
 */
public class LogVue extends TextArea implements Observer {

    //Nombre de lignes du journal déjà affichées
    private int affichees = 0;

    public LogVue(){
        this.setEditable(false);
        this.setWrapText(true);
        this.setFocusTraversable(false);
        this.getStyleClass().add("journal");
    }

    @Override
    public void update(Observable o, Object arg) {
        List<String> log = ((Niveau)o).getLog();
        //appendText fait défiler jusqu'en bas
        for(; affichees < log.size(); affichees++){
            String ligne = log.get(affichees);
            this.appendText((getLength() == 0 ? "" : ligne.startsWith("> ") ? "\n\n" : "\n") + ligne);
        }
    }
}
