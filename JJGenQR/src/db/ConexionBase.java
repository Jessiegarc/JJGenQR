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
    
    public static Vector<Anios> leerDatosVector2(String consulta2){
        Vector<Anios> anios= new Vector<Anios>();
        Anios ani=null;
        if(con==null) con = mysql.getConnect();
        ani=new Anios();
        ani.setIdHistorialVisita(0);
        ani.setfechaHoraVisita("--Escoja un año--");
        //ani.setidDispositivo("");
        anios.add(ani);
        try {
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(consulta2);
            while(rs.next()){
                ani=new Anios();
                ani.setIdHistorialVisita(rs.getInt(1));
                ani.setfechaHoraVisita(rs.getString(2));
                //ani.setidDispositivo(rs.getString(3));
                anios.add(ani); 
            }
            sent.close();
            rs.close();
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        return anios;
    }
    
    
    public static Vector<AniosDispositivo> leerDatosVector3(String consulta3){
        Vector<AniosDispositivo> aniosd= new Vector<AniosDispositivo>();
        AniosDispositivo anid=null;
        if(con==null) con = mysql.getConnect();
        anid=new AniosDispositivo();
        anid.setIdhistorialvisitadispositivo(0);
        anid.setFechahoravisitadispositivo("--Escoja un año--");
        //ani.setidDispositivo("");
        aniosd.add(anid);
        try {
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(consulta3);
            while(rs.next()){
                anid=new AniosDispositivo();
                anid.setIdhistorialvisitadispositivo(rs.getInt(1));
                anid.setFechahoravisitadispositivo(rs.getString(2));
                aniosd.add(anid); 
            }
            sent.close();
            rs.close();
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        return aniosd;
    
    
    }
}

    
