package source.editeur.action.table;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextArea;
import source.editeur.composants.objet.Objet;
import source.editeur.composants.variable.Variable;


/**
 * Vue de la TextArea pour le script
 */
public class Script extends TextArea {

	public static SimpleStringProperty script;

	public Script(){
		script = new SimpleStringProperty();
	}

	/**
	 * Methode servant a actualis� le contenu de la zone de script
	 */
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
		
		//String salle = "##SALLE##\n";
		//for(Salle sal : SalleTable.objet){
		//	salle += sal.toString() + "\n";
		//}
		//salle += "\n";

		//scriptFinal += salle;
		
		//String lien_salle = "##LIENS##\n";
		//for(Lien l : LienTable.lien){
		//	lien += lien_salle.toString() + "\n";
		//}
		//lien_salle += "\n";

		//scriptFinal += lien_salle;
		

		script.set(scriptFinal);
	}



	
}
