package source.editeur.modele;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import source.moteur.Direction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipFile;

import static org.junit.jupiter.api.Assertions.*;

class FormatJeuTest {

    /** Petit jeu complet utilisé par les tests */
    static ModeleJeu exemple() {
        ModeleJeu m = new ModeleJeu();
        m.setNom("Le manoir");
        Variable porte = new Variable("porte", "fermée");
        m.getVariables().add(porte);
        Objet cle = new Objet("clé", "ouvre les serrures : toutes", true, "obj_cle.png");
        Objet feuille = new Objet("feuille", "ligne 1\nligne 2 \\ fin", false, "obj_feuille.png");
        m.getObjets().addAll(cle, feuille);
        Salle bureau = new Salle("Bureau", "Un vieux bureau", "sal_bureau.jpg");
        Salle chambre = new Salle("Chambre", "", "sal_chambre.jpg");
        m.getSalles().addAll(bureau, chambre);
        m.setSalleDepart(chambre);
        m.getLiens().addAll(new Lien(bureau, Direction.NORD, chambre), new Lien(chambre, Direction.SUD, bureau));
        m.getAssociations().add(new Association(bureau, cle, 20, 75, false));
        Regle r = new Regle();
        r.setVerbe("ouvrir");
        r.setComplement("porte");
        r.setPreposition("avec");
        r.setComplementSecondaire("clé");
        r.setSalle(bureau);
        r.setConditionVariable(porte);
        r.setConditionValeur("fermée");
        r.setMessage("La porte s'ouvre.");
        r.setVariableModifiee(porte);
        r.setNouvelleValeur("ouverte");
        r.setActionObjet(ActionObjet.RETIRER);
        r.setObjetCible(cle);
        r.setDeplacement(chambre);
        m.getRegles().add(r);
        return m;
    }

    @Test
    void allerRetour() throws FormatJeuException {
        ModeleJeu m = exemple();
        String script = FormatJeu.ecrire(m);
        ModeleJeu lu = FormatJeu.lire(script);

        assertEquals(script, FormatJeu.ecrire(lu), "réécrire le jeu lu doit donner le même script");
        assertEquals("Le manoir", lu.getNom());
        assertEquals("Chambre", lu.getSalleDepart().getNom());
        assertEquals("ligne 1\nligne 2 \\ fin", lu.getObjets().get(1).getDescription());
        assertEquals("ouvre les serrures : toutes", lu.getObjets().get(0).getDescription());

        Association a = lu.getAssociations().get(0);
        assertSame(lu.getSalles().get(0), a.getSalle(), "les références pointent vers les éléments lus");
        assertSame(lu.getObjets().get(0), a.getObjet());
        assertEquals(20, a.getX());
        assertFalse(a.isVisible());

        Regle r = lu.getRegles().get(0);
        assertEquals("ouvrir porte avec clé", r.getCommande());
        assertSame(lu.getVariables().get(0), r.getConditionVariable());
        assertEquals(ActionObjet.RETIRER, r.getActionObjet());
        assertSame(lu.getSalles().get(1), r.getDeplacement());
        assertFalse(lu.isModifie());
    }

    @Test
    void scriptEcritAlaMainTolerant() throws FormatJeuException {
        String script = """
                # commentaire
                [SALLE]
                nom   =   Cuisine
                [salle]
                nom = Cave
                [lien]
                depart = cuisine
                direction = Est
                arrivee = CAVE
                [objet]
                nom = pain
                prenable = oui
                """;
        ModeleJeu m = FormatJeu.lire(script);
        assertEquals(Direction.EST, m.getLiens().get(0).getDirection());
        assertSame(m.getSalles().get(1), m.getLiens().get(0).getArrivee());
        assertTrue(m.getObjets().get(0).isPrenable());
        assertEquals("Cuisine", m.getSalleDepart().getNom(), "sans [jeu], la première salle est celle de départ");
    }

    private static void erreur(String script, int ligne, String extrait) {
        FormatJeuException e = assertThrows(FormatJeuException.class, () -> FormatJeu.lire(script));
        assertEquals(ligne, e.getLigne(), e.getMessage());
        assertTrue(e.getMessage().contains(extrait), e.getMessage());
    }

    @Test
    void erreursAvecNumeroDeLigne() {
        erreur("[salle]\nnom = A\n[lien]\ndepart = A\ndirection = NORD\narrivee = B\n", 6, "\"B\" n'existe pas");
        erreur("[salle]\nnom = A\n[salle]\nnom = a\n", 4, "existe déjà");
        erreur("[piece]\n", 1, "section inconnue");
        erreur("[objet]\nnom = x\ncouleur = rouge\n", 3, "clé inconnue");
        erreur("nom = x\n", 1, "dans une section");
        erreur("[objet]\ndescription = x\n", 1, "\"nom\" est obligatoire");
        erreur("[salle]\nnom = A\n[association]\nsalle = A\nobjet = rien\n", 5, "\"rien\" n'existe pas");
        erreur("[salle]\nnom = A\n[lien]\ndepart = A\ndirection = HAUT\narrivee = A\n", 5, "direction inconnue");
        erreur("[objet]\nnom = x\nprenable = peut-être\n", 3, "oui/non");
    }

