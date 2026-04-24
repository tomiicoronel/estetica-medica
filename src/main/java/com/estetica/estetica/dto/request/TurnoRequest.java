package com.estetica.estetica.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO de entrada para crear o actualizar un {@link com.estetica.estetica.model.Turno}.
 *
 * <p>El {@code profesionalId} no va en el body porque se toma del path
 * ({@code /api/profesionales/{profesionalId}/turnos}).</p>
 *
 * <h3>Ejemplo JSON:</h3>
 * <pre>{@code
 * {
 *   "pacienteId": "uuid-paciente",
 *   "fechaHora": "2026-05-10T15:30:00",
 *   "servicioIds": ["uuid-serv-1", "uuid-serv-2"],
 *   "observaciones": "Primera sesión"
 * }
 * }</pre>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-24
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TurnoRequest {

    /** UUID del paciente al que se le asigna el turno. Obligatorio. */
    @NotNull(message = "El ID del paciente es obligatorio")
    private UUID pacienteId;

    /**
     * Fecha y hora del turno. Obligatoria y debe ser futura.
     *
     * <p>{@code @Future} rechaza fechas iguales o anteriores a la actual.</p>
     */
    @NotNull(message = "La fecha y hora del turno es obligatoria")
    @Future(message = "La fecha del turno no puede ser en el pasado")
    private LocalDateTime fechaHora;

    /** Lista de UUIDs de servicios a incluir en el turno. Obligatoria, al menos uno. */
    @NotNull(message = "La lista de servicios es obligatoria")
    @NotEmpty(message = "El turno debe incluir al menos un servicio")
    private List<UUID> servicioIds;

    /** Observaciones opcionales sobre el turno. */
    private String observaciones;
}

