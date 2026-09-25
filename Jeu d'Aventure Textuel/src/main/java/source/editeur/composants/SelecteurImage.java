package source.editeur.composants;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import source.editeur.images.GestionImages;
import source.editeur.images.ObservableListImage;
import source.util.UtilEditor;

import java.io.File;
import java.io.IOException;

/**
 * Choix d'une image : liste des images disponibles (avec miniatures),
 * bouton pour importer une image depuis le disque, et aperçu
 */
public class SelecteurImage extends VBox {

    private static final double TAILLE_MINIATURE = 32;

    private final UtilEditor.ImageType type;
    private final ComboBox<String> choix;
    private final ImageView apercu = new ImageView();
    private final Label sansImage = new Label("Aucune image");

    /**
     * @param type Objet ou Salle
     * @param tailleApercu Taille de l'aperçu en pixels
     */
    public SelecteurImage(UtilEditor.ImageType type, double tailleApercu) {
        this.type = type;
        setSpacing(6);

        choix = new ComboBox<>(ObservableListImage.liste(type));
        choix.setPromptText("Choisir une image");
        choix.setMaxWidth(Double.MAX_VALUE);
        choix.setCellFactory(l -> new CelluleImage(type));
        choix.setButtonCell(new CelluleImage(type));
        HBox.setHgrow(choix, Priority.ALWAYS);

        Button importer = new Button("Importer…");
        importer.setTooltip(new Tooltip("Ajouter une image depuis votre ordinateur"));
        importer.setOnAction(e -> importer());

        apercu.setFitWidth(tailleApercu);
        apercu.setFitHeight(tailleApercu);
        apercu.setPreserveRatio(true);
        StackPane cadre = new StackPane(sansImage, apercu);
        cadre.getStyleClass().add("apercu-image");
        cadre.setMinSize(tailleApercu + 8, tailleApercu + 8);
        cadre.setMaxSize(tailleApercu + 8, tailleApercu + 8);
        cadre.setAlignment(Pos.CENTER);

        choix.valueProperty().addListener((o, a, n) -> actualiserApercu());

        getChildren().addAll(new HBox(6, choix, importer), cadre);
    }

    private void importer() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Importer une image");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images",
                GestionImages.EXTENSIONS.stream().map(e -> "*." + e).toList()));
        File f = fc.showOpenDialog(getScene() == null ? null : getScene().getWindow());
        if (f == null) {
            return;
        }
        try {
            setValeur(GestionImages.importer(f, type));
        } catch (IOException ex) {
            Dialogues.erreur(getScene().getWindow(), "Impossible d'importer l'image", ex.getMessage());
        }
    }

    private void actualiserApercu() {
        apercu.setImage(GestionImages.charger(type, choix.getValue(), apercu.getFitWidth() * 2));
        sansImage.setVisible(apercu.getImage() == null);
        if (choix.getValue() != null && apercu.getImage() == null) {
            sansImage.setText("Image introuvable");
        } else {
            sansImage.setText("Aucune image");
        }
    }

    public String getValeur() {
        return choix.getValue() == null ? "" : choix.getValue();
    }

    public void setValeur(String nom) {
        choix.setValue(nom == null || nom.isEmpty() ? null : nom);
        actualiserApercu();
    }

    /**
     * Cellule de liste affichant la miniature d'une image et son nom
     */
    public static class CelluleImage extends ListCell<String> {

        private final UtilEditor.ImageType type;
        private final ImageView vue = new ImageView();

        public CelluleImage(UtilEditor.ImageType type) {
            this.type = type;
            vue.setFitWidth(TAILLE_MINIATURE);
            vue.setFitHeight(TAILLE_MINIATURE);
            vue.setPreserveRatio(true);
        }

        @Override
        protected void updateItem(String nom, boolean vide) {
            super.updateItem(nom, vide);
            if (vide || nom == null) {
                setText(null);
                setGraphic(null);
            } else {
                setText(nom);
                vue.setImage(GestionImages.charger(type, nom, TAILLE_MINIATURE * 2));
                setGraphic(vue);
            }
        }
    }
}
