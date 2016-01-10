package source.moteur.grahique;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import source.Niveau;
import source.moteur.analyseur.Phrase;
import source.moteur.analyseur.analyse.Analyseur;
import source.moteur.analyseur.dictionnaire.Complement;
import source.moteur.analyseur.dictionnaire.Dictionnaire;
import source.moteur.analyseur.dictionnaire.Mot;
import source.moteur.analyseur.dictionnaire.Verbe;
import source.moteur.regles.Regle;

/**
 * Controler de la zone texte de l'utilisateur
 */
public class Controler implements EventHandler<ActionEvent> {

    private Niveau niveau;
    private Dictionnaire dico;
    /**
     * Constructeur du controler
     * @param niveau Observable
     */
    public Controler(Niveau niveau){
        this.niveau = niveau;
        Mot[] mots = {new Verbe("manger"), new Complement("pomme")};
        this.dico = new Dictionnaire(mots);
    }

    @Override
    public void handle(ActionEvent event) {
        TextField tf = (TextField) event.getSource();

        String txt = tf.getText();
        if(!txt.equals("")){
            tf.setText("");
            this.niveau.ajouterLog(txt);
            Analyseur analyseur = new Analyseur(dico, txt);
            Phrase phrase = analyseur.analyserPhrase();
            this.niveau.ajouterLog(Regle.analyser(phrase));

        }
    }
}
