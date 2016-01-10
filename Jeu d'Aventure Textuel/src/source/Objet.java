package source;

import java.awt.image.BufferedImage;

/**
 * Classe correspondant a un objet
 */
public class Objet {

    private String nom;
    private String description;
    private boolean prenable;
    private boolean visible;
    private BufferedImage image;
    private int x;
    private int y;

    /**
     * Constructeur d'un objet simple
     * @param nom Nom objet
     * @param description Description objet
     * @param image Image objet
     */
    public Objet(String nom,String description,BufferedImage image){
        this.nom = nom;
        this.description = description;
        this.prenable = false;
        this.visible = true;
        this.image = image;
        this.x = 0;
        this.y = 0;
    }

    /**
     * Constructeur d'un objet complexe
     * @param nom Nom objet
     * @param description Description objet
     * @param prenable Prenabilité objet
     * @param visible Visibilité objet
     * @param image Image objet
     * @param x Position x objet
     * @param y Position y objet
     */
    public Objet(String nom,String description,boolean prenable,boolean visible,BufferedImage image,int x,int y){
        this.nom = nom;
        this.description = description;
        this.prenable = prenable;
        this.visible = visible;
        this.image = image;
        this.x = x;
        this.y = y;
    }

    /**
     * Cache l'objet
     */
    public void cacher(){
        this.visible = false;
    }

    /**
     * Affiche l'objet
     */
    public void afficher(){
        this.visible = true;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPrenable() {
        return prenable;
    }

    public boolean isVisible() {
        return visible;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
