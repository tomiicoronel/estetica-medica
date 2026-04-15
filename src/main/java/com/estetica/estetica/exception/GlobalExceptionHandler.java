package com.estetica.estetica.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para toda la API REST.
 *
 * <p>{@code @RestControllerAdvice} intercepta las excepciones lanzadas por cualquier controller
 * y las traduce a respuestas HTTP con el código de status y formato adecuado.
 * Sin este handler, las excepciones no capturadas devuelven un {@code 500 Internal Server Error}
 * genérico con información interna del servidor (stack trace), lo cual es inseguro e inútil para el cliente.</p>
 *
 * <h3>Excepciones manejadas:</h3>
 * <table>
 *     <tr><th>Excepción</th><th>Status HTTP</th><th>Cuándo ocurre</th></tr>
 *     <tr><td>{@code EntityNotFoundException}</td><td>404 Not Found</td><td>Buscar/actualizar/eliminar con un ID inexistente</td></tr>
 *     <tr><td>{@code IllegalArgumentException}</td><td>400 Bad Request</td><td>Email duplicado u otras reglas de negocio</td></tr>
 *     <tr><td>{@code MethodArgumentNotValidException}</td><td>400 Bad Request</td><td>Falla de validación en {@code @Valid} (campos vacíos, formato inválido, etc.)</td></tr>
 * </table>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-14
 * @see com.estetica.estetica.controller.ProfesionalController
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Captura {@link EntityNotFoundException} y devuelve un {@code 404 Not Found}.
     *
     * <p>Se lanza cuando se intenta acceder a una profesional con un UUID que no existe en la base de datos.</p>
     *
     * @param ex la excepción capturada con el mensaje de error
     * @return respuesta HTTP 404 con timestamp, status, tipo de error y mensaje descriptivo
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFound(EntityNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 404);
        body.put("error", "No encontrado");
        body.put("mensaje", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /**
     * Captura {@link IllegalArgumentException} y devuelve un {@code 400 Bad Request}.
     *
     * <p>Se lanza cuando se viola una regla de negocio, como intentar registrar
     * una profesional con un email que ya existe.</p>
     *
     * @param ex la excepción capturada con el mensaje de error
     * @return respuesta HTTP 400 con timestamp, status, tipo de error y mensaje descriptivo
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 400);
        body.put("error", "Solicitud inválida");
        body.put("mensaje", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Captura {@link MethodArgumentNotValidException} y devuelve un {@code 400 Bad Request}
     * con el detalle de cada campo que falló la validación.
     *
     * <p>Se lanza automáticamente por Spring cuando un request body anotado con {@code @Valid}
     * no cumple las restricciones definidas en el DTO ({@code @NotBlank}, {@code @Email}, {@code @Size}, etc.).</p>
     *
     * <p>El response incluye un mapa {@code "mensajes"} donde la clave es el nombre del campo
     * y el valor es el mensaje de error definido en la anotación de validación.</p>
     *
     * @param ex la excepción capturada que contiene los errores de validación
     * @return respuesta HTTP 400 con timestamp, status, tipo de error y mapa de errores por campo
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                errores.put(fieldError.getField(), fieldError.getDefaultMessage())
        );

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 400);
        body.put("error", "Error de validación");
        body.put("mensajes", errores);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
