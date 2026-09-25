package source.moteur.grahique;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import source.moteur.MoteurJeu;

import java.util.ArrayList;
import java.util.List;

/**
 * Controler de la zone texte de l'utilisateur : envoie la commande au moteur.
 * Les flèches haut / bas rappellent les commandes précédentes.
 */
public class Controler implements EventHandler<ActionEvent> {

    private final MoteurJeu moteur;
    private final List<String> historique = new ArrayList<>();
    private int position = 0;

    /**
     * Constructeur du controler
     * @param moteur Moteur de la partie
     * @param champ Zone de saisie
     */
    public Controler(MoteurJeu moteur, TextField champ){
        this.moteur = moteur;
        champ.setOnAction(this);
        champ.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.UP && position > 0){
                position--;
                afficher(champ);
                e.consume();
            }else if(e.getCode() == KeyCode.DOWN && position < historique.size()){
                position++;
                afficher(champ);
                e.consume();
            }
        });
    }

    private void afficher(TextField champ){
        champ.setText(position < historique.size() ? historique.get(position) : "");
        champ.end();
    }

    @Override
    public void handle(ActionEvent event) {
        TextField tf = (TextField) event.getSource();

        String txt = tf.getText();
        if(!txt.isBlank()){
            tf.setText("");
            historique.add(txt);
            position = historique.size();
            moteur.executer(txt);
        }
    }
}
