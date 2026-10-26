/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package canchapro.interfaces;
import canchapro.codigo.Cancha;
import canchapro.codigo.Cliente;
import canchapro.codigo.GestionReservas;
import canchapro.codigo.Pago;
import canchapro.codigo.Reserva;
import canchapro.codigo.ServicioAdicional;
import java.time.LocalDate;
import java.time.LocalTime;
import canchapro.codigo.Tarifa;
import java.time.LocalDateTime;
import canchapro.codigo.GestionClientes;
import java.time.Month;
/**
 *
 * @author Padilla
 */
public class CanchaProSandraPadilla {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        /*
Tarifa tarifa = new Tarifa();
Cliente cliente = new Cliente("CL01", "Juan", "55551234", LocalDate.now());
Cancha cancha = new Cancha("C01", "La Bombonera", "Futbol 5", 10, 150.0, Cancha.DISPONIBLE, LocalTime.of(8, 0), LocalTime.of(23, 0));

// Reserva de prueba: viernes 18:00, 2 horas -> total 450
Reserva reserva = new Reserva("R01", cliente, cancha, LocalDate.of(2026, 10, 23), LocalTime.of(18, 0), 2);

System.out.println("Total: " + reserva.calcularTotal(tarifa));
System.out.println("Saldo inicial: " + reserva.calcularSaldo(tarifa));

System.out.println("--- Pago parcial de 200 ---");
System.out.println(reserva.registrarPago(new Pago("P01", LocalDateTime.now(), 200, Pago.EFECTIVO), tarifa));
System.out.println("Saldo: " + reserva.calcularSaldo(tarifa));

System.out.println("--- Pago mayor al saldo (300) ---");
System.out.println(reserva.registrarPago(new Pago("P02", LocalDateTime.now(), 300, Pago.EFECTIVO), tarifa));
System.out.println("Saldo: " + reserva.calcularSaldo(tarifa));

System.out.println("--- Pago negativo (-50) ---");
System.out.println(reserva.registrarPago(new Pago("P03", LocalDateTime.now(), -50, Pago.EFECTIVO), tarifa));

System.out.println("--- Pago total (250) ---");
System.out.println(reserva.registrarPago(new Pago("P04", LocalDateTime.now(), 250, Pago.TARJETA), tarifa));
System.out.println("Saldo: " + reserva.calcularSaldo(tarifa));

System.out.println("--- Cancelacion ---");
Reserva reserva2 = new Reserva("R02", cliente, cancha, LocalDate.of(2026, 10, 24), LocalTime.of(10, 0), 1);
System.out.println(reserva2.cancelar("Lluvia", "Lupe"));
System.out.println("Estado: " + reserva2.getEstado());
System.out.println("Pago en reserva cancelada: " + reserva2.registrarPago(new Pago("P05", LocalDateTime.now(), 50, Pago.EFECTIVO), tarifa));
System.out.println("Cancelar otra vez: " + reserva2.cancelar("Otro motivo", "Lupe"));
        



Reserva existente = new Reserva("R10", cliente, cancha, LocalDate.of(2026, 10, 23), LocalTime.of(18, 0), 2);
LocalDate dia = LocalDate.of(2026, 10, 23);

System.out.println("16:00 x 1h (false): " + existente.seTraslapaCon(dia, LocalTime.of(16, 0), 1));
System.out.println("20:00 x 1h (false): " + existente.seTraslapaCon(dia, LocalTime.of(20, 0), 1));
System.out.println("19:00 x 2h (true): " + existente.seTraslapaCon(dia, LocalTime.of(19, 0), 2));
System.out.println("18:00 x 1h (true): " + existente.seTraslapaCon(dia, LocalTime.of(18, 0), 1));
System.out.println("17:00 x 4h, contiene a la existente (true): " + existente.seTraslapaCon(dia, LocalTime.of(17, 0), 4));
System.out.println("Otro dia, misma hora (false): " + existente.seTraslapaCon(LocalDate.of(2026, 10, 24), LocalTime.of(18, 0), 2));

existente.cancelar("Prueba", "Lupe");
System.out.println("Reserva cancelada, 18:00 x 2h (false): " + existente.seTraslapaCon(dia, LocalTime.of(18, 0), 2));

GestionReservas gestor = new GestionReservas();
Cancha cancha2 = new Cancha("C02", "El Maracana", "Futbol 5", 10, 150.0, Cancha.DISPONIBLE, LocalTime.of(8, 0), LocalTime.of(23, 0));
Cancha cancha3 = new Cancha("C03", "El Monumental", "Futbol 7", 14, 150.0, Cancha.MANTENIMIENTO, LocalTime.of(8, 0), LocalTime.of(23, 0));
//LocalDate dia = LocalDate.of(2026, 10, 23);

System.out.println("Base 18:00 x 2h (aceptada): " + gestor.agregarReserva(new Reserva("R11", cliente, cancha, dia, LocalTime.of(18, 0), 2)));
System.out.println("1) 16:00 x 1h (aceptada): " + gestor.agregarReserva(new Reserva("R12", cliente, cancha, dia, LocalTime.of(16, 0), 1)));
System.out.println("2) 20:00 x 1h (aceptada): " + gestor.agregarReserva(new Reserva("R13", cliente, cancha, dia, LocalTime.of(20, 0), 1)));
System.out.println("3) 19:00 x 2h (rechazada): " + gestor.agregarReserva(new Reserva("R14", cliente, cancha, dia, LocalTime.of(19, 0), 2)));
System.out.println("4) 18:00 x 1h (rechazada): " + gestor.agregarReserva(new Reserva("R15", cliente, cancha, dia, LocalTime.of(18, 0), 1)));
System.out.println("5) Mantenimiento (rechazada): " + gestor.agregarReserva(new Reserva("R16", cliente, cancha3, dia, LocalTime.of(18, 0), 2)));
System.out.println("Otra cancha 18:00 x 2h (aceptada): " + gestor.agregarReserva(new Reserva("R17", cliente, cancha2, dia, LocalTime.of(18, 0), 2)));
System.out.println("Codigo duplicado (rechazada): " + gestor.agregarReserva(new Reserva("R11", cliente, cancha2, dia, LocalTime.of(10, 0), 1)));


    }
*/
        
        
        GestionClientes gestionClientes = new GestionClientes();
        System.out.println("Cliente 1 "+gestionClientes.agregarCliente(new Cliente ("CL01","Juan","55551234",LocalDate.of(2026, 10, 25)  )));
         System.out.println("Cliente 1 "+gestionClientes.agregarCliente(new Cliente ("CL01","Genesis","1458274",LocalDate.of(2026, 10, 25)  )));
          System.out.println("Cliente 1 "+gestionClientes.agregarCliente(new Cliente ("CL02","Ricardo","55551234",LocalDate.of(2026, 10, 25)  )));
                    System.out.println("Cliente 1 "+gestionClientes.agregarCliente(new Cliente ("CL03","Ricardo","55551235",LocalDate.of(2026, 10, 25)  )));
          System.out.println("Busqueda1: "+ gestionClientes.buscarClienteporCodigo("CL01"));
          System.out.println("Busqueda 2: "+ gestionClientes.buscarClienteporTelefono("55551235"));
//Creación del programa
//new Login().setVisible(true);

    }


    }
    

