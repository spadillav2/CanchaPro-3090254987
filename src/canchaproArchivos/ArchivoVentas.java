/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package canchaproArchivos;
import ModuloTienda.DetalleVenta;
import ModuloTienda.GestionProductos;
import ModuloTienda.GestionVentas;
import ModuloTienda.Producto;
import ModuloTienda.Venta;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
/**
 *
 * @author Padilla
 */
public class ArchivoVentas {
     public void guardarVentas(GestionVentas gestion, String rutaVentas, String rutaDetalles) {
        try {
            BufferedWriter bwVentas = new BufferedWriter(new FileWriter(rutaVentas));
            BufferedWriter bwDetalles = new BufferedWriter(new FileWriter(rutaDetalles));

            for (int i = 0; i < gestion.getCantVentas(); i++) {
                Venta v = gestion.getVenta()[i];
                bwVentas.write(v.getNumero() + "," + v.getFechaHora() + "," + v.getCodigoReserva() + "," + v.getMetodo() + "," + v.calcularTotal());
                bwVentas.newLine();

                for (int j = 0; j < v.getCantidadDetalles(); j++) {
                    DetalleVenta d = v.getDetalles()[j];
                    bwDetalles.write(v.getNumero() + "," + d.getProducto().getCodigo() + "," + d.getCantidad() + "," + d.getPrecioUnitario() + "," + d.calcularSubtotal());
                    bwDetalles.newLine();
                }
            }
            bwVentas.close();
            bwDetalles.close();
        } catch (IOException e) {
            System.out.println("Error al guardar ventas: " + e.getMessage());
        }
    }

    public String cargarVentas(GestionVentas gestion, GestionProductos gestionProductos, String rutaVentas, String rutaDetalles) {
        int cargados = 0;
        int errores = 0;

        // PRIMERO las ventas
        try {
            FileReader fr = new FileReader(rutaVentas);
            BufferedReader br = new BufferedReader(fr);
            String linea = br.readLine();
            while (linea != null) {
                if (!linea.trim().isEmpty()) {
                    try {
                        String[] datos = linea.split(",");
                        Venta v = new Venta(datos[0], LocalDateTime.parse(datos[1]), datos[2], datos[3]);
                        if (gestion.restaurarVenta(v)) {
                            cargados++;
                        } else {
                            errores++;
                        }
                    } catch (Exception e) {
                        errores++;
                    }
                }
                linea = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            return "No se pudo leer " + rutaVentas + ": " + e.getMessage();
        }

        // DESPUES los detalles de cada venta
        try {
            FileReader fr = new FileReader(rutaDetalles);
            BufferedReader br = new BufferedReader(fr);
            String linea = br.readLine();
            while (linea != null) {
                if (!linea.trim().isEmpty()) {
                    try {
                        String[] datos = linea.split(",");
                        Venta v = gestion.buscarPorNumero(datos[0]);
                        Producto p = gestionProductos.buscarPorCodigo(datos[1]);
                        if (v == null || p == null) {
                            errores++;
                        } else {
                            v.agregarDetalle(new DetalleVenta(p, Integer.parseInt(datos[2]), Double.parseDouble(datos[3])));
                        }
                    } catch (Exception e) {
                        errores++;
                    }
                }
                linea = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            System.out.println("No se pudo leer " + rutaDetalles + ": " + e.getMessage());
        }
        return "Ventas cargadas: " + cargados + ", lineas con error: " + errores;
    }
}
