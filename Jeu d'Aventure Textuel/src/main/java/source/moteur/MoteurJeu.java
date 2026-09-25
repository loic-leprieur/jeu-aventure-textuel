package source.moteur;

import source.Niveau;
import source.Objet;
import source.Salle;
import source.editeur.modele.ActionObjet;
import source.moteur.analyseur.Phrase;
import source.moteur.analyseur.analyse.Analyseur;
import source.moteur.analyseur.analyse.Normaliseur;
import source.moteur.analyseur.dictionnaire.Dictionnaire;
import source.moteur.analyseur.dictionnaire.Mot;
import source.moteur.analyseur.dictionnaire.Type;
import source.moteur.regles.RegleJeu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Moteur du jeu : exécute les commandes du joueur.
 * <p>
 * Pour chaque phrase comprise par l'analyseur :
 * <ol>
 *     <li>la première règle du jeu qui correspond (commande et conditions) est appliquée ;
 *     les règles avec le plus de conditions sont essayées en premier ;</li>
 *     <li>sinon, l'action prédéfinie du verbe est exécutée
 *     (aller, prendre, poser, regarder, inventaire, aide) ;</li>
 *     <li>sinon, rien ne se passe.</li>
 * </ol>
 * Une règle dont la commande nomme un objet ne s'applique que si l'objet est à portée du joueur
 * (dans son inventaire, ou visible dans la salle).
 */
public class MoteurJeu {

    private final Niveau niveau;
    private final Dictionnaire dico;
    private final Analyseur analyseur;
    private final List<RegleJeu> regles;
    //Un exemplaire de chaque objet du jeu, par nom normalisé : sert quand une règle donne
    //au joueur un objet qui n'est placé dans aucune salle
    private final Map<String, Objet> catalogue;
    //Verbes propres à ce jeu (hors actions prédéfinies), pour l'aide
    private final List<String> verbesDuJeu;

    /**
     * @param niveau État initial de la partie
     * @param dico Dictionnaire complet (vocabulaire commun et mots du jeu)
     * @param regles Règles du jeu, dont les mots sont sous forme canonique (voir {@link #canonique})
     * @param catalogue Un exemplaire de chaque objet du jeu, par nom normalisé
     */
    public MoteurJeu(Niveau niveau, Dictionnaire dico, List<RegleJeu> regles, Map<String, Objet> catalogue) {
        this.niveau = niveau;
        this.dico = dico;
        this.analyseur = new Analyseur(dico);
        //Tri stable : à précision égale, l'ordre de l'éditeur est conservé
        this.regles = regles.stream().sorted(Comparator.comparingInt(RegleJeu::precision).reversed()).toList();
        this.catalogue = catalogue;
        this.verbesDuJeu = regles.stream().map(RegleJeu::verbe).distinct()
                .filter(v -> !VocabulaireParDefaut.PREDEFINIS.contains(v)).toList();
    }

    public Niveau getNiveau() {
        return niveau;
    }

    /**
     * Forme canonique d'un mot pour comparer commandes et règles :
     * libellé du mot du dictionnaire correspondant, normalisé.
     * @return Chaîne vide si le mot est vide
     */
    public static String canonique(Dictionnaire dico, Type type, String mot) {
        if (mot == null || mot.isBlank()) {
            return "";
        }
        Mot m = dico.rechercher(type, mot, false);
        return Normaliseur.normaliser(m != null ? m.getLibelle() : mot);
    }

    /**
     * Affiche le message d'accueil et la première salle
     */
    public void demarrer() {
        if (!niveau.getNom().isBlank()) {
            niveau.ajouterLog("=== " + niveau.getNom() + " ===");
        }
        niveau.ajouterLog(decrireSalle());
        niveau.ajouterLog("(Tape « aide » pour savoir quoi faire.)");
    }

