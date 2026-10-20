/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package canchapro.codigo;
import java.time.LocalDateTime;
/**
 *
 * @author Padilla
 */
public class Pago {
    
    public static final String EFECTIVO ="efectivo";
    public static final String TRANSFERENCIA = "transferencia";
    public static final String TARJETA = "tarjeta";
    
    
    private String codigo;
    private LocalDateTime fechaHoraPago;
    private double monto;
    private String metodo;
    private String observacion;

    public Pago(String codigo, LocalDateTime fechaHoraPago, double monto, String metodo) {
        this.codigo = codigo;
        this.fechaHoraPago = fechaHoraPago;
        this.monto = monto;
        this.metodo = metodo;
    }

   

    public String getCodigo() {
        return codigo;
    }

    public LocalDateTime getFechaHoraPago() {
        return fechaHoraPago;
    }

    public double getMonto() {
        return monto;
    }

    public String getMetodo() {
        return metodo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setFechaHoraPago(LocalDateTime fechaHoraPago) {
        this.fechaHoraPago = fechaHoraPago;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
    
    @Override
    public String toString(){
        return codigo +" - "+fechaHoraPago+" - "+ monto + " - "+ metodo +" - "+observacion;
    }
    
}
