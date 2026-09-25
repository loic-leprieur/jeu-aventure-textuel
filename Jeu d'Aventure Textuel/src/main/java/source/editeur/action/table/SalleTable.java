package source.editeur.action.table;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import source.editeur.modele.ModeleJeu;
import source.editeur.modele.Salle;
import source.util.UtilEditor;

/**
 * Classe SalleTable
 */
public class SalleTable extends TableView<Salle> {

    public SalleTable() {
        ModeleJeu m = ModeleJeu.get();
        TableColumn<Salle, String> depart = Colonnes.texte("", 24, s -> s == m.getSalleDepart() ? "★" : "");
        depart.setMaxWidth(30);
        depart.setSortable(false);
        depart.setStyle("-fx-alignment: CENTER;");

        getColumns().addAll(java.util.List.of(
                depart,
                Colonnes.image("Image", UtilEditor.ImageType.SALLE, Salle::getImage),
                Colonnes.texte("Nom", 90, Salle::getNom),
                Colonnes.texte("Description", 150, Salle::getDescription)));
        setItems(m.getSalles());
        Colonnes.rafraichirAuxModifications(this);
    }
}
