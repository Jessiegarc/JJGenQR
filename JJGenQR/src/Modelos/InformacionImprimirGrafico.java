package Modelos;

import db.mysql;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InformacionImprimirGrafico {
    static Connection con;
    static Statement st;
    String accion;
    
    public LinkedList getInformacion(){
	LinkedList info = new LinkedList();
	ModeloImprimirGraficos consulta;
	try {
            if(con == null) con = mysql.getConnect();
            st = con.createStatement();
            ResultSet rs = null;
            accion = ItemSeleccionado.accionBoton;
            if(ItemSeleccionado.rol.contains("Vis")) rs = st.executeQuery("SELECT COUNT(*) AS contador FROM historialvisitas"); 
            else rs = st.executeQuery("SELECT COUNT(*) AS contador FROM historialdispositivos");
            rs.next();
            consulta = new ModeloImprimirGraficos();
            //agregamos la informacion a el objeto consulta
            consulta.setContador(rs.getObject("contador").toString());
            info.add(consulta);
            rs.close();
            return info;
	} catch (SQLException ex) {
            Logger.getLogger(InformacionImprimirUsuario.class.getName()).log(Level.SEVERE, null, ex);
	}
	return null;
    }
}
