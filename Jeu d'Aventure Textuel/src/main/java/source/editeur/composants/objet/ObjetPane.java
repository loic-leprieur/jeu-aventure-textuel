package source.editeur.composants.objet;

import source.editeur.action.table.ObjetTable;
import source.editeur.composants.ListePane;
import source.editeur.modele.Objet;

/**
 * Classe ObjetPane
 * Liste des éléments de type Objet, avec les boutons Ajouter / Modifier / Supprimer
 */
public class ObjetPane extends ListePane<Objet> {

    public ObjetPane() {
        super(new ObjetTable(), new ObjetFrame(),
                "Aucun objet. Cliquez sur Ajouter pour créer un objet (nom, description, image).");
    }
}
