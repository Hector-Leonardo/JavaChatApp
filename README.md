# Chat Web - Java WebSocket + Firebase

Aplicación de chat en tiempo real construida con **Java WebSockets nativos** y **Firebase Firestore** como base de datos.

## Características

- Mensajes privados en tiempo real
- Grupos de chat
- Solicitudes de amistad y gestión de contactos
- Llamadas de voz/video (WebRTC)
- Envío de imágenes (hasta 15MB)
- Foto de perfil y estado personalizable
- Notificaciones de conexión/desconexión
- Interfaz web responsive

## Estructura del Proyecto

```
JavaChatApp/
├── src/main/java/com/chatapp/
│   ├── server/ChatWebServer.java        # Punto de entrada principal
│   ├── firebase/FirebaseService.java    # Integración con Firestore
│   ├── handlers/StaticFileHandler.java  # Servidor HTTP de archivos estáticos
│   ├── models/                          # Modelos de datos
│   │   ├── User.java
│   │   ├── Message.java
│   │   ├── Group.java
│   │   ├── Contact.java
│   │   └── FriendRequest.java
│   └── websocket/WebSocketHandler.java  # Protocolo WebSocket y lógica de chat
├── public/                              # Archivos web (frontend)
│   ├── index.html
│   └── js/app.js
├── pom.xml                              # Configuración Maven
├── build.bat                            # Script de compilación (Windows)
├── start.bat                            # Script de ejecución (Windows)
└── serviceAccountKey.json               # Credenciales Firebase (NO versionado)
```

## Requisitos

- **Java 11** o superior
- **Maven 3.6** o superior
- **Proyecto Firebase** con Firestore habilitado

## Configuración de Firebase

1. Accede a [Firebase Console](https://console.firebase.google.com)
2. Crea un nuevo proyecto y habilita **Firestore Database**
3. Ve a **Configuración del proyecto** → **Cuentas de servicio**
4. Haz clic en **Generar nueva clave privada** y descarga el JSON
5. Renómbralo a `serviceAccountKey.json` y colócalo en la raíz del proyecto

> **Importante:** Nunca subas `serviceAccountKey.json` a un repositorio público.

## Compilar y ejecutar

### Usando scripts (Windows)

```batch
build.bat        # Compilar
start.bat        # Compilar y ejecutar
```

### Usando Maven directamente

```bash
# Compilar
mvn clean package -DskipTests

# Ejecutar
java -jar target/ChatWebServer-uber.jar
```

## Uso

1. Inicia el servidor
2. Abre tu navegador en **http://localhost:8080**
3. Regístrate con un nombre de usuario y contraseña
4. Para múltiples usuarios, abre varias pestañas

## Puertos

| Puerto | Servicio  |
|--------|-----------|
| 8080   | HTTP (web)|
| 8081   | WebSocket |

## Uso en Red Local (LAN)

1. Encuentra tu IP local: `ipconfig` (Windows) / `ifconfig` (Linux/Mac)
2. En otros dispositivos, abre: `http://TU_IP:8080`

## Tecnologías

- **Backend:** Java 11, WebSocket nativo (RFC 6455), com.sun.net.httpserver
- **Base de datos:** Firebase Firestore
- **Frontend:** HTML, CSS, JavaScript vanilla
- **Build:** Maven + Shade Plugin (uber JAR)
