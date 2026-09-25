package source.editeur.util;

/**
 * Classe contenant les constantes de l'éditeur
 */
public class Constante {

    public static final int TAILLE_NOM_MIN = 2;
    public static final int TAILLE_NOM_MAX = 30;
    public static final int TAILLE_DESCRIPTION_MAX = 300;
    public static final int TAILLE_VALEUR_MAX = 50;
    public static final int TAILLE_MESSAGE_MAX = 500;

    /**
     * Vérifie la longueur d'un nom
     * @param quoi "Le nom de l'objet"...
     * @return Message d'erreur, null si correct
     */
    public static String verifierNom(String quoi, String nom) {
        int taille = nom.trim().length();
        if (taille < TAILLE_NOM_MIN || taille > TAILLE_NOM_MAX) {
            return quoi + " doit contenir entre " + TAILLE_NOM_MIN + " et " + TAILLE_NOM_MAX
                    + " caractères (actuellement " + taille + ").";
        }
        return null;
    }

    /**
     * Vérifie la longueur maximale d'un texte
     * @return Message d'erreur, null si correct
     */
    public static String verifierLongueur(String quoi, String texte, int max) {
        int taille = texte.trim().length();
        if (taille > max) {
            return quoi + " ne doit pas dépasser " + max + " caractères (actuellement " + taille + ").";
        }
        return null;
    }

    /**
     * @return Première erreur non nulle, null s'il n'y en a pas
     */
    public static String premiere(String... erreurs) {
        for (String e : erreurs) {
            if (e != null) {
                return e;
            }
        }
        return null;
    }
}
