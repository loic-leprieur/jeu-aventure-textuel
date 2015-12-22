
import composants.association.AssociationPane;
import composants.carte.CartePane;
import composants.script.ScriptPane;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import composants.lien.LienPane;
import composants.objet.ObjetPane;
import composants.salle.SallePane;
import util.UtilEditor;
import composants.variable.VariablePane;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Création du Panel au centre
 */
public class PaneCenter extends GridPane {

    //Stage du panel
    private Stage stage;

    /**
     * Constructeur de PaneCenter
     * @param stage Stage du panel
     */
    public PaneCenter(Stage stage){

        this.stage = stage;

        this.setHgap(6);
        this.setVgap(6);
        this.setPadding(new Insets(0, 6, 0, 0));

        //Création du TabPane comprenant Salle et Lien
        Map<String,Pane> mapStPa = new LinkedHashMap<>();
        mapStPa.put("Salle",new SallePane());
        mapStPa.put("Lien", new LienPane());
        this.add(UtilEditor.createTabPane(stage,mapStPa),1,0);
        mapStPa.clear();

        //Création du TabPane comprenant Objet et Association
        mapStPa.put("Objet",new ObjetPane());
        mapStPa.put("Association",new AssociationPane());
        this.add(UtilEditor.createTabPane(stage,mapStPa),2,0);
        mapStPa.clear();

        //Création du GroupBox pour Variable
        this.add(UtilEditor.createGroupBox(stage,"Variable",new VariablePane()),3,0);

        //Création du GroupBox pour Carte actuel
        this.add(UtilEditor.createGroupBox(stage,"Carte actuel",new CartePane()),1,1);

        //Création du GroupBox pour Script
        this.add(UtilEditor.createGroupBox(stage,"Script",new ScriptPane()),2,1,2,1);

    }




}
