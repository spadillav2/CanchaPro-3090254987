/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package canchapro.interfaces;

import canchapro.codigo.Cancha;
import canchapro.codigo.Cliente;
import canchapro.codigo.GestionCanchas;
import canchapro.codigo.GestionClientes;
import canchapro.codigo.GestionReservas;
import canchapro.codigo.Reserva;
import java.time.LocalDate;
import java.time.LocalTime;

public class CanchaProSandraPadilla {

    public static void main(String[] args) {
/*
        // ---------- CLIENTES ----------
        GestionClientes gestionClientes = new GestionClientes();
        System.out.println("Cliente 1 (agregado): " + gestionClientes.agregarCliente(new Cliente("CL01", "Juan", "55551234", LocalDate.of(2026, 10, 25))));
        System.out.println("Codigo repetido (rechazado): " + gestionClientes.agregarCliente(new Cliente("CL01", "Genesis", "1458274", LocalDate.of(2026, 10, 25))));
        System.out.println("Telefono repetido (rechazado): " + gestionClientes.agregarCliente(new Cliente("CL02", "Ricardo", "55551234", LocalDate.of(2026, 10, 25))));
        System.out.println("Cliente 2 (agregado): " + gestionClientes.agregarCliente(new Cliente("CL03", "Ricardo", "55551235", LocalDate.of(2026, 10, 25))));
        System.out.println("Buscar por codigo CL01: " + gestionClientes.buscarClienteporCodigo("CL01"));
        System.out.println("Buscar por telefono 55551235: " + gestionClientes.buscarClienteporTelefono("55551235"));
        System.out.println("Buscar por nombre Raul (null): " + gestionClientes.buscarClienteporNombre("Raul"));

        // ---------- CANCHAS ----------
        GestionCanchas gestionCanchas = new GestionCanchas();
        Cancha c1 = new Cancha("C01", "La Bombonera", "Futbol 5", 10, 150.0, Cancha.DISPONIBLE, LocalTime.of(8, 0), LocalTime.of(23, 0));
        Cancha c2 = new Cancha("C02", "El Maracana", "Futbol 5", 10, 150.0, Cancha.DISPONIBLE, LocalTime.of(8, 0), LocalTime.of(23, 0));
        Cancha c3 = new Cancha("C03", "El Monumental", "Futbol 7", 14, 150.0, Cancha.DISPONIBLE, LocalTime.of(8, 0), LocalTime.of(23, 0));

        System.out.println("Cancha 1 (agregada): " + gestionCanchas.agregarCancha(c1));
        System.out.println("Cancha 2 (agregada): " + gestionCanchas.agregarCancha(c2));
        System.out.println("Cancha 3 (agregada): " + gestionCanchas.agregarCancha(c3));

        Cancha repetidaCodigo = new Cancha("C01", "Otra Cancha", "Futbol 5", 10, 150.0, Cancha.DISPONIBLE, LocalTime.of(8, 0), LocalTime.of(23, 0));
        Cancha repetidaNombre = new Cancha("C09", "El Maracana", "Futbol 5", 10, 150.0, Cancha.DISPONIBLE, LocalTime.of(8, 0), LocalTime.of(23, 0));
        System.out.println("Codigo repetido (rechazada): " + gestionCanchas.agregarCancha(repetidaCodigo));
        System.out.println("Nombre repetido (rechazada): " + gestionCanchas.agregarCancha(repetidaNombre));
        System.out.println("Buscar C09 (null): " + gestionCanchas.buscarCanchaporCodigo("C09"));

        // C03 pasa a mantenimiento para las pruebas
        gestionCanchas.buscarCanchaporCodigo("C03").setEstado(Cancha.MANTENIMIENTO);

        // ---------- RESERVAS ----------
        GestionReservas gestor = new GestionReservas();
        LocalDate dia = LocalDate.of(2026, 10, 23);
        Cliente cliente = gestionClientes.buscarClienteporCodigo("CL01");
        Cancha cancha1 = gestionCanchas.buscarCanchaporCodigo("C01");
        Cancha cancha2 = gestionCanchas.buscarCanchaporCodigo("C02");
        Cancha cancha3 = gestionCanchas.buscarCanchaporCodigo("C03");

        System.out.println("Base 18:00 x 2h en C01 (aceptada): " + gestor.agregarReserva(new Reserva("R11", cliente, cancha1, dia, LocalTime.of(18, 0), 2)));

        // ---------- DISPONIBILIDAD (antes de agregar mas reservas) ----------
        Cancha[] r1 = gestor.canchasDisponibles(gestionCanchas, dia, LocalTime.of(16, 0), 1, "Futbol 5");
        System.out.println("Futbol 5 a las 16:00 (C01 y C02): " + r1.length);
        for (int i = 0; i < r1.length; i++) {
            System.out.println("  " + r1[i]);
        }

        Cancha[] r2 = gestor.canchasDisponibles(gestionCanchas, dia, LocalTime.of(19, 0), 1, "Futbol 5");
        System.out.println("Futbol 5 a las 19:00 (solo C02): " + r2.length);
        for (int i = 0; i < r2.length; i++) {
            System.out.println("  " + r2[i]);
        }

        Cancha[] r3 = gestor.canchasDisponibles(gestionCanchas, dia, LocalTime.of(19, 0), 1, "Futbol 7");
        System.out.println("Futbol 7 a las 19:00 (ninguna, mantenimiento): " + r3.length);

        // ---------- PRUEBAS 1 A 5 DEL ENUNCIADO ----------
        System.out.println("1) 16:00 x 1h (aceptada): " + gestor.agregarReserva(new Reserva("R12", cliente, cancha1, dia, LocalTime.of(16, 0), 1)));
        System.out.println("2) 20:00 x 1h, justo despues (aceptada): " + gestor.agregarReserva(new Reserva("R13", cliente, cancha1, dia, LocalTime.of(20, 0), 1)));
        System.out.println("3) 19:00 x 2h, parcial (rechazada): " + gestor.agregarReserva(new Reserva("R14", cliente, cancha1, dia, LocalTime.of(19, 0), 2)));
        System.out.println("4) 18:00 x 1h, contenida (rechazada): " + gestor.agregarReserva(new Reserva("R15", cliente, cancha1, dia, LocalTime.of(18, 0), 1)));
        System.out.println("5) Cancha en mantenimiento (rechazada): " + gestor.agregarReserva(new Reserva("R16", cliente, cancha3, dia, LocalTime.of(18, 0), 2)));
        System.out.println("Otra cancha 18:00 x 2h (aceptada): " + gestor.agregarReserva(new Reserva("R17", cliente, cancha2, dia, LocalTime.of(18, 0), 2)));
        System.out.println("Codigo duplicado (rechazada): " + gestor.agregarReserva(new Reserva("R11", cliente, cancha2, dia, LocalTime.of(10, 0), 1)));
*/
        //new Login().setVisible(true);
        new Principal().setVisible(true);
    }
}

