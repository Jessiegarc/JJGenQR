/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

/**
 *
 * @author USUARIO
 */
public class CategoriasQr {
    private Integer idCategoria;
    private String nombreCategoria;
    
    
    public CategoriasQr(){
        super();
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }
    
    
    
    
    
    @Override
    public String toString(){
        return this.nombreCategoria;
    } 
}
