package source.editeur.composants;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Separator;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import source.editeur.modele.ModeleJeu;

/**
 * Fenêtre de saisie d'un élément du jeu, utilisée pour l'ajout et la modification.
 * <p>
 * Les sous-classes placent leurs champs avec {@link #ligne(String, Node)}, et implémentent
 * le remplissage des champs, la validation et l'enregistrement.
 * @param <T> Type d'élément édité
 */
public abstract class FormulaireFrame<T> {

    protected final ModeleJeu modele = ModeleJeu.get();
    protected final Stage stage = new Stage();
    protected final GridPane formulaire = new GridPane();
    private final String nomElement;
    private final Button valider = new Button();
    private int lignes = 0;
    //Élément modifié, null pour un ajout
    private T enCours;

    /**
     * @param nomElement Nom du type d'élément, pour le titre ("objet", "salle"...)
     */
    protected FormulaireFrame(String nomElement) {
        this.nomElement = nomElement;

        formulaire.setHgap(10);
        formulaire.setVgap(8);
        formulaire.setPadding(new Insets(12));
        ColumnConstraints libelles = new ColumnConstraints();
        libelles.setHalignment(HPos.RIGHT);
        ColumnConstraints champs = new ColumnConstraints();
        champs.setHgrow(Priority.ALWAYS);
        champs.setMinWidth(260);
        formulaire.getColumnConstraints().addAll(libelles, champs);

        Button annuler = new Button("Annuler");
        annuler.setCancelButton(true);
        annuler.setOnAction(e -> stage.hide());
        valider.setDefaultButton(true);
        valider.setOnAction(e -> valider());
        HBox boutons = new HBox(8, annuler, valider);
        boutons.setAlignment(Pos.CENTER_RIGHT);
        boutons.setPadding(new Insets(0, 12, 12, 12));

        BorderPane racine = new BorderPane(formulaire);
        racine.setBottom(boutons);
        racine.getStylesheets().add(FormulaireFrame.class.getResource("/source/editeur/util/style/style.css").toExternalForm());
        racine.getStyleClass().add("formulaire");

        Scene scene = new Scene(racine);
        //Ctrl+Entrée valide même depuis une zone de texte multiligne
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER && e.isShortcutDown()) {
                valider();
            }
        });
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(true);
    }

    /**
     * Ajoute une ligne "libellé : champ" au formulaire
     */
    protected void ligne(String libelle, Node champ) {
        Label l = new Label(libelle);
        l.setLabelFor(champ);
        formulaire.add(l, 0, lignes);
        formulaire.add(champ, 1, lignes);
        GridPane.setHgrow(champ, Priority.ALWAYS);
        lignes++;
    }

    /**
     * Ajoute un titre de section au formulaire
     */
    protected void section(String titre) {
        if (lignes > 0) {
            formulaire.add(new Separator(), 0, lignes++, 2, 1);
        }
        Label l = new Label(titre);
        l.getStyleClass().add("section-formulaire");
        GridPane.setHalignment(l, HPos.LEFT);
        formulaire.add(l, 0, lignes++, 2, 1);
    }

    /**
     * Ouvre le formulaire vide pour ajouter un élément
     */
    public void creer(Window parent) {
        enCours = null;
        preparer(parent);
        vider();
        stage.setTitle("Ajouter " + nomElement);
        valider.setText("Ajouter");
        afficher();
    }

    /**
     * Ouvre le formulaire rempli pour modifier un élément
     */
    public void modifier(Window parent, T element) {
        enCours = element;
        preparer(parent);
        remplir(element);
        stage.setTitle("Modifier " + nomElement);
        valider.setText("Enregistrer");
        afficher();
    }

    private void preparer(Window parent) {
        if (stage.getOwner() == null && parent != null) {
            stage.initOwner(parent);
        }
        actualiserListes();
    }

    private void afficher() {
        stage.sizeToScene();
        stage.show();
        stage.toFront();
    }

    private void valider() {
        String erreur = verifier(enCours);
        if (erreur != null) {
            Dialogues.erreur(stage, "Impossible d'enregistrer " + nomElement, erreur);
            return;
        }
        enregistrer(enCours);
        stage.hide();
    }

    /**
     * Met à jour les listes de choix (salles, objets...) avant l'ouverture
     */
    protected void actualiserListes() {
    }

    /**
     * Remet les champs à leur valeur par défaut
     */
    protected abstract void vider();

    /**
     * Remplit les champs avec un élément existant
     */
    protected abstract void remplir(T element);

    /**
     * Vérifie la saisie
     * @param existant Élément modifié, null pour un ajout
     * @return Message d'erreur, null si la saisie est correcte
     */
    protected abstract String verifier(T existant);

    /**
     * Enregistre la saisie : crée et ajoute un nouvel élément au modèle, ou modifie l'existant
     * @param existant Élément modifié, null pour un ajout
     */
    protected abstract void enregistrer(T existant);

    /**
     * Configure une liste de choix pouvant être vide : la valeur null s'affiche avec texteSiNull
     */
    protected static <E> void autoriserVide(ComboBox<E> cb, String texteSiNull) {
        cb.setCellFactory(l -> new CelluleOptionnelle<>(texteSiNull));
        cb.setButtonCell(new CelluleOptionnelle<>(texteSiNull));
        cb.setPromptText(texteSiNull);
        cb.setMaxWidth(Double.MAX_VALUE);
    }

    /**
     * Remplit une liste de choix : une première valeur vide (null) puis les éléments
     */
    protected static <E> void remplirAvecVide(ComboBox<E> cb, java.util.List<E> elements) {
        E selection = cb.getValue();
        java.util.List<E> items = new java.util.ArrayList<>();
        items.add(null);
        items.addAll(elements);
        cb.getItems().setAll(items);
        cb.setValue(elements.contains(selection) ? selection : null);
    }

    private static class CelluleOptionnelle<E> extends ListCell<E> {
        private final String texteSiNull;

        CelluleOptionnelle(String texteSiNull) {
            this.texteSiNull = texteSiNull;
        }

        @Override
        protected void updateItem(E item, boolean vide) {
            super.updateItem(item, vide);
            getStyleClass().remove("valeur-vide");
            if (vide) {
                setText(null);
            } else if (item == null) {
                setText(texteSiNull);
                getStyleClass().add("valeur-vide");
            } else {
                setText(item.toString());
            }
        }
    }
}
