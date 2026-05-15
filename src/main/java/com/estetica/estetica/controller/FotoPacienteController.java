package com.estetica.estetica.controller;

import com.estetica.estetica.dto.request.FotoPacienteRequest;
import com.estetica.estetica.dto.response.ErrorResponse;
import com.estetica.estetica.dto.response.FotoPacienteResponse;
import com.estetica.estetica.dto.response.ValidationErrorResponse;
import com.estetica.estetica.service.FotoPacienteService;
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
@Tag(name = "Fotos de Paciente", description = "Operaciones para registrar, consultar y eliminar fotos de seguimiento vinculadas a sesiones clínicas.")
public class FotoPacienteController {

    private final FotoPacienteService fotoPacienteService;

    @PostMapping("/api/sesiones-clinicas/{sesionId}/fotos")
    @Operation(summary = "Registrar foto de paciente", description = "Registra una foto vinculada a una sesión clínica. La ruta local se genera internamente y el paciente se deriva desde el turno de la sesión.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Foto registrada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o ID de sesión inconsistente",
                    content = @Content(schema = @Schema(oneOf = {ErrorResponse.class, ValidationErrorResponse.class}))),
            @ApiResponse(responseCode = "404", description = "Sesión clínica no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<FotoPacienteResponse> registrar(
            @Parameter(description = "UUID de la sesión clínica", example = "910e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID sesionId,
            @Valid @RequestBody FotoPacienteRequest request) {
        FotoPacienteResponse response = fotoPacienteService.registrar(sesionId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/api/pacientes/{pacienteId}/fotos")
    @Operation(summary = "Listar fotos de un paciente", description = "Devuelve todas las fotos registradas para un paciente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<FotoPacienteResponse>> listarPorPaciente(
            @Parameter(description = "UUID del paciente", example = "650e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID pacienteId) {
        return ResponseEntity.ok(fotoPacienteService.listarPorPaciente(pacienteId));
    }

    @GetMapping("/api/sesiones-clinicas/{sesionId}/fotos")
    @Operation(summary = "Listar fotos de una sesión clínica", description = "Devuelve todas las fotos vinculadas a una sesión clínica específica.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Sesión clínica no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<FotoPacienteResponse>> listarPorSesion(
            @Parameter(description = "UUID de la sesión clínica", example = "910e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID sesionId) {
        return ResponseEntity.ok(fotoPacienteService.listarPorSesion(sesionId));
    }

    @DeleteMapping("/api/fotos/{id}")
    @Operation(summary = "Eliminar foto", description = "Elimina el registro de la foto. En esta fase no elimina archivos físicos, solo el registro de base de datos.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Foto eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Foto no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "UUID de la foto", example = "920e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        fotoPacienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

