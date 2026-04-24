package com.estetica.estetica.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Entidad JPA intermedia que resuelve la relación muchos-a-muchos entre {@link Turno}
 * y {@link Servicio}, y <b>congela el precio del servicio</b> al momento de crear el turno.
 *
 * <p>Se mapea a la tabla {@code turno_servicios} en PostgreSQL. Cada fila representa
 * un servicio incluido en un turno específico, guardando una copia del precio vigente
 * al momento de crearlo. Así, si el precio del servicio cambia después, los turnos
 * ya creados conservan su precio original (snapshot).</p>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-24
 * @see Turno
 * @see Servicio
 */
@Entity
@Table(name = "turno_servicios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TurnoServicio {

    /** Identificador único (UUID v4). */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /** Turno al que pertenece esta línea. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "turno_id", nullable = false)
    private Turno turno;

    /** Servicio incluido en el turno. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicio servicio;

    /**
     * Precio del servicio al momento exacto de crear el turno (snapshot).
     *
     * <p>Se usa {@link BigDecimal} por precisión monetaria. Si el precio del servicio
     * cambia luego, este valor no se modifica: el turno conserva el precio acordado.</p>
     */
    @Column(name = "precio_momento", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioMomento;
}

