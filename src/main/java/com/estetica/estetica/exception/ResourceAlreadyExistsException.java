package com.estetica.estetica.exception;

/**
 * Excepción lanzada cuando se intenta crear un recurso que ya existe y por reglas
 * de negocio no puede duplicarse (relaciones 1:1, unicidad lógica, etc.).
 *
 * <p>El {@link GlobalExceptionHandler} la traduce a {@code 409 Conflict}.</p>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-22
 */
public class ResourceAlreadyExistsException extends RuntimeException {

    public ResourceAlreadyExistsException(String mensaje) {
        super(mensaje);
    }
}

