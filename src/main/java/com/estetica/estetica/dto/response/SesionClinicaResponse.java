package com.estetica.estetica.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "SesionClinicaResponse", description = "Datos devueltos por la API para una sesión clínica.")
public class SesionClinicaResponse {

    @Schema(description = "Identificador único UUID de la sesión clínica", example = "910e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "UUID del turno asociado", example = "850e8400-e29b-41d4-a716-446655440000")
    private UUID turnoId;

    @Schema(description = "UUID del paciente derivado del turno", example = "650e8400-e29b-41d4-a716-446655440000")
    private UUID pacienteId;

    @Schema(description = "UUID de la profesional derivada del turno", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID profesionalId;

    @Schema(description = "Número correlativo de sesión del paciente", example = "1")
    private Integer numeroSesion;

    @Schema(description = "Descripción de qué se hizo en la sesión")
    private String tratamiento;

    @Schema(description = "Cómo respondió y toleró el paciente el tratamiento")
    private String respuestaTolerancia;

    @Schema(description = "Observaciones opcionales")
    private String observaciones;

    @Schema(description = "Fecha y hora de creación", example = "2026-05-11T15:30:00")
    private LocalDateTime creadoEn;

    @Schema(description = "Fecha y hora de última actualización", example = "2026-05-11T15:30:00")
    private LocalDateTime actualizadoEn;
}

