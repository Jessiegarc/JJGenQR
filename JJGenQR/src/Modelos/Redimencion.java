/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

import java.awt.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Jess
 */
public class Redimencion {
    
    public void Mostrar_Visualizador(JLabel Pantalla, String ruta){
        ImageIcon fot = new ImageIcon(getClass().getResource(ruta));
        Icon icono = new ImageIcon(fot.getImage().getScaledInstance(Pantalla.getWidth(), Pantalla.getHeight(), Image.SCALE_DEFAULT));
        Pantalla.setIcon(icono);
        Pantalla.repaint();
    }
    
   /* public File getJasper(String ruta){
        try {
            File directorio = new File("temp");
            directorio.mkdirs();
            directorio.setWritable(true);
            //copias la direccion
            String archivo = directorio.getCanonicalPath() + File.pathSeparator + "erfc.pdf";
            //nuevo archivo en esa direccion
            File temp = new File(archivo);
            InputStream is = this.getClass().getResourceAsStream(ruta);
            FileOutputStream archivoDestino = new FileOutputStream(temp);
            FileWriter fw = new FileWriter(temp);
            byte[] buffer = new byte[512*1024];
            //lees el archivo hasta que se acabe...
            int nbLectura;
            while ((nbLectura = is.read(buffer)) != -1)
                archivoDestino.write(buffer, 0, nbLectura);
            //cierras el archivo,el inputS y el FileW
            fw.close();
            archivoDestino.close();
            is.close();
            return temp;
        } catch (IOException ex) {
            Logger.getLogger(InformacionImprimirUsuario.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }*/
}
