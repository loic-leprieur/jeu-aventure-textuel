package source;

import source.moteur.Direction;
import source.moteur.exception.SalleInexistanteException;

import java.util.List;

/**
 * Classe niveau
 */
public class Niveau {

    private String nom;
    private Salle salleActuel;
    private List<Salle> salles;

    /**
     * Constructeur Niveau
     * @param nom Nom du niveau
     * @param salles List de salle
     */
    public Niveau(String nom,List<Salle> salles){
        this.nom = nom;
        this.salles = salles;
        this.salleActuel = salles.get(0);
    }

    /**
     * Se déplace dans une salle
     * @param direction Direction
     * @return Deplacement effectué
     */
    public boolean deplacer(Direction direction) {
        boolean res = false;
        if(this.salleActuel.aller(direction)){
            res = true;
            try {
                this.salleActuel = this.salleActuel.getSalle(direction);
            } catch (SalleInexistanteException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    /**
     * Retourne la salle en fonction de la direction
     * @param direction Direction
     * @return Salle
     */
    public Salle getSalle(Direction direction){
        try {
            return this.salleActuel.getSalle(direction);
        } catch (SalleInexistanteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getNom() {
        return nom;
    }

    public Salle getSalleActuel() {
        return salleActuel;
    }

    public List<Salle> getSalles() {
        return salles;
    }
}
