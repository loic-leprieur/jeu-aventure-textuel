package action.bouton;

import action.table.Script;
import com.sun.java.swing.plaf.windows.WindowsInternalFrameTitlePane;
import composants.objet.Objet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import action.table.ObjetTable;
import util.Constante;

/**
 * Classe bouton ObjetBouton
 */
public class ObjetBouton implements EventHandler<ActionEvent> {

    private TextArea nom,description;
    private CheckBox prenable;
    private ComboBox<String> image;

    /**
     * COnstructeur de ObjetBouton
     * @param n Nom
     * @param d Description
     * @param p Prenable
     * @param i Image
     */
    public ObjetBouton(TextArea n, TextArea d, CheckBox p, ComboBox<String> i){
        this.nom = n;
        this.description = d;
        this.prenable = p;
        this.image = i;
    }

    @Override
    public void handle(ActionEvent e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur dans la création d'un objet");
        alert.setHeaderText("Une erreur est survenue lors de la création de l'objet");

        //Recupération du nom
        String texteNom = nom.getText();
        int tailleNom = texteNom.length();
        if(tailleNom > Constante.TAILLE_NOM_OBJET_MAX || tailleNom < Constante.TAILLE_NOM_OBJET_MIN){
            alert.setContentText("Le nom doit au moins contenir " + Constante.TAILLE_NOM_OBJET_MIN + " lettres et maximum " + Constante.TAILLE_NOM_OBJET_MAX + " lettres.\nActuellement, votre nom comporte " + tailleNom + " lettre(s), cette valeur est incorrecte.");
            alert.show();
            return;
        }

        //Récupération de la descritpion
        String texteDescription = description.getText();
        int tailleDescritpion = texteDescription.length();
        if(tailleDescritpion > Constante.TAILLE_DESCRIPTION_OBJET_MAX || tailleDescritpion < Constante.TAILLE_DESCRIPTION_OBJET_MIN){
            alert.setContentText("La description doit au moins contenir " + Constante.TAILLE_DESCRIPTION_OBJET_MIN + " lettres et maximum " + Constante.TAILLE_DESCRIPTION_OBJET_MAX + " lettres.\nActuellement, votre description comporte " + tailleNom + " lettre(s), cette valeur est incorrecte.");
            alert.show();
            return;
        }

        //Récupération de la checkbox Prenable
        String textePrenable;
        if(prenable.isSelected()){
            textePrenable = "true";
        }else{
            textePrenable = "false";
        }

        //Récupération du chemin de l'image
        String texteImage = image.getValue();
        if(image==null || texteImage.equals("")){
            alert.setContentText("Un objet doit forcément être relié à une image");
            alert.show();
            return;
        }

        Objet objet = new Objet(texteNom,texteDescription,textePrenable,texteImage);

        //Vérification objet n'est pas déjà présent
        if (ObjetTable.objet.contains(objet)){
            alert.setContentText("Le nom de l'objet est déjà utilisé par un autre objet.");
            alert.show();
            return;
        }

        ObjetTable.addItem(objet);
        this.clear();
    }

    /**
     * Remet à zéro la fenêtre
     */
    private void clear(){
        nom.clear();
        description.clear();
        image.setValue(image.getItems().get(0));
        prenable.setSelected(false);
    }
}
