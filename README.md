# ChihuChat — Chat Web en Tiempo Real con Java + Firebase

Aplicación de chat en tiempo real estilo WhatsApp construida con **Java (Jetty 10 WebSocket + HTTP)** y **Firebase Firestore** como base de datos, con soporte para **videollamadas/llamadas de voz (WebRTC)**, **cámara en tiempo real**, envío de **imágenes y videos** vía Cloudinary, y una interfaz web responsive.

---

## Características principales

| Categoría | Funcionalidades |
|---|---|
| **Mensajería** | Mensajes privados en tiempo real, mensajes de grupo, soporte de texto, emojis, imágenes y videos |
| **Videollamadas y cámara** | Llamadas de audio/video peer-to-peer (WebRTC) con acceso a la cámara del dispositivo de cada cliente conectado, toggle de cámara y micrófono en tiempo real |
| **Grupos** | Creación de grupos (mínimo 3 miembros), envío de mensajes multimedia al grupo |
| **Contactos** | Sistema de solicitudes de amistad (enviar, aceptar, rechazar), búsqueda de usuarios, eliminación de contactos |
| **Multimedia** | Envío de imágenes y videos alojados en Cloudinary (hasta 50 MB por mensaje), visualización inline con modal a pantalla completa |
| **Perfil** | Foto de perfil (subida a Cloudinary), estado personalizable, campo "Acerca de" |
| **Notificaciones** | Toast in-app y notificaciones nativas del navegador (`Notification API`) |
| **Presencia** | Indicador de "en línea" / "última vez" con timestamp relativo |
| **Responsive** | Diseño adaptable para escritorio, tablet y móvil (breakpoints: 1024px, 768px, 480px), soporte safe area iOS |

---

## Uso de la Cámara en el Chat (WebRTC)

Cada cliente conectado puede iniciar una **videollamada** que activa la cámara y el micrófono del dispositivo mediante la API del navegador:

```
navigator.mediaDevices.getUserMedia({ audio: true, video: true })
```

| API del navegador utilizada | Propósito |
|---|---|
| `navigator.mediaDevices.getUserMedia()` | Obtiene el stream de cámara y micrófono del usuario |
| `RTCPeerConnection` | Establece conexión peer-to-peer para transmisión de audio/video |
| Elemento `<video>` con `autoplay`, `playsinline` | Renderiza el video local (muted) y remoto |
| `AudioContext` / `MediaStreamDestination` | Desbloquea la reproducción de audio remoto (workaround para políticas de autoplay) |
| `MediaStream.getTracks()` / `track.enabled` | Permite activar/desactivar cámara y micrófono durante la llamada |

> **Nota:** La cámara se utiliza exclusivamente para videollamadas en tiempo real entre dos clientes. Cada usuario puede activar/desactivar su cámara y micrófono durante la llamada.

**Flujo de señalización WebRTC:**
1. El **emisor** solicita acceso a cámara/micrófono, crea un `RTCPeerConnection`, genera un SDP offer y lo envía al servidor vía WebSocket (`callRequest`)
2. El **receptor** recibe la solicitud de llamada con una notificación a pantalla completa
3. Al aceptar, el receptor obtiene su stream local, crea su `RTCPeerConnection`, establece el offer remoto, genera un SDP answer y lo envía de vuelta (`callAnswer`)
4. Ambos intercambian candidatos ICE (`iceCandidate`) vía WebSocket
5. Se establece la conexión peer-to-peer directa con video y audio

**Servidores STUN/TURN configurados** (necesarios para conectividad a través de NAT/firewalls):

| URL | Protocolo |
|---|---|
| `stun:stun.relay.metered.ca:80` | STUN |
| `turn:global.relay.metered.ca:80` | TURN (UDP) |
| `turn:global.relay.metered.ca:80?transport=tcp` | TURN (TCP) |
| `turn:global.relay.metered.ca:443` | TURN (TLS) |
| `turns:global.relay.metered.ca:443?transport=tcp` | TURNS (TCP) |

Las credenciales TURN se obtienen dinámicamente desde la API de Metered con fallback a credenciales estáticas.

---

## Estructura del Proyecto

