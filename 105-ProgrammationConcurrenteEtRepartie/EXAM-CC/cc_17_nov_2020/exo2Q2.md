Le processus Capteur  ...... (cocher une réponse si elle permet d'aboutir à un fonctionnement  correct de de toute l'application)

peut utiliser la même étiquette pour l'ensemble des messages à envoyer au processus Contrôleur. Cette étiquette sera la même pour tous les capteurs, mais doit être différente de celle utilisée pour les messages envoyés au processus Collecteur.

peut utiliser la même étiquette pour l'ensemble des messages à envoyer au processus Contrôleur. Cette étiquette sera différente pour chaque capteur (donc N étiquettes différentes)  et doit être différente de celle utilisée pour les messages envoyés au processus Collecteur.
passera à msgrcv(...)  une valeur  > 0  en étiquette afin de recevoir un message en provenance du processus Contrôleur. Cette étiquette sera la même pour tout les processus Capteur.

passera à msgrcv(...)  une valeur  0  en étiquette afin de recevoir n'importe quel message en provenance du processus Contrôleur. 

peut utiliser la même étiquette pour l'ensemble des messages à envoyer au processus Contrôleur. Cette étiquette sera la même pour tous les capteurs et pour tous les messages envoyés aussi au processus Collecteur.

passera à msgrcv(...)  une valeur  > 0  en étiquette afin de recevoir un message en provenance du processus Contrôleur. Cette étiquette sera différente pour chaque processus Capteur.
