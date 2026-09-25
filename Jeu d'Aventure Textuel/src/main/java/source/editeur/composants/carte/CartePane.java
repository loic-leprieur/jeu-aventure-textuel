package source.editeur.composants.carte;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import source.editeur.images.GestionImages;
import source.editeur.images.ObservableListImage;
import source.editeur.modele.Association;
import source.editeur.modele.Lien;
import source.editeur.modele.ModeleJeu;
import source.editeur.modele.Objet;
import source.editeur.modele.Salle;
import source.editeur.modele.Textes;
import source.util.UtilEditor;

import java.util.List;
import java.util.function.Consumer;

/**
 * Classe CartePane
 * Aperçu d'une salle : image de fond, objets placés (déplaçables à la souris) et sorties
 */
public class CartePane extends BorderPane {

    /** Taille d'un objet sur la carte, en proportion de la largeur de l'image de la salle */
    private static final double TAILLE_OBJET = 0.14;
    private static final double TAILLE_OBJET_MIN = 24;
    /** Largeur réservée au nom sous l'objet, en proportion de la taille de l'image */
    private static final double LARGEUR_ETIQUETTE = 1.6;

    private final ModeleJeu modele = ModeleJeu.get();
    private final ComboBox<Salle> choix = new ComboBox<>();
    private final Pane zone = new Pane();
    private final Label sorties = new Label();
    private final Consumer<Association> modifierPlacement;

    //Vrai pendant qu'un objet est déplacé : on ne redessine pas la carte
    private boolean glissement = false;

    /**
     * @param placerObjet Appelé pour placer un nouvel objet dans la salle affichée
     * @param modifierPlacement Appelé sur double-clic d'un objet de la carte
     */
    public CartePane(Consumer<Salle> placerObjet, Consumer<Association> modifierPlacement) {
        this.modifierPlacement = modifierPlacement;

        choix.setItems(modele.getSalles());
        choix.setPromptText("Choisir une salle");
        choix.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(choix, Priority.ALWAYS);
        Button placer = new Button("Placer un objet…");
        placer.setTooltip(new Tooltip("Ajouter un objet dans cette salle"));
        placer.disableProperty().bind(choix.valueProperty().isNull());
        placer.setOnAction(e -> placerObjet.accept(choix.getValue()));
        HBox haut = new HBox(6, choix, placer);
        haut.setPadding(new Insets(0, 0, 6, 0));
        setTop(haut);

        zone.setMinSize(0, 0);
        zone.getStyleClass().add("carte");
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(zone.widthProperty());
        clip.heightProperty().bind(zone.heightProperty());
        zone.setClip(clip);
        setCenter(zone);

        sorties.setWrapText(true);
        sorties.getStyleClass().add("texte-aide");
        sorties.setPadding(new Insets(4, 0, 0, 0));
        setBottom(sorties);

        choix.valueProperty().addListener(o -> dessiner());
        zone.widthProperty().addListener(o -> dessiner());
        zone.heightProperty().addListener(o -> dessiner());
        modele.versionProperty().addListener(o -> dessiner());
        ObservableListImage.imageObjetList.addListener((javafx.beans.Observable o) -> dessiner());
        ObservableListImage.imageSalleList.addListener((javafx.beans.Observable o) -> dessiner());
    }

    /**
     * Affiche une salle
     */
    public void afficher(Salle s) {
        if (s != null) {
            choix.setValue(s);
        }
    }

    private void dessiner() {
        if (glissement) {
            return;
        }
        zone.getChildren().clear();
        Salle s = choix.getValue();
        if (s != null && !modele.getSalles().contains(s)) {
            choix.setValue(null);
            return;
        }
        if (s == null) {
            sorties.setText("");
            message(modele.getSalles().isEmpty()
                    ? "Créez une salle pour la voir ici."
                    : "Sélectionnez une salle dans la liste ou ci-dessus.");
            return;
        }
        sorties.setText(texteSorties(s));

        double largeurZone = zone.getWidth();
        double hauteurZone = zone.getHeight();
        if (largeurZone <= 0 || hauteurZone <= 0) {
            return;
        }

        //Image de fond, centrée et agrandie au maximum sans déformation
        Image fond = GestionImages.charger(UtilEditor.ImageType.SALLE, s.getImage(), 0);
        double largeurImage = fond == null ? 4 : fond.getWidth();
        double hauteurImage = fond == null ? 3 : fond.getHeight();
        double echelle = Math.min(largeurZone / largeurImage, hauteurZone / hauteurImage);
        double l = largeurImage * echelle;
        double h = hauteurImage * echelle;
        double ox = (largeurZone - l) / 2;
        double oy = (hauteurZone - h) / 2;
        Cadre cadre = new Cadre(ox, oy, l, h);

        if (fond != null) {
            ImageView vue = new ImageView(fond);
            vue.setFitWidth(l);
            vue.setFitHeight(h);
            vue.relocate(ox, oy);
            zone.getChildren().add(vue);
        } else {
            Rectangle r = new Rectangle(ox, oy, l, h);
            r.getStyleClass().add("carte-sans-image");
            zone.getChildren().add(r);
            Label manque = new Label("Image de la salle introuvable : " + s.getImage());
            manque.relocate(ox + 8, oy + 8);
            zone.getChildren().add(manque);
        }

        for (Association a : modele.associationsDe(s)) {
            if (a.getObjet() != null) {
                zone.getChildren().add(creerObjet(a, cadre));
            }
        }
    }

