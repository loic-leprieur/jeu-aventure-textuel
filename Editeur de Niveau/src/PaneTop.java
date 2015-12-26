import images.ObservableListImage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Creation du panel top
 */
public class PaneTop extends MenuBar {

    /**
     * Constructeur de PaneTop
     * @param stage Stage du panel
     */
    public PaneTop(Stage stage){
        this.prefWidthProperty().bind(stage.widthProperty());

        //Menu Fichier
        Menu menuFichier = new Menu("Fichier");
        MenuItem menuFichierNouveau = new MenuItem("Nouveau");
        MenuItem menuFichierSauvegarder = new MenuItem("Sauvegarder");
        MenuItem menuFichierCharger = new MenuItem("Charger");
        MenuItem menuFichierQuitter = new MenuItem("Quitter");
        menuFichierQuitter.setOnAction(actionEvent -> Platform.exit());

        menuFichier.getItems().addAll(menuFichierNouveau,menuFichierSauvegarder,menuFichierCharger,new SeparatorMenuItem(),menuFichierQuitter);

        //Menu Option
        Menu menuOption = new Menu("Options");
        MenuItem menuOptionRafraichirImage = new MenuItem("Rafraichir images");
        menuOptionRafraichirImage.setOnAction(event -> {
            ObservableListImage.rafraichirImage();
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Mise à jour images");
            a.setHeaderText("Mise à jour des images effectuée");
            a.show();
        });

        menuOption.getItems().add(menuOptionRafraichirImage);

        //Ajout menu à la menubar
        this.getMenus().addAll(menuFichier,menuOption);
    }
}
