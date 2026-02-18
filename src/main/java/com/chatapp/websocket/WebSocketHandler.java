package com.chatapp.websocket;

import com.chatapp.firebase.FirebaseService;
import com.chatapp.models.Group;
import com.chatapp.models.Message;
import com.chatapp.models.User;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manejador para conexiones WebSocket (Jetty API)
 * Procesa mensajes y coordina la comunicación entre clientes
 */
@WebSocket(maxTextMessageSize = 50 * 1024 * 1024)
public class WebSocketHandler {
    // Referencias compartidas entre handlers
    private static Map<String, User> users;
    private static Map<String, WebSocketHandler> onlineUsers;
    private static Map<String, List<Message>> conversations;
    private static Map<String, Group> groups;
    private static final int MAX_IMAGE_SIZE = 15 * 1024 * 1024; // 15MB

    private Session session;
    private String username = null;
    private boolean connected = false;

    /**
     * Constructor con contexto compartido
     */
    public WebSocketHandler(Map<String, User> users, Map<String, WebSocketHandler> onlineUsers,
            Map<String, List<Message>> conversations, Map<String, Group> groups) {
        WebSocketHandler.users = users;
        WebSocketHandler.onlineUsers = onlineUsers;
        WebSocketHandler.conversations = conversations;
        WebSocketHandler.groups = groups;
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        this.session = session;
        this.connected = true;
        System.out.println("[+] Cliente conectado desde " + session.getRemoteAddress());
    }

    @OnWebSocketMessage
    public void onMessage(String message) {
        if (message != null) {
            processMessage(message);
        }
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        disconnect();
    }

    @OnWebSocketError
    public void onError(Throwable error) {
        System.err.println("[ERROR] WebSocket error: " + error.getMessage());
        disconnect();
    }

