package source.editeur.composants.regle;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import source.editeur.composants.FormulaireFrame;
import source.editeur.modele.ActionObjet;
import source.editeur.modele.Objet;
import source.editeur.modele.Regle;
import source.editeur.modele.Salle;
import source.editeur.modele.Textes;
import source.editeur.modele.Variable;
import source.editeur.util.Constante;

/**
 * Fenêtre permettant d'ajouter ou de modifier une règle du jeu
 */
public class RegleFrame extends FormulaireFrame<Regle> {

    //Commande
    private final TextField verbe = new TextField();
    private final ComboBox<String> complement = new ComboBox<>();
    private final TextField preposition = new TextField();
    private final ComboBox<String> complementSecondaire = new ComboBox<>();
    //Conditions
    private final ComboBox<Salle> salle = new ComboBox<>();
    private final ComboBox<Variable> conditionVariable = new ComboBox<>();
    private final TextField conditionValeur = new TextField();
    //Résultat
    private final TextArea message = new TextArea();
    private final ComboBox<Variable> variableModifiee = new ComboBox<>();
    private final TextField nouvelleValeur = new TextField();
    private final ComboBox<ActionObjet> actionObjet = new ComboBox<>();
    private final ComboBox<Objet> objetCible = new ComboBox<>();
    private final ComboBox<Salle> deplacement = new ComboBox<>();

    public RegleFrame() {
        super("une règle");

        section("Quand le joueur tape…");
        verbe.setPromptText("verbe à l'infinitif, ex : ouvrir");
        complement.setEditable(true);
        complement.setPromptText("ex : porte (facultatif)");
        complement.setMaxWidth(Double.MAX_VALUE);
        preposition.setPromptText("ex : avec (facultatif)");
        complementSecondaire.setEditable(true);
        complementSecondaire.setPromptText("ex : clé (facultatif)");
        complementSecondaire.setMaxWidth(Double.MAX_VALUE);
        ligne("Verbe", verbe);
        ligne("Complément", complement);
        ligne("Préposition", preposition);
        ligne("2nd complément", complementSecondaire);
        Label aide = new Label("Le joueur peut écrire la commande naturellement : « ouvre la porte avec la clé » "
                + "correspond à ouvrir / porte / avec / clé. Si la commande nomme un objet, la règle ne s'applique "
                + "que si le joueur l'a sur lui ou le voit dans la salle.");
        aide.setWrapText(true);
        aide.setMaxWidth(380);
        aide.getStyleClass().add("texte-aide");
        ligne("", aide);

        section("Conditions (facultatives)");
        autoriserVide(salle, "n'importe où");
        ligne("Si le joueur est", salle);
        autoriserVide(conditionVariable, "pas de condition");
        conditionValeur.setPromptText("valeur");
        conditionValeur.disableProperty().bind(conditionVariable.valueProperty().isNull());
        ligne("Si la variable", ligneValeur(conditionVariable, "=", conditionValeur));

        section("Alors…");
        message.setPromptText("Message affiché au joueur, ex : La porte s'ouvre en grinçant.");
        message.setPrefRowCount(3);
        message.setWrapText(true);
        ligne("Message", message);
        autoriserVide(variableModifiee, "aucune");
        nouvelleValeur.setPromptText("nouvelle valeur");
        nouvelleValeur.disableProperty().bind(variableModifiee.valueProperty().isNull());
        ligne("Modifier la variable", ligneValeur(variableModifiee, "←", nouvelleValeur));
        actionObjet.getItems().setAll(ActionObjet.values());
        autoriserVide(objetCible, "objet");
        objetCible.disableProperty().bind(actionObjet.valueProperty().isEqualTo(ActionObjet.AUCUNE));
        ligne("Action sur un objet", ligneValeur(actionObjet, "", objetCible));
        autoriserVide(deplacement, "ne pas bouger");
        ligne("Déplacer le joueur vers", deplacement);
    }

    private static HBox ligneValeur(ComboBox<?> choix, String separateur, javafx.scene.Node valeur) {
        choix.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(choix, Priority.ALWAYS);
        HBox.setHgrow(valeur, Priority.ALWAYS);
        HBox res = separateur.isEmpty() ? new HBox(6, choix, valeur) : new HBox(6, choix, new Label(separateur), valeur);
        res.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        return res;
    }

