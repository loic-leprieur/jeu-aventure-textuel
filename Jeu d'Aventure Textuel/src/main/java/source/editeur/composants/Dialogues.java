package source.editeur.composants;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

import java.util.Optional;

/**
 * Boîtes de dialogue de l'éditeur
 */
public final class Dialogues {

    public static final ButtonType ENREGISTRER = new ButtonType("Enregistrer", ButtonBar.ButtonData.YES);
    public static final ButtonType NE_PAS_ENREGISTRER = new ButtonType("Ne pas enregistrer", ButtonBar.ButtonData.NO);

    private Dialogues() {
    }

    private static Alert creer(Alert.AlertType type, Window parent, String titre, String entete, String message) {
        Alert a = new Alert(type);
        if (parent != null) {
            a.initOwner(parent);
        }
        a.setTitle(titre);
        a.setHeaderText(entete);
        a.setContentText(message);
        return a;
    }

    public static void erreur(Window parent, String entete, String message) {
        creer(Alert.AlertType.ERROR, parent, "Erreur", entete, message).showAndWait();
    }

    public static void information(Window parent, String entete, String message) {
        creer(Alert.AlertType.INFORMATION, parent, "Information", entete, message).showAndWait();
    }

    /**
     * @return Vrai si l'utilisateur confirme
     */
    public static boolean confirmer(Window parent, String entete, String message) {
        Optional<ButtonType> r = creer(Alert.AlertType.CONFIRMATION, parent, "Confirmation", entete, message).showAndWait();
        return r.isPresent() && r.get() == ButtonType.OK;
    }

    /**
     * Demande s'il faut enregistrer les modifications
     * @return ENREGISTRER, NE_PAS_ENREGISTRER ou ButtonType.CANCEL
     */
    public static ButtonType demanderEnregistrement(Window parent) {
        Alert a = creer(Alert.AlertType.WARNING, parent, "Modifications non enregistrées",
                "Le jeu a été modifié.", "Voulez-vous enregistrer les modifications ?");
        a.getButtonTypes().setAll(ENREGISTRER, NE_PAS_ENREGISTRER, ButtonType.CANCEL);
        return a.showAndWait().orElse(ButtonType.CANCEL);
    }
}
