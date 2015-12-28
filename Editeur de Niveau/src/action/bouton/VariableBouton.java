package action.bouton;

import action.table.Script;
import action.table.VariableTable;
import composants.variable.Variable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

/**
 * Classe VariableBouton
 */
public class VariableBouton implements EventHandler<ActionEvent> {


    private TextArea nom,valeur;

    /**
     * Constructeur de VariableBouton
     * @param nom Nom
     * @param valeur Valeur
     */
    public VariableBouton(TextArea nom,TextArea valeur){
        this.nom = nom;
        this.valeur = valeur;
    }

    @Override
    public void handle(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur dans la création d'une variable");
        alert.setHeaderText("Une erreur est survenue lors de la création de la variable");

        //Récupération Nom
        String texteNom = nom.getText();
        int tailleNom = texteNom.length();
        if(tailleNom > 16 || tailleNom<2){
            alert.setContentText("Le nom doit au moins contenir 2 lettres et maximum 16 lettres.\nActuellement, votre nom comporte " + tailleNom + " lettre(s), cette valeur est incorrecte.");
            alert.show();
            return;
        }

        //Récupération Valeur
        String texteValeur = valeur.getText();
        int tailleValeur = texteValeur.length();
        if(tailleValeur > 16 || tailleValeur<1){
            alert.setContentText("La valeur doit au moins contenir 1 lettre et maximum 16 lettres.\nActuellement, votre valeur comporte " + tailleValeur + " lettre(s), cette valeur est incorrecte.");
            alert.show();
            return;
        }

        Variable v = new Variable(texteNom,texteValeur);

        //Vérification variable n'est pas déjà présente
        if (VariableTable.variable.contains(v)){
            alert.setContentText("Le nom de la variable est déjà utilisé par une autre variable.");
            alert.show();
            return;
        }

        VariableTable.addItem(v);
        this.clear();
    }

    /**
     * Remet à zéro la fenetre
     */
    private void clear(){
        nom.clear();
        valeur.clear();
    }

}
