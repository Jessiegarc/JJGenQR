
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


public class InformacionImprimirCategorias {
    static Connection con;
    static Statement st;
    
    public LinkedList getInformacion(){
	LinkedList info = new LinkedList();
	ModeloImprimirCategoria consulta;
        //String idA = ItemSeleccionado.idArticulo;
	try {
            if(con == null) con = mysql.getConnect();
            st = con.createStatement();
            ResultSet rs = null;
            rs = st.executeQuery("SELECT IDCATEGORIA,NOMBRECATEGORIA,DESCRIPCIONCATEGORIA FROM categorias");
            while (rs.next())
            {
                consulta = new ModeloImprimirCategoria();
                //agregamos la informacion a el objeto consulta
                consulta.setIDCategoria(rs.getObject("IDCATEGORIA").toString());
                consulta.setNombreCategoria(rs.getObject("NOMBRECATEGORIA").toString());
                consulta.setDescripcionCategoria(rs.getObject("DESCRIPCIONCATEGORIA").toString());
                info.add(consulta);
            }
            rs.close();
            return info;
	} catch (SQLException ex) {
            Logger.getLogger(InformacionImprimirCategorias.class.getName()).log(Level.SEVERE, null, ex);
	}
	return null;
    }
    
}
