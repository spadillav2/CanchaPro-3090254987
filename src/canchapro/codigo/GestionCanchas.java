/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package canchapro.codigo;

/**
 *
 * @author Padilla
 */
public class GestionCanchas {
    
    private Cancha[] cancha = new Cancha[1000];
    private int cantCancha=0;

    public Cancha[] getCancha() {
        return cancha;
    }

    public int getCantCancha() {
        return cantCancha;
    }
    
    public Cancha buscarCanchaporCodigo(String codigo){
    Cancha encontrada = null;
    for (int i= 0; i<cantCancha; i++){
        if (cancha[i].getCodigo().equals(codigo)){
            encontrada= cancha[i];
        }
            }
    return encontrada;
    }
    
    public Cancha buscarCanchaporNombre(String nombre){
        Cancha encontrada= null;
        for (int i=0; i<cantCancha;i++){
            if (cancha[i].getNombre().equals(nombre)){
                encontrada= cancha[i];
            }
        }
        return encontrada;
    }
    
    public Cancha buscarCanchaporTipo(String tipo){
        Cancha encontrada= null;
        for (int i=0; i<cantCancha; i++){
            if (cancha[i].getTipo().equals(tipo)){
                encontrada= cancha[i];
            }
        }
        return encontrada;
    }
    
    
    public Cancha[] buscarCanchasPorNombre(String texto) {
    int contador = 0;
    for (int i = 0; i < cantCancha; i++) {
        if (cancha[i].getNombre().toLowerCase().contains(texto.toLowerCase())) {
            contador++;
        }
    }

    Cancha[] resultado = new Cancha[contador];
    int posicion = 0;
    for (int i = 0; i < cantCancha; i++) {
        if (cancha[i].getNombre().toLowerCase().contains(texto.toLowerCase())) {
            resultado[posicion] = cancha[i];
            posicion++;
        }
    }
    return resultado;
}

public Cancha[] buscarCanchasPorTipo(String tipo) {
    int contador = 0;
    for (int i = 0; i < cantCancha; i++) {
        if (cancha[i].getTipo().equals(tipo)) {
            contador++;
        }
    }

    Cancha[] resultado = new Cancha[contador];
    int posicion = 0;
    for (int i = 0; i < cantCancha; i++) {
        if (cancha[i].getTipo().equals(tipo)) {
            resultado[posicion] = cancha[i];
            posicion++;
        }
    }
    return resultado;
}
    
    
    
    
    
    
    
    
    
    public String agregarCancha(Cancha chachacha){
        
        if (cantCancha>= cancha.length){
            return "Se ha excedido de la cantidad de canchas que puede añadir";
        }
        if (buscarCanchaporCodigo(chachacha.getCodigo()) !=null){
            return "Ya existe una cancha con ese codigo";
        }
        
        if (buscarCanchaporNombre(chachacha.getNombre())!=null){
            return "Ya existe una cancha con ese nombre";
        }
        
        
        
        
        cancha[cantCancha]=chachacha;
        cantCancha ++;
        return "Cancha agregada correctamente";
    }
    
    
    
    
}