    /**
     * Procesa un mensaje JSON recibido del cliente
     */
    private void processMessage(String message) {
        try {
            String type = extractJsonValue(message, "type");

            switch (type) {
                case "register":
                    handleRegister(message);
                    break;
                case "login":
                    handleLogin(message);
                    break;
                case "getUsers":
                    handleGetUsers();
                    break;
                case "loadConversationHistory":
                    handleLoadConversationHistory();
                    break;
                case "loadPrivateConversation":
                    handleLoadPrivateConversation(message);
                    break;
                case "privateMessage":
                    handlePrivateMessage(message);
                    break;
                case "createGroup":
                    handleCreateGroup(message);
                    break;
                case "getGroups":
                    handleGetGroups();
                    break;
                case "groupMessage":
                    handleGroupMessage(message);
                    break;
                // WebRTC Call Signaling
                case "callRequest":
                    handleCallRequest(message);
                    break;
                case "callOffer":
                    handleCallOffer(message);
                    break;
                case "callAnswer":
                    handleCallAnswer(message);
                    break;
                case "iceCandidate":
                    handleIceCandidate(message);
                    break;
                case "callEnded":
                    handleCallEnded(message);
                    break;
                case "callRejected":
                    handleCallRejected(message);
                    break;
                case "updateProfilePicture":
                    handleUpdateProfilePicture(message);
                    break;
                case "updateProfile":
                    handleUpdateProfile(message);
                    break;
                // Contacts
                case "searchUser":
                    handleSearchUser(message);
                    break;
                case "addContact":
                    handleAddContact(message);
                    break;
                case "removeContact":
                    handleRemoveContact(message);
                    break;
                case "getContacts":
                    handleGetContacts();
                    break;
                // Friend Requests
                case "sendFriendRequest":
                    handleSendFriendRequest(message);
                    break;
                case "acceptFriendRequest":
                    handleAcceptFriendRequest(message);
                    break;
                case "rejectFriendRequest":
                    handleRejectFriendRequest(message);
                    break;
                case "getPendingRequests":
                    handleGetPendingRequests();
                    break;
                case "getSentRequests":
                    handleGetSentRequests();
                    break;
                default:
                    System.out.println("[DEBUG] Tipo de mensaje desconocido: " + type);
                    break;
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Error procesando mensaje: " + e.getMessage());
        }
    }

    // ==================== AUTH HANDLERS ====================

    /**
     * Maneja registro de usuario
     */
    private void handleRegister(String message) {
        String username = extractJsonValue(message, "username");
        String password = extractJsonValue(message, "password");

        if (username.isEmpty() || password.isEmpty()) {
            sendMessage("{\"type\":\"registerResponse\",\"success\":false,\"error\":\"Campos vacíos\"}");
            return;
        }

        if (users.containsKey(username)) {
            sendMessage("{\"type\":\"registerResponse\",\"success\":false,\"error\":\"Usuario ya existe\"}");
            return;
        }

        users.put(username, new User(username, password));
        try {
            FirebaseService.saveUser(username, password);
        } catch (Exception e) {
            System.err.println("[ERROR] No se pudo guardar usuario en Firebase: " + e.getMessage());
        }
        System.out.println("[REGISTER] Usuario registrado: " + username);
        sendMessage("{\"type\":\"registerResponse\",\"success\":true}");
    }

    /**
     * Maneja login de usuario
     */
    private void handleLogin(String message) {
        String username = extractJsonValue(message, "username");
        String password = extractJsonValue(message, "password");

        if (!users.containsKey(username)) {
            sendMessage("{\"type\":\"loginResponse\",\"success\":false,\"error\":\"Usuario no existe\"}");
            return;
        }

        User user = users.get(username);
        if (!user.getPassword().equals(password)) {
            sendMessage("{\"type\":\"loginResponse\",\"success\":false,\"error\":\"Contraseña incorrecta\"}");
            return;
        }

        if (onlineUsers.containsKey(username)) {
            sendMessage("{\"type\":\"loginResponse\",\"success\":false,\"error\":\"Usuario ya conectado\"}");
            return;
        }

        this.username = username;
        user.setOnline(true);
        onlineUsers.put(username, this);

        System.out.println("[LOGIN] " + username + " conectado");

        // Construir respuesta con foto de perfil si existe
        StringBuilder response = new StringBuilder();
        response.append("{\"type\":\"loginResponse\",\"success\":true,\"username\":\"").append(username).append("\"");
        if (user.getProfilePicture() != null && !user.getProfilePicture().isEmpty()) {
            response.append(",\"profilePicture\":").append(jsonString(user.getProfilePicture()));
        }
        if (user.getStatus() != null && !user.getStatus().isEmpty()) {
            response.append(",\"status\":").append(jsonString(user.getStatus()));
        }
        if (user.getAbout() != null && !user.getAbout().isEmpty()) {
            response.append(",\"about\":").append(jsonString(user.getAbout()));
        }
        response.append("}");
        sendMessage(response.toString());

        // Notificar a todos sobre nuevo usuario online
        broadcastUserStatus(username, true);
    }

    // ==================== USER HANDLERS ====================

    /**
     * Maneja solicitud de lista de contactos (solo contactos agregados)
     */
    private void handleGetUsers() {
        if (username == null)
            return;

        try {
            // Obtener solo los contactos del usuario actual desde Firebase
            List<String> contactList = FirebaseService.getContacts(username);

            StringBuilder userList = new StringBuilder("[");
            boolean first = true;
            for (String contactUsername : contactList) {
                if (users.containsKey(contactUsername)) {
                    if (!first)
                        userList.append(",");
                    boolean online = onlineUsers.containsKey(contactUsername);
                    User contactUser = users.get(contactUsername);
                    String profilePic = contactUser.getProfilePicture();
                    Long lastSeen = contactUser.getLastSeen();
                    userList.append("{\"username\":\"").append(contactUsername).append("\",\"online\":")
                            .append(online);
                    if (profilePic != null && !profilePic.isEmpty()) {
                        userList.append(",\"profilePicture\":").append(jsonString(profilePic));
                    }
                    if (lastSeen != null) {
                        userList.append(",\"lastSeen\":").append(lastSeen);
                    }
                    userList.append("}");
                    first = false;
                }
            }
            userList.append("]");

            sendMessage("{\"type\":\"userList\",\"users\":" + userList.toString() + "}");
        } catch (Exception e) {
            System.err.println("[ERROR] Error obteniendo contactos: " + e.getMessage());
            sendMessage("{\"type\":\"userList\",\"users\":[]}");
        }
    }

    /**
     * Maneja búsqueda de usuario por nombre
     */
    private void handleSearchUser(String message) {
        if (username == null)
            return;

        String searchUsername = extractJsonValue(message, "username");

        if (searchUsername == null || searchUsername.isEmpty()) {
            sendMessage("{\"type\":\"searchUserResponse\",\"success\":false,\"error\":\"Nombre de usuario vacío\"}");
            return;
        }

        // No permitir buscarse a sí mismo
        if (searchUsername.equals(username)) {
            sendMessage(
                    "{\"type\":\"searchUserResponse\",\"success\":false,\"error\":\"No puedes agregarte a ti mismo\"}");
            return;
        }

        try {
            // Verificar si el usuario existe
            boolean exists = FirebaseService.userExists(searchUsername);

            if (!exists) {
                sendMessage("{\"type\":\"searchUserResponse\",\"success\":false,\"error\":\"Usuario no encontrado\"}");
                return;
            }

            // Verificar si ya es contacto
            boolean alreadyContact = FirebaseService.contactExists(username, searchUsername);

            if (alreadyContact) {
                sendMessage(
                        "{\"type\":\"searchUserResponse\",\"success\":false,\"error\":\"Ya tienes este contacto agregado\"}");
                return;
            }

            // Usuario encontrado y no es contacto aún
            User foundUser = users.get(searchUsername);
            boolean online = onlineUsers.containsKey(searchUsername);
            String profilePic = foundUser != null ? foundUser.getProfilePicture() : null;

            StringBuilder response = new StringBuilder();
            response.append("{\"type\":\"searchUserResponse\",\"success\":true,\"user\":{");
            response.append("\"username\":\"").append(searchUsername).append("\",");
            response.append("\"online\":").append(online);
            if (profilePic != null && !profilePic.isEmpty()) {
                response.append(",\"profilePicture\":").append(jsonString(profilePic));
            }
            response.append("}}");

            sendMessage(response.toString());

        } catch (Exception e) {
            System.err.println("[ERROR] Error buscando usuario: " + e.getMessage());
            sendMessage("{\"type\":\"searchUserResponse\",\"success\":false,\"error\":\"Error al buscar usuario\"}");
        }
    }

    /**
     * Maneja agregar un contacto
     */
    private void handleAddContact(String message) {
        if (username == null)
            return;

        String contactUsername = extractJsonValue(message, "username");

        if (contactUsername == null || contactUsername.isEmpty()) {
            sendMessage("{\"type\":\"addContactResponse\",\"success\":false,\"error\":\"Nombre de usuario vacío\"}");
            return;
        }

        try {
            // Verificar que el usuario existe
            if (!FirebaseService.userExists(contactUsername)) {
                sendMessage("{\"type\":\"addContactResponse\",\"success\":false,\"error\":\"Usuario no existe\"}");
                return;
            }

            // Verificar que no sea él mismo
            if (contactUsername.equals(username)) {
                sendMessage(
                        "{\"type\":\"addContactResponse\",\"success\":false,\"error\":\"No puedes agregarte a ti mismo\"}");
                return;
            }

            // Verificar que no exista el contacto
            if (FirebaseService.contactExists(username, contactUsername)) {
                sendMessage("{\"type\":\"addContactResponse\",\"success\":false,\"error\":\"Contacto ya existe\"}");
                return;
            }

            // Agregar contacto
            FirebaseService.addContact(username, contactUsername);

            // Obtener info del contacto para la respuesta
            User contactUser = users.get(contactUsername);
            boolean online = onlineUsers.containsKey(contactUsername);
            String profilePic = contactUser != null ? contactUser.getProfilePicture() : null;

            StringBuilder response = new StringBuilder();
            response.append("{\"type\":\"addContactResponse\",\"success\":true,\"contact\":{");
            response.append("\"username\":\"").append(contactUsername).append("\",");
            response.append("\"online\":").append(online);
            if (profilePic != null && !profilePic.isEmpty()) {
                response.append(",\"profilePicture\":").append(jsonString(profilePic));
            }
            response.append("}}");

            sendMessage(response.toString());
            System.out.println("[CONTACT] " + username + " agregó a " + contactUsername);

        } catch (Exception e) {
            System.err.println("[ERROR] Error agregando contacto: " + e.getMessage());
            sendMessage("{\"type\":\"addContactResponse\",\"success\":false,\"error\":\"Error al agregar contacto\"}");
        }
    }

    /**
     * Maneja eliminar un contacto y su conversación
     */
    private void handleRemoveContact(String message) {
        if (username == null)
            return;

        String contactUsername = extractJsonValue(message, "username");

        try {
            // Eliminar contacto bidireccionalmente y la conversación
            FirebaseService.deleteContactAndConversation(username, contactUsername);

            // Eliminar de memoria local
            String conversationId = getConversationId(username, contactUsername);
            conversations.remove(conversationId);

            // Notificar al usuario que eliminó
            sendMessage(
                    "{\"type\":\"removeContactResponse\",\"success\":true,\"username\":\"" + contactUsername + "\"}");

            // Notificar al otro usuario si está conectado
            WebSocketHandler otherHandler = onlineUsers.get(contactUsername);
            if (otherHandler != null) {
                otherHandler.sendMessage(
                        "{\"type\":\"contactRemoved\",\"username\":\"" + username + "\"}");
            }

            System.out.println("[CONTACT] " + username + " eliminó contacto y chat con " + contactUsername);
        } catch (Exception e) {
            System.err.println("[ERROR] Error eliminando contacto: " + e.getMessage());
            sendMessage(
                    "{\"type\":\"removeContactResponse\",\"success\":false,\"error\":\"Error al eliminar contacto\"}");
        }
    }

    /**
     * Maneja obtener lista de contactos (alias de getUsers pero más explícito)
     */
    private void handleGetContacts() {
        handleGetUsers();
    }

    // ==================== FRIEND REQUEST HANDLERS ====================

    /**
     * Maneja envío de solicitud de amistad
     */
    private void handleSendFriendRequest(String message) {
        if (username == null)
            return;

        String toUsername = extractJsonValue(message, "username");

        if (toUsername == null || toUsername.isEmpty()) {
            sendMessage("{\"type\":\"friendRequestResponse\",\"success\":false,\"error\":\"Nombre de usuario vacío\"}");
            return;
        }

        try {
            // Verificar que el usuario existe
            if (!FirebaseService.userExists(toUsername)) {
                sendMessage(
                        "{\"type\":\"friendRequestResponse\",\"success\":false,\"error\":\"Usuario no encontrado\"}");
                return;
            }

            // Verificar que no sea él mismo
            if (toUsername.equals(username)) {
                sendMessage(
                        "{\"type\":\"friendRequestResponse\",\"success\":false,\"error\":\"No puedes enviarte solicitud a ti mismo\"}");
                return;
            }

            // Verificar que no sean ya contactos
            if (FirebaseService.contactExists(username, toUsername)) {
                sendMessage("{\"type\":\"friendRequestResponse\",\"success\":false,\"error\":\"Ya son contactos\"}");
                return;
            }

            // Verificar que no exista ya una solicitud pendiente
            if (FirebaseService.friendRequestExists(username, toUsername)) {
                sendMessage(
                        "{\"type\":\"friendRequestResponse\",\"success\":false,\"error\":\"Ya existe una solicitud pendiente\"}");
                return;
            }

            // Crear solicitud de amistad
            String requestId = FirebaseService.createFriendRequest(username, toUsername);

            // Obtener info del usuario que envía para la notificación
            User fromUser = users.get(username);
            String profilePic = fromUser != null ? fromUser.getProfilePicture() : null;

            // Responder al que envió
            sendMessage("{\"type\":\"friendRequestResponse\",\"success\":true,\"message\":\"Solicitud enviada\"}");
            System.out.println("[FRIEND REQUEST] " + username + " envió solicitud a " + toUsername);

            // Notificar al receptor en tiempo real si está conectado
            WebSocketHandler recipientHandler = onlineUsers.get(toUsername);
            if (recipientHandler != null) {
                StringBuilder notification = new StringBuilder();
                notification.append("{\"type\":\"newFriendRequest\",\"request\":{");
                notification.append("\"id\":\"").append(requestId).append("\",");
                notification.append("\"fromUsername\":\"").append(username).append("\",");
                notification.append("\"toUsername\":\"").append(toUsername).append("\"");
                if (profilePic != null && !profilePic.isEmpty()) {
                    notification.append(",\"profilePicture\":").append(jsonString(profilePic));
                }
                notification.append("}}");
                recipientHandler.sendMessage(notification.toString());
            }

        } catch (Exception e) {
            System.err.println("[ERROR] Error enviando solicitud: " + e.getMessage());
            sendMessage(
                    "{\"type\":\"friendRequestResponse\",\"success\":false,\"error\":\"Error al enviar solicitud\"}");
        }
    }

    /**
     * Maneja aceptación de solicitud de amistad
     */
    private void handleAcceptFriendRequest(String message) {
        if (username == null)
            return;

        String requestId = extractJsonValue(message, "requestId");

        try {
            // Obtener info de la solicitud antes de aceptar
            Map<String, Object> requestData = FirebaseService.getFriendRequest(requestId);
            if (requestData == null) {
                sendMessage(
                        "{\"type\":\"acceptRequestResponse\",\"success\":false,\"error\":\"Solicitud no encontrada\"}");
                return;
            }

            String fromUsername = (String) requestData.get("fromUsername");
            String toUsername = (String) requestData.get("toUsername");

            // Verificar que el usuario actual es el receptor
            if (!toUsername.equals(username)) {
                sendMessage("{\"type\":\"acceptRequestResponse\",\"success\":false,\"error\":\"No autorizado\"}");
                return;
            }

            // Aceptar solicitud (esto agrega contacto mutuo)
            FirebaseService.acceptFriendRequest(requestId);

            // Obtener info de ambos usuarios para las respuestas
            User fromUser = users.get(fromUsername);
            User acceptingUser = users.get(username);
            boolean fromOnline = onlineUsers.containsKey(fromUsername);
            boolean toOnline = onlineUsers.containsKey(username);

            // Responder al que aceptó con info del nuevo contacto
            StringBuilder response = new StringBuilder();
            response.append("{\"type\":\"acceptRequestResponse\",\"success\":true,\"contact\":{");
            response.append("\"username\":\"").append(fromUsername).append("\",");
            response.append("\"online\":").append(fromOnline);
            if (fromUser != null && fromUser.getProfilePicture() != null) {
                response.append(",\"profilePicture\":").append(jsonString(fromUser.getProfilePicture()));
            }
            response.append("}}");
            sendMessage(response.toString());

            System.out.println("[FRIEND REQUEST] " + username + " aceptó solicitud de " + fromUsername);

            // Notificar al que envió la solicitud que fue aceptada
            WebSocketHandler senderHandler = onlineUsers.get(fromUsername);
            if (senderHandler != null) {
                StringBuilder notification = new StringBuilder();
                notification.append("{\"type\":\"friendRequestAccepted\",\"contact\":{");
                notification.append("\"username\":\"").append(username).append("\",");
                notification.append("\"online\":").append(toOnline);
                if (acceptingUser != null && acceptingUser.getProfilePicture() != null) {
                    notification.append(",\"profilePicture\":").append(jsonString(acceptingUser.getProfilePicture()));
                }
                notification.append("}}");
                senderHandler.sendMessage(notification.toString());
            }

        } catch (Exception e) {
            System.err.println("[ERROR] Error aceptando solicitud: " + e.getMessage());
            sendMessage(
                    "{\"type\":\"acceptRequestResponse\",\"success\":false,\"error\":\"Error al aceptar solicitud\"}");
        }
    }

    /**
     * Maneja rechazo de solicitud de amistad
     */
    private void handleRejectFriendRequest(String message) {
        if (username == null)
            return;

        String requestId = extractJsonValue(message, "requestId");

        try {
            // Obtener info de la solicitud
            Map<String, Object> requestData = FirebaseService.getFriendRequest(requestId);
            if (requestData == null) {
                sendMessage(
                        "{\"type\":\"rejectRequestResponse\",\"success\":false,\"error\":\"Solicitud no encontrada\"}");
                return;
            }

            String toUsername = (String) requestData.get("toUsername");

            // Verificar que el usuario actual es el receptor
            if (!toUsername.equals(username)) {
                sendMessage("{\"type\":\"rejectRequestResponse\",\"success\":false,\"error\":\"No autorizado\"}");
                return;
            }

            // Rechazar solicitud
            FirebaseService.rejectFriendRequest(requestId);

            sendMessage("{\"type\":\"rejectRequestResponse\",\"success\":true,\"requestId\":\"" + requestId + "\"}");
            System.out.println("[FRIEND REQUEST] " + username + " rechazó solicitud: " + requestId);

        } catch (Exception e) {
            System.err.println("[ERROR] Error rechazando solicitud: " + e.getMessage());
            sendMessage(
                    "{\"type\":\"rejectRequestResponse\",\"success\":false,\"error\":\"Error al rechazar solicitud\"}");
        }
    }

    /**
     * Maneja obtener solicitudes pendientes recibidas
     */
    private void handleGetPendingRequests() {
        if (username == null)
            return;

        try {
            List<Map<String, Object>> requests = FirebaseService.getPendingFriendRequests(username);

            StringBuilder json = new StringBuilder("[");
            boolean first = true;
            for (Map<String, Object> request : requests) {
                if (!first)
                    json.append(",");
                String fromUsername = (String) request.get("fromUsername");
                User fromUser = users.get(fromUsername);
                boolean online = onlineUsers.containsKey(fromUsername);

                json.append("{");
                json.append("\"id\":\"").append(request.get("id")).append("\",");
                json.append("\"fromUsername\":\"").append(fromUsername).append("\",");
                json.append("\"online\":").append(online);
                if (fromUser != null && fromUser.getProfilePicture() != null) {
                    json.append(",\"profilePicture\":").append(jsonString(fromUser.getProfilePicture()));
                }
                json.append("}");
                first = false;
            }
            json.append("]");

            sendMessage("{\"type\":\"pendingRequests\",\"requests\":" + json.toString() + "}");

        } catch (Exception e) {
            System.err.println("[ERROR] Error obteniendo solicitudes: " + e.getMessage());
            sendMessage("{\"type\":\"pendingRequests\",\"requests\":[]}");
        }
    }

    /**
     * Maneja obtener solicitudes enviadas pendientes
     */
    private void handleGetSentRequests() {
        if (username == null)
            return;

        try {
            List<Map<String, Object>> requests = FirebaseService.getSentFriendRequests(username);

            StringBuilder json = new StringBuilder("[");
            boolean first = true;
            for (Map<String, Object> request : requests) {
                if (!first)
                    json.append(",");
                String toUsername = (String) request.get("toUsername");
                User toUser = users.get(toUsername);
                boolean online = onlineUsers.containsKey(toUsername);

                json.append("{");
                json.append("\"id\":\"").append(request.get("id")).append("\",");
                json.append("\"toUsername\":\"").append(toUsername).append("\",");
                json.append("\"online\":").append(online);
                if (toUser != null && toUser.getProfilePicture() != null) {
                    json.append(",\"profilePicture\":").append(jsonString(toUser.getProfilePicture()));
                }
                json.append("}");
                first = false;
            }
            json.append("]");

            sendMessage("{\"type\":\"sentRequests\",\"requests\":" + json.toString() + "}");

        } catch (Exception e) {
            System.err.println("[ERROR] Error obteniendo solicitudes enviadas: " + e.getMessage());
            sendMessage("{\"type\":\"sentRequests\",\"requests\":[]}");
        }
    }

    /**
     * Maneja carga de historial completo (privado y grupos)
     */
    private void handleLoadConversationHistory() {
        if (username == null)
            return;

        List<Map<String, Object>> userMessages;
        try {
            userMessages = FirebaseService.getPrivateMessagesForUser(username);
        } catch (Exception e) {
            System.err.println("[ERROR] No se pudo cargar historial de conversaciones: " + e.getMessage());
            userMessages = new ArrayList<>();
        }

        String conversationsJson = userMessages.isEmpty()
                ? buildPrivateConversationsJsonFromMemory(username)
                : buildPrivateConversationsJsonFromMessageList(userMessages, username);
        String groupsJson = buildGroupConversationsJson(username);

        String payload = "{\"type\":\"conversationHistory\",\"conversations\":" + conversationsJson
                + ",\"groups\":" + groupsJson + "}";
        sendMessage(payload);
    }

    /**
     * Maneja carga de historial de conversación privada específica
     */
    private void handleLoadPrivateConversation(String message) {
        if (username == null)
            return;

        String otherUser = extractJsonValue(message, "with");
        if (otherUser.isEmpty())
            return;

        String conversationId = getConversationId(username, otherUser);
        List<Map<String, Object>> messages;
        try {
            messages = FirebaseService.getPrivateConversationBetween(username, otherUser);
        } catch (Exception e) {
            System.err.println("[ERROR] No se pudo cargar conversación: " + e.getMessage());
            messages = new ArrayList<>();
        }

        String messagesJson;
        if (messages.isEmpty()) {
            List<Message> cached = getPrivateMessagesFromMemory(username, otherUser, conversationId);
            messagesJson = cached != null ? messagesToJsonFromModel(cached) : "[]";
        } else {
            messagesJson = messagesToJson(messages);
        }

        StringBuilder payload = new StringBuilder();
        payload.append("{\"type\":\"privateConversationHistory\",");
        payload.append("\"with\":").append(jsonString(otherUser)).append(",");
        payload.append("\"messages\":").append(messagesJson);
        payload.append("}");
        sendMessage(payload.toString());
    }

    // ==================== MESSAGE HANDLERS ====================

    /**
     * Maneja mensajes privados
     */
    private void handlePrivateMessage(String message) {
        if (username == null)
            return;

        String to = extractJsonValue(message, "to");
        String content = extractJsonValue(message, "content");
        String contentType = extractJsonValue(message, "contentType");

        if (content.isEmpty()) {
            System.err.println("[ERROR] Contenido vacío en mensaje privado");
            sendMessage("{\"type\":\"error\",\"message\":\"Error en contenido del mensaje\"}");
            return;
        }

        if (contentType.equals("image")) {
            if (content.length() > MAX_IMAGE_SIZE) {
                sendMessage("{\"type\":\"error\",\"message\":\"Imagen muy grande (max 15MB)\"}");
                return;
            }
        }

        // Guardar mensaje
        String conversationId = getConversationId(username, to);
        conversations.putIfAbsent(conversationId, new ArrayList<>());
        Message msg = new Message(username, to, content, contentType);
        conversations.get(conversationId).add(msg);

        try {
            FirebaseService.saveMessage(conversationId, username, to, content, contentType, msg.getTimestamp(), null);
        } catch (Exception e) {
            System.err.println("[ERROR] No se pudo guardar mensaje en Firebase: " + e.getMessage());
        }

        // Enviar al destinatario si está online
        WebSocketHandler recipient = onlineUsers.get(to);
        if (recipient != null) {
            StringBuilder msgBuilder = new StringBuilder();
            msgBuilder.append("{\"type\":\"privateMessage\",");
            msgBuilder.append("\"from\":").append(jsonString(username)).append(",");
            msgBuilder.append("\"content\":").append(jsonString(content)).append(",");
            msgBuilder.append("\"contentType\":").append(jsonString(contentType)).append(",");
            msgBuilder.append("\"timestamp\":").append(jsonString(msg.getTimestamp())).append("}");
            recipient.sendMessage(msgBuilder.toString());
        }

        // Confirmar al remitente
        StringBuilder confirmBuilder = new StringBuilder();
        confirmBuilder.append("{\"type\":\"messageSent\",");
        confirmBuilder.append("\"to\":").append(jsonString(to)).append(",");
        confirmBuilder.append("\"content\":").append(jsonString(content)).append(",");
        confirmBuilder.append("\"contentType\":").append(jsonString(contentType)).append(",");
        confirmBuilder.append("\"timestamp\":").append(jsonString(msg.getTimestamp())).append("}");
        sendMessage(confirmBuilder.toString());

        System.out.println("[MSG] " + username + " -> " + to + " (" + contentType + ")");
    }

    /**
     * Genera ID de conversación entre dos usuarios
     */
    private String getConversationId(String user1, String user2) {
        return user1.compareTo(user2) < 0 ? user1 + "_" + user2 : user2 + "_" + user1;
    }

    private String buildPrivateConversationsJsonFromMessageList(List<Map<String, Object>> messages, String username) {
        Map<String, List<Map<String, Object>>> merged = new HashMap<>();

        for (Map<String, Object> msg : messages) {
            String otherUser = detectOtherUserFromSingleMessage(msg, username);
            if (otherUser == null || otherUser.isEmpty())
                continue;
            merged.putIfAbsent(otherUser, new ArrayList<>());
            merged.get(otherUser).add(msg);
        }

        return buildPrivateConversationsJsonFromMerged(merged);
    }

    /**
     * Construye JSON de historial de grupos del usuario
     */
    private String buildGroupConversationsJson(String username) {
        StringBuilder json = new StringBuilder("{");
        boolean firstGroup = true;

        for (Group group : groups.values()) {
            if (!group.isMember(username)) {
                continue;
            }
            List<Map<String, Object>> messages;
            try {
                messages = FirebaseService.getGroupMessages(group.getId());
            } catch (Exception e) {
                System.err.println("[ERROR] No se pudo cargar mensajes de grupo: " + e.getMessage());
                messages = new ArrayList<>();
            }

            if (!firstGroup)
                json.append(",");
            json.append("\"").append(group.getId()).append("\":");
            if (messages.isEmpty()) {
                List<Message> cached = conversations.get(group.getId());
                json.append(cached != null ? messagesToJsonFromModel(cached) : "[]");
            } else {
                json.append(messagesToJson(messages));
            }
            firstGroup = false;
        }

        json.append("}");
        return json.toString();
    }

    /**
     * Construye JSON de conversaciones privadas desde memoria
     */
    private String buildPrivateConversationsJsonFromMemory(String username) {
        Map<String, List<Message>> merged = new HashMap<>();

        for (Map.Entry<String, List<Message>> entry : conversations.entrySet()) {
            String conversationId = entry.getKey();
            if (conversationId == null || conversationId.startsWith("group_")) {
                continue;
            }

            String fallbackOtherUser = detectOtherUserFromConversationId(conversationId, username);
            List<Message> messages = entry.getValue();
            if (messages == null)
                continue;
            for (Message msg : messages) {
                String otherUser = detectOtherUserFromSingleModelMessage(msg, username);
                if ((otherUser == null || otherUser.isEmpty()) && fallbackOtherUser != null) {
                    otherUser = fallbackOtherUser;
                }
                if (otherUser == null || otherUser.isEmpty())
                    continue;
                merged.putIfAbsent(otherUser, new ArrayList<>());
                merged.get(otherUser).add(msg);
            }
        }

        for (List<Message> messages : merged.values()) {
            messages.sort(Comparator.comparing(msg -> msg.getTimestamp() == null ? "" : msg.getTimestamp()));
        }

        StringBuilder json = new StringBuilder("{");
        boolean firstConv = true;
        for (Map.Entry<String, List<Message>> entry : new TreeMap<>(merged).entrySet()) {
            if (!firstConv)
                json.append(",");
            json.append("\"").append(entry.getKey()).append("\":");
            json.append(messagesToJsonFromModel(entry.getValue()));
            firstConv = false;
        }

        json.append("}");
        return json.toString();
    }

    private String buildPrivateConversationsJsonFromMerged(Map<String, List<Map<String, Object>>> merged) {
        for (List<Map<String, Object>> messages : merged.values()) {
            messages.sort(Comparator.comparing(m -> {
                Object ts = m.get("timestamp");
                return ts == null ? "" : ts.toString();
            }));
        }

        StringBuilder json = new StringBuilder("{");
        boolean firstConv = true;
        for (Map.Entry<String, List<Map<String, Object>>> entry : new TreeMap<>(merged).entrySet()) {
            if (!firstConv)
                json.append(",");
            json.append("\"").append(entry.getKey()).append("\":");
            json.append(messagesToJson(entry.getValue()));
            firstConv = false;
        }

        json.append("}");
        return json.toString();
    }

    private String detectOtherUserFromSingleMessage(Map<String, Object> msg, String username) {
        if (msg == null)
            return null;
        Object from = msg.get("from");
        Object to = msg.get("to");
        if (from != null && from.toString().equals(username) && to != null) {
            return to.toString();
        }
        if (to != null && to.toString().equals(username) && from != null) {
            return from.toString();
        }
        return null;
    }

    private String detectOtherUserFromSingleModelMessage(Message msg, String username) {
        if (msg == null)
            return null;
        if (username.equals(msg.getFrom()) && msg.getTo() != null) {
            return msg.getTo();
        }
        if (username.equals(msg.getTo()) && msg.getFrom() != null) {
            return msg.getFrom();
        }
        return null;
    }

    private String detectOtherUserFromConversationId(String conversationId, String username) {
        if (conversationId == null || !conversationId.contains("_")) {
            return null;
        }
        String[] users = conversationId.split("_");
        if (users.length != 2) {
            return null;
        }
        if (users[0].equals(username)) {
            return users[1];
        }
        if (users[1].equals(username)) {
            return users[0];
        }
        return null;
    }

    private List<Message> getPrivateMessagesFromMemory(String username, String otherUser, String conversationId) {
        List<Message> cached = conversations.get(conversationId);
        if (cached != null && !cached.isEmpty()) {
            return cached;
        }

        List<Message> merged = new ArrayList<>();
        for (List<Message> messages : conversations.values()) {
            if (messages == null)
                continue;
            for (Message msg : messages) {
                if (msg == null)
                    continue;
                if (username.equals(msg.getFrom()) && otherUser.equals(msg.getTo())) {
                    merged.add(msg);
                } else if (username.equals(msg.getTo()) && otherUser.equals(msg.getFrom())) {
                    merged.add(msg);
                }
            }
        }

        if (merged.isEmpty()) {
            return null;
        }

        merged.sort(Comparator.comparing(m -> m.getTimestamp() == null ? "" : m.getTimestamp()));
        return merged;
    }

    /**
     * Convierte lista de mensajes (Map) a JSON
     */
    private String messagesToJson(List<Map<String, Object>> messages) {
        StringBuilder json = new StringBuilder("[");
        boolean first = true;

        for (Map<String, Object> msg : messages) {
            if (!first)
                json.append(",");
            json.append("{");

            boolean firstField = true;
            firstField = appendJsonField(json, "from", msg.get("from"), firstField);
            firstField = appendJsonField(json, "to", msg.get("to"), firstField);
            firstField = appendJsonField(json, "content", msg.get("content"), firstField);
            firstField = appendJsonField(json, "contentType", msg.get("contentType"), firstField);
            firstField = appendJsonField(json, "timestamp", msg.get("timestamp"), firstField);
            appendJsonField(json, "groupId", msg.get("groupId"), firstField);

            json.append("}");
            first = false;
        }

        json.append("]");
        return json.toString();
    }

    /**
     * Convierte lista de mensajes (modelo) a JSON
     */
    private String messagesToJsonFromModel(List<Message> messages) {
        StringBuilder json = new StringBuilder("[");
        boolean first = true;

        for (Message msg : messages) {
            if (!first)
                json.append(",");
            json.append("{");

            boolean firstField = true;
            firstField = appendJsonField(json, "from", msg.getFrom(), firstField);
            firstField = appendJsonField(json, "to", msg.getTo(), firstField);
            firstField = appendJsonField(json, "content", msg.getContent(), firstField);
            firstField = appendJsonField(json, "contentType", msg.getContentType(), firstField);
            firstField = appendJsonField(json, "timestamp", msg.getTimestamp(), firstField);
            appendJsonField(json, "groupId", msg.getGroupId(), firstField);

            json.append("}");
            first = false;
        }

        json.append("]");
        return json.toString();
    }

    /**
     * Agrega un campo JSON al StringBuilder
     */
    private boolean appendJsonField(StringBuilder json, String key, Object value, boolean firstField) {
        if (value == null)
            return firstField;
        if (!firstField)
            json.append(",");
        json.append("\"").append(key).append("\":").append(jsonString(value.toString()));
        return false;
    }

    // ==================== GROUP HANDLERS ====================

    /**
     * Maneja creación de grupos
     */
    private void handleCreateGroup(String message) {
        if (username == null)
            return;

        String groupName = extractJsonValue(message, "name");
        String membersStr = extractJsonArrayValue(message, "members");

        if (groupName.isEmpty() || membersStr.isEmpty()) {
            sendMessage("{\"type\":\"createGroupResponse\",\"success\":false,\"error\":\"Datos incompletos\"}");
            return;
        }

        List<String> members = parseJsonArray(membersStr);
        if (!members.contains(username)) {
            members.add(username);
        }

        if (members.size() < 3) {
            sendMessage("{\"type\":\"createGroupResponse\",\"success\":false,\"error\":\"Mínimo 3 personas\"}");
            return;
        }

        String groupId = "group_" + System.currentTimeMillis();
        Group group = new Group(groupId, groupName, members, username);
        groups.put(groupId, group);

        try {
            FirebaseService.saveGroup(groupId, groupName, members, username);
        } catch (Exception e) {
            System.err.println("[ERROR] No se pudo guardar grupo en Firebase: " + e.getMessage());
        }

        System.out.println("[GROUP] Grupo creado: " + groupName + " por " + username);

        // Notificar a todos los miembros
        String groupJson = String.format(
                "{\"type\":\"newGroup\",\"id\":\"%s\",\"name\":\"%s\",\"members\":%s,\"creator\":\"%s\"}",
                groupId, escapeJson(groupName), membersToJson(members), username);

        for (String member : members) {
            WebSocketHandler handler = onlineUsers.get(member);
            if (handler != null) {
                handler.sendMessage(groupJson);
            }
        }

        sendMessage("{\"type\":\"createGroupResponse\",\"success\":true,\"groupId\":\"" + groupId + "\"}");
    }

    /**
     * Maneja solicitud de lista de grupos
     */
    private void handleGetGroups() {
        if (username == null)
            return;

        List<Group> userGroups = groups.values().stream()
                .filter(g -> g.isMember(username))
                .collect(Collectors.toList());

        StringBuilder groupsJson = new StringBuilder("[");
        boolean first = true;
        for (Group g : userGroups) {
            if (!first)
                groupsJson.append(",");
            groupsJson.append("{\"id\":\"").append(g.getId())
                    .append("\",\"name\":\"").append(escapeJson(g.getName()))
                    .append("\",\"members\":").append(membersToJson(g.getMembers()))
                    .append(",\"creator\":\"").append(g.getCreator())
                    .append("\"}");
            first = false;
        }
        groupsJson.append("]");

        sendMessage("{\"type\":\"groupList\",\"groups\":" + groupsJson.toString() + "}");
    }

    /**
     * Maneja mensajes de grupo
     */
    private void handleGroupMessage(String message) {
        if (username == null)
            return;

        String groupId = extractJsonValue(message, "groupId");
        String content = extractJsonValue(message, "content");
        String contentType = extractJsonValue(message, "contentType");

        Group group = groups.get(groupId);
        if (group == null || !group.isMember(username)) {
            sendMessage("{\"type\":\"error\",\"message\":\"No perteneces a este grupo\"}");
            return;
        }

        // Guardar mensaje grupal
        conversations.putIfAbsent(groupId, new ArrayList<>());
        Message msg = new Message(username, groupId, content, contentType, true);
        conversations.get(groupId).add(msg);

        try {
            FirebaseService.addGroupMessage(groupId, username, content, contentType, msg.getTimestamp());
        } catch (Exception e) {
            System.err.println("[ERROR] No se pudo guardar mensaje de grupo: " + e.getMessage());
        }

        // Enviar a todos los miembros online
        StringBuilder msgBuilder = new StringBuilder();
        msgBuilder.append("{\"type\":\"groupMessage\",");
        msgBuilder.append("\"groupId\":").append(jsonString(groupId)).append(",");
        msgBuilder.append("\"from\":").append(jsonString(username)).append(",");
        msgBuilder.append("\"content\":").append(jsonString(content)).append(",");
        msgBuilder.append("\"contentType\":").append(jsonString(contentType)).append(",");
        msgBuilder.append("\"timestamp\":").append(jsonString(msg.getTimestamp())).append("}");
        String msgJson = msgBuilder.toString();

        for (String member : group.getMembers()) {
            WebSocketHandler handler = onlineUsers.get(member);
            if (handler != null) {
                handler.sendMessage(msgJson);
            }
        }

        System.out.println("[GROUP MSG] " + username + " -> " + group.getName());
    }

    // ==================== WEBRTC HANDLERS ====================

    /**
     * Maneja solicitud de llamada
     */
    private void handleCallRequest(String message) {
        if (username == null)
            return;

        String to = extractJsonValue(message, "to");
        String from = extractJsonValue(message, "from");

        WebSocketHandler recipient = onlineUsers.get(to);
        if (recipient != null) {
            String callMsg = String.format(
                    "{\"type\":\"callRequest\",\"from\":\"%s\",\"to\":\"%s\"}",
                    from, to);
            recipient.sendMessage(callMsg);
            System.out.println("[CALL] Llamada de " + from + " a " + to);
        }
    }

    /**
     * Maneja oferta de llamada
     */
    private void handleCallOffer(String message) {
        if (username == null)
            return;

        String to = extractJsonValue(message, "to");
        String offer = extractJsonValue(message, "offer");

        WebSocketHandler recipient = onlineUsers.get(to);
        if (recipient != null) {
            String offerMsg = String.format(
                    "{\"type\":\"callOffer\",\"from\":\"%s\",\"offer\":%s}",
                    username, offer);
            recipient.sendMessage(offerMsg);
        }
    }

    /**
     * Maneja respuesta de llamada
     */
    private void handleCallAnswer(String message) {
        if (username == null)
            return;

        String to = extractJsonValue(message, "to");
        String answer = extractJsonValue(message, "answer");

        WebSocketHandler recipient = onlineUsers.get(to);
        if (recipient != null) {
            String answerMsg = String.format(
                    "{\"type\":\"callAnswer\",\"from\":\"%s\",\"answer\":%s}",
                    username, answer);
            recipient.sendMessage(answerMsg);
        }
    }

    /**
     * Maneja candidato ICE
     */
    private void handleIceCandidate(String message) {
        if (username == null)
            return;

        String to = extractJsonValue(message, "to");
        String candidate = extractJsonValue(message, "candidate");

        WebSocketHandler recipient = onlineUsers.get(to);
        if (recipient != null) {
            String candidateMsg = String.format(
                    "{\"type\":\"iceCandidate\",\"from\":\"%s\",\"candidate\":%s}",
                    username, candidate);
            recipient.sendMessage(candidateMsg);
        }
    }

    /**
     * Maneja fin de llamada
     */
    private void handleCallEnded(String message) {
        if (username == null)
            return;

        String to = extractJsonValue(message, "to");

        WebSocketHandler recipient = onlineUsers.get(to);
        if (recipient != null) {
            String endMsg = String.format(
                    "{\"type\":\"callEnded\",\"from\":\"%s\"}",
                    username);
            recipient.sendMessage(endMsg);
            System.out.println("[CALL] Llamada terminada entre " + username + " y " + to);
        }
    }

    /**
     * Maneja rechazo de llamada
     */
    private void handleCallRejected(String message) {
        if (username == null)
            return;

        String to = extractJsonValue(message, "to");

        WebSocketHandler recipient = onlineUsers.get(to);
        if (recipient != null) {
            String rejectedMsg = String.format(
                    "{\"type\":\"callRejected\",\"from\":\"%s\"}",
                    username);
            recipient.sendMessage(rejectedMsg);
            System.out.println("[CALL] Llamada rechazada de " + username + " a " + to);
        }
    }

    // ==================== UTILITY METHODS ====================

    /**
     * Extrae un array JSON de una cadena
     */
    private String extractJsonArrayValue(String json, String key) {
        String search = "\"" + key + "\":";
        int start = json.indexOf(search);
        if (start == -1)
            return "";
        start += search.length();

        int bracketCount = 0;
        int end = start;

        for (int i = start; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '[') {
                bracketCount++;
            } else if (c == ']') {
                bracketCount--;
                if (bracketCount == 0) {
                    end = i + 1;
                    break;
                }
            }
        }

        return end > start ? json.substring(start, end) : "";
    }

