package source.editeur.composants.salle;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import source.editeur.composants.FormulaireFrame;
import source.editeur.composants.SelecteurImage;
import source.editeur.modele.ModeleJeu;
import source.editeur.modele.Salle;
import source.editeur.util.Constante;
import source.util.UtilEditor;

/**
 * Classe SalleFrame
 * Fenêtre permettant d'ajouter ou de modifier une salle
 */
public class SalleFrame extends FormulaireFrame<Salle> {

    private final TextField nom = new TextField();
    private final TextArea description = new TextArea();
    private final SelecteurImage image = new SelecteurImage(UtilEditor.ImageType.SALLE, 200);

    public SalleFrame() {
        super("une salle");
        nom.setPromptText("ex : Bureau");
        description.setPromptText("Texte affiché quand le joueur entre dans la salle");
        description.setPrefRowCount(3);
        description.setWrapText(true);
        ligne("Nom", nom);
        ligne("Description", description);
        ligne("Image de fond", image);
    }

    @Override
    protected void vider() {
        nom.clear();
        description.clear();
        image.setValeur(null);
        nom.requestFocus();
    }

    @Override
    protected void remplir(Salle s) {
        nom.setText(s.getNom());
        description.setText(s.getDescription());
        image.setValeur(s.getImage());
    }

    @Override
    protected String verifier(Salle existant) {
        return Constante.premiere(
                Constante.verifierNom("Le nom de la salle", nom.getText()),
                ModeleJeu.nomUtilise(modele.getSalles(), nom.getText(), existant)
                        ? "Une autre salle s'appelle déjà « " + nom.getText().trim() + " »." : null,
                Constante.verifierLongueur("La description", description.getText(), Constante.TAILLE_DESCRIPTION_MAX),
                image.getValeur().isEmpty() ? "Choisissez une image de fond pour la salle, ou importez-en une." : null);
    }

    @Override
    protected void enregistrer(Salle existant) {
        Salle s = existant == null ? new Salle() : existant;
        s.setNom(nom.getText());
        s.setDescription(description.getText());
        s.setImage(image.getValeur());
        if (existant == null) {
            modele.getSalles().add(s);
        }
    }
}
