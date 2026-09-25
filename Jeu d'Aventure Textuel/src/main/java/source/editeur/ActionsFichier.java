package source.editeur;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import source.editeur.composants.Dialogues;
import source.editeur.images.GestionImages;
import source.editeur.images.ObservableListImage;
import source.editeur.modele.FichierJeu;
import source.editeur.modele.FormatJeuException;
import source.editeur.modele.ModeleJeu;
import source.editeur.modele.Textes;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Actions du menu Fichier : nouveau jeu, ouverture, enregistrement
 */
public class ActionsFichier {

    private static final String TITRE = "4LEditeur";

    private final Stage stage;
    private final ModeleJeu modele = ModeleJeu.get();
    //Fichier du jeu en cours, null s'il n'a jamais été enregistré
    private final ObjectProperty<File> fichier = new SimpleObjectProperty<>();
    private File dernierDossier;

    public ActionsFichier(Stage stage) {
        this.stage = stage;
        stage.titleProperty().bind(Bindings.createStringBinding(
                () -> (modele.isModifie() ? "* " : "")
                        + (fichier.get() == null ? "Nouveau jeu" : fichier.get().getName())
                        + (Textes.vide(modele.getNom()) ? "" : " (" + modele.getNom() + ")")
                        + " - " + TITRE,
                fichier, modele.modifieProperty(), modele.nomProperty()));
    }

    /**
     * Si le jeu a été modifié, propose de l'enregistrer
     * @return Vrai si l'on peut continuer (jeu enregistré ou modifications abandonnées)
     */
    public boolean confirmerAbandon() {
        if (!modele.isModifie()) {
            return true;
        }
        ButtonType choix = Dialogues.demanderEnregistrement(stage);
        if (choix == Dialogues.ENREGISTRER) {
            return enregistrer();
        }
        return choix == Dialogues.NE_PAS_ENREGISTRER;
    }

    public void nouveau() {
        if (confirmerAbandon()) {
            modele.vider();
            fichier.set(null);
        }
    }

    public void ouvrir() {
        if (!confirmerAbandon()) {
            return;
        }
        FileChooser fc = selecteur("Ouvrir un jeu");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Script texte (*.txt)", "*.txt"));
        File f = fc.showOpenDialog(stage);
        if (f == null) {
            return;
        }
        dernierDossier = f.getParentFile();
        try {
            ModeleJeu lu = FichierJeu.ouvrir(f.toPath(), GestionImages.dossierRacine());
            ObservableListImage.rafraichirImage();
            modele.remplacerPar(lu);
            //Un script texte seul sera enregistré dans un nouveau fichier .aventure
            fichier.set(f.getName().toLowerCase().endsWith("." + FichierJeu.EXTENSION) ? f : null);
        } catch (FormatJeuException e) {
            Dialogues.erreur(stage, "Le fichier « " + f.getName() + " » contient une erreur.", e.getMessage());
        } catch (IOException e) {
            Dialogues.erreur(stage, "Impossible d'ouvrir « " + f.getName() + " ».", String.valueOf(e.getMessage()));
        }
    }

    /**
     * @return Vrai si le jeu a été enregistré
     */
    public boolean enregistrer() {
        return fichier.get() == null ? enregistrerSous() : ecrire(fichier.get());
    }

    /**
     * @return Vrai si le jeu a été enregistré
     */
    public boolean enregistrerSous() {
        FileChooser fc = selecteur("Enregistrer le jeu");
        fc.setInitialFileName(fichier.get() != null ? fichier.get().getName()
                : (Textes.vide(modele.getNom()) ? "mon-jeu" : modele.getNom()) + "." + FichierJeu.EXTENSION);
        File f = fc.showSaveDialog(stage);
        if (f == null) {
            return false;
        }
        if (!f.getName().toLowerCase().endsWith("." + FichierJeu.EXTENSION)) {
            f = new File(f.getParentFile(), f.getName() + "." + FichierJeu.EXTENSION);
        }
        dernierDossier = f.getParentFile();
        return ecrire(f);
    }

    private boolean ecrire(File f) {
        List<String> manquantes = FichierJeu.imagesManquantes(modele, GestionImages.dossierRacine());
        if (!manquantes.isEmpty() && !Dialogues.confirmer(stage, "Certaines images sont introuvables",
                "Ces images ne seront pas incluses dans le fichier :\n  • " + String.join("\n  • ", manquantes)
                        + "\n\nEnregistrer quand même ?")) {
            return false;
        }
        try {
            FichierJeu.enregistrer(modele, f.toPath(), GestionImages.dossierRacine());
            modele.setModifie(false);
            fichier.set(f);
            return true;
        } catch (IOException e) {
            Dialogues.erreur(stage, "Impossible d'enregistrer « " + f.getName() + " ».", String.valueOf(e.getMessage()));
            return false;
        }
    }

    /**
     * Demande le nom du jeu (affiché au joueur)
     */
    public void renommerJeu() {
        TextInputDialog d = new TextInputDialog(modele.getNom());
        d.initOwner(stage);
        d.setTitle("Nom du jeu");
        d.setHeaderText("Nom du jeu, affiché au joueur");
        d.setContentText("Nom :");
        d.showAndWait().ifPresent(modele::setNom);
    }

    private FileChooser selecteur(String titre) {
        FileChooser fc = new FileChooser();
        fc.setTitle(titre);
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "Jeu d'aventure (*." + FichierJeu.EXTENSION + ")", "*." + FichierJeu.EXTENSION));
        if (dernierDossier != null && dernierDossier.isDirectory()) {
            fc.setInitialDirectory(dernierDossier);
        }
        return fc;
    }
}
