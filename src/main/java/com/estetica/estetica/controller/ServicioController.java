package com.estetica.estetica.controller;

import com.estetica.estetica.dto.request.ServicioRequest;
import com.estetica.estetica.dto.response.ServicioResponse;
import com.estetica.estetica.service.ServicioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Controlador REST que expone los endpoints para gestionar servicios de estética.
 *
 * <p>Los endpoints están organizados en dos niveles:</p>
 * <ul>
 *     <li><b>Anidados bajo profesional</b> ({@code /api/profesionales/{profesionalId}/servicios}) —
 *         para operaciones que dependen del contexto de una profesional (crear, listar).</li>
 *     <li><b>Directos</b> ({@code /api/servicios/{id}}) — para operaciones sobre un servicio
 *         específico que ya tiene su propio UUID (buscar, actualizar, activar/desactivar).</li>
 * </ul>
 *
 * <p>Este diseño de URLs sigue el estándar REST para recursos anidados: los servicios
 * son un sub-recurso de profesionales, y la URL refleja esa relación.</p>
 *
 * <h3>Endpoints disponibles:</h3>
 * <table>
 *     <tr><th>Método</th><th>Path</th><th>Acción</th><th>Status</th></tr>
 *     <tr><td>POST</td><td>/api/profesionales/{profesionalId}/servicios</td><td>Crear servicio</td><td>201 Created</td></tr>
 *     <tr><td>GET</td><td>/api/profesionales/{profesionalId}/servicios</td><td>Listar servicios de una profesional</td><td>200 OK</td></tr>
 *     <tr><td>GET</td><td>/api/profesionales/{profesionalId}/servicios/activos</td><td>Listar solo activos</td><td>200 OK</td></tr>
 *     <tr><td>GET</td><td>/api/servicios/{id}</td><td>Buscar servicio por ID</td><td>200 OK</td></tr>
 *     <tr><td>PUT</td><td>/api/servicios/{id}</td><td>Actualizar servicio</td><td>200 OK</td></tr>
 *     <tr><td>PATCH</td><td>/api/servicios/{id}/estado</td><td>Activar/desactivar</td><td>200 OK</td></tr>
 *     <tr><td>PATCH</td><td>/api/servicios/{id}/precio</td><td>Actualizar precio</td><td>200 OK</td></tr>
 * </table>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-15
 * @see ServicioService
 * @see com.estetica.estetica.exception.GlobalExceptionHandler
 */
@RestController
@RequiredArgsConstructor
public class ServicioController {

    /** Servicio inyectado por constructor que contiene la lógica de negocio de servicios. */
    private final ServicioService servicioService;

    // ---- Endpoints anidados bajo profesional ----

