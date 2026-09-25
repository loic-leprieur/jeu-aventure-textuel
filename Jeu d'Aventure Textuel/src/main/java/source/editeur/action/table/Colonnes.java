package source.editeur.action.table;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import source.editeur.images.GestionImages;
import source.editeur.modele.ModeleJeu;
import source.util.UtilEditor;

import java.util.function.Function;

/**
 * Création des colonnes des tableaux de l'éditeur
 */
public final class Colonnes {

    private static final double TAILLE_MINIATURE = 28;

    private Colonnes() {
    }

    /**
     * Colonne de texte calculé à partir de l'élément
     */
    public static <T> TableColumn<T, String> texte(String titre, double largeur, Function<T, String> valeur) {
        TableColumn<T, String> col = new TableColumn<>(titre);
        col.setCellValueFactory(c -> new ReadOnlyStringWrapper(valeur.apply(c.getValue())));
        col.setPrefWidth(largeur);
        col.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(String texte, boolean vide) {
                super.updateItem(texte, vide);
                setText(vide ? null : texte);
                setTooltip(vide || texte == null || texte.isEmpty() ? null : new Tooltip(texte));
            }
        });
        return col;
    }

    /**
     * Colonne affichant la miniature d'une image
     */
    public static <T> TableColumn<T, String> image(String titre, UtilEditor.ImageType type, Function<T, String> nomImage) {
        TableColumn<T, String> col = new TableColumn<>(titre);
        col.setCellValueFactory(c -> new ReadOnlyStringWrapper(nomImage.apply(c.getValue())));
        col.setPrefWidth(TAILLE_MINIATURE + 24);
        col.setMaxWidth(TAILLE_MINIATURE + 40);
        col.setSortable(false);
        col.setCellFactory(c -> new TableCell<>() {
            private final ImageView vue = new ImageView();
            {
                vue.setFitWidth(TAILLE_MINIATURE);
                vue.setFitHeight(TAILLE_MINIATURE);
                vue.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(String nom, boolean vide) {
                super.updateItem(nom, vide);
                if (vide || nom == null || nom.isEmpty()) {
                    setGraphic(null);
                    setText(null);
                    setTooltip(null);
                    return;
                }
                vue.setImage(GestionImages.charger(type, nom, TAILLE_MINIATURE * 2));
                setGraphic(vue.getImage() == null ? null : vue);
                setText(vue.getImage() == null ? "?" : null);
                setTooltip(new Tooltip(vue.getImage() == null ? nom + " (introuvable)" : nom));
            }
        });
        return col;
    }

    /**
     * Redessine le tableau à chaque modification du jeu
     * (ex : renommer une salle met à jour les liens qui l'affichent)
     */
    public static void rafraichirAuxModifications(TableView<?> table) {
        ModeleJeu.get().versionProperty().addListener(o -> table.refresh());
    }
}
