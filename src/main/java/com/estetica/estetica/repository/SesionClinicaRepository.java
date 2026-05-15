package com.estetica.estetica.repository;

import com.estetica.estetica.model.SesionClinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SesionClinicaRepository extends JpaRepository<SesionClinica, UUID> {

    Optional<SesionClinica> findByTurnoId(UUID turnoId);

    boolean existsByTurnoId(UUID turnoId);

    long countByTurno_Paciente_Id(UUID pacienteId);

    List<SesionClinica> findByTurno_Paciente_IdOrderByNumeroSesionAsc(UUID pacienteId);
}

