package com.estetica.estetica.repository;

import com.estetica.estetica.model.Profesional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio de acceso a datos para la entidad {@link Profesional}.
 *
 * <p>Extiende {@link JpaRepository}, lo que proporciona automáticamente los métodos CRUD estándar:</p>
 * <ul>
 *     <li>{@code save(entity)} — Crear o actualizar una profesional.</li>
 *     <li>{@code findById(id)} — Buscar por UUID.</li>
 *     <li>{@code findAll()} — Listar todas las profesionales.</li>
 *     <li>{@code deleteById(id)} — Eliminar por UUID.</li>
 *     <li>{@code existsById(id)} — Verificar existencia por UUID.</li>
 *     <li>{@code count()} — Contar el total de registros.</li>
 * </ul>
 *
 * <p>Además define métodos custom de consulta por email. Spring Data JPA los implementa
 * automáticamente analizando el nombre del método (Query Method Derivation).</p>
 *
 * <p>{@code @Repository} marca la interfaz como componente de Spring y habilita la traducción
 * automática de excepciones de persistencia a excepciones de Spring ({@code DataAccessException}).</p>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-14
 * @see Profesional
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
@Repository
public interface ProfesionalRepository extends JpaRepository<Profesional, UUID> {

    /**
     * Busca una profesional por su dirección de email.
     *
     * <p>Retorna un {@link Optional} vacío si no existe ninguna profesional con ese email.
     * Útil para el futuro sistema de autenticación donde el email será el login.</p>
     *
     * @param email el email a buscar (case-sensitive)
     * @return un {@link Optional} con la profesional encontrada, o vacío si no existe
     */
    Optional<Profesional> findByEmail(String email);

    /**
     * Verifica si ya existe una profesional con el email especificado.
     *
     * <p>Se usa antes de crear o actualizar una profesional para garantizar
     * la unicidad del email sin necesidad de capturar una excepción de constraint.</p>
     *
     * @param email el email a verificar
     * @return {@code true} si ya existe una profesional con ese email, {@code false} en caso contrario
     */
    boolean existsByEmail(String email);
}
