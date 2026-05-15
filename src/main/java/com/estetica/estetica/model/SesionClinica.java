package com.estetica.estetica.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "sesiones_clinicas",
        uniqueConstraints = @UniqueConstraint(name = "uk_sesion_clinica_turno", columnNames = "turno_id")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "SesionClinica", description = "Entidad JPA que registra lo realizado en una sesión clínica asociada a un turno realizado.")
public class SesionClinica {

    @Schema(description = "Identificador único UUID de la sesión clínica", example = "910e8400-e29b-41d4-a716-446655440000")
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Schema(description = "Turno realizado al que pertenece esta sesión clínica")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "turno_id", nullable = false, unique = true)
    private Turno turno;

    @Schema(description = "Número correlativo de sesión dentro del tratamiento del paciente", example = "1")
    @Column(name = "numero_sesion", nullable = false)
    private Integer numeroSesion;

    @Schema(description = "Descripción de qué se hizo en la sesión")
    @Column(name = "tratamiento", nullable = false, columnDefinition = "TEXT")
    private String tratamiento;

    @Schema(description = "Cómo respondió y toleró el paciente el tratamiento")
    @Column(name = "respuesta_tolerancia", nullable = false, columnDefinition = "TEXT")
    private String respuestaTolerancia;

    @Schema(description = "Observaciones opcionales de la sesión")
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Schema(description = "Fecha y hora de creación")
    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @Schema(description = "Fecha y hora de última actualización")
    @UpdateTimestamp
    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
}

