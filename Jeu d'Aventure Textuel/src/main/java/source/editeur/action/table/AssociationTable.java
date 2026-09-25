package source.editeur.action.table;

import javafx.scene.control.TableView;
import source.editeur.modele.Association;
import source.editeur.modele.ModeleJeu;
import source.editeur.modele.Textes;
import source.util.UtilEditor;

import java.util.List;

/**
 * Classe AssociationTable : objets placés dans les salles
 */
public class AssociationTable extends TableView<Association> {

    public AssociationTable() {
        getColumns().addAll(List.of(
                Colonnes.image("Image", UtilEditor.ImageType.OBJET, a -> a.getObjet() == null ? "" : a.getObjet().getImage()),
                Colonnes.texte("Objet", 80, a -> Textes.nom(a.getObjet(), "?")),
                Colonnes.texte("Salle", 80, a -> Textes.nom(a.getSalle(), "?")),
                Colonnes.texte("Position", 70, a -> a.getX() + " %, " + a.getY() + " %"),
                Colonnes.texte("Visible", 50, a -> a.isVisible() ? "Oui" : "Non")));
        setItems(ModeleJeu.get().getAssociations());
        Colonnes.rafraichirAuxModifications(this);
    }
}
