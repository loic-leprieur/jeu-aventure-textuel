package source.editeur;


import javafx.application.Application;
import javafx.stage.Stage;
import source.editeur.images.ObservableListImage;
import source.util.UtilEditor;

import java.io.File;

/**
 * Classe principale, lancement de l'application
 */
public class Principal extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setUserAgentStylesheet(STYLESHEET_MODENA);
        chargerFichier();
        new PrincipalFrame();
    }

    private static final String[] IMAGES_OBJETS_PAR_DEFAUT = {"obj_cle.png", "obj_feuille.png"};
    private static final String[] IMAGES_SALLES_PAR_DEFAUT = {"sal_bureau.jpg", "sal_chambre.jpg"};

    /**
     * Charge les fichiers d'images par défaut si inexistant
     */
    private void chargerFichier(){
        String urlCourante = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
        String urlDossier = "";
        String pathTab[] = urlCourante.split("/");

        //On regarde si on execute l'application depuis un jar
        //pour définir le chemin du dossier d'image a créer
        if(pathTab[pathTab.length-1].equalsIgnoreCase("4LEditor.jar")){
            for(int i =0;i<pathTab.length-1;i++){
                urlDossier += pathTab[i] + "/";
            }
        }
        urlDossier += "4LEditor";
        File f = new File(urlDossier);

        //Si le dossier des images n'existe pas on le créer
        if(!f.exists()){
            f.mkdir();
            f = new File(urlDossier + "/images");
            f.mkdir();
            f = new File(urlDossier + "/images/objets");
            f.mkdir();
            f = new File(urlDossier + "/images/salles");
            f.mkdir();

            //On copie les images par défaut embarquées dans le classpath (jar ou target/classes)
            for(String s : IMAGES_OBJETS_PAR_DEFAUT){
                UtilEditor.copierRessource("/source/editeur/images/objets/" + s, new File(urlDossier + "/images/objets/" + s));
            }
            for(String s : IMAGES_SALLES_PAR_DEFAUT){
                UtilEditor.copierRessource("/source/editeur/images/salles/" + s, new File(urlDossier + "/images/salles/" + s));
            }
        }
        UtilEditor.cheminImage = urlDossier + "/images/";
        ObservableListImage.rafraichirImage();
    }

}
