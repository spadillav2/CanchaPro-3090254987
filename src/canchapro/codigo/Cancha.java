/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package canchapro.codigo;

import java.time.LocalTime;
/**
 *
 * @author Padilla
 */
public class Cancha {
    
    public static final String DISPONIBLE = "disponible";
    public static final String MANTENIMIENTO ="mantenimiento";
    public static final String INACTIVA="inactiva";
    
    
    private String codigo;
    private String nombre;
    private String tipo;
    private int capacidad;
    private double precioBase;
    private String estado;
    private LocalTime horaApertura;
    private LocalTime horaCierre;

    public Cancha(String codigo, String nombre, String tipo, int capacidad, double precioBase, String estado, LocalTime horaApertura, LocalTime horaCierre) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.capacidad = capacidad;
        this.precioBase = precioBase;
        this.estado = estado;
        this.horaApertura = horaApertura;
        this.horaCierre = horaCierre;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public String getEstado() {
        return estado;
    }

    public LocalTime getHoraApertura() {
        return horaApertura;
    }

    public LocalTime getHoraCierre() {
        return horaCierre;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setHoraApertura(LocalTime horaApertura) {
        this.horaApertura = horaApertura;
    }

    public void setHoraCierre(LocalTime horaCierre) {
        this.horaCierre = horaCierre;
    }
    
    
    
    public boolean estaDisponible() {
    if (estado.equals(DISPONIBLE)){
        return true;
    }
        return false;
  
}
    
    @Override
    public String toString(){
        return codigo+" - "+   nombre  +" ( "+tipo+" ) ";
    }
    
    
}

