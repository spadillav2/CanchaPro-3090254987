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
    //Atributos de Gestion Reservas
    private Reserva[] reserva = new Reserva [1000];
    private int cantReservas = 0;
    
    //Verficar si la cancha está libre
     public Cancha[] canchasDisponibles(GestionCanchas canchas, LocalDate fecha, LocalTime inicio, int duracion, String tipo) {

    // PRIMER CICLO: contar cuántas canchas cumplen
    int contador = 0;
    for (int i = 0; i < canchas.getCantCancha(); i++) {
        Cancha c = canchas.getCancha()[i];
        if (c.getTipo().equals(tipo) && canchaLibre(c, fecha, inicio, duracion)) {
            contador++;
        }
    }

    // Crear el array del tamaño exacto
    Cancha[] resultado = new Cancha[contador];

    // SEGUNDO CICLO: llenar el array
    int posicion = 0;
    for (int i = 0; i < canchas.getCantCancha(); i++) {
        Cancha c = canchas.getCancha()[i];
        if (c.getTipo().equals(tipo) && canchaLibre(c, fecha, inicio, duracion)) {
            resultado[posicion] = c;
            posicion++;
        }
    }
    return resultado;
}
    
    
    
    
    
    // Evitar traslapes entre reservas 
    public boolean canchaLibre (Cancha cancha, LocalDate fecha,LocalTime horaInicio, int duracion ){
        boolean canchaLibre = true;
        LocalTime fin= horaInicio.plusMinutes(duracion);
        if (!cancha.estaDisponible()) {
        canchaLibre= false;
        // Si la hora de inicio de la cancha es antes de la hora de Apertura, o la reserva termina después de la hora de Cierre, o la reserva termina después de que inicia otra, la cancha no está disponible.
     if(horaInicio.isBefore(cancha.getHoraApertura())|| fin.isAfter(cancha.getHoraCierre())|| fin.isAfter(horaInicio)){
         canchaLibre=false;
     }
        
        
    }
    for (int i = 0; i < cantReservas; i++) {
     if (reserva[i].getCancha().getCodigo().equals(cancha.getCodigo())
        && reserva[i].seTraslapaCon(fecha, horaInicio, duracion)) {
    canchaLibre = false;
}
    }
        return canchaLibre;
    }
    
    
    
    //Getters

    public Reserva[] getReserva() {
        return reserva;
    }

    public int getCantReservas() {
        return cantReservas;
    }
    
    
    
    //Buscadore de Reservas
    
    public Reserva buscarReservaPorCodigo(String codigo) {
    Reserva encontrada = null;
    for (int i = 0; i < cantReservas; i++) {
        if (reserva[i].getCodigo().equals(codigo)) {
            encontrada = reserva[i];
        }
    }
    return encontrada;
}
    
    
    
    
    
    
    //Agregar Reserva
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