package com.estetica.estetica.repository;

import com.estetica.estetica.model.TurnoServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repositorio de acceso a datos para la entidad intermedia {@link TurnoServicio}.
 *
 * <p>Proporciona las operaciones CRUD estándar heredadas de {@link JpaRepository}
 * y una búsqueda por turno.</p>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-24
 */
@Repository
public interface TurnoServicioRepository extends JpaRepository<TurnoServicio, UUID> {

    /** Lista todas las líneas de servicios incluidas en un turno. */
    List<TurnoServicio> findByTurnoId(UUID turnoId);
}

