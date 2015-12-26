package composants.objet;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Classe composants.Objet
 */
public class Objet implements Comparable<Objet>{

    //Nom de l'objet
    private SimpleStringProperty  nom;
    //Description de l'objet
    private SimpleStringProperty description;
    //Prenabilité de l'objet
    private SimpleBooleanProperty prenable;
    //Lien vers l'image de l'objet
    private SimpleStringProperty  image;

    /**
     * Constructeur d'un composants.Objet
     * @param nom Nom de l'objet
     * @param description Description de l'objet
     * @param prenable Prenabilité de l'objet
     * @param image Lien vers Image de l'objet
     */
    public Objet(String nom,String description,boolean prenable,String image){
        this.nom = new SimpleStringProperty(nom.trim());
        this.description = new SimpleStringProperty(description.trim());
        this.prenable = new SimpleBooleanProperty(prenable);
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
     * @return boolean
     */
    public boolean isPrenable() {
        return prenable.get();
    }

    @Override
    public String toString(){
        return getNom() + ":" + getDescription() + ":" + isPrenable() + ":" + getImage();
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
