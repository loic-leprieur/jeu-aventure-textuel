package action.table;

import com.sun.java.swing.plaf.windows.WindowsInternalFrameTitlePane;
import composants.objet.Objet;
import composants.variable.Variable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextArea;


/**
 * Vue de la TextArea pour le script
 */
public class Script extends TextArea {

	public static SimpleStringProperty script;

	public Script(){
		script = new SimpleStringProperty();
	}

	public static void refresh(){
		String scriptFinal = "";

		String variable = "##VARIABLE##\n";
		for(Variable va : VariableTable.variable){
			variable += va.toString() + "\n";
		}
		variable += "\n";

		scriptFinal += variable;

		String objet = "##OBJET##\n";
		for(Objet obj : ObjetTable.objet){
			objet += obj.toString() + "\n";
		}
		objet += "\n";

		scriptFinal += objet;

		script.set(scriptFinal);
	}



	
}
