package source.editeur.modele;

import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Objet du jeu, défini par un nom, une description et une image
 */
public class Objet implements ElementNomme {

    //Nom de l'objet
    private final StringProperty nom = new SimpleStringProperty(this, "nom", "");
    //Description de l'objet
    private final StringProperty description = new SimpleStringProperty(this, "description", "");
    //Le joueur peut-il prendre l'objet
    private final BooleanProperty prenable = new SimpleBooleanProperty(this, "prenable", false);
    //Nom du fichier image de l'objet (dossier images/objets)
    private final StringProperty image = new SimpleStringProperty(this, "image", "");

    public Objet() {
    }

    /**
     * Constructeur d'un Objet
     * @param nom Nom de l'objet
     * @param description Description de l'objet
     * @param prenable Prenabilité de l'objet
     * @param image Nom du fichier image de l'objet
     */
    public Objet(String nom, String description, boolean prenable, String image) {
        setNom(nom);
        setDescription(description);
        setPrenable(prenable);
        setImage(image);
    }

    @Override
    public StringProperty nomProperty() { return nom; }
    public StringProperty descriptionProperty() { return description; }
    public BooleanProperty prenableProperty() { return prenable; }
    public StringProperty imageProperty() { return image; }

    public String getDescription() { return description.get(); }
    public boolean isPrenable() { return prenable.get(); }
    public String getImage() { return image.get(); }

    public void setNom(String nom) { this.nom.set(Textes.nettoyer(nom)); }
    public void setDescription(String description) { this.description.set(Textes.nettoyer(description)); }
    public void setPrenable(boolean prenable) { this.prenable.set(prenable); }
    public void setImage(String image) { this.image.set(Textes.nettoyer(image)); }

    @Override
    public Observable[] proprietes() {
        return new Observable[]{nom, description, prenable, image};
    }

    @Override
    public String toString() {
        return getNom();
    }
}
