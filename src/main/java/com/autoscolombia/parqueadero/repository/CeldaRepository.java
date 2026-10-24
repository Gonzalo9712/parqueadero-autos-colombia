package com.autoscolombia.parqueadero.repository;

import com.autoscolombia.parqueadero.model.Celda;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import java.util.Optional;

public interface CeldaRepository extends JpaRepository<Celda, Integer> {

    /** Bloqueo pesimista: evita que dos entradas simultáneas tomen la misma celda (RNF-05). */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Celda> findFirstByEstadoAndTipo_IdTipoOrderByCodigoAsc(String estado, Integer idTipo);
}
