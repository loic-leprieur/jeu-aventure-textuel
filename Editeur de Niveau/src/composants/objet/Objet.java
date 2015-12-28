package composants.objet;

import javafx.beans.property.SimpleStringProperty;

/**
 * Classe Objet
 */
public class Objet implements Comparable<Objet>{

    //Nom de l'objet
    private SimpleStringProperty  nom;
    //Description de l'objet
    private SimpleStringProperty description;
    //Prenabilité de l'objet
    private SimpleStringProperty prenable;
    //Lien vers l'image de l'objet
    private SimpleStringProperty  image;


    /**
     * Constructeur d'un composants.Objet
     * @param nom Nom de l'objet
     * @param description Description de l'objet
     * @param prenable Prenabilité de l'objet
     * @param image Lien vers Image de l'objet
     */
    public Objet(String nom,String description,String prenable,String image){
        this.nom = new SimpleStringProperty(nom.trim());
        this.description = new SimpleStringProperty(description.trim());
        this.prenable = new SimpleStringProperty(prenable);
        this.image = new SimpleStringProperty(image.trim());
    }

    /**
     * Retourne le nom de l'objet
     * @return String
     */
    public String getNom() {
        return nom.get();
    }

    /**
     * Retourne la description de l'objet
     * @return String
     */
    public String getDescription() {
        return description.get();
    }

    /**
     * Retourne lien vers image de l'objet
     * @return String
     */
    public String getImage() {
        return image.get();
    }

    /**
     * Retourne si l'objet est prenable
     * @return String
     */
    public String getPrenable() {
        return prenable.get();
    }

    /**
     * Défini le nom de l'objet
     * @param nom Nom de l'objet
     */
    public void setNom(String nom) {
        this.nom.set(nom);
    }

    /**
     * Défini la description de l'objet
     * @param description Description de l'objet
     */
    public void setDescription(String description) {
        this.description.set(description);
    }

    /**
     * Défini si l'objet est prenable ou non
     * @param prenable Prenable ou non
     */
    public void setPrenable(String prenable) {
        this.prenable.set(prenable);
    }

    /**
     * Défini le nom de l'image
     * @param image Nom Image
     */
    public void setImage(String image) {
        this.image.set(image);
    }

    @Override
    public String toString(){
        return getNom() + ":" + getDescription() + ":" + getPrenable() + ":" + getImage();
    }

     @Override
     public boolean equals(Object obj){
         boolean res = false;
         if(obj instanceof Objet){
             Objet o = (Objet)obj;
             if(o.getNom().equalsIgnoreCase(this.getNom())){
                 res = true;
             }
         }
         return res;
     }

    @Override
    public int hashCode(){
        return this.getNom().hashCode();
    }

    @Override
    public int compareTo(Objet o) {
        return getNom().compareTo(o.getNom());
    }

}
