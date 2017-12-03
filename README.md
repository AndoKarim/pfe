# Guide d'utilisation
*********************

L'application AuthApp permet de param�trer et tester 3 m�canismes d'authentification sur smartphone. Le guide d'utilisation permet d'utiliser ces m�canismes ainsi que leurs log:

+ PinCode
+ Android Unlock Scheme
+ PassFace



*C'est-�-dire ?*
Arriv� sur la page d'accueil de l'application 3 boutons sont pr�sents pour commencer une nouvelle exp�rimentation pour chaque m�canisme d'authentification. En cliquant sur l'un d'entre eux on est dirig� sur la page de param�trage du m�canisme choisi. En appuyant sur le bouton sauvegarder on lance l'exp�rimentation. L'utilisateur doit alors determiner son pincode/scheme/passface personnel puis peut proc�der � l'�xp�rimentation (ie. r�p�tition de son authentification)

##PinCode
### Param�tre
Les param�tres pouvant �tre modifier pour la saisie du code PIN:

+ **Random Pad** : si ce param�tre est activ� les chiffres seront plac� de mani�re al�atoire sur le clavier � chaque tentative. Sinon le clavier est un clavier num�rique classique. Par d�faut d�sactiv�.

+ **Length of Pin Code** : correspond au nombre de chiffres constituant le code PIN. Par d�faut 4.

+  **Number of try** :  correspond au nombre d'�checs accept�s lors de la saisie de code PIN avant que le t�l�phone se bloque th�oriquement. Par d�faut 3.
*Ce param�tre est pr�sent dans les autres m�canismes.*

+  **Capture Mode**: si ce param�tre est un fichier de log sera enregistr� dans le stockage interne du t�l�phone. Par d�faut activ�.
*Ce param�tre est pr�sent dans les autres m�canismes.*
+  **Indicator Type**: pr�cise � l'utilisateur la progression dans la saisie code PIN. Soit il n'y a aucune indication (*no indicator*), soit on pr�sente � l'utilisateur un nombre de point �gal � la longueur de PIN et ceux-ci se remplissent � chaque chiffre saisi (*Dots to fill*), soit les points apparaissent au fur et � mesure de la saisie de chiffre (*Appearing dots*).


### Log & utilisation

Si le param�tre *Capture Mode* est activ�. des fichiers de Log seront enregistr� dans le stockage interne du t�l�phone dans le dossier **AuthApp_Logs**. Les fichiers correspondant aux logs du PinCode sont nomm�s de la mani�re suivante : **pincode_date_de_l_experimentation.csv**.

La premi�re ligne du fichier pr�cise les param�tres de l'exp�rience:

+  shuffle .ie. si le clavier num�rique est g�n�r� de mani�re al�atoire
+   la taille du code
+  le nombre d'essais
+ le type d'indicateur (0 = No indicator, 1 = filling dots, 2 = Appearing dots)

Ensuite chaque ligne du tableau correspond � une tentative de l'utilisateur:

+ **Result**: sp�cifie le r�sultat de la tentative. *Success* correspond � un code PIN correct. *Fail* correspond � code PIN erron�. *Fatal error* correspond un code PIN erron� ainsi que le nombre d'essai autoris� atteint.  
+  **toucheN**: correspond � la position du chiffre dans le code PIN
+  **value**: � la valeur du chiffre choisie dans *toucheN*
+ **time**:  temps mis entre le chiffre choisi et le premier chiffre choisi

Lorsque l'on change d'utilisateur son *nom* appara�t en d�but de ligne (*userN*). Si un utilisateur quitte un exp�rimentation en cours les champs seront rempli par des cases vides.

##Unlock Graphic Scheme
###Param�tre
###Log & Utilisation

##PassFace
###Param�tre
###Log & Utilisation

> "Make the world a safer place" - Abdelkarim Andolerzak