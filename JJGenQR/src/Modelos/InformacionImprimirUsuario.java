

package Modelos;

import com.mysql.jdbc.StringUtils;
import db.mysql;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class InformacionImprimirUsuario {
    static Connection con;
    static Statement st;
    String accion;
    
    public LinkedList getInformacion(){
	LinkedList info = new LinkedList();
	ModeloImprimirUsuario consulta;
        //String idA = ItemSeleccionado.idArticulo;
	try {
            if(con == null) con = mysql.getConnect();
            st = con.createStatement();
            ResultSet rs = null;
            accion = ItemSeleccionado.accionBoton;
            rs = st.executeQuery("SELECT substring(TIPOUSUARIO, 1, 1) AS TIPOUSUARIO,NOMBRESUSUARIO,APELLIDOSUSUARIO,CONTRASENAUSUARIO,CEDULAUSUARIO,CORREOUSUARIO,ESTADOUSUARIO  FROM usuarios");
            while (rs.next())
            {
                consulta = new ModeloImprimirUsuario();
                //agregamos la informacion a el objeto consulta
                consulta.setTipoUsuario(rs.getObject("TIPOUSUARIO").toString());
                consulta.setNombreUsuario(rs.getObject("NOMBRESUSUARIO").toString());
                consulta.setApellidoUsuario(rs.getObject("APELLIDOSUSUARIO").toString());
                if(accion.contains("Administrador")) consulta.setContrasenaUsuario(rs.getObject("CONTRASENAUSUARIO").toString());
                else consulta.setContrasenaUsuario("********");
                consulta.setCedulaUsuario(rs.getObject("CEDULAUSUARIO").toString());
                consulta.setCorreoUsuario(rs.getObject("CORREOUSUARIO").toString());
                consulta.setEstadoUsuario(rs.getBoolean("ESTADOUSUARIO"));
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

    
   