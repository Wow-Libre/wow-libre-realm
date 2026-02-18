@echo off
REM Compila wow-libre-realm y copia el JAR a installer\app para el instalador.
setlocal
cd /d "%~dp0\.."
if not exist "target\wow-libre-client-0.0.1-SNAPSHOT.jar" (
  echo Compilando proyecto...
  call mvn clean package -DskipTests
  if errorlevel 1 ( echo Error en mvn. & exit /b 1 )
)
if not exist "installer\app" mkdir installer\app
copy /Y "target\wow-libre-client-0.0.1-SNAPSHOT.jar" "installer\app\"
echo JAR copiado a installer\app\
echo Abre WowLibreRealm.iss en Inno Setup y compila.
endlocal
