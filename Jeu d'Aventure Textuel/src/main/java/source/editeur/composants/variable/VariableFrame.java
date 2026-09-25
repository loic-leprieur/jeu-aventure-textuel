package source.editeur.composants.variable;

import javafx.scene.control.TextField;
import source.editeur.composants.FormulaireFrame;
import source.editeur.modele.ModeleJeu;
import source.editeur.modele.Variable;
import source.editeur.util.Constante;

/**
 * Classe VariableFrame
 * Fenêtre permettant d'ajouter ou de modifier une variable
 */
public class VariableFrame extends FormulaireFrame<Variable> {

    private final TextField nom = new TextField();
    private final TextField valeur = new TextField();

    public VariableFrame() {
        super("une variable");
        nom.setPromptText("ex : porte_ouverte");
        valeur.setPromptText("ex : non");
        ligne("Nom", nom);
        ligne("Valeur initiale", valeur);
    }

    @Override
    protected void vider() {
        nom.clear();
        valeur.clear();
        nom.requestFocus();
    }

    @Override
    protected void remplir(Variable v) {
        nom.setText(v.getNom());
        valeur.setText(v.getValeur());
    }

    @Override
    protected String verifier(Variable existant) {
        return Constante.premiere(
                Constante.verifierNom("Le nom de la variable", nom.getText()),
                ModeleJeu.nomUtilise(modele.getVariables(), nom.getText(), existant)
                        ? "Une autre variable s'appelle déjà « " + nom.getText().trim() + " »." : null,
                valeur.getText().isBlank() ? "La valeur initiale est obligatoire." : null,
                Constante.verifierLongueur("La valeur", valeur.getText(), Constante.TAILLE_VALEUR_MAX));
    }

    @Override
    protected void enregistrer(Variable existant) {
        Variable v = existant == null ? new Variable() : existant;
        v.setNom(nom.getText());
        v.setValeur(valeur.getText());
        if (existant == null) {
            modele.getVariables().add(v);
        }
    }
}
