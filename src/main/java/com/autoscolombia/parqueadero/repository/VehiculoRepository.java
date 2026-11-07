package com.autoscolombia.parqueadero.repository;

import com.autoscolombia.parqueadero.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {
    Optional<Vehiculo> findByPlacaIgnoreCase(String placa);
    List<Vehiculo> findByCliente_IdClienteOrderByPlacaAsc(Integer idCliente);
}
