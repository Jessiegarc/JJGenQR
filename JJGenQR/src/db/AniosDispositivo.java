/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

/**
 *
 * @author USUARIO
 */
public class AniosDispositivo {
    private Integer idhistorialvisitadispositivo;
    private String fechahoravisitadispositivo;
    //private String iddispositivo;
    
    public AniosDispositivo(Integer idhistorialvisita, String fechahoravisita){
        
        this.idhistorialvisitadispositivo=idhistorialvisitadispositivo;
        this.fechahoravisitadispositivo=fechahoravisitadispositivo;
    }
    
    public AniosDispositivo(){
        super();
    }

    public Integer getIdhistorialvisitadispositivo() {
        return idhistorialvisitadispositivo;
    }

    public void setIdhistorialvisitadispositivo(Integer idhistorialvisitadispositivo) {
        this.idhistorialvisitadispositivo = idhistorialvisitadispositivo;
    }

    public String getFechahoravisitadispositivo() {
        return fechahoravisitadispositivo;
    }

    public void setFechahoravisitadispositivo(String fechahoravisitadispositivo) {
        this.fechahoravisitadispositivo = fechahoravisitadispositivo;
    }
       
    
    @Override
    public String toString(){
        return this.fechahoravisitadispositivo;
    } 
}
