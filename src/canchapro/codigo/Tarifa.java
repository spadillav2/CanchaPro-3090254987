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
 private double porcentajeAnticipoMinimo = 0.25;
    private double LunesViernesMatutino = 150.00; 
    private double LunesViernesVespertino = 225.00;
    private double FinesdeSemana = 250.00;

    public double getLunesViernesMatutino() {
        return LunesViernesMatutino;
    }

    public double getLunesViernesVespertino() {
        return LunesViernesVespertino;
    }

    public double getFinesDeSemana() {
        return FinesdeSemana;
    }

    public double getPorcentajeAnticipoMinimo() {
        return porcentajeAnticipoMinimo;
    }

    
    
    
    
    public void setLunesViernesMatutino(double LunesViernesMatutino) {
        this.LunesViernesMatutino = LunesViernesMatutino;
    }

    public void setLunesViernesVespertino(double LunesViernesVespertino) {
        this.LunesViernesVespertino = LunesViernesVespertino;
    }

    public void setFinesDeSemana(double FinesdeSemana) {
        this.FinesdeSemana = FinesdeSemana;
    }

    public void setPorcentajeAnticipoMinimo(double porcentajeAnticipoMinimo) {
        this.porcentajeAnticipoMinimo = porcentajeAnticipoMinimo;
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



public double calcularPrecioTotal(LocalDate fecha, LocalTime horaInicio, int duracionMinutos) {
    int dia = fecha.getDayOfWeek().getValue();

    if (dia >= 6) {
        double precioTotal=(FinesdeSemana+duracionMinutos)/60.0;
        return precioTotal;
    }

    int inicioEnHoras= horaInicio.getHour();
    int inicioEnMinutos=horaInicio.getMinute();
    int inicioMinutos = inicioEnHoras * 60 + inicioEnMinutos;
    int corte = 17 * 60;

    int minutosMatutino = 0;
    if (inicioMinutos < corte) {
        minutosMatutino = Math.min(duracionMinutos, corte - inicioMinutos);
    }
    int minutosVespertino = duracionMinutos - minutosMatutino;
double precioTotalJornadasMixtas=(LunesViernesMatutino * minutosMatutino + LunesViernesVespertino * minutosVespertino) / 60.0;
    return precioTotalJornadasMixtas;
}

}