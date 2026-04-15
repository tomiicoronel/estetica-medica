package com.estetica.estetica.controller;

import com.estetica.estetica.dto.request.ProfesionalRequest;
import com.estetica.estetica.dto.response.ProfesionalResponse;
import com.estetica.estetica.service.ProfesionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST que expone los endpoints CRUD para gestionar profesionales.
 *
 * <p>Todos los endpoints están bajo el path base {@code /api/profesionales}.
 * Delega la lógica de negocio al {@link ProfesionalService}.</p>
 *
 * <h3>Anotaciones utilizadas:</h3>
 * <ul>
 *     <li>{@code @RestController} — Combina {@code @Controller} y {@code @ResponseBody}.
 *         Indica que todos los métodos devuelven datos serializados (JSON) en lugar de vistas HTML.</li>
 *     <li>{@code @RequestMapping("/api/profesionales")} — Define el path base para todos los endpoints.</li>
 *     <li>{@code @RequiredArgsConstructor} (Lombok) — Inyección de dependencias por constructor.</li>
 * </ul>
 *
 * <h3>Endpoints disponibles:</h3>
 * <table>
 *     <tr><th>Método</th><th>Path</th><th>Acción</th><th>Status</th></tr>
 *     <tr><td>POST</td><td>/api/profesionales</td><td>Crear profesional</td><td>201 Created</td></tr>
 *     <tr><td>GET</td><td>/api/profesionales/{id}</td><td>Buscar por ID</td><td>200 OK</td></tr>
 *     <tr><td>GET</td><td>/api/profesionales</td><td>Listar todas</td><td>200 OK</td></tr>
 *     <tr><td>PUT</td><td>/api/profesionales/{id}</td><td>Actualizar</td><td>200 OK</td></tr>
 *     <tr><td>DELETE</td><td>/api/profesionales/{id}</td><td>Eliminar</td><td>204 No Content</td></tr>
 * </table>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-14
 * @see ProfesionalService
 * @see com.estetica.estetica.exception.GlobalExceptionHandler
 */
@RestController
@RequestMapping("/api/profesionales")
@RequiredArgsConstructor
public class ProfesionalController {

    /** Servicio inyectado por constructor que contiene la lógica de negocio de profesionales. */
    private final ProfesionalService profesionalService;

    /**
     * Crea una nueva profesional.
     *
     * <p>{@code @Valid} activa las validaciones del DTO ({@code @NotBlank}, {@code @Email}, etc.).
     * Si falla alguna validación, Spring lanza {@code MethodArgumentNotValidException}
     * que es capturada por el {@link com.estetica.estetica.exception.GlobalExceptionHandler}.</p>
     *
     * @param request los datos de la profesional a crear (body JSON)
     * @return {@code 201 Created} con el {@link ProfesionalResponse} de la profesional creada
     */
    @PostMapping
    public ResponseEntity<ProfesionalResponse> crear(@Valid @RequestBody ProfesionalRequest request) {
        ProfesionalResponse response = profesionalService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Busca una profesional por su UUID.
     *
     * <p>{@code @PathVariable} extrae el ID de la URL y Spring lo convierte automáticamente a UUID.</p>
     *
     * @param id el UUID de la profesional (extraído del path)
     * @return {@code 200 OK} con el {@link ProfesionalResponse} de la profesional encontrada
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProfesionalResponse> buscarPorId(@PathVariable UUID id) {
        ProfesionalResponse response = profesionalService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista todas las profesionales registradas.
     *
     * @return {@code 200 OK} con la lista de {@link ProfesionalResponse}
     */
    @GetMapping
    public ResponseEntity<List<ProfesionalResponse>> listarTodos() {
        List<ProfesionalResponse> response = profesionalService.listarTodos();
        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza los datos de una profesional existente.
     *
     * @param id      el UUID de la profesional a actualizar (extraído del path)
     * @param request los nuevos datos de la profesional (body JSON validado)
     * @return {@code 200 OK} con el {@link ProfesionalResponse} actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProfesionalResponse> actualizar(@PathVariable UUID id, @Valid @RequestBody ProfesionalRequest request) {
        ProfesionalResponse response = profesionalService.actualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Elimina una profesional por su UUID.
     *
     * @param id el UUID de la profesional a eliminar (extraído del path)
     * @return {@code 204 No Content} sin body (la eliminación fue exitosa)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        profesionalService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
