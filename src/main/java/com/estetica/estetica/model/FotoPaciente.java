package com.estetica.estetica.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "fotos_paciente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "FotoPaciente", description = "Entidad JPA que guarda la ruta local de una foto de seguimiento vinculada a un paciente y a una sesión clínica.")
public class FotoPaciente {

    @Schema(description = "Identificador único UUID de la foto", example = "920e8400-e29b-41d4-a716-446655440000")
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Schema(description = "Paciente al que pertenece la foto")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @Schema(description = "Sesión clínica a la que pertenece la foto")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sesion_clinica_id", nullable = false)
    private SesionClinica sesionClinica;

    @Schema(description = "Ruta local donde se guarda el archivo de imagen", example = "uploads/pacientes/650e8400-e29b-41d4-a716-446655440000/sesiones/910e8400-e29b-41d4-a716-446655440000/foto-920e8400-e29b-41d4-a716-446655440000.jpg")
    @Column(name = "ruta_imagen", nullable = false, length = 500)
    private String rutaImagen;

    @Schema(description = "Fecha y hora asociada a la foto", example = "2026-05-11T15:30:00")
    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Schema(description = "Descripción opcional de la foto")
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Schema(description = "Fecha y hora de creación")
    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @Schema(description = "Fecha y hora de última actualización")
    @UpdateTimestamp
    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;

    @PrePersist
    void prePersist() {
        if (fecha == null) {
            fecha = LocalDateTime.now();
        }
    }
}

