package source.editeur.action.table;

import javafx.scene.control.TableView;
import source.editeur.modele.ModeleJeu;
import source.editeur.modele.Regle;

import java.util.List;

/**
 * Tableau des règles du jeu
 */
public class RegleTable extends TableView<Regle> {

    public RegleTable() {
        getColumns().addAll(List.of(
                Colonnes.texte("Quand le joueur tape", 150, Regle::getCommande),
                Colonnes.texte("Conditions", 140, Regle::getResumeConditions),
                Colonnes.texte("Message affiché", 200, r -> r.getMessage().replace('\n', ' ')),
                Colonnes.texte("Effets", 180, Regle::getResumeEffets)));
        setItems(ModeleJeu.get().getRegles());
        Colonnes.rafraichirAuxModifications(this);
    }
}
