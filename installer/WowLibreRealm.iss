; Script generado con estilo Inno Setup Script Wizard para Wow Libre Realm
; Requisito: compilar antes con mvn clean package y colocar el JAR en installer\app\

#define MyAppName "Wow Libre Realm"
#define MyAppVersion "0.0.1"
#define MyAppPublisher "Wow Libre"
#define MyAppURL "https://github.com/ManuChitiva/wow-libre-client"
#define JarName "wow-libre-client-0.0.1-SNAPSHOT.jar"

[Setup]
AppId={{A1B2C3D4-E5F6-7890-ABCD-EF1234567890}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}
AppUpdatesURL={#MyAppURL}
DefaultDirName={autopf}\WowLibreRealm
DefaultGroupName=Wow Libre
DisableProgramGroupPage=yes
LicenseFile=
OutputDir=output
OutputBaseFilename=WowLibreRealm-Setup-{#MyAppVersion}
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
Source: "app\{#JarName}"; DestDir: "{app}"; Flags: ignoreversion
Source: "IniciarRealm.bat"; DestDir: "{app}"; Flags: ignoreversion

[Icons]
Name: "{group}\Iniciar Wow Libre Realm"; Filename: "{app}\IniciarRealm.bat"; WorkingDir: "{app}"
Name: "{group}\Configurar variables (.env)"; Filename: "notepad.exe"; Parameters: "{app}\.env"; WorkingDir: "{app}"
Name: "{group}\{cm:UninstallProgram,{#MyAppName}}"; Filename: "{uninstallexe}"
Name: "{autodesktop}\Iniciar Wow Libre Realm"; Filename: "{app}\IniciarRealm.bat"; WorkingDir: "{app}"; Tasks: desktopicon

[Run]
Filename: "{app}\IniciarRealm.bat"; Description: "Iniciar Wow Libre Realm ahora"; Flags: postinstall nowait skipifsilent

[Code]
var
  PageDB, PageServer, PageRealm1, PageRealm2: TInputQueryWizardPage;

procedure InitializeWizard;
begin
  { Página 1: Base de datos (auth) }
  PageDB := CreateInputQueryPage(wpSelectDir,
    'Base de datos (Auth)', 'Configuración de MySQL para acore_auth',
    'Indica la URL JDBC, usuario y contraseña de la base de datos de autenticación del emulador.');
  PageDB.Add('URL JDBC (acore_auth):', False);
  PageDB.Values[0] := 'jdbc:mysql://localhost:3306/acore_auth';
  PageDB.Add('Usuario MySQL:', False);
  PageDB.Values[1] := 'root';
  PageDB.Add('Contraseña MySQL:', True);
  PageDB.Values[2] := '';

  { Página 2: Servidor y Core }
  PageServer := CreateInputQueryPage(PageDB.ID,
    'Servidor y WoW Libre Core', 'Puerto y URL del core',
    'Puerto donde escuchará este Realm y URL base de la API central WoW Libre.');
  PageServer.Add('Puerto del Realm (ej. 8090):', False);
  PageServer.Values[0] := '8090';
  PageServer.Add('URL base del Core (ej. http://localhost:8091):', False);
  PageServer.Values[1] := 'http://localhost:8091';
  PageServer.Add('SOAP URI por defecto (ej. http://127.0.0.1:7878):', False);
  PageServer.Values[2] := 'http://127.0.0.1:7878';
  PageServer.Add('Secret JWT (clave para tokens):', True);
  PageServer.Values[3] := '';

  { Página 3: Reino 1 }
  PageRealm1 := CreateInputQueryPage(PageServer.ID,
    'Reino 1 - Base de datos y SOAP', 'Characters, World y SOAP del primer reino',
    'URLs JDBC de acore_characters y acore_world, y credenciales SOAP (GM) del emulador.');
  PageRealm1.Add('URL JDBC acore_characters (Reino 1):', False);
  PageRealm1.Values[0] := 'jdbc:mysql://localhost:3306/acore_characters';
  PageRealm1.Add('URL JDBC acore_world (Reino 1):', False);
  PageRealm1.Values[1] := 'jdbc:mysql://localhost:3306/acore_world';
  PageRealm1.Add('URI SOAP Reino 1 (ej. http://127.0.0.1:7878):', False);
  PageRealm1.Values[2] := 'http://127.0.0.1:7878';
  PageRealm1.Add('Usuario GM SOAP Reino 1:', False);
  PageRealm1.Values[3] := '';
  PageRealm1.Add('Contraseña GM SOAP Reino 1:', True);
  PageRealm1.Values[4] := '';

  { Página 4: Reino 2 (opcional) }
  PageRealm2 := CreateInputQueryPage(PageRealm1.ID,
    'Reino 2 (opcional)', 'Si solo tienes un reino, deja los valores por defecto o en blanco',
    'Repite la configuración para un segundo reino. Puedes dejarlo y editar .env después.');
  PageRealm2.Add('URL JDBC acore_characters (Reino 2):', False);
  PageRealm2.Values[0] := 'jdbc:mysql://localhost:3306/acore_characters';
  PageRealm2.Add('URL JDBC acore_world (Reino 2):', False);
  PageRealm2.Values[1] := 'jdbc:mysql://localhost:3306/acore_world';
  PageRealm2.Add('URI SOAP Reino 2:', False);
  PageRealm2.Values[2] := 'http://127.0.0.1:7878';
  PageRealm2.Add('Usuario GM SOAP Reino 2:', False);
  PageRealm2.Values[3] := '';
  PageRealm2.Add('Contraseña GM SOAP Reino 2:', True);
  PageRealm2.Values[4] := '';
end;

function EnvLine(const Key, Value: String): String;
begin
  Result := Key + '=' + Value + #13#10;
end;

procedure CurStepChanged(CurStep: TSetupStep);
var
  EnvPath, S, JwtSecret: String;
  EnvContent: AnsiString;
begin
  if CurStep = ssPostInstall then
  begin
    EnvPath := ExpandConstant('{app}\.env');

    { Los valores se leen aquí, después de que el usuario haya pasado por las páginas }
    if Trim(PageServer.Values[3]) = '' then
      JwtSecret := 'CambiarClaveJWTSecretEnProduccion123'
    else
      JwtSecret := PageServer.Values[3];

    S := '';
    S := S + EnvLine('DB_WOW_CLIENT_HOST_AUTH', PageDB.Values[0]);
    S := S + EnvLine('DB_WOW_CLIENT_USERNAME', PageDB.Values[1]);
    S := S + EnvLine('DB_WOW_CLIENT_PASSWORD', PageDB.Values[2]);
    S := S + EnvLine('WOW_CLIENT_SERVER_PORT', PageServer.Values[0]);
    S := S + EnvLine('HOST_BASE_CORE', PageServer.Values[1]);
    S := S + EnvLine('SOAP_CLIENT_DEFAULT_URI', PageServer.Values[2]);
    S := S + EnvLine('WOW_CLIENT_SECRET_JWT', JwtSecret);
    S := S + EnvLine('DB_WOW_CLIENT_HOST_CHARACTERS', PageRealm1.Values[0]);
    S := S + EnvLine('DB_WOW_CLIENT_HOST_WORLD', PageRealm1.Values[1]);
    S := S + EnvLine('WOW_CLIENT_SOAP_URI', PageRealm1.Values[2]);
    S := S + EnvLine('WOW_CLIENT_SOAP_GM_USERNAME', PageRealm1.Values[3]);
    S := S + EnvLine('WOW_CLIENT_SOAP_GM_PASSWORD', PageRealm1.Values[4]);
    S := S + EnvLine('DB_WOW_CLIENT_HOST_CHARACTERS_REALM_2', PageRealm2.Values[0]);
    S := S + EnvLine('DB_WOW_CLIENT_HOST_WORLD_REALM_2', PageRealm2.Values[1]);
    S := S + EnvLine('WOW_CLIENT_SOAP_URI_REALM_2', PageRealm2.Values[2]);
    S := S + EnvLine('DB_WOW_CLIENT_USERNAME_REALM_2', PageDB.Values[1]);
    S := S + EnvLine('DB_WOW_CLIENT_PASSWORD_REALM_2', PageDB.Values[2]);
    S := S + EnvLine('WOW_CLIENT_SOAP_GM_USERNAME_REALM_2', PageRealm2.Values[3]);
    S := S + EnvLine('WOW_CLIENT_SOAP_GM_PASSWORD_REALM_2', PageRealm2.Values[4]);

    EnvContent := AnsiString(S);
    SaveStringToFile(EnvPath, EnvContent, False);
    { IniciarRealm.bat se copia desde [Files], no se genera por codigo }
  end;
end;
