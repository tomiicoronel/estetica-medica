package com.estetica.estetica.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO (Data Transfer Object) de entrada para crear o actualizar una profesional.
 *
 * <p>Contiene los datos que el cliente envía en el body de las peticiones POST y PUT.
 * No incluye {@code id} (lo genera el sistema) ni fechas de auditoría
 * (las maneja JPA automáticamente).</p>
 *
 * <p>Las anotaciones de validación ({@code @NotBlank}, {@code @Email}, {@code @Size})
 * se activan cuando el controller recibe el request con {@code @Valid}.
 * Si alguna validación falla, Spring lanza {@code MethodArgumentNotValidException}
 * que es capturada por el {@link com.estetica.estetica.exception.GlobalExceptionHandler}.</p>
 *
 * <h3>Ejemplo de uso en JSON:</h3>
 * <pre>{@code
 * {
 *     "nombre": "María",
 *     "apellido": "García",
 *     "email": "maria@email.com",
 *     "telefono": "1155443322",
 *     "especialidad": "Dermatología"
 * }
 * }</pre>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-14
 * @see com.estetica.estetica.dto.response.ProfesionalResponse
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfesionalRequest {

    /** Nombre de la profesional. Obligatorio, no puede estar vacío ni superar los 100 caracteres. */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    /** Apellido de la profesional. Obligatorio, no puede estar vacío ni superar los 100 caracteres. */
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede superar los 100 caracteres")
    private String apellido;

    /**
     * Email de la profesional. Obligatorio, debe tener formato válido y ser único en el sistema.
     *
     * <p>La unicidad no se valida con anotaciones sino en la capa de servicio
     * ({@link com.estetica.estetica.service.ProfesionalService}).</p>
     */
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 150, message = "El email no puede superar los 150 caracteres")
    private String email;

    /** Teléfono de contacto. Obligatorio, máximo 20 caracteres. */
    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
    private String telefono;

    /** Especialidad médica. Campo opcional, máximo 100 caracteres. */
    @Size(max = 100, message = "La especialidad no puede superar los 100 caracteres")
    private String especialidad;
}
