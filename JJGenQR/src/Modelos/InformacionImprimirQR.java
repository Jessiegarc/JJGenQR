package Modelos;

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

public class InformacionImprimirQR {
    static Connection con;
    static Statement st;
    String accion;
    
    public LinkedList getInformacion(){
	LinkedList info = new LinkedList();
	ModeloImprimirQR consulta;
        String idA = ItemSeleccionado.idArticulo;
	try {
            if(con == null) con = mysql.getConnect();
            st = con.createStatement();
            ResultSet rs = null;
            accion = ItemSeleccionado.accionBoton;
            if (accion.contains("ImprimirTotal")) rs = st.executeQuery("SELECT NOMBREARTICULO, IMAGENQRARTICULO FROM articulos");
            else if(accion.contains("ImprimirParcial")) rs = st.executeQuery("SELECT NOMBREARTICULO, IMAGENQRARTICULO FROM articulos WHERE IDARTICULO = " + idA);
            while (rs.next())
            {
                try {
                    consulta = new ModeloImprimirQR();
                    //agregamos la informacion a el objeto consulta
                    consulta.setNombreArticulo(rs.getObject("NOMBREARTICULO").toString());
                    if(!rs.getString("IMAGENQRARTICULO").isEmpty()){
                        //Image capturarImgSoloLectura = ImageIO.read(new File(rs.getString("IMAGENQRARTICULO")));
                        BufferedImage imagen = ImageIO.read(new File(rs.getString("IMAGENQRARTICULO")));
                        BufferedImage imagenARGB = convertToARGB(imagen);
                        int colorWhite, colorTranspa;
                        Color colorAux;
                        for (int i = 0; i < imagen.getWidth(); i++) {
                            for (int j = 0; j < imagen.getHeight(); j++) {
                                int alpha = 0;
                                colorAux = new Color(imagen.getRGB(i, j));
                                colorWhite = (int) ((colorAux.getRed() + colorAux.getGreen() + colorAux.getBlue())/3);
                                colorTranspa = (alpha << 24) | (colorWhite << 16) | (colorWhite << 8) | colorWhite;
                                if(colorWhite == 255) imagenARGB.setRGB(i, j, colorTranspa);
                            }
                        }
                        //ImageIcon icono = new ImageIcon(imagenARGB);
                        consulta.setImagenQRArticulo(imagenARGB);
                    } else consulta.setImagenQRArticulo(null);
                    info.add(consulta);
                } catch (IOException ex) {
                    Logger.getLogger(InformacionImprimirQR.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            rs.close();
            return info;
	} catch (SQLException ex) {
		Logger.getLogger(InformacionImprimirQR.class.getName()).log(Level.SEVERE, null, ex);
	}
	return null;
    }
    
    public static BufferedImage convertToARGB(BufferedImage image)
    {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }
}
