package source.editeur.action.table;

import javafx.scene.control.TableView;
import source.editeur.modele.Lien;
import source.editeur.modele.ModeleJeu;
import source.editeur.modele.Textes;

import java.util.List;

/**
 * Classe LienTable
 */
public class LienTable extends TableView<Lien> {

    public LienTable() {
        getColumns().addAll(List.of(
                Colonnes.texte("Depuis", 100, l -> Textes.nom(l.getDepart(), "?")),
                Colonnes.texte("Direction", 70, l -> Textes.libelle(l.getDirection())),
                Colonnes.texte("Vers", 100, l -> Textes.nom(l.getArrivee(), "?"))));
        setItems(ModeleJeu.get().getLiens());
        Colonnes.rafraichirAuxModifications(this);
    }
}
