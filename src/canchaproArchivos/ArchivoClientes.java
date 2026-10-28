/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package canchaproArchivos;

import canchapro.codigo.Cliente;
import canchapro.codigo.GestionClientes;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

/**
 *
 * @author Padilla
 */
public class ArchivoClientes {
    
    public void guardar(GestionClientes gestion, String ruta) {
        try {
            PrintWriter escritor = new PrintWriter(ruta);
            for (int i = 0; i < gestion.getCantClientes(); i++) {
                Cliente c = gestion.getCliente()[i];
                String correo = c.getCorreo();
                if (correo == null) {
                    correo = "";
                }
                escritor.println(c.getCodigo() + "," + c.getNombre() + "," + c.getTelefono() + "," + c.getFechaRegistro() + "," + correo);
            }
            escritor.close();
        } catch (IOException e) {
            System.out.println("Error al guardar clientes: " + e.getMessage());
        }
    }

    public String cargar(GestionClientes gestion, String ruta) {
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            return "No existe " + ruta + ", se inicia sin clientes";
        }

        int cargados = 0;
        int errores = 0;
        try {
            BufferedReader lector = new BufferedReader(new FileReader(archivo));
            String linea = lector.readLine();
            while (linea != null) {
                if (!linea.trim().isEmpty()) {
                    try {
                        String[] datos = linea.split(",");
                        Cliente c = new Cliente(datos[0], datos[1], datos[2], LocalDate.parse(datos[3]));
                        if (datos.length > 4) {
                            c.setCorreo(datos[4]);
                        }
                        String mensaje = gestion.agregarCliente(c);
                        if (mensaje.equals("Cliente agregado correctamente")) {
                            cargados++;
                        } else {
                            errores++;
                        }
                    } catch (Exception e) {
                        errores++;
                    }
                }
                linea = lector.readLine();
            }
            lector.close();
        } catch (IOException e) {
            return "Error al leer el archivo: " + e.getMessage();
        }
        return "Clientes cargados: " + cargados + ", lineas con error: " + errores;
    }
}
