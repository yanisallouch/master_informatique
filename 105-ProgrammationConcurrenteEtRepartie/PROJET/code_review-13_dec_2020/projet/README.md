# Systeme Reservation Distribue

## Intrduction

L’idée de ce projet s’inspire d’un système de réservation de ressources de calcul et/ou de stockage sur une plateforme de grille ou de cloud. Il a pour objectif de permettre à des clients de louer des puissances de calcul ou des espaces de stockage distants répondant à des besoins spécifiques (exemple : exécuter une simulation scientifique sur une architecture distribuée de pro- cesseurs et de mémoire pour stocker (le temps de la location) les données traitées et produites). Dans le système à mettre en oeuvre, un client aura la possibilité de louer des ressources, soit en mode exclusif (les ressources louées sont utilisées par un seul client pendant toute la durée de la réservation), soit en mode partagé (les ressources louées peuvent être utilisées en même temps par plusieurs clients).

## Utilisation

### Compilation

La commande `make all` suffit à compiler tous les sources et créer les éxecutables `serveur` et `client`

Voir le [Makefile]("./makefile") pour plus de détails.

### Éxecution

Éxecuter le `serveur` en lui donnant un port en paramètre ainsi que le fichier de [configuration des ressources]("./conf/ressources.json"). Puis sur un autre terminal, éxecuter un ou plusieurs processus `client` en leur donnant en paramètre l'addresse IP du serveur ainsi que le port. Voici un example :

```
$> serveur conf/ressources.json 34000
$> client 127.0.0.1 34000
```

## Note

Les réservations se font uniquement en mode exclusif. Le mode partagé n'est pas implémenté.
