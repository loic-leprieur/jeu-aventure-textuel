package source.moteur.grahique.pane;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import source.Niveau;
import source.Objet;

import java.util.Observable;
import java.util.Observer;
import java.util.function.Consumer;

/**
 * Inventaire du joueur. Double-clic sur un objet : l'examiner.
 */
public class InventairePane extends VBox implements Observer {

    private final ListView<Objet> liste = new ListView<>();

    /**
     * @param commande Exécute une commande (double-clic : "regarder objet")
     */
    public InventairePane(Consumer<String> commande){
        setSpacing(4);
        Label titre = new Label("Inventaire");
        titre.getStyleClass().add("titre-panneau");
        liste.setPlaceholder(new Label("Tu ne portes rien."));
        liste.setFocusTraversable(false);
        liste.setCellFactory(l -> new ListCell<>() {
            private final ImageView vue = new ImageView();
            {
                vue.setFitWidth(36);
                vue.setFitHeight(36);
                vue.setPreserveRatio(true);
                setOnMouseClicked(e -> {
                    if(e.getClickCount() == 2 && getItem() != null){
                        commande.accept("regarder " + getItem().getNom());
                    }
                });
            }

            @Override
            protected void updateItem(Objet o, boolean vide) {
                super.updateItem(o, vide);
                if(vide || o == null){
                    setText(null);
                    setGraphic(null);
                }else{
                    setText(o.getNom());
                    vue.setImage(o.getImage());
                    setGraphic(vue);
                }
            }
        });
        VBox.setVgrow(liste, Priority.ALWAYS);
        getChildren().addAll(titre, liste);
    }

    @Override
    public void update(Observable o, Object arg) {
        liste.getItems().setAll(((Niveau) o).getInventaire());
    }
}
