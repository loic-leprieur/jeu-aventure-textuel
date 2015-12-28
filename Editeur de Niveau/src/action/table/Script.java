package action.table;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.control.TextArea;

import java.util.Observable;
import java.util.Observer;

import composants.objet.Objet;

/**
 * Vue de la TextArea pour le script
 */
public class Script extends TextArea {
	
	private String variable;
	private String objet;
	private String salle;
	private String relation_objet;
	private String relation_salle;
	private String action;
	
	
	public Script() {
		super();
		this.setEditable(false);
		
	}
	
	
	
	
	
	
}
