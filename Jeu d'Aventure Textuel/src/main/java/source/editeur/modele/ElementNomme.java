package source.editeur.modele;

import javafx.beans.property.StringProperty;

/**
 * Élément identifié par un nom unique (salle, objet, variable)
 */
public interface ElementNomme extends ElementJeu {

    StringProperty nomProperty();

    default String getNom() {
        return nomProperty().get();
    }
}
