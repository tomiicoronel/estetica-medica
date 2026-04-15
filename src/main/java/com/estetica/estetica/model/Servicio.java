package com.estetica.estetica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad JPA que representa un servicio ofrecido por una profesional del centro de estética médica.
 *
 * <p>Se mapea a la tabla {@code servicios} en PostgreSQL. Cada servicio pertenece a una
 * {@link Profesional} mediante una relación {@code ManyToOne}: una profesional puede ofrecer
 * muchos servicios, pero cada servicio pertenece a una única profesional.</p>
 *
 * <h3>Anotaciones de Lombok utilizadas:</h3>
 * <ul>
 *     <li>{@code @Getter / @Setter} — Genera automáticamente los getters y setters de todos los campos.</li>
 *     <li>{@code @NoArgsConstructor} — Constructor vacío requerido por JPA.</li>
 *     <li>{@code @AllArgsConstructor} — Constructor con todos los campos, utilizado internamente por el Builder.</li>
 *     <li>{@code @Builder} — Permite construir instancias con el patrón Builder: {@code Servicio.builder().nombre("Limpieza facial").build()}.</li>
 * </ul>
 *
 * <h3>Campos de auditoría:</h3>
 * <p>{@code creadoEn} y {@code actualizadoEn} se llenan automáticamente mediante
 * los callbacks de JPA {@code @PrePersist} y {@code @PreUpdate}.</p>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-15
 * @see Profesional
 * @see com.estetica.estetica.repository.ServicioRepository
 */
@Entity
@Table(name = "servicios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Servicio {

    /**
     * Identificador único del servicio.
     *
     * <p>Se genera automáticamente como UUID v4 al persistir la entidad.
     * Se usa UUID en lugar de Long autoincremental por seguridad: los IDs
     * secuenciales son predecibles y vulnerables a ataques de enumeración (IDOR).</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Profesional que ofrece este servicio.
     *
     * <p>Relación {@code ManyToOne}: muchos servicios pueden pertenecer a una misma profesional.
     * {@code @JoinColumn} define la columna de clave foránea {@code profesional_id} en la tabla {@code servicios}.
     * {@code nullable = false} indica que todo servicio debe tener una profesional asignada.
     * {@code FetchType.LAZY} evita cargar la profesional automáticamente en cada consulta de servicio,
     * mejorando el rendimiento (se carga solo cuando se accede explícitamente).</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesional_id", nullable = false)
    private Profesional profesional;

    /** Nombre del servicio (ej: "Limpieza facial profunda"). Campo obligatorio, máximo 150 caracteres. */
    @NotBlank(message = "El nombre del servicio es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    /**
     * Descripción detallada del servicio.
     *
     * <p>Campo obligatorio. Se usa {@code @Column(columnDefinition = "TEXT")} para que PostgreSQL
     * lo almacene como tipo TEXT sin límite de longitud, ideal para descripciones largas.</p>
     */
    @Column(name = "descripcion", columnDefinition = "TEXT")
    @NotBlank(message = "La descripcion del servicio es obligatorio")
    private String descripcion;

    /**
     * Precio del servicio en la moneda local.
     *
     * <p>Se usa {@link BigDecimal} en lugar de {@code double} o {@code float} porque los tipos
     * de punto flotante tienen errores de precisión inaceptables para valores monetarios
     * (ej: {@code 0.1 + 0.2 = 0.30000000000000004}). {@code BigDecimal} garantiza precisión exacta.</p>
     *
     * <p>{@code precision = 10} permite hasta 10 dígitos en total y {@code scale = 2} reserva
     * 2 para los decimales (ej: 99999999.99).</p>
     */
    @NotNull(message = "El precio es obligatorio")
    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    /**
     * Indica si el servicio está activo y disponible para ser agendado.
     *
     * <p>En lugar de eliminar servicios de la base de datos (hard delete), se recomienda
     * desactivarlos (soft delete) para mantener el historial. Por defecto, un servicio
     * nuevo se crea como activo ({@code true}).</p>
     */
    @NotNull(message = "El campo activo es obligatorio")
    @Column(name = "activo", nullable = false)
    @Builder.Default
    private Boolean activo = true;

    /** Fecha y hora en que se creó el registro. Se setea automáticamente y no se puede modificar. */
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    /** Fecha y hora de la última modificación del registro. Se actualiza automáticamente en cada cambio. */
    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;

    /**
     * Callback de JPA que se ejecuta automáticamente <b>antes de insertar</b> la entidad en la base de datos.
     *
     * <p>Setea {@code creadoEn} y {@code actualizadoEn} con la fecha y hora actual.</p>
     */
    @PrePersist
    protected void onCreate() {
        this.creadoEn = LocalDateTime.now();
        this.actualizadoEn = LocalDateTime.now();
    }

    /**
     * Callback de JPA que se ejecuta automáticamente <b>antes de actualizar</b> la entidad en la base de datos.
     *
     * <p>Actualiza solo {@code actualizadoEn} con la fecha y hora actual.</p>
     */
    @PreUpdate
    protected void onUpdate() {
        this.actualizadoEn = LocalDateTime.now();
    }
}

