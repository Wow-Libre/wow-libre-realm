# Instaladores Wow Libre (Inno Setup)

Instaladores con **asistente de configuración** que pide las variables de entorno y genera el archivo `.env` y el launcher `.bat` para Windows.

## Requisitos

- **Inno Setup 6** (o superior): [Descargar](https://jrsoftware.org/isdl.php)
- **JDK 17** instalado en el equipo donde se ejecutará el instalador (el usuario final debe tener Java para ejecutar el JAR)
- Proyecto compilado: `mvn clean package`

## Wow Libre Realm

1. Compilar el cliente realm:
   ```bash
   cd /ruta/a/wow-libre-realm
   mvn clean package -DskipTests
   ```
2. Copiar el JAR al directorio del instalador:
   ```bash
   mkdir -p installer/app
   copy target\wow-libre-client-0.0.1-SNAPSHOT.jar installer\app\
   ```
   (En Windows: `copy target\wow-libre-client-0.0.1-SNAPSHOT.jar installer\app\`)
3. Abrir **Inno Setup Compiler** y cargar `installer\WowLibreRealm.iss`.
4. Menú **Build** → **Compile**. El instalador se generará en `installer\output\WowLibreRealm-Setup-0.0.1.exe`.

### Flujo del asistente (Realm)

- **Directorio de instalación**
- **Base de datos**: URL JDBC `acore_auth`, usuario y contraseña MySQL
- **Servidor y Core**: Puerto del realm (8090), URL del Core (8091), SOAP por defecto, secret JWT
- **Reino 1**: URLs JDBC characters/world, URI SOAP, usuario y contraseña GM
- **Reino 2 (opcional)**: Misma configuración para un segundo reino

Al finalizar, en la carpeta de instalación quedan: el JAR, `.env` y `IniciarRealm.bat`. El usuario ejecuta el `.bat` o el acceso directo para arrancar con `--spring.profiles.active=prod`.

## Wow Libre Core

1. Compilar el proyecto **wow-libre-core** y copiar su JAR a `installer\app-core\`.
2. En `WowLibreCore.iss` ajusta `#define JarName` al nombre real del JAR del core (ej. `wow-libre-core-0.0.1-SNAPSHOT.jar`).
3. Revisa las variables de entorno que usa el core en su `application.yml` (perfil `prod`) y, si no coinciden con las del script, edita la sección `[Code]` de `WowLibreCore.iss` para escribir los nombres correctos en el `.env`.
4. Compilar el script en Inno Setup. El instalador se generará en `installer\output-core\`.

### Variables por defecto (Core)

El script escribe por defecto: `DB_HOST`, `DB_USERNAME`, `DB_PASSWORD`, `SERVER_PORT`, `JWT_SECRET`, `REALM_BASE_URL` y opcionalmente `CORS_ORIGINS`. Si el core usa otros nombres (por ejemplo `SPRING_DATASOURCE_URL`), hay que modificar las llamadas a `EnvLine` en el `CurStepChanged` del script.

## Estructura esperada antes de compilar

```
installer/
├── WowLibreRealm.iss
├── WowLibreCore.iss
├── README.md
├── app/
│   └── wow-libre-client-0.0.1-SNAPSHOT.jar   ← copiar desde target/
├── app-core/
│   └── wow-libre-core-0.0.1-SNAPSHOT.jar    ← copiar desde el proyecto core
├── output/                                   ← generado por Inno (Realm)
└── output-core/                              ← generado por Inno (Core)
```

## Script de construcción (Windows)

Puedes usar `build-installer.bat` para compilar el realm y copiar el JAR antes de abrir Inno Setup (solo Realm; para Core hay que compilar ese proyecto aparte).
