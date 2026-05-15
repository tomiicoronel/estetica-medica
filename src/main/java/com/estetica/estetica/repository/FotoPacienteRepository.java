package com.estetica.estetica.repository;

import com.estetica.estetica.model.FotoPaciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FotoPacienteRepository extends JpaRepository<FotoPaciente, UUID> {

    List<FotoPaciente> findByPacienteId(UUID pacienteId);

    List<FotoPaciente> findBySesionClinicaId(UUID sesionClinicaId);

    @Modifying
    @Query("DELETE FROM FotoPaciente f WHERE f.id = :id")
    int eliminarPorId(@Param("id") UUID id);
}