    private void message(String texte) {
        Label l = new Label(texte);
        l.getStyleClass().add("texte-aide");
        l.setWrapText(true);
        l.setAlignment(Pos.CENTER);
        l.prefWidthProperty().bind(zone.widthProperty());
        l.prefHeightProperty().bind(zone.heightProperty());
        zone.getChildren().add(l);
    }

    private String texteSorties(Salle s) {
        List<Lien> liens = modele.liensDepuis(s);
        String depart = s == modele.getSalleDepart() ? "★ Salle de départ. " : "";
        if (liens.isEmpty()) {
            return depart + "Aucune sortie (onglet Lien pour en ajouter).";
        }
        StringBuilder sb = new StringBuilder(depart).append("Sorties : ");
        for (int i = 0; i < liens.size(); i++) {
            Lien l = liens.get(i);
            sb.append(i > 0 ? " · " : "").append(Textes.libelle(l.getDirection())).append(" → ")
                    .append(Textes.nom(l.getArrivee(), "?"));
        }
        return sb.toString();
    }

    /** Rectangle occupé par l'image de la salle dans la zone */
    private record Cadre(double x, double y, double largeur, double hauteur) {

        Point2D versZone(int px, int py) {
            return new Point2D(x + px * largeur / Association.POSITION_MAX, y + py * hauteur / Association.POSITION_MAX);
        }

        int versPourcentX(double zx) {
            return (int) Math.round(Math.max(0, Math.min(1, (zx - x) / largeur)) * Association.POSITION_MAX);
        }

        int versPourcentY(double zy) {
            return (int) Math.round(Math.max(0, Math.min(1, (zy - y) / hauteur)) * Association.POSITION_MAX);
        }
    }

    /**
     * Crée l'affichage d'un objet placé dans la salle, déplaçable à la souris
     */
    private VBox creerObjet(Association a, Cadre cadre) {
        Objet o = a.getObjet();
        double taille = Math.max(TAILLE_OBJET_MIN, cadre.largeur() * TAILLE_OBJET);

        ImageView vue = new ImageView(GestionImages.charger(UtilEditor.ImageType.OBJET, o.getImage(), 128));
        vue.setFitWidth(taille);
        vue.setFitHeight(taille);
        vue.setPreserveRatio(true);
        Label nom = new Label(o.getNom());
        nom.getStyleClass().add("carte-nom-objet");
        VBox objet = new VBox(2, vue, nom);
        double largeur = taille * LARGEUR_ETIQUETTE;
        objet.setMinWidth(largeur);
        objet.setPrefWidth(largeur);
        objet.setMaxWidth(largeur);
        nom.setMaxWidth(largeur);
        objet.setAlignment(Pos.CENTER);
        objet.setCursor(Cursor.MOVE);
        objet.setOpacity(a.isVisible() ? 1 : 0.5);
        Tooltip.install(objet, new Tooltip(o.getNom() + (a.isVisible() ? "" : " (caché au début du jeu)")
                + "\nGlisser pour déplacer, double-clic pour modifier"));

        //Le centre de l'image est placé à la position de l'objet
        Point2D centre = cadre.versZone(a.getX(), a.getY());
        placer(objet, centre, largeur, taille);

        //Écart entre le point saisi à la souris et le centre de l'objet, conservé pendant le déplacement
        Point2D[] ecart = {Point2D.ZERO};
        objet.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                glissement = true;
                ecart[0] = zone.sceneToLocal(e.getSceneX(), e.getSceneY()).subtract(cadre.versZone(a.getX(), a.getY()));
                objet.toFront();
                e.consume();
            }
        });
        objet.setOnMouseDragged(e -> {
            if (glissement) {
                Point2D p = zone.sceneToLocal(e.getSceneX(), e.getSceneY()).subtract(ecart[0]);
                placer(objet, cadre.versZone(cadre.versPourcentX(p.getX()), cadre.versPourcentY(p.getY())), largeur, taille);
                e.consume();
            }
        });
        objet.setOnMouseReleased(e -> {
            if (glissement) {
                glissement = false;
                Point2D p = zone.sceneToLocal(e.getSceneX(), e.getSceneY()).subtract(ecart[0]);
                //Si l'objet a bougé, le modèle change et la carte est redessinée
                a.setX(cadre.versPourcentX(p.getX()));
                a.setY(cadre.versPourcentY(p.getY()));
            }
        });
        objet.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                modifierPlacement.accept(a);
            }
        });
        return objet;
    }

    private static void placer(VBox objet, Point2D centre, double largeur, double taille) {
        objet.relocate(centre.getX() - largeur / 2, centre.getY() - taille / 2);
    }
}
