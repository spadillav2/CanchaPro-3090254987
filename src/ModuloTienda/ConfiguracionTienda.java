/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModuloTienda;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
/**
 *
 * @author Padilla
 */
public class ConfiguracionTienda {


    private boolean tiendaActiva = false;

    public boolean isTiendaActiva() {
        return tiendaActiva;
    }

    public void setTiendaActiva(boolean tiendaActiva) {
        this.tiendaActiva = tiendaActiva;
    }

    public void guardar(String ruta) {
        try {
            PrintWriter escritor = new PrintWriter(ruta);
            escritor.println("modulo_tienda=" + tiendaActiva);
            escritor.close();
        } catch (IOException e) {
            System.out.println("Error al guardar la configuracion: " + e.getMessage());
        }
    }

    public void cargar(String ruta) {
        File archivo = new File(ruta);
        if (archivo.exists()) {
            try {
                BufferedReader lector = new BufferedReader(new FileReader(archivo));
                String linea = lector.readLine();
                while (linea != null) {
                    String[] partes = linea.split("=");
                    if (partes.length == 2 && partes[0].trim().equals("modulo_tienda")) {
                        tiendaActiva = Boolean.parseBoolean(partes[1].trim());
                    }
                    linea = lector.readLine();
                }
                lector.close();
            } catch (IOException e) {
                System.out.println("Error al leer la configuracion: " + e.getMessage());
            }
        }
    }
    
    
    
    
    
    
    
    
}

