package com.cajero.service;

import com.cajero.exception.CuentaInactivaException;
import com.cajero.exception.LimiteExtraccionExcedidoException;
import com.cajero.exception.SaldoInsuficienteException;
import com.cajero.model.CuentaBancaria;
import com.cajero.model.TipoTransaccion;
import com.cajero.model.Transaccion;

import java.util.ArrayList;
import java.util.HashMap;

public class CajeroService {

    private static final double LIMITE_EXTRACCION = 10000.0;

    // HashMap: clave = numeroCuenta, valor = objeto CuentaBancaria
    private HashMap<String, CuentaBancaria> cuentas;

    public CajeroService() {
        this.cuentas = new HashMap<>();
    }

    public void agregarCuenta(CuentaBancaria cuenta) {
        cuentas.put(cuenta.getNumeroCuenta(), cuenta);
    }

    public CuentaBancaria buscarCuenta(String numeroCuenta) {
        return cuentas.get(numeroCuenta);
    }

    public void desactivarCuenta(String numeroCuenta) {
        CuentaBancaria cuenta = cuentas.get(numeroCuenta);
        if (cuenta != null) {
            cuenta.setActiva(false);
        }
    }

    // Valida que la cuenta exista y este activa antes de operar
    private CuentaBancaria validarCuenta(String numeroCuenta) throws CuentaInactivaException {
        CuentaBancaria cuenta = cuentas.get(numeroCuenta);
        if (cuenta == null) {
            throw new IllegalArgumentException("Cuenta no encontrada: " + numeroCuenta);
        }
        if (!cuenta.isActiva()) {
            throw new CuentaInactivaException(numeroCuenta);
        }
        return cuenta;
    }

    public void depositar(String numeroCuenta, double monto) throws CuentaInactivaException {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero.");
        }

        CuentaBancaria cuenta = validarCuenta(numeroCuenta);
        cuenta.setSaldo(cuenta.getSaldo() + monto);
        cuenta.registrarTransaccion(new Transaccion(
                TipoTransaccion.DEPOSITO, monto, "Deposito en efectivo", cuenta.getSaldo()
        ));
    }

    public void extraer(String numeroCuenta, double monto)
            throws CuentaInactivaException, SaldoInsuficienteException, LimiteExtraccionExcedidoException {

        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero.");
        }
        if (monto > LIMITE_EXTRACCION) {
            throw new LimiteExtraccionExcedidoException(monto);
        }

        CuentaBancaria cuenta = validarCuenta(numeroCuenta);

        if (cuenta.getSaldo() < monto) {
            throw new SaldoInsuficienteException(cuenta.getSaldo(), monto);
        }

        cuenta.setSaldo(cuenta.getSaldo() - monto);
        cuenta.registrarTransaccion(new Transaccion(
                TipoTransaccion.EXTRACCION, monto, "Extraccion en cajero", cuenta.getSaldo()
        ));
    }

    public void transferir(String nroOrigen, String nroDestino, double monto)
            throws CuentaInactivaException, SaldoInsuficienteException, LimiteExtraccionExcedidoException {

        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero.");
        }

        // Validar las dos cuentas antes de tocar cualquier saldo
        CuentaBancaria origen = validarCuenta(nroOrigen);
        CuentaBancaria destino = validarCuenta(nroDestino);

        if (origen.getSaldo() < monto) {
            throw new SaldoInsuficienteException(origen.getSaldo(), monto);
        }
        if (monto > LIMITE_EXTRACCION) {
            throw new LimiteExtraccionExcedidoException(monto);
        }

        // Recien aca se modifica el saldo de ambas cuentas
        origen.setSaldo(origen.getSaldo() - monto);
        destino.setSaldo(destino.getSaldo() + monto);

        origen.registrarTransaccion(new Transaccion(
                TipoTransaccion.TRANSFERENCIA, monto,
                "Transferencia enviada a " + nroDestino, origen.getSaldo()
        ));
        destino.registrarTransaccion(new Transaccion(
                TipoTransaccion.TRANSFERENCIA, monto,
                "Transferencia recibida de " + nroOrigen, destino.getSaldo()
        ));
    }

    public double consultarSaldo(String numeroCuenta) throws CuentaInactivaException {
        CuentaBancaria cuenta = validarCuenta(numeroCuenta);
        cuenta.registrarTransaccion(new Transaccion(
                TipoTransaccion.CONSULTA, 0, "Consulta de saldo", cuenta.getSaldo()
        ));
        return cuenta.getSaldo();
    }

    public ArrayList<Transaccion> obtenerHistorial(String numeroCuenta) throws CuentaInactivaException {
        CuentaBancaria cuenta = validarCuenta(numeroCuenta);
        return cuenta.getHistorialTransacciones();
    }
}
