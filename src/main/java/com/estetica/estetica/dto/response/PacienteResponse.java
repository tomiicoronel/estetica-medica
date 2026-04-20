package com.estetica.estetica.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de salida que representa los datos de un paciente devueltos por la API.
 *
 * <p>Incluye el {@code id} generado por el sistema, todos los campos relevantes
 * del paciente, el {@code profesionalId} para identificar a qué profesional pertenece,
 * y las fechas de auditoría.</p>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-20
 * @see com.estetica.estetica.dto.request.PacienteRequest
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacienteResponse {

    /** Identificador único del paciente (UUID). */
    private UUID id;

    /** UUID de la profesional que atiende a este paciente. */
    private UUID profesionalId;

    /** Nombre del paciente. */
    private String nombre;

    /** Apellido del paciente. */
    private String apellido;

    /** DNI o CUIT del paciente. */
    private String dniCuit;

    /** Fecha de nacimiento. */
    private LocalDate fechaNacimiento;

    /** Teléfono de contacto. */
    private String telefono;

    /** Email del paciente. */
    private String email;

    /** Profesión u ocupación. */
    private String profesion;

    /** Domicilio. */
    private String domicilio;

    /** Obra social. */
    private String obraSocial;

    /** Número de afiliado de la obra social. */
    private String numeroObraSocial;

    /** Nombre del contacto de emergencia. */
    private String contactoEmergenciaNombre;

    /** Teléfono del contacto de emergencia. */
    private String contactoEmergenciaTelefono;

    /** Parentesco del contacto de emergencia. */
    private String contactoEmergenciaParentesco;

    /** Primera entidad de traslado. */
    private String entidadTraslado1;

    /** Segunda entidad de traslado. */
    private String entidadTraslado2;

    /** Indica si el paciente está activo o archivado. */
    private Boolean activo;

    /** Fecha y hora de creación del registro. */
    private LocalDateTime creadoEn;

    /** Fecha y hora de la última actualización. */
    private LocalDateTime actualizadoEn;
}

