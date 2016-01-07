package tests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import analyse.Analyseur;
import dictionnaire.Dictionnaire;
import dictionnaire.Verbe;

public class TestAnalyseur {

	private Analyseur analyseur;
	private Dictionnaire dico;
	
	@Before
	public void setup(){
		List<String> listSynonymes = null;
		listSynonymes.add("aller");
		listSynonymes.add("courir");
		listSynonymes.add("marcher");
		
		dico.ajouterNouvelleAction(new Verbe("avancer"), listSynonymes);
		analyseur = new Analyseur(dico, "aller vers porte");
	}
	
	@Test
	public void testPhrase3Mots() {
		analyseur.analyserPhrase();
	}

}
