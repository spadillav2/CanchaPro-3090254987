/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModuloTienda;

/**
 *
 * @author Padilla
 */
public class GestionVentas {
    private Venta[] venta = new Venta[1000];
    private int cantVentas = 0;

    public Venta[] getVenta() {
        return venta;
    }

    public int getCantVentas() {
        return cantVentas;
    }

    public Venta buscarPorNumero(String codigoVenta) {
        for (int i = 0; i < cantVentas; i++) {
            if (venta[i].getNumero().equals(codigoVenta)) {
                return venta[i];
            }
        }
        return null;
    }

    public String generarNumero() {
        int codigoVenta = cantVentas + 1;
        while (buscarPorNumero("V" + codigoVenta) != null) {
            codigoVenta++;
        }
        return "V" + codigoVenta;
    }

    public String registrarVenta(Venta v) {
        if (cantVentas >= venta.length) {
            return "Se alcanzó el límite de ventas";
        }
        if (v.getCantidadDetalles() == 0) {
            return "La venta no tiene productos";
        }

    
        for (int i = 0; i < v.getCantidadDetalles(); i++) {
            DetalleVenta d = v.getDetalles()[i];
            Producto p = d.getProducto();
            if (!p.getEstado().equals(Producto.ACTIVO)) {
                return "El producto " + p.getNombre() + " está inactivo";
            }
            if (d.getCantidad() <= 0) {
                return "La cantidad debe ser mayor a 0";
            }
            if (d.getCantidad() > p.getExistencia()) {
                return "Inventario insuficiente de " + p.getNombre() + ". Disponible: " + p.getExistencia();
            }
        }

        
        for (int i = 0; i < v.getCantidadDetalles(); i++) {
            DetalleVenta detalle = v.getDetalles()[i];
            detalle.getProducto().descontar(detalle.getCantidad());
        }

       
        venta[cantVentas] = v;
        cantVentas++;
        return "Venta registrada correctamente";
    }

 
    public boolean restaurarVenta(Venta v) {
        if (cantVentas >= venta.length || buscarPorNumero(v.getNumero()) != null) {
            return false;
        }
        venta[cantVentas] = v;
        cantVentas++;
        return true;
    }

    public double calcularTotalVendido() {
        double total = 0;
        for (int i = 0; i < cantVentas; i++) {
            total = total + venta[i].calcularTotal();
        }
        return total;
    }
    
        
}
