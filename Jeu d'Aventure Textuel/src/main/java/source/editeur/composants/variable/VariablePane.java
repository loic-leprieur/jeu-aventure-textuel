package source.editeur.composants.variable;

import source.editeur.action.table.VariableTable;
import source.editeur.composants.ListePane;
import source.editeur.modele.Variable;

/**
 * Classe VariablePane
 * Liste des éléments de type Variable, avec les boutons Ajouter / Modifier / Supprimer
 */
public class VariablePane extends ListePane<Variable> {

    public VariablePane() {
        super(new VariableTable(), new VariableFrame(),
                "Aucune variable. Les variables mémorisent l'état du jeu (ex : porte_ouverte = non).");
    }
}
