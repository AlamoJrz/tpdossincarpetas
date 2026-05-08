package com.cajero.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaccion {

    private TipoTransaccion tipo;
    private double monto;
    private LocalDateTime fechaHora;
    private String descripcion;
    private double saldoResultante;

    public Transaccion(TipoTransaccion tipo, double monto, String descripcion, double saldoResultante) {
        this.tipo = tipo;
        this.monto = monto;
        this.fechaHora = LocalDateTime.now();
        this.descripcion = descripcion;
        this.saldoResultante = saldoResultante;
    }

    public TipoTransaccion getTipo() {
        return tipo;
    }

    public double getMonto() {
        return monto;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getSaldoResultante() {
        return saldoResultante;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(fechaHora.format(formatter)).append("] ");
        sb.append(tipo).append(": ");
        sb.append(String.format("$%,.2f", monto));
        sb.append(" | Saldo: ").append(String.format("$%,.2f", saldoResultante));
        sb.append(" | ").append(descripcion);
        return sb.toString();
    }
}
