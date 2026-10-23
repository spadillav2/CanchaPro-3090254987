/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package canchapro.interfaces;
import canchapro.codigo.Cancha;
import canchapro.codigo.Cliente;
import canchapro.codigo.Pago;
import canchapro.codigo.Reserva;
import canchapro.codigo.ServicioAdicional;
import java.time.LocalDate;
import java.time.LocalTime;
import canchapro.codigo.Tarifa;
import java.time.LocalDateTime;
/**
 *
 * @author Padilla
 */
public class CanchaProSandraPadilla {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
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
        
    }

        
//Creación del programa
//new Login().setVisible(true);




    }
    

