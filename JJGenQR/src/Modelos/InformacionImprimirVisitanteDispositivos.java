
package Modelos;

import db.mysql;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class InformacionImprimirVisitanteDispositivos {
    static Connection con;
    static Statement st;
    String accion;
    
    public LinkedList getInformacion(){
	LinkedList info = new LinkedList();
	ModeloImprimirVisitanteDispositivo consulta;
        String idVD = ItemSeleccionado.idVisitanteDispositivo;
        
	try {
            if(con == null) con = mysql.getConnect();
            st = con.createStatement();
            ResultSet rs = null;
            accion = ItemSeleccionado.accionBoton;
            if (accion.contains("ImprimirTotal")) rs = st.executeQuery("SELECT IDDISPOSITIVO, FECHAVISITADISPOSITIVO FROM historialdispositivos");
            else if(accion.contains("ImprimirParcial")) rs = st.executeQuery("SELECT IDDISPOSITIVO, FECHAVISITADISPOSITIVO FROM historialdispositivos WHERE IDHISTORIALDISPOSITIVOS = " + idVD);
            while (rs.next())
            {
                    consulta = new ModeloImprimirVisitanteDispositivo();
                    //agregamos la informacion a el objeto consulta
                    consulta.setIdDispositivo(rs.getObject("IDDISPOSITIVO").toString());
                    consulta.setFechavisitaDispositivo(rs.getObject("FECHAVISITADISPOSITIVO").toString());
                    info.add(consulta);
            }
            rs.close();
            return info;
	} catch (SQLException ex) {
            Logger.getLogger(InformacionImprimirUsuario.class.getName()).log(Level.SEVERE, null, ex);
	}
	return null;
    }
    
}
