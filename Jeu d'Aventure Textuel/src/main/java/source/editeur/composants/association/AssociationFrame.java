package source.editeur.composants.association;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Spinner;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import source.editeur.composants.FormulaireFrame;
import source.editeur.images.GestionImages;
import source.editeur.modele.Association;
import source.editeur.modele.Objet;
import source.editeur.modele.Salle;
import source.util.UtilEditor;

/**
 * Classe AssociationFrame
 * Fenêtre permettant de placer un objet dans une salle
 */
public class AssociationFrame extends FormulaireFrame<Association> {

    private final ComboBox<Objet> objet = new ComboBox<>();
    private final ComboBox<Salle> salle = new ComboBox<>();
    private final Spinner<Integer> x = new Spinner<>(0, Association.POSITION_MAX, Association.POSITION_MAX / 2, 5);
    private final Spinner<Integer> y = new Spinner<>(0, Association.POSITION_MAX, Association.POSITION_MAX / 2, 5);
    private final CheckBox visible = new CheckBox("Visible au début du jeu");

    public AssociationFrame() {
        super("un objet dans une salle");
        objet.setPromptText("Objet");
        objet.setMaxWidth(Double.MAX_VALUE);
        objet.setCellFactory(l -> new CelluleObjet());
        objet.setButtonCell(new CelluleObjet());
        salle.setPromptText("Salle");
        salle.setMaxWidth(Double.MAX_VALUE);
        x.setEditable(true);
        y.setEditable(true);
        x.setPrefWidth(80);
        y.setPrefWidth(80);

        ligne("Objet", objet);
        ligne("Dans la salle", salle);
        ligne("Position (%)", new HBox(6, new Label("gauche →"), x, new Label("haut ↓"), y));
        ligne("", visible);
        Label aide = new Label("Astuce : vous pouvez aussi déplacer l'objet à la souris sur la carte.");
        aide.getStyleClass().add("texte-aide");
        ligne("", aide);
    }

    /**
     * Présélectionne une salle (ajout depuis la carte)
     */
    public void preselectionnerSalle(Salle s) {
        salle.setValue(s);
    }

    @Override
    protected void actualiserListes() {
        objet.getItems().setAll(modele.getObjets());
        salle.getItems().setAll(modele.getSalles());
    }

    @Override
    protected void vider() {
        objet.setValue(null);
        if (!salle.getItems().contains(salle.getValue())) {
            salle.setValue(null);
        }
        x.getValueFactory().setValue(Association.POSITION_MAX / 2);
        y.getValueFactory().setValue(Association.POSITION_MAX / 2);
        visible.setSelected(true);
    }

    @Override
    protected void remplir(Association a) {
        objet.setValue(a.getObjet());
        salle.setValue(a.getSalle());
        x.getValueFactory().setValue(a.getX());
        y.getValueFactory().setValue(a.getY());
        visible.setSelected(a.isVisible());
    }

    /**
     * Prend en compte un nombre tapé au clavier sans avoir appuyé sur Entrée
     * @return Vrai si le texte est un nombre valide
     */
    private static boolean valider(Spinner<Integer> s) {
        try {
            s.commitValue();
            return true;
        } catch (RuntimeException e) {
            s.cancelEdit();
            return false;
        }
    }

    @Override
    protected String verifier(Association existant) {
        if (!valider(x) || !valider(y)) {
            return "La position doit être un nombre entre 0 et " + Association.POSITION_MAX + ".";
        }
        if (modele.getObjets().isEmpty() || modele.getSalles().isEmpty()) {
            return "Créez d'abord au moins un objet et une salle.";
        }
        if (objet.getValue() == null || salle.getValue() == null) {
            return "Choisissez l'objet et la salle.";
        }
        boolean doublon = modele.getAssociations().stream()
                .anyMatch(a -> a != existant && a.getObjet() == objet.getValue() && a.getSalle() == salle.getValue());
        if (doublon) {
            return "« " + objet.getValue() + " » est déjà placé dans « " + salle.getValue() + " ».";
        }
        return null;
    }

    @Override
    protected void enregistrer(Association existant) {
        Association a = existant == null ? new Association() : existant;
        a.setObjet(objet.getValue());
        a.setSalle(salle.getValue());
        a.setX(x.getValue());
        a.setY(y.getValue());
        a.setVisible(visible.isSelected());
        if (existant == null) {
            modele.getAssociations().add(a);
        }
    }

    /**
     * Cellule affichant la miniature et le nom d'un objet
     */
    private static class CelluleObjet extends ListCell<Objet> {
        private final ImageView vue = new ImageView();

        CelluleObjet() {
            vue.setFitWidth(28);
            vue.setFitHeight(28);
            vue.setPreserveRatio(true);
        }

        @Override
        protected void updateItem(Objet o, boolean vide) {
            super.updateItem(o, vide);
            if (vide || o == null) {
                setText(null);
                setGraphic(null);
            } else {
                setText(o.getNom());
                vue.setImage(GestionImages.charger(UtilEditor.ImageType.OBJET, o.getImage(), 56));
                setGraphic(vue);
            }
        }
    }
}
