package source.editeur;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import source.editeur.composants.association.AssociationPane;
import source.editeur.composants.carte.CartePane;
import source.editeur.composants.lien.LienPane;
import source.editeur.composants.objet.ObjetPane;
import source.editeur.composants.salle.SallePane;
import source.editeur.composants.script.ScriptPane;
import source.editeur.composants.variable.VariablePane;
import source.util.UtilEditor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Création du Panel au centre
 */
public class PaneCenter extends GridPane {

    /**
     * Constructeur de PaneCenter
     */
    public PaneCenter(){

        this.setHgap(6);
        this.setVgap(6);
        this.setPadding(new Insets(0, 6, 0, 0));

        //Création du TabPane comprenant Salle et Lien
        Map<String,Pane> mapStPa = new LinkedHashMap<>();
        mapStPa.put("Salle",new SallePane());
        mapStPa.put("Lien", new LienPane());
        this.add(UtilEditor.createTabPane(this,mapStPa),1,0);
        mapStPa.clear();

        //Création du TabPane comprenant Objet et Association
        mapStPa.put("Objet",new ObjetPane());
        mapStPa.put("Association",new AssociationPane());
        this.add(UtilEditor.createTabPane(this,mapStPa),2,0);
        mapStPa.clear();

        //Création du GroupBox pour Variable
        this.add(UtilEditor.createGroupBox(new VariablePane(),"Variable",this),3,0);

        //Création du GroupBox pour Carte actuel
        this.add(UtilEditor.createGroupBox(new CartePane(),"Carte actuel",this),1,1);

        //Création du GroupBox pour Script
        this.add(UtilEditor.createGroupBox(new ScriptPane(),"Script",this),2,1,2,1);

    }




}
