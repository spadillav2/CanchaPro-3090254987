/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package canchapro.codigo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Padilla
 */
public class Reportes {
    private GestionReservas gestionReservas;
    private GestionCanchas gestionCanchas;
    private GestionClientes gestionClientes;
    private Tarifa tarifa;

    // Filtros: null significa "sin filtro"
    private LocalDate desde = null;
    private LocalDate hasta = null;
    private String codigoCancha = null;
    private String estado = null;

    public Reportes(GestionReservas gestionReservas, GestionCanchas gestionCanchas, GestionClientes gestionClientes, Tarifa tarifa) {
        this.gestionReservas = gestionReservas;
        this.gestionCanchas = gestionCanchas;
        this.gestionClientes = gestionClientes;
        this.tarifa = tarifa;
    }

    public void setFiltros(LocalDate desde, LocalDate hasta, String codigoCancha, String estado) {
        this.desde = desde;
        this.hasta = hasta;
        this.codigoCancha = codigoCancha;
        this.estado = estado;
    }

    // ---------- FILTROS ----------
    private boolean cumpleFecha(LocalDate fecha) {
        if (desde != null && fecha.isBefore(desde)) {
            return false;
        }
        if (hasta != null && fecha.isAfter(hasta)) {
            return false;
        }
        return true;
    }

    private boolean cumpleCancha(Reserva r) {
        return codigoCancha == null || r.getCancha().getCodigo().equals(codigoCancha);
    }

    private boolean cumpleEstado(Reserva r) {
        return estado == null || r.getEstado().equals(estado);
    }

    private boolean cumple(Reserva r) {
        return cumpleFecha(r.getFecha()) && cumpleCancha(r) && cumpleEstado(r);
    }

    private boolean activa(Reserva r) {
        return !r.getEstado().equals(Reserva.CANCELADA);
    }

  

    private int indiceCancha(String codigo) {
        for (int i = 0; i < gestionCanchas.getCantCancha(); i++) {
            if (gestionCanchas.getCancha()[i].getCodigo().equals(codigo)) {
                return i;
            }
        }
        return -1;
    }

    private int indiceCliente(String codigo) {
        for (int i = 0; i < gestionClientes.getCantClientes(); i++) {
            if (gestionClientes.getCliente()[i].getCodigo().equals(codigo)) {
                return i;
            }
        }
        return -1;
    }

