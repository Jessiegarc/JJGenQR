
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
            if (accion.contains("1")) rs = st.executeQuery("SELECT * FROM historialvisitas UNION SELECT * FROM historialdispositivos ORDER BY IDHISTORIALVISITA DESC"); 
            else if(accion.contains("2")) rs = st.executeQuery("SELECT * FROM historialvisitas where substring(FECHAHORAVISITA,1,10)  = '" + fechaIni + "' UNION SELECT * FROM historialdispositivos where substring(FECHAVISITADISPOSITIVO,1,10) = '" + fechaIni + "' ORDER BY IDHISTORIALVISITA DESC");
            else if(accion.contains("3")) rs = st.executeQuery("SELECT * FROM historialvisitas where substring(FECHAHORAVISITA,1,10)  >= '" + fechaIni + "' AND substring(FECHAHORAVISITA,1,10) <='" + fechaFin + "'UNION SELECT * FROM historialdispositivos where substring(FECHAVISITADISPOSITIVO,1,10) >= '" + fechaIni + "' AND substring(FECHAVISITADISPOSITIVO,1,10) <='" + fechaFin + "' ORDER BY IDHISTORIALVISITA DESC");
            else if(accion.contains("4")) rs = st.executeQuery("SELECT * FROM historialvisitas where substring(FECHAHORAVISITA,1,10)  >= '" + fechaIni + "' AND substring(FECHAHORAVISITA,1,10) <='" + fechaFin + "'UNION SELECT * FROM historialdispositivos where substring(FECHAVISITADISPOSITIVO,1,10) >= '" + fechaIni + "' AND substring(FECHAVISITADISPOSITIVO,1,10) <='" + fechaFin + "' ORDER BY IDHISTORIALVISITA DESC");
            while (rs.next())
            {
                    consulta = new ModeloImprimirVisitante();
                    //agregamos la informacion a el objeto consulta
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
