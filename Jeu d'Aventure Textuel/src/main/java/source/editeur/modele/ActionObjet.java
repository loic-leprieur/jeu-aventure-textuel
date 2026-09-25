package source.editeur.modele;

/**
 * Effet d'une règle sur un objet
 */
public enum ActionObjet {
    AUCUNE("Aucune"),
    DONNER("Mettre dans l'inventaire"),
    RETIRER("Retirer de l'inventaire"),
    AFFICHER("Faire apparaître"),
    CACHER("Faire disparaître");

    private final String libelle;

    ActionObjet(String libelle) {
        this.libelle = libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }
}
