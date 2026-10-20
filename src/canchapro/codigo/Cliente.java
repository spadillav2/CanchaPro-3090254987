/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package canchapro.codigo;

import java.time.LocalDate;

/**
 *
 * @author Padilla
 */
public class Cliente {

    private String codigo;
    private String nombre;
    private String correo;
    private String telefono;
    private LocalDate fechaRegistro;

    public Cliente(String codigo, String nombre, String telefono, LocalDate fecha) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.telefono = telefono;
        this.fechaRegistro = fecha;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " -" + telefono + "-";
    }

}
