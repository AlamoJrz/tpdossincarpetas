package com.cajero.exception;

public class SaldoInsuficienteException extends Exception {

    public SaldoInsuficienteException(double saldoActual, double montoSolicitado) {
        super("Saldo insuficiente. Saldo actual: " + String.format("$%,.2f", saldoActual) +
              " | Monto solicitado: " + String.format("$%,.2f", montoSolicitado));
    }
}
