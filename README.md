
# Plateforme de Services Dynamiques BRi

[![Architecture](https://img.shields.io/badge/Architecture-Client--Serveur-blue?style=for-the-badge)](#)
[![Module](https://img.shields.io/badge/R5A08-Qualit%C3%A9_de_d%C3%A9veloppement-green?style=for-the-badge)](#)

Plateforme d'échanges et de chargement dynamique de services Java à chaud (inspirée du standard OSGi), permettant d'enregistrer et d'exécuter des services distants sans jamais redémarrer le serveur central.

---

## Présentation du projet

La plateforme **BRi** accueille deux types d'acteurs sur deux ports distincts :

* **Les Programmeurs** : conçoivent des services Java, les déploient sur leur propre serveur distant (FTP/HTTP) et les enregistrent à chaud sur BRi via un port dédié sans interruption de la JVM.
* **Les Amateurs** : se connectent sur un port distinct, consultent la liste des services disponibles et les exécutent en temps réel via des flux réseau (sockets).

---

## Architecture du projet

```text
BRi/
├── src/
│   ├── brette/                  # Exemple de services fournis par le développeur 'brette'
│   │   ├── ServiceInversion.java
│   │   └── ServiceNombreMots.java
│   ├── bri/                     # Cœur de la plateforme serveur
│   │   ├── BRiException.java    # Exceptions personnalisées de la plateforme
│   │   ├── BRiLaunch.java       # Point d'entrée principal du serveur (main)
│   │   ├── BRiService.java      # Interface normalisée de service
│   │   ├── ChargeurService.java # Chargement dynamique (URLClassLoader & Réflexion)
│   │   ├── Echange.java         # Utilitaires de communication sur flux socket
│   │   ├── Programmeur.java     # Modèle représentant un programmeur
│   │   ├── ProgrammeurManager.java # Gestion et persistance des identifiants
│   │   ├── ServeurBRi.java      # Multi-sockets d'écoute (amateur et programmeur)
│   │   ├── ServiceRegistry.java # Registre central thread-safe des services
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
📐 Règles et Norme BRi
Pour qu'un service soit validé et accepté par la plateforme :

Interface : la classe doit implémenter l'interface bri.BRiService.

Package dédié : la classe doit impérativement se situer dans un package portant le nom du login du programmeur (ex: le développeur brette doit coder dans le package brette.*).

Constructeur : la classe doit disposer d'un constructeur prenant en argument la socket du client (Socket).

Déploiement à chaud : la classe est chargée dynamiquement via son URL distante (URLClassLoader) sans redémarrer le serveur.

Guide de démarrage pas à pas
Pour exécuter une démonstration complète, ouvrez 4 terminaux distincts.

Note Windows (PowerShell) : pour corriger l'affichage des accents dans la console, exécutez d'abord :

PowerShell
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
Étape 0 : Compilation
Placez-vous à la racine du projet (Qualite_dev_BRI) :

Sous Windows (PowerShell) :

PowerShell
javac -d bin -sourcepath src (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName })
Sous Linux / macOS :

Bash
mkdir -p bin
javac -d bin -sourcepath src $(find src -name "*.java")
🖥️ Terminal 1 : Hébergement local du service (FTP / HTTP)
Simulez le serveur distant du programmeur brette hébergeant sa classe compilée :

Sous Windows (PowerShell) :

PowerShell
New-Item -ItemType Directory -Force -Path C:\temp_bri\brette
Copy-Item bin\brette\ServiceInversion.class C:\temp_bri\brette\
cd C:\temp_bri
python -m http.server 2121
Sous Linux / macOS :

Bash
mkdir -p /tmp/temp_bri/brette
cp bin/brette/ServiceInversion.class /tmp/temp_bri/brette/
cd /tmp/temp_bri
python3 -m http.server 2121
🖥️ Terminal 2 : Démarrage du Serveur BRi
Depuis la racine du projet :

Bash
java -cp bin bri.BRiLaunch
Le serveur écoute simultanément sur les deux ports (port amateur et port programmeur).

🖥️ Terminal 3 : Client Programmeur (Déploiement du service)
Depuis la racine du projet :

Bash
java -cp bin clientprog.ClientProg
Connexion / Enregistrement :

Login : brette

Mot de passe : secret (ou tout mot de passe de votre choix)

URL du serveur : http://localhost:2121/ (ou ftp://localhost:2121/)

Ajout du service :

Choisir l'option 1 - Fournir un nouveau service.

Indiquer le nom de la classe : ServiceInversion.

🖥️ Terminal 4 : Client Amateur (Utilisation du service)
Depuis la racine du projet :

Bash
java -cp bin clientama.ClientAma
Observez la liste des services disponibles (vous devez voir brette.ServiceInversion).

Saisissez le numéro correspondant au service.

Entrez un texte (ex: bonjour) et observez le résultat inversé renvoyé par le service (ruojnob).


Projet universitaire réalisé dans le cadre de la ressource R5A08 - Qualité de développement :

Ayoub

Osman

Sienna

(Groupe 302)
