package com.estetica.estetica.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO (Data Transfer Object) de salida que representa los datos de una profesional devueltos por la API.
 *
 * <p>Contiene los datos que el servidor envía al cliente en las respuestas de los endpoints.
 * Incluye el {@code id} (para que el cliente sepa cuál se creó o consultó) y las fechas
 * de auditoría ({@code creadoEn}, {@code actualizadoEn}).</p>
 *
 * <p>Este DTO desacopla la entidad JPA de la respuesta HTTP. Si en el futuro la entidad
 * cambia (por ejemplo, se agrega un campo interno que no debe exponerse), la API
 * no se ve afectada.</p>
 *
 * <h3>Ejemplo de respuesta JSON:</h3>
 * <pre>{@code
 * {
 *     "id": "550e8400-e29b-41d4-a716-446655440000",
 *     "nombre": "María",
 *     "apellido": "García",
 *     "email": "maria@email.com",
 *     "telefono": "1155443322",
 *     "especialidad": "Dermatología",
 *     "creadoEn": "2026-04-14T10:30:00",
 *     "actualizadoEn": "2026-04-14T10:30:00"
 * }
 * }</pre>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-14
 * @see com.estetica.estetica.dto.request.ProfesionalRequest
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfesionalResponse {

    /** Identificador único UUID de la profesional. */
    private UUID id;

    /** Nombre de la profesional. */
    private String nombre;

    /** Apellido de la profesional. */
    private String apellido;

    /** Email de la profesional (identificador natural). */
    private String email;

    /** Teléfono de contacto de la profesional. */
    private String telefono;

    /** Especialidad médica de la profesional. Puede ser {@code null} si no se especificó. */
    private String especialidad;

    /** Fecha y hora en que se creó el registro en la base de datos. */
    private LocalDateTime creadoEn;

    /** Fecha y hora de la última actualización del registro. */
    private LocalDateTime actualizadoEn;
}
