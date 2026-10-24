package com.autoscolombia.parqueadero.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipo_vehiculo")
@Getter @Setter @NoArgsConstructor
public class TipoVehiculo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo")
    private Integer idTipo;

    @Column(nullable = false, unique = true, length = 20)
    private String nombre;

    public TipoVehiculo(String nombre) { this.nombre = nombre; }
}
