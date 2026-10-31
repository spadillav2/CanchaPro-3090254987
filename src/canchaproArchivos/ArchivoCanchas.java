/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package canchaproArchivos;

import canchapro.codigo.Cancha;
import canchapro.codigo.GestionCanchas;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;

/**
 *
 * @author Padilla
 */
public class ArchivoCanchas {
    
  public void guardarCanchas(GestionCanchas gestion, String ruta) {
        try {
            FileWriter fw = new FileWriter(ruta);
            BufferedWriter bw = new BufferedWriter(fw);
            for (int i = 0; i < gestion.getCantCancha(); i++) {
                Cancha c = gestion.getCancha()[i];
                bw.write(c.getCodigo() + "," + c.getNombre() + "," + c.getTipo() + "," + c.getCapacidad() + "," + c.getPrecioBase() + "," + c.getEstado() + "," + c.getHoraApertura() + "," + c.getHoraCierre());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("Error al guardar canchas: " + e.getMessage());
        }
    }

    public String cargarCanchas(GestionCanchas gestion, String ruta) {
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
                        Cancha c = new Cancha(datos[0], datos[1], datos[2], Integer.parseInt(datos[3]), Double.parseDouble(datos[4]), datos[5], LocalTime.parse(datos[6]), LocalTime.parse(datos[7]));
                        String mensaje = gestion.agregarCancha(c);
                        if (mensaje.equals("Cancha agregada correctamente")) {
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
        return "Canchas cargadas: " + cargados + ", lineas con error: " + errores;
    }
}