@echo off
REM Script para compilar Chat Web Server

echo.
echo ========================================
echo   Chat Web Server - Build con Maven
echo ========================================
echo.

REM Verificar si Maven esta instalado
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven no esta instalado o no esta en el PATH
    echo Instala Maven desde: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

REM Verificar credenciales de Firebase
if not exist serviceAccountKey.json (
    echo ADVERTENCIA: No se encontro serviceAccountKey.json
    echo Consulta README.md para instrucciones de configuracion.
    echo.
    set /p continue="Continuar de todas formas? (s/n): "
    if /i not "%continue%"=="s" exit /b 1
)

echo Compilando proyecto...
echo.
call mvn clean package -DskipTests

if %ERRORLEVEL% == 0 (
    echo.
    echo ========================================
    echo   Compilacion exitosa!
    echo ========================================
    echo.
    echo Para ejecutar: java -jar target\ChatWebServer-uber.jar
    echo O ejecuta: start.bat
    echo.
) else (
    echo.
    echo ERROR durante la compilacion.
    echo.
)

pause
