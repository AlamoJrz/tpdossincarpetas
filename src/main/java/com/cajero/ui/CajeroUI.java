package com.cajero.ui;

import com.cajero.exception.CuentaInactivaException;
import com.cajero.exception.LimiteExtraccionExcedidoException;
import com.cajero.exception.SaldoInsuficienteException;
import com.cajero.model.CuentaBancaria;
import com.cajero.model.Transaccion;
import com.cajero.service.CajeroService;
import com.cajero.util.Formateador;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CajeroUI {

    private CajeroService servicio;
    private Scanner scanner;

    public CajeroUI(CajeroService servicio) {
        this.servicio = servicio;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        System.out.println("Bienvenido al cajero automatico");

        boolean continuar = true;

        while (continuar) {
            System.out.print("Ingrese numero de cuenta (o 'salir'): ");
            String numeroCuenta = scanner.nextLine().trim();

            if (numeroCuenta.equalsIgnoreCase("salir")) {
                System.out.println("Hasta luego.");
                continuar = false;
            } else {
                CuentaBancaria cuenta = servicio.buscarCuenta(numeroCuenta);
                if (cuenta == null) {
                    System.out.println("Cuenta no encontrada.");
                } else {
                    continuar = mostrarMenu(cuenta);
                }
            }
        }
    }

    private boolean mostrarMenu(CuentaBancaria cuenta) {

        boolean seguirEnCuenta = true;

        while (seguirEnCuenta) {
            System.out.println("\nCuenta: " + cuenta.getNumeroCuenta()
                    + " | Titular: " + cuenta.getTitular()
                    + " | Saldo: " + Formateador.formatearMoneda(cuenta.getSaldo()));
            System.out.println("1. Depositar");
            System.out.println("2. Extraer");
            System.out.println("3. Transferir");
            System.out.println("4. Consultar saldo");
            System.out.println("5. Ver historial");
            System.out.println("6. Cambiar de cuenta");
            System.out.println("0. Salir");
            System.out.print("Opcion: ");

            // try-catch para InputMismatchException, como se vio en clase
            int opcion = leerEntero();

            // switch expression con flechas, como se vio en clase
            switch (opcion) {
                case 1 -> depositar(cuenta.getNumeroCuenta());
                case 2 -> extraer(cuenta.getNumeroCuenta());
                case 3 -> transferir(cuenta.getNumeroCuenta());
                case 4 -> consultarSaldo(cuenta.getNumeroCuenta());
                case 5 -> verHistorial(cuenta.getNumeroCuenta());
                case 6 -> seguirEnCuenta = false;
                case 0 -> { return false; }
                default -> System.out.println("Opcion invalida.");
            }
        }

        return true;
    }

    private void depositar(String numeroCuenta) {
        System.out.print("Monto a depositar: ");
        double monto = leerDouble();

        try {
            servicio.depositar(numeroCuenta, monto);
            System.out.println("Deposito realizado correctamente.");
        } catch (CuentaInactivaException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void extraer(String numeroCuenta) {
        System.out.print("Monto a extraer: ");
        double monto = leerDouble();

        try {
            servicio.extraer(numeroCuenta, monto);
            System.out.println("Extraccion realizada correctamente.");
        } catch (SaldoInsuficienteException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (LimiteExtraccionExcedidoException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (CuentaInactivaException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void transferir(String nroOrigen) {
        System.out.print("Numero de cuenta destino: ");
        String nroDestino = scanner.nextLine().trim();
        System.out.print("Monto a transferir: ");
        double monto = leerDouble();

        try {
            servicio.transferir(nroOrigen, nroDestino, monto);
            System.out.println("Transferencia realizada correctamente.");
        } catch (SaldoInsuficienteException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (LimiteExtraccionExcedidoException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (CuentaInactivaException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void consultarSaldo(String numeroCuenta) {
        try {
            double saldo = servicio.consultarSaldo(numeroCuenta);
            System.out.println("Saldo disponible: " + Formateador.formatearMoneda(saldo));
        } catch (CuentaInactivaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void verHistorial(String numeroCuenta) {
        try {
            ArrayList<Transaccion> historial = servicio.obtenerHistorial(numeroCuenta);
            System.out.println("Ultimas transacciones:");
            if (historial.isEmpty()) {
                System.out.println("  Sin movimientos registrados.");
            } else {
                for (Transaccion t : historial) {
                    System.out.println("  " + t.toString());
                }
            }
        } catch (CuentaInactivaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Lee un entero y captura el error si el usuario escribe algo que no es numero
    private int leerEntero() {
        try {
            int valor = scanner.nextInt();
            scanner.nextLine(); // limpia el buffer del Scanner
            return valor;
        } catch (InputMismatchException e) {
            scanner.nextLine(); // limpia el buffer igualmente
            System.out.println("Debe ingresar un numero entero.");
            return -1;
        }
    }

    private double leerDouble() {
        try {
            double valor = scanner.nextDouble();
            scanner.nextLine();
            return valor;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("Debe ingresar un numero.");
            return -1;
        }
    }
}
