/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

/**
 *
 * @author USUARIO
 */
public class Categorias {
    private Integer idCategoria;
    private String nombreCategoria;
    private String descripcionCategoria;
    
    public Categorias(Integer idCategoria, String nombreCategoria, String descripcionCategoria){
        this.idCategoria=idCategoria;
        this.nombreCategoria=nombreCategoria;
        this.descripcionCategoria=descripcionCategoria;
    }
    
    public Categorias(){
    }
    
    public Integer getIdCategoria(){
        return idCategoria;
    }
    public void setIdCategoria(Integer idCategoria){
        this.idCategoria=idCategoria;
    }
    
     public String getNombreCategoria(){
        return nombreCategoria;
    }
    
    public void setNombreCategoria(String nombreCategoria){
        this.nombreCategoria=nombreCategoria;
    }
    
    public String getDescripcionCategoria(){
        return descripcionCategoria;
    }
    
    public void setDescripcionCategoria(String descripcionCategoria){
        this.descripcionCategoria=descripcionCategoria;
    }
    
    @Override
    public String toString(){
        return this.nombreCategoria;
    } 
}
