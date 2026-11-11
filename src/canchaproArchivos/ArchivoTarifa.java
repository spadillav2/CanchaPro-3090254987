/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package canchaproArchivos;
import canchapro.codigo.Tarifa;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
/**
 *
 * @author Padilla
 */
public class ArchivoTarifa {
     public void guardar(Tarifa t, String ruta) {
        try {
            FileWriter fw = new FileWriter(ruta);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(t.getLunesViernesMatutino() + "," + t.getLunesViernesVespertino() + "," + t.getFinesDeSemana() + "," + t.getPorcentajeAnticipoMinimo());
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            System.out.println("Error al guardar tarifas: " + e.getMessage());
        }
    }

    public String cargar(Tarifa t, String ruta) {
        try {
            FileReader fr = new FileReader(ruta);
            BufferedReader br = new BufferedReader(fr);
            String linea = br.readLine();
            br.close();
            if (linea == null) {
                return "El archivo de tarifas está vacío, se usan los valores por defecto";
            }
            String[] datos = linea.split(",");
            t.setLunesViernesMatutino(Double.parseDouble(datos[0]));
            t.setLunesViernesVespertino(Double.parseDouble(datos[1]));
            t.setFinesDeSemana(Double.parseDouble(datos[2]));
            t.setPorcentajeAnticipoMinimo(Double.parseDouble(datos[3]));
            return "Tarifas cargadas";
        } catch (IOException e) {
            return "No se pudo leer " + ruta + ": " + e.getMessage();
        } catch (Exception e) {
            return "Línea de tarifas incorrecta, se usan los valores por defecto";
        }
    }
}
