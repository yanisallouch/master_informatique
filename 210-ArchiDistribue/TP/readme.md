# Import & Configuration

* import [exportEclipseRenduTP.zip](./exportEclipseRenduTP.zip) dans Eclipse.
* Changer les variables path de classe String. Voir un exemple dans le fichier Client.Java dans le dossier ClientRMI-CabinetVeterinaire.
* Vérifier le build path selon l'énoncé du projet.

# Arborescence

* Le projet **1** contient le code java RMI client/serveur et implémentation pour répondre aux questions 1 à 3.

### Ce qui suit réponds a la question 4 et sert de base a la question 5

* Le projet **ClassRMI-CabinetVeterinaire** contient les interfaces a ajoutés au build path du projet client et du server.
* Le projet **ClientRMI-CabinetVeterinaire** possède l'implémentation de l'interface Connexion. Il faut faire attendion a host/port d'écoute du registre RMI.
* Le projet **ServerRMI-CabinetVeterinaire** possède le reste des implémentations à fournir sous la proprièté "java.rmi.server.codebase" pour le client.