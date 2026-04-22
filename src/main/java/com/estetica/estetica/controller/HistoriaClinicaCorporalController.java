package com.estetica.estetica.controller;

import com.estetica.estetica.dto.request.HistoriaClinicaCorporalRequest;
import com.estetica.estetica.dto.response.HistoriaClinicaCorporalResponse;
import com.estetica.estetica.service.HistoriaClinicaCorporalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controlador REST que expone los endpoints para gestionar la
 * historia clínica corporal de los pacientes.
 *
 * <h3>Endpoints disponibles</h3>
 * <table>
 *     <tr><th>Método</th><th>Path</th><th>Acción</th><th>Status</th></tr>
 *     <tr><td>POST</td><td>/api/pacientes/{pacienteId}/historia-clinica-corporal</td>
 *         <td>Crear ficha corporal de un paciente</td><td>201 Created</td></tr>
 *     <tr><td>GET</td><td>/api/pacientes/{pacienteId}/historia-clinica-corporal</td>
 *         <td>Obtener ficha corporal de un paciente</td><td>200 OK</td></tr>
 *     <tr><td>PUT</td><td>/api/historia-clinica-corporal/{id}</td>
 *         <td>Actualizar ficha por ID</td><td>200 OK</td></tr>
 * </table>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-22
 * @see HistoriaClinicaCorporalService
 * @see com.estetica.estetica.exception.GlobalExceptionHandler
 */
@RestController
@RequiredArgsConstructor
public class HistoriaClinicaCorporalController {

    private final HistoriaClinicaCorporalService service;

    /**
     * Crea la ficha clínica corporal de un paciente.
     *
     * @param pacienteId UUID del paciente (path)
     * @param request    datos clínicos (body validado)
     * @return {@code 201 Created} con la ficha creada
     */
    @PostMapping("/api/pacientes/{pacienteId}/historia-clinica-corporal")
    public ResponseEntity<HistoriaClinicaCorporalResponse> crear(
            @PathVariable UUID pacienteId,
            @Valid @RequestBody HistoriaClinicaCorporalRequest request) {
        HistoriaClinicaCorporalResponse response = service.crearFicha(pacienteId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Obtiene la ficha clínica corporal de un paciente.
     *
     * @param pacienteId UUID del paciente (path)
     * @return {@code 200 OK} con la ficha
     */
    @GetMapping("/api/pacientes/{pacienteId}/historia-clinica-corporal")
    public ResponseEntity<HistoriaClinicaCorporalResponse> obtenerPorPaciente(
            @PathVariable UUID pacienteId) {
        HistoriaClinicaCorporalResponse response = service.buscarPorPaciente(pacienteId);
        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza la ficha clínica corporal identificada por su UUID.
     *
     * @param id      UUID de la ficha (path)
     * @param request datos clínicos actualizados (body validado, JSON completo)
     * @return {@code 200 OK} con la ficha actualizada
     */
    @PutMapping("/api/historia-clinica-corporal/{id}")
    public ResponseEntity<HistoriaClinicaCorporalResponse> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody HistoriaClinicaCorporalRequest request) {
        HistoriaClinicaCorporalResponse response = service.actualizarFicha(id, request);
        return ResponseEntity.ok(response);
    }
}

