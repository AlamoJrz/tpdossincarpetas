package com.cajero.model;

import java.util.ArrayList;

public class CuentaBancaria {

    // final: el numero de cuenta no puede cambiar una vez creado
    private final String numeroCuenta;
    private double saldo;
    private String titular;
    private boolean activa;
    private ArrayList<Transaccion> historialTransacciones;

    public CuentaBancaria(String numeroCuenta, double saldo, String titular) {
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldo;
        this.titular = titular;
        this.activa = true;
        this.historialTransacciones = new ArrayList<>();
    }

    // Getters
    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public double getSaldo() {
        return saldo;
    }

    public String getTitular() {
        return titular;
    }

    public boolean isActiva() {
        return activa;
    }

    public ArrayList<Transaccion> getHistorialTransacciones() {
        return historialTransacciones;
    }

    // Setters (numeroCuenta no tiene setter porque es final)
    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    // Guarda la transaccion, maximo 10 en el historial
    public void registrarTransaccion(Transaccion transaccion) {
        historialTransacciones.add(transaccion);
        if (historialTransacciones.size() > 10) {
            historialTransacciones.remove(0);
        }
    }

    @Override
    public String toString() {
        return "Cuenta: " + numeroCuenta + " | Titular: " + titular +
               " | Saldo: " + String.format("$%,.2f", saldo) +
               " | Estado: " + (activa ? "ACTIVA" : "INACTIVA");
    }
}