    /**
     * Exécute le texte tapé par le joueur, qui peut contenir plusieurs commandes
     * ("prends la clé puis va au nord"). La commande et les réponses sont ajoutées au journal.
     */
    public void executer(String texte) {
        if (texte == null || texte.isBlank()) {
            return;
        }
        niveau.ajouterLog("> " + texte.strip());
        List<Phrase> phrases = analyseur.analyser(completerDirection(texte));
        if (phrases.isEmpty()) {
            niveau.ajouterLog("Je n'ai pas compris ce que tu veux faire.");
        }
        for (Phrase p : phrases) {
            niveau.ajouterLog(repondre(p));
        }
    }

    /**
     * Une direction seule ("nord", "n") signifie "aller nord"
     */
    private static String completerDirection(String texte) {
        String t = Normaliseur.normaliser(texte);
        for (String d : VocabulaireParDefaut.DIRECTIONS) {
            if (t.equals(d) || t.equals(d.substring(0, 1))) {
                return VocabulaireParDefaut.ALLER + " " + d;
            }
        }
        return texte;
    }

    /**
     * Réponse du jeu à une phrase analysée
     */
    String repondre(Phrase p) {
        if (!p.estComprise()) {
            return p.getErreur();
        }
        if (p.estNegative()) {
            return "D'accord, tu ne fais rien.";
        }
        for (RegleJeu r : regles) {
            if (correspond(r, p) && conditionsRemplies(r)) {
                return appliquer(r);
            }
        }
        return actionPredefinie(p);
    }

    // ------------------------------------------------------------------ règles

    private static String n(String s) {
        return s == null ? "" : Normaliseur.normaliser(s);
    }

    private boolean correspond(RegleJeu r, Phrase p) {
        return r.verbe().equals(n(p.getVerbe()))
                && r.complement().equals(n(p.getComplement()))
                && r.complementSecondaire().equals(n(p.getComplementSecondaire()))
                && (r.preposition().isEmpty() || r.preposition().equals(n(p.getPreposition())));
    }

    private boolean conditionsRemplies(RegleJeu r) {
        if (r.salle() != null && !niveau.getSalleActuel().getNom().equalsIgnoreCase(r.salle())) {
            return false;
        }
        if (r.conditionVariable() != null
                && !n(niveau.getVariable(r.conditionVariable())).equals(n(r.conditionValeur()))) {
            return false;
        }
        //Un objet nommé dans la commande doit être à portée
        for (String mot : new String[]{r.complement(), r.complementSecondaire()}) {
            if (catalogue.containsKey(mot) && !aPortee(mot)) {
                return false;
            }
        }
        return true;
    }

    private String appliquer(RegleJeu r) {
        List<String> lignes = new ArrayList<>();
        if (!r.message().isBlank()) {
            lignes.add(r.message());
        }
        if (r.variableModifiee() != null) {
            niveau.setVariable(r.variableModifiee(), r.nouvelleValeur());
        }
        if (r.objetCible() != null) {
            appliquerSurObjet(r.actionObjet(), n(r.objetCible()));
        }
        if (r.deplacement() != null) {
            Salle s = niveau.getSalle(r.deplacement());
            if (s != null) {
                niveau.setSalleActuel(s);
                lignes.add(decrireSalle());
            }
        }
        niveau.changer();
        return lignes.isEmpty() ? "D'accord." : String.join("\n", lignes);
    }

    private void appliquerSurObjet(ActionObjet action, String nom) {
        switch (action) {
            case DONNER -> donner(nom);
            case RETIRER -> niveau.getInventaire().removeIf(o -> n(o.getNom()).equals(nom));
            case AFFICHER -> exemplaires(nom).forEach(Objet::afficher);
            case CACHER -> exemplaires(nom).forEach(Objet::cacher);
            case AUCUNE -> { }
        }
    }

