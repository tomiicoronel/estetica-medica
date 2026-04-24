package com.estetica.estetica.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidad JPA que representa un <b>turno</b> agendado por una profesional para un paciente.
 *
 * <p>Se mapea a la tabla {@code turnos} en PostgreSQL. Un turno pertenece a una
 * {@link Profesional} y a un {@link Paciente} (ambas relaciones {@code ManyToOne})
 * e incluye uno o más servicios a través de la entidad intermedia
 * {@link TurnoServicio}, que congela el precio de cada servicio al momento de crear el turno.</p>
 *
 * <h3>Estados posibles:</h3>
 * <ul>
 *     <li>{@code PENDIENTE} — valor por defecto al crear el turno.</li>
 *     <li>{@code CONFIRMADO} — la paciente confirmó asistencia.</li>
 *     <li>{@code REALIZADO} — estado final, la sesión se completó.</li>
 *     <li>{@code CANCELADO} — estado final, el turno no se realizará.</li>
 * </ul>
 *
 * <h3>Transiciones válidas:</h3>
 * <p>{@code PENDIENTE → CONFIRMADO | CANCELADO},
 * {@code CONFIRMADO → REALIZADO | CANCELADO}.
 * {@code REALIZADO} y {@code CANCELADO} son estados finales.</p>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-24
 * @see TurnoServicio
 * @see Profesional
 * @see Paciente
 */
@Entity
@Table(name = "turnos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Turno {

    /** Estados posibles del turno. */
    public static final String ESTADO_PENDIENTE = "PENDIENTE";
    public static final String ESTADO_CONFIRMADO = "CONFIRMADO";
    public static final String ESTADO_REALIZADO = "REALIZADO";
    public static final String ESTADO_CANCELADO = "CANCELADO";

    /** Identificador único del turno (UUID v4). */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Profesional dueña del turno.
     *
     * <p>Relación {@code ManyToOne}: muchos turnos pertenecen a una misma profesional.
     * {@code FetchType.LAZY} para evitar cargar la profesional en cada consulta.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profesional_id", nullable = false)
    private Profesional profesional;

    /**
     * Paciente al que se le asigna el turno.
     *
     * <p>Relación {@code ManyToOne}: muchos turnos pueden pertenecer a un mismo paciente.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    /** Fecha y hora en que está agendado el turno. */
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    /**
     * Estado actual del turno.
     *
     * <p>Por defecto {@code PENDIENTE} al crear. Ver transiciones permitidas arriba.</p>
     */
    @Builder.Default
    @Column(name = "estado", nullable = false, length = 20)
    private String estado = ESTADO_PENDIENTE;

    /**
     * Monto total del turno.
     *
     * <p>Se calcula sumando los {@code precioMomento} de cada {@link TurnoServicio}
     * asociado. Se guarda denormalizado en el turno para evitar recalcular en cada consulta.</p>
     */
    @Column(name = "monto_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoTotal;

    /** Notas u observaciones sobre el turno (opcional). */
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    /**
     * Servicios incluidos en el turno, con su precio congelado al momento de crear.
     *
     * <p>Relación {@code OneToMany} inversa: el dueño de la FK es {@link TurnoServicio}.
     * {@code cascade = ALL} + {@code orphanRemoval = true} para que los TurnoServicio
     * se persistan y eliminen junto con el turno.</p>
     */
    @Builder.Default
    @OneToMany(mappedBy = "turno", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TurnoServicio> turnoServicios = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @UpdateTimestamp
    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
}

