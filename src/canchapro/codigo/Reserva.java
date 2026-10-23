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
    private LocalDate fechaCancelacion;
private String motivoCancelacion;
private String operadorCancelacion;
    
    
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
    private int  duracion;
    private String estado;
    private ServicioAdicional[] servicios;
    private int cantidadServicios;
    private Pago[] pago;
    private int cantidadPagos;

    public Reserva(String codigo, Cliente cliente, Cancha cancha, LocalDate fecha, LocalTime horaInicio, int duracion) {
        this.codigo = codigo;
        this.cliente = cliente;
        this.cancha = cancha;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.duracion = duracion;
        this.estado = PENDIENTE;
     this.servicios = new ServicioAdicional [10];
    this.cantidadServicios=0;
      this.pago= new Pago[20];
      this.cantidadPagos=0;
    }

    public String getCodigo() {
        return codigo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Cancha getCancha() {
        return cancha;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }



    public int getDuracion() {
        return duracion;
    }

    public String getEstado() {
        return estado;
    }

    public ServicioAdicional[] getServicios() {
        return servicios;
    }

    public int getCantidadServicios() {
        return cantidadServicios;
    }

    public Pago[] getPago() {
        return pago;
    }

    public int getCantidadPagos() {
        return cantidadPagos;
    }

    public LocalDate getFechaCancelacion() {
        return fechaCancelacion;
    }

    public String getMotivoCancelacion() {
        return motivoCancelacion;
    }

    public String getOperadorCancelacion() {
        return operadorCancelacion;
    }

    
    
    
    
    
    
    
    
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setCancha(Cancha cancha) {
        this.cancha = cancha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

   

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
    
    
public double calcularTotalPagado(){
    double total=0;
    for (int i=0; i< cantidadPagos; i++){
        total = total + pago[i].getMonto();
    }
    return total;
}
    
@Override
public String toString(){
    return codigo + " - "+ cliente.getNombre()+" - " + cancha.getNombre()+" - "+ fecha + " - "+ horaInicio + " - "+estado;
}


public double calcularPrecioCancha(Tarifa tarifa) {
    double total = tarifa.calcularPrecioHoraCancha(fecha, horaInicio) * duracion;
    return total;
}




public double calcularTotal(Tarifa tarifa) {
    double precioCancha = calcularPrecioCancha(tarifa);
    double totalServicios = calcularTotalServicios();
    double total = precioCancha + totalServicios;
    return total;
}

    
public double calcularSaldo(Tarifa tarifa){
    double preciototal=calcularTotal(tarifa);
    double totalpagado=calcularTotalPagado();
    double saldo;
    saldo= preciototal-totalpagado;
    return saldo;
}


public String registrarPago(Pago p, Tarifa tarifa) {
    if (estado.equals(CANCELADA)) {
        return "La reserva está cancelada";
    }
    if (p.getMonto() <= 0) {
        return "El monto debe ser mayor a 0";
    }
    if (p.getMonto() > calcularSaldo(tarifa)) {
        return "El monto debe ser menor o igual a Q. " + calcularSaldo(tarifa);
    }
    if (cantidadPagos >= pago.length) {
        return "Ha excedido la cantidad máxima de pagos";
    }
    pago[cantidadPagos] = p;
    cantidadPagos++;
    return "Pago Aceptado";
}




public boolean cancelar(String motivo, String operador) {
    if (estado.equals(FINALIZADA) || estado.equals(CANCELADA)) {
        return false;
    }
    estado = CANCELADA;
    fechaCancelacion = LocalDate.now();
    motivoCancelacion = motivo;
    operadorCancelacion = operador;
    return true;
}




/*
public boolean seTraslapaCon(LocalDate fechaNueva, LocalTime inicioNueva, int duracionNueva) {
   boolean traslape=false;
    if (estado.equals(Reserva.CANCELADA)){
       traslape= false;
    }
        LocalTime finNueva = inicioNueva.plusHours(duracionNueva);
        else if ( inicioNueva.isBefore(calcularHoraFin()) && finNueva.isAfter(horaInicio)){
       traslape= true;
   }
        return traslape;
}
*/
}
