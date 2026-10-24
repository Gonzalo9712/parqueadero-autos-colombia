package com.autoscolombia.parqueadero.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario_sistema")
@Getter @Setter @NoArgsConstructor
public class UsuarioSistema {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario_sistema")
    private Integer idUsuarioSistema;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private Boolean activo = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_rol")
    private Rol rol;
}
