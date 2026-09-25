package source.moteur;

import javafx.scene.image.Image;
import source.Niveau;
import source.Objet;
import source.Salle;
import source.editeur.modele.ActionObjet;
import source.editeur.modele.Association;
import source.editeur.modele.FichierJeu;
import source.editeur.modele.FormatJeu;
import source.editeur.modele.FormatJeuException;
import source.editeur.modele.Lien;
import source.editeur.modele.ModeleJeu;
import source.editeur.modele.Regle;
import source.editeur.modele.Textes;
import source.editeur.modele.Variable;
import source.moteur.analyseur.analyse.Normaliseur;
import source.moteur.analyseur.dictionnaire.Complement;
import source.moteur.analyseur.dictionnaire.Dictionnaire;
import source.moteur.analyseur.dictionnaire.Mot;
import source.moteur.analyseur.dictionnaire.Preposition;
import source.moteur.analyseur.dictionnaire.Type;
import source.moteur.analyseur.dictionnaire.Verbe;
import source.moteur.exception.JeuInvalideException;
import source.moteur.regles.RegleJeu;
import source.util.UtilEditor;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Crée une partie à partir des données d'un jeu (fichier .aventure, ou jeu en cours d'édition)
 */
public final class ChargeurJeu {

    private static final String DEMO = "/source/moteur/demo/demo.txt";
    private static final List<String> IMAGES_DEMO = List.of(
            "objets/obj_cle.png", "objets/obj_feuille.png", "salles/sal_bureau.jpg", "salles/sal_chambre.jpg");

    //Dossiers temporaires des images des jeux ouverts, supprimés à la fermeture
    private static final List<Path> TEMPORAIRES = new ArrayList<>();

    /**
     * Jeu prêt à être lancé (et relancé) : ses données et le dossier de ses images
     */
    public record JeuCharge(ModeleJeu modele, Path dossierImages) {

        /**
         * Crée une nouvelle partie de ce jeu
         */
        public MoteurJeu nouvellePartie() throws JeuInvalideException {
            return creer(modele, dossierImages);
        }
    }

    private ChargeurJeu() {
    }

    /**
     * Ouvre un fichier de jeu. Les images d'un fichier .aventure sont extraites dans un dossier temporaire ;
     * pour un script texte, elles sont cherchées dans le dossier images/ voisin, sinon dans celui de l'éditeur.
     */
    public static JeuCharge ouvrir(Path fichier) throws IOException, FormatJeuException {
        Path dossier;
        if (FichierJeu.estZip(fichier)) {
            dossier = dossierTemporaire();
        } else {
            Path voisin = fichier.toAbsolutePath().getParent().resolve("images");
            dossier = Files.isDirectory(voisin) ? voisin : Paths.get(UtilEditor.cheminImage);
        }
        return new JeuCharge(FichierJeu.ouvrir(fichier, dossier), dossier);
    }

    /**
     * @return Petit jeu de démonstration intégré à l'application
     */
    public static JeuCharge demo() throws IOException, FormatJeuException {
        Path dossier = dossierTemporaire();
        for (String image : IMAGES_DEMO) {
            Path cible = dossier.resolve(image);
            Files.createDirectories(cible.getParent());
            UtilEditor.copierRessource("/source/editeur/images/" + image, cible.toFile());
        }
        return new JeuCharge(FormatJeu.lire(scriptDemo()), dossier);
    }

    /**
     * @return Script du jeu de démonstration
     */
    public static String scriptDemo() throws IOException {
        try (InputStream in = ChargeurJeu.class.getResourceAsStream(DEMO)) {
            if (in == null) {
                throw new IOException("jeu de démonstration introuvable : " + DEMO);
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private static synchronized Path dossierTemporaire() throws IOException {
        if (TEMPORAIRES.isEmpty()) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> TEMPORAIRES.forEach(ChargeurJeu::supprimer)));
        }
        Path dossier = Files.createTempDirectory("aventure-");
        TEMPORAIRES.add(dossier);
        return dossier;
    }

    private static void supprimer(Path dossier) {
        try (Stream<Path> chemins = Files.walk(dossier)) {
            chemins.sorted(Comparator.reverseOrder()).forEach(p -> p.toFile().delete());
        } catch (IOException | UncheckedIOException e) {
            //Dossier temporaire : tant pis s'il reste
        }
    }

    /**
     * Crée une partie en chargeant les images depuis un dossier (contenant objets/ et salles/)
     */
    public static MoteurJeu creer(ModeleJeu m, Path dossierImages) throws JeuInvalideException {
        Map<Path, Image> cache = new HashMap<>();
        Function<Path, Image> charger = p -> Files.isRegularFile(p)
                ? cache.computeIfAbsent(p, f -> new Image(f.toUri().toString()))
                : null;
        return creer(m,
                nom -> nom.isBlank() ? null : charger.apply(dossierImages.resolve(FichierJeu.DOSSIER_SALLES).resolve(nom)),
                nom -> nom.isBlank() ? null : charger.apply(dossierImages.resolve(FichierJeu.DOSSIER_OBJETS).resolve(nom)));
    }

