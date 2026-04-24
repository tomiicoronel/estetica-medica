package com.estetica.estetica.controller;

import com.estetica.estetica.dto.request.TurnoRequest;
import com.estetica.estetica.dto.response.TurnoResponse;
import com.estetica.estetica.service.TurnoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para gestionar {@link com.estetica.estetica.model.Turno}.
 *
 * <h3>Endpoints:</h3>
 * <table>
 *     <tr><th>Método</th><th>Path</th><th>Descripción</th></tr>
 *     <tr><td>POST</td><td>/api/profesionales/{profesionalId}/turnos</td><td>Crear turno</td></tr>
 *     <tr><td>GET</td><td>/api/profesionales/{profesionalId}/turnos</td><td>Listar turnos (opcional estado / rango de fechas)</td></tr>
 *     <tr><td>GET</td><td>/api/pacientes/{pacienteId}/turnos</td><td>Listar turnos de un paciente</td></tr>
 *     <tr><td>GET</td><td>/api/turnos/{id}</td><td>Buscar turno por ID</td></tr>
 *     <tr><td>PATCH</td><td>/api/turnos/{id}/estado?nuevoEstado=CONFIRMADO</td><td>Cambiar estado</td></tr>
 * </table>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-24
 */
@RestController
@RequiredArgsConstructor
public class TurnoController {

    private final TurnoService turnoService;

    /** Crea un turno para una profesional. */
    @PostMapping("/api/profesionales/{profesionalId}/turnos")
    public ResponseEntity<TurnoResponse> crear(
            @PathVariable UUID profesionalId,
            @Valid @RequestBody TurnoRequest request) {
        TurnoResponse response = turnoService.crear(profesionalId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Lista los turnos de una profesional. Soporta filtros opcionales mutuamente excluyentes:
     * <ul>
     *     <li>{@code ?estado=PENDIENTE} filtra por estado.</li>
     *     <li>{@code ?desde=...&hasta=...} filtra por rango de fechas (ISO-8601,
     *         ej: {@code 2026-04-01T00:00:00}).</li>
     * </ul>
     * Si no se envía ningún filtro, devuelve todos los turnos de la profesional.
     */
    @GetMapping("/api/profesionales/{profesionalId}/turnos")
    public ResponseEntity<List<TurnoResponse>> listarPorProfesional(
            @PathVariable UUID profesionalId,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {

        if (estado != null) {
            return ResponseEntity.ok(turnoService.listarPorProfesionalYEstado(profesionalId, estado));
        }
        if (desde != null || hasta != null) {
            return ResponseEntity.ok(turnoService.listarPorProfesionalYRango(profesionalId, desde, hasta));
        }
        return ResponseEntity.ok(turnoService.listarPorProfesional(profesionalId));
    }

    /** Lista los turnos de un paciente. */
    @GetMapping("/api/pacientes/{pacienteId}/turnos")
    public ResponseEntity<List<TurnoResponse>> listarPorPaciente(@PathVariable UUID pacienteId) {
        return ResponseEntity.ok(turnoService.listarPorPaciente(pacienteId));
    }

    /** Busca un turno por su UUID. */
    @GetMapping("/api/turnos/{id}")
    public ResponseEntity<TurnoResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(turnoService.buscarPorId(id));
    }

    /**
     * Cambia el estado del turno.
     *
     * <p>Ejemplo: {@code PATCH /api/turnos/{id}/estado?nuevoEstado=CONFIRMADO}.</p>
     */
    @PatchMapping("/api/turnos/{id}/estado")
    public ResponseEntity<TurnoResponse> cambiarEstado(
            @PathVariable UUID id,
            @RequestParam String nuevoEstado) {
        return ResponseEntity.ok(turnoService.cambiarEstado(id, nuevoEstado));
    }
}

