package com.autoscolombia.parqueadero.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "mensualidad")
@Getter @Setter @NoArgsConstructor
public class Mensualidad {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensualidad")
    private Integer idMensualidad;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(nullable = false, length = 15)
    private String estado = "ACTIVA"; // ACTIVA, VENCIDA

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    public boolean estaVigente() {
        LocalDate hoy = LocalDate.now();
        return "ACTIVA".equals(estado) && !hoy.isBefore(fechaInicio) && !hoy.isAfter(fechaFin);
    }
}
