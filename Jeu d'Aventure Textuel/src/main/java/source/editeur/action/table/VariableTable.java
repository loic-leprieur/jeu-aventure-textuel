package source.editeur.action.table;

import javafx.scene.control.TableView;
import source.editeur.modele.ModeleJeu;
import source.editeur.modele.Variable;

import java.util.List;

/**
 * Classe VariableTable
 */
public class VariableTable extends TableView<Variable> {

    public VariableTable() {
        getColumns().addAll(List.of(
                Colonnes.texte("Nom", 100, Variable::getNom),
                Colonnes.texte("Valeur initiale", 100, Variable::getValeur)));
        setItems(ModeleJeu.get().getVariables());
        Colonnes.rafraichirAuxModifications(this);
    }
}
