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
public class Reserva {
    public static final String PENDIENTE = "pendiente";
    public static final String CONFIRMADA = "confirmada";
    public static final String EN_CURSO ="en_curso";
    public static final String FINALIZADA = "finalizada";
    public static final String CANCELADA = "cancelada";
    
    
    private String codigo;
    private Cliente cliente;
    private Cancha cancha;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horafin;
    private int  duracion;
    private String estado;
    private ServicioAdicional[] servicios;
    private int cantidadServicios;
    private Pago[] pago;
    private int cantidadPagos;

    public Reserva(String codigo, Cliente cliente, Cancha cancha, LocalDate fecha, LocalTime horaInicio, int duracion, String estado, ServicioAdicional[] servicios, int cantidadServicios, Pago[] pago, int cantidadPagos) {
        this.codigo = codigo;
        this.cliente = cliente;
        this.cancha = cancha;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
    ;
        this.duracion = duracion;
        this.estado = PENDIENTE;
     this.servicios = new ServicioAdicional [10];
    this.cantidadServicios=0;
      this.pago= new Pago[10];
      this.cantidadPagos=0;
    }
    
    public LocalTime calcularHoraFin(){
        return horaInicio.plusHours(duracion);
    }
    
    
    
    public boolean agregarServicio(ServicioAdicional s){
        if (cantidadServicios >= servicios.length ){
            return false;
        }
        servicios [cantidadServicios]= s;
        cantidadServicios ++;
        return true;
    }
    
    public double calcularTotalServicios(){
        double total=0;
        for (int i =0; i<cantidadServicios; i++){
            total = total + servicios[i].getPrecio();
        }
        return total;
    }
    
    

    
    
}
