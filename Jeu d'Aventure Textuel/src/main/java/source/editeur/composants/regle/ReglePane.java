package source.editeur.composants.regle;

import source.editeur.action.table.RegleTable;
import source.editeur.composants.ListePane;
import source.editeur.modele.Regle;

/**
 * Classe ReglePane
 * Liste des éléments de type Regle, avec les boutons Ajouter / Modifier / Supprimer
 */
public class ReglePane extends ListePane<Regle> {

    public ReglePane() {
        super(new RegleTable(), new RegleFrame(),
                "Aucune règle. Une règle décrit ce qui se passe quand le joueur tape une commande (ex : ouvrir porte avec clé).");
    }
}
