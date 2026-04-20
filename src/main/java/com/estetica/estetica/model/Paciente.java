package com.estetica.estetica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad JPA que representa a un paciente del centro de estética médica.
 *
 * <p>Se mapea a la tabla {@code pacientes} en PostgreSQL. Cada paciente pertenece a una
 * {@link Profesional} mediante una relación {@code ManyToOne}: una profesional puede atender
 * a muchos pacientes, pero cada registro de paciente pertenece a una única profesional
 * (modelo multi-tenant aislado).</p>
 *
 * <h3>Anotaciones de Lombok utilizadas:</h3>
 * <ul>
 *     <li>{@code @Getter / @Setter} — Genera automáticamente los getters y setters de todos los campos.</li>
 *     <li>{@code @NoArgsConstructor} — Constructor vacío requerido por JPA.</li>
 *     <li>{@code @AllArgsConstructor} — Constructor con todos los campos, utilizado internamente por el Builder.</li>
 *     <li>{@code @Builder} — Permite construir instancias con el patrón Builder.</li>
 * </ul>
 *
 * <h3>Campos de auditoría:</h3>
 * <p>{@code creadoEn} y {@code actualizadoEn} se llenan automáticamente mediante
 * los callbacks de JPA {@code @PrePersist} y {@code @PreUpdate}.</p>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-17
 * @see Profesional
 * @see com.estetica.estetica.repository.PacienteRepository
 */
@Entity
@Table(name = "pacientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paciente {

    /**
     * Identificador único del paciente.
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
     * Profesional que atiende a este paciente.
     *
     * <p>Relación {@code ManyToOne}: muchos pacientes pueden pertenecer a una misma profesional.
     * {@code @JoinColumn} define la columna de clave foránea {@code profesional_id} en la tabla {@code pacientes}.
     * {@code nullable = false} indica que todo paciente debe tener una profesional asignada.
     * {@code FetchType.LAZY} evita cargar la profesional automáticamente en cada consulta de paciente,
     * mejorando el rendimiento (se carga solo cuando se accede explícitamente).</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesional_id", nullable = false)
    private Profesional profesional;

    /** Nombre del paciente. Campo obligatorio, máximo 100 caracteres. */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    /** Apellido del paciente. Campo obligatorio, máximo 100 caracteres. */
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede superar los 100 caracteres")
    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    /**
     * DNI o CUIT del paciente.
     *
     * <p>Campo obligatorio. Se almacena como String para soportar formatos con guiones
     * o caracteres especiales (ej: "20-12345678-9" para CUIT).</p>
     */
    @NotBlank(message = "El DNI/CUIT es obligatorio")
    @Size(max = 20, message = "El DNI/CUIT no puede superar los 20 caracteres")
    @Column(name = "dni_cuit", nullable = false, length = 20)
    private String dniCuit;

    /**
     * Fecha de nacimiento del paciente.
     *
     * <p>Campo opcional. Se usa {@link LocalDate} porque solo importa la fecha, no la hora.
     * Relevante para tratamientos estéticos donde la edad del paciente influye.</p>
     */
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    /** Teléfono de contacto del paciente. Campo obligatorio, máximo 20 caracteres. */
    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
    @Column(name = "telefono", nullable = false, length = 20)
    private String telefono;

    /**
     * Email del paciente. Campo opcional y de contacto.
     *
     * <p>A diferencia de {@link Profesional}, el email del paciente NO es único
     * porque el paciente no usa el sistema (no es un login).</p>
     */
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 150, message = "El email no puede superar los 150 caracteres")
    @Column(name = "email", length = 150)
    private String email;

    /** Profesión u ocupación del paciente. Campo opcional, máximo 100 caracteres. */
    @Size(max = 100, message = "La profesión no puede superar los 100 caracteres")
    @Column(name = "profesion", length = 100)
    private String profesion;

    /** Domicilio del paciente. Campo opcional, máximo 255 caracteres. */
    @Size(max = 255, message = "El domicilio no puede superar los 255 caracteres")
    @Column(name = "domicilio")
    private String domicilio;

    /** Nombre de la obra social del paciente. Campo opcional, máximo 100 caracteres. */
    @Size(max = 100, message = "La obra social no puede superar los 100 caracteres")
    @Column(name = "obra_social", length = 100)
    private String obraSocial;

    /** Número de afiliado de la obra social. Campo opcional, máximo 50 caracteres. */
    @Size(max = 50, message = "El número de obra social no puede superar los 50 caracteres")
    @Column(name = "numero_obra_social", length = 50)
    private String numeroObraSocial;

    // ========================
    // CONTACTO DE EMERGENCIA
    // ========================

    /** Nombre del contacto de emergencia. Campo opcional, máximo 100 caracteres. */
    @Size(max = 100, message = "El nombre del contacto de emergencia no puede superar los 100 caracteres")
    @Column(name = "contacto_emergencia_nombre", length = 100)
    private String contactoEmergenciaNombre;

    /** Teléfono del contacto de emergencia. Campo opcional, máximo 20 caracteres. */
    @Size(max = 20, message = "El teléfono del contacto de emergencia no puede superar los 20 caracteres")
    @Column(name = "contacto_emergencia_telefono", length = 20)
    private String contactoEmergenciaTelefono;

    /** Parentesco del contacto de emergencia (ej: "Madre", "Esposo"). Campo opcional, máximo 50 caracteres. */
    @Size(max = 50, message = "El parentesco no puede superar los 50 caracteres")
    @Column(name = "contacto_emergencia_parentesco", length = 50)
    private String contactoEmergenciaParentesco;

    // ========================
    // ENTIDADES DE TRASLADO
    // ========================

    /** Primera entidad de traslado en caso de emergencia. Campo opcional, máximo 100 caracteres. */
    @Size(max = 100, message = "La entidad de traslado no puede superar los 100 caracteres")
    @Column(name = "entidad_traslado_1", length = 100)
    private String entidadTraslado1;

    /** Segunda entidad de traslado en caso de emergencia. Campo opcional, máximo 100 caracteres. */
    @Size(max = 100, message = "La entidad de traslado no puede superar los 100 caracteres")
    @Column(name = "entidad_traslado_2", length = 100)
    private String entidadTraslado2;

    // ========================
    // ESTADO
    // ========================

    /**
     * Indica si el paciente está activo o archivado (soft delete).
     *
     * <p>Por defecto {@code true} al crear. Cuando se "elimina" un paciente, se setea en
     * {@code false} en lugar de borrar el registro, preservando historial clínico, turnos y pagos.</p>
     */
    @Builder.Default
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    // ========================
    // CAMPOS DE AUDITORÍA
    // ========================

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