    /**
     * Met un objet dans l'inventaire : de préférence celui de la salle actuelle,
     * sinon un exemplaire placé ailleurs, sinon celui du catalogue
     */
    private void donner(String nom) {
        if (dansInventaire(nom) != null) {
            return;
        }
        Objet o = trouverDans(niveau.getSalleActuel().getObjets(), nom);
        Salle origine = o == null ? null : niveau.getSalleActuel();
        for (Salle s : niveau.getSalles()) {
            if (o == null) {
                o = trouverDans(s.getObjets(), nom);
                origine = o == null ? null : s;
            }
        }
        if (origine != null) {
            origine.getObjets().remove(o);
        }
        if (o == null) {
            o = catalogue.get(nom);
        }
        if (o != null) {
            o.afficher();
            niveau.getInventaire().add(o);
        }
    }

    // ------------------------------------------------------------------ actions prédéfinies

    private String actionPredefinie(Phrase p) {
        String complement = n(p.getComplement());
        String nomAffiche = p.getComplement();
        switch (n(p.getVerbe())) {
            case VocabulaireParDefaut.ALLER:
                return aller(complement, nomAffiche);
            case VocabulaireParDefaut.PRENDRE:
                return prendre(complement, nomAffiche);
            case VocabulaireParDefaut.POSER:
                return poser(complement, nomAffiche);
            case VocabulaireParDefaut.REGARDER:
                return regarder(complement, nomAffiche);
            case VocabulaireParDefaut.INVENTAIRE:
                return inventaire();
            case VocabulaireParDefaut.AIDE:
                return aide();
            default:
                if (catalogue.containsKey(complement) && !aPortee(complement)) {
                    return "Tu ne vois pas de " + nomAffiche + " ici.";
                }
                String secondaire = n(p.getComplementSecondaire());
                if (catalogue.containsKey(secondaire) && !aPortee(secondaire)) {
                    return "Tu n'as pas de " + p.getComplementSecondaire() + ".";
                }
                return "Rien ne se passe.";
        }
    }

    private String aller(String complement, String nomAffiche) {
        if (complement.isEmpty()) {
            return "Où veux-tu aller ? " + texteSorties();
        }
        int i = VocabulaireParDefaut.DIRECTIONS.indexOf(complement);
        if (i >= 0) {
            Direction d = Direction.values()[i];
            if (niveau.deplacer(d)) {
                return decrireSalle();
            }
            return "Tu ne peux pas aller vers le " + complement + ". " + texteSorties();
        }
        Salle cible = niveau.getSalle(nomAffiche);
        if (cible == null) {
            return "Tu ne sais pas comment aller là-bas.";
        }
        if (cible == niveau.getSalleActuel()) {
            return "Tu y es déjà.";
        }
        if (niveau.getSalleActuel().getLiens().containsValue(cible)) {
            niveau.setSalleActuel(cible);
            return decrireSalle();
        }
        return "Tu ne peux pas aller directement là-bas. " + texteSorties();
    }

    private String prendre(String nom, String nomAffiche) {
        if (nom.isEmpty()) {
            return "Prendre quoi ?";
        }
        if (dansInventaire(nom) != null) {
            return "Tu as déjà " + article(nomAffiche) + ".";
        }
        Salle ici = niveau.getSalleActuel();
        Objet o = trouverDans(ici.getObjetsVisibles(), nom);
        if (o == null) {
            return catalogue.containsKey(nom) ? "Il n'y a pas de " + nomAffiche + " ici." : "Tu ne peux pas prendre ça.";
        }
        if (!o.isPrenable()) {
            return "Tu ne peux pas prendre " + article(o.getNom()) + ".";
        }
        ici.getObjets().remove(o);
        niveau.getInventaire().add(o);
        niveau.changer();
        return "Tu prends " + article(o.getNom()) + ".";
    }

    private String poser(String nom, String nomAffiche) {
        if (nom.isEmpty()) {
            return "Poser quoi ?";
        }
        Objet o = dansInventaire(nom);
        if (o == null) {
            return "Tu n'as pas de " + nomAffiche + ".";
        }
        niveau.getInventaire().remove(o);
        niveau.getSalleActuel().getObjets().add(o);
        niveau.changer();
        return "Tu poses " + article(o.getNom()) + ".";
    }