    /**
     * Parsea un array JSON a lista de strings
     */
    private List<String> parseJsonArray(String jsonArray) {
        List<String> result = new ArrayList<>();
        if (jsonArray.isEmpty() || !jsonArray.startsWith("["))
            return result;

        String content = jsonArray.substring(1, jsonArray.length() - 1);
        String[] items = content.split(",");

        for (String item : items) {
            String cleaned = item.trim().replace("\"", "");
            if (!cleaned.isEmpty()) {
                result.add(cleaned);
            }
        }

        return result;
    }

    /**
     * Convierte lista de miembros a JSON
     */
    private String membersToJson(List<String> members) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < members.size(); i++) {
            if (i > 0)
                json.append(",");
            json.append("\"").append(members.get(i)).append("\"");
        }
        json.append("]");
        return json.toString();
    }

    /**
     * Broadcast del estado de usuario a todos los clientes
     */
    private void broadcastUserStatus(String user, boolean online) {
        StringBuilder statusMsg = new StringBuilder();
        statusMsg.append("{\"type\":\"userStatus\",\"username\":\"").append(user).append("\",\"online\":")
                .append(online);

        // Si se desconecta, incluir el timestamp de última conexión
        if (!online) {
            long lastSeenTimestamp = System.currentTimeMillis();
            statusMsg.append(",\"lastSeen\":").append(lastSeenTimestamp);

            // Actualizar en memoria también
            User userObj = users.get(user);
            if (userObj != null) {
                userObj.setLastSeen(lastSeenTimestamp);
            }
        }
        statusMsg.append("}");

        String msg = statusMsg.toString();
        for (WebSocketHandler client : onlineUsers.values()) {
            if (!client.username.equals(user)) {
                client.sendMessage(msg);
            }
        }
    }

    /**
     * Maneja actualización de foto de perfil
     */
    private void handleUpdateProfilePicture(String message) {
        if (username == null)
            return;

        String profilePictureUrl = extractJsonValue(message, "profilePicture");
        if (profilePictureUrl.isEmpty()) {
            sendMessage("{\"type\":\"error\",\"message\":\"URL de foto de perfil vacía\"}");
            return;
        }

        // Actualizar en memoria
        User user = users.get(username);
        if (user != null) {
            user.setProfilePicture(profilePictureUrl);
        }

        // Guardar en Firebase
        try {
            FirebaseService.updateProfilePicture(username, profilePictureUrl);
        } catch (Exception e) {
            System.err.println("[ERROR] No se pudo guardar foto de perfil en Firebase: " + e.getMessage());
        }

        // Confirmar al usuario
        sendMessage("{\"type\":\"profilePictureUpdated\",\"success\":true,\"profilePicture\":"
                + jsonString(profilePictureUrl) + "}");

        // Notificar a todos los demás usuarios
        broadcastProfilePictureUpdate(username, profilePictureUrl);

        System.out.println("[PROFILE] " + username + " actualizó su foto de perfil");
    }

    /**
     * Broadcast de actualización de foto de perfil a todos los clientes
     */
    private void broadcastProfilePictureUpdate(String user, String profilePictureUrl) {
        StringBuilder msg = new StringBuilder();
        msg.append("{\"type\":\"profilePictureUpdate\",\"username\":\"").append(user).append("\"");
        msg.append(",\"profilePicture\":").append(jsonString(profilePictureUrl)).append("}");

        for (WebSocketHandler client : onlineUsers.values()) {
            if (!client.username.equals(user)) {
                client.sendMessage(msg.toString());
            }
        }
    }

    /**
     * Maneja actualización del perfil (status y about)
     */
    private void handleUpdateProfile(String message) {
        if (username == null)
            return;

        String status = extractJsonValue(message, "status");
        String about = extractJsonValue(message, "about");

        // Actualizar en memoria
        User user = users.get(username);
        if (user != null) {
            user.setStatus(status);
            user.setAbout(about);
        }

        // Guardar en Firebase
        try {
            FirebaseService.updateUserProfile(username, status, about);
        } catch (Exception e) {
            System.err.println("[ERROR] No se pudo guardar perfil en Firebase: " + e.getMessage());
        }

        // Confirmar al usuario
        sendMessage("{\"type\":\"profileUpdated\",\"success\":true}");

        System.out.println("[PROFILE] " + username + " actualizó su perfil");
    }

    /**
     * Envía un mensaje al cliente via Jetty Session
     */
    public void sendMessage(String message) {
        try {
            if (session != null && session.isOpen()) {
                session.getRemote().sendString(message);
            }
        } catch (IOException e) {
            disconnect();
        }
    }

    /**
     * Desconecta el cliente
     */
    private void disconnect() {
        if (!connected)
            return;
        connected = false;

        if (username != null) {
            User user = users.get(username);
            if (user != null)
                user.setOnline(false);

            onlineUsers.remove(username);
            System.out.println("[-] " + username + " desconectado");

            broadcastUserStatus(username, false);
        }

        try {
            if (session != null && session.isOpen()) {
                session.close();
            }
        } catch (Exception e) {
            // Ignorar
        }
    }

    // ==================== JSON PARSING ====================

    /**
     * Extrae valor de una clave JSON
     */
    private String extractJsonValue(String json, String key) {
        String search = "\"" + key + "\":";
        int searchIndex = 0;
        int start = -1;

        while (true) {
            int idx = json.indexOf(search, searchIndex);
            if (idx == -1)
                return "";

            if (idx > 0) {
                char prevChar = json.charAt(idx - 1);
                if (prevChar == '{' || prevChar == ',' || prevChar == ' ' || prevChar == '\t' || prevChar == '\n') {
                    start = idx;
                    break;
                }
            }
            searchIndex = idx + 1;
        }

        if (start == -1)
            return "";
        start += search.length();

        while (start < json.length() && Character.isWhitespace(json.charAt(start))) {
            start++;
        }

        if (start >= json.length())
            return "";

        if (json.charAt(start) == '"') {
            start++;
            StringBuilder rawResult = new StringBuilder();
            for (int i = start; i < json.length(); i++) {
                char c = json.charAt(i);

                if (c == '\\' && i + 1 < json.length()) {
                    rawResult.append(c);
                    rawResult.append(json.charAt(i + 1));
                    i++;
                } else if (c == '"') {
                    return unescapeJson(rawResult.toString());
                } else {
                    rawResult.append(c);
                }
            }
        }

        int end = start;
        while (end < json.length()) {
            char c = json.charAt(end);
            if (c == ',' || c == '}' || c == ']')
                break;
            end++;
        }

        return end == start ? "" : json.substring(start, end).trim();
    }

    /**
     * Desescapa cadena JSON
     */
    private String unescapeJson(String s) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\' && i + 1 < s.length()) {
                char next = s.charAt(i + 1);
                switch (next) {
                    case '"':
                        result.append('"');
                        i++;
                        break;
                    case '\\':
                        result.append('\\');
                        i++;
                        break;
                    case '/':
                        result.append('/');
                        i++;
                        break;
                    case 'b':
                        result.append('\b');
                        i++;
                        break;
                    case 'f':
                        result.append('\f');
                        i++;
                        break;
                    case 'n':
                        result.append('\n');
                        i++;
                        break;
                    case 'r':
                        result.append('\r');
                        i++;
                        break;
                    case 't':
                        result.append('\t');
                        i++;
                        break;
                    case 'u':
                        if (i + 5 < s.length()) {
                            try {
                                String hex = s.substring(i + 2, i + 6);
                                result.append((char) Integer.parseInt(hex, 16));
                                i += 5;
                            } catch (NumberFormatException e) {
                                result.append(c);
                            }
                        } else {
                            result.append(c);
                        }
                        break;
                    default:
                        result.append(c);
                }
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * Escapa cadena a JSON
     */
    private String jsonString(String s) {
        if (s == null)
            return "\"\"";
        StringBuilder sb = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    if (c < 32) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        sb.append("\"");
        return sb.toString();
    }

    /**
     * Escapa cadena simple para JSON
     */
    private String escapeJson(String s) {
        if (s == null)
            return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
