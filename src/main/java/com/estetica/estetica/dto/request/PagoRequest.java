package com.estetica.estetica.dto.request;

import com.estetica.estetica.model.MetodoPago;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "PagoRequest", description = "Datos para registrar un pago de un turno. El turnoId se toma del path.")
public class PagoRequest {

    @Schema(description = "Método de pago utilizado", example = "EFECTIVO", allowableValues = {"EFECTIVO", "TRANSFERENCIA", "MERCADO_PAGO", "TRUEQUE"}, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El método de pago es obligatorio")
    private MetodoPago metodo;

    @Schema(description = "Monto del pago. Debe ser mayor a cero", example = "15000.00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    @Schema(description = "Indica si este pago es una seña anticipada", example = "false")
    @Builder.Default
    private Boolean esSena = false;

    @Schema(description = "Detalle obligatorio cuando el método es TRUEQUE", example = "Intercambio por sesión de fotografía profesional")
    private String detalleTrueque;

    @Schema(description = "Fecha y hora del pago. Si no se envía, se usa la fecha actual", example = "2026-05-15T18:30:00")
    private LocalDateTime fecha;
}