```
JavaChatApp/
├── src/main/java/com/chatapp/
│   ├── server/
│   │   └── ChatWebServer.java           # Punto de entrada — Inicia Jetty (HTTP + WebSocket en un solo puerto)
│   ├── firebase/
│   │   └── FirebaseService.java         # Integración con Firebase Firestore (CRUD usuarios, mensajes, grupos, contactos)
│   ├── models/                          # Modelos de datos Java
│   │   ├── User.java                    # username, password, online, profilePicture, lastSeen, status, about
│   │   ├── Message.java                 # from, to, content, contentType (text/emoji/image/video), timestamp, groupId
│   │   └── Group.java                   # id, name, members[], creator, timestamp
│   └── websocket/
│       └── WebSocketHandler.java        # Lógica WebSocket — maneja 29+ tipos de mensajes (chat, llamadas, perfil, contactos)
├── public/                              # Frontend — Archivos web servidos por Jetty
│   ├── index.html                       # Interfaz completa (HTML + CSS inline ~2300 líneas)
│   └── js/
│       └── app.js                       # Lógica del cliente (módulos: Auth, WebSocket, Chat, Media, VideoCall, Contacts, Profile, Groups)
├── pom.xml                              # Configuración Maven (dependencias y plugins)
├── build.bat                            # Script de compilación para Windows
├── start.bat                            # Script de compilación + ejecución para Windows
├── Dockerfile                           # Build multi-etapa para despliegue en contenedor
├── serviceAccountKey.json               # Credenciales Firebase (⚠️ NO subir a repositorios públicos)
└── README.md
```

---

## Requisitos previos

| Requisito | Versión mínima | URL de descarga |
|---|---|---|
| **Java JDK** | 11 o superior | https://adoptium.net/temurin/releases/ |
| **Apache Maven** | 3.6 o superior | https://maven.apache.org/download.cgi |
| **Proyecto Firebase** | — | https://console.firebase.google.com |
| **Navegador moderno** | Chrome 80+, Firefox 75+, Edge 80+ | — |

> **Nota:** Tanto `java` como `mvn` deben estar disponibles en el `PATH` del sistema. Para verificar, ejecuta `java -version` y `mvn -version` en la terminal.

---

## Configuración paso a paso

### 1. Clonar o descargar el proyecto

```bash
git clone <Uhttps://github.com/Hector-Leonardo/JavaChatApp.git>
cd JavaChatApp
```

O descarga el ZIP y extráelo en una carpeta.

### 2. Configurar Firebase Firestore

