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

    public static void main(String[]args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setUserAgentStylesheet(STYLESHEET_MODENA);
        chargerFichier();
        new PrincipalFrame();
    }

    /**
     * Charge les fichiers d'images par défaut si inexistant
     */
    private void chargerFichier(){
        String urlCourante = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
        String urlDossier = "";
        boolean jar = false;
        String pathTab[] = urlCourante.split("/");

        //On regarde si on execute l'application depuis un jar ou depuis les sources
        //pour définir le chemin du dossier d'image a créer
        if(pathTab[pathTab.length-1].equalsIgnoreCase("4LEditor.jar")){
            for(int i =0;i<pathTab.length-1;i++){
                urlDossier += pathTab[i] + "/";
            }
            jar = true;
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

            //Si l'on provient d'un jar on va extraire les images
            if(jar){
                UtilEditor.copieCollerFromJar(urlCourante,urlDossier);
            }else{ //sinon on les copies simplement
                for(String s : UtilEditor.getCheminImage(UtilEditor.ImageType.OBJET)){
                    UtilEditor.copierColler(new File("src/source/editeur/images/objets/" + s),new File(urlDossier + "/images/objets/" + s));
                }
                for(String s : UtilEditor.getCheminImage(UtilEditor.ImageType.SALLE)){
                    UtilEditor.copierColler(new File("src/source/editeur/images/salles/" + s),new File(urlDossier + "/images/salles/" + s));
                }
            }
        }
        UtilEditor.cheminImage = urlDossier + "/images/";
        ObservableListImage.rafraichirImage();
    }

}
