package source.editeur.modele;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Salle du jeu : un lieu avec une image d'arrière-plan
 */
public class Salle implements ElementNomme {

    private final StringProperty nom = new SimpleStringProperty(this, "nom", "");
    private final StringProperty description = new SimpleStringProperty(this, "description", "");
    //Nom du fichier image de la salle (dossier images/salles)
    private final StringProperty image = new SimpleStringProperty(this, "image", "");

    public Salle() {
    }

    public Salle(String nom, String description, String image) {
        setNom(nom);
        setDescription(description);
        setImage(image);
    }

    @Override
    public StringProperty nomProperty() { return nom; }
    public StringProperty descriptionProperty() { return description; }
    public StringProperty imageProperty() { return image; }

    public String getDescription() { return description.get(); }
    public String getImage() { return image.get(); }

    public void setNom(String nom) { this.nom.set(Textes.nettoyer(nom)); }
    public void setDescription(String description) { this.description.set(Textes.nettoyer(description)); }
    public void setImage(String image) { this.image.set(Textes.nettoyer(image)); }

    @Override
    public Observable[] proprietes() {
        return new Observable[]{nom, description, image};
    }

    @Override
    public String toString() {
        return getNom();
    }
}
