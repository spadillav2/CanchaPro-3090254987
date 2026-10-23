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
public class Tarifa {
 
    private double LunesViernesMatutino = 150.00; 
    private double LunesViernesVespertino = 225.00;
    private double FinesdeSemana = 250.00;

    public double getLunesViernesMatutino() {
        return LunesViernesMatutino;
    }

    public double getLunesViernesVespertino() {
        return LunesViernesVespertino;
    }

    public double getFinesdeSemana() {
        return FinesdeSemana;
    }

    public void setLunesViernesMatutino(double LunesViernesMatutino) {
        this.LunesViernesMatutino = LunesViernesMatutino;
    }

    public void setLunesViernesVespertino(double LunesViernesVespertino) {
        this.LunesViernesVespertino = LunesViernesVespertino;
    }

    public void setFinesdeSemana(double FinesdeSemana) {
        this.FinesdeSemana = FinesdeSemana;
    }
    
    
    
    
    
    
    public double calcularPrecioHoraCancha(LocalDate fecha, LocalTime horaInicio){
        int dia = fecha.getDayOfWeek().getValue();
        double hora = horaInicio.getHour();
        double precioHoraCancha=0;
        if (dia <6 &&  hora<17.00){
            precioHoraCancha = LunesViernesMatutino;
        }
        else if (dia <6 && hora >=17){
            precioHoraCancha = LunesViernesVespertino;
        }
        else if (dia >= 6){
            precioHoraCancha= FinesdeSemana;
        }
        return precioHoraCancha;
    }
}
