package com.autoscolombia.parqueadero.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehiculo")
@Getter @Setter @NoArgsConstructor
public class Vehiculo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehiculo")
    private Integer idVehiculo;

    @Column(nullable = false, unique = true, length = 10)
    private String placa;

    @Column(length = 50)
    private String marca;

    @Column(length = 30)
    private String color;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_tipo")
    private TipoVehiculo tipo;
}
