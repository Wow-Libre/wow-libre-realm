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

| Tecnología        | Uso                                      |
|-------------------|------------------------------------------|
| **Java 17**       | Lenguaje                                 |
| **Spring Boot 3.5** | Web, Security, JPA, Validación         |
| **Spring WS**     | Cliente SOAP (AzerothCore / TrinityCore) |
| **MySQL**         | Persistencia (auth, characters, world)   |
| **JWT (JJWT)**    | Autenticación API                        |
| **SpringDoc OpenAPI** | Documentación Swagger/OpenAPI      |
| **Lombok**        | Reducción de boilerplate                 |
| **Jacoco**        | Cobertura de tests                       |

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

En **producción** (`--spring.profiles.active=prod`) se usan estas variables (nombres según `application.yml`):

#### Base de datos (requerido)

| Variable | Descripción |
|----------|-------------|
| `DB_WOW_CLIENT_HOST_AUTH` | URL JDBC de `acore_auth` |
| `DB_WOW_CLIENT_HOST_CHARACTERS` | URL JDBC de `acore_characters` (reino 1) |
| `DB_WOW_CLIENT_HOST_WORLD` | URL JDBC de `acore_world` (reino 1) |
| `DB_WOW_CLIENT_USERNAME` | Usuario MySQL |
| `DB_WOW_CLIENT_PASSWORD` | Contraseña MySQL |

Para un **segundo reino** (opcional):  
`DB_WOW_CLIENT_HOST_CHARACTERS_REALM_2`, `DB_WOW_CLIENT_HOST_WORLD_REALM_2`,  
`DB_WOW_CLIENT_USERNAME_REALM_2`, `DB_WOW_CLIENT_PASSWORD_REALM_2`.

#### Servidor y red

| Variable | Descripción |
|----------|-------------|
| `WOW_CLIENT_SERVER_PORT` | Puerto HTTP de la app (ej. 8090) |
| `HOST_BASE_CORE` | URL base del core/central WoW Libre (integraciones) |

#### SOAP (emulador)

| Variable | Descripción |
|----------|-------------|
| `SOAP_CLIENT_DEFAULT_URI` | URI del servicio SOAP (ej. `http://172.17.0.1:7878`) |
| `WOW_CLIENT_SOAP_URI` | URI SOAP reino 1 |
| `WOW_CLIENT_SOAP_GM_USERNAME` | Usuario GM para SOAP reino 1 |
| `WOW_CLIENT_SOAP_GM_PASSWORD` | Contraseña GM reino 1 |

Reino 2 (si aplica): `WOW_CLIENT_SOAP_URI_REALM_2`, `WOW_CLIENT_SOAP_GM_USERNAME_REALM_2`, `WOW_CLIENT_SOAP_GM_PASSWORD_REALM_2`.

#### Seguridad

| Variable | Descripción |
|----------|-------------|
| `WOW_CLIENT_SECRET_JWT` | Clave secreta para firmar/validar JWT (recomendado 256 bits en hex) |

#### Integración con central WoW Libre (opcional)

Si no usas la central, puedes dejar valores dummy. Para integración real:

- `API_KEY_WOW_LIBRE`, `USERNAME_WOW_LIBRE`, `PASSWORD_WOW_LIBRE`  
Más información: [www.wowlibre.com/integrations](https://www.wowlibre.com/integrations)

#### Google (opcional)

- `GOOGLE_API_SECRET`, `GOOGLE_API_KEY` — para funcionalidades que usen APIs de Google.

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

El proyecto incluye `docker-compose.yml`. Configura las variables que usa el servicio `app` (por ejemplo en un `.env`) y levanta:

```bash
docker compose up -d
```

La aplicación se expone en el puerto definido (ej. 8090). Nginx puede actuar como reverso proxy (puertos 80/443).

---

## API principal

Los controladores exponen una API REST bajo prefijos como `/api/...`. Resumen por recurso:

| Recurso | Ruta base | Uso breve |
|---------|-----------|-----------|
| Cuentas | `/api/account` | Crear cuenta, cambiar contraseña, listar, obtener por ID |
| Personajes | `/api/characters` | Listar, detalle, inventario, transferencia, teleport, stats |
| Transacciones | `/api/transaction` | Compra, beneficios suscripción, promociones, gremio, máquina, deducir tokens |
| Gremios | `/api/guilds` | Listar, detalle, adjuntar, miembros, editar |
| Social | `/api/social` | Amigos, enviar dinero/nivel |
| Profesiones | `/api/professions` | Listar, anuncios |
| Premium | `/api/premium` | Estado y gestión por cuenta |
| Dashboard | `/api/dashboard` | Stats, email, ban, configuración emulador, rutas |
| Reinos | `/api/realmlist` | Lista de reinos |
| Cliente | `/api/client` | Alta/baja de clientes (admin) |
| Comandos | `/commands` | Ejecución de comandos (SOAP) |
| Banco / correo | `/api/bank`, `/api/mails` | Pagos, correo de personaje |

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
