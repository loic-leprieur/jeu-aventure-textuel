package source.editeur.modele;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.ReadOnlyLongWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Données complètes d'un jeu en cours d'édition : salles, objets, liens,
 * associations, variables et règles.
 * <p>
 * Toute modification (ajout, suppression ou changement d'une propriété d'un élément)
 * incrémente {@link #versionProperty()} et passe {@link #modifieProperty()} à vrai :
 * les vues de l'éditeur s'en servent pour se mettre à jour.
 */
public class ModeleJeu {

    private static final ModeleJeu INSTANCE = new ModeleJeu();

    private final StringProperty nom = new SimpleStringProperty(this, "nom", "");
    private final ObjectProperty<Salle> salleDepart = new SimpleObjectProperty<>(this, "salleDepart");

    private final ObservableList<Variable> variables = creerListe();
    private final ObservableList<Objet> objets = creerListe();
    private final ObservableList<Salle> salles = creerListe();
    private final ObservableList<Lien> liens = creerListe();
    private final ObservableList<Association> associations = creerListe();
    private final ObservableList<Regle> regles = creerListe();

    private final ReadOnlyLongWrapper version = new ReadOnlyLongWrapper(this, "version");
    private final BooleanProperty modifie = new SimpleBooleanProperty(this, "modifie", false);

    public ModeleJeu() {
        for (ObservableList<? extends ElementJeu> liste : List.of(variables, objets, salles, liens, associations, regles)) {
            liste.addListener((ListChangeListener<ElementJeu>) c -> signalerModification());
        }
        nom.addListener(o -> signalerModification());
        salleDepart.addListener(o -> signalerModification());
        //La première salle créée devient la salle de départ
        salles.addListener((ListChangeListener<Salle>) c -> {
            if (getSalleDepart() == null && !salles.isEmpty()) {
                setSalleDepart(salles.get(0));
            }
        });
    }

    /**
     * @return Modèle du jeu édité par l'application
     */
    public static ModeleJeu get() {
        return INSTANCE;
    }

    private static <T extends ElementJeu> ObservableList<T> creerListe() {
        //l'extracteur signale aussi les changements de propriétés des éléments
        return FXCollections.observableArrayList(ElementJeu::proprietes);
    }

    private void signalerModification() {
        version.set(version.get() + 1);
        modifie.set(true);
    }

    public StringProperty nomProperty() { return nom; }
    public ObjectProperty<Salle> salleDepartProperty() { return salleDepart; }
    public ReadOnlyLongProperty versionProperty() { return version.getReadOnlyProperty(); }
    public BooleanProperty modifieProperty() { return modifie; }

    public String getNom() { return nom.get(); }
    public Salle getSalleDepart() { return salleDepart.get(); }
    public boolean isModifie() { return modifie.get(); }

    public void setNom(String n) { nom.set(Textes.nettoyer(n)); }
    public void setSalleDepart(Salle s) { salleDepart.set(s); }
    public void setModifie(boolean m) { modifie.set(m); }

    public ObservableList<Variable> getVariables() { return variables; }
    public ObservableList<Objet> getObjets() { return objets; }
    public ObservableList<Salle> getSalles() { return salles; }
    public ObservableList<Lien> getLiens() { return liens; }
    public ObservableList<Association> getAssociations() { return associations; }
    public ObservableList<Regle> getRegles() { return regles; }

    /**
     * Vide le jeu (Fichier > Nouveau)
     */
    public void vider() {
        regles.clear();
        associations.clear();
        liens.clear();
        salles.clear();
        objets.clear();
        variables.clear();
        setSalleDepart(null);
        setNom("");
        setModifie(false);
    }

    /**
     * Remplace le contenu de ce modèle par celui d'un autre (chargement d'un fichier)
     * @param autre Modèle chargé
     */
    public void remplacerPar(ModeleJeu autre) {
        vider();
        variables.setAll(autre.variables);
        objets.setAll(autre.objets);
        salles.setAll(autre.salles);
        liens.setAll(autre.liens);
        associations.setAll(autre.associations);
        regles.setAll(autre.regles);
        setSalleDepart(autre.getSalleDepart());
        setNom(autre.getNom());
        setModifie(false);
    }

    /**
     * Recherche un élément par son nom (sans tenir compte de la casse)
     * @return Élément, null si absent
     */
    public static <T extends ElementNomme> T chercher(List<T> liste, String nom) {
        for (T e : liste) {
            if (e.getNom().equalsIgnoreCase(Textes.nettoyer(nom))) {
                return e;
            }
        }
        return null;
    }

    /**
     * Indique si un nom est déjà pris par un autre élément de la liste
     * @param exclu Élément en cours de modification (ignoré), peut être null
     */
    public static <T extends ElementNomme> boolean nomUtilise(List<T> liste, String nom, T exclu) {
        T trouve = chercher(liste, nom);
        return trouve != null && trouve != exclu;
    }

    /**
     * @return Liens partant d'une salle
     */
    public List<Lien> liensDepuis(Salle s) {
        return filtrer(liens, l -> l.getDepart() == s);
    }

    /**
     * @return Objets placés dans une salle
     */
    public List<Association> associationsDe(Salle s) {
        return filtrer(associations, a -> a.getSalle() == s);
    }

    /**
     * Décrit les éléments qui dépendent de e et seront supprimés ou modifiés avec lui
     * @return Liste de descriptions ("2 lien(s)"...), vide si aucune dépendance
     */
    public List<String> dependances(ElementJeu e) {
        List<String> res = new ArrayList<>();
        ajouterCompte(res, filtrer(liens, l -> l.getDepart() == e || l.getArrivee() == e).size(), "lien(s) supprimé(s)");
        ajouterCompte(res, filtrer(associations, a -> a.getSalle() == e || a.getObjet() == e).size(), "placement(s) d'objet supprimé(s)");
        ajouterCompte(res, filtrer(regles, r -> utilise(r, e)).size(), "règle(s) modifiée(s)");
        if (e != null && e == getSalleDepart()) {
            res.add("la salle de départ ne sera plus définie");
        }
        return res;
    }

    private static void ajouterCompte(List<String> res, int n, String libelle) {
        if (n > 0) {
            res.add(n + " " + libelle);
        }
    }

    private static boolean utilise(Regle r, ElementJeu e) {
        return r.getSalle() == e || r.getDeplacement() == e || r.getObjetCible() == e
                || r.getConditionVariable() == e || r.getVariableModifiee() == e;
    }

    /**
     * Supprime un élément et tout ce qui en dépend :
     * les liens et placements qui l'utilisent sont supprimés,
     * les règles qui y font référence perdent la condition ou l'effet concerné.
     */
    public void supprimer(ElementJeu e) {
        liens.removeIf(l -> l.getDepart() == e || l.getArrivee() == e);
        associations.removeIf(a -> a.getSalle() == e || a.getObjet() == e);
        for (Regle r : regles) {
            if (r.getSalle() == e) {
                r.setSalle(null);
            }
            if (r.getDeplacement() == e) {
                r.setDeplacement(null);
            }
            if (r.getObjetCible() == e) {
                r.setObjetCible(null);
                r.setActionObjet(ActionObjet.AUCUNE);
            }
            if (r.getConditionVariable() == e) {
                r.setConditionVariable(null);
                r.setConditionValeur("");
            }
            if (r.getVariableModifiee() == e) {
                r.setVariableModifiee(null);
                r.setNouvelleValeur("");
            }
        }
        if (e == getSalleDepart()) {
            setSalleDepart(null);
        }
        salles.remove(e);
        objets.remove(e);
        variables.remove(e);
        liens.remove(e);
        associations.remove(e);
        regles.remove(e);
        if (getSalleDepart() == null && !salles.isEmpty()) {
            setSalleDepart(salles.get(0));
        }
    }

    private static <T> List<T> filtrer(List<T> liste, Predicate<T> p) {
        return liste.stream().filter(p).toList();
    }
}
