/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Vector;
import java.sql.ResultSet;
import db.mysql;
import javax.swing.JOptionPane;



/**
 *
 * @author USUARIO
 */
public class ConexionBase {
static Connection con;
static Statement sent;    
    
    
    public static Vector<Usuarios> leerDatosVector(String consulta){
        Vector<Usuarios> usuarios= new Vector<Usuarios> ();
        Usuarios us=null;
        if(con==null) con = mysql.getConnect();
        try {
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(consulta);
            while(rs.next()){
                //JOptionPane.showMessageDialog(null, rs.getString(2));
                us=new Usuarios();
                us.setIdUsuarios(rs.getInt(1));
                us.setTipoUsuarios(rs.getString(2));
                us.setNombresUsuarios(rs.getString(3));
                us.setApellidoUsuarios(rs.getString(4));
                us.setContrasenaUsuarios(rs.getString(5));
                us.setCedulaUsuarios(rs.getString(6));
                us.setCorreoUsuarios(rs.getString(7));
                //JOptionPane.showMessageDialog(null, String.valueOf(rs.getBoolean(7)));
                us.setEstadoUsuarios(rs.getBoolean(8));
                usuarios.add(us); 
             }
            sent.close();
            rs.close();
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        return usuarios;
    }
    
    
    public static Vector<Categorias> leerDatosVector1(String consulta1){
        Vector<Categorias> categorias= new Vector<Categorias>();
        Categorias cat=null;
        if(con==null) con = mysql.getConnect();
        cat=new Categorias();
        cat.setIdCategoria(0);
        cat.setNombreCategoria("--Seleccione una categoria--");
        cat.setDescripcionCategoria("");
        categorias.add(cat);
        try {
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(consulta1);
            while(rs.next()){
                cat=new Categorias();
                cat.setIdCategoria(rs.getInt(1));
                cat.setNombreCategoria(rs.getString(2));
                cat.setDescripcionCategoria(rs.getString(3));
                categorias.add(cat); 
            }
            sent.close();
            rs.close();
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        return categorias;
    }
    
   } 
    
