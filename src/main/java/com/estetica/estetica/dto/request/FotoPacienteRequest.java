package com.estetica.estetica.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "FotoPacienteRequest", description = "Datos para registrar una foto. La ruta de imagen y el paciente se manejan internamente.")
public class FotoPacienteRequest {

    @Schema(description = "UUID de la sesión clínica a la que se vincula la foto", example = "910e8400-e29b-41d4-a716-446655440000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID de la sesión clínica es obligatorio")
    private UUID sesionClinicaId;

    @Schema(description = "Descripción opcional de la foto", example = "Antes del tratamiento, perfil derecho")
    private String descripcion;
}

