package com.autoscolombia.parqueadero.repository;

import com.autoscolombia.parqueadero.model.Novedad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NovedadRepository extends JpaRepository<Novedad, Integer> {
    List<Novedad> findTop20ByOrderByFechaHoraDesc();
}
