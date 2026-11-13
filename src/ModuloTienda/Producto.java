/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModuloTienda;

/**
 *
 * @author Padilla
 */
public class Producto {
    public static final String ACTIVO = "activo";
    public static final String INACTIVO = "inactivo";

    private String codigo;
    private String nombre;
    private String categoria;
    private double precio;
    private int existencia;
    private int stockMinimo;
    private String estado;

    public Producto(String codigo, String nombre, String categoria, double precio, int existencia, int stockMinimo, String estado) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.existencia = existencia;
        this.stockMinimo = stockMinimo;
        this.estado = estado;
    }

    public static String getACTIVO() {
        return ACTIVO;
    }

    public static String getINACTIVO() {
        return INACTIVO;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public double getPrecio() {
        return precio;
    }

    public int getExistencia() {
        return existencia;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public String getEstado() {
        return estado;
    }
    

  //Setters
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

  
    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean tieneStockBajo() {
        return existencia <= stockMinimo;
    }

    public void descontar(int cantidad) {
        existencia = existencia - cantidad;
    }
}


