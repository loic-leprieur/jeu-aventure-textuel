package source.editeur;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import source.editeur.composants.association.AssociationPane;
import source.editeur.composants.carte.CartePane;
import source.editeur.composants.lien.LienPane;
import source.editeur.composants.objet.ObjetPane;
import source.editeur.composants.regle.ReglePane;
import source.editeur.composants.salle.SallePane;
import source.editeur.composants.script.ScriptPane;
import source.editeur.composants.variable.VariablePane;
import source.util.GroupBox;

/**
 * Création du Panel au centre
 * <pre>
 * | Salles / Liens | Objets / Placements | Variables       |
 * | Carte actuelle | Règles / Script                       |
 * </pre>
 */
public class PaneCenter extends GridPane {

    /**
     * Constructeur de PaneCenter
     */
    public PaneCenter(){

        this.setHgap(6);
        this.setVgap(6);
        this.setPadding(new Insets(0, 6, 6, 0));

        for(int i = 0; i < 3; i++){
            ColumnConstraints c = new ColumnConstraints();
            c.setPercentWidth(100.0 / 3);
            c.setHgrow(Priority.ALWAYS);
            getColumnConstraints().add(c);
        }
        RowConstraints haut = new RowConstraints();
        haut.setPercentHeight(45);
        haut.setVgrow(Priority.ALWAYS);
        RowConstraints bas = new RowConstraints();
        bas.setPercentHeight(55);
        bas.setVgrow(Priority.ALWAYS);
        getRowConstraints().addAll(haut, bas);

        SallePane salles = new SallePane();
        LienPane liens = new LienPane();
        ObjetPane objets = new ObjetPane();
        AssociationPane placements = new AssociationPane();
        CartePane carte = new CartePane(placements::placerDans, placements::modifier);

        //La carte affiche la salle sélectionnée dans les listes
        salles.getTable().getSelectionModel().selectedItemProperty().addListener((o, a, s) -> carte.afficher(s));
        liens.getTable().getSelectionModel().selectedItemProperty().addListener((o, a, l) -> {
            if(l != null){
                carte.afficher(l.getDepart());
            }
        });
        placements.getTable().getSelectionModel().selectedItemProperty().addListener((o, a, p) -> {
            if(p != null){
                carte.afficher(p.getSalle());
            }
        });

        add(onglets(new Tab("Salles", salles), new Tab("Liens", liens)), 0, 0);
        add(onglets(new Tab("Objets", objets), new Tab("Placements", placements)), 1, 0);
        add(cadre("Variables", new VariablePane()), 2, 0);
        add(cadre("Carte actuelle", carte), 0, 1);
        add(onglets(new Tab("Règles", new ReglePane()), new Tab("Script", new ScriptPane())), 1, 1, 2, 1);
    }

    private static TabPane onglets(Tab... tabs){
        TabPane tp = new TabPane(tabs);
        tp.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tp.setMinSize(0, 0);
        return tp;
    }

    private static GroupBox cadre(String titre, Node contenu){
        GroupBox gb = new GroupBox(titre, contenu);
        gb.setMinSize(0, 0);
        return gb;
    }
}
