package com.cajero.exception;

public class CuentaInactivaException extends Exception {

    public CuentaInactivaException(String numeroCuenta) {
        super("La cuenta " + numeroCuenta + " esta inactiva. No se pueden realizar operaciones.");
    }
}
