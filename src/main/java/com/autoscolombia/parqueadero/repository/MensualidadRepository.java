package com.autoscolombia.parqueadero.repository;

import com.autoscolombia.parqueadero.model.Mensualidad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MensualidadRepository extends JpaRepository<Mensualidad, Integer> {
    List<Mensualidad> findByCliente_IdCliente(Integer idCliente);
}
