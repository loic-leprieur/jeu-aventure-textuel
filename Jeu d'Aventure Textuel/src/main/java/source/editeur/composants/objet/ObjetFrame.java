package source.editeur.composants.objet;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import source.editeur.composants.FormulaireFrame;
import source.editeur.composants.SelecteurImage;
import source.editeur.modele.ModeleJeu;
import source.editeur.modele.Objet;
import source.editeur.util.Constante;
import source.util.UtilEditor;

/**
 * Classe ObjetFrame
 * Fenêtre permettant d'ajouter ou de modifier un objet
 */
public class ObjetFrame extends FormulaireFrame<Objet> {

    private final TextField nom = new TextField();
    private final TextArea description = new TextArea();
    private final CheckBox prenable = new CheckBox("Le joueur peut prendre cet objet");
    private final SelecteurImage image = new SelecteurImage(UtilEditor.ImageType.OBJET, 120);

    public ObjetFrame() {
        super("un objet");
        nom.setPromptText("ex : clé");
        description.setPromptText("ex : Une petite clé rouillée");
        description.setPrefRowCount(3);
        description.setWrapText(true);
        ligne("Nom", nom);
        ligne("Description", description);
        ligne("", prenable);
        ligne("Image", image);
    }

    @Override
    protected void vider() {
        nom.clear();
        description.clear();
        prenable.setSelected(false);
        image.setValeur(null);
        nom.requestFocus();
    }

    @Override
    protected void remplir(Objet o) {
        nom.setText(o.getNom());
        description.setText(o.getDescription());
        prenable.setSelected(o.isPrenable());
        image.setValeur(o.getImage());
    }

    @Override
    protected String verifier(Objet existant) {
        return Constante.premiere(
                Constante.verifierNom("Le nom de l'objet", nom.getText()),
                ModeleJeu.nomUtilise(modele.getObjets(), nom.getText(), existant)
                        ? "Un autre objet s'appelle déjà « " + nom.getText().trim() + " »." : null,
                Constante.verifierLongueur("La description", description.getText(), Constante.TAILLE_DESCRIPTION_MAX),
                image.getValeur().isEmpty() ? "Choisissez une image pour l'objet, ou importez-en une." : null);
    }

    @Override
    protected void enregistrer(Objet existant) {
        Objet o = existant == null ? new Objet() : existant;
        o.setNom(nom.getText());
        o.setDescription(description.getText());
        o.setPrenable(prenable.isSelected());
        o.setImage(image.getValeur());
        if (existant == null) {
            modele.getObjets().add(o);
        }
    }
}
