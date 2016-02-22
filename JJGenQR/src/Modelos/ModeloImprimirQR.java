package Modelos;

import java.awt.Image;

public class ModeloImprimirQR {
    private String nombreArticulo;
    private Image imagenQRArticulo;

    public String getNombreArticulo() {
        return nombreArticulo;
    }

    public Image getImagenQRArticulo() {
        return imagenQRArticulo;
    }

    public void setNombreArticulo(String nombreArticulo) {
        this.nombreArticulo = nombreArticulo;
    }

    public void setImagenQRArticulo(Image imagenQRArticulo) {
        this.imagenQRArticulo = imagenQRArticulo;
    }
}
