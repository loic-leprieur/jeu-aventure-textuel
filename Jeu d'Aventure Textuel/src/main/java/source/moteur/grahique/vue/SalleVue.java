package source.moteur.grahique.vue;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import source.Niveau;
import source.Objet;
import source.Salle;
import source.editeur.modele.Association;

import java.util.Observable;
import java.util.Observer;
import java.util.function.Consumer;

/**
 * Image de la salle actuelle, avec les objets visibles posés dessus.
 * Un clic sur un objet l'examine.
 */
public class SalleVue extends Pane implements Observer {

    /** Taille d'un objet, en proportion de la largeur de l'image de la salle */
    private static final double TAILLE_OBJET = 0.12;
    private static final double TAILLE_OBJET_MIN = 24;

    private final Consumer<String> commande;
    private Niveau niveau;

    /**
     * @param commande Exécute une commande (clic sur un objet : "regarder objet")
     */
    public SalleVue(Consumer<String> commande){
        this.commande = commande;
        getStyleClass().add("salle-vue");
        setMinSize(0, 0);
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(widthProperty());
        clip.heightProperty().bind(heightProperty());
        setClip(clip);
        widthProperty().addListener(o -> dessiner());
        heightProperty().addListener(o -> dessiner());
    }

    @Override
    public void update(Observable o, Object arg) {
        this.niveau = (Niveau) o;
        dessiner();
    }

    private void dessiner(){
        getChildren().clear();
        if(niveau == null || getWidth() <= 0 || getHeight() <= 0){
            return;
        }
        Salle salle = niveau.getSalleActuel();
        Image fond = salle.getImage();
        double largeurImage = fond == null || fond.getWidth() <= 0 ? 4 : fond.getWidth();
        double hauteurImage = fond == null || fond.getHeight() <= 0 ? 3 : fond.getHeight();
        double echelle = Math.min(getWidth() / largeurImage, getHeight() / hauteurImage);
        double l = largeurImage * echelle;
        double h = hauteurImage * echelle;
        double ox = (getWidth() - l) / 2;
        double oy = (getHeight() - h) / 2;

        if(fond != null){
            ImageView vue = new ImageView(fond);
            vue.setFitWidth(l);
            vue.setFitHeight(h);
            vue.relocate(ox, oy);
            getChildren().add(vue);
        }
        Label titre = new Label(salle.getNom());
        titre.getStyleClass().add("salle-titre");
        titre.relocate(ox + 8, oy + 8);
        getChildren().add(titre);

        double taille = Math.max(TAILLE_OBJET_MIN, l * TAILLE_OBJET);
        for(Objet objet : salle.getObjetsVisibles()){
            ImageView image = new ImageView(objet.getImage());
            image.setFitWidth(taille);
            image.setFitHeight(taille);
            image.setPreserveRatio(true);
            VBox boite = new VBox(image);
            boite.setAlignment(Pos.CENTER);
            boite.setPrefSize(taille, taille);
            if(objet.getImage() == null){
                boite.getChildren().setAll(new Label(objet.getNom()));
            }
            boite.setCursor(Cursor.HAND);
            Tooltip.install(boite, new Tooltip(objet.getNom() + " (cliquer pour examiner)"));
            boite.setOnMouseClicked(e -> commande.accept("regarder " + objet.getNom()));
            boite.relocate(ox + objet.getX() * l / Association.POSITION_MAX - taille / 2,
                    oy + objet.getY() * h / Association.POSITION_MAX - taille / 2);
            getChildren().add(boite);
        }
    }
}
