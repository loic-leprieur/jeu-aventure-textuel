package images;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.UtilEditor;

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
        imageObjetList.clear();
        for(String s : UtilEditor.getCheminImage(UtilEditor.ImageType.OBJET)){
            imageObjetList.add(s);
        }

        imageSalleList.clear();
        for(String s : UtilEditor.getCheminImage(UtilEditor.ImageType.SALLE)){
            imageSalleList.add(s);
        }
    }

}
