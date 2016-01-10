package source;

import source.moteur.Direction;
import source.moteur.exception.ObjetDejaExistantDansSalleException;
import source.moteur.exception.ObjetPasDansSalleException;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe correspondant a une salle
 */
public class Salle {

    private String nom;
    private String description;
    private BufferedImage image;
    private List<Objet> objets;
    private Map<Direction,Salle> liens;

    /**
     * Constructeur d'une Salle simple;
     * @param nom Nom salle
     * @param description Description salle
     * @param image Image salle
     */
    public Salle(String nom,String description,BufferedImage image){
        this.nom = nom;
        this.description = description;
        this.image = image;
        this.objets = new ArrayList<>();
        this.liens = new HashMap<>();
    }

    /**
     * Constructeur d'une Salle complexe
     * @param nom Nom salle
     * @param description Description salle
     * @param image Image salle
     * @param objets Objet de la salle
     * @param liens Lien qui relie les salles
     */
    public Salle(String nom,String description,BufferedImage image,List<Objet> objets, Map<Direction,Salle> liens){
        this.nom = nom;
        this.description = description;
        this.image = image;
        this.objets = objets;
        this.liens = liens;
    }

    /**
     * Se déplace en fonction de la direction
     * @param direction Direction a prendre
     * @return Si le déplacement est effectué
     */
    public boolean aller(Direction direction) {
        boolean res = false;
        if(liens.containsKey(direction)){
            liens.get(direction).afficher();
            res = true;
        }
        return res;
    }

    /**
     * Affiche une salle
     */
    public void afficher(){
        //TODO:Afficher un objet
    }

    /**
     * Affiche un objet dans la salle
     * @param objet objet a afficher
     * @throws ObjetPasDansSalleException
     */
    public void afficherObjet(Objet objet) throws ObjetPasDansSalleException {
        if(objets.contains(objet)){
            objet.afficher();
        }else{
            throw new ObjetPasDansSalleException(objet.getNom() + " n'est pas présent dans la salle " + this.nom);
        }
    }

    /**
     * Cache un objet dans la salle
     * @param objet
     * @throws ObjetPasDansSalleException
     */
    public void cacherObjet(Objet objet) throws ObjetPasDansSalleException {
        if(objets.contains(objet)){
            objet.cacher();
        }else{
            throw new ObjetPasDansSalleException(objet.getNom() + " n'est pas présent dans la salle " + this.nom);
        }
    }

    /**
     * Ajoute un objet à la salle
     * @param objet Objet a ajoute
     * @throws ObjetDejaExistantDansSalleException
     */
    public void ajouterObjet(Objet objet) throws ObjetDejaExistantDansSalleException {
        if(!objets.contains(objet)){
            this.objets.add(objet);
        }else{
            throw new ObjetDejaExistantDansSalleException(objet.getNom() + " est déjà présent dans cette salle");
        }
    }

    /**
     * Retire un obejt de la salle
     * @param objet Objet a retirer
     * @throws ObjetPasDansSalleException
     */
    public void retirerObjet(Objet objet) throws ObjetPasDansSalleException{
        if(objets.contains(objet)){
            int index = this.objets.indexOf(objet);
            this.objets.remove(index);
        }else{
            throw new ObjetPasDansSalleException(objet.getNom() + " n'est pas présent dans la salle " + this.nom);
        }
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public BufferedImage getImage() {
        return image;
    }

    public List<Objet> getObjets() {
        return objets;
    }

    public Map<Direction, Salle> getLiens() {
        return liens;
    }
}
