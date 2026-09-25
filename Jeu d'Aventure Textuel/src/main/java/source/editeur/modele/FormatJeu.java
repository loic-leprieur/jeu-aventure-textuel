package source.editeur.modele;

import source.moteur.Direction;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Script texte d'un jeu : lecture et écriture.
 * <p>
 * Le script est une suite de blocs, un par élément :
 * <pre>
 * [salle]
 * nom = Bureau
 * description = Un vieux bureau poussiéreux
 * image = sal_bureau.jpg
 *
 * [lien]
 * depart = Bureau
 * direction = NORD
 * arrivee = Chambre
 * </pre>
 * Les éléments se référencent par leur nom (sans tenir compte de la casse).
 * Les lignes vides et celles commençant par # sont ignorées.
 * Dans les valeurs, un retour à la ligne s'écrit \n et un antislash \\.
 */
public final class FormatJeu {

    public static final int VERSION = 1;

    private static final String JEU = "jeu";
    private static final String VARIABLE = "variable";
    private static final String OBJET = "objet";
    private static final String SALLE = "salle";
    private static final String LIEN = "lien";
    private static final String ASSOCIATION = "association";
    private static final String REGLE = "regle";

    /** Clés autorisées dans chaque section */
    private static final Map<String, Set<String>> CLES = Map.of(
            JEU, Set.of("nom", "salleDepart", "version"),
            VARIABLE, Set.of("nom", "valeur"),
            OBJET, Set.of("nom", "description", "prenable", "image"),
            SALLE, Set.of("nom", "description", "image"),
            LIEN, Set.of("depart", "direction", "arrivee"),
            ASSOCIATION, Set.of("salle", "objet", "x", "y", "visible"),
            REGLE, Set.of("verbe", "complement", "preposition", "complementSecondaire",
                    "salle", "condition.variable", "condition.valeur",
                    "message", "effet.variable", "effet.valeur",
                    "effet.objet.action", "effet.objet", "effet.salle"));

    private FormatJeu() {
    }

    // ------------------------------------------------------------------ écriture

    /**
     * Écrit le script complet d'un jeu
     * @param m Modèle
     * @return Texte du script
     */
    public static String ecrire(ModeleJeu m) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Jeu d'aventure textuel - script généré par 4LEditeur\n\n");

        bloc(sb, JEU, "version", String.valueOf(VERSION),
                "nom", m.getNom(),
                "salleDepart", Textes.nom(m.getSalleDepart(), ""));

