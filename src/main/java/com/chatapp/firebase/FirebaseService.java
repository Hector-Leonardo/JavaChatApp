package com.chatapp.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Servicio para comunicación con Firebase Firestore
 * Maneja todas las operaciones CRUD con la base de datos
 */
public class FirebaseService {
    private static Firestore db;
    private static final String USERS_COLLECTION = "users";
    private static final String GROUPS_COLLECTION = "groups";
    private static final String CONVERSATIONS_COLLECTION = "conversations";

    /**
     * Inicializa Firebase Admin SDK y Firestore
     * Primero intenta desde variable de entorno FIREBASE_CREDENTIALS
     * (producción/Render)
     * Si no existe, usa serviceAccountKey.json (desarrollo local)
     */
    public static void initialize() {
        try {
            InputStream credentialsStream = null;

            // Opción 1: Variable de entorno (para Render / producción)
            String envCredentials = System.getenv("FIREBASE_CREDENTIALS");
            if (envCredentials != null && !envCredentials.isEmpty()) {
                System.out
                        .println("[FIREBASE] Cargando credenciales desde variable de entorno FIREBASE_CREDENTIALS...");
                credentialsStream = new ByteArrayInputStream(envCredentials.getBytes(StandardCharsets.UTF_8));
                System.out.println("[FIREBASE] ✓ Credenciales leídas desde variable de entorno");
            } else {
                // Opción 2: Archivo local (para desarrollo)
                System.out.println("[FIREBASE] Variable FIREBASE_CREDENTIALS no encontrada, buscando archivo...");
                File keyFile = new File("serviceAccountKey.json");
                if (!keyFile.exists()) {
                    System.err.println("[FIREBASE ERROR] ❌ No hay credenciales disponibles");
                    System.err.println(
                            "[FIREBASE ERROR] Configure FIREBASE_CREDENTIALS o coloque serviceAccountKey.json");
                    return;
                }
                System.out.println("[FIREBASE] ✓ Archivo encontrado: " + keyFile.getAbsolutePath());
                credentialsStream = new FileInputStream(keyFile);
            }

            System.out.println("[FIREBASE] Leyendo credenciales...");

            GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);
            System.out.println("[FIREBASE] ✓ Credenciales cargadas correctamente");

            @SuppressWarnings("deprecation")
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(credentials)
                    .build();

            System.out.println("[FIREBASE] Inicializando Firebase App...");
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("[FIREBASE] ✓ Firebase App inicializado");
            } else {
                System.out.println("[FIREBASE] ✓ Firebase App ya estaba inicializado");
            }

