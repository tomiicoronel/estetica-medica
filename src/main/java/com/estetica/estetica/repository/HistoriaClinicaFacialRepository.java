package com.estetica.estetica.repository;

import com.estetica.estetica.model.HistoriaClinicaFacial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para la entidad {@link HistoriaClinicaFacial}.
 *
 * <p>Extiende {@link JpaRepository} para obtener operaciones CRUD automáticas
 * (incluido {@code save}, que se usa tanto para crear como para <b>editar</b> la
 * ficha: si la entidad tiene {@code id} asignado, Hibernate hace {@code UPDATE}).</p>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-22
 * @see HistoriaClinicaFacial
 */
@Repository
public interface HistoriaClinicaFacialRepository extends JpaRepository<HistoriaClinicaFacial, UUID> {

    /**
     * Busca la ficha clínica facial de un paciente.
     *
     * @param pacienteId UUID del paciente
     * @return {@link Optional} con la ficha si existe
     */
    Optional<HistoriaClinicaFacial> findByPacienteId(UUID pacienteId);

    /**
     * Verifica si un paciente ya tiene una ficha clínica facial cargada.
     *
     * <p>Se usa en el servicio para impedir la creación duplicada
     * (un paciente solo puede tener una ficha facial).</p>
     *
     * @param pacienteId UUID del paciente
     * @return {@code true} si el paciente ya tiene ficha facial
     */
    boolean existsByPacienteId(UUID pacienteId);
}