    /**
     * Crea un nuevo servicio vinculado a una profesional.
     *
     * <p>El {@code profesionalId} se extrae del path y se inyecta automáticamente
     * en el request body antes de delegar al service. Así el cliente no necesita
     * repetir el ID en el body si ya lo puso en la URL.</p>
     *
     * @param profesionalId el UUID de la profesional (extraído del path)
     * @param request       los datos del servicio a crear (body JSON validado)
     * @return {@code 201 Created} con el {@link ServicioResponse} del servicio creado
     */
    @PostMapping("/api/profesionales/{profesionalId}/servicios")
    public ResponseEntity<ServicioResponse> crear(
            @PathVariable UUID profesionalId,
            @Valid @RequestBody ServicioRequest request) {
        request.setProfesionalId(profesionalId);
        ServicioResponse response = servicioService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Lista todos los servicios (activos e inactivos) de una profesional.
     *
     * @param profesionalId el UUID de la profesional (extraído del path)
     * @return {@code 200 OK} con la lista de {@link ServicioResponse}
     */
    @GetMapping("/api/profesionales/{profesionalId}/servicios")
    public ResponseEntity<List<ServicioResponse>> listarPorProfesional(
            @PathVariable UUID profesionalId) {
        List<ServicioResponse> response = servicioService.listarPorProfesional(profesionalId);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista solo los servicios activos de una profesional.
     *
     * <p>Útil para el cliente final que necesita ver solo los servicios disponibles
     * para agendar un turno.</p>
     *
     * @param profesionalId el UUID de la profesional (extraído del path)
     * @return {@code 200 OK} con la lista de {@link ServicioResponse} activos
     */
    @GetMapping("/api/profesionales/{profesionalId}/servicios/activos")
    public ResponseEntity<List<ServicioResponse>> listarActivosPorProfesional(
            @PathVariable UUID profesionalId) {
        List<ServicioResponse> response = servicioService.listarActivosPorProfesional(profesionalId);
        return ResponseEntity.ok(response);
    }

    // ---- Endpoints directos sobre servicio ----

    /**
     * Busca un servicio por su UUID.
     *
     * <p>No necesita el {@code profesionalId} en el path porque el servicio
     * ya tiene su propio identificador único.</p>
     *
     * @param id el UUID del servicio (extraído del path)
     * @return {@code 200 OK} con el {@link ServicioResponse} del servicio encontrado
     */
    @GetMapping("/api/servicios/{id}")
    public ResponseEntity<ServicioResponse> buscarPorId(@PathVariable UUID id) {
        ServicioResponse response = servicioService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza los datos de un servicio existente.
     *
     * <p>Permite cambiar nombre, descripción, precio e incluso reasignar el servicio
     * a otra profesional (enviando un {@code profesionalId} diferente en el body).</p>
     *
     * @param id      el UUID del servicio a actualizar (extraído del path)
     * @param request los nuevos datos del servicio (body JSON validado)
     * @return {@code 200 OK} con el {@link ServicioResponse} actualizado
     */
    @PutMapping("/api/servicios/{id}")
    public ResponseEntity<ServicioResponse> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody ServicioRequest request) {
        ServicioResponse response = servicioService.actualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Activa o desactiva un servicio (soft delete).
     *
     * <p>Se usa {@code PATCH} en lugar de {@code PUT} porque solo se modifica un campo
     * ({@code activo}), no el recurso completo. El parámetro {@code activo} se envía
     * como query parameter: {@code PATCH /api/servicios/{id}/estado?activo=false}.</p>
     *
     * @param id     el UUID del servicio a activar/desactivar (extraído del path)
     * @param activo {@code true} para activar, {@code false} para desactivar (query param)
     * @return {@code 200 OK} con un mensaje confirmando el cambio de estado
     */
    @PatchMapping("/api/servicios/{id}/estado")
    public ResponseEntity<String> cambiarEstado(
            @PathVariable UUID id,
            @RequestParam Boolean activo) {
        servicioService.cambiarEstado(id, activo);
        String estado = activo ? "activado" : "desactivado";
        return ResponseEntity.ok("Servicio " + estado + " correctamente");
    }

    /**
     * Actualiza únicamente el precio de un servicio.
     *
     * <p>Se usa {@code PATCH} porque solo se modifica el campo {@code precio},
     * no el recurso completo. El nuevo precio se envía como query parameter:
     * {@code PATCH /api/servicios/{id}/precio?nuevoPrecio=18000.00}.</p>
     *
     * <p>Usa la query custom del repository que actualiza directamente en la base de datos
     * sin cargar la entidad completa, lo cual es más eficiente.</p>
     *
     * @param id          el UUID del servicio cuyo precio se quiere actualizar (extraído del path)
     * @param nuevoPrecio el nuevo precio a establecer (query param, debe ser mayor a 0)
     * @return {@code 200 OK} con un mensaje confirmando la actualización del precio
     */
    @PatchMapping("/api/servicios/{id}/precio")
    public ResponseEntity<String> actualizarPrecio(
            @PathVariable UUID id,
            @RequestParam BigDecimal nuevoPrecio) {
        servicioService.actualizarPrecio(id, nuevoPrecio);
        return ResponseEntity.ok("Precio actualizado a " + nuevoPrecio + " correctamente");
    }
}




