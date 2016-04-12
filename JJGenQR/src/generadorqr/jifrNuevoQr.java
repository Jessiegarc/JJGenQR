/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generadorqr;

import Modelos.ItemSeleccionado;
import Modelos.GeneradorQR;
import Modelos.ValoresConstantes;
import db.Categorias;
import db.ConexionBase;
import db.mysql;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import java.awt.Image;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.io.FileUtils;
/**
 *
 * @author Jess
 */
public class jifrNuevoQr extends javax.swing.JInternalFrame {
Connection conn;
Statement sent;
File fichero;
int numeroAleatorioTitulo = 0, desde = 10000, hasta = 99999, idCategoria = 0, id = 0, idCat = 0;
jifrGestionArticulos internalGestionArticulos;
ItemSeleccionado as = new ItemSeleccionado();
BufferedImage bufferedImage;
DefaultComboBoxModel mdlC;
Vector<Categorias> categorias;
String audio = "", tempAudio = "", video = "", tempVideo = "", imagenQR = "", codigoImagenQR = "", fechaActual = "", accion = "", categoria = "";
String[] imagen = {"", "", ""}, tempImagen = {"", "", ""}, tempNombreArchivo = {"", "", ""}, tempNombreMultimedia = {"", ""}, tempRutaActual = {"", "", "", "", "", ""};
    
    
    public jifrNuevoQr() {
        initComponents();
        jcbCategoriasQR.setFocusable(true);
        Random rnd = new Random();
        Date fecha = new Date();
        numeroAleatorioTitulo = rnd.nextInt(hasta-desde+1)+desde;
        DateFormat formatoFechaHora = new SimpleDateFormat("ddMMyyyyHHmmss");
        fechaActual = formatoFechaHora.format(fecha);
        //this.setLocationRelativeTo(null);
        conn = mysql.getConnect();
        lblIdQR.setVisible(false);
        String SQLC="SELECT IDCATEGORIA,NOMBRECATEGORIA,DESCRIPCIONCATEGORIA FROM categorias";
        mdlC= new DefaultComboBoxModel(ConexionBase.leerDatosVector1(SQLC));
        categorias = ConexionBase.leerDatosVector1(SQLC);
        this.jcbCategoriasQR.setModel(mdlC);
        lblIdQR.setVisible(false);
        accion=ItemSeleccionado.accionBoton;
        btnGenerarNuevoQr.setText(accion);
        try{
            //Muestra los usuarios existentes en la base de datos
            if(accion.contains("Actualizar")){
                txtNombreQr.setEnabled(false);
                jlGenerarQr.setText(accion + " datos del QR");
                lblIdQR.setText("ID del Usuario: \t\t" + ItemSeleccionado.idArticulo);
                lblIdQR.setVisible(true);
                for (int i = 0; i < categorias.size(); i++) {
                    String tempCategoria = categorias.get(i).getNombreCategoria();
                    if(tempCategoria.contains(ItemSeleccionado.idCategoria)) idCat = i;
                }
                jcbCategoriasQR.setSelectedIndex(idCat);
                String SQLTU ="SELECT * FROM articulos WHERE IDARTICULO = " + ItemSeleccionado.idArticulo; 
                sent = conn.createStatement();
                ResultSet rs = sent.executeQuery(SQLTU);
                rs.next();
                txtNombreQr.setText(rs.getString("NOMBREARTICULO"));
                txtCantidadArticulo.setText(rs.getString("CANTIDADARTICULO"));
                txtAreaDescripcionNuevoQr.setText(rs.getString("DESCRIPCIONARTICULO"));
                tempRutaActual[0] = rs.getString("IMAGENUNOARTICULO");
                tempRutaActual[1] = rs.getString("IMAGENDOSARTICULO");
                tempRutaActual[2] = rs.getString("IMAGENTRESARTICULO");
                tempRutaActual[3] = rs.getString("SONIDOARTICULO");
                tempRutaActual[4] = rs.getString("VIDEOARTICULO");
                tempRutaActual[5] = rs.getString("IMAGENQRARTICULO");
                rs.close();
                Mostrar_Visualizador(btnImagen1, tempRutaActual[0]);
                if(!tempRutaActual[1].isEmpty()) Mostrar_Visualizador(btnImagen2, tempRutaActual[1]);
                if(!tempRutaActual[2].isEmpty()) Mostrar_Visualizador(btnImagen3, tempRutaActual[2]);
                if(!tempRutaActual[3].isEmpty()) jlAudioQr.setText("Audio.mp3");
                if(!tempRutaActual[4].isEmpty()) jlVideoQr.setText("Video.mp4");
                Mostrar_Visualizador(lblImagenQR, tempRutaActual[5]);
            }
        }
        catch(Exception e){
        }
    }
    
