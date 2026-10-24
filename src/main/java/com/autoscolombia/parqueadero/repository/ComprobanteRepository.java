package com.autoscolombia.parqueadero.repository;

import com.autoscolombia.parqueadero.model.Comprobante;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ComprobanteRepository extends JpaRepository<Comprobante, Integer> {
    Optional<Comprobante> findTopByOrderByConsecutivoDesc();
}
