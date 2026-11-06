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

public boolean canchaLibre(Cancha cancha, LocalDate fecha, LocalTime horaInicio, int duracion) {
    //  Verificar si la cancha está activa/disponible en el sistema (mantenimiento, etc.)
    if (!cancha.estaDisponible()) {
        return false;
    }

    LocalTime fin = horaInicio.plusMinutes(duracion);

    // 2. Validar que la reserva no pase de medianoche (si da la vuelta en el reloj, fin es menor que horaInicio)
    if (fin.isBefore(horaInicio)) {
        return false;
    }

    // 3. Validar horarios de apertura y cierre de la cancha
   
    if (horaInicio.isBefore(cancha.getHoraApertura()) 
            || !horaInicio.isBefore(cancha.getHoraCierre()) 
            || fin.isAfter(cancha.getHoraCierre())) {
        return false;
    }

    // 4. Verificar que no haya traslapes con otras reservas existentes
    for (int i = 0; i < cantReservas; i++) {
        if (reserva[i].getCancha().getCodigo().equals(cancha.getCodigo())
                && reserva[i].seTraslapaCon(fecha, horaInicio, duracion)) {
            return false;
        }
    }

    return true;
}
    
    
    
    //Getters

    public Reserva[] getReserva() {
        return reserva;
    }

    public int getCantReservas() {
        return cantReservas;
    }
    
    
    
    //Buscadores de Reservas
    
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
    
   public String generarCodigo(){
    int numero= getCantReservas() +1;
    while (buscarReservaPorCodigo("R"+numero) != null){
        numero++;
    }
    String codigoReserva = "R"+numero;
    return codigoReserva;
}
    
  
}