            db = FirestoreClient.getFirestore();
            System.out.println("[FIREBASE] ✅ Conexión establecida con Firestore");
            System.out.println("[FIREBASE] Estado: Listo para guardar datos");
        } catch (FileNotFoundException e) {
            System.err.println("[FIREBASE ERROR] ❌ Archivo NO encontrado: serviceAccountKey.json");
            System.err.println("[FIREBASE ERROR] Ruta actual: " + new File(".").getAbsolutePath());
            System.err.println("[FIREBASE ERROR] Detalle: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[FIREBASE ERROR] ❌ Error durante inicialización: " + e.getClass().getSimpleName());
            System.err.println("[FIREBASE ERROR] Mensaje: " + e.getMessage());
            System.err.println("[FIREBASE ERROR] Asegúrate de que:");
            System.err.println("  1. serviceAccountKey.json existe en la raíz del proyecto");
            System.err.println("  2. El JSON contiene credenciales válidas de Firebase");
            System.err.println("  3. Tienes conexión a internet para conectar a Firestore");
            e.printStackTrace();
        }
    }

    // =============== USERS ===============

    /**
     * Guarda un usuario en Firestore
     */
    public static void saveUser(String username, String password) throws ExecutionException, InterruptedException {
        if (db == null) {
            System.err.println("[FIREBASE ERROR] ❌ DB es NULL - No se pudo guardar usuario: " + username);
            System.err.println("[FIREBASE ERROR] Firestore NO está inicializado correctamente");
            return;
        }

        Map<String, Object> userData = new HashMap<>();
        userData.put("username", username);
        userData.put("password", password);
        userData.put("online", false);
        userData.put("createdAt", System.currentTimeMillis());

        db.collection(USERS_COLLECTION).document(username).set(userData).get();
        System.out.println("[FIREBASE] ✓ Usuario guardado: " + username);
    }

    /**
     * Obtiene un usuario específico
     */
    public static Map<String, Object> getUser(String username) throws ExecutionException, InterruptedException {
        if (db == null)
            return null;

        DocumentSnapshot doc = db.collection(USERS_COLLECTION).document(username).get().get();
        return doc.exists() ? doc.getData() : null;
    }

    /**
     * Obtiene todos los usuarios
     */
    public static Map<String, Map<String, Object>> getAllUsers() throws ExecutionException, InterruptedException {
        if (db == null)
            return new HashMap<>();

        Map<String, Map<String, Object>> users = new HashMap<>();
        QuerySnapshot querySnapshot = db.collection(USERS_COLLECTION).get().get();

        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
            users.put(doc.getId(), doc.getData());
        }

        return users;
    }

    /**
     * Actualiza el estado en línea de un usuario
     */
    public static void updateUserStatus(String username, boolean online)
            throws ExecutionException, InterruptedException {
        if (db == null)
            return;

        if (online) {
            db.collection(USERS_COLLECTION).document(username).update("online", online).get();
        } else {
            // Cuando se desconecta, guardar timestamp de última conexión
            java.util.Map<String, Object> updates = new java.util.HashMap<>();
            updates.put("online", false);
            updates.put("lastSeen", System.currentTimeMillis());
            db.collection(USERS_COLLECTION).document(username).update(updates).get();
        }
    }

    /**
     * Actualiza la foto de perfil de un usuario
     */
    public static void updateProfilePicture(String username, String profilePictureUrl)
            throws ExecutionException, InterruptedException {
        if (db == null)
            return;

        db.collection(USERS_COLLECTION).document(username).update("profilePicture", profilePictureUrl).get();
        System.out.println("[FIREBASE] ✓ Foto de perfil actualizada para: " + username);
    }

    /**
     * Actualiza el perfil de un usuario (status y about)
     */
    public static void updateUserProfile(String username, String status, String about)
            throws ExecutionException, InterruptedException {
        if (db == null)
            return;

        Map<String, Object> updates = new HashMap<>();
        updates.put("status", status);
        updates.put("about", about);

        db.collection(USERS_COLLECTION).document(username).update(updates).get();
        System.out.println("[FIREBASE] ✓ Perfil actualizado para: " + username);
    }

    // =============== MESSAGES ===============

    /**
     * Guarda un mensaje en una conversación
     */
    public static void saveMessage(String conversationId, String from, String to, String content,
            String contentType, String timestamp, String groupId)
            throws ExecutionException, InterruptedException {
        if (db == null) {
            System.err.println("[FIREBASE ERROR] ❌ DB es NULL - No se pudo guardar mensaje en: " + conversationId);
            return;
        }

        Map<String, Object> conversationData = new HashMap<>();
        conversationData.put("updatedAt", System.currentTimeMillis());
        if (to != null && !to.isEmpty()) {
            conversationData.put("participants", Arrays.asList(from, to));
        }

        db.collection(CONVERSATIONS_COLLECTION)
                .document(conversationId)
                .set(conversationData, SetOptions.merge())
                .get();

        Map<String, Object> messageData = new HashMap<>();
        messageData.put("from", from);
        messageData.put("to", to);
        messageData.put("content", content);
        messageData.put("contentType", contentType);
        messageData.put("timestamp", timestamp);
        messageData.put("read", false);

        if (groupId != null) {
            messageData.put("groupId", groupId);
        }

        db.collection(CONVERSATIONS_COLLECTION).document(conversationId)
                .collection("messages").add(messageData).get();

        System.out.println("[FIREBASE] ✓ Mensaje guardado en conversación: " + conversationId);
    }

    /**
     * Obtiene todos los mensajes de una conversación
     */
    public static List<Map<String, Object>> getConversationMessages(String conversationId)
            throws ExecutionException, InterruptedException {
        if (db == null)
            return new ArrayList<>();

        List<Map<String, Object>> messages = new ArrayList<>();
        QuerySnapshot querySnapshot = db.collection(CONVERSATIONS_COLLECTION)
                .document(conversationId).collection("messages")
                .orderBy("timestamp")
                .get().get();

        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
            messages.add(doc.getData());
        }

        return messages;
    }

    /**
     * Elimina una conversación y todos sus mensajes
     */
    public static void deleteConversation(String conversationId)
            throws ExecutionException, InterruptedException {
        if (db == null)
            return;

        // Primero eliminar todos los mensajes de la subcolección
        QuerySnapshot messagesSnapshot = db.collection(CONVERSATIONS_COLLECTION)
                .document(conversationId)
                .collection("messages")
                .get().get();

        for (DocumentSnapshot doc : messagesSnapshot.getDocuments()) {
            doc.getReference().delete().get();
        }

        // Luego eliminar el documento de la conversación
        db.collection(CONVERSATIONS_COLLECTION).document(conversationId).delete().get();
        System.out.println("[FIREBASE] ✓ Conversación eliminada: " + conversationId);
    }

    /**
     * Elimina un contacto bidireccionalmente y su conversación
     */
    public static void deleteContactAndConversation(String user1, String user2)
            throws ExecutionException, InterruptedException {
        // Eliminar contacto en ambas direcciones
        removeContact(user1, user2);
        removeContact(user2, user1);

        // Generar ID de conversación (mismo formato que WebSocketHandler)
        String conversationId = user1.compareTo(user2) < 0 ? user1 + "_" + user2 : user2 + "_" + user1;

        // Eliminar la conversación
        deleteConversation(conversationId);

        System.out.println("[FIREBASE] ✓ Contacto y conversación eliminados entre: " + user1 + " y " + user2);
    }

    /**
     * Obtiene todas las conversaciones
     */
    public static Map<String, List<Map<String, Object>>> getAllConversations()
            throws ExecutionException, InterruptedException {
        if (db == null)
            return new HashMap<>();

        Map<String, List<Map<String, Object>>> conversations = new HashMap<>();

        QuerySnapshot querySnapshot = db.collectionGroup("messages").get().get();
        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
            DocumentReference messageRef = doc.getReference();
            DocumentReference parentDoc = messageRef.getParent().getParent();
            if (parentDoc == null || parentDoc.getParent() == null) {
                continue;
            }

            String parentCollection = parentDoc.getParent().getId();
            if (!CONVERSATIONS_COLLECTION.equals(parentCollection)) {
                continue;
            }

            String conversationId = parentDoc.getId();
            conversations.putIfAbsent(conversationId, new ArrayList<>());
            conversations.get(conversationId).add(doc.getData());
        }

        for (List<Map<String, Object>> messages : conversations.values()) {
            messages.sort(Comparator.comparing(m -> {
                Object ts = m.get("timestamp");
                return ts == null ? "" : ts.toString();
            }));
        }

        return conversations;
    }

    /**
     * Obtiene todos los mensajes privados de un usuario
     */
    public static List<Map<String, Object>> getPrivateMessagesForUser(String username)
            throws ExecutionException, InterruptedException {
        if (db == null)
            return new ArrayList<>();

        List<Map<String, Object>> messages = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        QuerySnapshot fromSnapshot = db.collectionGroup("messages")
                .whereEqualTo("from", username)
                .get().get();

        addConversationMessages(fromSnapshot, messages, seen);

        QuerySnapshot toSnapshot = db.collectionGroup("messages")
                .whereEqualTo("to", username)
                .get().get();

        addConversationMessages(toSnapshot, messages, seen);

        messages.sort(Comparator.comparing(m -> {
            Object ts = m.get("timestamp");
            return ts == null ? "" : ts.toString();
        }));

        return messages;
    }

    /**
     * Obtiene los mensajes privados entre dos usuarios
     */
    public static List<Map<String, Object>> getPrivateConversationBetween(String user1, String user2)
            throws ExecutionException, InterruptedException {
        if (db == null)
            return new ArrayList<>();

        List<Map<String, Object>> messages = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        QuerySnapshot aToB = db.collectionGroup("messages")
                .whereEqualTo("from", user1)
                .whereEqualTo("to", user2)
                .get().get();

        addConversationMessages(aToB, messages, seen);

        QuerySnapshot bToA = db.collectionGroup("messages")
                .whereEqualTo("from", user2)
                .whereEqualTo("to", user1)
                .get().get();

        addConversationMessages(bToA, messages, seen);

        messages.sort(Comparator.comparing(m -> {
            Object ts = m.get("timestamp");
            return ts == null ? "" : ts.toString();
        }));

        return messages;
    }

    private static void addConversationMessages(QuerySnapshot snapshot, List<Map<String, Object>> target,
            Set<String> seen) {
        for (DocumentSnapshot doc : snapshot.getDocuments()) {
            if (!isConversationMessage(doc)) {
                continue;
            }
            String key = doc.getReference().getPath();
            if (seen.add(key)) {
                target.add(doc.getData());
            }
        }
    }

    private static boolean isConversationMessage(DocumentSnapshot doc) {
        DocumentReference messageRef = doc.getReference();
        DocumentReference parentDoc = messageRef.getParent().getParent();
        if (parentDoc == null || parentDoc.getParent() == null) {
            return false;
        }
        return CONVERSATIONS_COLLECTION.equals(parentDoc.getParent().getId());
    }

    // =============== GROUPS ===============

    /**
     * Guarda un grupo en Firestore
     */
    public static void saveGroup(String groupId, String name, List<String> members, String creator)
            throws ExecutionException, InterruptedException {
        if (db == null) {
            System.err.println("[FIREBASE ERROR] ❌ DB es NULL - No se pudo guardar grupo: " + name);
            return;
        }

        Map<String, Object> groupData = new HashMap<>();
        groupData.put("id", groupId);
        groupData.put("name", name);
        groupData.put("members", members);
        groupData.put("creator", creator);
        groupData.put("timestamp", System.currentTimeMillis());

        db.collection(GROUPS_COLLECTION).document(groupId).set(groupData).get();
        System.out.println("[FIREBASE] ✓ Grupo guardado: " + name);
    }

    /**
     * Obtiene un grupo específico
     */
    public static Map<String, Object> getGroup(String groupId) throws ExecutionException, InterruptedException {
        if (db == null)
            return null;

        DocumentSnapshot doc = db.collection(GROUPS_COLLECTION).document(groupId).get().get();
        return doc.exists() ? doc.getData() : null;
    }

    /**
     * Obtiene todos los grupos
     */
    public static Map<String, Map<String, Object>> getAllGroups() throws ExecutionException, InterruptedException {
        if (db == null)
            return new HashMap<>();

        Map<String, Map<String, Object>> groups = new HashMap<>();
        QuerySnapshot querySnapshot = db.collection(GROUPS_COLLECTION).get().get();

        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
            groups.put(doc.getId(), doc.getData());
        }

        return groups;
    }

    /**
     * Agrega un mensaje a un grupo
     */
    public static void addGroupMessage(String groupId, String from, String content,
            String contentType, String timestamp)
            throws ExecutionException, InterruptedException {
        if (db == null)
            return;

        Map<String, Object> messageData = new HashMap<>();
        messageData.put("from", from);
        messageData.put("content", content);
        messageData.put("contentType", contentType);
        messageData.put("timestamp", timestamp);

        db.collection(GROUPS_COLLECTION).document(groupId)
                .collection("messages").add(messageData).get();
    }

    /**
     * Obtiene todos los mensajes de un grupo
     */
    public static List<Map<String, Object>> getGroupMessages(String groupId)
            throws ExecutionException, InterruptedException {
        if (db == null)
            return new ArrayList<>();

        List<Map<String, Object>> messages = new ArrayList<>();
        QuerySnapshot querySnapshot = db.collection(GROUPS_COLLECTION).document(groupId)
                .collection("messages").orderBy("timestamp").get().get();

        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
            messages.add(doc.getData());
        }

        return messages;
    }

    // =============== CONTACTS ===============
    private static final String CONTACTS_COLLECTION = "contacts";

    /**
     * Agrega un contacto para un usuario
     */
    public static void addContact(String ownerUsername, String contactUsername)
            throws ExecutionException, InterruptedException {
        if (db == null) {
            System.err.println("[FIREBASE ERROR] ❌ DB es NULL - No se pudo agregar contacto");
            return;
        }

        // Document ID único para la relación
        String docId = ownerUsername + "_" + contactUsername;

        Map<String, Object> contactData = new HashMap<>();
        contactData.put("ownerUsername", ownerUsername);
        contactData.put("contactUsername", contactUsername);
        contactData.put("addedAt", System.currentTimeMillis());

        db.collection(CONTACTS_COLLECTION).document(docId).set(contactData).get();
        System.out.println("[FIREBASE] ✓ Contacto agregado: " + ownerUsername + " -> " + contactUsername);
    }

    /**
     * Obtiene todos los contactos de un usuario
     */
    public static List<String> getContacts(String ownerUsername)
            throws ExecutionException, InterruptedException {
        if (db == null)
            return new ArrayList<>();

        List<String> contacts = new ArrayList<>();
        QuerySnapshot querySnapshot = db.collection(CONTACTS_COLLECTION)
                .whereEqualTo("ownerUsername", ownerUsername)
                .get().get();

        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
            String contactUsername = (String) doc.get("contactUsername");
            if (contactUsername != null) {
                contacts.add(contactUsername);
            }
        }

        return contacts;
    }

    /**
     * Verifica si un usuario existe
     */
    public static boolean userExists(String username) throws ExecutionException, InterruptedException {
        if (db == null)
            return false;

        DocumentSnapshot doc = db.collection(USERS_COLLECTION).document(username).get().get();
        return doc.exists();
    }

    /**
     * Verifica si un contacto ya existe
     */
    public static boolean contactExists(String ownerUsername, String contactUsername)
            throws ExecutionException, InterruptedException {
        if (db == null)
            return false;

        String docId = ownerUsername + "_" + contactUsername;
        DocumentSnapshot doc = db.collection(CONTACTS_COLLECTION).document(docId).get().get();
        return doc.exists();
    }

    /**
     * Elimina un contacto
     */
    public static void removeContact(String ownerUsername, String contactUsername)
            throws ExecutionException, InterruptedException {
        if (db == null)
            return;

        String docId = ownerUsername + "_" + contactUsername;
        db.collection(CONTACTS_COLLECTION).document(docId).delete().get();
        System.out.println("[FIREBASE] ✓ Contacto eliminado: " + ownerUsername + " -> " + contactUsername);
    }

    // =============== FRIEND REQUESTS ===============
    private static final String FRIEND_REQUESTS_COLLECTION = "friend_requests";

    /**
     * Crea una solicitud de amistad
     */
    public static String createFriendRequest(String fromUsername, String toUsername)
            throws ExecutionException, InterruptedException {
        if (db == null) {
            System.err.println("[FIREBASE ERROR] ❌ DB es NULL - No se pudo crear solicitud");
            return null;
        }

        String requestId = fromUsername + "_to_" + toUsername;

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("id", requestId);
        requestData.put("fromUsername", fromUsername);
        requestData.put("toUsername", toUsername);
        requestData.put("status", "pending");
        requestData.put("createdAt", System.currentTimeMillis());

        db.collection(FRIEND_REQUESTS_COLLECTION).document(requestId).set(requestData).get();
        System.out.println("[FIREBASE] ✓ Solicitud de amistad creada: " + fromUsername + " -> " + toUsername);
        return requestId;
    }

    /**
     * Verifica si ya existe una solicitud pendiente entre dos usuarios
     */
    public static boolean friendRequestExists(String fromUsername, String toUsername)
            throws ExecutionException, InterruptedException {
        if (db == null)
            return false;

        // Verificar en ambas direcciones
        String requestId1 = fromUsername + "_to_" + toUsername;
        String requestId2 = toUsername + "_to_" + fromUsername;

        DocumentSnapshot doc1 = db.collection(FRIEND_REQUESTS_COLLECTION).document(requestId1).get().get();
        DocumentSnapshot doc2 = db.collection(FRIEND_REQUESTS_COLLECTION).document(requestId2).get().get();

        if (doc1.exists()) {
            String status = (String) doc1.get("status");
            if ("pending".equals(status))
                return true;
        }
        if (doc2.exists()) {
            String status = (String) doc2.get("status");
            if ("pending".equals(status))
                return true;
        }

        return false;
    }

    /**
     * Obtiene todas las solicitudes pendientes recibidas por un usuario
     */
    public static List<Map<String, Object>> getPendingFriendRequests(String username)
            throws ExecutionException, InterruptedException {
        if (db == null)
            return new ArrayList<>();

        List<Map<String, Object>> requests = new ArrayList<>();
        QuerySnapshot querySnapshot = db.collection(FRIEND_REQUESTS_COLLECTION)
                .whereEqualTo("toUsername", username)
                .whereEqualTo("status", "pending")
                .get().get();

        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
            requests.add(doc.getData());
        }

        return requests;
    }

    /**
     * Obtiene todas las solicitudes enviadas pendientes por un usuario
     */
    public static List<Map<String, Object>> getSentFriendRequests(String username)
            throws ExecutionException, InterruptedException {
        if (db == null)
            return new ArrayList<>();

        List<Map<String, Object>> requests = new ArrayList<>();
        QuerySnapshot querySnapshot = db.collection(FRIEND_REQUESTS_COLLECTION)
                .whereEqualTo("fromUsername", username)
                .whereEqualTo("status", "pending")
                .get().get();

        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
            requests.add(doc.getData());
        }

        return requests;
    }

    /**
     * Acepta una solicitud de amistad (agrega contacto mutuo)
     */
    public static void acceptFriendRequest(String requestId)
            throws ExecutionException, InterruptedException {
        if (db == null)
            return;

        // Obtener la solicitud
        DocumentSnapshot doc = db.collection(FRIEND_REQUESTS_COLLECTION).document(requestId).get().get();
        if (!doc.exists()) {
            System.err.println("[FIREBASE ERROR] Solicitud no encontrada: " + requestId);
            return;
        }

        String fromUsername = (String) doc.get("fromUsername");
        String toUsername = (String) doc.get("toUsername");

        // Actualizar estado de la solicitud
        db.collection(FRIEND_REQUESTS_COLLECTION).document(requestId)
                .update("status", "accepted").get();

        // Agregar contacto mutuo (ambos se agregan mutuamente)
        addContact(fromUsername, toUsername);
        addContact(toUsername, fromUsername);

        System.out.println("[FIREBASE] ✓ Solicitud aceptada: " + fromUsername + " <-> " + toUsername);
    }

    /**
     * Rechaza una solicitud de amistad
     */
    public static void rejectFriendRequest(String requestId)
            throws ExecutionException, InterruptedException {
        if (db == null)
            return;

        db.collection(FRIEND_REQUESTS_COLLECTION).document(requestId)
                .update("status", "rejected").get();
        System.out.println("[FIREBASE] ✓ Solicitud rechazada: " + requestId);
    }

    /**
     * Elimina una solicitud de amistad
     */
    public static void deleteFriendRequest(String requestId)
            throws ExecutionException, InterruptedException {
        if (db == null)
            return;

        db.collection(FRIEND_REQUESTS_COLLECTION).document(requestId).delete().get();
    }

    /**
     * Obtiene información de una solicitud específica
     */
    public static Map<String, Object> getFriendRequest(String requestId)
            throws ExecutionException, InterruptedException {
        if (db == null)
            return null;

        DocumentSnapshot doc = db.collection(FRIEND_REQUESTS_COLLECTION).document(requestId).get().get();
        return doc.exists() ? doc.getData() : null;
    }
}
