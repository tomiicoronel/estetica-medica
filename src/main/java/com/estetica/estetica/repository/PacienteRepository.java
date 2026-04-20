package com.estetica.estetica.repository;

import com.estetica.estetica.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repositorio JPA para la entidad {@link Paciente}.
 *
 * <p>Extiende {@link JpaRepository} para obtener operaciones CRUD automáticas
 * (save, findById, findAll, deleteById, etc.). Incluye métodos custom derivados
 * por Spring Data JPA a partir del nombre del método.</p>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-20
 * @see Paciente
 */
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, UUID> {

    /**
     * Lista todos los pacientes que pertenecen a una profesional específica.
     *
     * @param profesionalId UUID de la profesional
     * @return lista de pacientes de esa profesional
     */
    List<Paciente> findByProfesionalId(UUID profesionalId);

    /**
     * Verifica si ya existe un paciente con el mismo DNI/CUIT dentro de la misma profesional.
     *
     * <p>Esto garantiza que una profesional no pueda cargar dos veces al mismo paciente,
     * pero permite que distintas profesionales tengan pacientes con el mismo DNI
     * (modelo multi-tenant aislado).</p>
     *
     * @param dniCuit   DNI o CUIT del paciente
     * @param profesionalId UUID de la profesional
     * @return {@code true} si ya existe esa combinación
     */
    boolean existsByDniCuitAndProfesionalId(String dniCuit, UUID profesionalId);

    /**
     * Lista solo los pacientes activos (o inactivos) de una profesional.
     *
     * @param profesionalId UUID de la profesional
     * @param activo        {@code true} para activos, {@code false} para archivados
     * @return lista de pacientes filtrados por estado
     */
    List<Paciente> findByProfesionalIdAndActivo(UUID profesionalId, Boolean activo);

    /**
     * Cambia el estado activo/inactivo de un paciente (soft delete).
     *
     * @param id     UUID del paciente
     * @param activo nuevo estado
     * @return cantidad de filas afectadas
     */
    @Modifying
    @Query("UPDATE Paciente p SET p.activo = :activo, p.actualizadoEn = CURRENT_TIMESTAMP WHERE p.id = :id")
    int cambiarEstado(@Param("id") UUID id, @Param("activo") Boolean activo);
}

