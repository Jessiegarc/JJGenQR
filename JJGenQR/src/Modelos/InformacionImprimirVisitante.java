
package Modelos;

import db.mysql;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class InformacionImprimirVisitante {
    static Connection con;
    static Statement st;
    String accion;
    
    public LinkedList getInformacion(){
	LinkedList info = new LinkedList();
	ModeloImprimirVisitante consulta;
        String fechaIni = ItemSeleccionado.fechaInicio;
        String fechaFin = ItemSeleccionado.fechaFinal;
	try {
            if(con == null) con = mysql.getConnect();
            st = con.createStatement();
            ResultSet rs = null;
            accion = ItemSeleccionado.accionBoton;
            if (accion.contains("1")) rs = st.executeQuery("SELECT * FROM historialvisitas UNION SELECT * FROM historialdispositivos ORDER BY FECHAHORAVISITA ASC"); 
            else if(accion.contains("2")) rs = st.executeQuery("SELECT * FROM historialvisitas WHERE DATE(FECHAHORAVISITA) BETWEEN '" + fechaIni + "' AND '" + fechaIni + "' UNION SELECT * FROM historialdispositivos WHERE DATE(FECHAVISITADISPOSITIVO) BETWEEN '" + fechaIni + "' AND '" + fechaIni + "' ORDER BY FECHAHORAVISITA ASC");
            else if(accion.contains("3")) rs = st.executeQuery("SELECT * FROM historialvisitas WHERE DATE(FECHAHORAVISITA) BETWEEN '" + fechaIni + "' AND '" + fechaFin + "' UNION SELECT * FROM historialdispositivos WHERE DATE(FECHAVISITADISPOSITIVO) BETWEEN '" + fechaIni + "' AND '" + fechaFin + "' ORDER BY FECHAHORAVISITA ASC");
            else if(accion.contains("4")) rs = st.executeQuery("SELECT * FROM historialvisitas ORDER BY FECHAHORAVISITA ASC");
            else if(accion.contains("5")) rs = st.executeQuery("SELECT * FROM historialvisitas WHERE DATE(FECHAHORAVISITA) BETWEEN '" + fechaIni + "' AND '" + fechaIni + "' ORDER BY FECHAHORAVISITA ASC");
            else if(accion.contains("6")) rs = st.executeQuery("SELECT * FROM historialvisitas where DATE(FECHAHORAVISITA) BETWEEN '" + fechaIni + "' AND '" + fechaFin + "' ORDER BY FECHAHORAVISITA ASC");
            else if(accion.contains("7")) rs = st.executeQuery("SELECT IDHISTORIALDISPOSITIVOS AS IDHISTORIALVISITA, IDDISPOSITIVO AS NOMBRESAPELLIDOSVISITANTE, FECHAVISITADISPOSITIVO AS FECHAHORAVISITA FROM historialdispositivos ORDER BY FECHAVISITADISPOSITIVO ASC");
            else if(accion.contains("8")) rs = st.executeQuery("SELECT IDHISTORIALDISPOSITIVOS AS IDHISTORIALVISITA, IDDISPOSITIVO AS NOMBRESAPELLIDOSVISITANTE, FECHAVISITADISPOSITIVO AS FECHAHORAVISITA FROM historialdispositivos WHERE DATE(FECHAVISITADISPOSITIVO) BETWEEN '" + fechaIni + "' AND '" + fechaIni + "' ORDER BY FECHAVISITADISPOSITIVO ASC");
            else if(accion.contains("9")) rs = st.executeQuery("SELECT IDHISTORIALDISPOSITIVOS AS IDHISTORIALVISITA, IDDISPOSITIVO AS NOMBRESAPELLIDOSVISITANTE, FECHAVISITADISPOSITIVO AS FECHAHORAVISITA FROM historialdispositivos WHERE DATE(FECHAVISITADISPOSITIVO) BETWEEN '" + fechaIni + "' AND '" + fechaFin + "' ORDER BY FECHAVISITADISPOSITIVO ASC");
            while (rs.next())
            {
                consulta = new ModeloImprimirVisitante();
                //agregamos la informacion a el objeto consulta
                consulta.setIDVisitante(rs.getObject("IDHISTORIALVISITA").toString());
                consulta.setNombresapellidosVisitante(rs.getObject("NOMBRESAPELLIDOSVISITANTE").toString());
                consulta.setFechahoraVisita(rs.getObject("FECHAHORAVISITA").toString());
                info.add(consulta);
            }
            rs.close();
            return info;
            
            /*if (accion.contains("ImprimirTotal")) rs = st.executeQuery("SELECT NOMBRESAPELLIDOSVISITANTE, FECHAHORAVISITA FROM historialvisitas");
            else if(accion.contains("ImprimirParcial")) rs = st.executeQuery("SELECT NOMBRESAPELLIDOSVISITANTE, FECHAHORAVISITA FROM historialvisitas WHERE IDHISTORIALVISITA = " + idV);
            while (rs.next())
            {
                    consulta = new ModeloImprimirVisitante();
                    //agregamos la informacion a el objeto consulta
                    consulta.setNombresapellidosVisitante(rs.getObject("NOMBRESAPELLIDOSVISITANTE").toString());
                    consulta.setFechahoraVisita(rs.getObject("FECHAHORAVISITA").toString());
                    info.add(consulta);
            }
            rs.close();
            return info;*/
	} catch (SQLException ex) {
            Logger.getLogger(InformacionImprimirUsuario.class.getName()).log(Level.SEVERE, null, ex);
	}
	return null;
    }
    
}
