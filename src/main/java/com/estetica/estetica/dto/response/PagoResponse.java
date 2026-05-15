package com.estetica.estetica.dto.response;

import com.estetica.estetica.model.MetodoPago;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "PagoResponse", description = "Datos devueltos por la API para un pago registrado.")
public class PagoResponse {

    @Schema(description = "Identificador único UUID del pago", example = "930e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "UUID del turno pagado", example = "850e8400-e29b-41d4-a716-446655440000")
    private UUID turnoId;

    @Schema(description = "Método de pago utilizado", example = "EFECTIVO", allowableValues = {"EFECTIVO", "TRANSFERENCIA", "MERCADO_PAGO", "TRUEQUE"})
    private MetodoPago metodo;

    @Schema(description = "Monto imputado al turno", example = "15000.00")
    private BigDecimal monto;

    @Schema(description = "Indica si el pago fue registrado como seña", example = "false")
    private Boolean esSena;

    @Schema(description = "Indica si el pago corresponde a un trueque", example = "false")
    private Boolean esTrueque;

    @Schema(description = "Detalle del trueque, si corresponde", example = "Intercambio por sesión de fotografía profesional")
    private String detalleTrueque;

    @Schema(description = "Fecha y hora efectiva del pago", example = "2026-05-15T18:30:00")
    private LocalDateTime fecha;

    @Schema(description = "Fecha y hora de creación", example = "2026-05-15T18:30:00")
    private LocalDateTime creadoEn;

    @Schema(description = "Fecha y hora de última actualización", example = "2026-05-15T18:30:00")
    private LocalDateTime actualizadoEn;
}

