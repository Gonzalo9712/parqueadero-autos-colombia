package com.autoscolombia.parqueadero.repository;

import com.autoscolombia.parqueadero.model.TipoVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TipoVehiculoRepository extends JpaRepository<TipoVehiculo, Integer> {
    Optional<TipoVehiculo> findByNombre(String nombre);
}
