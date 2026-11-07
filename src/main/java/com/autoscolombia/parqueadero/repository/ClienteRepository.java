package com.autoscolombia.parqueadero.repository;

import com.autoscolombia.parqueadero.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    Optional<Cliente> findByDocumento(String documento);

    List<Cliente> findAllByOrderByApellidosAscNombresAsc();

    @Query("select c from Cliente c " +
           "where lower(c.documento) like lower(concat('%', :q, '%')) " +
           "or lower(c.nombres) like lower(concat('%', :q, '%')) " +
           "or lower(c.apellidos) like lower(concat('%', :q, '%')) " +
           "order by c.apellidos, c.nombres")
    List<Cliente> buscar(@Param("q") String q);
}
