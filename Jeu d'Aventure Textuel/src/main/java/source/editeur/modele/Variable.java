package source.editeur.modele;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Variable d'état du jeu (ex : porte_ouverte = non)
 */
public class Variable implements ElementNomme {

    private final StringProperty nom = new SimpleStringProperty(this, "nom", "");
    //Valeur initiale de la variable
    private final StringProperty valeur = new SimpleStringProperty(this, "valeur", "");

    public Variable() {
    }

    public Variable(String nom, String valeur) {
        setNom(nom);
        setValeur(valeur);
    }

    @Override
    public StringProperty nomProperty() { return nom; }
    public StringProperty valeurProperty() { return valeur; }

    public String getValeur() { return valeur.get(); }

    public void setNom(String nom) { this.nom.set(Textes.nettoyer(nom)); }
    public void setValeur(String valeur) { this.valeur.set(Textes.nettoyer(valeur)); }

    @Override
    public Observable[] proprietes() {
        return new Observable[]{nom, valeur};
    }

    @Override
    public String toString() {
        return getNom();
    }
}
