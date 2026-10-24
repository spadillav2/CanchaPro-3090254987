/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package canchapro.codigo;
import java.time.LocalDate;
import java.time.LocalTime;


/**
 *
 * @author Padilla
 */
public class GestionReservas {
    
    private Reserva[] reserva = new Reserva [1000];
    private int cantReservas = 0;
    
    public boolean canchaLibre (Cancha cancha, LocalDate fecha,LocalTime horaInicio, int duracion ){
        boolean canchaLibre = true;
        if (!cancha.estaDisponible()) {
        canchaLibre= false;
    }
    for (int i = 0; i < cantReservas; i++) {
     if (reserva[i].getCancha().getCodigo().equals(cancha.getCodigo())
        && reserva[i].seTraslapaCon(fecha, horaInicio, duracion)) {
    canchaLibre = false;
}
    }
        return canchaLibre;
    }
    
    public Reserva buscarReservaPorCodigo(String codigo) {
    Reserva encontrada = null;
    for (int i = 0; i < cantReservas; i++) {
        if (reserva[i].getCodigo().equals(codigo)) {
            encontrada = reserva[i];
        }
    }
    return encontrada;
}
    
    
    
    
    
   public String agregarReserva(Reserva r) {
    if (cantReservas >= reserva.length) {
        return "Se ha excedido la cantidad de reservas";
    }
    if (buscarReservaPorCodigo(r.getCodigo()) != null) {
        return "Ya existe una reserva con ese codigo";
    }
    if (canchaLibre(r.getCancha(), r.getFecha(), r.getHoraInicio(), r.getDuracion()) == false) {
        return "Cancha no disponible";
    }
    reserva[cantReservas] = r;
    cantReservas++;
    return "Reserva agregada correctamente";
}
    
    
}
