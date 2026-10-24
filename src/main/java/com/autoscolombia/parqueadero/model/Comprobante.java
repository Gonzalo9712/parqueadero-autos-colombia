package com.autoscolombia.parqueadero.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comprobante")
@Getter @Setter @NoArgsConstructor
public class Comprobante {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comprobante")
    private Integer idComprobante;

    @Column(nullable = false, unique = true)
    private Integer consecutivo;

    @Column(nullable = false, length = 10)
    private String tipo; // ENTRADA, SALIDA

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_registro")
    private RegistroAcceso registro;
}
