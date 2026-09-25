package source.editeur.modele;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import source.moteur.Direction;

/**
 * Passage entre deux salles : depuis la salle de départ, le joueur
 * peut aller dans la direction indiquée pour arriver dans la salle d'arrivée
 */
public class Lien implements ElementJeu {

    private final ObjectProperty<Salle> depart = new SimpleObjectProperty<>(this, "depart");
    private final ObjectProperty<Direction> direction = new SimpleObjectProperty<>(this, "direction", Direction.NORD);
    private final ObjectProperty<Salle> arrivee = new SimpleObjectProperty<>(this, "arrivee");

    public Lien() {
    }

    public Lien(Salle depart, Direction direction, Salle arrivee) {
        setDepart(depart);
        setDirection(direction);
        setArrivee(arrivee);
    }

    public ObjectProperty<Salle> departProperty() { return depart; }
    public ObjectProperty<Direction> directionProperty() { return direction; }
    public ObjectProperty<Salle> arriveeProperty() { return arrivee; }

    public Salle getDepart() { return depart.get(); }
    public Direction getDirection() { return direction.get(); }
    public Salle getArrivee() { return arrivee.get(); }

    public void setDepart(Salle depart) { this.depart.set(depart); }
    public void setDirection(Direction direction) { this.direction.set(direction); }
    public void setArrivee(Salle arrivee) { this.arrivee.set(arrivee); }

    /**
     * @param d Direction
     * @return Direction opposée (NORD -> SUD...)
     */
    public static Direction oppose(Direction d) {
        return switch (d) {
            case NORD -> Direction.SUD;
            case SUD -> Direction.NORD;
            case EST -> Direction.OUEST;
            case OUEST -> Direction.EST;
        };
    }

    @Override
    public Observable[] proprietes() {
        return new Observable[]{depart, direction, arrivee};
    }

    @Override
    public String toString() {
        return getDepart() + " -> " + Textes.libelle(getDirection()) + " -> " + getArrivee();
    }
}
