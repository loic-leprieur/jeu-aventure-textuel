package source.editeur.composants.salle;

import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import source.editeur.action.table.SalleTable;
import source.editeur.composants.ListePane;
import source.editeur.modele.ModeleJeu;
import source.editeur.modele.Salle;

/**
 * Classe SallePane
 * Liste des salles ; la salle de départ du joueur est marquée d'une étoile
 */
public class SallePane extends ListePane<Salle> {

    public SallePane() {
        super(new SalleTable(), new SalleFrame(),
                "Aucune salle. Cliquez sur Ajouter pour créer une salle avec son image de fond.");

        MenuItem depart = new MenuItem("Définir comme salle de départ ★");
        depart.setOnAction(e -> {
            Salle s = table.getSelectionModel().getSelectedItem();
            if (s != null) {
                ModeleJeu.get().setSalleDepart(s);
            }
        });
        menu.getItems().addAll(0, java.util.List.of(depart, new SeparatorMenuItem()));
    }
}
