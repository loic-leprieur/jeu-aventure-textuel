package source.editeur.action.table;

import javafx.scene.control.TableView;
import source.editeur.modele.ModeleJeu;
import source.editeur.modele.Objet;
import source.util.UtilEditor;

import java.util.List;

/**
 * Classe ObjetTable
 */
public class ObjetTable extends TableView<Objet> {

    public ObjetTable() {
        getColumns().addAll(List.of(
                Colonnes.image("Image", UtilEditor.ImageType.OBJET, Objet::getImage),
                Colonnes.texte("Nom", 80, Objet::getNom),
                Colonnes.texte("Description", 150, Objet::getDescription),
                Colonnes.texte("Prenable", 60, o -> o.isPrenable() ? "Oui" : "Non")));
        setItems(ModeleJeu.get().getObjets());
        Colonnes.rafraichirAuxModifications(this);
    }
}