        for (Variable v : m.getVariables()) {
            bloc(sb, VARIABLE, "nom", v.getNom(), "valeur", v.getValeur());
        }
        for (Objet o : m.getObjets()) {
            bloc(sb, OBJET, "nom", o.getNom(), "description", o.getDescription(),
                    "prenable", String.valueOf(o.isPrenable()), "image", o.getImage());
        }
        for (Salle s : m.getSalles()) {
            bloc(sb, SALLE, "nom", s.getNom(), "description", s.getDescription(), "image", s.getImage());
        }
        for (Lien l : m.getLiens()) {
            bloc(sb, LIEN, "depart", Textes.nom(l.getDepart(), ""),
                    "direction", l.getDirection() == null ? "" : l.getDirection().name(),
                    "arrivee", Textes.nom(l.getArrivee(), ""));
        }
        for (Association a : m.getAssociations()) {
            bloc(sb, ASSOCIATION, "salle", Textes.nom(a.getSalle(), ""), "objet", Textes.nom(a.getObjet(), ""),
                    "x", String.valueOf(a.getX()), "y", String.valueOf(a.getY()),
                    "visible", String.valueOf(a.isVisible()));
        }
        for (Regle r : m.getRegles()) {
            bloc(sb, REGLE, "verbe", r.getVerbe(), "complement", r.getComplement(),
                    "preposition", r.getPreposition(), "complementSecondaire", r.getComplementSecondaire(),
                    "salle", Textes.nom(r.getSalle(), ""),
                    "condition.variable", Textes.nom(r.getConditionVariable(), ""),
                    "condition.valeur", r.getConditionVariable() == null ? "" : r.getConditionValeur(),
                    "message", r.getMessage(),
                    "effet.variable", Textes.nom(r.getVariableModifiee(), ""),
                    "effet.valeur", r.getVariableModifiee() == null ? "" : r.getNouvelleValeur(),
                    "effet.objet.action", r.getActionObjet() == ActionObjet.AUCUNE ? "" : r.getActionObjet().name(),
                    "effet.objet", r.getActionObjet() == ActionObjet.AUCUNE ? "" : Textes.nom(r.getObjetCible(), ""),
                    "effet.salle", Textes.nom(r.getDeplacement(), ""));
        }
        return sb.toString();
    }

    /**
     * Écrit un bloc ; les valeurs vides sont omises
     * @param clesValeurs clé1, valeur1, clé2, valeur2...
     */
    private static void bloc(StringBuilder sb, String section, String... clesValeurs) {
        sb.append('[').append(section).append("]\n");
        for (int i = 0; i < clesValeurs.length; i += 2) {
            String valeur = clesValeurs[i + 1];
            if (!Textes.vide(valeur)) {
                sb.append(clesValeurs[i]).append(" = ").append(echapper(valeur)).append('\n');
            }
        }
        sb.append('\n');
    }

    static String echapper(String s) {
        return s.replace("\\", "\\\\").replace("\r", "").replace("\n", "\\n");
    }

    static String desechapper(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\' && i + 1 < s.length()) {
                char suivant = s.charAt(++i);
                sb.append(suivant == 'n' ? '\n' : suivant);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    // ------------------------------------------------------------------ lecture

    /** Bloc lu dans le script, avant résolution des références */
    private record Bloc(String section, int ligne, Map<String, String> valeurs, Map<String, Integer> lignes) {

        String get(String cle) {
            return valeurs.getOrDefault(cle, "");
        }

        int ligneDe(String cle) {
            return lignes.getOrDefault(cle, ligne);
        }

        String obligatoire(String cle) throws FormatJeuException {
            String v = get(cle);
            if (Textes.vide(v)) {
                throw new FormatJeuException("la clé \"" + cle + "\" est obligatoire dans [" + section + "]", ligne);
            }
            return v;
        }
    }

    /**
     * Lit un script et construit le jeu correspondant
     * @param texte Script
     * @return Nouveau modèle (non modifié)
     * @throws FormatJeuException Script invalide, avec le numéro de ligne
     */
    public static ModeleJeu lire(String texte) throws FormatJeuException {
        List<Bloc> blocs = decouper(texte);
        ModeleJeu m = new ModeleJeu();

        //Les éléments nommés d'abord, puis ceux qui les référencent
        for (Bloc b : blocs(blocs, VARIABLE)) {
            String nom = nomUnique(m.getVariables(), b, "variable");
            m.getVariables().add(new Variable(nom, b.get("valeur")));
        }
        for (Bloc b : blocs(blocs, OBJET)) {
            String nom = nomUnique(m.getObjets(), b, "objet");
            m.getObjets().add(new Objet(nom, b.get("description"), booleen(b, "prenable", false), b.get("image")));
        }
        for (Bloc b : blocs(blocs, SALLE)) {
            String nom = nomUnique(m.getSalles(), b, "salle");
            m.getSalles().add(new Salle(nom, b.get("description"), b.get("image")));
        }
        for (Bloc b : blocs(blocs, LIEN)) {
            m.getLiens().add(new Lien(
                    reference(m.getSalles(), b, "depart", true),
                    direction(b),
                    reference(m.getSalles(), b, "arrivee", true)));
        }
        for (Bloc b : blocs(blocs, ASSOCIATION)) {
            m.getAssociations().add(new Association(
                    reference(m.getSalles(), b, "salle", true),
                    reference(m.getObjets(), b, "objet", true),
                    entier(b, "x", Association.POSITION_MAX / 2),
                    entier(b, "y", Association.POSITION_MAX / 2),
                    booleen(b, "visible", true)));
        }
        for (Bloc b : blocs(blocs, REGLE)) {
            m.getRegles().add(regle(m, b));
        }

        List<Bloc> jeu = blocs(blocs, JEU);
        if (jeu.size() > 1) {
            throw new FormatJeuException("une seule section [jeu] est autorisée", jeu.get(1).ligne());
        }
        if (!jeu.isEmpty()) {
            Bloc b = jeu.get(0);
            m.setNom(b.get("nom"));
            if (!Textes.vide(b.get("salleDepart"))) {
                m.setSalleDepart(reference(m.getSalles(), b, "salleDepart", true));
            }
            int version = entier(b, "version", VERSION);
            if (version > VERSION) {
                throw new FormatJeuException("ce fichier a été créé par une version plus récente de l'éditeur (format "
                        + version + ")", b.ligneDe("version"));
            }
        }
        m.setModifie(false);
        return m;
    }

    private static Regle regle(ModeleJeu m, Bloc b) throws FormatJeuException {
        Regle r = new Regle();
        r.setVerbe(b.obligatoire("verbe"));
        r.setComplement(b.get("complement"));
        r.setPreposition(b.get("preposition"));
        r.setComplementSecondaire(b.get("complementSecondaire"));
        r.setSalle(reference(m.getSalles(), b, "salle", false));
        r.setConditionVariable(reference(m.getVariables(), b, "condition.variable", false));
        r.setConditionValeur(b.get("condition.valeur"));
        r.setMessage(b.get("message"));
        r.setVariableModifiee(reference(m.getVariables(), b, "effet.variable", false));
        r.setNouvelleValeur(b.get("effet.valeur"));
        String action = b.get("effet.objet.action");
        if (!Textes.vide(action)) {
            try {
                r.setActionObjet(ActionObjet.valueOf(action.toUpperCase(Locale.ROOT)));
            } catch (IllegalArgumentException e) {
                throw new FormatJeuException("action sur objet inconnue \"" + action + "\" (valeurs possibles : "
                        + List.of(ActionObjet.values()).stream().map(Enum::name).toList() + ")", b.ligneDe("effet.objet.action"));
            }
            r.setObjetCible(reference(m.getObjets(), b, "effet.objet", true));
        }
        r.setDeplacement(reference(m.getSalles(), b, "effet.salle", false));
        return r;
    }

    /**
     * Découpe le texte en blocs [section] cle = valeur
     */
    private static List<Bloc> decouper(String texte) throws FormatJeuException {
        List<Bloc> res = new ArrayList<>();
        Bloc courant = null;
        String[] lignes = texte.split("\r?\n", -1);
        for (int i = 0; i < lignes.length; i++) {
            int numero = i + 1;
            String ligne = lignes[i].strip();
            if (i == 0 && ligne.startsWith("﻿")) {
                ligne = ligne.substring(1);
            }
            if (ligne.isEmpty() || ligne.startsWith("#")) {
                continue;
            }
            if (ligne.startsWith("[")) {
                if (!ligne.endsWith("]")) {
                    throw new FormatJeuException("section mal formée \"" + ligne + "\", attendu [nom]", numero);
                }
                String section = ligne.substring(1, ligne.length() - 1).strip().toLowerCase(Locale.ROOT);
                if (!CLES.containsKey(section)) {
                    throw new FormatJeuException("section inconnue [" + section + "] (sections possibles : "
                            + String.join(", ", List.of(JEU, VARIABLE, OBJET, SALLE, LIEN, ASSOCIATION, REGLE)) + ")", numero);
                }
                courant = new Bloc(section, numero, new LinkedHashMap<>(), new LinkedHashMap<>());
                res.add(courant);
                continue;
            }
            int egal = ligne.indexOf('=');
            if (egal < 0) {
                throw new FormatJeuException("ligne incomprise \"" + ligne + "\", attendu : cle = valeur", numero);
            }
            if (courant == null) {
                throw new FormatJeuException("\"" + ligne + "\" doit se trouver dans une section, par exemple [objet]", numero);
            }
            String cle = ligne.substring(0, egal).strip();
            if (!CLES.get(courant.section()).contains(cle)) {
                throw new FormatJeuException("clé inconnue \"" + cle + "\" dans [" + courant.section() + "] (clés possibles : "
                        + String.join(", ", CLES.get(courant.section()).stream().sorted().toList()) + ")", numero);
            }
            if (courant.valeurs().containsKey(cle)) {
                throw new FormatJeuException("la clé \"" + cle + "\" est déjà définie dans ce bloc", numero);
            }
            courant.valeurs().put(cle, desechapper(ligne.substring(egal + 1).strip()));
            courant.lignes().put(cle, numero);
        }
        return res;
    }

    private static List<Bloc> blocs(List<Bloc> blocs, String section) {
        return blocs.stream().filter(b -> b.section().equals(section)).toList();
    }

    private static <T extends ElementNomme> String nomUnique(List<T> liste, Bloc b, String type) throws FormatJeuException {
        String nom = b.obligatoire("nom");
        if (ModeleJeu.chercher(liste, nom) != null) {
            throw new FormatJeuException("il existe déjà un(e) " + type + " nommé(e) \"" + nom + "\"", b.ligneDe("nom"));
        }
        return nom;
    }

    private static <T extends ElementNomme> T reference(List<T> liste, Bloc b, String cle, boolean obligatoire)
            throws FormatJeuException {
        String nom = obligatoire ? b.obligatoire(cle) : b.get(cle);
        if (Textes.vide(nom)) {
            return null;
        }
        T res = ModeleJeu.chercher(liste, nom);
        if (res == null) {
            throw new FormatJeuException("\"" + nom + "\" n'existe pas (clé " + cle + ")", b.ligneDe(cle));
        }
        return res;
    }

    private static Direction direction(Bloc b) throws FormatJeuException {
        String d = b.obligatoire("direction");
        try {
            return Direction.valueOf(d.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new FormatJeuException("direction inconnue \"" + d + "\" (NORD, EST, SUD ou OUEST)", b.ligneDe("direction"));
        }
    }

    private static int entier(Bloc b, String cle, int defaut) throws FormatJeuException {
        String v = b.get(cle);
        if (Textes.vide(v)) {
            return defaut;
        }
        try {
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {
            throw new FormatJeuException("\"" + v + "\" n'est pas un nombre entier (clé " + cle + ")", b.ligneDe(cle));
        }
    }

    private static boolean booleen(Bloc b, String cle, boolean defaut) throws FormatJeuException {
        String v = b.get(cle).toLowerCase(Locale.ROOT);
        return switch (v) {
            case "" -> defaut;
            case "true", "vrai", "oui" -> true;
            case "false", "faux", "non" -> false;
            default -> throw new FormatJeuException("\"" + v + "\" n'est pas une valeur oui/non (clé " + cle + ")", b.ligneDe(cle));
        };
    }
}
