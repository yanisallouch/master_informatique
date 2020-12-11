Dans le cadre d’une application multi-processus, on souhaite collecter et traiter des données mesurées par  N capteurs. L’architecture de cette application est composée des processus suivants :

- Un processus Contrôleur qui ,en boucle, permet sur demande (saisie clavier) une des actions suivantes 

    demander aux N capteurs de commencer des mesures.
    suspendre des mesures en cours d'un ou de plusieurs capteurs.
    reprendre une ou des mesures suspendues.
    arrêter/stopper les mesures des N capteurs et terminer proprement l'application.

- N processus Capteur. Un processus Capteur exécute les actions demandées par le processus Contrôleur. Pendant les mesures,  un capteur envoie aussi les données mesurées à un processus Collecteur.

- Un processus Collecteur qui collecte les données envoyées par les capteurs et les traite.

Pour le bon déroulement de l'application, un capteur doit notifier (accuser la réception et l'exécution) le contrôleur des actions suivantes : lancement, suspension, reprise et arrêt des mesures. Ainsi le contrôleur pourra terminer proprement l'application une fois que toutes les données mesurées ont été réceptionnées par le collecteur.

Pour la mise en oeuvre de cette application, on choisit d'utiliser une seule file de messages pour tous les échanges entre les processus Contrôleur, Capteur et Collecteur.

Avant de répondre aux questions suivantes, réfléchir à un protocole d'échange (sans détailler le contenu des messages) entre les différents processus.



