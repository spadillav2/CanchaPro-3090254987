/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package canchapro.codigo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    // ---------- APOYO ----------
    private String encabezado(String nombre) {
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
        return nombre + "\nDesde: " + d + " | Hasta: " + h + " | Cancha: " + c + " | Estado: " + e + "\n----------------------------------------\n";
    }

    private String linea(Reserva r) {
        return r.getFecha() + " " + r.getHoraInicio() + "-" + r.calcularHoraFin() + " | " + r.getCodigo() + " | " + r.getCancha().getNombre() + " | " + r.getCliente().getNombre() + " | " + r.getEstado();
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

    // ---------- 1. RESERVAS POR FECHA (filtra y ordena) ----------
    public String reservasPorFecha() {
        Reserva[] lista = new Reserva[gestionReservas.getCantReservas()];
        int cantidad = 0;
        for (int i = 0; i < gestionReservas.getCantReservas(); i++) {
            Reserva r = gestionReservas.getReserva()[i];
            if (cumple(r)) {
                lista[cantidad] = r;
                cantidad++;
            }
        }

        // ordenar por fecha y hora (burbuja)
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

        String texto = encabezado("RESERVAS POR FECHA");
        for (int i = 0; i < cantidad; i++) {
            texto = texto + linea(lista[i]) + "\n";
        }
        if (cantidad == 0) {
            texto = texto + "No hay reservas con esos filtros.";
        } else {
            texto = texto + "\nTotal de reservas: " + cantidad;
        }
        return texto;
    }

    // ---------- 2. RESERVAS POR CANCHA (agrupa, cuenta y suma) ----------
    public String reservasPorCancha() {
        String texto = encabezado("RESERVAS POR CANCHA");
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
                texto = texto + c.getNombre() + " (" + c.getCodigo() + "): " + cantidad + " reservas, total reservado Q" + String.format("%.2f", reservado) + "\n";
            }
        }
        return texto;
    }

    // ---------- 3. INGRESOS POR PERIODO (suma pagos por fecha de pago) ----------
    public String ingresosPorPeriodo() {
        double total = 0;
        double efectivo = 0;
        double transferencia = 0;
        double tarjeta = 0;
        int cantidadPagos = 0;

        for (int i = 0; i < gestionReservas.getCantReservas(); i++) {
            Reserva r = gestionReservas.getReserva()[i];
            if (cumpleCancha(r)) {
                for (int j = 0; j < r.getCantidadPagos(); j++) {
                    Pago p = r.getPago()[j];
                    if (cumpleFecha(p.getFechaHoraPago().toLocalDate())) {
                        total = total + p.getMonto();
                        cantidadPagos++;
                        if (p.getMetodo().equals(Pago.EFECTIVO)) {
                            efectivo = efectivo + p.getMonto();
                        } else if (p.getMetodo().equals(Pago.TRANSFERENCIA)) {
                            transferencia = transferencia + p.getMonto();
                        } else if (p.getMetodo().equals(Pago.TARJETA)) {
                            tarjeta = tarjeta + p.getMonto();
                        }
                    }
                }
            }
        }

        String texto = encabezado("INGRESOS POR PERIODO");
        texto = texto + "Pagos recibidos: " + cantidadPagos + "\n";
        texto = texto + "Efectivo: Q" + String.format("%.2f", efectivo) + "\n";
        texto = texto + "Transferencia: Q" + String.format("%.2f", transferencia) + "\n";
        texto = texto + "Tarjeta: Q" + String.format("%.2f", tarjeta) + "\n";
        texto = texto + "\nTOTAL: Q" + String.format("%.2f", total);
        return texto;
    }

    // ---------- 4. PAGOS PENDIENTES ----------
    public String pagosPendientes() {
        String texto = encabezado("PAGOS PENDIENTES");
        double totalPendiente = 0;
        int cantidad = 0;
        for (int i = 0; i < gestionReservas.getCantReservas(); i++) {
            Reserva r = gestionReservas.getReserva()[i];
            if (cumple(r) && activa(r)) {
                double saldo = r.calcularSaldo(tarifa);
                if (saldo > 0) {
                    texto = texto + r.getCodigo() + " | " + r.getCliente().getNombre() + " | " + r.getFecha()
                            + " | total Q" + String.format("%.2f", r.calcularTotal(tarifa))
                            + " | pagado Q" + String.format("%.2f", r.calcularTotalPagado())
                            + " | saldo Q" + String.format("%.2f", saldo) + "\n";
                    totalPendiente = totalPendiente + saldo;
                    cantidad++;
                }
            }
        }
        if (cantidad == 0) {
            texto = texto + "No hay saldos pendientes.";
        } else {
            texto = texto + "\nReservas con saldo: " + cantidad + "\nTotal por cobrar: Q" + String.format("%.2f", totalPendiente);
        }
        return texto;
    }

    // ---------- 5. RESERVAS CANCELADAS ----------
    public String reservasCanceladas() {
        String texto = encabezado("RESERVAS CANCELADAS");
        int cantidad = 0;
        for (int i = 0; i < gestionReservas.getCantReservas(); i++) {
            Reserva r = gestionReservas.getReserva()[i];
            if (r.getEstado().equals(Reserva.CANCELADA) && cumpleFecha(r.getFecha()) && cumpleCancha(r)) {
                texto = texto + r.getCodigo() + " | " + r.getCliente().getNombre() + " | " + r.getFecha()
                        + " | cancelada el " + r.getFechaCancelacion()
                        + " | motivo: " + r.getMotivoCancelacion()
                        + " | operador: " + r.getOperadorCancelacion() + "\n";
                cantidad++;
            }
        }
        if (cantidad == 0) {
            texto = texto + "No hay reservas canceladas con esos filtros.";
        } else {
            texto = texto + "\nTotal de canceladas: " + cantidad;
        }
        return texto;
    }

    // ---------- 6. CANCHA MAS UTILIZADA (cuenta y compara) ----------
    public String canchaMasUtilizada() {
        String texto = encabezado("CANCHA MAS UTILIZADA");
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

        int mayor = 0;
        for (int i = 0; i < cantidad; i++) {
            texto = texto + gestionCanchas.getCancha()[i].getNombre() + ": " + conteo[i] + " reservas\n";
            if (conteo[i] > conteo[mayor]) {
                mayor = i;
            }
        }

        if (cantidad == 0 || conteo[mayor] == 0) {
            texto = texto + "\nNo hay reservas con esos filtros.";
        } else {
            texto = texto + "\nLa mas utilizada: " + gestionCanchas.getCancha()[mayor].getNombre() + " con " + conteo[mayor] + " reservas";
        }
        return texto;
    }

    // ---------- 7. HORARIOS DE MAYOR DEMANDA ----------
    public String horariosDeMayorDemanda() {
        String texto = encabezado("HORARIOS DE MAYOR DEMANDA");
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

        boolean hayDatos = false;
        for (int puesto = 1; puesto <= 5; puesto++) {
            int mejor = 0;
            for (int h = 1; h < 24; h++) {
                if (porHora[h] > porHora[mejor]) {
                    mejor = h;
                }
            }
            if (porHora[mejor] > 0) {
                LocalTime inicio = LocalTime.of(mejor, 0);
                texto = texto + puesto + ". " + inicio + " - " + inicio.plusHours(1) + "  ->  " + porHora[mejor] + " reservas\n";
                porHora[mejor] = 0;
                hayDatos = true;
            }
        }
        if (!hayDatos) {
            texto = texto + "No hay reservas con esos filtros.";
        }
        return texto;
    }

    // ---------- 8. CLIENTES CON MAS RESERVAS ----------
    public String clientesConMasReservas() {
        String texto = encabezado("CLIENTES CON MAS RESERVAS");
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

        boolean hayDatos = false;
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
                    texto = texto + puesto + ". " + c.getNombre() + " (" + c.getCodigo() + "): " + conteo[mejor] + " reservas\n";
                    conteo[mejor] = 0;
                    hayDatos = true;
                }
            }
        }
        if (!hayDatos) {
            texto = texto + "No hay reservas con esos filtros.";
        }
        return texto;
    }
}
