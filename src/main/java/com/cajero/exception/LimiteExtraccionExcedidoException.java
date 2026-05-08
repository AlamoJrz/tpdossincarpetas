package com.cajero.exception;

public class LimiteExtraccionExcedidoException extends Exception {

    public LimiteExtraccionExcedidoException(double montoIntentado) {
        super("Limite de extraccion excedido. El maximo por operacion es $10,000.00" +
              " | Monto ingresado: " + String.format("$%,.2f", montoIntentado));
    }
}