    @Override
    protected void actualiserListes() {
        java.util.List<String> nomsObjets = modele.getObjets().stream().map(Objet::getNom).toList();
        complement.getItems().setAll(nomsObjets);
        complementSecondaire.getItems().setAll(nomsObjets);
        remplirAvecVide(salle, modele.getSalles());
        remplirAvecVide(conditionVariable, modele.getVariables());
        remplirAvecVide(variableModifiee, modele.getVariables());
        remplirAvecVide(objetCible, modele.getObjets());
        remplirAvecVide(deplacement, modele.getSalles());
    }

    @Override
    protected void vider() {
        verbe.clear();
        complement.setValue(null);
        complement.getEditor().clear();
        preposition.clear();
        complementSecondaire.setValue(null);
        complementSecondaire.getEditor().clear();
        salle.setValue(null);
        conditionVariable.setValue(null);
        conditionValeur.clear();
        message.clear();
        variableModifiee.setValue(null);
        nouvelleValeur.clear();
        actionObjet.setValue(ActionObjet.AUCUNE);
        objetCible.setValue(null);
        deplacement.setValue(null);
    }

    @Override
    protected void remplir(Regle r) {
        verbe.setText(r.getVerbe());
        complement.setValue(r.getComplement());
        complement.getEditor().setText(r.getComplement());
        preposition.setText(r.getPreposition());
        complementSecondaire.setValue(r.getComplementSecondaire());
        complementSecondaire.getEditor().setText(r.getComplementSecondaire());
        salle.setValue(r.getSalle());
        conditionVariable.setValue(r.getConditionVariable());
        conditionValeur.setText(r.getConditionValeur());
        message.setText(r.getMessage());
        variableModifiee.setValue(r.getVariableModifiee());
        nouvelleValeur.setText(r.getNouvelleValeur());
        actionObjet.setValue(r.getActionObjet());
        objetCible.setValue(r.getObjetCible());
        deplacement.setValue(r.getDeplacement());
    }

    @Override
    protected String verifier(Regle existant) {
        if (Textes.vide(verbe.getText())) {
            return "Le verbe est obligatoire (ex : prendre, ouvrir, regarder).";
        }
        if (conditionVariable.getValue() != null && Textes.vide(conditionValeur.getText())) {
            return "Indiquez la valeur que doit avoir la variable « " + conditionVariable.getValue() + " ».";
        }
        if (variableModifiee.getValue() != null && Textes.vide(nouvelleValeur.getText())) {
            return "Indiquez la nouvelle valeur de la variable « " + variableModifiee.getValue() + " ».";
        }
        if (actionObjet.getValue() != ActionObjet.AUCUNE && objetCible.getValue() == null) {
            return "Choisissez l'objet concerné par l'action « " + actionObjet.getValue() + " ».";
        }
        boolean effet = variableModifiee.getValue() != null || actionObjet.getValue() != ActionObjet.AUCUNE
                || deplacement.getValue() != null;
        if (Textes.vide(message.getText()) && !effet) {
            return "La règle doit afficher un message ou avoir au moins un effet.";
        }
        return Constante.premiere(
                Constante.verifierLongueur("Le verbe", verbe.getText(), Constante.TAILLE_NOM_MAX),
                Constante.verifierLongueur("Le message", message.getText(), Constante.TAILLE_MESSAGE_MAX));
    }

    @Override
    protected void enregistrer(Regle existant) {
        Regle r = existant == null ? new Regle() : existant;
        r.setVerbe(verbe.getText());
        r.setComplement(complement.getEditor().getText());
        r.setPreposition(preposition.getText());
        r.setComplementSecondaire(complementSecondaire.getEditor().getText());
        r.setSalle(salle.getValue());
        r.setConditionVariable(conditionVariable.getValue());
        r.setConditionValeur(conditionVariable.getValue() == null ? "" : conditionValeur.getText());
        r.setMessage(message.getText());
        r.setVariableModifiee(variableModifiee.getValue());
        r.setNouvelleValeur(variableModifiee.getValue() == null ? "" : nouvelleValeur.getText());
        r.setActionObjet(actionObjet.getValue());
        r.setObjetCible(actionObjet.getValue() == ActionObjet.AUCUNE ? null : objetCible.getValue());
        r.setDeplacement(deplacement.getValue());
        if (existant == null) {
            modele.getRegles().add(r);
        }
    }
}
