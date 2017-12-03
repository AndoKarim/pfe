# Guide d'utilisation
*********************

L'application AuthApp permet de paramétrer et tester 3 mécanismes d'authentification sur smartphone. Le guide d'utilisation permet d'utiliser ces mécanismes ainsi que leurs log:

+ PinCode
+ Android Unlock Scheme
+ PassFace



*C'est-à-dire ?*
Arrivé sur la page d'accueil de l'application 3 boutons sont présents pour commencer une nouvelle expérimentation pour chaque mécanisme d'authentification. En cliquant sur l'un d'entre eux on est dirigé sur la page de paramétrage du mécanisme choisi. En appuyant sur le bouton sauvegarder on lance l'expérimentation. L'utilisateur doit alors determiner son pincode/scheme/passface personnel puis peut procéder à l'éxpérimentation (ie. répétition de son authentification)

## PinCode
### Paramètre
Les paramètres pouvant être modifier pour la saisie du code PIN:

+ **Random Pad** : si ce paramètre est activé les chiffres seront placé de manière aléatoire sur le clavier à chaque tentative. Sinon le clavier est un clavier numérique classique. Par défaut désactivé.

+ **Length of Pin Code** : correspond au nombre de chiffres constituant le code PIN. Par défaut 4.

+  **Number of try** :  correspond au nombre d'échecs acceptés lors de la saisie de code PIN avant que le téléphone se bloque théoriquement. Par défaut 3.
*Ce paramètre est présent dans les autres mécanismes.*

+  **Capture Mode**: si ce paramètre est un fichier de log sera enregistré dans le stockage interne du téléphone. Par défaut activé.
*Ce paramètre est présent dans les autres mécanismes.*
+  **Indicator Type**: précise à l'utilisateur la progression dans la saisie code PIN. Soit il n'y a aucune indication (*no indicator*), soit on présente à l'utilisateur un nombre de point égal à la longueur de PIN et ceux-ci se remplissent à chaque chiffre saisi (*Dots to fill*), soit les points apparaissent au fur et à mesure de la saisie de chiffre (*Appearing dots*).


### Log & utilisation

Si le paramètre *Capture Mode* est activé. des fichiers de Log seront enregistré dans le stockage interne du téléphone dans le dossier **AuthApp_Logs**. Les fichiers correspondant aux logs du PinCode sont nommés de la manière suivante : **pincode_date_de_l_experimentation.csv**.

La première ligne du fichier précise les paramètres de l'expérience:

+  shuffle .ie. si le clavier numérique est généré de manière aléatoire
+   la taille du code
+  le nombre d'essais
+ le type d'indicateur (0 = No indicator, 1 = filling dots, 2 = Appearing dots)

Ensuite chaque ligne du tableau correspond à une tentative de l'utilisateur:

+ **Result**: spécifie le résultat de la tentative. *Success* correspond à un code PIN correct. *Fail* correspond à code PIN erroné. *Fatal error* correspond un code PIN erroné ainsi que le nombre d'essai autorisé atteint.  
+  **toucheN**: correspond à la position du chiffre dans le code PIN
+  **value**: à la valeur du chiffre choisie dans *toucheN*
+ **time**:  temps mis entre le chiffre choisi et le premier chiffre choisi

Lorsque l'on change d'utilisateur son *nom* apparaît en début de ligne (*userN*). Si un utilisateur quitte un expérimentation en cours les champs seront rempli par des cases vides.

## Unlock Graphic Scheme
### Paramètre
### Log & Utilisation

## PassFace


### Paramètre

Les paramètres qui peuvent être variés sont :
+ **Number of picture shown** : Le nombre de photos affichées dans la grille. La valeur peut être entre 1 et 12 (inclus). Par défaut 9.
+ **Type of pictures** : La catégorie de photos affichés à l'utilisateurs. Cela peut être des visages, comme des animaux. Par défaut "Faces".
+ **Password length per steps** : Le nombre de photos que l'on peut utiliser par étape. Par défaut 1.
+ **Number of steps** : Le nombre d'étapes pour la constitution du mot de passe. La valeur peut être entre 1 et 5 (inclus). Par défaut 5.
+ **Type of matching** : Quelle correspondance entre le mot de passe choisi et sélectionné. La valeur peut être Exact ou Semantic. **Exact** veut dire que l'on ne peux choisir que l'image choisie. **Semantic** veut dire que si le mot de passe est par exemple composé d'une image de Tigre, une autre photo de Tige pourra être choisie pendant l'étape correspondante. Par défaut Exact.
+ **Shufle images** : Est ce que la position des images est mélangée lors de chaque étape et chaque utilisation? Par défaut Yes.
+ **Use an image twice** : Est ce que l'on peut utiliser une image deux fois si **Password length** est superieur à 1. Par défaut No.

### Log & Utilisation

> "Make the world a safer place" - Abdelkarim Andolerzak
