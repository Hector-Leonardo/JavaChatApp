package com.chatapp.server;

import com.chatapp.firebase.FirebaseService;
import com.chatapp.models.Group;
import com.chatapp.models.Message;
import com.chatapp.models.User;
import com.chatapp.websocket.WebSocketHandler;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.websocket.server.config.JettyWebSocketServletContainerInitializer;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servidor principal de Chat Web
 * Usa Jetty para HTTP + WebSocket en un solo puerto (compatible con Render)
 */
public class ChatWebServer {
    private static final int DEFAULT_PORT = 8080;

    // Almacenamiento en memoria (sincronizado)
    private static Map<String, User> users = new ConcurrentHashMap<>();
    private static Map<String, WebSocketHandler> onlineUsers = new ConcurrentHashMap<>();
    private static Map<String, List<Message>> conversations = new ConcurrentHashMap<>();
    private static Map<String, Group> groups = new ConcurrentHashMap<>();

    /**
     * Punto de entrada de la aplicación
     */
    public static void main(String[] args) throws Exception {
        System.out.println("\n===========================================");
        System.out.println("   INICIALIZANDO CHAT WEB SERVER");
        System.out.println("===========================================\n");

        // Puerto desde variable de entorno (Render lo asigna dinámicamente)
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", String.valueOf(DEFAULT_PORT)));

        // Inicializar Firebase Firestore
        FirebaseService.initialize();

        // Cargar datos iniciales de Firestore
        loadInitialData();

        // Iniciar servidor Jetty (HTTP + WebSocket en un solo puerto)
        startServer(port);
    }

    /**
     * Inicia servidor Jetty con HTTP estático y WebSocket en el mismo puerto
     */
    private static void startServer(int port) throws Exception {
        Server server = new Server(port);

        // Contexto principal
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        // Resolver la ruta del directorio public/
        Path publicPath = resolvePublicDir();
        context.setBaseResource(Resource.newResource(publicPath.toUri()));

        // Configurar WebSocket en la ruta /ws
        JettyWebSocketServletContainerInitializer.configure(context, (servletContext, wsContainer) -> {
            wsContainer.setMaxTextMessageSize(50 * 1024 * 1024); // 50MB
            wsContainer.setMaxBinaryMessageSize(50 * 1024 * 1024);
            wsContainer.addMapping("/ws",
                    (req, resp) -> new WebSocketHandler(users, onlineUsers, conversations, groups));
        });

        // Servlet para archivos estáticos (index.html, js/, css/)
        ServletHolder staticHolder = new ServletHolder("default", DefaultServlet.class);
        staticHolder.setInitParameter("dirAllowed", "false");
        staticHolder.setInitParameter("welcomeFiles", "index.html");
        context.addServlet(staticHolder, "/");

        server.setHandler(context);
        server.start();

        System.out.println("===========================================");
        System.out.println("   CHAT WEB SERVER INICIADO");
        System.out.println("===========================================");
        System.out.println("✓ Servidor HTTP + WebSocket: puerto " + port);
        System.out.println("✓ WebSocket endpoint: /ws");
        System.out.println("✓ Base de datos: Firebase Firestore");
        System.out.println("-------------------------------------------");
        System.out.println("→ Abre tu navegador en: http://localhost:" + port);
        System.out.println("===========================================\n");

        server.join(); // Bloquea hasta que el servidor se detenga
    }

    /**
     * Resuelve la ruta del directorio public/ (funciona en JAR y en desarrollo)
     */
    private static Path resolvePublicDir() {
        // Primero intentar directorio relativo (desarrollo y producción)
        Path publicDir = Paths.get("public");
        if (Files.isDirectory(publicDir)) {
            System.out.println("[SERVER] Sirviendo archivos desde: " + publicDir.toAbsolutePath());
            return publicDir;
        }

        // Fallback: buscar junto al JAR
        try {
            URL jarLocation = ChatWebServer.class.getProtectionDomain().getCodeSource().getLocation();
            Path jarDir = Paths.get(jarLocation.toURI()).getParent();
            Path jarPublicDir = jarDir.resolve("public");
            if (Files.isDirectory(jarPublicDir)) {
                System.out.println("[SERVER] Sirviendo archivos desde: " + jarPublicDir.toAbsolutePath());
                return jarPublicDir;
            }
        } catch (Exception e) {
            System.err.println("[WARN] No se pudo resolver directorio del JAR: " + e.getMessage());
        }

        System.err.println("[WARN] Directorio public/ no encontrado, usando directorio actual");
        return Paths.get(".");
    }

