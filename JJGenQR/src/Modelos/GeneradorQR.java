/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Writer;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.EnumMap;
/**
 *
 * @author Niborson
 */
public class GeneradorQR {
    
    public GeneradorQR() {}
    
    public BufferedImage CrearQR(String texto, int tamano){
        BitMatrix matrix;
        Writer escrib = new MultiFormatWriter();
        try {
            EnumMap<EncodeHintType, String> pasos = new EnumMap<EncodeHintType, String>(EncodeHintType.class);
            pasos.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            matrix = escrib.encode(texto, BarcodeFormat.QR_CODE, tamano, tamano, pasos);
            ByteArrayOutputStream salida = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", salida);
            byte[] array = salida.toByteArray();
            ByteArrayInputStream entrada = new ByteArrayInputStream(array);
            return ImageIO.read(entrada);
        } catch (com.google.zxing.WriterException ex){
            System.err.println(ex.getMessage());
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }
}
