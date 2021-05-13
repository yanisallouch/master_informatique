# Projet Compilation - Spécifications

Les spécification du projet de compilation.

* Concevoir et implanter en Common LISP :

	* Générateur de code LISP vers ASM ;
	* Machine virtuelle (VM) qui interprète le fichier ASM produit par le générateur de code.
* Il faudra penser aux étapes suivantes :
	* compiler la fonction fibo écrite en lisp
	* charger ce code dans la VM
	* compiler un appel à la fonction fibo, par exemple (fibo 10)
	* charger ce code dans la VM
	* lancer l'exécution de ce code
* Il faut pouvoir compiler les choses suivantes
	* fonctions récursives en LISP
	* structures de contrôle LISP : let, if, loop, select etc. donc en particulier gérer les paramètres et les variables locales et leur portée
	* fonctions locales (labels)
	* fermetures
