package source.moteur;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import source.Niveau;
import source.editeur.modele.ActionObjet;
import source.editeur.modele.Association;
import source.editeur.modele.FormatJeu;
import source.editeur.modele.ModeleJeu;
import source.editeur.modele.Objet;
import source.editeur.modele.Regle;
import source.editeur.modele.Salle;
import source.moteur.exception.JeuInvalideException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MoteurJeuTest {

    private MoteurJeu moteur;
    private Niveau niveau;

    /** Crée une partie sans charger d'images (pas besoin de l'interface graphique) */
    private static MoteurJeu partie(ModeleJeu m) throws JeuInvalideException {
        return ChargeurJeu.creer(m, nom -> null, nom -> null);
    }

    @BeforeEach
    void demo() throws Exception {
        moteur = partie(FormatJeu.lire(ChargeurJeu.scriptDemo()));
        niveau = moteur.getNiveau();
        moteur.demarrer();
    }

    /** Exécute une commande et renvoie les réponses du jeu */
    private String jouer(String commande) {
        int avant = niveau.getLog().size();
        moteur.executer(commande);
        List<String> reponses = niveau.getLog().subList(avant + 1, niveau.getLog().size());
        return String.join("\n", reponses);
    }

    private void verifier(String commande, String attendu) {
        String reponse = jouer(commande);
        assertTrue(reponse.contains(attendu), "« " + commande + " » -> " + reponse);
    }

    @Test
    void accueil() {
        assertEquals("=== Le bureau fermé ===", niveau.getLog().get(0));
        assertTrue(niveau.getLog().get(1).contains("— Bureau —"));
        assertTrue(niveau.getLog().get(1).contains("Tu vois : feuille."), "la clé cachée n'est pas listée");
        assertTrue(niveau.getLog().get(1).contains("Sorties : nord."));
    }

    @Test
    void solutionDeLaDemo() {
        verifier("nord", "La porte est fermée à clé.");
        verifier("prends la clé", "Il n'y a pas de clé ici.");
        verifier("ouvre la porte avec la clé", "Tu n'as pas de clé.");
        verifier("lis la feuille", "sous le bureau");
        verifier("regarde sous le bureau", "tu trouves une petite clé");
        verifier("regarde sous le bureau", "Il n'y a plus rien");
        verifier("prends la clé", "Tu prends « clé ».");
        verifier("inventaire", "Tu portes : clé.");
        verifier("ouvre la porte", "fermée à clé. Il te faudrait");
        verifier("ouvre la porte avec la clé", "la porte s'ouvre !");
        assertEquals("oui", niveau.getVariable("porte_ouverte"));
        verifier("ouvre la porte avec la clé", "déjà ouverte");
        verifier("va au nord", "— Chambre —");
        assertEquals("Chambre", niveau.getSalleActuel().getNom());
    }

    @Test
    void plusieursCommandesDansUnePhrase() {
        String r = jouer("fouille le bureau puis prends la clé et la feuille");
        assertTrue(r.contains("tu trouves une petite clé"), r);
        assertEquals(List.of("clé", "feuille"), niveau.getInventaire().stream().map(source.Objet::getNom).toList());
        assertTrue(niveau.getSalleActuel().getObjets().isEmpty());
    }

    @Test
    void actionsPredefinies() {
        verifier("sud", "Tu ne peux pas aller vers le sud.");
        verifier("regarde la feuille", "écriture pressée");
        verifier("regarde le nord", "Au nord : Chambre.");
        verifier("pose la feuille", "Tu n'as pas de feuille.");
        verifier("prends la feuille", "Tu prends « feuille ».");
        verifier("prends la feuille", "Tu as déjà « feuille ».");
        verifier("pose la feuille", "Tu poses « feuille ».");
        verifier("regarder", "Tu vois : feuille.");
        verifier("aide", "essaie aussi : lire, fouiller, ouvrir.");
        verifier("inventaire", "Tu ne portes rien.");
        verifier("manger la feuille", "Rien ne se passe.");
    }

    @Test
    void phrasesIncomprisesOuNegatives() {
        verifier("danse le tango", "Je ne comprends pas");
        verifier("ne prends pas la feuille", "tu ne fais rien");
        assertTrue(niveau.getInventaire().isEmpty());
        verifier("la feuille", "Que veux-tu faire avec feuille ?");
    }

    @Test
    void reglesDuJeuEnCoursDEdition() throws Exception {
        ModeleJeu m = new ModeleJeu();
        Salle cuisine = new Salle("Cuisine", "Ça sent bon.", "");
        Salle cave = new Salle("Cave", "Il fait noir.", "");
        m.getSalles().addAll(cuisine, cave);
        Objet pain = new Objet("pain", "Un pain croustillant", false, "");
        Objet lampe = new Objet("lampe", "Une lampe à huile", true, "");
        m.getObjets().addAll(pain, lampe);
        m.getAssociations().add(new Association(cuisine, pain, 50, 50, true));

        Regle manger = new Regle();
        manger.setVerbe("croquer");            //synonyme de « manger » : la règle répond aux deux
        manger.setComplement("pain");
        manger.setMessage("Miam !");
        manger.setActionObjet(ActionObjet.CACHER);
        manger.setObjetCible(pain);
        manger.setDeplacement(cave);
        Regle trappe = new Regle();
        trappe.setVerbe("soulever");           //verbe inconnu du vocabulaire commun : ajouté au dictionnaire
        trappe.setComplement("trappe");
        trappe.setMessage("Une lampe tombe de la trappe.");
        trappe.setActionObjet(ActionObjet.DONNER);
        trappe.setObjetCible(lampe);
        m.getRegles().addAll(manger, trappe);

        MoteurJeu p = partie(m);
        p.demarrer();
        Niveau n = p.getNiveau();
        p.executer("prends le pain");
        assertTrue(n.getLog().getLast().contains("Tu ne peux pas prendre « pain »."), n.getLog().getLast());
        p.executer("je mange le pain");
        assertTrue(n.getLog().getLast().startsWith("Miam !\n— Cave —"), n.getLog().getLast());
        assertFalse(cuisineObjetVisible(n), "le pain a disparu");
        p.executer("soulève la trappe");
        assertEquals("lampe", n.getInventaire().getFirst().getNom(), "objet placé nulle part : pris dans le catalogue");
    }

    private static boolean cuisineObjetVisible(Niveau n) {
        return !n.getSalle("Cuisine").getObjetsVisibles().isEmpty();
    }

    @Test
    void jeuSansSalle() {
        assertThrows(JeuInvalideException.class, () -> partie(new ModeleJeu()));
    }
}
