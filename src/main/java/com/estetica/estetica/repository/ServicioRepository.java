package com.estetica.estetica.repository;

import com.estetica.estetica.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Repositorio de acceso a datos para la entidad {@link Servicio}.
 *
 * <p>Extiende {@link JpaRepository}, lo que proporciona automáticamente los métodos CRUD estándar:</p>
 * <ul>
 *     <li>{@code save(entity)} — Crear o actualizar un servicio.</li>
 *     <li>{@code findById(id)} — Buscar por UUID.</li>
 *     <li>{@code findAll()} — Listar todos los servicios.</li>
 *     <li>{@code deleteById(id)} — Eliminar por UUID.</li>
 *     <li>{@code existsById(id)} — Verificar existencia por UUID.</li>
 *     <li>{@code count()} — Contar el total de registros.</li>
 * </ul>
 *
 * <p>Además define métodos custom de consulta. Spring Data JPA los implementa
 * automáticamente analizando el nombre del método (Query Method Derivation).</p>
 *
 * <p>{@code @Repository} marca la interfaz como componente de Spring y habilita la traducción
 * automática de excepciones de persistencia a excepciones de Spring ({@code DataAccessException}).</p>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-15
 * @see Servicio
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
@Repository
public interface ServicioRepository extends JpaRepository<Servicio, UUID> {

    /**
     * Lista todos los servicios que pertenecen a una profesional específica.
     *
     * <p>Spring Data JPA genera automáticamente la query {@code SELECT * FROM servicios WHERE profesional_id = ?}
     * analizando el nombre del método: {@code findBy} + {@code Profesional} (la relación) + {@code Id} (el campo).</p>
     *
     * @param profesionalId el UUID de la profesional cuyos servicios se quieren listar
     * @return lista de servicios de la profesional, vacía si no tiene ninguno
     */
    List<Servicio> findByProfesionalId(UUID profesionalId);

    /**
     * Lista todos los servicios activos de una profesional específica.
     *
     * <p>Útil para mostrar solo los servicios disponibles para agendar,
     * excluyendo los que fueron desactivados (soft delete).</p>
     *
     * @param profesionalId el UUID de la profesional
     * @param activo        {@code true} para servicios activos, {@code false} para inactivos
     * @return lista de servicios filtrados por profesional y estado
     */
    List<Servicio> findByProfesionalIdAndActivo(UUID profesionalId, Boolean activo);

    /**
     * Verifica si ya existe un servicio con el mismo nombre para una profesional específica.
     *
     * <p>Se usa antes de crear un servicio para evitar duplicados dentro de la misma profesional.
     * Dos profesionales distintas pueden tener servicios con el mismo nombre, pero una misma
     * profesional no debería tener dos servicios llamados igual.</p>
     *
     * @param nombre        el nombre del servicio a verificar
     * @param profesionalId el UUID de la profesional
     * @return {@code true} si ya existe un servicio con ese nombre para esa profesional
     */
    boolean existsByNombreAndProfesionalId(String nombre, UUID profesionalId);

    /**
     * Actualiza el precio de un servicio directamente en la base de datos.
     *
     * <p>Se usa {@code @Modifying} junto con {@code @Query} porque Spring Data JPA no puede
     * generar queries de UPDATE automáticamente a partir del nombre del método (solo soporta
     * SELECT y DELETE por derivación). La query JPQL modifica el campo {@code precio}
     * y también actualiza {@code actualizadoEn} con la fecha actual para mantener la auditoría.</p>
     *
     * <p>{@code @Modifying} indica que esta query modifica datos (no es un SELECT).
     * Es obligatorio para queries UPDATE o DELETE con {@code @Query}.</p>
     *
     * @param id          el UUID del servicio cuyo precio se quiere actualizar
     * @param nuevoPrecio el nuevo precio a establecer
     * @return la cantidad de registros actualizados (0 si no existe el servicio, 1 si se actualizó)
     */
    @Modifying
    @Query("UPDATE Servicio s SET s.precio = :nuevoPrecio, s.actualizadoEn = CURRENT_TIMESTAMP WHERE s.id = :id")
    int actualizarPrecio(@Param("id") UUID id, @Param("nuevoPrecio") BigDecimal nuevoPrecio);

    /**
     * Cambia el estado activo/inactivo de un servicio directamente en la base de datos.
     *
     * <p>Se usa para implementar <b>soft delete</b>: en lugar de eliminar el servicio,
     * se desactiva seteando {@code activo = false}. Así se mantiene el historial
     * (turnos pasados siguen referenciando al servicio) pero ya no aparece como disponible.</p>
     *
     * <p>También permite reactivar un servicio seteando {@code activo = true}.</p>
     *
     * @param id     el UUID del servicio a activar/desactivar
     * @param activo {@code true} para activar, {@code false} para desactivar
     * @return la cantidad de registros actualizados (0 si no existe el servicio, 1 si se actualizó)
     */
    @Modifying
    @Query("UPDATE Servicio s SET s.activo = :activo, s.actualizadoEn = CURRENT_TIMESTAMP WHERE s.id = :id")
    int cambiarEstado(@Param("id") UUID id, @Param("activo") Boolean activo);
}




