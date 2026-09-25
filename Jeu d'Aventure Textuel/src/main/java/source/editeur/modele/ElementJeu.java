package source.editeur.modele;

import javafx.beans.Observable;

/**
 * Élément d'un jeu (salle, objet, lien...) : expose ses propriétés
 * pour que le modèle soit prévenu de toute modification.
 */
public interface ElementJeu {

    /**
     * @return Propriétés observables de l'élément
     */
    Observable[] proprietes();
}
