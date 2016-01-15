package source.moteur.grahique;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import source.Niveau;
import source.moteur.analyseur.analyse.Analyseur;
import source.moteur.analyseur.dictionnaire.Complement;
import source.moteur.analyseur.dictionnaire.Dictionnaire;
import source.moteur.analyseur.dictionnaire.Mot;
import source.moteur.analyseur.dictionnaire.Verbe;
import source.moteur.regles.Regle;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Controler de la zone texte de l'utilisateur
 */
public class Controler implements EventHandler<ActionEvent> {

    private Niveau niveau;
    private Analyseur analyseur;

    /**
     * Constructeur du controler
     * @param niveau Observable
     */
    public Controler(Niveau niveau){
        this.niveau = niveau;
        ArrayList<Mot> mots = new ArrayList<>();
        Mot m = new Verbe("manger");
        mots.add(m);
        mots.add(new Complement("pomme"));
        mots.add(new Complement("frite"));
        Dictionnaire dico = new Dictionnaire(mots);
        ArrayList<String> syn = new ArrayList<>();
        syn.add("avaler");
        syn.add("croquer");
        dico.ajouterNouvelleAction(m,syn);
        analyseur = new Analyseur(dico);
    }

    @Override
    public void handle(ActionEvent event) {
        TextField tf = (TextField) event.getSource();

        String txt = tf.getText();
        if(!txt.equals("")){
            tf.setText("");
            this.niveau.ajouterLog(txt);
            this.niveau.ajouterLog(Regle.analyser(analyseur.analyserPhrase(txt)));

        }
    }
}
