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
@Schema(name = "FotoPacienteResponse", description = "Datos devueltos por la API para una foto de seguimiento de paciente.")
public class FotoPacienteResponse {

    @Schema(description = "Identificador único UUID de la foto", example = "920e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "UUID del paciente al que pertenece la foto", example = "650e8400-e29b-41d4-a716-446655440000")
    private UUID pacienteId;

    @Schema(description = "UUID de la sesión clínica asociada", example = "910e8400-e29b-41d4-a716-446655440000")
    private UUID sesionClinicaId;

    @Schema(description = "Ruta local generada para la imagen", example = "uploads/pacientes/650e8400-e29b-41d4-a716-446655440000/sesiones/910e8400-e29b-41d4-a716-446655440000/foto-920e8400-e29b-41d4-a716-446655440000.jpg")
    private String rutaImagen;

    @Schema(description = "Fecha y hora asociada a la foto", example = "2026-05-11T15:30:00")
    private LocalDateTime fecha;

    @Schema(description = "Descripción opcional de la foto")
    private String descripcion;

    @Schema(description = "Fecha y hora de creación", example = "2026-05-11T15:30:00")
    private LocalDateTime creadoEn;
}

