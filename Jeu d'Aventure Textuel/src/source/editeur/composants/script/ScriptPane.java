package source.editeur.composants.script;


import javafx.collections.ListChangeListener;
import javafx.scene.control.TextArea;
import source.editeur.action.table.ObjetTable;
import source.editeur.action.table.Script;
import source.editeur.action.table.VariableTable;
import source.editeur.composants.GridPaneEditeur;
import source.editeur.composants.objet.Objet;
import source.editeur.composants.variable.Variable;

/**
 * Classe ScriptPane
 */
public class ScriptPane extends GridPaneEditeur {

    /**
     * Constructeur de ScriptPane
     */
    public ScriptPane(){
        final TextArea ta = new Script();
        ta.prefWidthProperty().bind(this.widthProperty());
        ta.prefHeightProperty().bind(this.heightProperty());
        ta.setEditable(false);
        addListenerVariable(ta);
        this.add(ta,1,0);
        Script.refresh();
    }

    private void addListenerVariable(final TextArea t){

        //Ajout d'un Listener à la liste d'objet
        ObjetTable.objet.addListener((ListChangeListener<Objet>) c -> Script.refresh());

        //AJout d'un Listener à la liste de variable
        VariableTable.variable.addListener((ListChangeListener<Variable>) c -> Script.refresh());


        Script.script.addListener((observable, oldValue, newValue) -> t.setText(newValue));
    }
}
