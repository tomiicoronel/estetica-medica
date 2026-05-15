package com.estetica.estetica.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Métodos de pago admitidos. El pago combinado se representa cargando múltiples pagos para el mismo turno.")
public enum MetodoPago {
    EFECTIVO,
    TRANSFERENCIA,
    MERCADO_PAGO,
    TRUEQUE
}

