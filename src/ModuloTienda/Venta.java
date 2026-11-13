/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModuloTienda;

import java.time.LocalDateTime;

/**
 *
 * @author Padilla
 */
public class Venta {
    private String numero;
    private LocalDateTime fechaHora;
    private String codigoReserva;  
    private String metodo;
    private DetalleVenta[] detalles = new DetalleVenta[100];
    private int cantidadDetalles = 0;

    public Venta(String numero, LocalDateTime fechaHora, String codigoReserva, String metodo) {
        this.numero = numero;
        this.fechaHora = fechaHora;
        this.codigoReserva = codigoReserva;
        this.metodo = metodo;
    }

//Getters

    public String getNumero() {
        return numero;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getCodigoReserva() {
        return codigoReserva;
    }

    public String getMetodo() {
        return metodo;
    }

    public DetalleVenta[] getDetalles() {
        return detalles;
    }

    public int getCantidadDetalles() {
        return cantidadDetalles;
    }
    
    
    
    
    //Setters

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public void setCodigoReserva(String codigoReserva) {
        this.codigoReserva = codigoReserva;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public void setDetalles(DetalleVenta[] detalles) {
        this.detalles = detalles;
    }

    public void setCantidadDetalles(int cantidadDetalles) {
        this.cantidadDetalles = cantidadDetalles;
    }
    
    
    
    

    public boolean agregarDetalle(DetalleVenta d) {
        if (cantidadDetalles >= detalles.length) {
            return false;
        }
        detalles[cantidadDetalles] = d;
        cantidadDetalles++;
        return true;
    }

    public double calcularTotal() {
        double total = 0;
        for (int i = 0; i < cantidadDetalles; i++) {
            total = total + detalles[i].calcularSubtotal();
        }
        return total;
    }
}
