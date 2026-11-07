/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package canchaproArchivos;


import canchapro.codigo.GestionReservas;
import canchapro.codigo.Reserva;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import canchapro.codigo.Cancha;
import canchapro.codigo.Cliente;
import canchapro.codigo.GestionCanchas;
import canchapro.codigo.GestionClientes;
import canchapro.codigo.Pago;
import canchapro.codigo.ServicioAdicional;
import java.time.LocalDateTime;
/**
 *
 * @author Padilla
 */
public class ArchivoReservas {

    public void guardarReservas(GestionReservas gestion, String rutaReservas, String rutaServicios, String rutaPagos) {
        try {
            BufferedWriter bwReservas = new BufferedWriter(new FileWriter(rutaReservas));
            BufferedWriter bwServicios = new BufferedWriter(new FileWriter(rutaServicios));
            BufferedWriter bwPagos = new BufferedWriter(new FileWriter(rutaPagos));

            for (int i = 0; i < gestion.getCantReservas(); i++) {
                Reserva r = gestion.getReserva()[i];

                String linea = r.getCodigo() + "," + r.getCliente().getCodigo() + "," + r.getCancha().getCodigo() + "," + r.getFecha() + "," + r.getHoraInicio() + "," + r.getDuracion() + "," + r.getEstado();
                if (r.getFechaCancelacion() != null) {
                    linea = linea + "," + r.getFechaCancelacion() + "," + r.getMotivoCancelacion().replace(",", " ") + "," + r.getOperadorCancelacion().replace(",", " ");
                }
                bwReservas.write(linea);
                bwReservas.newLine();

                for (int j = 0; j < r.getCantidadServicios(); j++) {
                    ServicioAdicional s = r.getServicios()[j];
                    bwServicios.write(r.getCodigo() + "," + s.getCodigo() + "," + s.getNombre() + "," + s.getPrecio());
                    bwServicios.newLine();
                }

                for (int j = 0; j < r.getCantidadPagos(); j++) {
                    Pago p = r.getPago()[j];
                    String observacion = p.getObservacion();
                    if (observacion == null) {
                        observacion = "";
                    }
                    bwPagos.write(r.getCodigo() + "," + p.getCodigo() + "," + p.getFechaHoraPago() + "," + p.getMonto() + "," + p.getMetodo() + "," + observacion.replace(",", " "));
                    bwPagos.newLine();
                }
            }

            bwReservas.close();
            bwServicios.close();
            bwPagos.close();
        } catch (IOException e) {
            System.out.println("Error al guardar reservas: " + e.getMessage());
        }
    }

    public String cargarReservas(GestionReservas gestion, GestionClientes gestionClientes, GestionCanchas gestionCanchas, String rutaReservas, String rutaServicios, String rutaPagos) {
        int cargados = 0;
        int errores = 0;

        // Cargan las reservas
        try {
            FileReader fr = new FileReader(rutaReservas);
            BufferedReader br = new BufferedReader(fr);
            String linea = br.readLine();
            while (linea != null) {
                if (!linea.trim().isEmpty()) {
                    try {
                        String[] datos = linea.split(",");
                        Cliente cliente = gestionClientes.buscarClienteporCodigo(datos[1]);
                        Cancha cancha = gestionCanchas.buscarCanchaporCodigo(datos[2]);

                        if (cliente == null || cancha == null) {
                            errores++;
                        } else {
                            Reserva r = new Reserva(datos[0], cliente, cancha, LocalDate.parse(datos[3]), LocalTime.parse(datos[4]), Integer.parseInt(datos[5]));
                            r.setEstado(datos[6]);
                            if (datos.length > 9) {
                                r.restaurarCancelacion(LocalDate.parse(datos[7]), datos[8], datos[9]);
                            }
                            if (gestion.restaurarReserva(r)) {
                                cargados++;
                            } else {
                                errores++;
                            }
                        }
                    } catch (Exception e) {
                        errores++;
                    }
                }
                linea = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            return "No se pudo leer " + rutaReservas + ": " + e.getMessage();
        }

        //Cargan los servicios y pagos de cada reserva
        errores = errores + cargarServicios(gestion, rutaServicios);
        errores = errores + cargarPagos(gestion, rutaPagos);

        return "Reservas cargadas: " + cargados + ", lineas con error: " + errores;
    }

    private int cargarServicios(GestionReservas gestion, String rutaServicios) {
        int errores = 0;
        try {
            FileReader fr = new FileReader(rutaServicios);
            BufferedReader br = new BufferedReader(fr);
            String linea = br.readLine();
            while (linea != null) {
                if (!linea.trim().isEmpty()) {
                    try {
                        String[] datos = linea.split(",");
                        Reserva r = gestion.buscarReservaPorCodigo(datos[0]);
                        if (r == null) {
                            errores++;
                        } else {
                            r.agregarServicio(new ServicioAdicional(datos[1], datos[2], Double.parseDouble(datos[3])));
                        }
                    } catch (Exception e) {
                        errores++;
                    }
                }
                linea = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            System.out.println("No se pudo leer " + rutaServicios + ": " + e.getMessage());
        }
        return errores;
    }

    private int cargarPagos(GestionReservas gestion, String rutaPagos) {
        int errores = 0;
        try {
            FileReader fr = new FileReader(rutaPagos);
            BufferedReader br = new BufferedReader(fr);
            String linea = br.readLine();
            while (linea != null) {
                if (!linea.trim().isEmpty()) {
                    try {
                        String[] datos = linea.split(",");
                        Reserva r = gestion.buscarReservaPorCodigo(datos[0]);
                        if (r == null) {
                            errores++;
                        } else {
                            Pago p = new Pago(datos[1], LocalDateTime.parse(datos[2]), Double.parseDouble(datos[3]), datos[4]);
                            if (datos.length > 5) {
                                p.setObservacion(datos[5]);
                            }
                            r.restaurarPago(p);
                        }
                    } catch (Exception e) {
                        errores++;
                    }
                }
                linea = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            System.out.println("No se pudo leer " + rutaPagos + ": " + e.getMessage());
        }
        return errores;
    }
}