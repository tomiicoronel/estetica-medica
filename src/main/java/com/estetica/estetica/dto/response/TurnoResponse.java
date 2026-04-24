package com.estetica.estetica.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO de respuesta para un {@link com.estetica.estetica.model.Turno}.
 *
 * <p>Incluye los datos del turno más la lista de servicios incluidos (cada uno con
 * su UUID de servicio, nombre y precio congelado al momento de crear el turno).</p>
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
public class TurnoResponse {

    private UUID id;
    private UUID profesionalId;
    private UUID pacienteId;
    private LocalDateTime fechaHora;
    private String estado;
    private BigDecimal montoTotal;
    private String observaciones;

    /** Lista de servicios incluidos en el turno, con su precio congelado. */
    private List<TurnoServicioResponse> servicios;

    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;

    /**
     * DTO anidado que representa un servicio incluido en un turno.
     *
     * <p>Incluye el UUID del servicio, su nombre actual y el {@code precioMomento}
     * (snapshot del precio vigente cuando se creó el turno).</p>
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TurnoServicioResponse {
        private UUID servicioId;
        private String nombre;
        private BigDecimal precioMomento;
    }
}

