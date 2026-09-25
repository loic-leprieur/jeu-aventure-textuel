package source.editeur.modele;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Fichier de jeu .aventure : une archive zip contenant le script du jeu (jeu.txt)
 * et les images qu'il utilise (images/objets/..., images/salles/...),
 * pour pouvoir partager un jeu complet en un seul fichier.
 * <p>
 * Un script texte seul (.txt) peut aussi être ouvert.
 */
public final class FichierJeu {

    public static final String EXTENSION = "aventure";
    public static final String DOSSIER_OBJETS = "objets";
    public static final String DOSSIER_SALLES = "salles";

    static final String ENTREE_SCRIPT = "jeu.txt";
    private static final String PREFIXE_IMAGES = "images/";

    private FichierJeu() {
    }

    /**
     * Enregistre le jeu. Le fichier est d'abord écrit à côté puis renommé,
     * pour ne jamais laisser un fichier à moitié écrit en cas d'erreur.
     * @param m Jeu
     * @param fichier Fichier .aventure
     * @param dossierImages Dossier contenant les sous-dossiers objets/ et salles/
     */
    public static void enregistrer(ModeleJeu m, Path fichier, Path dossierImages) throws IOException {
        Path cible = fichier.toAbsolutePath();
        Path temp = Files.createTempFile(cible.getParent(), "enregistrement", ".tmp");
        try {
            try (ZipOutputStream zip = new ZipOutputStream(Files.newOutputStream(temp))) {
                zip.putNextEntry(new ZipEntry(ENTREE_SCRIPT));
                zip.write(FormatJeu.ecrire(m).getBytes(StandardCharsets.UTF_8));
                zip.closeEntry();
                ecrireImages(zip, DOSSIER_OBJETS, imagesObjets(m), dossierImages);
                ecrireImages(zip, DOSSIER_SALLES, imagesSalles(m), dossierImages);
            }
            try {
                Files.move(temp, cible, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temp, cible, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temp);
        }
    }

    private static void ecrireImages(ZipOutputStream zip, String type, Set<String> noms, Path dossierImages) throws IOException {
        for (String nom : noms) {
            Path image = dossierImages.resolve(type).resolve(nom);
            if (Files.isRegularFile(image)) {
                zip.putNextEntry(new ZipEntry(PREFIXE_IMAGES + type + "/" + nom));
                Files.copy(image, zip);
                zip.closeEntry();
            }
        }
    }

    /**
     * @return Images utilisées par le jeu mais absentes du dossier d'images
     * (elles ne pourront pas être enregistrées dans le fichier)
     */
    public static List<String> imagesManquantes(ModeleJeu m, Path dossierImages) {
        return java.util.stream.Stream.concat(
                        imagesObjets(m).stream().map(n -> DOSSIER_OBJETS + "/" + n),
                        imagesSalles(m).stream().map(n -> DOSSIER_SALLES + "/" + n))
                .filter(chemin -> !Files.isRegularFile(dossierImages.resolve(chemin)))
                .toList();
    }

    private static Set<String> imagesObjets(ModeleJeu m) {
        Set<String> res = new LinkedHashSet<>();
        m.getObjets().stream().map(Objet::getImage).filter(n -> !Textes.vide(n)).forEach(res::add);
        return res;
    }

    private static Set<String> imagesSalles(ModeleJeu m) {
        Set<String> res = new LinkedHashSet<>();
        m.getSalles().stream().map(Salle::getImage).filter(n -> !Textes.vide(n)).forEach(res::add);
        return res;
    }

    /**
     * Ouvre un fichier de jeu. Les images qu'il contient sont copiées dans le dossier d'images ;
     * si une image différente porte déjà le même nom, l'image du jeu est renommée (nom_2.png...).
     * @param fichier Fichier .aventure (ou script .txt)
     * @param dossierImages Dossier contenant les sous-dossiers objets/ et salles/
     * @return Jeu lu (non modifié)
     * @throws FormatJeuException Script invalide
     */
    public static ModeleJeu ouvrir(Path fichier, Path dossierImages) throws IOException, FormatJeuException {
        String script = null;
        Map<String, byte[]> objets = new HashMap<>();
        Map<String, byte[]> salles = new HashMap<>();

        if (estZip(fichier)) {
            try (ZipInputStream zip = new ZipInputStream(Files.newInputStream(fichier))) {
                ZipEntry entree;
                while ((entree = zip.getNextEntry()) != null) {
                    String nom = entree.getName().replace('\\', '/');
                    if (entree.isDirectory()) {
                        continue;
                    }
                    if (nom.equals(ENTREE_SCRIPT)) {
                        script = new String(lireTout(zip), StandardCharsets.UTF_8);
                    } else if (nom.startsWith(PREFIXE_IMAGES + DOSSIER_OBJETS + "/")) {
                        objets.put(nomFichier(nom), lireTout(zip));
                    } else if (nom.startsWith(PREFIXE_IMAGES + DOSSIER_SALLES + "/")) {
                        salles.put(nomFichier(nom), lireTout(zip));
                    }
                }
            }
            if (script == null) {
                throw new FormatJeuException("le fichier ne contient pas de script de jeu (" + ENTREE_SCRIPT + ")", 0);
            }
        } else {
            script = Files.readString(fichier, StandardCharsets.UTF_8);
        }

        //On lit le script avant de copier les images : un script invalide ne touche pas au dossier d'images
        ModeleJeu m = FormatJeu.lire(script);

        Map<String, String> renommesObjets = importerImages(objets, dossierImages.resolve(DOSSIER_OBJETS));
        Map<String, String> renommesSalles = importerImages(salles, dossierImages.resolve(DOSSIER_SALLES));
        for (Objet o : m.getObjets()) {
            o.setImage(renommesObjets.getOrDefault(o.getImage(), o.getImage()));
        }
        for (Salle s : m.getSalles()) {
            s.setImage(renommesSalles.getOrDefault(s.getImage(), s.getImage()));
        }
        m.setModifie(false);
        return m;
    }

    /**
     * @return Vrai si le fichier est une archive zip (fichier .aventure), faux pour un script texte
     */
    public static boolean estZip(Path fichier) throws IOException {
        try (InputStream in = Files.newInputStream(fichier)) {
            byte[] entete = in.readNBytes(4);
            return entete.length == 4 && entete[0] == 'P' && entete[1] == 'K' && entete[2] == 3 && entete[3] == 4;
        }
    }

    private static byte[] lireTout(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        in.transferTo(out);
        return out.toByteArray();
    }

    /**
     * Garde seulement le nom du fichier (protège contre les chemins "../" dans l'archive)
     */
    private static String nomFichier(String chemin) {
        return chemin.substring(chemin.lastIndexOf('/') + 1);
    }

    /**
     * Copie des images dans un dossier
     * @return Correspondance ancien nom -> nouveau nom pour les images renommées
     */
    private static Map<String, String> importerImages(Map<String, byte[]> images, Path dossier) throws IOException {
        Map<String, String> renommes = new HashMap<>();
        if (images.isEmpty()) {
            return renommes;
        }
        Files.createDirectories(dossier);
        for (Map.Entry<String, byte[]> e : images.entrySet()) {
            if (e.getKey().isEmpty()) {
                continue;
            }
            String nom = copierSansEcraser(e.getValue(), dossier, e.getKey());
            if (!nom.equals(e.getKey())) {
                renommes.put(e.getKey(), nom);
            }
        }
        return renommes;
    }

    /**
     * Copie un contenu dans un dossier sans écraser une image différente de même nom :
     * si le nom est pris par une image identique, elle est réutilisée ;
     * sinon le contenu est écrit sous un nom libre (image_2.png, image_3.png...).
     * @return Nom du fichier utilisé
     */
    public static String copierSansEcraser(byte[] contenu, Path dossier, String nom) throws IOException {
        int point = nom.lastIndexOf('.');
        String base = point > 0 ? nom.substring(0, point) : nom;
        String extension = point > 0 ? nom.substring(point) : "";
        String candidat = nom;
        for (int i = 2; ; i++) {
            Path cible = dossier.resolve(candidat);
            if (!Files.exists(cible)) {
                try (OutputStream out = Files.newOutputStream(cible)) {
                    out.write(contenu);
                }
                return candidat;
            }
            if (Arrays.equals(Files.readAllBytes(cible), contenu)) {
                return candidat;
            }
            candidat = base + "_" + i + extension;
        }
    }
}
