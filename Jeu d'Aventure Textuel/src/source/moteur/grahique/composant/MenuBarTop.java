package source.moteur.grahique.composant;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

/**
 * Created by louzw on 10/01/2016.
 */
public class MenuBarTop extends MenuBar {

    public MenuBarTop(Pane pane){
        this.prefWidthProperty().bind(pane.widthProperty());

        //Menu Fichier
        Menu menuFichier = new Menu("Fichier");
        MenuItem menuFichierCharger = new MenuItem("Charger");
        MenuItem menuFichierQuitter = new MenuItem("Quitter");
        menuFichierQuitter.setOnAction(actionEvent -> Platform.exit());

        menuFichier.getItems().addAll(menuFichierCharger,new SeparatorMenuItem(),menuFichierQuitter);

        //Ajout menu à la menubar
        this.getMenus().add(menuFichier);
    }
}
