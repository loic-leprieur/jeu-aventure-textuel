package source.editeur.modele;

import source.moteur.Direction;

/**
 * Petites fonctions de mise en forme des textes du modèle
 */
public final class Textes {

    private Textes() {
    }

    /**
     * @return Texte sans espaces superflus, jamais null
     */
    public static String nettoyer(String s) {
        return s == null ? "" : s.trim();
    }

    /**
     * @return Vrai si le texte est null ou vide
     */
    public static boolean vide(String s) {
        return s == null || s.isBlank();
    }

    /**
     * @return Libellé d'une direction : "Nord", "Est"...
     */
    public static String libelle(Direction d) {
        if (d == null) {
            return "";
        }
        String n = d.name().toLowerCase();
        return Character.toUpperCase(n.charAt(0)) + n.substring(1);
    }

    /**
     * @return Nom d'un élément, ou texteSiNull
     */
    public static String nom(ElementNomme e, String texteSiNull) {
        return e == null ? texteSiNull : e.getNom();
    }
}
