S22 : Jeu d'aventure textuelle

Tuteur : Vincent THOMAS

Membres du groupe :
* Lucas GÉRARD
* Loïc LEPRIEUR
* Laura TRIVINO
* Louis ZWAWIAK

## Ouvrir le projet dans Eclipse

Le code se trouve dans le dossier `Jeu d'Aventure Textuel/` (projet Maven, Java 25, JavaFX 26).

1. *File > Import... > General > Existing Projects into Workspace*, puis choisir le dossier `Jeu d'Aventure Textuel`
   (projet `JeuAventureTextuel`). Ne pas importer le dossier racine du dépôt.
2. Si besoin : clic droit sur le projet > *Maven > Update Project...* (Alt+F5).
3. Lancer depuis *Run > Run Configurations... > Maven Build* :
   * **Lancer Editeur** : éditeur de niveau (`mvn javafx:run`)
   * **Lancer Jeu** : moteur de jeu (`mvn javafx:run -Pjeu`)
   * **Tests** : tests unitaires (`mvn test`) ; on peut aussi faire clic droit sur `src/test/java` > *Run As > JUnit Test*.

Pour lancer depuis Eclipse sans Maven : clic droit sur `source.editeur.LanceurEditeur` ou `source.moteur.LanceurJeu`
puis *Run As > Java Application*.

Remarque : lancer directement `Principal` ou `Jeu` avec *Run As > Java Application* échoue
(« la méthode principale doit renvoyer une valeur de type void… »), car ces classes héritent de
`javafx.application.Application` et JavaFX est chargé depuis le classpath.


## Éditeur de niveau (4LEditeur)

L'éditeur permet de créer un jeu complet :

* **Salles** (onglet Salles) : nom, description et image de fond. La salle de départ est marquée d'une ★
  (clic droit > *Définir comme salle de départ*).
* **Liens** : passages entre salles (nord, sud, est, ouest), avec création automatique du passage retour.
* **Objets** : nom, description, image, et si le joueur peut le prendre.
* **Placements** : objets posés dans une salle. On peut les déplacer à la souris sur la *Carte actuelle*.
* **Variables** : état du jeu (ex : `porte_ouverte = non`).
* **Règles** : ce qui se passe quand le joueur tape une commande (ex : *ouvrir porte avec clé*),
  avec des conditions facultatives (salle, valeur d'une variable) et un résultat
  (message, modification d'une variable, objet donné/retiré/affiché/caché, déplacement du joueur).

Dans chaque liste : *Ajouter*, *Modifier* (ou double-clic) et *Supprimer* (ou touche Suppr).
Supprimer un élément supprime aussi les liens et placements qui l'utilisent.

**Images** : dans le formulaire d'un objet ou d'une salle, le bouton *Importer…* copie une image de
votre ordinateur dans le dossier `4LEditor/images/` de l'éditeur (*Options > Ouvrir le dossier des images*).

**Enregistrement** : *Fichier > Enregistrer* (Ctrl+S) crée un fichier `.aventure`. C'est une archive zip
qui contient le script du jeu (`jeu.txt`, visible dans l'onglet *Script*) et les images utilisées :
un jeu peut ainsi être ouvert sur un autre ordinateur.

Extrait d'un script :

```
[salle]
nom = Bureau
image = sal_bureau.jpg

[regle]
verbe = ouvrir
complement = porte
preposition = avec
complementSecondaire = clé
condition.variable = porte_ouverte
condition.valeur = non
message = La porte s'ouvre en grinçant.
effet.variable = porte_ouverte
effet.valeur = oui
```


## Moteur de jeu

Le moteur (`source.moteur.Jeu`, ou `LanceurJeu` depuis Eclipse) lance un petit jeu de démonstration.
*Fichier > Ouvrir un jeu…* (Ctrl+O) charge un fichier `.aventure` créé avec l'éditeur ; on peut aussi
passer le fichier en argument du programme. Dans l'éditeur, *Jeu > Tester le jeu* (F9) lance le jeu
en cours d'édition ; *Recommencer la partie* (Ctrl+R) y prend en compte les dernières modifications.

Le joueur écrit ses commandes en langage naturel (« prends la clé puis va au nord »). Pour chaque commande :

1. la première **règle** du jeu qui correspond (commande et conditions) s'applique ; les règles
   avec le plus de conditions sont essayées en premier. Si la commande nomme un objet, la règle ne
   s'applique que si le joueur l'a sur lui ou le voit dans la salle ;
2. sinon, l'**action prédéfinie** du verbe s'exécute : *aller* (ou une direction seule : « nord »),
   *prendre*, *poser*, *regarder*, *inventaire*, *aide* ;
3. sinon, « Rien ne se passe. »

Le vocabulaire compris réunit les noms des objets et des salles, les mots des règles et un vocabulaire
commun (synonymes et verbes irréguliers : `source.moteur.VocabulaireParDefaut`). Une règle peut donc
redéfinir une action prédéfinie, par exemple « aller nord » tant que la porte est fermée.
