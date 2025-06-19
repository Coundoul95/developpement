#!/bin/bash

# Activer le mode strict
set -e

# Répertoire racine des services
BASE_DIR="/home/ehac6/ehac/stage/aflilins/gestion-enquete"

# Démarrage du serveur de découverte
echo "🚀 Démarrage du serveur de découverte..."
cd "$BASE_DIR/serveurdecouverte"
mvn spring-boot:run > "$BASE_DIR/back-v1/inf/logs/decouverte.log" 2>&1 &
echo $! > "$BASE_DIR/back-v1/inf/pids/decouverte.pid"
echo "✅ Serveur de découverte lancé (PID: $(cat $BASE_DIR/back-v1/inf/pids/decouverte.pid))"

# Démarrage du serveur distributeur
echo "🚀 Démarrage du serveur distributeur..."
cd "$BASE_DIR/serveurdistributeur"
mvn spring-boot:run > "$BASE_DIR/back-v1/inf/logs/distributeur.log" 2>&1 &
echo $! > "$BASE_DIR/back-v1/inf/pids/distributeur.pid"
echo "✅ Serveur distributeur lancé (PID: $(cat $BASE_DIR/back-v1/inf/pids/distributeur.pid))"

echo "📂 Les logs sont disponibles dans $BASE_DIR/back-v1/inf/logs/"

echo