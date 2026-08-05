package source.editeur.images;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import source.util.UtilEditor;

import java.util.stream.Collectors;

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
        imageObjetList.addAll(UtilEditor.getCheminImage(UtilEditor.ImageType.OBJET).stream().collect(Collectors.toList()));

        imageSalleList.clear();
        imageSalleList.addAll(UtilEditor.getCheminImage(UtilEditor.ImageType.SALLE).stream().collect(Collectors.toList()));
    }

}