1. Accede a [Firebase Console](https://console.firebase.google.com)
2. Crea un nuevo proyecto (o usa uno existente)
3. Ve a **Build** → **Firestore Database** → **Crear base de datos** (modo de prueba para desarrollo)
4. Ve a **Configuración del proyecto** (icono de engranaje) → **Cuentas de servicio**
5. Selecciona **Java** y haz clic en **Generar nueva clave privada**
6. Descarga el archivo JSON generado
7. Renómbralo a **`serviceAccountKey.json`** y colócalo en la **raíz del proyecto** (`JavaChatApp/serviceAccountKey.json`)

### 3. Compilar el proyecto

#### Opción A — Script de Windows

```batch
build.bat
```

#### Opción B — Maven directo

```bash
mvn clean package -DskipTests
```

Esto genera el archivo ejecutable: `target/ChatWebServer-uber.jar` (uber JAR empaquetado con todas las dependencias).

### 4. Ejecutar el servidor

#### Opción A — Script de Windows

```batch
start.bat
```

El script verifica que existan las credenciales de Firebase, compila si es necesario y ejecuta el servidor.

#### Opción B — Ejecución directa

```bash
java -jar target/ChatWebServer-uber.jar
```

El servidor se inicia en el **puerto 8080** (HTTP + WebSocket unificados en un solo puerto).

### 5. Abrir la aplicación

1. Abre tu navegador en: **http://localhost:8080**
2. **Regístrate** con un nombre de usuario y contraseña
3. Para probar con múltiples usuarios, abre **varias pestañas** o usa diferentes navegadores
4. Agrega contactos usando el botón "+" → busca por nombre de usuario → envía solicitud de amistad
5. Una vez aceptada la solicitud, puedes enviar mensajes, imágenes, videos o iniciar videollamadas

---

## Abrir el proyecto en un IDE

### Visual Studio Code

1. Abre VS Code
2. **File** → **Open Folder** → selecciona la carpeta `JavaChatApp`
3. Instala la extensión **Extension Pack for Java** (Microsoft) si no la tienes
4. Las tareas de build están preconfiguradas:
   - `Ctrl+Shift+B` → **maven: clean package** (compilar)
   - Terminal → **Run Task** → **Start Server** (compilar y ejecutar)

### IntelliJ IDEA

1. **File** → **Open** → selecciona la carpeta `JavaChatApp`
2. IntelliJ detectará automáticamente el `pom.xml` e importará las dependencias Maven
3. Configura el SDK del proyecto como **Java 11** (**File** → **Project Structure** → **Project SDK**)
4. Para ejecutar: clic derecho sobre `ChatWebServer.java` → **Run**

### Eclipse

1. **File** → **Import** → **Maven** → **Existing Maven Projects**
2. Selecciona la carpeta `JavaChatApp` como Root Directory
3. Eclipse resolverá las dependencias automáticamente
4. Clic derecho sobre `ChatWebServer.java` → **Run As** → **Java Application**

---

## Uso en Red Local (LAN)

Para que otros dispositivos en la misma red accedan al chat:

1. Obtén tu IP local:
   - **Windows:** `ipconfig` → busca "Dirección IPv4"
   - **Linux/Mac:** `ifconfig` o `ip addr`
2. En otros dispositivos, abre: `http://TU_IP:8080`
3. Para videollamadas en LAN, la conexión WebRTC funciona directamente vía STUN

---

## Despliegue con Docker

```bash
# Construir la imagen
docker build -t chihuchat .

# Ejecutar el contenedor
docker run -p 8080:8080 chihuchat
```

El `Dockerfile` usa build multi-etapa:
- **Etapa 1 (build):** `maven:3.9-eclipse-temurin-11` — descarga dependencias y compila
- **Etapa 2 (runtime):** `eclipse-temurin:11-jre` — imagen ligera solo con el JRE, copia el uber JAR y el directorio `public/`

---

## Puerto

| Puerto | Servicio |
|---|---|
| **8080** | HTTP (interfaz web) + WebSocket (`/ws`) — unificados en un solo puerto |

El puerto es configurable vía variable de entorno `PORT` (útil para despliegues en la nube como Render).

---

## Arquitectura técnica

### Backend (Servidor Java)

| Componente | Tecnología | Versión |
|---|---|---|
| Servidor HTTP + WebSocket | **Eclipse Jetty** | 10.0.20 |
| Base de datos | **Firebase Firestore** (Admin SDK) | 9.2.0 |
| Serialización JSON | **Google Gson** | 2.10.1 |
| Build/empaquetado | **Apache Maven** + Shade Plugin | 3.5.0 |
| Lenguaje | **Java** | 11 |

El servidor `ChatWebServer.java` inicializa Firebase, carga datos existentes (usuarios, conversaciones, grupos) en mapas `ConcurrentHashMap` en memoria para acceso rápido, y arranca Jetty con HTTP y WebSocket en un solo puerto. El endpoint WebSocket es `/ws` con un tamaño máximo de mensaje de **50 MB**.

### Frontend (Cliente Web)

| Componente | Tecnología |
|---|---|
| Interfaz | **HTML5, CSS3, JavaScript vanilla** (sin frameworks) |
| Comunicación | **WebSocket API** nativa del navegador |
| Videollamadas | **WebRTC** (`RTCPeerConnection`, `getUserMedia`) |
| Subida de archivos | **Cloudinary API** (vía `fetch` + `FormData`) |
| Notificaciones | **Notification API** del navegador |
| Iconos | SVGs inline |

El frontend está organizado en módulos lógicos: `AuthManager`, `WebSocketManager`, `MessageHandler`, `ChatManager`, `UIManager`, `MediaManager`, `ContactManager`, `ProfileManager`, `GroupManager` y `VideoCallManager`.

---

## Dependencias y librerías

### Backend (Maven — `pom.xml`)

| Dependencia | Versión | Propósito | URL |
|---|---|---|---|
| `com.google.firebase:firebase-admin` | 9.2.0 | SDK de administración de Firebase (Firestore) | https://firebase.google.com/docs/admin/setup |
| `com.google.code.gson:gson` | 2.10.1 | Serialización/deserialización JSON | https://github.com/google/gson |
| `org.eclipse.jetty:jetty-server` | 10.0.20 | Servidor HTTP embebido | https://eclipse.dev/jetty/ |
| `org.eclipse.jetty:jetty-servlet` | 10.0.20 | Contenedor de servlets | https://eclipse.dev/jetty/ |
| `org.eclipse.jetty.websocket:websocket-jetty-server` | 10.0.20 | Servidor WebSocket | https://eclipse.dev/jetty/ |
| `org.eclipse.jetty.websocket:websocket-jetty-api` | 10.0.20 | API de anotaciones WebSocket | https://eclipse.dev/jetty/ |

### Plugins Maven

| Plugin | Versión | Propósito |
|---|---|---|
| `maven-compiler-plugin` | 3.11.0 | Compilación Java 11 |
| `maven-shade-plugin` | 3.5.0 | Genera uber JAR con todas las dependencias incluidas |

### Frontend (APIs y servicios externos)

| Servicio | Propósito | URL |
|---|---|---|
| **Cloudinary** | Almacenamiento y entrega de imágenes/videos (fotos de perfil, archivos multimedia en el chat) | https://cloudinary.com |
| **Metered TURN** | Servidores STUN/TURN para conectividad WebRTC a través de NAT/firewalls | https://www.metered.ca/stun-turn |
| **Firebase Firestore** | Base de datos NoSQL en tiempo real (usuarios, mensajes, grupos, contactos, solicitudes) | https://firebase.google.com/docs/firestore |

### APIs del navegador utilizadas

| API | Propósito |
|---|---|
| `WebSocket` | Comunicación bidireccional en tiempo real con el servidor |
| `MediaDevices.getUserMedia()` | Acceso a la cámara y micrófono del dispositivo |
| `RTCPeerConnection` | Conexión peer-to-peer para video/audio (WebRTC) |
| `Notification` | Notificaciones push del navegador |
| `AudioContext` | Desbloqueo de audio remoto en videollamadas |
| `FileReader` | Lectura de archivos locales para envío |
| `fetch` | Subida de archivos a Cloudinary |

---

## Colecciones Firebase Firestore

| Colección | Campos principales | Descripción |
|---|---|---|
| `users` | `username`, `password`, `online`, `profilePicture`, `lastSeen`, `status`, `about` | Datos de usuarios registrados |
| `conversations` | `participants`, `updatedAt` + subcolección `messages` (`from`, `to`, `content`, `contentType`, `timestamp`, `read`) | Conversaciones privadas y su historial |
| `groups` | `id`, `name`, `members[]`, `creator`, `timestamp` + subcolección `messages` | Grupos de chat |
| `contacts` | `ownerUsername`, `contactUsername`, `addedAt` | Relaciones de contacto entre usuarios |
| `friend_requests` | `fromUsername`, `toUsername`, `status` (pending/accepted/rejected), `createdAt` | Solicitudes de amistad |

---

## Archivos fuente

### Servidor (Java)

| Archivo | Descripción |
|---|---|
| `src/main/java/com/chatapp/server/ChatWebServer.java` | Clase principal — configura e inicia el servidor Jetty con HTTP y WebSocket |
| `src/main/java/com/chatapp/firebase/FirebaseService.java` | Servicio de Firebase — inicialización, operaciones CRUD sobre Firestore |
| `src/main/java/com/chatapp/websocket/WebSocketHandler.java` | Lógica WebSocket — procesa 29+ tipos de mensajes (chat, llamadas WebRTC, perfil, contactos, grupos) |
| `src/main/java/com/chatapp/models/User.java` | Modelo de datos: Usuario |
| `src/main/java/com/chatapp/models/Message.java` | Modelo de datos: Mensaje (soporta text, emoji, image, video) |
| `src/main/java/com/chatapp/models/Group.java` | Modelo de datos: Grupo de chat |

### Cliente (Frontend)

| Archivo | Descripción |
|---|---|
| `public/index.html` | Interfaz completa del chat (HTML + CSS inline, ~2300 líneas de estilos) |
| `public/js/app.js` | Lógica completa del cliente en JavaScript vanilla — módulos de autenticación, WebSocket, chat, multimedia, videollamadas, contactos, perfil y grupos |

---

## Tipos de mensajes WebSocket

El protocolo de comunicación entre cliente y servidor utiliza JSON con un campo `type` que define la acción:

<details>
<summary><strong>Ver todos los tipos de mensajes (29+ tipos)</strong></summary>

| Tipo | Dirección | Descripción |
|---|---|---|
| `register` | Cliente → Servidor | Registrar nuevo usuario |
| `login` | Cliente → Servidor | Iniciar sesión |
| `getUsers` | Cliente → Servidor | Obtener lista de usuarios |
| `loadConversationHistory` | Cliente → Servidor | Cargar historial completo |
| `loadPrivateConversation` | Cliente → Servidor | Cargar conversación con usuario específico |
| `privateMessage` | Bidireccional | Enviar/recibir mensaje privado |
| `createGroup` | Cliente → Servidor | Crear grupo nuevo |
| `getGroups` | Cliente → Servidor | Obtener lista de grupos |
| `groupMessage` | Bidireccional | Enviar/recibir mensaje de grupo |
| `callRequest` | Cliente → Servidor | Solicitar videollamada (incluye SDP offer) |
| `callOffer` | Cliente → Servidor | Enviar offer SDP |
| `callAnswer` | Cliente → Servidor | Enviar answer SDP |
| `iceCandidate` | Bidireccional | Intercambio de candidatos ICE (WebRTC) |
| `callEnded` | Bidireccional | Notificar fin de llamada |
| `callRejected` | Cliente → Servidor | Rechazar llamada entrante |
| `updateProfilePicture` | Cliente → Servidor | Actualizar foto de perfil |
| `updateProfile` | Cliente → Servidor | Actualizar estado y "Acerca de" |
| `searchUser` | Cliente → Servidor | Buscar usuario por nombre |
| `addContact` | Cliente → Servidor | Agregar contacto |
| `removeContact` | Cliente → Servidor | Eliminar contacto y conversación |
| `getContacts` | Cliente → Servidor | Obtener lista de contactos |
| `sendFriendRequest` | Cliente → Servidor | Enviar solicitud de amistad |
| `acceptFriendRequest` | Cliente → Servidor | Aceptar solicitud |
| `rejectFriendRequest` | Cliente → Servidor | Rechazar solicitud |
| `getPendingRequests` | Cliente → Servidor | Obtener solicitudes pendientes recibidas |
| `getSentRequests` | Cliente → Servidor | Obtener solicitudes enviadas |

</details>

---

## Troubleshooting

| Problema | Solución |
|---|---|
| `mvn` no reconocido | Agrega Maven al `PATH` del sistema. Descarga: https://maven.apache.org/download.cgi |
| `java` no reconocido | Instala JDK 11+ y agrégalo al `PATH`. Descarga: https://adoptium.net/temurin/releases/ |
| Error de credenciales Firebase | Verifica que `serviceAccountKey.json` existe en la raíz del proyecto y contiene las credenciales correctas |
| Puerto 8080 en uso | Cambia el puerto con la variable de entorno `PORT`: `set PORT=3000` (Windows) o `export PORT=3000` (Linux/Mac) |
| Videollamada no conecta | Verifica que el navegador tiene permisos de cámara/micrófono. Si estás detrás de un firewall estricto, los servidores TURN se encargan de la conectividad |
| Imágenes/videos no se suben | Verifica tu conexión a internet (se suben a Cloudinary). El límite de mensaje WebSocket es 50 MB |

---

## Tecnologías utilizadas

| Capa | Tecnologías |
|---|---|
| **Backend** | Java 11, Eclipse Jetty 10.0.20, Firebase Admin SDK 9.2.0, Gson 2.10.1 |
| **Frontend** | HTML5, CSS3, JavaScript vanilla (ES6+) |
| **Base de datos** | Firebase Firestore (NoSQL, tiempo real) |
| **Comunicación** | WebSocket (Jetty), WebRTC (peer-to-peer audio/video) |
| **Multimedia** | Cloudinary (almacenamiento de imágenes/videos) |
| **Conectividad** | Metered STUN/TURN (traversal NAT para WebRTC) |
| **Build** | Apache Maven 3 + Shade Plugin (uber JAR) |
| **Contenedores** | Docker (build multi-etapa con Maven + Eclipse Temurin JRE 11) |
