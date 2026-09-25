package source.moteur.analyseur.analyse;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Outils de normalisation du texte saisi par le joueur :
 * minuscules, suppression des accents, découpage en mots,
 * réduction des formes conjuguées ou au pluriel.
 */
public final class Normaliseur {

    /** Terminaisons retirées pour obtenir le radical d'un verbe (les plus longues d'abord) */
    private static final List<String> TERMINAISONS_VERBE = Arrays.asList(
            "issons", "issez", "erons", "erez", "ons", "ez", "er", "ir", "re", "es", "is", "e", "s");

    private static final int LONGUEUR_MIN_RADICAL = 3;

    private Normaliseur() {
    }

    /**
     * Met en minuscules et retire les accents ("Clé" -> "cle", "œuf" -> "oeuf")
     * @param texte Texte brut
     * @return Texte sans accents
     */
    public static String sansAccents(String texte) {
        String res = texte.toLowerCase(Locale.FRENCH)
                .replace("œ", "oe")
                .replace("æ", "ae");
        res = Normalizer.normalize(res, Normalizer.Form.NFD);
        return res.replaceAll("\\p{M}", "");
    }

    /**
     * Normalise une expression pour la comparaison : sans accents,
     * apostrophes et tirets remplacés par des espaces ("l'épée" -> "l epee",
     * "prends-la" -> "prends la"), ponctuation supprimée, espaces réduits.
     * @param texte Texte brut
     * @return Texte normalisé
     */
    public static String normaliser(String texte) {
        return sansAccents(texte)
                .replaceAll("[^a-z0-9]+", " ")
                .trim();
    }

    /**
     * Découpe un texte normalisé en mots
     * @param texte Texte brut
     * @return Liste des mots (vide si le texte est vide)
     */
    public static List<String> decouper(String texte) {
        String norm = normaliser(texte);
        if (norm.isEmpty()) {
            return List.of();
        }
        return Arrays.asList(norm.split(" "));
    }

    /**
     * Radicaux possibles d'un verbe, en retirant chaque terminaison connue.
     * Deux formes d'un même verbe régulier partagent au moins un radical :
     * "ouvrir" {ouvrir, ouvr} et "ouvre" {ouvre, ouv, ouvr} ;
     * "manger" {manger, mang} et "mangeons" {mangeons, mange, mang, mangeon}.
     * Les verbes irréguliers (aller/va, prendre/prenez...) doivent être ajoutés en synonymes.
     * @param mot Mot normalisé
     * @return Ensemble des radicaux (contient toujours le mot lui-même)
     */
    public static Set<String> radicauxVerbe(String mot) {
        Set<String> res = new HashSet<>();
        res.add(mot);
        for (String terminaison : TERMINAISONS_VERBE) {
            if (mot.endsWith(terminaison) && mot.length() - terminaison.length() >= LONGUEUR_MIN_RADICAL) {
                String radical = mot.substring(0, mot.length() - terminaison.length());
                res.add(radical);
                if (radical.endsWith("e") && radical.length() > LONGUEUR_MIN_RADICAL) {
                    res.add(radical.substring(0, radical.length() - 1));
                }
            }
        }
        //Consonne doublée : "appelle" -> "appell" -> "appel", comme "appeler" -> "appel"
        for (String radical : Set.copyOf(res)) {
            int n = radical.length();
            if (n > LONGUEUR_MIN_RADICAL && radical.charAt(n - 1) == radical.charAt(n - 2)
                    && "aeiouy".indexOf(radical.charAt(n - 1)) < 0) {
                res.add(radical.substring(0, n - 1));
            }
        }
        return res;
    }

    /**
     * @return Vrai si les deux mots sont probablement deux formes du même verbe
     */
    public static boolean memeVerbe(String a, String b) {
        Set<String> radicaux = radicauxVerbe(a);
        radicaux.retainAll(radicauxVerbe(b));
        return !radicaux.isEmpty();
    }

    /**
     * Forme au singulier approximative d'un nom : "pommes" -> "pomme", "chevaux" -> "chevau"
     * (il suffit que les deux formes comparées se réduisent de la même façon)
     * @param mot Mot normalisé
     * @return Mot sans marque du pluriel
     */
    public static String singulier(String mot) {
        if (mot.length() > LONGUEUR_MIN_RADICAL && (mot.endsWith("s") || mot.endsWith("x"))) {
            return mot.substring(0, mot.length() - 1);
        }
        return mot;
    }

    /**
     * Distance de Levenshtein entre deux mots (nombre d'insertions,
     * suppressions ou substitutions pour passer de l'un à l'autre)
     */
    public static int distance(String a, String b) {
        int[] precedent = new int[b.length() + 1];
        int[] courant = new int[b.length() + 1];
        for (int j = 0; j <= b.length(); j++) {
            precedent[j] = j;
        }
        for (int i = 1; i <= a.length(); i++) {
            courant[0] = i;
            for (int j = 1; j <= b.length(); j++) {
                int cout = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
                courant[j] = Math.min(Math.min(courant[j - 1] + 1, precedent[j] + 1), precedent[j - 1] + cout);
            }
            int[] tmp = precedent;
            precedent = courant;
            courant = tmp;
        }
        return precedent[b.length()];
    }
}
