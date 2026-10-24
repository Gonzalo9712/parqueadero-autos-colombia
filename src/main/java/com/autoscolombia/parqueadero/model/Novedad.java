package com.autoscolombia.parqueadero.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "novedad")
@Getter @Setter @NoArgsConstructor
public class Novedad {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_novedad")
    private Integer idNovedad;

    @Column(nullable = false, length = 30)
    private String tipo; // DANO, OBJETO_OLVIDADO, INCIDENTE

    @Column(nullable = false, length = 1000)
    private String descripcion;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_registro")
    private RegistroAcceso registro;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario_sistema")
    private UsuarioSistema usuario;
}