    private String regarder(String nom, String nomAffiche) {
        if (nom.isEmpty() || nom.equals(n(niveau.getSalleActuel().getNom()))) {
            return decrireSalle();
        }
        Objet o = dansInventaire(nom);
        if (o == null) {
            o = trouverDans(niveau.getSalleActuel().getObjetsVisibles(), nom);
        }
        if (o != null) {
            return o.getDescription().isBlank() ? "Rien de particulier." : o.getDescription();
        }
        int i = VocabulaireParDefaut.DIRECTIONS.indexOf(nom);
        if (i >= 0) {
            Salle s = niveau.getSalle(Direction.values()[i]);
            return s == null ? "Rien de ce côté." : "Au " + nom + " : " + s.getNom() + ".";
        }
        return "Tu ne vois pas de " + nomAffiche + " ici.";
    }

    private String inventaire() {
        if (niveau.getInventaire().isEmpty()) {
            return "Tu ne portes rien.";
        }
        return "Tu portes : " + niveau.getInventaire().stream().map(Objet::getNom).collect(Collectors.joining(", ")) + ".";
    }

    private String aide() {
        String res = "Écris ce que tu veux faire, par exemple « prends la clé » ou « va au nord puis regarde ».\n"
                + "Commandes : aller <direction>, prendre <objet>, poser <objet>, regarder [objet], inventaire.";
        return verbesDuJeu.isEmpty() ? res : res + "\nDans ce jeu, essaie aussi : " + String.join(", ", verbesDuJeu) + ".";
    }

    // ------------------------------------------------------------------ descriptions et recherche

    /**
     * @return Nom, description, objets visibles et sorties de la salle actuelle
     */
    public String decrireSalle() {
        Salle s = niveau.getSalleActuel();
        StringBuilder sb = new StringBuilder("— ").append(s.getNom()).append(" —");
        if (!s.getDescription().isBlank()) {
            sb.append('\n').append(s.getDescription());
        }
        List<Objet> visibles = s.getObjetsVisibles();
        if (!visibles.isEmpty()) {
            sb.append("\nTu vois : ").append(visibles.stream().map(Objet::getNom).collect(Collectors.joining(", "))).append('.');
        }
        sb.append('\n').append(texteSorties());
        return sb.toString();
    }

    /**
     * @return "Sorties : nord, sud." ou "Aucune sortie."
     */
    public String texteSorties() {
        List<String> sorties = new ArrayList<>();
        for (Direction d : Direction.values()) {
            if (niveau.getSalleActuel().getLiens().containsKey(d)) {
                sorties.add(VocabulaireParDefaut.DIRECTIONS.get(d.ordinal()));
            }
        }
        return sorties.isEmpty() ? "Aucune sortie." : "Sorties : " + String.join(", ", sorties) + ".";
    }

    /**
     * Nom d'objet entre guillemets : le moteur ne connaît pas le genre des noms
     * pour choisir l'article (« le », « la »...)
     */
    private static String article(String nom) {
        return "« " + nom + " »";
    }

    private boolean aPortee(String nom) {
        return dansInventaire(nom) != null || trouverDans(niveau.getSalleActuel().getObjetsVisibles(), nom) != null;
    }

    private Objet dansInventaire(String nom) {
        return trouverDans(niveau.getInventaire(), nom);
    }

    private static Objet trouverDans(List<Objet> objets, String nom) {
        for (Objet o : objets) {
            if (n(o.getNom()).equals(nom)) {
                return o;
            }
        }
        return null;
    }

    private List<Objet> exemplaires(String nom) {
        List<Objet> res = new ArrayList<>();
        for (Salle s : niveau.getSalles()) {
            for (Objet o : s.getObjets()) {
                if (n(o.getNom()).equals(nom)) {
                    res.add(o);
                }
            }
        }
        return res;
    }
}
