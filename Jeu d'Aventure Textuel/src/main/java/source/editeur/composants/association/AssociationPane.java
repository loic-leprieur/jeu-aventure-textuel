package source.editeur.composants.association;

import source.editeur.action.table.AssociationTable;
import source.editeur.composants.ListePane;
import source.editeur.modele.Association;
import source.editeur.modele.Salle;

/**
 * Classe AssociationPane
 * Liste des objets placés dans les salles
 */
public class AssociationPane extends ListePane<Association> {

    public AssociationPane() {
        super(new AssociationTable(), new AssociationFrame(),
                "Aucun objet placé. Ajoutez un objet dans une salle pour qu'il apparaisse sur la carte.");
    }

    /**
     * Ouvre le formulaire d'ajout avec une salle déjà choisie
     */
    public void placerDans(Salle s) {
        ((AssociationFrame) formulaire).preselectionnerSalle(s);
        formulaire.creer(getScene() == null ? null : getScene().getWindow());
    }

    /**
     * Ouvre le formulaire de modification d'un placement
     */
    public void modifier(Association a) {
        formulaire.modifier(getScene() == null ? null : getScene().getWindow(), a);
    }
}
