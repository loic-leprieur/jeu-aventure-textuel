package source.moteur;

import source.moteur.analyseur.dictionnaire.Complement;
import source.moteur.analyseur.dictionnaire.Dictionnaire;
import source.moteur.analyseur.dictionnaire.Preposition;
import source.moteur.analyseur.dictionnaire.Verbe;

import java.util.List;

/**
 * Vocabulaire compris dans tous les jeux, en plus des mots utilisés par le jeu lui-même
 * (noms des objets et des salles, mots des règles).
 * <p>
 * Les formes conjuguées régulières (prends, ouvrez...) sont reconnues par l'analyseur ;
 * seules les formes irrégulières et les synonymes sont listés ici.
 */
public final class VocabulaireParDefaut {

    //Verbes ayant une action prédéfinie dans le moteur
    public static final String ALLER = "aller";
    public static final String PRENDRE = "prendre";
    public static final String POSER = "poser";
    public static final String REGARDER = "regarder";
    public static final String INVENTAIRE = "inventaire";
    public static final String AIDE = "aide";

    /** Verbes ayant une action prédéfinie */
    public static final List<String> PREDEFINIS = List.of(ALLER, PRENDRE, POSER, REGARDER, INVENTAIRE, AIDE);

    /** Directions, dans l'ordre de l'énumération Direction */
    public static final List<String> DIRECTIONS = List.of("nord", "est", "sud", "ouest");

    private VocabulaireParDefaut() {
    }

    /**
     * Ajoute le vocabulaire commun au dictionnaire
     */
    public static void ajouter(Dictionnaire dico) {
        //Actions prédéfinies
        dico.ajouterMot(new Verbe(ALLER), "va", "vas", "allez", "allons", "avancer", "marcher", "partir",
                "se diriger", "se deplacer", "courir", "entrer", "sortir");
        dico.ajouterMot(new Verbe(PRENDRE), "prenez", "prenons", "ramasser", "attraper", "saisir", "recuperer");
        dico.ajouterMot(new Verbe(POSER), "lacher", "deposer", "laisser");
        dico.ajouterMot(new Verbe(REGARDER), "examiner", "observer", "inspecter", "voir", "vois", "jeter un oeil",
                "decrire", "contempler");
        dico.ajouterMot(new Verbe(INVENTAIRE), "inv", "sac");
        dico.ajouterMot(new Verbe(AIDE), "aider");

        //Verbes courants sans action prédéfinie : les règles du jeu leur donnent un sens
        dico.ajouterMot(new Verbe("fouiller"), "chercher", "explorer");
        dico.ajouterMot(new Verbe("lire"), "lis", "lisez", "lisons", "dechiffrer");
        dico.ajouterMot(new Verbe("ouvrir"), "deverrouiller");
        dico.ajouterMot(new Verbe("fermer"), "verrouiller");
        dico.ajouterMot(new Verbe("utiliser"), "employer", "se servir de");
        dico.ajouterMot(new Verbe("parler"), "discuter", "dire");
        dico.ajouterMot(new Verbe("donner"), "offrir");
        dico.ajouterMot(new Verbe("manger"), "avaler", "croquer", "gouter");
        dico.ajouterMot(new Verbe("boire"), "bois", "buvez", "buvons");
        dico.ajouterMot(new Verbe("pousser"));
        dico.ajouterMot(new Verbe("tirer"));
        dico.ajouterMot(new Verbe("allumer"));
        dico.ajouterMot(new Verbe("eteindre"), "eteins", "eteignez");

        for (String d : DIRECTIONS) {
            dico.ajouterMot(new Complement(d));
        }

        dico.ajouterMot(new Preposition("avec"), "a l'aide de", "grace a");
        dico.ajouterMot(new Preposition("à"));
        dico.ajouterMot(new Preposition("sur"));
        dico.ajouterMot(new Preposition("sous"));
        dico.ajouterMot(new Preposition("dans"));
        dico.ajouterMot(new Preposition("vers"));
        dico.ajouterMot(new Preposition("derrière"));
        dico.ajouterMot(new Preposition("devant"));
        dico.ajouterMot(new Preposition("contre"));
    }
}