    Boolean CopiaArchivos(String home, String destiny, String[] multimedia, String nombre, Integer indice){
        File origen = new File(home);
        File destino = new File(destiny);
        try {
            //Localisa la carpeta de origen y ubica la carpeta d destino
            File rutaPrincipalImagenes = new File(multimedia[indice]);
            if(!rutaPrincipalImagenes.exists()) rutaPrincipalImagenes.mkdir();
            FileUtils.copyFileToDirectory(origen, destino, false);
            File nombreOriginal = new File(destiny + "\\" + tempNombreArchivo[indice]);
            File nombreModificado = new File(destiny + "\\" + nombre);
            Boolean cambioNombre = nombreOriginal.renameTo(nombreModificado);
            if(!cambioNombre){
                JOptionPane.showMessageDialog(this, "El renombrado no se pudo realizar");
                return false;
            }
        } catch (Exception e) {
        }
        return true;
    }
    
    Boolean CopiaArchivos(String home, String destiny, String nombre, Integer indice){
        File origen = new File(home);
        File destino = new File(destiny);
        try {
            //Localisa la carpeta de origen y ubica la carpeta d destino
            File rutaPrincipalMultimedia = new File(destiny);
            if(!rutaPrincipalMultimedia.exists()) rutaPrincipalMultimedia.mkdir();
            FileUtils.copyFileToDirectory(origen, destino, false);
            File nombreOriginal = new File(destiny + "\\" + tempNombreMultimedia[indice]);
            File nombreModificado = new File(destiny + "\\" + nombre);
            Boolean cambioNombre = nombreOriginal.renameTo(nombreModificado);
            if(!cambioNombre){
                JOptionPane.showMessageDialog(this, "El renombrado no se pudo realizar");
                return false;
            }
        } catch (Exception e) {
        }
        return true;
    }
    
