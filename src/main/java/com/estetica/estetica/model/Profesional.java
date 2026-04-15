package com.estetica.estetica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidad JPA que representa a una profesional del centro de estética médica.
 *
 * <p>Se mapea a la tabla {@code profesionales} en PostgreSQL. Hibernate crea y actualiza
 * la tabla automáticamente gracias a la propiedad {@code spring.jpa.hibernate.ddl-auto=update}.</p>
 *
 * <h3>Anotaciones de Lombok utilizadas:</h3>
 * <ul>
 *     <li>{@code @Getter / @Setter} — Genera automáticamente los getters y setters de todos los campos.</li>
 *     <li>{@code @NoArgsConstructor} — Constructor vacío requerido por JPA.</li>
 *     <li>{@code @AllArgsConstructor} — Constructor con todos los campos, utilizado internamente por el Builder.</li>
 *     <li>{@code @Builder} — Permite construir instancias con el patrón Builder: {@code Profesional.builder().nombre("Ana").build()}.</li>
 * </ul>
 *
 * <h3>Campos de auditoría:</h3>
 * <p>{@code creadoEn} y {@code actualizadoEn} se llenan automáticamente mediante
 * los callbacks de JPA {@code @PrePersist} y {@code @PreUpdate}.</p>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-14
 * @see com.estetica.estetica.repository.ProfesionalRepository
 */
@Entity
@Table(name = "profesionales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profesional {

    /**
     * Identificador único de la profesional.
     *
     * <p>Se genera automáticamente como UUID v4 al persistir la entidad.
     * Se usa UUID en lugar de Long autoincremental por seguridad: los IDs
     * secuenciales son predecibles y vulnerables a ataques de enumeración (IDOR).</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /** Nombre de la profesional. Campo obligatorio, máximo 100 caracteres. */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    /** Apellido de la profesional. Campo obligatorio, máximo 100 caracteres. */
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede superar los 100 caracteres")
    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    /**
     * Email de la profesional. Identificador natural y futuro login.
     *
     * <p>Es obligatorio, debe tener formato de email válido y es único
     * en la base de datos (no puede haber dos profesionales con el mismo email).</p>
     */
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 150, message = "El email no puede superar los 150 caracteres")
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    /** Teléfono de contacto de la profesional. Campo obligatorio, máximo 20 caracteres. */
    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
    @Column(name = "telefono", nullable = false, length = 20)
    private String telefono;

    /** Especialidad médica de la profesional (ej: "Dermatología"). Campo opcional, máximo 100 caracteres. */
    @Size(max = 100, message = "La especialidad no puede superar los 100 caracteres")
    @Column(name = "especialidad", length = 100)
    private String especialidad;

    /**
     * Lista de servicios ofrecidos por esta profesional.
     *
     * <p>Relación {@code OneToMany} bidireccional mapeada por el campo {@code profesional} de la entidad
     * {@link Servicio}. {@code cascade = ALL} propaga todas las operaciones (persist, merge, remove)
     * a los servicios asociados. {@code orphanRemoval = true} elimina automáticamente los servicios
     * que se desvinculen de la profesional.</p>
     */
    @OneToMany(mappedBy = "profesional", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Servicio> servicios = new ArrayList<>();

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
