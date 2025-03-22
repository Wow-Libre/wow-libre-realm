# World of Warcraft Community Wow Libre

<br>

## ¡Bienvenido a WowLibre!

![Imagen de WhatsApp 2025-02-18 a las 14 05 02_722e5195](https://github.com/user-attachments/assets/2830afc3-a607-45cc-914e-f5e1ef1bd593)

¿Qué es WowLibre?
WowLibre es una comunidad dedicada al mundo de World of Warcraft, con el objetivo de ofrecer experiencias de juego innovadoras que mejoren la interacción entre jugadores y administradores. Nuestro propósito es contribuir al crecimiento de la comunidad a través del desarrollo de herramientas útiles que faciliten tanto la gestión de servidores como la experiencia de juego en sí.

Aunque no somos expertos en emuladores, buscamos aportar desde nuestra experiencia y conocimiento en el desarrollo de aplicaciones web y móviles. Queremos crear soluciones que permitan a los administradores de servidores gestionar sus proyectos de manera más sencilla, y a la vez, mejorar la experiencia de juego para toda la comunidad.

Es por esto que hemos creado esta aplicación web con Spring Boot, diseñada para transformar la manera en que los jugadores interactúan con World of Warcraft. ¡Nuestro proyecto es de código abierto y está en constante evolución, siempre enfocado en ofrecer nuevas herramientas que potencien tu experiencia en el juego!
<br>
<br>
<br>

# ¡Requisitos!

- Debes tener instalado JDK de java 17 o superior

  - Guía: [Instalación de Java](https://www.youtube.com/watch?v=TRsCMJrKglw)
  - Descargar : [Java 17](https://www.oracle.com/co/java/technologies/downloads/#java17)

  ```sh
   java --version
  ```

  - ![image](https://github.com/user-attachments/assets/c0dd8669-ca59-4929-a45b-3ee879f9682f)

## Instalación de Maven en Windows

1. Descarga Maven desde el sitio oficial:

   - [Apache Maven](https://maven.apache.org/download.cgi)  
     ![image](https://github.com/user-attachments/assets/46306253-45da-40ad-80bb-556f6c004362)

2. Extrae el archivo en una ubicación de tu elección (ejemplo: `C:\Program Files\Apache\Maven`)
   ![image](https://github.com/user-attachments/assets/31019dcc-def1-4ded-a5ea-13c6f38052c3)

3. Configura las variables de entorno:

   - Añade Maven a la variable `Path`  
     ![image](https://github.com/user-attachments/assets/861cb9c4-4806-4fbd-9774-a9f74236d9c9)

4. Verifica la instalación ejecutando en un CMD o powershell:
   ```sh
   mvn -version
   ```
   ![image](https://github.com/user-attachments/assets/ec779e69-cf71-4226-883c-021550269937)

## Mysql

Es necesario contar con MySQL instalado y configurado correctamente.

🔹 Descargar MySQL:

- Sitio oficial: MySQL [Community Downloads](https://dev.mysql.com/downloads/workbench/)
- Guía de instalación: [Cómo instalar MySQL](https://www.youtube.com/watch?v=EmQZt6o6-78)

🔹 Verificar instalación:

Después de instalar MySQL, asegúrate de que el servicio esté en ejecución con el siguiente comando:

```sh
  mysql --version
```

El emulador AzerothCore utiliza las siguientes bases de datos en MySQL, y la aplicación web está diseñada para integrarse con este emulador:

- acore_auth → Gestiona las cuentas de usuario y autenticación.
- acore_characters → Almacena la información de los personajes.
- acore_world → Contiene todos los datos del mundo, como NPCs, objetos y misiones.

Es fundamental que estas bases de datos estén correctamente configuradas para garantizar el correcto funcionamiento tanto del emulador como de la aplicación web. 🚀

![image](https://github.com/user-attachments/assets/17c4ec24-0e68-406c-ac2b-abc8bd3521a9)

## Ejecución de Scripts

Para ejecutar los scripts necesarios para el proyecto.

```sql
ALTER TABLE acore_auth.account
   ADD COLUMN user_id bigint;
```

```sql
CREATE TABLE acore_auth.client
(
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
```

```sql
ALTER TABLE acore_characters.guild
    ADD COLUMN public_access boolean,
    ADD COLUMN discord       text,
    ADD COLUMN multi_faction boolean;
```

```sql
CREATE TABLE acore_characters.character_transaction
(
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
```

```sql
CREATE TABLE acore_auth.server_publications
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    img         text NOT NULL,
    title       VARCHAR(80)  NOT NULL,
    description TEXT         NOT NULL
);
```

## Variables de entorno

Para el correcto funcionamiento del sistema, es necesario configurar las siguientes variables de entorno:

- **Opcionales:** Las siguientes variables de entorno están marcadas como opcionales, lo que significa que deben ser creadas y definidas, pero su valor puede ser cualquier atributo.
  Estas variables permiten la integración con la central de WoW Libre. Si no deseas o no estás interesado en conectarte con WoW Libre, puedes asignarles cualquier valor sin afectar el funcionamiento interno del sistema. ✅

  Encuentra mas informacion de las variables de integracion en www.wowlibre.com/integrations

  - Ejemplo
    - ![image](https://github.com/user-attachments/assets/a14a1af0-4b60-4d35-8ddd-617716edc31f)

- **Requeridas:** Las siguientes variables de entorno son obligatorias para la correcta configuración y funcionamiento del sistema web. Sin ellas, el sistema no podrá iniciarse correctamente. Asegúrate de definirlas con los valores adecuados antes de ejecutar la aplicación.

### 1. Configuración de la Base de Datos (Requerido)❤️

Estas variables permiten la conexión con la base de datos.

- **DB_WOW_LIBRE_USERNAME** → Nombre de usuario para acceder a la base de datos.
- **DB_WOW_LIBRE_PASSWORD** → Contraseña asociada al usuario de la base de datos.

### 2. Credenciales de Administración (Opcional)

Credenciales utilizadas para acceder a la interfaz de administración del sistema.

Asignar una cuenta de Game Master (GM) a la aplicación web permitirá el envío de ítems dentro del sistema. Esto es necesario, ya que la cuenta debe tener los permisos adecuados para ejecutar el comando:

```code
.send items
```

- **GM_USERNAME** → Nombre de usuario del administrador.
- **GM_PASSWORD** → Contraseña del administrador.

### 3. Autenticación y API del Sistema (Opcional)

Claves necesarias para la integración con la API del sistema central WowLibre

- API_KEY_WOW_LIBRE → Clave de acceso a la API del sistema.
- **USERNAME_WOW_LIBRE** → Usuario de www.wowlibre.com para la autenticación en la API.
- **PASSWORD_WOW_LIBRE** → Contraseña de www.wowlibre.com para la autenticación en la API.

### 4. Seguridad y Servicios Externos (Requerido)

Variables utilizadas para la seguridad y servicios de terceros.

- **SECRET_JWT** → Clave secreta para la generación y validación de tokens JWT. (Ejemplo A3F1E4B2D0A728C9F54D8B32C7A59A7D0B9A8F94D1F6C762E7DA56231988C158) Genera tu propio SECRET
- **GOOGLE_API_SECRET** → Clave secreta para integraciones con los servicios de Google.
- **GOOGLE_API_KEY** → Clave de API para el uso de servicios de Google.

### 5. Configuración del Servidor Web (Requerido)❤️

Define el nombre del servidor web en el entorno de ejecución.

- **SERVER_WEB_NAME** → Nombre personalizado del servidor web.
- 📌 Nota: Asegúrate de configurar estas variables en el entorno adecuado para garantizar el correcto funcionamiento del sistema. 🚀

## Cómo Iniciar la Aplicación

### Compilar aplicacion

- Descargar repositorio

  ```sh
  git clone https://github.com/ManuChitiva/wow-libre-client.git
  ```

- Compilar proyecto - Maven

  Ingresar a la carpeta donde clono el repositorio:

  Ejemplo C:\Users\usuario\Documents\worskpace\wow-libre-client Ejecutar un powershell
  ![image](https://github.com/user-attachments/assets/6a75b140-13fc-4ee4-8036-24d3fe60ca28)

  Ejecutar en el powershell

  ```sh
  mvn install
  ```

  ![image](https://github.com/user-attachments/assets/3ec5ec88-1fab-4d75-b0dd-c5019f070cad)
  ![image](https://github.com/user-attachments/assets/209a2eb6-6ebd-483c-9325-3cc274108943)

  Al compilar el proyecto se debio generar una carpeta llamada "target"

  ![image](https://github.com/user-attachments/assets/f90eddef-8c12-4e88-9164-42eed97ebbbb)

  Dentro de "target" debes tener un .jar como muestra la imagen, esta es nuestra App compilada.

  ![image](https://github.com/user-attachments/assets/421e62d7-385c-4015-b7ee-55d080668ec0)

  En el mismo CMD o Powershell vamos a ejecutar.

  ```sh
  java -jar wow-libre-client-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
  ```

  ![image](https://github.com/user-attachments/assets/4aba2c1b-5b69-4efe-9592-5df606e8a088)

  Si la ejecusion fue un exito deberias poder ver que la aplicacion web se esta ejecutando en http://localhost:8090/

  ![image](https://github.com/user-attachments/assets/e0a4760a-aaad-4a81-9bf3-a1a68f281e8b)

## 🌟 APP EJECUTANDOSE CON EXITO 🌟

http://localhost:8090/

![image](https://github.com/user-attachments/assets/5c2f3089-cbca-4e40-9f81-4ff25d2fe9c5)
![image](https://github.com/user-attachments/assets/a24a8d39-623e-4483-a1be-8a40a8bafafc)

## 🌟 Únete a Nuestra Comunidad 🌟

![fgasdasd](https://github.com/user-attachments/assets/6a4dd599-86ec-4e16-ace0-a9b9f7bdd510)

¡Sumérgete en el universo de World of Warcraft como nunca antes! Síguenos en nuestras redes sociales y sé parte de una comunidad apasionada, donde compartimos la emoción, las aventuras y los secretos del juego que nos une.

Conéctate con otros jugadores, recibe las últimas novedades, y disfruta de contenido exclusivo que te llevará al siguiente nivel. ¡No te pierdas nada!

📲 Síguenos y forma parte de algo épico:

[![Facebook](https://img.shields.io/badge/Facebook-1877F2?style=for-the-badge&logo=facebook&logoColor=white)](https://www.facebook.com/WowLibre/)
[![YouTube](https://img.shields.io/badge/YouTube-FF0000?style=for-the-badge&logo=youtube&logoColor=white)](https://www.youtube.com/@WowLibre)
[![Discord](https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=discord&logoColor=white)](https://discord.gg/gPgNaXF87p)
[![WhatsApp](https://img.shields.io/badge/WhatsApp-25D366?style=for-the-badge&logo=whatsapp&logoColor=white)](https://chat.whatsapp.com/BDELJKhuJkWIMKxF8ExIdN)
[![TikTok](https://img.shields.io/badge/TikTok-000000?style=for-the-badge&logo=tiktok&logoColor=white)](https://www.tiktok.com/@wowlibre?_t=8ootaqKLQKj&_r=1)

