package com.cajero;

import com.cajero.exception.CuentaInactivaException;
import com.cajero.exception.LimiteExtraccionExcedidoException;
import com.cajero.exception.SaldoInsuficienteException;
import com.cajero.model.CuentaBancaria;
import com.cajero.model.Transaccion;
import com.cajero.service.CajeroService;
import com.cajero.ui.CajeroUI;
import com.cajero.util.Formateador;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        CajeroService servicio = new CajeroService();

        // 3 cuentas con distintos saldos iniciales
        CuentaBancaria cuenta1 = new CuentaBancaria("12345678", 50000.00, "Ana Garcia");
        CuentaBancaria cuenta2 = new CuentaBancaria("87654321", 15000.00, "Bruno Lopez");
        CuentaBancaria cuenta3 = new CuentaBancaria("11223344",  3000.00, "Carla Diaz");

        servicio.agregarCuenta(cuenta1);
        servicio.agregarCuenta(cuenta2);
        servicio.agregarCuenta(cuenta3);

        System.out.println("=== SIMULACION DEL DIA ===");
        System.out.println();

        // Transaccion 1
        try {
            servicio.depositar("12345678", 5000);
            System.out.println("1. Deposito OK - Cuenta: 12345678 - Monto: $5,000.00 - Saldo: "
                    + Formateador.formatearMoneda(cuenta1.getSaldo()));
        } catch (CuentaInactivaException e) {
            System.out.println("1. Error: " + e.getMessage());
        }

        // Transaccion 2
        try {
            servicio.extraer("12345678", 2000);
            System.out.println("2. Extraccion OK - Cuenta: 12345678 - Monto: $2,000.00 - Saldo: "
                    + Formateador.formatearMoneda(cuenta1.getSaldo()));
        } catch (SaldoInsuficienteException | LimiteExtraccionExcedidoException | CuentaInactivaException e) {
            System.out.println("2. Error: " + e.getMessage());
        }

        // Transaccion 3
        try {
            double saldo = servicio.consultarSaldo("87654321");
            System.out.println("3. Consulta OK - Cuenta: 87654321 - Saldo: "
                    + Formateador.formatearMoneda(saldo));
        } catch (CuentaInactivaException e) {
            System.out.println("3. Error: " + e.getMessage());
        }

        // Transaccion 4
        try {
            servicio.transferir("12345678", "87654321", 8000);
            System.out.println("4. Transferencia OK - De: 12345678 a: 87654321 - Monto: $8,000.00");
        } catch (SaldoInsuficienteException | LimiteExtraccionExcedidoException | CuentaInactivaException e) {
            System.out.println("4. Error: " + e.getMessage());
        }

        // Transaccion 5
        try {
            servicio.depositar("11223344", 1000);
            System.out.println("5. Deposito OK - Cuenta: 11223344 - Monto: $1,000.00 - Saldo: "
                    + Formateador.formatearMoneda(cuenta3.getSaldo()));
        } catch (CuentaInactivaException e) {
            System.out.println("5. Error: " + e.getMessage());
        }

        // Transaccion 6
        try {
            servicio.extraer("87654321", 3000);
            System.out.println("6. Extraccion OK - Cuenta: 87654321 - Monto: $3,000.00 - Saldo: "
                    + Formateador.formatearMoneda(cuenta2.getSaldo()));
        } catch (SaldoInsuficienteException | LimiteExtraccionExcedidoException | CuentaInactivaException e) {
            System.out.println("6. Error: " + e.getMessage());
        }

        // Transaccion 7
        try {
            servicio.transferir("87654321", "11223344", 5000);
            System.out.println("7. Transferencia OK - De: 87654321 a: 11223344 - Monto: $5,000.00");
        } catch (SaldoInsuficienteException | LimiteExtraccionExcedidoException | CuentaInactivaException e) {
            System.out.println("7. Error: " + e.getMessage());
        }

        // Transaccion 8
        try {
            double saldo = servicio.consultarSaldo("11223344");
            System.out.println("8. Consulta OK - Cuenta: 11223344 - Saldo: "
                    + Formateador.formatearMoneda(saldo));
        } catch (CuentaInactivaException e) {
            System.out.println("8. Error: " + e.getMessage());
        }

        // Transaccion 9
        try {
            servicio.depositar("87654321", 2500);
            System.out.println("9. Deposito OK - Cuenta: 87654321 - Monto: $2,500.00 - Saldo: "
                    + Formateador.formatearMoneda(cuenta2.getSaldo()));
        } catch (CuentaInactivaException e) {
            System.out.println("9. Error: " + e.getMessage());
        }

        // Transaccion 10
        try {
            servicio.extraer("11223344", 500);
            System.out.println("10. Extraccion OK - Cuenta: 11223344 - Monto: $500.00 - Saldo: "
                    + Formateador.formatearMoneda(cuenta3.getSaldo()));
        } catch (SaldoInsuficienteException | LimiteExtraccionExcedidoException | CuentaInactivaException e) {
            System.out.println("10. Error: " + e.getMessage());
        }

        // Transaccion 11 - Error intencional: supera el limite
        System.out.println("11. Intento de extraccion de $15,000.00 (supera el limite de $10,000.00)");
        try {
            servicio.extraer("12345678", 15000);
        } catch (LimiteExtraccionExcedidoException e) {
            System.out.println("    Error: " + e.getMessage());
        } catch (SaldoInsuficienteException | CuentaInactivaException e) {
            System.out.println("    Error: " + e.getMessage());
        }

        // Transaccion 12 - Error intencional: saldo insuficiente
        System.out.println("12. Intento de extraccion de $50,000.00 con saldo insuficiente");
        try {
            servicio.extraer("11223344", 50000);
        } catch (SaldoInsuficienteException e) {
            System.out.println("    Error: " + e.getMessage());
        } catch (LimiteExtraccionExcedidoException | CuentaInactivaException e) {
            System.out.println("    Error: " + e.getMessage());
        }

        // Transaccion 13 - Error intencional: cuenta inactiva
        servicio.desactivarCuenta("11223344");
        System.out.println("13. Intento de deposito en cuenta inactiva (11223344)");
        try {
            servicio.depositar("11223344", 1000);
        } catch (CuentaInactivaException e) {
            System.out.println("    Error: " + e.getMessage());
        }

        // Transaccion 14
        try {
            servicio.transferir("12345678", "87654321", 1000);
            System.out.println("14. Transferencia OK - De: 12345678 a: 87654321 - Monto: $1,000.00");
        } catch (SaldoInsuficienteException | LimiteExtraccionExcedidoException | CuentaInactivaException e) {
            System.out.println("14. Error: " + e.getMessage());
        }

        // Transaccion 15
        try {
            servicio.depositar("12345678", 3000);
            System.out.println("15. Deposito OK - Cuenta: 12345678 - Monto: $3,000.00 - Saldo: "
                    + Formateador.formatearMoneda(cuenta1.getSaldo()));
        } catch (CuentaInactivaException e) {
            System.out.println("15. Error: " + e.getMessage());
        }

        // Historial final de las dos cuentas activas
        System.out.println();
        System.out.println("=== HISTORIAL FINAL ===");

        try {
            System.out.println();
            System.out.println("Ana Garcia (12345678):");
            ArrayList<Transaccion> h1 = servicio.obtenerHistorial("12345678");
            for (Transaccion t : h1) {
                System.out.println("  " + t.toString());
            }
        } catch (CuentaInactivaException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            System.out.println();
            System.out.println("Bruno Lopez (87654321):");
            ArrayList<Transaccion> h2 = servicio.obtenerHistorial("87654321");
            for (Transaccion t : h2) {
                System.out.println("  " + t.toString());
            }
        } catch (CuentaInactivaException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Inicio del menu interactivo
        System.out.println();
        System.out.println("=== MODO INTERACTIVO ===");
        System.out.println("Cuentas disponibles: 12345678 / 87654321");
        System.out.println();

        CajeroUI ui = new CajeroUI(servicio);
        ui.iniciar();
    }
}
