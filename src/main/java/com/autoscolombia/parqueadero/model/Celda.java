package com.autoscolombia.parqueadero.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "celda")
@Getter @Setter @NoArgsConstructor
public class Celda {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_celda")
    private Integer idCelda;

    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    @Column(nullable = false, length = 15)
    private String estado = "DISPONIBLE"; // DISPONIBLE, OCUPADA, INACTIVA

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_tipo")
    private TipoVehiculo tipo;

    public Celda(String codigo, TipoVehiculo tipo) {
        this.codigo = codigo;
        this.tipo = tipo;
    }
}
