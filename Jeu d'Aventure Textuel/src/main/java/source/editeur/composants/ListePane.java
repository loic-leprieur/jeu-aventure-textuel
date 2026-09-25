package source.editeur.composants;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import source.editeur.modele.ElementJeu;
import source.editeur.modele.ModeleJeu;

import java.util.List;

/**
 * Liste d'éléments du jeu : boutons Ajouter / Modifier / Supprimer et tableau.
 * Double-clic sur une ligne : modifier ; touche Suppr : supprimer.
 * @param <T> Type d'élément
 */
public class ListePane<T extends ElementJeu> extends VBox {

    protected final TableView<T> table;
    protected final FormulaireFrame<T> formulaire;
    protected final ContextMenu menu = new ContextMenu();

    /**
     * @param table Tableau des éléments
     * @param formulaire Fenêtre d'ajout et de modification
     * @param texteVide Texte affiché quand la liste est vide
     */
    public ListePane(TableView<T> table, FormulaireFrame<T> formulaire, String texteVide) {
        this.table = table;
        this.formulaire = formulaire;
        setSpacing(6);
        setPadding(new Insets(4, 2, 2, 2));

        Button ajouter = bouton("Ajouter", "Ajouter un élément (touche Inser dans la liste)");
        ajouter.setOnAction(e -> formulaire.creer(fenetre()));
        Button modifier = bouton("Modifier", "Modifier l'élément sélectionné (double-clic)");
        modifier.setOnAction(e -> modifierSelection());
        Button supprimer = bouton("Supprimer", "Supprimer l'élément sélectionné (Suppr)");
        supprimer.setOnAction(e -> supprimerSelection());
        modifier.disableProperty().bind(Bindings.isNull(table.getSelectionModel().selectedItemProperty()));
        supprimer.disableProperty().bind(modifier.disableProperty());

        Label vide = new Label(texteVide);
        vide.setWrapText(true);
        vide.getStyleClass().add("texte-aide");
        table.setPlaceholder(vide);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        VBox.setVgrow(table, Priority.ALWAYS);

        MenuItem menuModifier = new MenuItem("Modifier…");
        menuModifier.setOnAction(e -> modifierSelection());
        MenuItem menuSupprimer = new MenuItem("Supprimer");
        menuSupprimer.setOnAction(e -> supprimerSelection());
        menu.getItems().addAll(menuModifier, menuSupprimer);

        table.setRowFactory(t -> {
            TableRow<T> ligne = new TableRow<>();
            ligne.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2 && !ligne.isEmpty()) {
                    formulaire.modifier(fenetre(), ligne.getItem());
                }
            });
            ligne.contextMenuProperty().bind(Bindings.when(ligne.emptyProperty()).then((ContextMenu) null).otherwise(menu));
            return ligne;
        });
        table.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.DELETE) {
                supprimerSelection();
            } else if (e.getCode() == KeyCode.ENTER) {
                modifierSelection();
            } else if (e.getCode() == KeyCode.INSERT) {
                formulaire.creer(fenetre());
            }
        });

        HBox boutons = new HBox(6, ajouter, modifier, supprimer);
        getChildren().addAll(boutons, table);
    }

    private static Button bouton(String texte, String aide) {
        Button b = new Button(texte);
        b.setTooltip(new Tooltip(aide));
        b.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(b, Priority.ALWAYS);
        return b;
    }

    private Window fenetre() {
        return getScene() == null ? null : getScene().getWindow();
    }

    private void modifierSelection() {
        T e = table.getSelectionModel().getSelectedItem();
        if (e != null) {
            formulaire.modifier(fenetre(), e);
        }
    }

    private void supprimerSelection() {
        T e = table.getSelectionModel().getSelectedItem();
        if (e == null) {
            return;
        }
        List<String> dependances = ModeleJeu.get().dependances(e);
        String message = dependances.isEmpty()
                ? "Cette action est définitive."
                : "Conséquences :\n  • " + String.join("\n  • ", dependances);
        if (Dialogues.confirmer(fenetre(), "Supprimer « " + e + " » ?", message)) {
            ModeleJeu.get().supprimer(e);
        }
    }

    public TableView<T> getTable() {
        return table;
    }
}
