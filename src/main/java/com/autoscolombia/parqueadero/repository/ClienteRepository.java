package com.autoscolombia.parqueadero.repository;

import com.autoscolombia.parqueadero.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}
