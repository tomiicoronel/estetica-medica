package com.estetica.estetica.dto.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "ResumenPagoResponse", description = "Resumen financiero de un turno: total, pagos registrados y deuda pendiente.")
public class ResumenPagoResponse {

    @Schema(description = "UUID del turno", example = "850e8400-e29b-41d4-a716-446655440000")
    private UUID turnoId;

    @Schema(description = "Monto total del turno", example = "30000.00")
    private BigDecimal montoTotal;

    @Schema(description = "Suma de todos los pagos registrados para el turno", example = "15000.00")
    private BigDecimal montoPagado;

    @Schema(description = "Monto pendiente de pago", example = "15000.00")
    private BigDecimal deuda;

    @Schema(description = "Indica si todavía queda deuda pendiente", example = "true")
    private Boolean tieneDeuda;

    @ArraySchema(schema = @Schema(implementation = PagoResponse.class))
    private List<PagoResponse> pagos;
}

