package source.moteur.grahique.composant;

import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import source.moteur.grahique.PrincipalFrame;

/**
 * Menu de la fenêtre de jeu
 */
public class MenuBarTop extends MenuBar {

    public MenuBarTop(PrincipalFrame fenetre){

        //Menu Fichier
        Menu menuFichier = new Menu("_Fichier");
        MenuItem ouvrir = new MenuItem("Ouvrir un jeu…");
        ouvrir.setAccelerator(KeyCombination.keyCombination("Shortcut+O"));
        ouvrir.setOnAction(e -> fenetre.ouvrir());

        MenuItem recommencer = new MenuItem("Recommencer la partie");
        recommencer.setAccelerator(KeyCombination.keyCombination("Shortcut+R"));
        recommencer.setOnAction(e -> fenetre.recommencer());

        MenuItem fermer = new MenuItem("Fermer");
        fermer.setAccelerator(KeyCombination.keyCombination("Shortcut+W"));
        fermer.setOnAction(e -> fenetre.fermer());

        menuFichier.getItems().addAll(ouvrir, recommencer, new SeparatorMenuItem(), fermer);

        //Ajout menu à la menubar
        this.getMenus().add(menuFichier);
    }
}
