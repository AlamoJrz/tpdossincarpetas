package com.cajero.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Formateador {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Formateador() {}

    public static String formatearMoneda(double monto) {
        return String.format("$%,.2f", monto);
    }

    // Formato requerido por el TP:
    // [2024-01-15 14:30:25] EXTRACCION: $500.00 | Saldo: $2,450.00
    public static String generarLog(String tipo, double monto, double saldoResultante) {
        StringBuilder sb = new StringBuilder();
        sb.append("[")
          .append(LocalDateTime.now().format(FORMATO_FECHA))
          .append("] ")
          .append(tipo)
          .append(": ")
          .append(formatearMoneda(monto))
          .append(" | Saldo: ")
          .append(formatearMoneda(saldoResultante));
        return sb.toString();
    }
}
