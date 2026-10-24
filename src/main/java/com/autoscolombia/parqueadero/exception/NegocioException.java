package com.autoscolombia.parqueadero.exception;

/** Error de regla de negocio (placa sin mensualidad, sin celdas, etc.). */
public class NegocioException extends RuntimeException {
    public NegocioException(String mensaje) {
        super(mensaje);
    }
}
