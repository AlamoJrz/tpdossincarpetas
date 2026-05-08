package com.cajero.exception;

public class PinInvalidoException extends Exception {

    public PinInvalidoException(String numeroCuenta) {
        super("PIN invalido para la cuenta " + numeroCuenta + ". Acceso denegado.");
    }
}
