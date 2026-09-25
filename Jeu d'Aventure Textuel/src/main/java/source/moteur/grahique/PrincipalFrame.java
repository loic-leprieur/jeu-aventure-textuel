package source.moteur.grahique;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import source.editeur.composants.Dialogues;
import source.editeur.modele.FichierJeu;
import source.editeur.modele.FormatJeuException;
import source.moteur.ChargeurJeu;
import source.moteur.MoteurJeu;
import source.moteur.exception.JeuInvalideException;
import source.moteur.grahique.composant.MenuBarTop;
import source.moteur.grahique.pane.InformationPane;
import source.moteur.grahique.pane.InventairePane;
import source.moteur.grahique.pane.ZoneTextePane;
import source.moteur.grahique.vue.SalleVue;
import source.util.UtilEditor;

import java.io.File;
import java.io.IOException;

/**
 * Fenêtre de jeu
 * <pre>
 * | image de la salle        | nom du jeu, lieu, sorties |
 * |--------------------------| inventaire                |
 * | journal + saisie         |                           |
 * </pre>
 */
public class PrincipalFrame {

    private static final String TITRE = "Jeu d'aventure";

    private final Stage stage;
    private final BorderPane racine = new BorderPane();
    private ChargeurJeu.JeuCharge jeu;
    private File dernierDossier;

    /**
     * Ouvre une fenêtre et lance une partie
     * @param jeu Jeu à lancer
     */
    public PrincipalFrame(ChargeurJeu.JeuCharge jeu){
        stage = UtilEditor.createStage(TITRE, 1100, 720, racine, true);
        stage.setMinWidth(700);
        stage.setMinHeight(500);
        racine.setTop(new MenuBarTop(this));
        stage.show();
        jouer(jeu);
    }

    /**
     * Lance une nouvelle partie d'un jeu
     */
    public void jouer(ChargeurJeu.JeuCharge nouveau){
        MoteurJeu moteur;
        try {
            moteur = nouveau.nouvellePartie();
        } catch (JeuInvalideException e) {
            Dialogues.erreur(stage, "Impossible de lancer le jeu", e.getMessage());
            return;
        }
        this.jeu = nouveau;
        String nom = moteur.getNiveau().getNom();
        stage.setTitle(nom.isBlank() ? TITRE : nom + " - " + TITRE);

        SalleVue salle = new SalleVue(moteur::executer);
        ZoneTextePane texte = new ZoneTextePane(moteur);
        InformationPane infos = new InformationPane(moteur);
        InventairePane inventaire = new InventairePane(moteur::executer);
        moteur.getNiveau().addObserver(salle);
        moteur.getNiveau().addObserver(infos);
        moteur.getNiveau().addObserver(inventaire);

        SplitPane gauche = new SplitPane(salle, texte);
        gauche.setOrientation(Orientation.VERTICAL);
        gauche.setDividerPositions(0.58);
        VBox droite = new VBox(12, infos, inventaire);
        droite.setPadding(new Insets(10));
        droite.setMinWidth(200);
        VBox.setVgrow(inventaire, Priority.ALWAYS);
        SplitPane centre = new SplitPane(gauche, droite);
        centre.setDividerPositions(0.74);
        SplitPane.setResizableWithParent(droite, false);
        racine.setCenter(centre);

        moteur.demarrer();
        texte.activerSaisie();
    }

    /**
     * Recommence la partie en cours depuis le début
     */
    public void recommencer(){
        if(jeu != null){
            jouer(jeu);
        }
    }

    /**
     * Choisit et lance un fichier de jeu
     */
    public void ouvrir(){
        FileChooser fc = new FileChooser();
        fc.setTitle("Ouvrir un jeu");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Jeu d'aventure (*." + FichierJeu.EXTENSION + ")", "*." + FichierJeu.EXTENSION),
                new FileChooser.ExtensionFilter("Script texte (*.txt)", "*.txt"));
        if(dernierDossier != null && dernierDossier.isDirectory()){
            fc.setInitialDirectory(dernierDossier);
        }
        File f = fc.showOpenDialog(stage);
        if(f != null){
            dernierDossier = f.getParentFile();
            ouvrir(f);
        }
    }

    /**
     * Lance un fichier de jeu
     * @return Vrai si le jeu a été lancé
     */
    public boolean ouvrir(File f){
        try {
            jouer(ChargeurJeu.ouvrir(f.toPath()));
            return true;
        } catch (FormatJeuException e) {
            Dialogues.erreur(stage, "Le fichier « " + f.getName() + " » contient une erreur.", e.getMessage());
        } catch (IOException e) {
            Dialogues.erreur(stage, "Impossible d'ouvrir « " + f.getName() + " ».", String.valueOf(e.getMessage()));
        }
        return false;
    }

    public void fermer(){
        stage.close();
    }
}
