@echo off
cd /d "%~dp0"
title Wow Libre Realm
echo.
echo [Wow Libre Realm]
echo.
if not exist "wow-libre-client-0.0.1-SNAPSHOT.jar" (
  echo ERROR: No se encuentra el JAR en esta carpeta.
  echo Ruta: %CD%
  goto :fin
)
if exist .env for /f "usebackq eol=# tokens=* delims=" %%a in (".env") do set "%%a"
echo Iniciando aplicacion...
java -jar "wow-libre-client-0.0.1-SNAPSHOT.jar" --spring.profiles.active=prod
:fin
echo.
pause
