/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

/**
 *
 * @author USUARIO
 */
public class Anios {
    private Integer idhistorialvisita;
    private String fechahoravisita;
    //private String iddispositivo;
    
    public Anios(Integer idhistorialvisita, String fechahoravisita){
        
        this.idhistorialvisita=idhistorialvisita;
        this.fechahoravisita=fechahoravisita;
        //this.iddispositivo=iddispositivo;
    }
    
    public Anios(){
        super();
    }
    
    public Integer getIdHistorialVisita(){
        return idhistorialvisita;
    }
    public void setIdHistorialVisita(Integer idhistorialvisita){
        this.idhistorialvisita=idhistorialvisita;
    }
    
     public String getfechaHoravisita(){
        return fechahoravisita;
    }
    
    public void setfechaHoraVisita(String fechahoravisita){
        this.fechahoravisita=fechahoravisita;
    }
    
    
    
    @Override
    public String toString(){
        return this.fechahoravisita;
    } 
}