    void GuardarQr(){
        if (txtNombreQr.getText().trim().isEmpty() || txtAreaDescripcionNuevoQr.getText().trim().isEmpty())
            JOptionPane.showMessageDialog(null, "Ingrese Los Campos Obligatorios");
        else{  
            try {
                if(btnGenerarNuevoQr.getText().contains("Guardar")){
                    if(idCategoria == 0){
                        JOptionPane.showMessageDialog(this, "Debe de seleccionar una categoría");
                        return;
                    }
                    //Ingreso en nuevo usuario
                    if(!tempImagen[0].isEmpty()){
                        File imagen1 = new File(imagen[0]);
                        if(!imagen1.exists()) imagen1.mkdir();
                        imagen[0] += "\\Imagenes";
                        if(CopiaArchivos(tempImagen[0], imagen[0], imagen, "Imagen1.jpg", 0)) imagen[0] += "\\Imagen1.jpg";
                        else return;
                        if(!tempImagen[1].isEmpty()){
                            if(CopiaArchivos(tempImagen[1], imagen[1], imagen, "Imagen2.jpg", 1)) imagen[1] += "\\Imagen2.jpg";
                            else return;
                        }
                        if(!tempImagen[2].isEmpty()){
                            if(CopiaArchivos(tempImagen[2], imagen[2], imagen, "Imagen3.jpg", 2)) imagen[2] += "\\Imagen3.jpg";
                            else return;
                        }
                        if(!tempAudio.isEmpty()){
                            if(CopiaArchivos(tempAudio, audio, "Audio.mp3", 0)) audio += "\\Audio.mp3";
                            else return;
                        }
                        if(!tempVideo.isEmpty()){
                            if(CopiaArchivos(tempVideo, video, "Video.mp4", 1)) video += "\\Video.mp4";
                            else return;
                        }
                        File guardarQR = new File(imagenQR);
                        try {
                            ImageIO.write(bufferedImage, "png", guardarQR);
                        } catch (Exception e) {
                        }
                        String SQLA = "INSERT INTO articulos(IDCATEGORIA,NOMBREARTICULO,CANTIDADARTICULO,DESCRIPCIONARTICULO,IMAGENUNOARTICULO,IMAGENDOSARTICULO,"
                                + "IMAGENTRESARTICULO,SONIDOARTICULO,VIDEOARTICULO,CODIGOQRARTICULO,IMAGENQRARTICULO)"
                                      + " VALUES(?,?,?,?,?,?,?,?,?,?,?)";
                        PreparedStatement ps = conn.prepareStatement(SQLA);
                        ps.setInt(1, idCategoria);
                        ps.setString(2, txtNombreQr.getText());
                        ps.setString(3, txtCantidadArticulo.getText());
                        ps.setString(4, txtAreaDescripcionNuevoQr.getText());
                        ps.setString(5, imagen[0]);
                        ps.setString(6, imagen[1]);
                        ps.setString(7, imagen[2]);
                        ps.setString(8, audio);
                        ps.setString(9, video);
                        ps.setString(10, codigoImagenQR);
                        ps.setString(11, imagenQR);
                        int n = ps.executeUpdate();
                        if (n > 0) {
                            JOptionPane.showMessageDialog(null, "Nuevo Qr creado Correctamente");
                            dispose();
                            internalGestionArticulos = new jifrGestionArticulos();
                            Principal.centrarVentanaGestionCA(internalGestionArticulos);
                        }
                    } else JOptionPane.showMessageDialog(this, "Debe por lo menos agregar una imagen al reconocimiento QR");
                } else {
                    if(idCategoria == idCat) idCategoria = idCat;
                    else if(idCategoria == 0){
                        JOptionPane.showMessageDialog(this, "Debe de seleccionar una categoría");
                        return;
                    }
                    
                    imagen[0] += "\\Imagenes";
                    if(tempImagen[0].isEmpty()) imagen[0] = tempRutaActual[0];
                    else {
                        File borrarImagenAntigua = new File(tempRutaActual[0]);
                        borrarImagenAntigua.delete();
                        if(CopiaArchivos(tempImagen[0], imagen[0], imagen, "Imagen1.jpg", 0)) imagen[0] += "\\Imagen1.jpg";
                        else return;
                    }
                    if(tempImagen[1].isEmpty()) imagen[1] = tempRutaActual[1];
                    else {
                        File borrarImagenAntigua = new File(tempRutaActual[1]);
                        borrarImagenAntigua.delete();
                        if(CopiaArchivos(tempImagen[1], imagen[1], imagen, "Imagen2.jpg", 1)) imagen[1] += "\\Imagen2.jpg";
                        else return;
                    }
                    if(tempImagen[2].isEmpty()) imagen[2] = tempRutaActual[2];
                    else {
                        File borrarImagenAntigua = new File(tempRutaActual[2]);
                        borrarImagenAntigua.delete();
                        if(CopiaArchivos(tempImagen[2], imagen[2], imagen, "Imagen3.jpg", 2)) imagen[2] += "\\Imagen3.jpg";
                        else return;
                    }
                    if(tempAudio.isEmpty()) audio = tempRutaActual[3];
                    else {
                        File borrarMultimediaAntigua = new File(tempRutaActual[3]);
                        borrarMultimediaAntigua.delete();
                        if(CopiaArchivos(tempAudio, audio, "Audio.mp3", 0)) audio += "\\Audio.mp3";
                        else return;
                    }
                    if(tempVideo.isEmpty()) video = tempRutaActual[4];
                    else {
                        File borrarMultimediaAntigua = new File(tempRutaActual[4]);
                        borrarMultimediaAntigua.delete();
                        if(CopiaArchivos(tempVideo, video, "Video.mp4", 1)) video += "\\Video.mp4";
                        else return;
                    }
                    if (txtNombreQr.getText().trim().isEmpty() || txtAreaDescripcionNuevoQr.getText().trim().isEmpty())
                        JOptionPane.showMessageDialog(null, "Ingrese Los Campos Obligatorios");
                    else{
                        String SQL = "UPDATE articulos SET IDCATEGORIA = ?, NOMBREARTICULO = ?,CANTIDADARTICULO = ?, DESCRIPCIONARTICULO = ?, IMAGENUNOARTICULO = ?, "
                                + "IMAGENDOSARTICULO = ?, IMAGENTRESARTICULO = ?, SONIDOARTICULO = ?, VIDEOARTICULO = ? "
                                + "WHERE IDARTICULO = " + ItemSeleccionado.idArticulo;
                        PreparedStatement ps = conn.prepareStatement(SQL);
                        ps.setInt(1, idCategoria);
                        ps.setString(2, txtNombreQr.getText());
                        ps.setString(3, txtCantidadArticulo.getText());
                        ps.setString(4, txtAreaDescripcionNuevoQr.getText());
                        ps.setString(5, imagen[0]);
                        ps.setString(6, imagen[1]);
                        ps.setString(7, imagen[2]);
                        ps.setString(8, audio);
                        ps.setString(9, video);
                        int n = ps.executeUpdate();
                        if (n > 0) {
                            JOptionPane.showMessageDialog(null, "Información del QR actualizada Correctamente");
                            dispose();
                            internalGestionArticulos = new jifrGestionArticulos();
                            Principal.centrarVentanaGestionCA(internalGestionArticulos);
                        }
                    }
                }
            }catch (SQLException e) {
                JOptionPane.showConfirmDialog(null, "Error: " + e.getMessage());
                File borrarDirectorio = new File(ValoresConstantes.DIRECTORIO_PRINCIPAL + "\\" + txtNombreQr.getText());
                if(borrarDirectorio.exists()) try {
                    FileUtils.deleteDirectory(borrarDirectorio);
                } catch (IOException ex) {
                    Logger.getLogger(jifrNuevoQr.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static void Mostrar_Visualizador(JLabel Pantalla, String RutaDestino){
        try
        {
            Image capturarImgSoloLectura = ImageIO.read(new File(RutaDestino));
            Image obtenerImagen = capturarImgSoloLectura.getScaledInstance(Pantalla.getPreferredSize().width, Pantalla.getPreferredSize().height, Image.SCALE_SMOOTH);
            Icon iconoEscalado = new ImageIcon(obtenerImagen);
            Pantalla.setIcon(iconoEscalado);
        }
        catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
    
    void CargarImagen(JLabel label, Integer identificador){
        int resultado;
        // ventana = new CargarFoto();
        JFileChooser jfchCargarfoto= new JFileChooser();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("JPG", "jpg");
        jfchCargarfoto.setFileFilter(filtro);
        resultado= jfchCargarfoto.showOpenDialog(null);
        if (JFileChooser.APPROVE_OPTION == resultado){
            fichero = jfchCargarfoto.getSelectedFile();
            try{
                tempImagen[identificador] = fichero.getPath();
                tempNombreArchivo[identificador] = fichero.getName();
                ImageIcon icon = new ImageIcon(fichero.toString());
                Icon icono = new ImageIcon(icon.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_DEFAULT));
                label.setText(null);
                label.setIcon(icono);
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null, "Error abriendo la imagen" + ex);
            }
        } 
    }
    
    void CargarVideo(JLabel label, Integer identificador){
        int resultado;
        // ventana = new CargarFoto();
        JFileChooser jfchCargarVideo= new JFileChooser();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("MP4", "mp4");
        jfchCargarVideo.setFileFilter(filtro);
        resultado= jfchCargarVideo.showOpenDialog(null);
        if (JFileChooser.APPROVE_OPTION == resultado){
            fichero = jfchCargarVideo.getSelectedFile();
            try{
                tempVideo = fichero.getPath();
                tempNombreMultimedia[identificador] = fichero.getName();
                label.setText(tempNombreMultimedia[identificador]);
                if(label.getText().length() > 10) label.setText(tempNombreMultimedia[identificador].substring(0, 10) + "...mp4");
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null, "Error abriendo la imagen" + ex);
            }
        } 
    }
    
    void CargarAudio(JLabel label, Integer identificador){
        int resultado;
        // ventana = new CargarFoto();
        JFileChooser jfchCargarVideo= new JFileChooser();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("MP3", "mp3");
        jfchCargarVideo.setFileFilter(filtro);
        resultado= jfchCargarVideo.showOpenDialog(null);
        if (JFileChooser.APPROVE_OPTION == resultado){
            fichero = jfchCargarVideo.getSelectedFile();
            try{
                tempAudio = fichero.getPath();
                tempNombreMultimedia[identificador] = fichero.getName();
                label.setText(tempNombreMultimedia[identificador]);
                if(label.getText().length() > 10) label.setText(tempNombreMultimedia[identificador].substring(0, 10) + "...mp3");
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null, "Error abriendo la imagen" + ex);
            }
        } 
    }
    
    public void setImagenQR(BufferedImage bufferedImage){
        if(bufferedImage != null){
            this.bufferedImage = bufferedImage;
            ImageIcon icon = new ImageIcon(bufferedImage);
            lblImagenQR.setIcon(icon);
        }
    }
    
    void Limpiar(){
        as.setAccionBoton("");
        as.setIdArticulo("");
        as.setIdCategoria("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblIdQR = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jlCategoriaQr = new javax.swing.JLabel();
        jcbCategoriasQR = new javax.swing.JComboBox();
        jlNombreQr = new javax.swing.JLabel();
        txtNombreQr = new javax.swing.JTextField();
        jlImagen1 = new javax.swing.JLabel();
        jlImagen2 = new javax.swing.JLabel();
        jlImagen3 = new javax.swing.JLabel();
        jlVideoQr = new javax.swing.JLabel();
        btnVideoQr = new javax.swing.JLabel();
        jlAudioQr = new javax.swing.JLabel();
        btnAudioQr = new javax.swing.JLabel();
        jlNombreQr6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaDescripcionNuevoQr = new javax.swing.JTextArea();
        btnImagen1 = new javax.swing.JLabel();
        btnImagen3 = new javax.swing.JLabel();
        btnImagen2 = new javax.swing.JLabel();
        jlCategoriaQr1 = new javax.swing.JLabel();
        txtCantidadArticulo = new javax.swing.JTextField();
        lblImagenQR = new javax.swing.JLabel();
        jlGenerarQr = new javax.swing.JLabel();
        btnGenerarNuevoQr = new javax.swing.JButton();
        btnCancelarNuevoQr = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(34, 81, 122));

        lblIdQR.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        lblIdQR.setForeground(new java.awt.Color(0, 153, 204));
        lblIdQR.setText("Categoria");

        jPanel2.setBackground(new java.awt.Color(34, 81, 122));

        jlCategoriaQr.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jlCategoriaQr.setForeground(new java.awt.Color(255, 255, 255));
        jlCategoriaQr.setText("Categoria");

        jcbCategoriasQR.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbCategoriasQRItemStateChanged(evt);
            }
        });

