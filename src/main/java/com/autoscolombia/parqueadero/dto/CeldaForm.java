package com.autoscolombia.parqueadero.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CeldaForm {
    private Integer id;

    @NotBlank(message = "El código de la celda es obligatorio")
    @Size(max = 10, message = "El código no puede superar 10 caracteres")
    private String codigo;

    @NotNull(message = "Seleccione el tipo de vehículo")
    private Integer idTipo;

    /** DISPONIBLE o INACTIVA (solo se usa al editar). */
    private String estado = "DISPONIBLE";
}
