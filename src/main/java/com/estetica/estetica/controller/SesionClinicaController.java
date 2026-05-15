package com.estetica.estetica.controller;

import com.estetica.estetica.dto.request.SesionClinicaRequest;
import com.estetica.estetica.dto.response.ErrorResponse;
import com.estetica.estetica.dto.response.SesionClinicaResponse;
import com.estetica.estetica.dto.response.ValidationErrorResponse;
import com.estetica.estetica.service.SesionClinicaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Sesiones Clínicas", description = "Operaciones para registrar, consultar y actualizar sesiones clínicas vinculadas a turnos realizados.")
public class SesionClinicaController {

    private final SesionClinicaService sesionClinicaService;

    @PostMapping("/api/turnos/{turnoId}/sesion-clinica")
    @Operation(summary = "Crear sesión clínica", description = "Registra una sesión clínica para un turno. El turno debe estar REALIZADO y solo puede tener una sesión clínica.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sesión clínica creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o turno no realizado",
                    content = @Content(schema = @Schema(oneOf = {ErrorResponse.class, ValidationErrorResponse.class}))),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "El turno ya tiene una sesión clínica",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<SesionClinicaResponse> crear(
            @Parameter(description = "UUID del turno", example = "850e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID turnoId,
            @Valid @RequestBody SesionClinicaRequest request) {
        SesionClinicaResponse response = sesionClinicaService.crear(turnoId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/api/turnos/{turnoId}/sesion-clinica")
    @Operation(summary = "Obtener sesión clínica de un turno", description = "Devuelve la sesión clínica registrada para un turno específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sesión clínica encontrada"),
            @ApiResponse(responseCode = "404", description = "Turno o sesión clínica no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<SesionClinicaResponse> obtenerPorTurno(
            @Parameter(description = "UUID del turno", example = "850e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID turnoId) {
        return ResponseEntity.ok(sesionClinicaService.buscarPorTurno(turnoId));
    }

    @GetMapping("/api/pacientes/{pacienteId}/sesiones-clinicas")
    @Operation(summary = "Listar sesiones clínicas de un paciente", description = "Lista todas las sesiones clínicas de un paciente a través de sus turnos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<SesionClinicaResponse>> listarPorPaciente(
            @Parameter(description = "UUID del paciente", example = "650e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID pacienteId) {
        return ResponseEntity.ok(sesionClinicaService.listarPorPaciente(pacienteId));
    }

    @PutMapping("/api/sesiones-clinicas/{id}")
    @Operation(summary = "Actualizar sesión clínica", description = "Actualiza tratamiento, respuesta/tolerancia y observaciones de una sesión clínica existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sesión clínica actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(schema = @Schema(oneOf = {ErrorResponse.class, ValidationErrorResponse.class}))),
            @ApiResponse(responseCode = "404", description = "Sesión clínica no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<SesionClinicaResponse> actualizar(
            @Parameter(description = "UUID de la sesión clínica", example = "910e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id,
            @Valid @RequestBody SesionClinicaRequest request) {
        return ResponseEntity.ok(sesionClinicaService.actualizar(id, request));
    }
}

