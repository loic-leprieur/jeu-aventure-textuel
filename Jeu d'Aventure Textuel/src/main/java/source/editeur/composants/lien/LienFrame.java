package source.editeur.composants.lien;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import source.editeur.composants.FormulaireFrame;
import source.editeur.modele.Lien;
import source.editeur.modele.Salle;
import source.editeur.modele.Textes;
import source.moteur.Direction;

/**
 * Classe LienFrame
 * Fenêtre permettant d'ajouter ou de modifier un passage entre deux salles
 */
public class LienFrame extends FormulaireFrame<Lien> {

    private final ComboBox<Salle> depart = new ComboBox<>();
    private final ComboBox<Direction> direction = new ComboBox<>();
    private final ComboBox<Salle> arrivee = new ComboBox<>();
    private final CheckBox retour = new CheckBox();

    public LienFrame() {
        super("un lien");
        depart.setPromptText("Salle de départ");
        arrivee.setPromptText("Salle d'arrivée");
        depart.setMaxWidth(Double.MAX_VALUE);
        arrivee.setMaxWidth(Double.MAX_VALUE);
        direction.getItems().setAll(Direction.values());
        direction.setMaxWidth(Double.MAX_VALUE);
        direction.setConverter(new StringConverter<>() {
            @Override
            public String toString(Direction d) {
                return Textes.libelle(d);
            }

            @Override
            public Direction fromString(String s) {
                return null;
            }
        });
        direction.valueProperty().addListener(o -> actualiserTexteRetour());
        depart.valueProperty().addListener(o -> actualiserTexteRetour());
        arrivee.valueProperty().addListener(o -> actualiserTexteRetour());

        ligne("Depuis la salle", depart);
        ligne("Aller vers le", direction);
        ligne("Mène à la salle", arrivee);
        ligne("", retour);
    }

    private void actualiserTexteRetour() {
        Direction d = direction.getValue();
        retour.setText("Créer aussi le passage retour"
                + (d == null ? "" : " (" + Textes.libelle(Lien.oppose(d)).toLowerCase()
                + (arrivee.getValue() == null || depart.getValue() == null ? ")"
                : " : " + arrivee.getValue() + " → " + depart.getValue() + ")")));
    }

    @Override
    protected void actualiserListes() {
        depart.getItems().setAll(modele.getSalles());
        arrivee.getItems().setAll(modele.getSalles());
    }

    @Override
    protected void vider() {
        depart.setValue(null);
        direction.setValue(Direction.NORD);
        arrivee.setValue(null);
        retour.setSelected(true);
        retour.setVisible(true);
        retour.setManaged(true);
    }

    @Override
    protected void remplir(Lien l) {
        depart.setValue(l.getDepart());
        direction.setValue(l.getDirection());
        arrivee.setValue(l.getArrivee());
        retour.setSelected(false);
        retour.setVisible(false);
        retour.setManaged(false);
    }

    /**
     * @return Vrai si un autre lien part déjà de cette salle dans cette direction
     */
    private boolean directionPrise(Salle s, Direction d, Lien exclu) {
        return modele.getLiens().stream().anyMatch(l -> l != exclu && l.getDepart() == s && l.getDirection() == d);
    }

    @Override
    protected String verifier(Lien existant) {
        if (modele.getSalles().size() < 2) {
            return "Il faut au moins deux salles pour créer un lien.";
        }
        if (depart.getValue() == null || arrivee.getValue() == null || direction.getValue() == null) {
            return "Choisissez la salle de départ, la direction et la salle d'arrivée.";
        }
        if (depart.getValue() == arrivee.getValue()) {
            return "La salle d'arrivée doit être différente de la salle de départ.";
        }
        if (directionPrise(depart.getValue(), direction.getValue(), existant)) {
            return "Il existe déjà un passage vers le " + Textes.libelle(direction.getValue()).toLowerCase()
                    + " depuis « " + depart.getValue() + " ».";
        }
        if (existant == null && retour.isSelected()
                && directionPrise(arrivee.getValue(), Lien.oppose(direction.getValue()), null)) {
            return "Impossible de créer le passage retour : il existe déjà un passage vers le "
                    + Textes.libelle(Lien.oppose(direction.getValue())).toLowerCase()
                    + " depuis « " + arrivee.getValue() + " ». Décochez « passage retour ».";
        }
        return null;
    }

    @Override
    protected void enregistrer(Lien existant) {
        Lien l = existant == null ? new Lien() : existant;
        l.setDepart(depart.getValue());
        l.setDirection(direction.getValue());
        l.setArrivee(arrivee.getValue());
        if (existant == null) {
            modele.getLiens().add(l);
            if (retour.isSelected()) {
                modele.getLiens().add(new Lien(arrivee.getValue(), Lien.oppose(direction.getValue()), depart.getValue()));
            }
        }
    }
}
