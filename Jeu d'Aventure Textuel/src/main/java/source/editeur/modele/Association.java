package source.editeur.modele;

import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Place un objet dans une salle, à une position exprimée en pourcentage
 * de la largeur (x) et de la hauteur (y) de l'image de la salle
 */
public class Association implements ElementJeu {

    public static final int POSITION_MAX = 100;

    private final ObjectProperty<Salle> salle = new SimpleObjectProperty<>(this, "salle");
    private final ObjectProperty<Objet> objet = new SimpleObjectProperty<>(this, "objet");
    private final IntegerProperty x = new SimpleIntegerProperty(this, "x", POSITION_MAX / 2);
    private final IntegerProperty y = new SimpleIntegerProperty(this, "y", POSITION_MAX / 2);
    //L'objet est-il visible au début du jeu
    private final BooleanProperty visible = new SimpleBooleanProperty(this, "visible", true);

    public Association() {
    }

    public Association(Salle salle, Objet objet, int x, int y, boolean visible) {
        setSalle(salle);
        setObjet(objet);
        setX(x);
        setY(y);
        setVisible(visible);
    }

    public ObjectProperty<Salle> salleProperty() { return salle; }
    public ObjectProperty<Objet> objetProperty() { return objet; }
    public IntegerProperty xProperty() { return x; }
    public IntegerProperty yProperty() { return y; }
    public BooleanProperty visibleProperty() { return visible; }

    public Salle getSalle() { return salle.get(); }
    public Objet getObjet() { return objet.get(); }
    public int getX() { return x.get(); }
    public int getY() { return y.get(); }
    public boolean isVisible() { return visible.get(); }

    public void setSalle(Salle salle) { this.salle.set(salle); }
    public void setObjet(Objet objet) { this.objet.set(objet); }
    public void setX(int x) { this.x.set(borner(x)); }
    public void setY(int y) { this.y.set(borner(y)); }
    public void setVisible(boolean visible) { this.visible.set(visible); }

    private static int borner(int v) {
        return Math.max(0, Math.min(POSITION_MAX, v));
    }

    @Override
    public Observable[] proprietes() {
        return new Observable[]{salle, objet, x, y, visible};
    }

    @Override
    public String toString() {
        return getObjet() + " dans " + getSalle();
    }
}
