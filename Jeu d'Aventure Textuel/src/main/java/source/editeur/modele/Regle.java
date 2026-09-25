package source.editeur.modele;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Règle du jeu : ce qui se passe quand le joueur tape une commande.
 * <ul>
 *     <li>Commande : verbe [complément] [préposition complément secondaire],
 *     telle que reconnue par l'analyseur ("ouvrir porte avec clé")</li>
 *     <li>Conditions (facultatives) : le joueur est dans une salle, une variable a une valeur</li>
 *     <li>Résultat : un message, et éventuellement une variable modifiée,
 *     une action sur un objet, un déplacement du joueur</li>
 * </ul>
 */
public class Regle implements ElementJeu {

    //Commande
    private final StringProperty verbe = new SimpleStringProperty(this, "verbe", "");
    private final StringProperty complement = new SimpleStringProperty(this, "complement", "");
    private final StringProperty preposition = new SimpleStringProperty(this, "preposition", "");
    private final StringProperty complementSecondaire = new SimpleStringProperty(this, "complementSecondaire", "");

    //Conditions
    private final ObjectProperty<Salle> salle = new SimpleObjectProperty<>(this, "salle");
    private final ObjectProperty<Variable> conditionVariable = new SimpleObjectProperty<>(this, "conditionVariable");
    private final StringProperty conditionValeur = new SimpleStringProperty(this, "conditionValeur", "");

    //Résultat
    private final StringProperty message = new SimpleStringProperty(this, "message", "");
    private final ObjectProperty<Variable> variableModifiee = new SimpleObjectProperty<>(this, "variableModifiee");
    private final StringProperty nouvelleValeur = new SimpleStringProperty(this, "nouvelleValeur", "");
    private final ObjectProperty<ActionObjet> actionObjet = new SimpleObjectProperty<>(this, "actionObjet", ActionObjet.AUCUNE);
    private final ObjectProperty<Objet> objetCible = new SimpleObjectProperty<>(this, "objetCible");
    private final ObjectProperty<Salle> deplacement = new SimpleObjectProperty<>(this, "deplacement");

    public StringProperty verbeProperty() { return verbe; }
    public StringProperty complementProperty() { return complement; }
    public StringProperty prepositionProperty() { return preposition; }
    public StringProperty complementSecondaireProperty() { return complementSecondaire; }
    public ObjectProperty<Salle> salleProperty() { return salle; }
    public ObjectProperty<Variable> conditionVariableProperty() { return conditionVariable; }
    public StringProperty conditionValeurProperty() { return conditionValeur; }
    public StringProperty messageProperty() { return message; }
    public ObjectProperty<Variable> variableModifieeProperty() { return variableModifiee; }
    public StringProperty nouvelleValeurProperty() { return nouvelleValeur; }
    public ObjectProperty<ActionObjet> actionObjetProperty() { return actionObjet; }
    public ObjectProperty<Objet> objetCibleProperty() { return objetCible; }
    public ObjectProperty<Salle> deplacementProperty() { return deplacement; }

    public String getVerbe() { return verbe.get(); }
    public String getComplement() { return complement.get(); }
    public String getPreposition() { return preposition.get(); }
    public String getComplementSecondaire() { return complementSecondaire.get(); }
    public Salle getSalle() { return salle.get(); }
    public Variable getConditionVariable() { return conditionVariable.get(); }
    public String getConditionValeur() { return conditionValeur.get(); }
    public String getMessage() { return message.get(); }
    public Variable getVariableModifiee() { return variableModifiee.get(); }
    public String getNouvelleValeur() { return nouvelleValeur.get(); }
    public ActionObjet getActionObjet() { return actionObjet.get(); }
    public Objet getObjetCible() { return objetCible.get(); }
    public Salle getDeplacement() { return deplacement.get(); }

    public void setVerbe(String v) { verbe.set(Textes.nettoyer(v)); }
    public void setComplement(String v) { complement.set(Textes.nettoyer(v)); }
    public void setPreposition(String v) { preposition.set(Textes.nettoyer(v)); }
    public void setComplementSecondaire(String v) { complementSecondaire.set(Textes.nettoyer(v)); }
    public void setSalle(Salle s) { salle.set(s); }
    public void setConditionVariable(Variable v) { conditionVariable.set(v); }
    public void setConditionValeur(String v) { conditionValeur.set(Textes.nettoyer(v)); }
    public void setMessage(String v) { message.set(Textes.nettoyer(v)); }
    public void setVariableModifiee(Variable v) { variableModifiee.set(v); }
    public void setNouvelleValeur(String v) { nouvelleValeur.set(Textes.nettoyer(v)); }
    public void setActionObjet(ActionObjet a) { actionObjet.set(a == null ? ActionObjet.AUCUNE : a); }
    public void setObjetCible(Objet o) { objetCible.set(o); }
    public void setDeplacement(Salle s) { deplacement.set(s); }

    /**
     * @return Commande sous forme lisible : "ouvrir porte avec clé", "regarder sous lit"
     */
    public String getCommande() {
        StringBuilder sb = new StringBuilder(getVerbe());
        //Sans second complément, la préposition porte sur le complément : "regarder sous lit"
        String[] mots = Textes.vide(getComplementSecondaire())
                ? new String[]{getPreposition(), getComplement()}
                : new String[]{getComplement(), getPreposition(), getComplementSecondaire()};
        for (String mot : mots) {
            if (!Textes.vide(mot)) {
                sb.append(' ').append(mot);
            }
        }
        return sb.toString();
    }

    /**
     * @return Conditions sous forme lisible : "dans Bureau, si porte = ouverte"
     */
    public String getResumeConditions() {
        List<String> res = new ArrayList<>();
        if (getSalle() != null) {
            res.add("dans " + getSalle().getNom());
        }
        if (getConditionVariable() != null) {
            res.add("si " + getConditionVariable().getNom() + " = " + getConditionValeur());
        }
        return res.isEmpty() ? "toujours" : String.join(", ", res);
    }

    /**
     * @return Effets sous forme lisible, sans le message : "porte := ouverte ; clé : Mettre dans l'inventaire"
     */
    public String getResumeEffets() {
        List<String> res = new ArrayList<>();
        if (getVariableModifiee() != null) {
            res.add(getVariableModifiee().getNom() + " := " + getNouvelleValeur());
        }
        if (getActionObjet() != ActionObjet.AUCUNE && getObjetCible() != null) {
            res.add(getObjetCible().getNom() + " : " + getActionObjet());
        }
        if (getDeplacement() != null) {
            res.add("aller à " + getDeplacement().getNom());
        }
        return String.join(" ; ", res);
    }

    @Override
    public Observable[] proprietes() {
        return new Observable[]{verbe, complement, preposition, complementSecondaire,
                salle, conditionVariable, conditionValeur,
                message, variableModifiee, nouvelleValeur, actionObjet, objetCible, deplacement};
    }

    @Override
    public String toString() {
        return getCommande();
    }
}
