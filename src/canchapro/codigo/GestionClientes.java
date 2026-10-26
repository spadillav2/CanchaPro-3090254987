/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package canchapro.codigo;

/**
 *
 * @author Padilla
 */
public class GestionClientes {
    private Cliente[ ]  cliente = new Cliente [1000];
    private int cantClientes =0;
    
    
    
    public Cliente buscarClienteporCodigo( String codigo){
     Cliente encontrado = null;
     for (int i= 0;i<cantClientes; i++){
         if (cliente[i].getCodigo().equals(codigo))
    encontrado=cliente[i];
             }
     return encontrado;
    }
    
    
    public Cliente buscarClienteporTelefono(String telefono){
        Cliente encontrado= null;
        for(int i =0; i<cantClientes; i++){
            if(cliente[i].getTelefono().equals(telefono))
                encontrado= cliente[i];
        }
        return encontrado;
    }
    
    
    public String agregarCliente(Cliente c){
        if (cantClientes >= cliente.length){
            return "Se ha excedido la cantidad de clientes que puedes añadir";
        }
        
        if (buscarClienteporCodigo(c.getCodigo()) !=null){
            return "Ya existe un cliente con ese codigo";
        }
        
        if (buscarClienteporTelefono(c.getTelefono())!=null){
            return "Ya existe un cliente con ese numero de telefono";
        }
        cliente[cantClientes]= c;
        cantClientes ++;
        return "Cliente agregado correctamente";
    }
    
    

}
