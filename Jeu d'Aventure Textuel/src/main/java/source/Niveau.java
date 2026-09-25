package source;

import source.moteur.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

/**
 * Classe niveau : état d'une partie en cours
 * (salle actuelle, inventaire, variables, journal des messages).
 * Les vues sont prévenues de chaque changement (motif Observateur).
 */
public class Niveau extends Observable{

    private String nom;
    private Salle salleActuel;
    private List<Salle> salles;
    private ArrayList<String> log;
    private final List<Objet> inventaire = new ArrayList<>();
    //Valeurs actuelles des variables du jeu, par nom
    private final Map<String, String> variables = new LinkedHashMap<>();

    /**
     * Constructeur Niveau
     * @param nom Nom du niveau
     * @param salles List de salle
     */
    public Niveau(String nom,List<Salle> salles){
        this(nom, salles, salles.get(0));
    }

    /**
     * Constructeur Niveau
     * @param nom Nom du niveau
     * @param salles List de salle
     * @param depart Salle de départ du joueur
     */
    public Niveau(String nom, List<Salle> salles, Salle depart){
        this.nom = nom;
        this.salles = salles;
        this.salleActuel = depart;
        this.log = new ArrayList<>();
    }

    /**
     * Se déplace dans une salle
     * @param direction Direction
     * @return Deplacement effectué
     */
    public boolean deplacer(Direction direction) {
        Salle suivante = getSalle(direction);
        if(suivante != null){
            setSalleActuel(suivante);
        }
        return suivante != null;
    }

    /**
     * Retourne la salle en fonction de la direction
     * @param direction Direction
     * @return Salle, null s'il n'y a pas de passage dans cette direction
     */
    public Salle getSalle(Direction direction){
        return this.salleActuel.getLiens().get(direction);
    }

    /**
     * Retourne une salle par son nom (sans tenir compte de la casse)
     * @return Salle, null si elle n'existe pas
     */
    public Salle getSalle(String nomSalle){
        for(Salle s : salles){
            if(s.getNom().equalsIgnoreCase(nomSalle)){
                return s;
            }
        }
        return null;
    }

    public void setSalleActuel(Salle salle){
        this.salleActuel = salle;
        changer();
    }

    /**
     * Ajoute les logs
     * @param str Phrase
     */
    public void ajouterLog(String str){
        log.add(str);
        changer();
    }

    /**
     * Prévient les vues d'un changement
     */
    public void changer(){
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * @return Valeur actuelle d'une variable, null si elle n'existe pas
     */
    public String getVariable(String nomVariable){
        for(Map.Entry<String, String> e : variables.entrySet()){
            if(e.getKey().equalsIgnoreCase(nomVariable)){
                return e.getValue();
            }
        }
        return null;
    }

    public void setVariable(String nomVariable, String valeur){
        for(String cle : variables.keySet()){
            if(cle.equalsIgnoreCase(nomVariable)){
                variables.put(cle, valeur);
                changer();
                return;
            }
        }
        variables.put(nomVariable, valeur);
        changer();
    }

    public Map<String, String> getVariables(){
        return Collections.unmodifiableMap(variables);
    }

    /**
     * @return Objets portés par le joueur
     */
    public List<Objet> getInventaire(){
        return inventaire;
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

    public ArrayList<String> getLog(){
        return this.log;
    }
}
