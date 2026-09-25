package source.moteur.regles;

import source.editeur.modele.ActionObjet;

/**
 * Règle du jeu telle qu'utilisée par le moteur.
 * <p>
 * Les mots de la commande (verbe, compléments, préposition) sont sous forme canonique :
 * le libellé du mot correspondant dans le dictionnaire, normalisé (minuscules, sans accents),
 * pour être comparés directement à ceux d'une phrase analysée. Chaîne vide = absent.
 * Les références (salle, variable, objet) sont des noms, null si absentes.
 *
 * @param verbe Verbe de la commande
 * @param complement Complément de la commande
 * @param preposition Préposition ; vide = n'importe laquelle
 * @param complementSecondaire Complément après la préposition
 * @param salle Condition : salle où se trouve le joueur
 * @param conditionVariable Condition : nom de la variable
 * @param conditionValeur Condition : valeur attendue
 * @param message Message affiché
 * @param variableModifiee Effet : variable modifiée
 * @param nouvelleValeur Effet : nouvelle valeur
 * @param actionObjet Effet sur un objet
 * @param objetCible Objet concerné par l'effet
 * @param deplacement Effet : salle où le joueur est déplacé
 */
public record RegleJeu(String verbe, String complement, String preposition, String complementSecondaire,
                       String salle, String conditionVariable, String conditionValeur,
                       String message, String variableModifiee, String nouvelleValeur,
                       ActionObjet actionObjet, String objetCible, String deplacement) {

    /**
     * Nombre de conditions : les règles les plus précises sont essayées en premier
     */
    public int precision() {
        return (salle != null ? 1 : 0) + (conditionVariable != null ? 1 : 0);
    }
}
