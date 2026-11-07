package com.autoscolombia.parqueadero.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClienteForm {
    private Integer id;

    @NotBlank(message = "Seleccione el tipo de documento")
    private String tipoDocumento = "CC";

    @NotBlank(message = "El documento es obligatorio")
    @Size(max = 20, message = "El documento no puede superar 20 caracteres")
    private String documento;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 80)
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 80)
    private String apellidos;

    @Size(max = 20, message = "El teléfono no puede superar 20 caracteres")
    private String telefono;

    @Email(message = "El correo no es válido")
    @Size(max = 100)
    private String email;
}
