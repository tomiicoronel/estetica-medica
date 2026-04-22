package com.estetica.estetica.repository;

import com.estetica.estetica.model.HistoriaClinicaCorporal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para la entidad {@link HistoriaClinicaCorporal}.
 *
 * <p>Extiende {@link JpaRepository} para obtener operaciones CRUD automáticas.
 * Los métodos {@code save} / {@code saveAndFlush} heredados se usan tanto para crear
 * como para actualizar (si la entidad tiene {@code id}, Hibernate genera {@code UPDATE}).</p>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-22
 * @see HistoriaClinicaCorporal
 */
@Repository
public interface HistoriaClinicaCorporalRepository extends JpaRepository<HistoriaClinicaCorporal, UUID> {

    /**
     * Busca la ficha clínica corporal de un paciente.
     *
     * @param pacienteId UUID del paciente
     * @return {@link Optional} con la ficha si existe
     */
    Optional<HistoriaClinicaCorporal> findByPacienteId(UUID pacienteId);

    /**
     * Verifica si un paciente ya tiene una ficha clínica corporal cargada.
     * Se usa en el servicio para impedir la creación duplicada.
     *
     * @param pacienteId UUID del paciente
     * @return {@code true} si el paciente ya tiene ficha corporal
     */
    boolean existsByPacienteId(UUID pacienteId);
}

