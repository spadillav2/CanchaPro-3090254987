/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package canchaproArchivos;
import ModuloTienda.GestionProductos;
import ModuloTienda.Producto;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
/**
 *
 * @author Padilla
 */
public class ArchivoProductos {
    public void guardarProductos(GestionProductos gestion, String ruta) {
        try {
            FileWriter fw = new FileWriter(ruta);
            BufferedWriter bw = new BufferedWriter(fw);
            for (int i = 0; i < gestion.getCantProductos(); i++) {
                Producto p = gestion.getProducto()[i];
                bw.write(p.getCodigo() + "," + p.getNombre() + "," + p.getCategoria() + "," + p.getPrecio() + "," + p.getExistencia() + "," + p.getStockMinimo() + "," + p.getEstado());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("Error al guardar productos: " + e.getMessage());
        }
    }

    public String cargarProductos(GestionProductos gestion, String ruta) {
        int cargados = 0;
        int errores = 0;
        try {
            FileReader fr = new FileReader(ruta);
            BufferedReader br = new BufferedReader(fr);
            String linea = br.readLine();
            while (linea != null) {
                if (!linea.trim().isEmpty()) {
                    try {
                        String[] datos = linea.split(",");
                        Producto p = new Producto(datos[0], datos[1], datos[2], Double.parseDouble(datos[3]), Integer.parseInt(datos[4]), Integer.parseInt(datos[5]), datos[6]);
                        String mensaje = gestion.agregarProducto(p);
                        if (mensaje.equals("Producto agregado correctamente")) {
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
            return "No se pudo leer " + ruta + ": " + e.getMessage();
        }
        return "Productos cargados: " + cargados + ", lineas con error: " + errores;
    }
}
