# ============================================================
# STAGE 1 — Build
# Usiamo un'immagine con Maven e JDK 17 solo per compilare il progetto.
# Il risultato (il file .jar) viene poi copiato nello stage finale,
# così l'immagine di produzione non si porta dietro Maven e i sorgenti.
# ============================================================
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copiamo prima solo il pom.xml: se le dipendenze non cambiano,
# Docker riusa la cache di questo passo nelle build successive
# (più veloce ad ogni nuovo deploy).
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Ora copiamo il codice sorgente e compiliamo
COPY src ./src
RUN mvn clean package -DskipTests -B

# ============================================================
# STAGE 2 — Runtime
# Immagine leggera con solo la JRE (non l'intero JDK), perché
# in produzione serve solo eseguire il .jar, non compilare nulla.
# ============================================================
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/calcio-amatoriale-0.0.1-SNAPSHOT.jar app.jar

# Render imposta automaticamente la variabile PORT e si aspetta che
# l'app risponda su quella porta (vedi server.port in application.properties)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]