        jlNombreQr.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jlNombreQr.setForeground(new java.awt.Color(255, 255, 255));
        jlNombreQr.setText("Nombre del Articulo");

        txtNombreQr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNombreQrKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreQrKeyTyped(evt);
            }
        });

        jlImagen1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jlImagen1.setForeground(new java.awt.Color(255, 255, 255));
        jlImagen1.setText("Imagen 1");

        jlImagen2.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jlImagen2.setForeground(new java.awt.Color(255, 255, 255));
        jlImagen2.setText("Imagen 2");

        jlImagen3.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jlImagen3.setForeground(new java.awt.Color(255, 255, 255));
        jlImagen3.setText("Imagen 3");

        jlVideoQr.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jlVideoQr.setForeground(new java.awt.Color(255, 255, 255));
        jlVideoQr.setText("Video");

        btnVideoQr.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/video.png"))); // NOI18N
        btnVideoQr.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVideoQr.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnVideoQrMouseClicked(evt);
            }
        });

        jlAudioQr.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jlAudioQr.setForeground(new java.awt.Color(255, 255, 255));
        jlAudioQr.setText("Audio");

        btnAudioQr.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/audio.png"))); // NOI18N
        btnAudioQr.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAudioQr.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAudioQrMouseClicked(evt);
            }
        });

        jlNombreQr6.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jlNombreQr6.setForeground(new java.awt.Color(255, 255, 255));
        jlNombreQr6.setText("Descripcion");

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        txtAreaDescripcionNuevoQr.setLineWrap(true);
        txtAreaDescripcionNuevoQr.setRows(5);
        txtAreaDescripcionNuevoQr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAreaDescripcionNuevoQrKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(txtAreaDescripcionNuevoQr);

        btnImagen1.setForeground(new java.awt.Color(255, 255, 51));
        btnImagen1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/imagen.jpg"))); // NOI18N
        btnImagen1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnImagen1.setMaximumSize(new java.awt.Dimension(72, 52));
        btnImagen1.setMinimumSize(new java.awt.Dimension(72, 52));
        btnImagen1.setPreferredSize(new java.awt.Dimension(72, 52));
        btnImagen1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnImagen1MouseClicked(evt);
            }
        });

        btnImagen3.setForeground(new java.awt.Color(255, 255, 51));
        btnImagen3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/imagen.jpg"))); // NOI18N
        btnImagen3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnImagen3.setMaximumSize(new java.awt.Dimension(72, 52));
        btnImagen3.setMinimumSize(new java.awt.Dimension(72, 52));
        btnImagen3.setPreferredSize(new java.awt.Dimension(72, 52));
        btnImagen3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnImagen3MouseClicked(evt);
            }
        });

        btnImagen2.setForeground(new java.awt.Color(255, 255, 51));
        btnImagen2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/imagen.jpg"))); // NOI18N
        btnImagen2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnImagen2.setMaximumSize(new java.awt.Dimension(72, 52));
        btnImagen2.setMinimumSize(new java.awt.Dimension(72, 52));
        btnImagen2.setPreferredSize(new java.awt.Dimension(72, 52));
        btnImagen2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnImagen2MouseClicked(evt);
            }
        });

        jlCategoriaQr1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jlCategoriaQr1.setForeground(new java.awt.Color(255, 255, 255));
        jlCategoriaQr1.setText("Cantidad");

        txtCantidadArticulo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCantidadArticulo.setText("1");
        txtCantidadArticulo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantidadArticuloKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlNombreQr, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jlCategoriaQr))
                        .addGap(14, 14, 14)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jcbCategoriasQR, 0, 199, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jlCategoriaQr1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCantidadArticulo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtNombreQr)))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                            .addGap(97, 97, 97)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btnVideoQr)
                                .addComponent(jlVideoQr, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btnAudioQr)
                                .addComponent(jlAudioQr, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                            .addGap(23, 23, 23)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnImagen1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jlImagen1, javax.swing.GroupLayout.Alignment.LEADING))
                            .addGap(117, 117, 117)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jlImagen2)
                                .addComponent(btnImagen2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(117, 117, 117)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btnImagen3, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jlImagen3)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jlNombreQr6)
                            .addGap(0, 346, Short.MAX_VALUE)))
                    .addContainerGap()))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlCategoriaQr)
                    .addComponent(jcbCategoriasQR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlCategoriaQr1)
                    .addComponent(txtCantidadArticulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlNombreQr)
                    .addComponent(txtNombreQr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnImagen1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlImagen1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnImagen2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlImagen2))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnImagen3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlImagen3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnVideoQr)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(jlVideoQr))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnAudioQr)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlAudioQr)))
                .addGap(263, 263, 263))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap(239, Short.MAX_VALUE)
                    .addComponent(jlNombreQr6)
                    .addGap(11, 11, 11)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(51, 51, 51)))
        );

        lblImagenQR.setBackground(new java.awt.Color(255, 255, 255));
        lblImagenQR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/qrjjqr.jpg"))); // NOI18N
        lblImagenQR.setMaximumSize(new java.awt.Dimension(307, 395));
        lblImagenQR.setMinimumSize(new java.awt.Dimension(307, 395));
        lblImagenQR.setPreferredSize(new java.awt.Dimension(307, 395));

        jlGenerarQr.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jlGenerarQr.setForeground(new java.awt.Color(255, 255, 255));
        jlGenerarQr.setText("Generacion de Qr");

        btnGenerarNuevoQr.setBackground(new java.awt.Color(0, 0, 0));
        btnGenerarNuevoQr.setForeground(new java.awt.Color(255, 255, 255));
        btnGenerarNuevoQr.setText("Generar");
        btnGenerarNuevoQr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarNuevoQrActionPerformed(evt);
            }
        });

        btnCancelarNuevoQr.setBackground(new java.awt.Color(0, 0, 0));
        btnCancelarNuevoQr.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelarNuevoQr.setText("Cancelar");
        btnCancelarNuevoQr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarNuevoQrActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("NUEVO CODIGO QR");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(lblIdQR)
                        .addGap(285, 285, 285)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblImagenQR, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(76, 76, 76)
                                .addComponent(jlGenerarQr)
                                .addGap(112, 112, 112)))))
                .addContainerGap(40, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(230, 230, 230)
                .addComponent(btnGenerarNuevoQr, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(250, 250, 250)
                .addComponent(btnCancelarNuevoQr, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblIdQR, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jlGenerarQr)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblImagenQR, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 448, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelarNuevoQr)
                    .addComponent(btnGenerarNuevoQr))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jcbCategoriasQRItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbCategoriasQRItemStateChanged
        idCategoria = categorias.get(jcbCategoriasQR.getSelectedIndex()).getIdCategoria();
    }//GEN-LAST:event_jcbCategoriasQRItemStateChanged

    private void txtNombreQrKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreQrKeyReleased
        if(txtNombreQr.getText().length() != 0){
            GeneradorQR gQR = new GeneradorQR();
            codigoImagenQR = txtNombreQr.getText() + "-" + fechaActual + "/" + numeroAleatorioTitulo;
            imagenQR = ValoresConstantes.DIRECTORIO_PRINCIPAL + "\\" + txtNombreQr.getText().toString() + "\\QR.png";
            setImagenQR(gQR.CrearQR(codigoImagenQR, 300));
        } else lblImagenQR.setIcon(null);
    }//GEN-LAST:event_txtNombreQrKeyReleased

    private void btnVideoQrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVideoQrMouseClicked
        CargarVideo(jlVideoQr, 1);
        video = ValoresConstantes.DIRECTORIO_PRINCIPAL+ "\\" + txtNombreQr.getText().toString() + "\\Multimedia";
    }//GEN-LAST:event_btnVideoQrMouseClicked

    private void btnAudioQrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAudioQrMouseClicked
        CargarAudio(jlAudioQr, 0);
        audio = ValoresConstantes.DIRECTORIO_PRINCIPAL + "\\" + txtNombreQr.getText().toString() + "\\Multimedia";
    }//GEN-LAST:event_btnAudioQrMouseClicked

    private void btnCancelarNuevoQrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarNuevoQrActionPerformed
        dispose();
        Limpiar();
    }//GEN-LAST:event_btnCancelarNuevoQrActionPerformed

    private void btnGenerarNuevoQrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarNuevoQrActionPerformed
        GuardarQr();
        Limpiar();
    }//GEN-LAST:event_btnGenerarNuevoQrActionPerformed

    private void btnImagen1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnImagen1MouseClicked
        CargarImagen(btnImagen1, 0);
        imagen[0] = ValoresConstantes.DIRECTORIO_PRINCIPAL + "\\" + txtNombreQr.getText().toString();
    }//GEN-LAST:event_btnImagen1MouseClicked

    private void btnImagen3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnImagen3MouseClicked
        CargarImagen(btnImagen3, 2);
        imagen[2] = ValoresConstantes.DIRECTORIO_PRINCIPAL + "\\" + txtNombreQr.getText().toString() + "\\Imagenes";
    }//GEN-LAST:event_btnImagen3MouseClicked

    private void btnImagen2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnImagen2MouseClicked
        CargarImagen(btnImagen2, 1);
        imagen[1] = ValoresConstantes.DIRECTORIO_PRINCIPAL + "\\" + txtNombreQr.getText().toString() + "\\Imagenes";
    }//GEN-LAST:event_btnImagen2MouseClicked

    private void txtAreaDescripcionNuevoQrKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaDescripcionNuevoQrKeyTyped
         int limite  = 4000;
        if (txtAreaDescripcionNuevoQr.getText().length()== limite){ 
            evt.consume();
        }
    }//GEN-LAST:event_txtAreaDescripcionNuevoQrKeyTyped

    private void txtCantidadArticuloKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadArticuloKeyTyped
        char car=evt.getKeyChar();
        if((car<'1' || car>'9')) evt.consume();
        int limite  = 3;
        if (txtCantidadArticulo.getText().length()==limite) evt.consume();
    }//GEN-LAST:event_txtCantidadArticuloKeyTyped

    private void txtNombreQrKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreQrKeyTyped
        int limite  = 50;
        if (txtNombreQr.getText().length()== limite) evt.consume();
    }//GEN-LAST:event_txtNombreQrKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnAudioQr;
    private javax.swing.JButton btnCancelarNuevoQr;
    private javax.swing.JButton btnGenerarNuevoQr;
    private javax.swing.JLabel btnImagen1;
    private javax.swing.JLabel btnImagen2;
    private javax.swing.JLabel btnImagen3;
    private javax.swing.JLabel btnVideoQr;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox jcbCategoriasQR;
    public static javax.swing.JLabel jlAudioQr;
    private javax.swing.JLabel jlCategoriaQr;
    private javax.swing.JLabel jlCategoriaQr1;
    private javax.swing.JLabel jlGenerarQr;
    private javax.swing.JLabel jlImagen1;
    private javax.swing.JLabel jlImagen2;
    private javax.swing.JLabel jlImagen3;
    private javax.swing.JLabel jlNombreQr;
    private javax.swing.JLabel jlNombreQr6;
    public static javax.swing.JLabel jlVideoQr;
    private javax.swing.JLabel lblIdQR;
    private javax.swing.JLabel lblImagenQR;
    private javax.swing.JTextArea txtAreaDescripcionNuevoQr;
    private javax.swing.JTextField txtCantidadArticulo;
    private javax.swing.JTextField txtNombreQr;
    // End of variables declaration//GEN-END:variables
}
