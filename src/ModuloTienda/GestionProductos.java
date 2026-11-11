/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModuloTienda;

/**
 *
 * @author Padilla
 */
public class GestionProductos {
    private Producto[] producto = new Producto[200];
    private int cantProductos = 0;

    public Producto[] getProducto() {
        return producto;
    }

    public int getCantProductos() {
        return cantProductos;
    }

    public Producto buscarPorCodigo(String codigo) {
        for (int i = 0; i < cantProductos; i++) {
            if (producto[i].getCodigo().equals(codigo)) {
                return producto[i];
            }
        }
        return null;
    }

    public String validar(Producto p) {
        if (p.getPrecio() <= 0) {
            return "El precio debe ser mayor a 0";
        }
        if (p.getExistencia() < 0 || p.getStockMinimo() < 0) {
            return "La existencia y el stock mínimo no pueden ser negativos";
        }
        return "OK";
    }

    public String agregarProducto(Producto p) {
        String validacion = validar(p);
        if (cantProductos >= producto.length) {
            return "Se alcanzó el límite de productos";
        }
        if (buscarPorCodigo(p.getCodigo()) != null) {
            return "Ya existe un producto con ese código";
        }
        if (!validacion.equals("OK")) {
            return validacion;
        }
        producto[cantProductos] = p;
        cantProductos++;
        return "Producto agregado correctamente";
    }
    
    public String generarCodigo(){
        int codigoProducto= cantProductos+1;
        while(buscarPorCodigo("P"+codigoProducto)!=null){
            codigoProducto++;
        }
        return "P"+codigoProducto;
    }
}
