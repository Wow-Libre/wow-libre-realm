; Script generado con estilo Inno Setup Script Wizard para Wow Libre Core
; Ajusta las variables y el nombre del JAR según el proyecto wow-libre-core.
; Requisito: compilar el core con mvn clean package y colocar el JAR en installer\app-core\

#define MyAppName "Wow Libre Core"
#define MyAppVersion "0.0.1"
#define MyAppPublisher "Wow Libre"
#define MyAppURL "https://github.com/ManuChitiva/wow-libre-core"
; Nombre del JAR del proyecto core (cambiar según artefacto Maven)
#define JarName "wow-libre-core-0.0.1-SNAPSHOT.jar"

[Setup]
AppId={{B2C3D4E5-F6A7-8901-BCDE-F23456789012}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}
AppUpdatesURL={#MyAppURL}
DefaultDirName={autopf}\WowLibreCore
DefaultGroupName=Wow Libre
DisableProgramGroupPage=yes
LicenseFile=
OutputDir=output-core
OutputBaseFilename=WowLibreCore-Setup-{#MyAppVersion}
SetupIconFile=
Compression=lzma
SolidCompression=yes
WizardStyle=modern
PrivilegesRequired=admin
ArchitecturesAllowed=x64compatible
ArchitecturesInstallIn64BitMode=x64compatible

[Languages]
Name: "spanish"; MessagesFile: "compiler:Languages\Spanish.isl"
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: "app-core\{#JarName}"; DestDir: "{app}"; Flags: ignoreversion

[Icons]
Name: "{group}\Iniciar Wow Libre Core"; Filename: "{app}\IniciarCore.bat"; WorkingDir: "{app}"
Name: "{group}\Configurar variables (.env)"; Filename: "notepad.exe"; Parameters: "{app}\.env"; WorkingDir: "{app}"
Name: "{group}\{cm:UninstallProgram,{#MyAppName}}"; Filename: "{uninstallexe}"
Name: "{autodesktop}\Iniciar Wow Libre Core"; Filename: "{app}\IniciarCore.bat"; WorkingDir: "{app}"; Tasks: desktopicon

[Run]
Filename: "{app}\IniciarCore.bat"; Description: "Iniciar Wow Libre Core ahora"; Flags: postinstall nowait skipifsilent

[Code]
var
  PageDB, PageServer, PageExtra: TInputQueryWizardPage;

procedure InitializeWizard;
begin
  { Página 1: Base de datos (API central) }
  PageDB := CreateInputQueryPage(wpSelectDir,
    'Base de datos', 'MySQL del Core (plataforma central)',
    'URL JDBC, usuario y contraseña de la base de datos del Core.');
  PageDB.Add('URL JDBC (ej. jdbc:mysql://localhost:3306/platform):', False);
  PageDB.Values[0] := 'jdbc:mysql://localhost:3306/platform';
  PageDB.Add('Usuario MySQL:', False);
  PageDB.Values[1] := 'root';
  PageDB.Add('Contraseña MySQL:', True);
  PageDB.Values[2] := '';

  { Página 2: Servidor }
  PageServer := CreateInputQueryPage(PageDB.ID,
    'Servidor', 'Puerto y configuración del Core',
    'Puerto de la API central y secret JWT. Ajusta según application.yml del core.');
  PageServer.Add('Puerto del Core (ej. 8091):', False);
  PageServer.Values[0] := '8091';
  PageServer.Add('Secret JWT:', True);
  PageServer.Values[1] := '';
  PageServer.Add('URL pública del Realm (ej. http://localhost:8090):', False);
  PageServer.Values[2] := 'http://localhost:8090';

  { Página 3: Variables extra (comunes en APIs) }
  PageExtra := CreateInputQueryPage(PageServer.ID,
    'Otras variables (opcional)', 'Si tu core usa más variables de entorno, las puedes añadir después en .env',
    'Algunas variables típicas. Deja en blanco si no aplican.');
  PageExtra.Add('CORS origins (ej. http://localhost:3000):', False);
  PageExtra.Values[0] := '';
  PageExtra.Add('Variable extra 1 (nombre=valor):', False);
  PageExtra.Values[1] := '';
  PageExtra.Add('Variable extra 2 (nombre=valor):', False);
  PageExtra.Values[2] := '';
end;

function EnvLine(const Key, Value: String): String;
begin
  Result := Key + '=' + Value + #13#10;
end;

procedure CurStepChanged(CurStep: TSetupStep);
var
  EnvPath, BatPath, S, JwtSecret: String;
  EnvContent: AnsiString;
begin
  if CurStep = ssPostInstall then
  begin
    EnvPath := ExpandConstant('{app}\.env');
    BatPath := ExpandConstant('{app}\IniciarCore.bat');

    if Trim(PageServer.Values[1]) = '' then
      JwtSecret := 'CambiarClaveJWTSecretCoreEnProduccion456'
    else
      JwtSecret := PageServer.Values[1];

    { Nombres de variables típicos para un Core Spring Boot; ajustar según application.yml del core }
    S := '';
    S := S + EnvLine('DB_HOST', PageDB.Values[0]);
    S := S + EnvLine('DB_USERNAME', PageDB.Values[1]);
    S := S + EnvLine('DB_PASSWORD', PageDB.Values[2]);
    S := S + EnvLine('SERVER_PORT', PageServer.Values[0]);
    S := S + EnvLine('JWT_SECRET', JwtSecret);
    S := S + EnvLine('REALM_BASE_URL', PageServer.Values[2]);
    if Trim(PageExtra.Values[0]) <> '' then
      S := S + EnvLine('CORS_ORIGINS', PageExtra.Values[0]);
    if Trim(PageExtra.Values[1]) <> '' then
      S := S + AnsiString(PageExtra.Values[1]) + #13#10;
    if Trim(PageExtra.Values[2]) <> '' then
      S := S + AnsiString(PageExtra.Values[2]) + #13#10;

    EnvContent := AnsiString(S);
    SaveStringToFile(EnvPath, EnvContent, False);

    S := '@echo off' + #13#10;
    S := 'cd /d "%~dp0"' + #13#10;
    S := 'title Wow Libre Core' + #13#10;
    S := 'echo.' + #13#10;
    S := 'echo ========================================' + #13#10;
    S := 'echo   Wow Libre Core' + #13#10;
    S := 'echo ========================================' + #13#10;
    S := 'echo.' + #13#10;
    S := 'if not exist "{#JarName}" (' + #13#10;
    S := '  echo ERROR: No se encuentra {#JarName}' + #13#10;
    S := '  goto :fin)' + #13#10;
    S := 'if exist .env for /f "usebackq eol=# tokens=* delims=" %%a in (".env") do set "%%a"' + #13#10;
    S := 'echo Iniciando aplicacion...' + #13#10;
    S := 'echo.' + #13#10;
    S := 'java -jar "{#JarName}" --spring.profiles.active=prod' + #13#10;
    S := ':fin' + #13#10;
    S := 'echo.' + #13#10;
    S := 'pause' + #13#10;
    SaveStringToFile(BatPath, AnsiString(S), False);
  end;
end;
