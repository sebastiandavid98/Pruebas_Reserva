package com.hotel.Hotel.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReservaTest {

    // ──────────────────────────────────────────────
    // Prueba 1: confirmar reserva (dominio puro)
    // ──────────────────────────────────────────────
    @Test
    @DisplayName("Debe confirmar una reserva en estado PENDIENTE y cambiar estado de habitación")
    void debeConfirmarReservaPendiente() {
        // Arrange
        Cliente cliente = new Cliente("Carlos Gómez", "carlos@correo.com");
        Habitacion habitacion = new HabitacionEstandar("101", 2, 100.0, 1);

        RangoFechas periodo = new RangoFechas(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(4));
        Reserva reserva = new Reserva(cliente, habitacion, periodo);

        // Act
        reserva.confirmar();

        // Assert
        assertEquals(EstadoReserva.CONFIRMADA, reserva.getEstado());
        assertEquals(EstadoHabitacion.OCUPADA, habitacion.getEstado());
    }

    // ──────────────────────────────────────────────
    // Prueba 2: cancelar reserva (dominio puro)
    // ──────────────────────────────────────────────
    @Test
    @DisplayName("Debe cancelar la reserva y liberar la habitación a estado DISPONIBLE")
    void debeCancelarReservaYLiberarHabitacion() {
        // Arrange
        Cliente cliente = new Cliente("Laura Pérez", "laura@correo.com");
        Habitacion habitacion = new HabitacionEstandar("102", 2, 150.0, 2);

        RangoFechas periodo = new RangoFechas(
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(5));
        Reserva reserva = new Reserva(cliente, habitacion, periodo);

        // Confirmamos primero para que la habitación quede OCUPADA
        reserva.confirmar();
        assertEquals(EstadoHabitacion.OCUPADA, habitacion.getEstado());

        // Act
        reserva.cancelar();

        // Assert
        assertEquals(EstadoReserva.CANCELADA, reserva.getEstado());
        assertEquals(EstadoHabitacion.DISPONIBLE, habitacion.getEstado());
    }

    // ──────────────────────────────────────────────
    // Prueba 3: cálculo del costo total (dominio puro)
    // ──────────────────────────────────────────────
    @Test
    @DisplayName("El costo total debe calcularse como precioPorNoche × número de días")
    void debeCalcularCostoTotalCorrectamente() {
        // Arrange
        Cliente cliente = new Cliente("Miguel Torres", "miguel@correo.com");
        double precioPorNoche = 200.0;
        Habitacion habitacion = new HabitacionEstandar("103", 2, precioPorNoche, 1);

        LocalDateTime inicio = LocalDateTime.now().plusDays(1);
        LocalDateTime fin    = inicio.plusDays(3); // 3 noches
        RangoFechas periodo  = new RangoFechas(inicio, fin);

        // Act
        Reserva reserva = new Reserva(cliente, habitacion, periodo);

        // Assert
        assertEquals(precioPorNoche * 3, reserva.getCostoTotal(), 0.001);
    }
}