package com.estetica.estetica.repository;

import com.estetica.estetica.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repositorio de acceso a datos para la entidad {@link Turno}.
 *
 * <p>Extiende {@link JpaRepository} para obtener las operaciones CRUD estándar
 * y define consultas derivadas para filtrar turnos por profesional, paciente,
 * estado y rango de fechas.</p>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-24
 */
@Repository
public interface TurnoRepository extends JpaRepository<Turno, UUID> {

    /** Lista todos los turnos de una profesional. */
    List<Turno> findByProfesionalId(UUID profesionalId);

    /** Lista los turnos de una profesional filtrados por estado (PENDIENTE, CONFIRMADO, etc.). */
    List<Turno> findByProfesionalIdAndEstado(UUID profesionalId, String estado);

    /** Lista todos los turnos de un paciente. */
    List<Turno> findByPacienteId(UUID pacienteId);

    /**
     * Lista los turnos de una profesional cuya {@code fechaHora} cae dentro del rango indicado.
     *
     * @param profesionalId UUID de la profesional
     * @param desde         inicio del rango (inclusive)
     * @param hasta         fin del rango (inclusive)
     * @return lista de turnos en el rango
     */
    List<Turno> findByProfesionalIdAndFechaHoraBetween(UUID profesionalId, LocalDateTime desde, LocalDateTime hasta);
}

