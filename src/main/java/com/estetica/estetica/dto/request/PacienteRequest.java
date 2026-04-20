package com.estetica.estetica.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO de entrada para crear o actualizar un paciente.
 *
 * <p>Contiene los datos que el cliente envía en el body de las peticiones POST y PUT.
 * No incluye {@code id}, {@code activo} ni fechas de auditoría (los maneja el sistema).</p>
 *
 * <p>Campos obligatorios: nombre, apellido, dniCuit, telefono, profesionalId.
 * El resto son opcionales y se pueden completar después con un PUT.</p>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-20
 * @see com.estetica.estetica.dto.response.PacienteResponse
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacienteRequest {

    /** UUID de la profesional a la que pertenece el paciente. Obligatorio. */
    @NotNull(message = "El ID de la profesional es obligatorio")
    private UUID profesionalId;

    /** Nombre del paciente. Obligatorio, máximo 100 caracteres. */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    /** Apellido del paciente. Obligatorio, máximo 100 caracteres. */
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede superar los 100 caracteres")
    private String apellido;

    /** DNI o CUIT del paciente. Obligatorio, máximo 20 caracteres. */
    @NotBlank(message = "El DNI/CUIT es obligatorio")
    @Size(max = 20, message = "El DNI/CUIT no puede superar los 20 caracteres")
    private String dniCuit;

    /** Fecha de nacimiento del paciente. Opcional. */
    private LocalDate fechaNacimiento;

    /** Teléfono de contacto. Obligatorio, máximo 20 caracteres. */
    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
    private String telefono;

    /** Email del paciente. Opcional, pero si se envía debe tener formato válido. */
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 150, message = "El email no puede superar los 150 caracteres")
    private String email;

    /** Profesión u ocupación. Opcional, máximo 100 caracteres. */
    @Size(max = 100, message = "La profesión no puede superar los 100 caracteres")
    private String profesion;

    /** Domicilio. Opcional, máximo 255 caracteres. */
    @Size(max = 255, message = "El domicilio no puede superar los 255 caracteres")
    private String domicilio;

    /** Obra social. Opcional, máximo 100 caracteres. */
    @Size(max = 100, message = "La obra social no puede superar los 100 caracteres")
    private String obraSocial;

    /** Número de afiliado de la obra social. Opcional, máximo 50 caracteres. */
    @Size(max = 50, message = "El número de obra social no puede superar los 50 caracteres")
    private String numeroObraSocial;

    /** Nombre del contacto de emergencia. Opcional, máximo 100 caracteres. */
    @Size(max = 100, message = "El nombre del contacto de emergencia no puede superar los 100 caracteres")
    private String contactoEmergenciaNombre;

    /** Teléfono del contacto de emergencia. Opcional, máximo 20 caracteres. */
    @Size(max = 20, message = "El teléfono del contacto de emergencia no puede superar los 20 caracteres")
    private String contactoEmergenciaTelefono;

    /** Parentesco del contacto de emergencia. Opcional, máximo 50 caracteres. */
    @Size(max = 50, message = "El parentesco no puede superar los 50 caracteres")
    private String contactoEmergenciaParentesco;

    /** Primera entidad de traslado. Opcional, máximo 100 caracteres. */
    @Size(max = 100, message = "La entidad de traslado no puede superar los 100 caracteres")
    private String entidadTraslado1;

    /** Segunda entidad de traslado. Opcional, máximo 100 caracteres. */
    @Size(max = 100, message = "La entidad de traslado no puede superar los 100 caracteres")
    private String entidadTraslado2;
}