    /**
     * Crée une partie
     * @param m Données du jeu (non modifiées : la partie en est une copie)
     * @param imageSalle Chargement de l'image d'une salle d'après son nom de fichier (peut renvoyer null)
     * @param imageObjet Chargement de l'image d'un objet d'après son nom de fichier (peut renvoyer null)
     * @throws JeuInvalideException Le jeu n'a aucune salle
     */
    public static MoteurJeu creer(ModeleJeu m, Function<String, Image> imageSalle, Function<String, Image> imageObjet)
            throws JeuInvalideException {
        if (m.getSalles().isEmpty()) {
            throw new JeuInvalideException("Le jeu ne contient aucune salle : ajoutez-en au moins une.");
        }

        //Salles et passages
        Map<source.editeur.modele.Salle, Salle> salles = new LinkedHashMap<>();
        for (source.editeur.modele.Salle s : m.getSalles()) {
            salles.put(s, new Salle(s.getNom(), s.getDescription(), imageSalle.apply(s.getImage())));
        }
        for (Lien l : m.getLiens()) {
            if (l.getDepart() != null && l.getArrivee() != null && l.getDirection() != null) {
                salles.get(l.getDepart()).ajouterLien(l.getDirection(), salles.get(l.getArrivee()));
            }
        }

        //Objets : un exemplaire par placement dans une salle, plus un exemplaire de réserve (catalogue)
        Map<String, Objet> catalogue = new LinkedHashMap<>();
        for (source.editeur.modele.Objet o : m.getObjets()) {
            catalogue.put(Normaliseur.normaliser(o.getNom()), new Objet(o.getNom(), o.getDescription(), o.isPrenable(),
                    true, imageObjet.apply(o.getImage()), Association.POSITION_MAX / 2, Association.POSITION_MAX * 4 / 5));
        }
        for (Association a : m.getAssociations()) {
            source.editeur.modele.Objet o = a.getObjet();
            if (o != null && a.getSalle() != null) {
                salles.get(a.getSalle()).getObjets().add(new Objet(o.getNom(), o.getDescription(), o.isPrenable(),
                        a.isVisible(), imageObjet.apply(o.getImage()), a.getX(), a.getY()));
            }
        }

        source.editeur.modele.Salle depart = m.getSalleDepart() != null ? m.getSalleDepart() : m.getSalles().get(0);
        Niveau niveau = new Niveau(m.getNom(), new ArrayList<>(salles.values()), salles.get(depart));
        for (Variable v : m.getVariables()) {
            niveau.setVariable(v.getNom(), v.getValeur());
        }

        Dictionnaire dico = creerDictionnaire(m);
        List<RegleJeu> regles = new ArrayList<>();
        for (Regle r : m.getRegles()) {
            regles.add(new RegleJeu(
                    MoteurJeu.canonique(dico, Type.verbe, r.getVerbe()),
                    MoteurJeu.canonique(dico, Type.complement, r.getComplement()),
                    MoteurJeu.canonique(dico, Type.preposition, r.getPreposition()),
                    MoteurJeu.canonique(dico, Type.complement, r.getComplementSecondaire()),
                    nom(r.getSalle()), nom(r.getConditionVariable()), r.getConditionValeur(),
                    r.getMessage(), nom(r.getVariableModifiee()), r.getNouvelleValeur(),
                    r.getActionObjet(),
                    r.getActionObjet() == ActionObjet.AUCUNE ? null : nom(r.getObjetCible()),
                    nom(r.getDeplacement())));
        }
        return new MoteurJeu(niveau, dico, regles, catalogue);
    }

    private static String nom(source.editeur.modele.ElementNomme e) {
        return e == null ? null : e.getNom();
    }

    /**
     * Dictionnaire du jeu : vocabulaire commun, noms des objets et des salles, mots des règles
     */
    static Dictionnaire creerDictionnaire(ModeleJeu m) {
        Dictionnaire dico = new Dictionnaire(new ArrayList<>());
        VocabulaireParDefaut.ajouter(dico);
        for (source.editeur.modele.Objet o : m.getObjets()) {
            ajouterSiAbsent(dico, new Complement(o.getNom()));
        }
        for (source.editeur.modele.Salle s : m.getSalles()) {
            ajouterSiAbsent(dico, new Complement(s.getNom()));
        }
        for (Regle r : m.getRegles()) {
            ajouterSiAbsent(dico, new Verbe(r.getVerbe()));
            ajouterSiAbsent(dico, new Complement(r.getComplement()));
            ajouterSiAbsent(dico, new Preposition(r.getPreposition()));
            ajouterSiAbsent(dico, new Complement(r.getComplementSecondaire()));
        }
        return dico;
    }

    private static void ajouterSiAbsent(Dictionnaire dico, Mot mot) {
        Type type = Type.valueOf(mot.getType());
        if (!Textes.vide(mot.getLibelle()) && dico.rechercher(type, mot.getLibelle(), false) == null) {
            dico.ajouterMot(mot);
        }
    }
}
