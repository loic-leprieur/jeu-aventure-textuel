package source.editeur;


import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import source.editeur.composants.Dialogues;
import source.editeur.images.GestionImages;
import source.editeur.images.ObservableListImage;
import source.editeur.modele.ModeleJeu;
import source.moteur.ChargeurJeu;

import java.awt.Desktop;
import java.io.IOException;

/**
 * Creation du panel top
 */
public class PaneTop extends MenuBar {

    /**
     * Constructeur de PaneTop
     * @param stage Stage du panel
     * @param actions Actions du menu Fichier
     */
    public PaneTop(Stage stage, ActionsFichier actions){

        //Menu Fichier
        Menu menuFichier = new Menu("_Fichier");
        MenuItem nouveau = element("Nouveau", "Shortcut+N");
        nouveau.setOnAction(e -> actions.nouveau());
        MenuItem ouvrir = element("Ouvrir…", "Shortcut+O");
        ouvrir.setOnAction(e -> actions.ouvrir());
        MenuItem enregistrer = element("Enregistrer", "Shortcut+S");
        enregistrer.setOnAction(e -> actions.enregistrer());
        MenuItem enregistrerSous = element("Enregistrer sous…", "Shortcut+Shift+S");
        enregistrerSous.setOnAction(e -> actions.enregistrerSous());
        MenuItem quitter = element("Quitter", "Shortcut+Q");
        quitter.setOnAction(e -> {
            if(actions.confirmerAbandon()){
                Platform.exit();
            }
        });
        menuFichier.getItems().addAll(nouveau, ouvrir, new SeparatorMenuItem(), enregistrer, enregistrerSous,
                new SeparatorMenuItem(), quitter);

        //Menu Jeu
        Menu menuJeu = new Menu("_Jeu");
        MenuItem tester = element("Tester le jeu", "F9");
        tester.setOnAction(e -> tester(stage));
        MenuItem nom = new MenuItem("Nom du jeu…");
        nom.setOnAction(e -> actions.renommerJeu());
        menuJeu.getItems().addAll(tester, new SeparatorMenuItem(), nom);

        //Menu Option
        Menu menuOption = new Menu("_Options");
        MenuItem rafraichir = element("Rafraîchir les images", "F5");
        rafraichir.setOnAction(event -> ObservableListImage.rafraichirImage());
        MenuItem dossier = new MenuItem("Ouvrir le dossier des images");
        dossier.setOnAction(e -> ouvrirDossierImages(stage));
        menuOption.getItems().addAll(rafraichir, dossier);

        //Ajout menu à la menubar
        this.getMenus().addAll(menuFichier, menuJeu, menuOption);
    }

    private static MenuItem element(String texte, String raccourci){
        MenuItem m = new MenuItem(texte);
        m.setAccelerator(KeyCombination.keyCombination(raccourci));
        return m;
    }

    /**
     * Lance le jeu en cours d'édition dans la fenêtre du moteur.
     * « Recommencer la partie » y prend en compte les dernières modifications.
     */
    private static void tester(Stage stage){
        if(ModeleJeu.get().getSalles().isEmpty()){
            Dialogues.erreur(stage, "Impossible de tester le jeu", "Ajoutez au moins une salle.");
            return;
        }
        new source.moteur.grahique.PrincipalFrame(new ChargeurJeu.JeuCharge(ModeleJeu.get(), GestionImages.dossierRacine()));
    }

    private static void ouvrirDossierImages(Stage stage){
        java.io.File dossier = GestionImages.dossierRacine().toAbsolutePath().toFile();
        //Desktop.open peut bloquer : on l'appelle hors du thread de l'interface
        Thread t = new Thread(() -> {
            try {
                Desktop.getDesktop().open(dossier);
            } catch (IOException | UnsupportedOperationException e) {
                Platform.runLater(() -> Dialogues.information(stage, "Dossier des images", dossier.getPath()));
            }
        });
        t.setDaemon(true);
        t.start();
    }
}
