package com.autoscolombia.parqueadero.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NovedadForm {
    @NotBlank(message = "La placa es obligatoria")
    private String placa;

    @NotBlank(message = "El tipo de novedad es obligatorio")
    private String tipo;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;
}
