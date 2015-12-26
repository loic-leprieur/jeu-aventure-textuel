package action.bouton;

import composants.Objet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import action.table.ObjetTable;

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
        if(tailleNom > 16 || tailleNom<2){
            alert.setContentText("Le nom doit au moins contenir 2 lettres et maximum 16 lettres.\nActuellement, votre nom comporte " + tailleNom + " lettre(s), cette valeur est incorrecte.");
            alert.show();
            return;
        }

        //Récupération de la descritpion
        String texteDescription = description.getText();
        int tailleDescritpion = texteDescription.length();
        if(tailleDescritpion>40 || tailleDescritpion<2){
            alert.setContentText("La description doit au moins contenir 2 lettres et maximum 40 lettres.\nActuellement, votre description comporte " + tailleNom + " lettre(s), cette valeur est incorrecte.");
            alert.show();
            return;
        }

        //Récupération de la checkbox Prenable
        boolean boolPrenable = prenable.isSelected();

        //Récupération du chemin de l'image
        String texteImage = image.getValue();

        Objet objet = new Objet(texteNom,texteDescription,boolPrenable,texteImage);

        //Vérification objet n'est pas déjà présent
        if (ObjetTable.itemListViewObjet.contains(objet)){
            alert.setContentText("Le nom de l'objet est déjà utilisé par un autre objet.");
            alert.show();
            return;
        }

        ObjetTable.addItem(objet);
        this.clear();
    }

    private void clear(){
        nom.clear();
        description.clear();
        image.setValue(image.getItems().get(0));
        prenable.setSelected(false);
    }
}
