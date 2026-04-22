package com.estetica.estetica.controller;

import com.estetica.estetica.dto.request.HistoriaClinicaFacialRequest;
import com.estetica.estetica.dto.response.HistoriaClinicaFacialResponse;
import com.estetica.estetica.service.HistoriaClinicaFacialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controlador REST que expone los endpoints para gestionar la
 * historia clínica facial de los pacientes.
 *
 * <h3>Endpoints disponibles</h3>
 * <table>
 *     <tr><th>Método</th><th>Path</th><th>Acción</th><th>Status</th></tr>
 *     <tr><td>POST</td><td>/api/pacientes/{pacienteId}/historia-clinica-facial</td>
 *         <td>Crear ficha facial de un paciente</td><td>201 Created</td></tr>
 *     <tr><td>GET</td><td>/api/pacientes/{pacienteId}/historia-clinica-facial</td>
 *         <td>Obtener ficha facial de un paciente</td><td>200 OK</td></tr>
 *     <tr><td>PUT</td><td>/api/historia-clinica-facial/{id}</td>
 *         <td>Actualizar ficha por ID</td><td>200 OK</td></tr>
 * </table>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-22
 * @see HistoriaClinicaFacialService
 * @see com.estetica.estetica.exception.GlobalExceptionHandler
 */
@RestController
@RequiredArgsConstructor
public class HistoriaClinicaFacialController {

    private final HistoriaClinicaFacialService service;

    /**
     * Crea la ficha clínica facial de un paciente.
     *
     * @param pacienteId UUID del paciente (path)
     * @param request    datos clínicos (body validado)
     * @return {@code 201 Created} con la ficha creada
     */
    @PostMapping("/api/pacientes/{pacienteId}/historia-clinica-facial")
    public ResponseEntity<HistoriaClinicaFacialResponse> crear(
            @PathVariable UUID pacienteId,
            @Valid @RequestBody HistoriaClinicaFacialRequest request) {
        HistoriaClinicaFacialResponse response = service.crearFicha(pacienteId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Obtiene la ficha clínica facial de un paciente.
     *
     * @param pacienteId UUID del paciente (path)
     * @return {@code 200 OK} con la ficha
     */
    @GetMapping("/api/pacientes/{pacienteId}/historia-clinica-facial")
    public ResponseEntity<HistoriaClinicaFacialResponse> obtenerPorPaciente(
            @PathVariable UUID pacienteId) {
        HistoriaClinicaFacialResponse response = service.buscarPorPaciente(pacienteId);
        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza la ficha clínica facial identificada por su UUID.
     *
     * @param id      UUID de la ficha (path)
     * @param request datos clínicos actualizados (body validado)
     * @return {@code 200 OK} con la ficha actualizada
     */
    @PutMapping("/api/historia-clinica-facial/{id}")
    public ResponseEntity<HistoriaClinicaFacialResponse> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody HistoriaClinicaFacialRequest request) {
        HistoriaClinicaFacialResponse response = service.actualizarFicha(id, request);
        return ResponseEntity.ok(response);
    }
}

