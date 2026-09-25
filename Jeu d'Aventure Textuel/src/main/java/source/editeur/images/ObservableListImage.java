package source.editeur.images;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import source.util.UtilEditor;

/**
 * Classe ObservableListImage contenant les ObservableList pour les images
 */
public class ObservableListImage {

    //ObservableList pour Objet
    public static ObservableList<String> imageObjetList = FXCollections.observableArrayList();
    //ObservableList pour Salle
    public static ObservableList<String> imageSalleList = FXCollections.observableArrayList();

    /**
     * Met à jour les ObservableList pour Objet et Salle
     */
    public static void rafraichirImage(){
        GestionImages.viderCache();
        imageObjetList.setAll(UtilEditor.getCheminImage(UtilEditor.ImageType.OBJET));
        imageSalleList.setAll(UtilEditor.getCheminImage(UtilEditor.ImageType.SALLE));
    }

    /**
     * @return Liste des images d'un type
     */
    public static ObservableList<String> liste(UtilEditor.ImageType type){
        return type == UtilEditor.ImageType.OBJET ? imageObjetList : imageSalleList;
    }

}
