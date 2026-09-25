package source.editeur.images;

import javafx.scene.image.Image;
import source.editeur.modele.FichierJeu;
import source.util.UtilEditor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Accès aux images de l'éditeur : dossier, import de nouvelles images, miniatures
 */
public final class GestionImages {

    /** Extensions d'images acceptées */
    public static final List<String> EXTENSIONS = List.of("png", "jpg", "jpeg", "gif", "bmp");

    private static final Map<String, Image> CACHE = new HashMap<>();

    private GestionImages() {
    }

    /**
     * @return Dossier racine des images (contient objets/ et salles/)
     */
    public static Path dossierRacine() {
        return Paths.get(UtilEditor.cheminImage);
    }

    /**
     * @return Dossier des images d'un type
     */
    public static Path dossier(UtilEditor.ImageType type) {
        return dossierRacine().resolve(type == UtilEditor.ImageType.OBJET ? FichierJeu.DOSSIER_OBJETS : FichierJeu.DOSSIER_SALLES);
    }

    /**
     * @return Vrai si le fichier a une extension d'image acceptée
     */
    public static boolean estImage(String nomFichier) {
        int point = nomFichier.lastIndexOf('.');
        return point > 0 && EXTENSIONS.contains(nomFichier.substring(point + 1).toLowerCase(Locale.ROOT));
    }

    /**
     * Copie une image choisie par l'utilisateur dans le dossier d'images de l'éditeur.
     * Si une image identique existe déjà, elle est réutilisée ; si une image différente
     * porte le même nom, la nouvelle est renommée.
     * @param source Fichier image
     * @param type Objet ou Salle
     * @return Nom de l'image dans le dossier de l'éditeur
     */
    public static String importer(File source, UtilEditor.ImageType type) throws IOException {
        Path dossier = dossier(type);
        Files.createDirectories(dossier);
        String nom = FichierJeu.copierSansEcraser(Files.readAllBytes(source.toPath()), dossier, source.getName());
        ObservableListImage.rafraichirImage();
        return nom;
    }

    /**
     * Charge une image redimensionnée (mise en cache)
     * @param type Objet ou Salle
     * @param nom Nom du fichier
     * @param taille Taille maximale (largeur et hauteur), 0 pour la taille réelle
     * @return Image, null si le fichier n'existe pas
     */
    public static Image charger(UtilEditor.ImageType type, String nom, double taille) {
        if (nom == null || nom.isBlank()) {
            return null;
        }
        Path fichier = dossier(type).resolve(nom);
        String cle = fichier.toAbsolutePath() + "@" + taille;
        Image image = CACHE.get(cle);
        if (image == null) {
            if (!Files.isRegularFile(fichier)) {
                return null;
            }
            image = new Image(fichier.toUri().toString(), taille, taille, true, true, false);
            CACHE.put(cle, image);
        }
        return image;
    }

    /**
     * Oublie les images chargées (après modification des fichiers sur le disque)
     */
    public static void viderCache() {
        CACHE.clear();
    }
}
