# Wow Libre Client

Cliente de conexión a **WoW Libre**: aplicación backend que integra tu servidor de World of Warcraft (AzerothCore/TrinityCore) con la central WoW Libre para cuentas, personajes, transacciones, recompensas y gestión de reinos.

---

## Contenido

- [¿Qué es Wow Libre?](#qué-es-wow-libre)
- [Stack técnico](#stack-técnico)
- [Arquitectura del proyecto](#arquitectura-del-proyecto)
- [Requisitos](#requisitos)
- [Configuración](#configuración)
- [Base de datos y scripts](#base-de-datos-y-scripts)
- [Ejecución](#ejecución)
  - [Docker Compose — solución completa](#docker-compose--solución-completa-guía)
- [API principal](#api-principal)
- [Comunidad](#comunidad)

---

## ¿Qué es Wow Libre?

**Wow Libre** es una comunidad centrada en World of Warcraft. Este repositorio es el **cliente backend** que:

- Se conecta a las bases de datos del emulador (AzerothCore: `acore_auth`, `acore_characters`, `acore_world`).
- Habla con el core del juego vía **SOAP** (envío de comandos GM, ítems, oro, nivel, etc.).
- Gestiona **transacciones** (compras, suscripciones, promociones, máquina de recompensas).
- Expone una **API REST** para que la central WoW Libre o un frontend consuman personajes, cuentas, gremios, premium, dashboard, etc.

Es código abierto y está pensado para que administradores y desarrolladores integren su servidor con la central o lo usen como base para su propia plataforma.

---

## Stack técnico

| Tecnología            | Uso                                      |
| --------------------- | ---------------------------------------- |
| **Java 17**           | Lenguaje                                 |
| **Spring Boot 3.5**   | Web, Security, JPA, Validación           |
| **Spring WS**         | Cliente SOAP (AzerothCore / TrinityCore) |
| **MySQL**             | Persistencia (auth, characters, world)   |
| **JWT (JJWT)**        | Autenticación API                        |
| **SpringDoc OpenAPI** | Documentación Swagger/OpenAPI            |
| **Lombok**            | Reducción de boilerplate                 |
| **Jacoco**            | Cobertura de tests                       |

---

## Arquitectura del proyecto

El código sigue un estilo **hexagonal (puertos y adaptadores)**:

```
com.auth.wow.libre
├── domain/                    # Núcleo: modelos, DTOs, enums, constantes
│   ├── model/                 # Entidades de dominio, DTOs, enums
│   ├── ports/
│   │   ├── in/                # Puertos de entrada (casos de uso)
│   │   │   ├── account, characters, transaction, guild, premium, dashboard, ...
│   │   └── out/               # Puertos de salida (repositorios, clientes externos)
│   ├── strategy/              # Estrategias por emulador (LK, War Within, etc.)
│   └── context/               # Contexto de petición (reino, etc.)
├── application/               # Capa de aplicación (servicios)
│   └── services/              # CharactersService, TransactionService, AccountService, ...
├── infrastructure/            # Adaptadores e implementaciones
│   ├── controller/            # REST (Account, Characters, Transaction, Guild, etc.)
│   ├── client/                # SOAP (AzerothCore, TrinityCore), WowLibre HTTP
│   ├── conf/                  # Configuración, datasources, realm, JWT
│   ├── repositories/         # JPA (auth, characters, realmlist, premium)
│   ├── entities/              # JPA entities (auth, characters, world)
│   ├── security/              # Spring Security, filtros JWT
│   ├── filter/                # Filtros (JWT, Realm)
│   └── schedule/              # Tareas programadas (transacciones, stats)
```

- **Dominio**: sin dependencias de framework; define qué hace la aplicación.
- **Application**: orquesta casos de uso usando los puertos.
- **Infrastructure**: implementa REST, SOAP, BD y seguridad.

Se soportan **múltiples reinos** (multi-tenant por realm) con datasources y clientes SOAP por reino.

---

## Requisitos

- **JDK 17+**
- **Maven 3.6+**
- **MySQL 8** (bases del emulador: `acore_auth`, `acore_characters`, `acore_world`)
- Emulador **AzerothCore** (o compatible) con **SOAP** habilitado (puerto 7878 por defecto)

Guías rápidas:

- [Instalación de Java](https://www.youtube.com/watch?v=TRsCMJrKglw) · [Descargar Java 17](https://www.oracle.com/co/java/technologies/downloads/#java17)
- [Apache Maven](https://maven.apache.org/download.cgi)
- [MySQL / Workbench](https://dev.mysql.com/downloads/workbench/) · [Instalar MySQL](https://www.youtube.com/watch?v=EmQZt6o6-78)

Comprobación:

```bash
java --version
mvn -version
mysql --version
```

---

## Configuración

### Perfiles

- **`local`**: desarrollo local (BD y SOAP en localhost; ver `application.yml`).
- **`prod`**: producción; toda la configuración sale de variables de entorno.

### Variables de entorno

La aplicación **solo** lee las variables que aparecen en `src/main/resources/application.yml`. No usa ninguna otra variable de entorno (por ejemplo `API_KEY_*`, `GM_USERNAME`, `GOOGLE_*`, etc.); si defines más en el `.env` o en el sistema, este servicio las ignora.

En **producción** (`--spring.profiles.active=prod`) las variables listadas abajo son **obligatorias** (el perfil prod no define valores por defecto).

#### Perfil prod — Reino 1

| Variable                        | Uso en `application.yml`                     |
| ------------------------------- | -------------------------------------------- |
| `DB_WOW_CLIENT_HOST_AUTH`       | `spring.datasource.primary.url` (acore_auth) |
| `DB_WOW_CLIENT_USERNAME`        | Usuario MySQL (auth, characters, world)      |
| `DB_WOW_CLIENT_PASSWORD`        | Contraseña MySQL                             |
| `DB_WOW_CLIENT_HOST_CHARACTERS` | URL JDBC acore_characters (reino 1)          |
| `DB_WOW_CLIENT_HOST_WORLD`      | URL JDBC acore_world (reino 1)               |
| `WOW_CLIENT_SERVER_PORT`        | `server.port`                                |
| `HOST_BASE_CORE`                | `application.urls.core-base`                 |
| `WOW_CLIENT_SOAP_URI`           | SOAP reino 1 — URI                           |
| `WOW_CLIENT_SOAP_GM_USERNAME`   | SOAP reino 1 — usuario GM                    |
| `WOW_CLIENT_SOAP_GM_PASSWORD`   | SOAP reino 1 — contraseña GM                 |
| `WOW_CLIENT_SECRET_JWT`         | `application.security.jwt.secret-key`        |

#### Perfil prod — Reino 2 (opcional)

Si tienes el segundo reino configurado en el YAML, debes definir:

| Variable                                | Uso                               |
| --------------------------------------- | --------------------------------- |
| `DB_WOW_CLIENT_HOST_CHARACTERS_REALM_2` | URL JDBC acore_characters reino 2 |
| `DB_WOW_CLIENT_HOST_WORLD_REALM_2`      | URL JDBC acore_world reino 2      |
| `DB_WOW_CLIENT_USERNAME_REALM_2`        | Usuario MySQL reino 2             |
| `DB_WOW_CLIENT_PASSWORD_REALM_2`        | Contraseña MySQL reino 2          |
| `WOW_CLIENT_SOAP_URI_REALM_2`           | URI SOAP reino 2                  |
| `WOW_CLIENT_SOAP_GM_USERNAME_REALM_2`   | Usuario GM SOAP reino 2           |
| `WOW_CLIENT_SOAP_GM_PASSWORD_REALM_2`   | Contraseña GM SOAP reino 2        |

#### SOAP URI por defecto (común)

| Variable                  | Uso                                                                                                                                                                                                     |
| ------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `SOAP_CLIENT_DEFAULT_URI` | `soap.client.default-uri`. En el bloque por defecto del YAML tiene valor por defecto `http://172.17.0.1:7878`; en el perfil prod se usa esta variable sin default, así que en prod debe estar definida. |

---

## Base de datos y scripts

El emulador usa tres bases:

- **acore_auth** — cuentas y autenticación.
- **acore_characters** — personajes, inventario, transacciones de personaje, gremios.
- **acore_world** — mundo (ítems, NPCs, misiones, etc.).

Scripts necesarios para este cliente (ejecutar en el orden que tenga sentido para tu esquema):

```sql
-- acore_auth: enlace cuenta ↔ usuario de la app
ALTER TABLE acore_auth.account
   ADD COLUMN user_id bigint;

-- acore_auth: clientes de la aplicación (admin, integraciones)
CREATE TABLE acore_auth.client (
    id              bigint AUTO_INCREMENT NOT NULL,
    username        varchar(50)           NOT NULL,
    password        text                  NOT NULL,
    status          boolean               NOT NULL,
    rol             varchar(50)           NOT NULL,
    jwt             text,
    refresh_token   text,
    expiration_date date,
    PRIMARY KEY (id),
    CONSTRAINT client_username_uq UNIQUE (username)
);

-- acore_characters: gremios (campos extra)
ALTER TABLE acore_characters.guild
    ADD COLUMN public_access boolean,
    ADD COLUMN discord       text,
    ADD COLUMN multi_faction boolean;

-- acore_characters: historial de transacciones por personaje
CREATE TABLE acore_characters.character_transaction (
    id               bigint auto_increment NOT NULL,
    character_id     bigint                NOT NULL,
    account_id       bigint                NOT NULL,
    user_id          bigint                NOT NULL,
    amount           bigint                NOT NULL,
    command          text,
    successful       boolean               NOT NULL,
    transaction_id   text,
    indebtedness     boolean               NOT NULL,
    transaction_date date                  NOT NULL,
    reference        varchar(50)           NOT NULL,
    status           boolean               NOT NULL,
    transaction_type varchar(60)           NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT character_transaction_reference_uq UNIQUE (reference)
);

-- acore_auth: publicaciones del servidor (opcional)
CREATE TABLE acore_auth.server_publications (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    img         text NOT NULL,
    title       VARCHAR(80)  NOT NULL,
    description TEXT         NOT NULL
);
```

Ajusta tipos (`date`/`datetime`) y nombres de columnas si tu esquema del emulador ya tiene cambios.

---

## Ejecución

### Clonar y compilar

```bash
git clone https://github.com/ManuChitiva/wow-libre-client.git
cd wow-libre-client
mvn clean install
```

### Perfil local (desarrollo)

En `src/main/resources/application.yml` el perfil `local` ya define URLs de BD y SOAP. Solo asegura que MySQL y el emulador estén levantados y que las credenciales coincidan.

```bash
java -jar target/wow-libre-client-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
```

Por defecto la app queda en **http://localhost:8090**.

### Perfil producción

Configura las variables de entorno necesarias (ver tabla anterior) y ejecuta:

```bash
java -jar target/wow-libre-client-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### Docker

El proyecto incluye `docker-compose.yml` para levantar solo este servicio. Para que la app arranque con perfil `prod`, configura las **mismas variables de entorno** que exige el perfil prod en `application.yml` (ver tabla de variables más arriba).

### Docker Compose — solución completa (guía)

A continuación, un **docker-compose de referencia** para desplegar toda la solución WoW Libre: core, realm (este cliente), Nginx y MySQL. Úsalo como guía y adapta imágenes, puertos y `.env` a tu entorno.

**Servicios:**

| Servicio            | Imagen                            | Puerto    | Descripción                   |
| ------------------- | --------------------------------- | --------- | ----------------------------- |
| **wow-libre-core**  | wowlibre96/wow-libre-core:latest  | 8091      | API central WoW Libre         |
| **wow-libre-realm** | wowlibre96/wow-libre-realm:latest | 8090      | Este cliente (reino/emulador) |
| **nginx**           | nginx:alpine                      | 80, 443   | Reverso proxy y TLS           |
| **mysql**           | mysql:8.0                         | 3307→3306 | Base de datos                 |

Ambos servicios WoW Libre pueden usar el mismo archivo **`.env`**. Para **wow-libre-realm** (este proyecto) define **solo** las variables listadas en la sección [Variables de entorno](#variables-de-entorno) — son las únicas que lee `application.yml`. Para wow-libre-core usa las que documente ese otro proyecto.

**`docker-compose.yml` de referencia:**

```yaml
services:
  wow-libre-core:
    image: wowlibre96/wow-libre-core:latest
    ports:
      - "8091:8091"
    mem_limit: 868m
    networks:
      - app-network
    env_file:
      - ./.env

  wow-libre-realm:
    image: wowlibre96/wow-libre-realm:latest
    ports:
      - "8090:8090"
    mem_limit: 768m
    networks:
      - app-network
    extra_hosts:
      - "host.docker.internal:host-gateway"
    env_file:
      - ./.env

  nginx:
    image: nginx:alpine
    container_name: nginx
    mem_limit: 528m
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./nginx:/etc/nginx/conf.d
      - /etc/letsencrypt:/etc/letsencrypt:ro
    depends_on:
      - wow-libre-realm
    networks:
      - app-network
    restart: always

  mysql:
    image: mysql:8.0
    container_name: mysql
    restart: always
    mem_limit: 1g
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_DATABASE: platform
      MYSQL_USER: api_core
      MYSQL_PASSWORD: Apicore1996@
    ports:
      - "3307:3306"
    volumes:
      - mysql_data:/var/lib/mysql
    networks:
      - app-network

networks:
  app-network:
    driver: bridge

volumes:
  mysql_data:
```

**Uso:**

1. Crea un `.env` en la misma carpeta que el `docker-compose.yml`. Para **wow-libre-realm** incluye **únicamente** las variables de la sección [Variables de entorno](#variables-de-entorno) (las del `application.yml`). Para **wow-libre-core** las que indique su documentación.
2. Crea la carpeta `nginx` y tu configuración en `./nginx/` (por ejemplo `default.conf`). Abajo tienes un **ejemplo** que puedes copiar y adaptar.
3. Si usas certificados Let's Encrypt, monta el volumen tal como está; si no, adapta o quita ese volumen.
4. Ejecuta:

```bash
docker compose up -d
```

**Ejemplo de configuración Nginx** (`./nginx/default.conf`):

Sustituye `api.tu-dominio.com` por tu dominio y las rutas de los certificados SSL por las tuyas (o usa otro método para TLS). Este ejemplo enruta `/core/` al central y `/realm/` a este cliente (wow-libre-realm).

```nginx
server {
    listen 443 ssl;
    server_name api.tu-dominio.com;

    ssl_certificate /etc/letsencrypt/live/api.tu-dominio.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/api.tu-dominio.com/privkey.pem;
    ssl_protocols TLSv1.2 TLSv1.3;

    # Central WoW Libre (core)
    location /core/ {
        proxy_pass http://wow-libre-core:8091/core/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_set_header transaction_id $http_transaction_id;
        proxy_set_header Authorization $http_authorization;
    }

    # Cliente reino / emulador (este proyecto)
    location /realm/ {
        proxy_pass http://wow-libre-realm:8090/realm/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_set_header transaction_id $http_transaction_id;
        proxy_set_header Authorization $http_authorization;
    }

    # Health check
    location / {
        return 200 "API Gateway OK\n";
        add_header Content-Type text/plain;
    }
}

server {
    listen 80;
    server_name api.tu-dominio.com;
    return 301 https://$host$request_uri;
}
```

Si tu aplicación no usa el prefijo `/realm/`, puedes enrutar directamente por path (por ejemplo `location /api/` con `proxy_pass http://wow-libre-realm:8090/api/;`) o exponer el puerto 8090 sin Nginx en desarrollo.

**Nota:** El servicio **wow-libre-realm** es este repositorio (wow-libre-client). En el `.env` solo deben estar las variables que aparecen en `application.yml` (ver tablas de Variables de entorno). `host.docker.internal:host-gateway` permite que el contenedor acceda al host (p. ej. para conectar al SOAP del emulador).

---

## API principal

Los controladores exponen una API REST bajo prefijos como `/api/...`. Resumen por recurso:

| Recurso        | Ruta base                 | Uso breve                                                                    |
| -------------- | ------------------------- | ---------------------------------------------------------------------------- |
| Cuentas        | `/api/account`            | Crear cuenta, cambiar contraseña, listar, obtener por ID                     |
| Personajes     | `/api/characters`         | Listar, detalle, inventario, transferencia, teleport, stats                  |
| Transacciones  | `/api/transaction`        | Compra, beneficios suscripción, promociones, gremio, máquina, deducir tokens |
| Gremios        | `/api/guilds`             | Listar, detalle, adjuntar, miembros, editar                                  |
| Social         | `/api/social`             | Amigos, enviar dinero/nivel                                                  |
| Profesiones    | `/api/professions`        | Listar, anuncios                                                             |
| Premium        | `/api/premium`            | Estado y gestión por cuenta                                                  |
| Dashboard      | `/api/dashboard`          | Stats, email, ban, configuración emulador, rutas                             |
| Reinos         | `/api/realmlist`          | Lista de reinos                                                              |
| Cliente        | `/api/client`             | Alta/baja de clientes (admin)                                                |
| Comandos       | `/commands`               | Ejecución de comandos (SOAP)                                                 |
| Banco / correo | `/api/bank`, `/api/mails` | Pagos, correo de personaje                                                   |

La documentación interactiva **Swagger/OpenAPI** (SpringDoc) suele estar disponible en:

- **http://localhost:8090/swagger-ui.html** (o la ruta configurada en tu perfil).

La autenticación suele ser por **JWT** en header o cookie según la configuración de seguridad.

---

## Comunidad

Wow Libre es un proyecto comunitario. Puedes seguirnos y participar aquí:

[![Facebook](https://img.shields.io/badge/Facebook-1877F2?style=for-the-badge&logo=facebook&logoColor=white)](https://www.facebook.com/WowLibre/)
[![YouTube](https://img.shields.io/badge/YouTube-FF0000?style=for-the-badge&logo=youtube&logoColor=white)](https://www.youtube.com/@WowLibre)
[![Discord](https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=discord&logoColor=white)](https://discord.gg/gPgNaXF87p)
[![WhatsApp](https://img.shields.io/badge/WhatsApp-25D366?style=for-the-badge&logo=whatsapp&logoColor=white)](https://chat.whatsapp.com/BDELJKhuJkWIMKxF8ExIdN)
[![TikTok](https://img.shields.io/badge/TikTok-000000?style=for-the-badge&logo=tiktok&logoColor=white)](https://www.tiktok.com/@wowlibre?_t=8ootaqKLQKj&_r=1)

---

**Nota:** Las credenciales GM (`WOW_CLIENT_SOAP_GM_USERNAME` / `WOW_CLIENT_SOAP_GM_PASSWORD`) deben corresponder a una cuenta con permisos para ejecutar comandos en el emulador (por ejemplo `.send items`). Sin SOAP correctamente configurado, las funcionalidades que envían ítems, oro o nivel no funcionarán.
