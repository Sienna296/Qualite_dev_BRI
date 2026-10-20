Plateforme de Services Dynamiques BRi

Présentation du projet

La plateforme BRi permet à deux types d'utilisateurs d'interagir sans interruption du serveur :

Les Programmeurs : conçoivent des services Java, les déploient sur leur propre serveur distant (FTP/HTTP) et les enregistrent à chaud sur BRi via un port dédié sans nécessiter de redémarrage de la JVM.

Les Amateurs : se connectent sur un port distinct, consultent la liste des services disponibles et les exécutent en temps réel via des flux réseau (sockets).

Architecture du projet

BRi/
├── src/
│   ├── brette/                  # Exemple de services fournis par le développeur 'brette'
│   │   ├── ServiceInversion.java
│   │   └── ServiceNombreMots.java
│   ├── bri/                     # Cœur de la plateforme serveur
│   │   ├── BRiException.java
│   │   ├── BRiLaunch.java       # Point d'entrée principal du serveur (main)
│   │   ├── BRiService.java      # Interface normalisée de service
│   │   ├── ChargeurService.java # Chargement dynamique (URLClassLoader & Réflexion)
│   │   ├── Echange.java         # Utilitaires de communication sur flux socket
│   │   ├── Programmeur.java     # Modèle représentant un programmeur
│   │   ├── ProgrammeurManager.java # Gestion des comptes et identifiants
│   │   ├── ServeurBRi.java      # Multi-sockets d'écoute (amateur et programmeur)
│   │   ├── ServiceRegistry.java # Registre central des services disponibles
│   │   ├── SessionAmateur.java  # Thread dédié à un client amateur
│   │   └── SessionProgrammeur.java # Thread dédié aux actions d'un programmeur
│   ├── client/                  # Socle commun pour les clients
│   │   └── ClientBase.java
│   ├── clientama/               # Client terminal pour amateur
│   │   └── ClientAma.java
│   └── clientprog/              # Client terminal pour programmeur
│       └── ClientProg.java
├── bin/                         # Bytecode compilé (.class)
└── README.md


Règles et Norme BRi

Pour qu'un service soit accepté par la plateforme :

La classe doit implémenter l'interface bri.BRiService.

La classe doit impérativement se situer dans un package portant le nom du login du programmeur (ex: le programmeur brette doit coder dans le package brette).

La classe doit disposer d'un constructeur prenant en argument la socket du client (Socket).

La classe est chargée dynamiquement via son URL sans redémarrer le serveur.

Guide de démarrage et commandes d'exécution

Pour exécuter une démonstration complète, ouvrez 4 terminaux distincts.

(Optionnel sous Windows) Pour corriger l'encodage des accents dans PowerShell :

[Console]::OutputEncoding = [System.Text.Encoding]::UTF8


Étape 0 : Compilation

Depuis la racine du projet (Qualite_dev_BRI) :

Sous PowerShell (Windows) :

javac -d bin -sourcepath src (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName })


Sous Linux / macOS :

mkdir -p bin
javac -d bin -sourcepath src $(find src -name "*.java")


Terminal 1 : Hébergement local du service (FTP / HTTP)

Simulez le serveur distant du programmeur brette hébergeant sa classe compilée :

Sous Windows (PowerShell) :

New-Item -ItemType Directory -Force -Path C:\temp_bri\brette
Copy-Item bin\brette\ServiceInversion.class C:\temp_bri\brette\
cd C:\temp_bri
python -m http.server 2121


Sous Linux / macOS :

mkdir -p /tmp/temp_bri/brette
cp bin/brette/ServiceInversion.class /tmp/temp_bri/brette/
cd /tmp/temp_bri
python3 -m http.server 2121


Terminal 2 : Démarrage du Serveur BRi

Depuis la racine du projet :

java -cp bin bri.BRiLaunch


Le serveur écoute simultanément sur les deux ports (port amateur et port programmeur).

Terminal 3 : Client Programmeur (Déploiement du service)

Depuis la racine du projet :

java -cp bin clientprog.ClientProg


Connexion / Enregistrement :

Login : brette

Mot de passe : secret (ou tout mot de passe de votre choix)

URL du serveur : http://localhost:2121/ (ou ftp://localhost:2121/)

Ajout du service :

Choisir l'option 1 - Fournir un nouveau service.

Indiquer le nom de la classe : ServiceInversion.

Terminal 4 : Client Amateur (Utilisation du service)

Depuis la racine du projet :

java -cp bin clientama.ClientAma


Observez la liste des services disponibles (vous devez voir brette.ServiceInversion).

Saisissez le numéro correspondant au service.

Entrez un texte (ex: bonjour) et observez le résultat inversé renvoyé par le service (ruojnob).


Projet universitaire réalisé par Ayoub, Osman et Sienna du groupe 302.
