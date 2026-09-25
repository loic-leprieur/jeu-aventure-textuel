package source.editeur.composants.lien;

import source.editeur.action.table.LienTable;
import source.editeur.composants.ListePane;
import source.editeur.modele.Lien;

/**
 * Classe LienPane
 * Liste des éléments de type Lien, avec les boutons Ajouter / Modifier / Supprimer
 */
public class LienPane extends ListePane<Lien> {

    public LienPane() {
        super(new LienTable(), new LienFrame(),
                "Aucun lien. Les liens définissent les passages entre les salles (nord, sud, est, ouest).");
    }
}
