Pour terminer l'application, il revient au processus Contrôleur de détruire la file de messages. Toutefois, pour terminer proprement, cette destruction doit se produire après que le processus Collecteur ait lu tous les messages qui lui sont destinés (sinon, on perd des données collectées). Pour s'en assurer, ... (cocher une réponse si elle est correcte) 

Le processus Collecteur n'a pas d'autre choix que d'envoyer un message au processus Contrôleur pour lui signaler la fin de la collecte et du traitement des données.

Aucune réponse n'est correcte : il faut penser une autre solution.

le processus Contrôleur peut vérifier qu'il n'y a plus de message dans la file sans devoir faire appel à msgrcv(...). 
