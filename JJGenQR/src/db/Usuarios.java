/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.util.ArrayList;

/**
 *
 * @author USUARIO
 */
public class Usuarios {
    
    private Integer idUsuarios;
    private String tipoUsuarios;
    private String nombresUsuarios;
    private String apellidoUsuarios;
    private String contrasenaUsuarios;
    private String cedulaUsuarios;
    private String correoUsuarios;
    private Boolean estadoUsuarios;
    
    
    public Usuarios(Integer idUsuarios, String tipoUsuarios, String nombresUsuarios, String apellidoUsuarios, String contrasenaUsuarios, String cedulaUsuarios, String correoUsuarios, Boolean estadoUsuarios){
      this.idUsuarios=idUsuarios;
      this.nombresUsuarios=nombresUsuarios;
      this.apellidoUsuarios=apellidoUsuarios;
      this.contrasenaUsuarios=contrasenaUsuarios;
      this.cedulaUsuarios=cedulaUsuarios;
      this.correoUsuarios=correoUsuarios;
      this.estadoUsuarios=estadoUsuarios;
    }
    
    public Usuarios(){
    }
    
    public Integer getIdUsuarios(){
        return idUsuarios;
    }
    
    public void setIdUsuarios(Integer idUsuarios){
        this.idUsuarios=idUsuarios;
    }
    
    public String getTipoUsuarios(){
        return tipoUsuarios;
    }
    
    public void setTipoUsuarios(String tipoUsuarios){
        this.tipoUsuarios=tipoUsuarios;
    }
    
    public String getNombresUsuarios(){
        return nombresUsuarios;
    }
    
    public void setNombresUsuarios(String nombresUsuarios){
        this.nombresUsuarios=nombresUsuarios;
    }
    
    public String getApellidoUsuarios(){
        return apellidoUsuarios;
    }
    
    public void setApellidoUsuarios(String apellidoUsuarios){
        this.apellidoUsuarios=apellidoUsuarios;
    }
    
    public String getContrasenaUsuarios(){
        return contrasenaUsuarios;
    }
    
    public void setContrasenaUsuarios(String contrasenaUsuarios){
        this.contrasenaUsuarios=contrasenaUsuarios;
    }
    
    public String getCedulaUsuarios(){
        return cedulaUsuarios;
    }
    
    public void setCedulaUsuarios(String cedulaUsuarios){
        this.cedulaUsuarios=cedulaUsuarios;
    }
    
    public String getCorreoUsuarios(){
        return correoUsuarios;
    }
    
    public void setCorreoUsuarios(String correoUsuarios){
        this.correoUsuarios=correoUsuarios;
    }
    
    public Boolean  getEstadoUsuarios(){
        return estadoUsuarios;
    }
    
    public void setEstadoUsuarios(Boolean estadoUsuarios){
        this.estadoUsuarios=estadoUsuarios;
    }
    
    
    
    @Override
    public String toString(){
        return this.tipoUsuarios;
    }
    
       
}
