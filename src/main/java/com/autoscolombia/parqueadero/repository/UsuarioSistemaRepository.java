package com.autoscolombia.parqueadero.repository;

import com.autoscolombia.parqueadero.model.UsuarioSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioSistemaRepository extends JpaRepository<UsuarioSistema, Integer> {
    Optional<UsuarioSistema> findByUsername(String username);
}
