Le processus Contrôleur  ...... (cocher une réponse si elle permet d'aboutir à un fonctionnement  correct de de toute l'application)

peut déposer dans la file un seul message de suspension de mesures. Ce message sera lu par tous les processus Capteur ciblés. Il en est de même pour la reprise des mesures.

passera à msgrcv(...)  une valeur > 0 en étiquette afin de recevoir une notification (de lancement, d'arrêt, de suspension ou de reprise de mesures) en provenance d'un capteur. L'étiquette sera la même pour toutes les notifications, peut importe s'il s'agit d'un lancement, d'un arrêt, ....

passera à msgrcv(...)  une valeur > 0  en étiquette afin de recevoir une notification (de lancement, d'arrêt, de suspension ou de reprise de mesures) en provenance d'un capteur. L'étiquette sera différente pour chaque type de notification (exemple : 4 pour toutes les notifications d'arrêt, 5 pour toutes les notifications de suspension...).

peut déposer dans la file un seul message de lancement de mesures. Ce message sera lu par tous les processus Capteur. Il en est de même pour l'arrêt des mesures.


doit déposer dans la file N messages de lancement de mesures. Il en fera de même pour l'arrêt des mesures.

doit déposer dans la file un message de suspension de mesures par Capteur ciblé. Ce message sera lu par tous les processus Capteur ciblés. Il en est de même pour la reprise des mesures.

passera à msgrcv(...) la valeur 0 en étiquette afin de recevoir une notification (de lancement, d'arrêt, de suspension ou de reprise de mesures) en provenance d'un capteur, 

passera à msgrcv(...)  une valeur > 0  en étiquette afin de recevoir une notification (de lancement, d'arrêt, de suspension ou de reprise de mesures) en provenance d'un capteur. L'étiquette sera différente pour chaque capteur peut importe le type de notification (autant d'étiquettes que de capteurs). 
