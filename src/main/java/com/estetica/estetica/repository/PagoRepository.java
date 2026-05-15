package com.estetica.estetica.repository;

import com.estetica.estetica.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface PagoRepository extends JpaRepository<Pago, UUID> {

    List<Pago> findByTurnoId(UUID turnoId);

    List<Pago> findByTurnoIdOrderByFechaAsc(UUID turnoId);

    List<Pago> findByTurno_Profesional_Id(UUID profesionalId);

    @Query("SELECT SUM(p.monto) FROM Pago p WHERE p.turno.id = :turnoId")
    BigDecimal sumMontoByTurnoId(@Param("turnoId") UUID turnoId);
}


