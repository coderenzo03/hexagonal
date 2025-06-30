package com.utp.hexagonal.Infraestructura.Controller;

import com.utp.hexagonal.Dominio.excepciones.StockInsuficienteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<Object> handleStockInsuficienteException(StockInsuficienteException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value()); // Código de estado HTTP
        body.put("error", "Bad Request"); // Tipo de error
        body.put("message", ex.getMessage()); // Mensaje detallado de la excepción
        body.put("path", request.getDescription(false).replace("uri=", "")); // URL de la solicitud

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST); // Retorna 400 Bad Request
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", "Ocurrió un error inesperado en el servidor."); // Mensaje genérico por seguridad
        body.put("path", request.getDescription(false).replace("uri=", ""));

        // Imprime la traza de la pila en la consola del servidor para depuración
        ex.printStackTrace();

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR); // Retorna 500 Internal Server Error
    }
}
