# Guide d'utilisation
*********************

L'application AuthApp permet de paramétrer et tester 3 mécanismes d'authentification sur smartphone. Le guide d'utilisation permet d'utiliser ces mécanismes ainsi que leurs log:

+ PinCode (from [https://github.com/aritraroy/PinLockView](https://github.com/aritraroy/PinLockView))
+ LockPattern (from [https://github.com/aritraroy/PatternLockView](https://github.com/aritraroy/PatternLockView))
+ PassFace



*C'est-à-dire ?*

Arrivé sur la page d'accueil de l'application 3 boutons sont présents pour commencer une nouvelle expérimentation pour chaque mécanisme d'authentification. En cliquant sur l'un d'entre eux on est dirigé sur la page de paramétrage du mécanisme choisi. En appuyant sur le bouton sauvegarder on lance l'expérimentation. L'utilisateur doit alors determiner son pincode/scheme/passface personnel puis peut procéder à l'éxpérimentation (ie. répétition de son authentification)
## Paramètres

### PinCode
Les paramètres pouvant être modifier pour la saisie du code PIN:

+ **Random Pad** : si ce paramètre est activé les chiffres seront placé de manière aléatoire sur le clavier à chaque tentative. Sinon le clavier est un clavier numérique classique. Par défaut désactivé.

+ **Length of Pin Code** : correspond au nombre de chiffres constituant le code PIN. Par défaut 4.

+  **Number of try** :  correspond au nombre d'échecs acceptés lors de la saisie de code PIN avant que le téléphone se bloque théoriquement. Par défaut 3.
*Ce paramètre est présent dans les autres mécanismes.*

+  **Capture Mode**: si ce paramètre est activé un fichier de log sera enregistré dans le stockage interne du téléphone. Par défaut activé.
*Ce paramètre est présent dans les autres mécanismes.*
+  **Indicator Type**: pécise à l'utilisateur la progression dans la saise du code PIN:
 1. *no indicator*: aucune indication
 2. *Dots to fill*:  soit on présente à l'utilisateur un nombre de point égal à la longueur de PIN et ceux-ci se remplissent à chaque chiffre saisi
 3. *Appearing dots*: soit les points apparaissent au fur et à mesure de la saisie de chiffre
 4. *With Num*: affiche le dernier chiffre du code PIN saisi ainsi que le nombre de chiffre déjà saisie.


### PassFace
Les paramètres qui peuvent être variés sont :
+ **Number of picture shown** : Le nombre de photos affichées dans la grille. La valeur peut être entre 1 et 12 (inclus). Par défaut 9.
+ **Type of pictures** : La catégorie de photos affichés à l'utilisateurs. Cela peut être des visages, comme des animaux. Par défaut "Faces".
+ **Password length per steps** : Le nombre de photos que l'on peut utiliser par étape. Par défaut 1.
+ **Number of steps** : Le nombre d'étapes pour la constitution du mot de passe. La valeur peut être entre 1 et 5 (inclus). Par défaut 5.
+ **Type of matching** : Quelle correspondance entre le mot de passe choisi et sélectionné. La valeur peut être Exact ou Semantic. **Exact** veut dire que l'on ne peux choisir que l'image choisie. **Semantic** veut dire que si le mot de passe est par exemple composé d'une image de Tigre, une autre photo de Tige pourra être choisie pendant l'étape correspondante. Par défaut Exact.
+ **Shufle images** : Est ce que la position des images est mélangée lors de chaque étape et chaque utilisation? Par défaut Yes.
+ **Use an image twice** : Est ce que l'on peut utiliser une image deux fois si **Password length** est superieur à 1. Par défaut No.

### LockPattern
Les paramètres pouvant être modifier pour la saisie du code Pattern:

+ **Number of rows** : nombre de lignes pour le dessin du Pattern. Par défaut 3.

+ **Number of columns** : nombre de colonnes pour le dessin du Pattern. Par défaut 3.

+  **Number of try** :  correspond au nombre d'échecs acceptés lors de la saisie du Pattern avant que le téléphone se bloque théoriquement. Par défaut 3.
*Ce paramètre est présent dans les autres mécanismes.*

+  **Capture Mode**: si ce paramètre est activé, un fichier de log sera enregistré dans le stockage interne du téléphone. Par défaut activé.
*Ce paramètre est présent dans les autres mécanismes.*

+  **Vibration**: si ce paramètre est activé alors les vibrations lors de la saisie du Pattern seront activées.

+  **Stealth**: si ce paramètre est activé alors le tracé du schema ne sera pas visible.


## Log & utilisation

Si le paramètre *Capture Mode* est activé. des fichiers de Log seront enregistré dans le stockage interne du téléphone dans le dossier **AuthApp_Logs**. Les fichiers correspondant aux logs du PinCode sont nommés de la manière suivante : **pincode_date_de_l_experimentation.csv**.

### PinCode

La première ligne du fichier précise les paramètres de l'expérience:

+  shuffle .ie. si le clavier numérique est généré de manière aléatoire
+   la taille du code
+  le nombre d'essais
+ le type d'indicateur (0 = No indicator, 1 = filling dots, 2 = Appearing dots, 3 = With Num)

Ensuite chaque ligne du tableau correspond à une tentative de l'utilisateur:

+ **Result**: spécifie le résultat de la tentative. *Success* correspond à un code PIN correct. *Fail* correspond à code PIN erroné. *Fatal fail* correspond un code PIN erroné ainsi que le nombre d'essai autorisé atteint.  
+  **toucheN**: correspond à la position du chiffre dans le code PIN
+  **value**: à la valeur du chiffre choisie dans *toucheN*
+ **time**:  temps mis entre le chiffre choisi et le premier chiffre choisi

Lorsque l'on change d'utilisateur son *nom* apparaît en début de ligne (*userN*). Si un utilisateur quitte un expérimentation en cours les champs seront rempli par des cases vides.

### PassFace

La première ligne du fichier affiche les paramètres choisis pour l'expérience:
+ Number Of Photos
+ Type of Photos
+ Length per steps
+ Number of Steps
+ Type of Matching
+ Shuffle
+ Use twice an image

Les lignes suivantes suivent l'organisation suivante;
+ Une ligne avec les évènements surveillés
+ **Date** La date à laquelle l'évènement a été observé (en secondes)
+ **Username** Le nom d'utilisateur qui fait l'expérience **User**+**Nombre qui incrémente**
+ **Event** Le type d'évènement qui a été observé. Les valeurs peuvent être : "Start of the experience", "Photo Clicked", "Save button clicked", "New password saved", "Submit button clicked", "Good password entered", "Wrong password entered", "Change button clicked"
+ **Position** La position de la photo qui a été cliquée. La valeur est vide si l'on clique sur un bouton.  La valeur est vide si l'on clique sur un bouton ou si on observe autre chose qu'un click. 
+ **Value** Le nom de l'image cliquée. La valeur est vide si l'on clique sur un bouton ou si on observe autre chose qu'un click.

+ Les valeurs observées, une ligne correspond à un évènement.  

### LockPattern
La première ligne du fichier contient les différents paramètres de l'expérience:

+  nombre de lignes
+  nombre de colonnes
+  le nombre d'essais
+  vibration
+  stealth ie. si schema est affiché

Ensuite chaque ligne du tableau correspond à une tentative de l'utilisateur.




> "Make the world a safer place" - Abdelkarim Andolerzak