    @Test
    void suppressionEnCascade() {
        ModeleJeu m = exemple();
        Salle bureau = m.getSalles().get(0);
        assertEquals(3, m.dependances(bureau).size(), m.dependances(bureau).toString());

        m.supprimer(bureau);
        assertTrue(m.getLiens().isEmpty(), "les liens vers le bureau sont supprimés");
        assertTrue(m.getAssociations().isEmpty());
        assertNull(m.getRegles().get(0).getSalle(), "la règle perd sa condition de salle");
        assertEquals(1, m.getSalles().size());

        Objet cle = m.getObjets().get(0);
        m.supprimer(cle);
        assertEquals(ActionObjet.AUCUNE, m.getRegles().get(0).getActionObjet());
        assertNull(m.getRegles().get(0).getObjetCible());
    }

    @Test
    void suiviDesModifications() {
        ModeleJeu m = exemple();
        m.setModifie(false);
        long version = m.versionProperty().get();
        m.getSalles().get(0).setNom("Grand bureau");
        assertTrue(m.isModifie(), "modifier une propriété d'un élément marque le jeu modifié");
        assertTrue(m.versionProperty().get() > version);
        assertTrue(FormatJeu.ecrire(m).contains("depart = Grand bureau"), "les références suivent le renommage");
    }

    @Test
    void fichierAventureAvecImages(@TempDir Path dossier) throws IOException, FormatJeuException {
        Path images = dossier.resolve("images");
        Files.createDirectories(images.resolve("objets"));
        Files.createDirectories(images.resolve("salles"));
        Files.write(images.resolve("objets/obj_cle.png"), new byte[]{1, 2, 3});
        Files.write(images.resolve("salles/sal_bureau.jpg"), new byte[]{4, 5});

        ModeleJeu m = exemple();
        assertEquals(java.util.List.of("objets/obj_feuille.png", "salles/sal_chambre.jpg"), FichierJeu.imagesManquantes(m, images), "feuille et chambre n'ont pas d'image sur le disque");

        Path fichier = dossier.resolve("manoir.aventure");
        FichierJeu.enregistrer(m, fichier, images);
        try (ZipFile zip = new ZipFile(fichier.toFile())) {
            assertNotNull(zip.getEntry("jeu.txt"));
            assertNotNull(zip.getEntry("images/objets/obj_cle.png"));
            assertNotNull(zip.getEntry("images/salles/sal_bureau.jpg"));
        }

        //Ouverture sur un autre poste : une image différente porte déjà le même nom
        Path autresImages = dossier.resolve("autre");
        Files.createDirectories(autresImages.resolve("objets"));
        Files.write(autresImages.resolve("objets/obj_cle.png"), new byte[]{9, 9});

        ModeleJeu lu = FichierJeu.ouvrir(fichier, autresImages);
        assertEquals("obj_cle_2.png", lu.getObjets().get(0).getImage(), "l'image du jeu est renommée");
        assertArrayEquals(new byte[]{9, 9}, Files.readAllBytes(autresImages.resolve("objets/obj_cle.png")), "l'image existante est conservée");
        assertArrayEquals(new byte[]{1, 2, 3}, Files.readAllBytes(autresImages.resolve("objets/obj_cle_2.png")));
        assertArrayEquals(new byte[]{4, 5}, Files.readAllBytes(autresImages.resolve("salles/sal_bureau.jpg")));

        //Rouvrir le même fichier réutilise les images déjà importées
        ModeleJeu relu = FichierJeu.ouvrir(fichier, autresImages);
        assertEquals("obj_cle_2.png", relu.getObjets().get(0).getImage());
        assertFalse(Files.exists(autresImages.resolve("objets/obj_cle_3.png")));
    }

    @Test
    void ouvrirScriptTexte(@TempDir Path dossier) throws IOException, FormatJeuException {
        Path fichier = dossier.resolve("jeu.txt");
        Files.writeString(fichier, FormatJeu.ecrire(exemple()));
        assertEquals(2, FichierJeu.ouvrir(fichier, dossier).getSalles().size());
    }

    @Test
    void scriptInvalideNeTouchePasAuxImages(@TempDir Path dossier) throws IOException {
        Path fichier = dossier.resolve("jeu.txt");
        Files.writeString(fichier, "[inconnu]\n");
        assertThrows(FormatJeuException.class, () -> FichierJeu.ouvrir(fichier, dossier.resolve("images")));
        assertFalse(Files.exists(dossier.resolve("images")));
    }
}
