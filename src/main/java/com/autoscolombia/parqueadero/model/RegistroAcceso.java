package com.autoscolombia.parqueadero.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "registro_acceso")
@Getter @Setter @NoArgsConstructor
public class RegistroAcceso {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_registro")
    private Integer idRegistro;

    @Column(name = "fecha_hora_entrada", nullable = false)
    private LocalDateTime fechaHoraEntrada;

    @Column(name = "fecha_hora_salida")
    private LocalDateTime fechaHoraSalida;

    @Column(name = "minutos_permanencia")
    private Integer minutosPermanencia;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_vehiculo")
    private Vehiculo vehiculo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_celda")
    private Celda celda;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario_entrada")
    private UsuarioSistema usuarioEntrada;

    @ManyToOne
    @JoinColumn(name = "id_usuario_salida")
    private UsuarioSistema usuarioSalida;

    public void calcularPermanencia() {
        if (fechaHoraSalida != null) {
            this.minutosPermanencia = (int) Duration.between(fechaHoraEntrada, fechaHoraSalida).toMinutes();
        }
    }

    /** Permanencia en formato "2 h 15 min". */
    @Transient
    public String getPermanenciaTexto() {
        if (minutosPermanencia == null) return "-";
        return (minutosPermanencia / 60) + " h " + (minutosPermanencia % 60) + " min";
    }
}
