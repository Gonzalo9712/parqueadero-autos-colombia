package com.autoscolombia.parqueadero.repository;

import com.autoscolombia.parqueadero.model.RegistroAcceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RegistroAccesoRepository extends JpaRepository<RegistroAcceso, Integer> {

    Optional<RegistroAcceso> findByVehiculo_PlacaIgnoreCaseAndFechaHoraSalidaIsNull(String placa);

    List<RegistroAcceso> findByFechaHoraSalidaIsNullOrderByFechaHoraEntradaDesc();

    @Query("select r from RegistroAcceso r " +
           "where (:placa = '' or upper(r.vehiculo.placa) = upper(:placa)) " +
           "and r.fechaHoraEntrada between :desde and :hasta " +
           "order by r.fechaHoraEntrada desc")
    List<RegistroAcceso> buscarHistorial(@Param("placa") String placa,
                                         @Param("desde") LocalDateTime desde,
                                         @Param("hasta") LocalDateTime hasta);
}
