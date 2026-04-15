package com.estetica.estetica.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO (Data Transfer Object) de entrada para crear o actualizar un servicio.
 *
 * <p>Contiene los datos que el cliente envía en el body de las peticiones POST y PUT.
 * No incluye {@code id} (lo genera el sistema), {@code activo} (por defecto {@code true}
 * al crear) ni fechas de auditoría (las maneja JPA automáticamente).</p>
 *
 * <p>Las anotaciones de validación ({@code @NotBlank}, {@code @NotNull}, {@code @DecimalMin}, {@code @Size})
 * se activan cuando el controller recibe el request con {@code @Valid}.
 * Si alguna validación falla, Spring lanza {@code MethodArgumentNotValidException}
 * que es capturada por el {@link com.estetica.estetica.exception.GlobalExceptionHandler}.</p>
 *
 * <h3>Ejemplo de uso en JSON:</h3>
 * <pre>{@code
 * {
 *     "profesionalId": "550e8400-e29b-41d4-a716-446655440000",
 *     "nombre": "Limpieza facial profunda",
 *     "descripcion": "Tratamiento de limpieza facial con extracción de comedones",
 *     "precio": 15000.00
 * }
 * }</pre>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-15
 * @see com.estetica.estetica.dto.response.ServicioResponse
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicioRequest {

    /**
     * UUID de la profesional a la que pertenece el servicio.
     *
     * <p>Es obligatorio. El servicio necesita estar asociado a una profesional existente.
     * La existencia de la profesional se valida en la capa de servicio
     * ({@link com.estetica.estetica.service.ServicioService}).</p>
     */
    @NotNull(message = "El ID de la profesional es obligatorio")
    private UUID profesionalId;

    /** Nombre del servicio. Obligatorio, no puede estar vacío ni superar los 150 caracteres. */
    @NotBlank(message = "El nombre del servicio es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
    private String nombre;

    /** Descripción detallada del servicio. Obligatorio, no puede estar vacío. */
    @NotBlank(message = "La descripción del servicio es obligatoria")
    private String descripcion;

    /**
     * Precio del servicio en la moneda local.
     *
     * <p>Es obligatorio y debe ser mayor a 0. Se usa {@code @DecimalMin} con {@code inclusive = false}
     * para rechazar valores de 0 o negativos (un servicio gratuito no tiene sentido en este sistema).</p>
     */
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal precio;
}

