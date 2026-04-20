package com.estetica.estetica.controller;

import com.estetica.estetica.dto.request.PacienteRequest;
import com.estetica.estetica.dto.response.PacienteResponse;
import com.estetica.estetica.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST que expone los endpoints para gestionar pacientes.
 *
 * <p>Los endpoints están organizados en dos niveles:</p>
 * <ul>
 *     <li><b>Anidados bajo profesional</b> ({@code /api/profesionales/{profesionalId}/pacientes}) —
 *         para crear y listar pacientes de una profesional.</li>
 *     <li><b>Directos</b> ({@code /api/pacientes/{id}}) — para buscar y actualizar
 *         un paciente específico por su UUID.</li>
 * </ul>
 *
 * <h3>Endpoints disponibles:</h3>
 * <table>
 *     <tr><th>Método</th><th>Path</th><th>Acción</th><th>Status</th></tr>
 *     <tr><td>POST</td><td>/api/profesionales/{profesionalId}/pacientes</td><td>Crear paciente</td><td>201 Created</td></tr>
 *     <tr><td>GET</td><td>/api/profesionales/{profesionalId}/pacientes</td><td>Listar pacientes de una profesional</td><td>200 OK</td></tr>
 *     <tr><td>GET</td><td>/api/pacientes/{id}</td><td>Buscar paciente por ID</td><td>200 OK</td></tr>
 *     <tr><td>PUT</td><td>/api/pacientes/{id}</td><td>Actualizar paciente</td><td>200 OK</td></tr>
 * </table>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-20
 * @see PacienteService
 * @see com.estetica.estetica.exception.GlobalExceptionHandler
 */
@RestController
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    // ---- Endpoints anidados bajo profesional ----

    /**
     * Crea un nuevo paciente vinculado a una profesional.
     *
     * <p>El {@code profesionalId} se extrae del path y se inyecta en el request
     * para que el cliente no tenga que repetirlo en el body.</p>
     *
     * @param profesionalId UUID de la profesional (path)
     * @param request       datos del paciente (body JSON validado)
     * @return {@code 201 Created} con el {@link PacienteResponse} creado
     */
    @PostMapping("/api/profesionales/{profesionalId}/pacientes")
    public ResponseEntity<PacienteResponse> crear(
            @PathVariable UUID profesionalId,
            @Valid @RequestBody PacienteRequest request) {
        request.setProfesionalId(profesionalId);
        PacienteResponse response = pacienteService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Lista todos los pacientes de una profesional.
     *
     * @param profesionalId UUID de la profesional (path)
     * @return {@code 200 OK} con la lista de {@link PacienteResponse}
     */
    @GetMapping("/api/profesionales/{profesionalId}/pacientes")
    public ResponseEntity<List<PacienteResponse>> listarPorProfesional(
            @PathVariable UUID profesionalId) {
        List<PacienteResponse> response = pacienteService.listarPorProfesional(profesionalId);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista solo los pacientes activos de una profesional.
     *
     * @param profesionalId UUID de la profesional (path)
     * @return {@code 200 OK} con la lista de {@link PacienteResponse} activos
     */
    @GetMapping("/api/profesionales/{profesionalId}/pacientes/activos")
    public ResponseEntity<List<PacienteResponse>> listarActivosPorProfesional(
            @PathVariable UUID profesionalId) {
        List<PacienteResponse> response = pacienteService.listarActivosPorProfesional(profesionalId);
        return ResponseEntity.ok(response);
    }

    // ---- Endpoints directos sobre paciente ----

    /**
     * Busca un paciente por su UUID.
     *
     * @param id UUID del paciente (path)
     * @return {@code 200 OK} con el {@link PacienteResponse}
     */
    @GetMapping("/api/pacientes/{id}")
    public ResponseEntity<PacienteResponse> buscarPorId(@PathVariable UUID id) {
        PacienteResponse response = pacienteService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza los datos de un paciente existente.
     *
     * @param id      UUID del paciente a actualizar (path)
     * @param request nuevos datos del paciente (body JSON validado)
     * @return {@code 200 OK} con el {@link PacienteResponse} actualizado
     */
    @PutMapping("/api/pacientes/{id}")
    public ResponseEntity<PacienteResponse> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody PacienteRequest request) {
        PacienteResponse response = pacienteService.actualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Activa o archiva un paciente (soft delete).
     *
     * <p>Ejemplo: {@code PATCH /api/pacientes/{id}/estado?activo=false} para archivar.</p>
     *
     * @param id     UUID del paciente (path)
     * @param activo {@code true} para activar, {@code false} para archivar (query param)
     * @return {@code 200 OK} con mensaje de confirmación
     */
    @PatchMapping("/api/pacientes/{id}/estado")
    public ResponseEntity<String> cambiarEstado(
            @PathVariable UUID id,
            @RequestParam Boolean activo) {
        pacienteService.cambiarEstado(id, activo);
        String estado = activo ? "activado" : "archivado";
        return ResponseEntity.ok("Paciente " + estado + " correctamente");
    }
}