    /**
     * Carga datos iniciales desde Firebase Firestore
     */
    private static void loadInitialData() {
        try {
            // Cargar usuarios
            var allUsersData = FirebaseService.getAllUsers();
            for (var entry : allUsersData.entrySet()) {
                Map<String, Object> userData = entry.getValue();
                User user = new User(
                        (String) userData.get("username"),
                        (String) userData.get("password"));
                // Cargar foto de perfil si existe
                if (userData.get("profilePicture") != null) {
                    user.setProfilePicture((String) userData.get("profilePicture"));
                }
                // Cargar última conexión si existe
                if (userData.get("lastSeen") != null) {
                    user.setLastSeen((Long) userData.get("lastSeen"));
                }
                users.put(entry.getKey(), user);
            }

            // Cargar conversaciones
            conversations = new ConcurrentHashMap<>();
            var allConversations = FirebaseService.getAllConversations();
            for (var entry : allConversations.entrySet()) {
                List<Message> msgList = new ArrayList<>();
                List<Map<String, Object>> rawMessages = entry.getValue();

                if (rawMessages != null) {
                    for (Map<String, Object> raw : rawMessages) {
                        String from = raw.get("from") != null ? raw.get("from").toString() : "";
                        String to = raw.get("to") != null ? raw.get("to").toString() : "";
                        String content = raw.get("content") != null ? raw.get("content").toString() : "";
                        String contentType = raw.get("contentType") != null ? raw.get("contentType").toString()
                                : "text";
                        String timestamp = raw.get("timestamp") != null ? raw.get("timestamp").toString() : "";

                        Message msg = new Message(from, to, content, contentType);
                        if (!timestamp.isEmpty()) {
                            msg.setTimestamp(timestamp);
                        }
                        msgList.add(msg);
                    }
                }

                conversations.put(entry.getKey(), msgList);
            }

            // Cargar grupos
            groups = new ConcurrentHashMap<>();
            var allGroups = FirebaseService.getAllGroups();
            for (var entry : allGroups.entrySet()) {
                Map<String, Object> groupData = entry.getValue();
                @SuppressWarnings("unchecked")
                List<String> members = (List<String>) groupData.get("members");
                Group group = new Group(
                        (String) groupData.get("id"),
                        (String) groupData.get("name"),
                        members,
                        (String) groupData.get("creator"));
                groups.put(entry.getKey(), group);
            }

            // Cargar mensajes de grupos en memoria
            for (Group group : groups.values()) {
                List<Message> msgList = new ArrayList<>();
                List<Map<String, Object>> rawMessages = FirebaseService.getGroupMessages(group.getId());
                if (rawMessages != null) {
                    for (Map<String, Object> raw : rawMessages) {
                        String from = raw.get("from") != null ? raw.get("from").toString() : "";
                        String content = raw.get("content") != null ? raw.get("content").toString() : "";
                        String contentType = raw.get("contentType") != null ? raw.get("contentType").toString()
                                : "text";
                        String timestamp = raw.get("timestamp") != null ? raw.get("timestamp").toString() : "";

                        Message msg = new Message(from, group.getId(), content, contentType, true);
                        if (!timestamp.isEmpty()) {
                            msg.setTimestamp(timestamp);
                        }
                        msgList.add(msg);
                    }
                }

                conversations.put(group.getId(), msgList);
            }

            System.out.println("[FIREBASE] Datos cargados: " + users.size() + " usuarios, " +
                    conversations.size() + " conversaciones, " + groups.size() + " grupos\n");
        } catch (Exception e) {
            System.err.println("[ERROR] No se pudieron cargar los datos iniciales de Firestore");
            e.printStackTrace();
        }
    }

    // Métodos getter para acceso a datos en caso de necesidad
    public static Map<String, User> getUsers() {
        return users;
    }

    public static Map<String, WebSocketHandler> getOnlineUsers() {
        return onlineUsers;
    }

    public static Map<String, List<Message>> getConversations() {
        return conversations;
    }

    public static Map<String, Group> getGroups() {
        return groups;
    }
}
