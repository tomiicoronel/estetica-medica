package com.estetica.estetica.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "SesionClinicaRequest", description = "Datos para crear o actualizar una sesión clínica. El número de sesión lo calcula el sistema.")
public class SesionClinicaRequest {

    @Schema(description = "Descripción de qué se hizo en la sesión", example = "Limpieza facial profunda con extracción y máscara calmante", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El tratamiento es obligatorio")
    private String tratamiento;

    @Schema(description = "Cómo respondió y toleró el paciente el tratamiento", example = "Buena tolerancia, leve eritema esperado", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La respuesta y tolerancia es obligatoria")
    private String respuestaTolerancia;

    @Schema(description = "Observaciones opcionales de la sesión", example = "Indicar protector solar y control en 15 días")
    private String observaciones;
}

