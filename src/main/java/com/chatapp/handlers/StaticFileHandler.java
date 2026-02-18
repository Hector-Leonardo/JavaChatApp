package com.chatapp.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Manejador para servir archivos estáticos (HTML, CSS, JS)
 * Sirve archivos desde el directorio public/
 */
public class StaticFileHandler implements HttpHandler {

    private static final String PUBLIC_DIR = "public";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        // Redirigir raíz a index.html
        if (path.equals("/")) {
            path = "/index.html";
        }

        // Prevenir acceso fuera del directorio public (path traversal)
        File publicDir = new File(PUBLIC_DIR).getCanonicalFile();
        File file = new File(PUBLIC_DIR + path).getCanonicalFile();

        if (!file.getPath().startsWith(publicDir.getPath())) {
            String response = "403 Forbidden";
            exchange.sendResponseHeaders(403, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
            return;
        }

        // Validar que el archivo existe y es un archivo regular
        if (file.exists() && file.isFile()) {
            String contentType = getContentType(path);
            byte[] bytes = Files.readAllBytes(file.toPath());

            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.sendResponseHeaders(200, bytes.length);
            exchange.getResponseBody().write(bytes);
        } else {
            String response = "404 Not Found";
            exchange.sendResponseHeaders(404, response.length());
            exchange.getResponseBody().write(response.getBytes());
        }

        exchange.getResponseBody().close();
    }

    /**
     * Determina el tipo de contenido basado en la extensión del archivo
     */
    private String getContentType(String path) {
        if (path.endsWith(".html")) {
            return "text/html; charset=UTF-8";
        }
        if (path.endsWith(".css")) {
            return "text/css";
        }
        if (path.endsWith(".js")) {
            return "application/javascript";
        }
        if (path.endsWith(".png")) {
            return "image/png";
        }
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        if (path.endsWith(".gif")) {
            return "image/gif";
        }
        if (path.endsWith(".json")) {
            return "application/json";
        }
        return "text/plain";
    }
}
