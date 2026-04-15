package com.estetica.estetica.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO (Data Transfer Object) de salida que representa los datos de un servicio devueltos por la API.
 *
 * <p>Contiene los datos que el servidor envía al cliente en las respuestas de los endpoints.
 * Incluye el {@code id} (para que el cliente sepa cuál se creó o consultó), el {@code profesionalId}
 * (para identificar a qué profesional pertenece) y las fechas de auditoría.</p>
 *
 * <p>Este DTO desacopla la entidad JPA de la respuesta HTTP. Devuelve solo el {@code profesionalId}
 * en lugar del objeto {@link com.estetica.estetica.model.Profesional} completo, evitando
 * referencias circulares en la serialización JSON y reduciendo el tamaño de la respuesta.</p>
 *
 * <h3>Ejemplo de respuesta JSON:</h3>
 * <pre>{@code
 * {
 *     "id": "660e8400-e29b-41d4-a716-446655440001",
 *     "profesionalId": "550e8400-e29b-41d4-a716-446655440000",
 *     "nombre": "Limpieza facial profunda",
 *     "descripcion": "Tratamiento de limpieza facial con extracción de comedones",
 *     "precio": 15000.00,
 *     "activo": true,
 *     "creadoEn": "2026-04-15T10:30:00",
 *     "actualizadoEn": "2026-04-15T10:30:00"
 * }
 * }</pre>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-15
 * @see com.estetica.estetica.dto.request.ServicioRequest
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicioResponse {

    /** Identificador único UUID del servicio. */
    private UUID id;

    /** UUID de la profesional a la que pertenece este servicio. */
    private UUID profesionalId;

    /** Nombre del servicio (ej: "Limpieza facial profunda"). */
    private String nombre;

    /** Descripción detallada del servicio. */
    private String descripcion;

    /** Precio del servicio en la moneda local. */
    private BigDecimal precio;

    /** Indica si el servicio está activo y disponible para ser agendado. */
    private Boolean activo;

    /** Fecha y hora en que se creó el registro en la base de datos. */
    private LocalDateTime creadoEn;

    /** Fecha y hora de la última actualización del registro. */
    private LocalDateTime actualizadoEn;
}

