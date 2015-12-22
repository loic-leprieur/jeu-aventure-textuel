import javafx.application.*;

import javafx.stage.Stage;

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
        new PrincipalFrame();

    }


}
