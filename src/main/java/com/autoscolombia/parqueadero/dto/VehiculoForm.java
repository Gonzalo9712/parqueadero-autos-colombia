package com.autoscolombia.parqueadero.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VehiculoForm {
    @NotBlank(message = "La placa es obligatoria")
    @Pattern(regexp = "^[A-Za-z0-9]{5,10}$", message = "La placa debe tener entre 5 y 10 letras o números, sin espacios")
    private String placa;

    @NotNull(message = "Seleccione el tipo de vehículo")
    private Integer idTipo;

    @Size(max = 50)
    private String marca;

    @Size(max = 30)
    private String color;
}
