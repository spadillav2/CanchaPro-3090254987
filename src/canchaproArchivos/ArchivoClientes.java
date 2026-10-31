package canchaproArchivos;

import canchapro.codigo.Cliente;
import canchapro.codigo.GestionClientes;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class ArchivoClientes {

    public void guardar(GestionClientes gestion, String ruta) {
        try {
            FileWriter fw = new FileWriter(ruta);
            BufferedWriter bw = new BufferedWriter(fw);
            for (int i = 0; i < gestion.getCantClientes(); i++) {
                Cliente c = gestion.getCliente()[i];
                String correo = c.getCorreo();
                if (correo == null) {
                    correo = "";
                }
                bw.write(c.getCodigo() + "," + c.getNombre() + "," + c.getTelefono() + "," + c.getFechaRegistro() + "," + correo);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("Error al guardar clientes: " + e.getMessage());
        }
    }

    public String cargarArchivos(GestionClientes gestion, String ruta) {
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
                linea = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            return "No se pudo leer " + ruta + ": " + e.getMessage();
        }
        return "Clientes cargados: " + cargados + ", lineas con error: " + errores;
    }
}