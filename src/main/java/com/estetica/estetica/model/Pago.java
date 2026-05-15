package com.estetica.estetica.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pagos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Pago", description = "Entidad JPA que representa un pago parcial o total asociado a un turno.")
public class Pago {

    @Schema(description = "Identificador único UUID del pago", example = "930e8400-e29b-41d4-a716-446655440000")
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Schema(description = "Turno al que pertenece el pago")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "turno_id", nullable = false)
    private Turno turno;

    @Schema(description = "Método utilizado para registrar el pago", example = "EFECTIVO", allowableValues = {"EFECTIVO", "TRANSFERENCIA", "MERCADO_PAGO", "TRUEQUE"})
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo", nullable = false, length = 30)
    private MetodoPago metodo;

    @Schema(description = "Monto imputado al turno", example = "15000.00")
    @Column(name = "monto", nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    @Schema(description = "Indica si el pago funciona como seña anticipada", example = "false")
    @Builder.Default
    @Column(name = "es_sena", nullable = false)
    private Boolean esSena = false;

    @Schema(description = "Indica si el pago corresponde a un trueque", example = "false")
    @Builder.Default
    @Column(name = "es_trueque", nullable = false)
    private Boolean esTrueque = false;

    @Schema(description = "Detalle del bien o servicio intercambiado cuando el método es TRUEQUE", example = "Intercambio por sesión de fotografía profesional")
    @Column(name = "detalle_trueque", columnDefinition = "TEXT")
    private String detalleTrueque;

    @Schema(description = "Fecha y hora efectiva del pago", example = "2026-05-15T18:30:00")
    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Schema(description = "Fecha y hora de creación")
    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @Schema(description = "Fecha y hora de última actualización")
    @UpdateTimestamp
    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
}

