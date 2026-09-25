package source.moteur.analyseur.analyse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import source.moteur.analyseur.Phrase;
import source.moteur.analyseur.dictionnaire.Complement;
import source.moteur.analyseur.dictionnaire.Dictionnaire;
import source.moteur.analyseur.dictionnaire.Preposition;
import source.moteur.analyseur.dictionnaire.Type;
import source.moteur.analyseur.dictionnaire.Verbe;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnalyseurTest {

    private Dictionnaire dico;
    private Analyseur analyseur;

    @BeforeEach
    void setUp() {
        dico = new Dictionnaire(new ArrayList<>());
        dico.ajouterMot(new Verbe("manger"), "avaler", "croquer");
        dico.ajouterMot(new Verbe("prendre"), "prenez", "ramasser");
        dico.ajouterMot(new Verbe("ouvrir"));
        dico.ajouterMot(new Verbe("aller"), "va", "allez");
        dico.ajouterMot(new Verbe("regarder"), "jeter un oeil");
        dico.ajouterMot(new Complement("pomme"));
        dico.ajouterMot(new Complement("frite"));
        dico.ajouterMot(new Complement("clé"), "clef");
        dico.ajouterMot(new Complement("porte d'entrée"));
        dico.ajouterMot(new Complement("porte"));
        dico.ajouterMot(new Complement("nord"));
        dico.ajouterMot(new Preposition("avec"), "à l'aide de");
        dico.ajouterMot(new Preposition("à"));
        analyseur = new Analyseur(dico);
    }

    private Phrase une(String texte) {
        List<Phrase> phrases = analyseur.analyser(texte);
        assertEquals(1, phrases.size(), () -> "phrases : " + phrases);
        return phrases.get(0);
    }

    private void verifier(Phrase p, String verbe, String complement, String preposition, String secondaire) {
        assertTrue(p.estComprise(), p::toString);
        assertEquals(verbe, p.getVerbe());
        assertEquals(complement, p.getComplement());
        assertEquals(preposition, p.getPreposition());
        assertEquals(secondaire, p.getComplementSecondaire());
    }

    @Test
    void verbeEtNomSimple() {
        verifier(une("manger pomme"), "manger", "pomme", null, null);
    }

    @Test
    void articlesMajusculesEtPonctuationIgnores() {
        verifier(une("Mange la pomme !"), "manger", "pomme", null, null);
    }

    @Test
    void synonymeSansNullPointer() {
        // bug historique : NPE sur un complément sans synonyme qui n'est pas le premier
        verifier(une("avaler frite"), "manger", "frite", null, null);
        assertEquals("frite", dico.getAction("frite", Type.complement));
    }

    @Test
    void formesConjuguees() {
        verifier(une("je mange une pomme"), "manger", "pomme", null, null);
        verifier(une("mangez la pomme"), "manger", "pomme", null, null);
        verifier(une("j'ouvre la porte"), "ouvrir", "porte", null, null);
    }

    @Test
    void consonneDoublee() {
        dico.ajouterMot(new Verbe("appeler"));
        dico.ajouterMot(new Verbe("jeter"));
        analyseur = new Analyseur(dico);
        verifier(une("j'appelle"), "appeler", null, null, null);
        verifier(une("jette la pomme"), "jeter", "pomme", null, null);
    }

    @Test
    void pluriel() {
        verifier(une("mange les pommes"), "manger", "pomme", null, null);
    }

    @Test
    void accentsOptionnels() {
        verifier(une("prends la cle"), "prendre", "clé", null, null);
        verifier(une("prenez la clef"), "prendre", "clé", null, null);
    }

    @Test
    void formulesDePolitesse() {
        verifier(une("je voudrais manger la pomme s'il te plaît"), "manger", "pomme", null, null);
        verifier(une("peux-tu ramasser la clé ?"), "prendre", "clé", null, null);
    }

    @Test
    void prepositionEtComplementSecondaire() {
        verifier(une("ouvre la porte avec la clé"), "ouvrir", "porte", "avec", "clé");
        verifier(une("ouvre la porte à l'aide de la clé"), "ouvrir", "porte", "avec", "clé");
    }

    @Test
    void contractionAu() {
        verifier(une("va au nord"), "aller", "nord", "à", null);
    }

    @Test
    void expressionsDePlusieursMots() {
        verifier(une("ouvre la porte d'entrée"), "ouvrir", "porte d'entrée", null, null);
        verifier(une("jeter un oeil à la pomme"), "regarder", "pomme", "à", null);
    }

    @Test
    void fauteDeFrappe() {
        verifier(une("mangr la pomme"), "manger", "pomme", null, null);
        verifier(une("prends la pome"), "prendre", "pomme", null, null);
    }

    @Test
    void plusieursCommandes() {
        List<Phrase> p = analyseur.analyser("prends la clé puis ouvre la porte avec la clé. Va au nord");
        assertEquals(3, p.size());
        verifier(p.get(0), "prendre", "clé", null, null);
        verifier(p.get(1), "ouvrir", "porte", "avec", "clé");
        verifier(p.get(2), "aller", "nord", "à", null);
    }

    @Test
    void etSuiviDUnVerbe() {
        List<Phrase> p = analyseur.analyser("prends la clé et ouvre la porte");
        assertEquals(2, p.size());
        verifier(p.get(0), "prendre", "clé", null, null);
        verifier(p.get(1), "ouvrir", "porte", null, null);
    }

    @Test
    void enumerationDeComplements() {
        List<Phrase> p = analyseur.analyser("mange la pomme, la frite et la pomme");
        assertEquals(3, p.size());
        verifier(p.get(0), "manger", "pomme", null, null);
        verifier(p.get(1), "manger", "frite", null, null);
        verifier(p.get(2), "manger", "pomme", null, null);
    }

    @Test
    void negation() {
        Phrase p = une("ne mange pas la pomme");
        verifier(p, "manger", "pomme", null, null);
        assertTrue(p.estNegative());
        assertTrue(une("n'ouvre pas la porte").estNegative());
    }

    @Test
    void verbeSeul() {
        verifier(une("regarder"), "regarder", null, null, null);
    }

    @Test
    void motInconnu() {
        Phrase p = une("mange la banane");
        assertFalse(p.estComprise());
        assertTrue(p.getErreur().contains("banane"), p.getErreur());
    }

    @Test
    void complementSansVerbe() {
        Phrase p = une("la pomme");
        assertFalse(p.estComprise());
        assertEquals("Que veux-tu faire avec pomme ?", p.getErreur());
    }

    @Test
    void texteVide() {
        assertTrue(analyseur.analyser("   ").isEmpty());
        assertFalse(analyseur.analyserPhrase("").estComprise());
    }
}