    private String resumen = "";

public String getResumen() {
    return resumen;
}

public String descripcionFiltros() {
    String d = "sin limite";
    String h = "sin limite";
    String c = "todas";
    String e = "todos";
    if (desde != null) {
        d = desde.toString();
    }
    if (hasta != null) {
        h = hasta.toString();
    }
    if (codigoCancha != null) {
        c = codigoCancha;
    }
    if (estado != null) {
        e = estado;
    }
    return "Desde: " + d + " | Hasta: " + h + " | Cancha: " + c + " | Estado: " + e;
}

private String resumenDe(DefaultTableModel modelo, String texto) {
    if (modelo.getRowCount() == 0) {
        return "No hay datos con esos filtros.";
    }
    return texto;
}

// 1. RESERVAS POR FECHA (filtra y ordena)
public DefaultTableModel reservasPorFecha() {
    Reserva[] lista = new Reserva[gestionReservas.getCantReservas()];
    int cantidad = 0;
    for (int i = 0; i < gestionReservas.getCantReservas(); i++) {
        Reserva r = gestionReservas.getReserva()[i];
        if (cumple(r)) {
            lista[cantidad] = r;
            cantidad++;
        }
    }
    for (int i = 0; i < cantidad - 1; i++) {
        for (int j = 0; j < cantidad - 1 - i; j++) {
            LocalDateTime a = LocalDateTime.of(lista[j].getFecha(), lista[j].getHoraInicio());
            LocalDateTime b = LocalDateTime.of(lista[j + 1].getFecha(), lista[j + 1].getHoraInicio());
            if (a.isAfter(b)) {
                Reserva temporal = lista[j];
                lista[j] = lista[j + 1];
                lista[j + 1] = temporal;
            }
        }
    }

    DefaultTableModel modelo = new DefaultTableModel(new String[]{"Fecha", "Inicio", "Fin", "Código", "Cancha", "Cliente", "Estado"}, 0);
    for (int i = 0; i < cantidad; i++) {
        Reserva r = lista[i];
        modelo.addRow(new Object[]{r.getFecha(), r.getHoraInicio(), r.calcularHoraFin(), r.getCodigo(), r.getCancha().getNombre(), r.getCliente().getNombre(), r.getEstado()});
    }
    resumen = resumenDe(modelo, "Total de reservas: " + cantidad);
    return modelo;
}

// 2. RESERVAS POR CANCHA (agrupa, cuenta y suma)
public DefaultTableModel reservasPorCancha() {
    DefaultTableModel modelo = new DefaultTableModel(new String[]{"Cancha", "Código", "Reservas", "Total reservado (Q)"}, 0);
    int totalReservas = 0;
    double totalReservado = 0;

    for (int i = 0; i < gestionCanchas.getCantCancha(); i++) {
        Cancha c = gestionCanchas.getCancha()[i];
        if (codigoCancha == null || c.getCodigo().equals(codigoCancha)) {
            int cantidad = 0;
            double reservado = 0;
            for (int j = 0; j < gestionReservas.getCantReservas(); j++) {
                Reserva r = gestionReservas.getReserva()[j];
                if (r.getCancha().getCodigo().equals(c.getCodigo()) && cumpleFecha(r.getFecha()) && cumpleEstado(r)) {
                    cantidad++;
                    if (activa(r)) {
                        reservado = reservado + r.calcularTotal(tarifa);
                    }
                }
            }
            modelo.addRow(new Object[]{c.getNombre(), c.getCodigo(), cantidad, String.format("%.2f", reservado)});
            totalReservas = totalReservas + cantidad;
            totalReservado = totalReservado + reservado;
        }
    }
    resumen = resumenDe(modelo, "Total de reservas: " + totalReservas + " | Total reservado: Q" + String.format("%.2f", totalReservado));
    return modelo;
}

// 3. INGRESOS POR PERIODO (suma los pagos por fecha de pago)
public DefaultTableModel ingresosPorPeriodo() {
    double efectivo = 0;
    double transferencia = 0;
    double tarjeta = 0;
    int cEfectivo = 0;
    int cTransferencia = 0;
    int cTarjeta = 0;

    for (int i = 0; i < gestionReservas.getCantReservas(); i++) {
        Reserva r = gestionReservas.getReserva()[i];
        if (cumpleCancha(r)) {
            for (int j = 0; j < r.getCantidadPagos(); j++) {
                Pago p = r.getPago()[j];
                if (cumpleFecha(p.getFechaHoraPago().toLocalDate())) {
                    if (p.getMetodo().equals(Pago.EFECTIVO)) {
                        efectivo = efectivo + p.getMonto();
                        cEfectivo++;
                    } else if (p.getMetodo().equals(Pago.TRANSFERENCIA)) {
                        transferencia = transferencia + p.getMonto();
                        cTransferencia++;
                    } else if (p.getMetodo().equals(Pago.TARJETA)) {
                        tarjeta = tarjeta + p.getMonto();
                        cTarjeta++;
                    }
                }
            }
        }
    }

    DefaultTableModel modelo = new DefaultTableModel(new String[]{"Método de pago", "Pagos", "Monto (Q)"}, 0);
    modelo.addRow(new Object[]{"Efectivo", cEfectivo, String.format("%.2f", efectivo)});
    modelo.addRow(new Object[]{"Transferencia", cTransferencia, String.format("%.2f", transferencia)});
    modelo.addRow(new Object[]{"Tarjeta", cTarjeta, String.format("%.2f", tarjeta)});
    modelo.addRow(new Object[]{"TOTAL", cEfectivo + cTransferencia + cTarjeta, String.format("%.2f", efectivo + transferencia + tarjeta)});
    resumen = "Pagos recibidos: " + (cEfectivo + cTransferencia + cTarjeta) + " | Total: Q" + String.format("%.2f", efectivo + transferencia + tarjeta);
    return modelo;
}

// 4. PAGOS PENDIENTES
public DefaultTableModel pagosPendientes() {
    DefaultTableModel modelo = new DefaultTableModel(new String[]{"Reserva", "Cliente", "Fecha", "Total (Q)", "Pagado (Q)", "Saldo (Q)"}, 0);
    double totalPendiente = 0;
    for (int i = 0; i < gestionReservas.getCantReservas(); i++) {
        Reserva r = gestionReservas.getReserva()[i];
        if (cumple(r) && activa(r)) {
            double saldo = r.calcularSaldo(tarifa);
            if (saldo > 0) {
                modelo.addRow(new Object[]{r.getCodigo(), r.getCliente().getNombre(), r.getFecha(),
                    String.format("%.2f", r.calcularTotal(tarifa)), String.format("%.2f", r.calcularTotalPagado()), String.format("%.2f", saldo)});
                totalPendiente = totalPendiente + saldo;
            }
        }
    }
    resumen = resumenDe(modelo, "Reservas con saldo: " + modelo.getRowCount() + " | Total por cobrar: Q" + String.format("%.2f", totalPendiente));
    return modelo;
}

// 5. RESERVAS CANCELADAS
public DefaultTableModel reservasCanceladas() {
    DefaultTableModel modelo = new DefaultTableModel(new String[]{"Reserva", "Cliente", "Fecha", "Cancelada el", "Motivo", "Operador"}, 0);
    for (int i = 0; i < gestionReservas.getCantReservas(); i++) {
        Reserva r = gestionReservas.getReserva()[i];
        if (r.getEstado().equals(Reserva.CANCELADA) && cumpleFecha(r.getFecha()) && cumpleCancha(r)) {
            modelo.addRow(new Object[]{r.getCodigo(), r.getCliente().getNombre(), r.getFecha(), r.getFechaCancelacion(), r.getMotivoCancelacion(), r.getOperadorCancelacion()});
        }
    }
    resumen = resumenDe(modelo, "Total de canceladas: " + modelo.getRowCount());
    return modelo;
}

// 6. CANCHA MAS UTILIZADA (cuenta y compara)
public DefaultTableModel canchaMasUtilizada() {
    int cantidad = gestionCanchas.getCantCancha();
    int[] conteo = new int[cantidad];

    for (int i = 0; i < gestionReservas.getCantReservas(); i++) {
        Reserva r = gestionReservas.getReserva()[i];
        if (cumple(r) && activa(r)) {
            int indice = indiceCancha(r.getCancha().getCodigo());
            if (indice >= 0) {
                conteo[indice]++;
            }
        }
    }

    DefaultTableModel modelo = new DefaultTableModel(new String[]{"Cancha", "Reservas"}, 0);
    int mayor = 0;
    for (int i = 0; i < cantidad; i++) {
        modelo.addRow(new Object[]{gestionCanchas.getCancha()[i].getNombre(), conteo[i]});
        if (conteo[i] > conteo[mayor]) {
            mayor = i;
        }
    }

    if (cantidad == 0 || conteo[mayor] == 0) {
        resumen = "No hay datos con esos filtros.";
    } else {
        resumen = "La más utilizada: " + gestionCanchas.getCancha()[mayor].getNombre() + " con " + conteo[mayor] + " reservas";
    }
    return modelo;
}

// 7. HORARIOS DE MAYOR DEMANDA
public DefaultTableModel horariosDeMayorDemanda() {
    int[] porHora = new int[24];
    for (int i = 0; i < gestionReservas.getCantReservas(); i++) {
        Reserva r = gestionReservas.getReserva()[i];
        if (cumple(r) && activa(r)) {
            int horaInicio = r.getHoraInicio().getHour();
            LocalTime fin = r.calcularHoraFin();
            int horaFin = fin.getHour();
            if (fin.getMinute() > 0) {
                horaFin++;
            }
            for (int h = horaInicio; h < horaFin; h++) {
                porHora[h]++;
            }
        }
    }

    DefaultTableModel modelo = new DefaultTableModel(new String[]{"Puesto", "Horario", "Reservas"}, 0);
    for (int puesto = 1; puesto <= 5; puesto++) {
        int mejor = 0;
        for (int h = 1; h < 24; h++) {
            if (porHora[h] > porHora[mejor]) {
                mejor = h;
            }
        }
        if (porHora[mejor] > 0) {
            LocalTime inicio = LocalTime.of(mejor, 0);
            modelo.addRow(new Object[]{puesto, inicio + " - " + inicio.plusHours(1), porHora[mejor]});
            porHora[mejor] = 0;
        }
    }
    resumen = resumenDe(modelo, "Las 5 horas más solicitadas");
    return modelo;
}

// 8. CLIENTES CON MAS RESERVAS
public DefaultTableModel clientesConMasReservas() {
    int cantidad = gestionClientes.getCantClientes();
    int[] conteo = new int[cantidad];

    for (int i = 0; i < gestionReservas.getCantReservas(); i++) {
        Reserva r = gestionReservas.getReserva()[i];
        if (cumple(r) && activa(r)) {
            int indice = indiceCliente(r.getCliente().getCodigo());
            if (indice >= 0) {
                conteo[indice]++;
            }
        }
    }

    
    
    DefaultTableModel modelo = new DefaultTableModel(new String[]{"Puesto", "Cliente", "Código", "Reservas"}, 0);
    if (cantidad > 0) {
        for (int puesto = 1; puesto <= 5; puesto++) {
            int mejor = 0;
            for (int i = 1; i < cantidad; i++) {
                if (conteo[i] > conteo[mejor]) {
                    mejor = i;
                }
            }
            if (conteo[mejor] > 0) {
                Cliente c = gestionClientes.getCliente()[mejor];
                modelo.addRow(new Object[]{puesto, c.getNombre(), c.getCodigo(), conteo[mejor]});
                conteo[mejor] = 0;
            }
        }
    }
    resumen = resumenDe(modelo, "Los 5 clientes con más reservas");
    return modelo;
}
}
