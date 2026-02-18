@echo off
cls
echo.
echo ========================================
echo    Chat Web Server - Iniciando...
echo ========================================
echo.

REM Verificar credenciales
if not exist serviceAccountKey.json (
    echo ERROR: No se encontro serviceAccountKey.json
    echo Consulta README.md para instrucciones de configuracion.
    pause
    exit /b 1
)

REM Ejecutar JAR si existe, sino compilar primero
if exist "target\ChatWebServer-uber.jar" (
    echo JAR encontrado. Iniciando servidor...
    echo.
    java -jar target\ChatWebServer-uber.jar
) else (
    echo JAR no encontrado. Compilando proyecto...
    call build.bat
    if exist "target\ChatWebServer-uber.jar" (
        echo.
        echo Iniciando servidor...
        echo.
        java -jar target\ChatWebServer-uber.jar
    ) else (
        echo Error: No se pudo generar el JAR.
        pause
        exit /b 1
    )
)

pause
