import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
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
        Menu menuFichier = new Menu("Fichier");
        MenuItem menuFichierNouveau = new MenuItem("Nouveau");
        MenuItem menuFichierSauvegarder = new MenuItem("Sauvegarder");
        MenuItem menuFichierCharger = new MenuItem("Charger");
        MenuItem menuFichierQuitter = new MenuItem("Quitter");
        menuFichierQuitter.setOnAction(actionEvent -> Platform.exit());

        menuFichier.getItems().addAll(menuFichierNouveau,menuFichierSauvegarder,menuFichierCharger,new SeparatorMenuItem(),menuFichierQuitter);

        this.getMenus().addAll(menuFichier);
    }
}
