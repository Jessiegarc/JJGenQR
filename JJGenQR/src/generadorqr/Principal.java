package generadorqr;

import Modelos.ItemSeleccionado;
import Modelos.UsuarioIngresado;
import Modelos.ValoresConstantes;
import com.mysql.jdbc.StringUtils;
import com.toedter.calendar.JDateChooser;
import db.Anios;
import db.AniosDispositivo;
import db.ConexionBase;
import db.mysql;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.SpinnerDateModel;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.io.FileUtils;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.table.TableColumnModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;


public final class Principal extends javax.swing.JFrame implements Printable{
    DefaultTableModel model;
    static Connection con;
    static Statement sent;
    public String variable,variabled;
    File fichero;
    jifrTerminosyCondiciones internalTerminos;
    jifrPoliticasdePrivacidad internalPoliticas;
    jifrNuevoUsuario internalNuevoUsuario;
    jifrGestionCategoria internalGestionCategoria;
    jifrGestionArticulos internalGestionArticulo;
    jifrContactanos internalContactanos;
    ItemSeleccionado is=new ItemSeleccionado();
    String id = "", rol = "", estado = "";
    Integer buscar = 0, x = 0, y = 0,opimG=0;
    String imagenQR = "", codigoImagenQR = "";
    String[] imagen = {"", "", "", "", ""}, tempImagen = {"", "", "", "", "", "", ""}, tempNombreArchivo = {"", "", "", "", "", "", ""}, tempRutaActual = {"", "", "", "", "", "", ""};
    private Timer t;
    private ActionListener al;
    Integer a=0,m=0,d=0,idAnio = 0,idAnioDis = 0,a1=0,m1=0,d1=0,imprimirOpcion=0,idCatQr=0;
    DefaultComboBoxModel mdlAGE,mdlAGEDis;
    Vector<Anios> anios;
    Vector<AniosDispositivo> aniosd;
    ItemSeleccionado isV=new ItemSeleccionado();
    ItemSeleccionado isVD=new ItemSeleccionado();
    String idV = "",idVD = "";
    String audio = "", tempAudio = "", video = "", tempVideo = "", fechaActual = "", accion = "", categoria = "";
    String[] tempNombreMultimedia = {"", ""} ;
    String fq="",ff="",cal="",cal1="",paraImpre1="",paraImpre2="",paraImpre3="",paraImpre4="";
    
    public Principal() {
        initComponents();
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        btnGestionCategoria.setBackground(Color.black);
        PrivilegiosUsuarios();
        btgSeleccion.add(rbtnActivo);
        btgSeleccion.add(rbtnInactivo);
        btgBuscarHistorialPor.add(jrbFiltroVisitante);
        btgBuscarHistorialPor.add(jrbFiltroDispositivo);
        btgBuscarHistorialPor.add(jrbFiltroTodo);
        jrbFiltroTodo.setSelected(true);
        btgVerEstadisticas.add(jrbEstadisticasVisitantes);
        btgVerEstadisticas.add(jrbEstadisticasDispositivos);
        if(con == null) con = mysql.getConnect();
        lblNuevo.setVisible(false);
        lblLimiteNombreMuseo.setVisible(false);
        lblLimiteFundadorMuseo.setVisible(false);
        lblLimiteHistoriaMuseo.setVisible(false);
        
        ((DefaultEditor) spnFecha.getEditor()).getTextField().setEditable(false);
        lblUsuarioyRolPrincipal.setText("Bienvenid@ " + UsuarioIngresado.parametroU + " tu rol es de " + UsuarioIngresado.parametroR);
        lblUsuarioyRolUsuarios.setText("Bienvenid@ " + UsuarioIngresado.parametroU + " tu rol es de " + UsuarioIngresado.parametroR);
        lblUsuarioyRolGaleria.setText("Bienvenid@ " + UsuarioIngresado.parametroU + " tu rol es de " + UsuarioIngresado.parametroR);
        lblUsuarioyRolContactanos.setText("Bienvenid@ " + UsuarioIngresado.parametroU + " tu rol es de " + UsuarioIngresado.parametroR);
        lblUsuarioyRolAcerca.setText("Bienvenid@ " + UsuarioIngresado.parametroU + " tu rol es de " + UsuarioIngresado.parametroR);
        
        int cont;
        try{
            String SQL ="SELECT COUNT(*) AS Total FROM museo";
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            rs.next();
            cont = rs.getInt("Total");
            rs.close();
            if(cont == 0){
                String SQLA = "INSERT INTO museo(NOMBREMUSEO) VALUES('MUSEO ISIDRO AYORA')";
                PreparedStatement ps = con.prepareStatement(SQLA);
                ps.executeUpdate();
            }
        } catch (Exception e){}
        
        String Ruta=getClass().getResource("/images/Mas.png").getPath();
        Mostrar_Visualizador(btnNuevoUsuario, Ruta);
        String Ruta1=getClass().getResource("/images/actualizar.png").getPath();
        Mostrar_Visualizador(btnActualizarUsuario, Ruta1);
        String Ruta2=getClass().getResource("/images/Eliminar1.png").getPath();
        Mostrar_Visualizador(btnEliminarUsuario, Ruta2);
        String Ruta3=getClass().getResource("/images/search.png").getPath();
        Mostrar_Visualizador(btnBuscarUsuarios, Ruta3);
        
        
        //String rutaAA=getClass().getResource("/images/search.png").getPath();
        //Mostrar_Visualizador(btnBuscarHistorialVisitaDispositivo, rutaAA);
        String rutaAApp=getClass().getResource("/images/search.png").getPath();
        Mostrar_Visualizador(btnVerEstadisticaAnualDispositivos, rutaAApp);
        //String rutaAVN=getClass().getResource("/images/imprimir2.png").getPath();
        //Mostrar_Visualizador(btnImprimirVisitantesDispositivo, rutaAVN);
        String rutaAVA=getClass().getResource("/images/imprimir2.png").getPath();
        Mostrar_Visualizador(btnImprimirEstadisticaDispositivos, rutaAVA);
        
        String rutaHistorial=getClass().getResource("/images/search.png").getPath();
        Mostrar_Visualizador(lblBuscarHistorialVisita, rutaHistorial);
        String rutaEstadistica=getClass().getResource("/images/search.png").getPath();
        Mostrar_Visualizador(btnVerEstadisticaAnual, rutaEstadistica);
        String rutaImprimirUsuarios=getClass().getResource("/images/imprimir2.png").getPath();
        Mostrar_Visualizador(btnImprimirUsuarios, rutaImprimirUsuarios);
        
        String RutaV=getClass().getResource("/images/Mas.png").getPath();
        Mostrar_Visualizador(btnNuevoVisitante, RutaV);
        String RutaIv=getClass().getResource("/images/imprimir2.png").getPath();
        Mostrar_Visualizador(btnImprimirVisitantes, RutaIv);
        String RutaIE=getClass().getResource("/images/imprimir2.png").getPath();
        Mostrar_Visualizador(btnImprimirEstadistica, RutaIE);

        jtHistorialVisita.setModel(LlenarTablaHistorialUnido()); 
        //jtHistorialVisita.setModel(LlenarTablaHistorialBusquedaUnido()); 
        //jtHistorialVisita.setModel(LlenarTablaHistorialVisita()); 
        //jtHistorialVisitaDispositivo.setModel(LlenarTablaHistorialVisitaconDispositivo());
        
        txtBuscarPor.setEnabled(false);
        rbtnActivo.setEnabled(false);
        rbtnInactivo.setEnabled(false);
        jcbBuscarPor.setVisible(false);
        if(UsuarioIngresado.parametroR.contains("Administrador/a")) {
        jtUsuarios.setModel(LlenarTablaUsuariosAdmin());
        TableColumnModel columnModel = jtUsuarios.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(10);
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(3).setPreferredWidth(100);
        columnModel.getColumn(4).setPreferredWidth(100);
        columnModel.getColumn(5).setPreferredWidth(100);
        columnModel.getColumn(6).setPreferredWidth(200);
        columnModel.getColumn(7).setPreferredWidth(100);
        }
        else         jtUsuarios.setModel(LlenarTablaUsuarios());
        //jtHistorialVisita.setModel(LlenarTablaHistorialVisita());
        txtTotalVisitas.setText(String.valueOf((Integer.parseInt(contarTotalVisitas())+(Integer.parseInt(contarTotalVisitasDispositivos())))));
        lblTotalUsuarios.setText(contarTotalU());
        Limpiar();
        
         
        String SQLAGE="SELECT IDHISTORIALVISITA, YEAR(FECHAHORAVISITA) AS FECHAHORAVISITA FROM historialvisitas GROUP BY YEAR(FECHAHORAVISITA)";
        mdlAGE= new DefaultComboBoxModel(ConexionBase.leerDatosVector2(SQLAGE));
        anios = ConexionBase.leerDatosVector2(SQLAGE);
        this.jcbAñoGraficoEstadistico.setModel(mdlAGE);
        variable=(jcbAñoGraficoEstadistico.getSelectedItem()).toString();
        
        String SQLAGEDis="SELECT IDHISTORIALDISPOSITIVOS, YEAR(FECHAVISITADISPOSITIVO) AS FECHAVISITADISPOSITIVO FROM historialdispositivos GROUP BY YEAR(FECHAVISITADISPOSITIVO)";
        mdlAGEDis= new DefaultComboBoxModel(ConexionBase.leerDatosVector3(SQLAGEDis));
        aniosd = ConexionBase.leerDatosVector3(SQLAGEDis);
        this.jcbAñoGraficoEstadisticoComparativo.setModel(mdlAGEDis);
        variabled=(jcbAñoGraficoEstadisticoComparativo.getSelectedItem()).toString();
        
         /*TableColumnModel columnModelH = jtHistorialVisita.getColumnModel();
        columnModelH.getColumn(0).setPreferredWidth(5);
        columnModelH.getColumn(1).setPreferredWidth(50);
        columnModelH.getColumn(2).setPreferredWidth(50);*/
    }
    
            
    
    void PrivilegiosUsuarios(){
        if(UsuarioIngresado.parametroR.contains("Administrador/a")) {
        //jtUsuarios.setModel(LlenarTablaUsuarios());
        return;
        }
        if(UsuarioIngresado.parametroR.contains("Secretario/a")) {
            
            
            btnNuevoUsuario.setVisible(false);
            lblNuevoUsuario.setVisible(false);
            lblActualizarUsuario.setVisible(false);
            btnActualizarUsuario.setVisible(false);
            btnEliminarUsuario.setVisible(false);
            lblEliminarUsuario.setVisible(false);
            lblBuscarUsuario.setVisible(false);
        }
        if(UsuarioIngresado.parametroR.contains("Consultor/a")) {            
            btnNuevoUsuario.setVisible(false);
            lblNuevoUsuario.setVisible(false);
            lblActualizarUsuario.setVisible(false);
            btnActualizarUsuario.setVisible(false);
            btnEliminarUsuario.setVisible(false);
            lblEliminarUsuario.setVisible(false);
            lblBuscarUsuario.setVisible(false);
            
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
    
    Boolean CopiaArchivos(String home, String destiny, String[] multimedia, String nombre, Integer indice){
        File origen = new File(home);
        File destino = new File(destiny);
        try {
            //Localisa la carpeta de origen y ubica la carpeta d destino imagnes
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
    
    void habilitar(){
        txtNombreMuseo.setEditable(true);
        spnFecha.setEnabled(true);
        txtFundadorMuseo.setEditable(true);
        txtHistoriaMuseo.setEditable(true);
        imgMuseo1.setEnabled(true);
        imgMuseo2.setEnabled(true);
        imgMuseo3.setEnabled(true);
        imgMuseo4.setEnabled(true);
        imgMuseo5.setEnabled(true);
        btnVideoMuseo.setEnabled(true);
        jlVideoMuseo.setEnabled(true);
        btnAudioMuseo.setEnabled(true);
        jlAudioMuseo.setEnabled(true);
    }
    
    void Deshabilitar(){
        txtNombreMuseo.setEditable(false);
        spnFecha.setEnabled(false);
        txtFundadorMuseo.setEditable(false);
        txtHistoriaMuseo.setEditable(false);
        imgMuseo1.setEnabled(false);
        imgMuseo2.setEnabled(false);
        imgMuseo3.setEnabled(false);
        imgMuseo4.setEnabled(false);
        imgMuseo5.setEnabled(false);
        btnVideoMuseo.setEnabled(false);
        jlVideoMuseo.setEnabled(false);
        btnAudioMuseo.setEnabled(false);
        jlAudioMuseo.setEnabled(false);
    }
    
    void EditarQr(){
        if(btnEditarMuseo.getText().contains("Editar")) {
            btnEditarMuseo.setText("Guardar");
            habilitar();
            txtNombreMuseo.requestFocus();
            Limpiar();
        } else {
            if (txtNombreMuseo.getText().trim().isEmpty() || txtFundadorMuseo.getText().trim().isEmpty()
                    || txtHistoriaMuseo.getText().trim().isEmpty())
                JOptionPane.showMessageDialog(null, "Ingrese Los Campos Obligatorios");
            else{
                try {       
                    //Actualizar 
                    if(StringUtils.isNullOrEmpty(tempImagen[0])) imagen[0] = tempRutaActual[0];
                    else {
                        File borrarImagenAntigua = new File(tempRutaActual[0]);
                        borrarImagenAntigua.delete();
                        if(CopiaArchivos(tempImagen[0], imagen[0], imagen, "Imagen1.jpg", 0)) imagen[0] += "\\Imagen1.jpg";
                        else return;
                    }
                    if(StringUtils.isNullOrEmpty(tempImagen[1])) imagen[1] = tempRutaActual[1];
                    else {
                        File borrarImagenAntigua = new File(tempRutaActual[1]);
                        borrarImagenAntigua.delete();
                        if(CopiaArchivos(tempImagen[1], imagen[1], imagen, "Imagen2.jpg", 1)) imagen[1] += "\\Imagen2.jpg";
                        else return;
                    }
                    if(StringUtils.isNullOrEmpty(tempImagen[2])) imagen[2] = tempRutaActual[2];
                    else {
                        File borrarImagenAntigua = new File(tempRutaActual[2]);
                        borrarImagenAntigua.delete();
                        if(CopiaArchivos(tempImagen[2], imagen[2], imagen, "Imagen3.jpg", 2)) imagen[2] += "\\Imagen3.jpg";
                        else return;
                    }
                    if(StringUtils.isNullOrEmpty(tempImagen[3])) imagen[3] = tempRutaActual[3];
                    else {
                        File borrarImagenAntigua = new File(tempRutaActual[3]);
                        borrarImagenAntigua.delete();
                        if(CopiaArchivos(tempImagen[3], imagen[3], imagen, "Imagen4.jpg", 3)) imagen[3] += "\\Imagen4.jpg";
                        else return;
                    }
                    if(StringUtils.isNullOrEmpty(tempImagen[4])) imagen[4] = tempRutaActual[4];
                    else {
                        File borrarImagenAntigua = new File(tempRutaActual[4]);
                        borrarImagenAntigua.delete();
                        if(CopiaArchivos(tempImagen[4], imagen[4], imagen, "Imagen5.jpg", 4)) imagen[4] += "\\Imagen5.jpg";
                        else return;
                    }
                    if(StringUtils.isNullOrEmpty(tempAudio)) audio = tempRutaActual[5];
                    else {
                        //File borrarMultimediaAntigua = new File(tempRutaActual[5]);
                        File borrarImagenAntigua = new File(tempRutaActual[5]);
                        borrarImagenAntigua.delete();
                        //if(CopiaArchivos(tempImagen[5], audio, "Audio.mp3", 5)) audio += "\\Audio.mp3";
                        if(CopiaArchivos(tempAudio, audio, "Audio.mp3", 0)) audio += "\\Audio.mp3";
                        else return;
                    }
                    if(StringUtils.isNullOrEmpty(tempVideo)) video = tempRutaActual[6];
                    else {
                        File borrarImagenAntigua = new File(tempRutaActual[6]);
                        borrarImagenAntigua.delete();
                        //if(CopiaArchivos(tempImagen[6], video, "Video.mp4", 6)) video += "\\Video.mp4";
                        if(CopiaArchivos(tempVideo, video, "Video.mp4", 1)) video += "\\Video.mp4";
                        else return;
                    }
                    String SQL = "UPDATE museo SET NOMBREMUSEO = ?, FECHAFUNDACIONMUSEO = ?, FUNDADORMUSEO = ?, HISTORIAMUSEO = ?, "
                        + "IMAGENUNOMUSEO = ?, IMAGENDOSMUSEO = ?, IMAGENTRESMUSEO = ?, IMAGENCUATROMUSEO = ?, IMAGENCINCOMUSEO = ?, "
                            + "VIDEOMUSEO = ?, SONIDOMUSEO = ? WHERE IDMUSEO = 1";
                    PreparedStatement ps = con.prepareStatement(SQL);
                    ps.setString(1, txtNombreMuseo.getText());
                    Date fecha1 = (Date) spnFecha.getValue();
                    java.sql.Date sqlFecha = new java.sql.Date(fecha1.getTime());
                    ps.setDate(2, sqlFecha);
                    ps.setString(3, txtFundadorMuseo.getText());
                    ps.setString(4, txtHistoriaMuseo.getText());
                    ps.setString(5, imagen[0]);
                    ps.setString(6, imagen[1]);
                    ps.setString(7, imagen[2]);
                    ps.setString(8, imagen[3]);
                    ps.setString(9, imagen[4]);
                    ps.setString(10, video);
                    ps.setString(11, audio);
                    int n = ps.executeUpdate();
                    if (n > 0) {
                        JOptionPane.showMessageDialog(null, "Información actualizada Correctamente");
                        btnEditarMuseo.setText("Editar");
                        Deshabilitar();
                    }
                    ps.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "La actualizacion no se efectuó");
                    File borrarImagenAntigua = new File(ValoresConstantes.DIRECTORIO_PRINCIPAL + "\\MUSEO ISIDRO AYORA\\Imagen1.jpg");
                    if(borrarImagenAntigua.exists()) borrarImagenAntigua.delete();
                    borrarImagenAntigua = new File(ValoresConstantes.DIRECTORIO_PRINCIPAL + "\\MUSEO ISIDRO AYORA\\Imagen2.jpg");
                    if(borrarImagenAntigua.exists()) borrarImagenAntigua.delete();
                    borrarImagenAntigua = new File(ValoresConstantes.DIRECTORIO_PRINCIPAL + "\\MUSEO ISIDRO AYORA\\Imagen3.jpg");
                    if(borrarImagenAntigua.exists()) borrarImagenAntigua.delete();
                    borrarImagenAntigua = new File(ValoresConstantes.DIRECTORIO_PRINCIPAL + "\\MUSEO ISIDRO AYORA\\Imagen4.jpg");
                    if(borrarImagenAntigua.exists()) borrarImagenAntigua.delete();
                    borrarImagenAntigua = new File(ValoresConstantes.DIRECTORIO_PRINCIPAL + "\\MUSEO ISIDRO AYORA\\Imagen5.jpg");
                    if(borrarImagenAntigua.exists()) borrarImagenAntigua.delete();
                    borrarImagenAntigua = new File(ValoresConstantes.DIRECTORIO_PRINCIPAL + "\\MUSEO ISIDRO AYORA\\Audio.mp3");
                    if(borrarImagenAntigua.exists()) borrarImagenAntigua.delete();
                    borrarImagenAntigua = new File(ValoresConstantes.DIRECTORIO_PRINCIPAL + "\\MUSEO ISIDRO AYORA\\Video.mp4");
                    if(borrarImagenAntigua.exists()) borrarImagenAntigua.delete();
                }       
            }
        }    
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
            System.out.println("error");
        }
        return true;
    }
    
    void Limpiar(){
        for (int i = 0; i < 5; i++) {
            imagen[i] = "";
            tempImagen[i] = "";
            tempNombreArchivo[i] = "";
        }
        try {
            String SQLTM ="SELECT * FROM museo WHERE IDMUSEO = 1"; 
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQLTM);
            rs.next();
            txtNombreMuseo.setText(rs.getString("NOMBREMUSEO"));
            String fe = rs.getString("FECHAFUNDACIONMUSEO");
            if(!StringUtils.isNullOrEmpty(fe)) {
                ((DefaultEditor) spnFecha.getEditor()).getTextField().setText(fe);
            }
            txtFundadorMuseo.setText(rs.getString("FUNDADORMUSEO"));
            txtHistoriaMuseo.setText(rs.getString("HISTORIAMUSEO"));
            tempRutaActual[0] = rs.getString("IMAGENUNOMUSEO");
            if(StringUtils.isNullOrEmpty(tempRutaActual[0])) tempRutaActual[0] = "";
            tempRutaActual[1] = rs.getString("IMAGENDOSMUSEO");
            if(StringUtils.isNullOrEmpty(tempRutaActual[1])) tempRutaActual[1] = "";
            tempRutaActual[2] = rs.getString("IMAGENTRESMUSEO");
            if(StringUtils.isNullOrEmpty(tempRutaActual[2])) tempRutaActual[2] = "";
            tempRutaActual[3] = rs.getString("IMAGENCUATROMUSEO");
            if(StringUtils.isNullOrEmpty(tempRutaActual[3])) tempRutaActual[3] = "";
            tempRutaActual[4] = rs.getString("IMAGENCINCOMUSEO");
            if(StringUtils.isNullOrEmpty(tempRutaActual[4])) tempRutaActual[4] = "";
            rs.close();
        } catch (SQLException e) {
        //JOptionPane.showMessageDialog(null, "La actualizacion no se efectuó");
        }
        String img=getClass().getResource("/images/imagen.jpg").getPath();
        Mostrar_Visualizador(imgMuseo1, img);
        Mostrar_Visualizador(imgMuseo2, img);
        Mostrar_Visualizador(imgMuseo3, img);
        Mostrar_Visualizador(imgMuseo4,img);
        Mostrar_Visualizador(imgMuseo5, img);
        if(!StringUtils.isNullOrEmpty(tempRutaActual[0])) Mostrar_Visualizador(imgMuseo1, tempRutaActual[0]);
        if(!StringUtils.isNullOrEmpty(tempRutaActual[1])) Mostrar_Visualizador(imgMuseo2, tempRutaActual[1]);
        if(!StringUtils.isNullOrEmpty(tempRutaActual[2])) Mostrar_Visualizador(imgMuseo3, tempRutaActual[2]);
        if(!StringUtils.isNullOrEmpty(tempRutaActual[3])) Mostrar_Visualizador(imgMuseo4, tempRutaActual[3]);
        if(!StringUtils.isNullOrEmpty(tempRutaActual[4])) Mostrar_Visualizador(imgMuseo5, tempRutaActual[4]);
        String imgqr=getClass().getResource("/images/QR.png").getPath();
        Mostrar_Visualizador(imgQrMuseo, imgqr);
        
    }
    
    public static String contarTotalU(){
        String cont;
        try {
            String SQL ="SELECT COUNT(*) AS Total FROM usuarios";
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            rs.next();
            cont = rs.getString("Total");
            rs.close();
        
        } catch (SQLException e) {
            cont = "Sin conexión";
        }
        return cont;
    }    
    
    /*
    public void actualizarVisitantes(){
    al=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                jtHistorialVisita.setModel(LlenarTablaHistorialVisita()); 
                //if(t2=0){
            }
        };
        t=new Timer(1000,al);
        if(!t.isRunning()){
        t.start();
        }
}*/
    
    
    public static String contarTotalVisitas(){
        String cont;
        try {
            String SQL ="SELECT COUNT(*) AS Total FROM historialvisitas";
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            rs.next();
            cont = rs.getString("Total");
            rs.close();
        
        } catch (SQLException e) {
            cont = "error";
        }
        return cont;
    }  
    
    public static String contarTotalVisitasDispositivos(){
        String cont;
        try {
            String SQL ="SELECT COUNT(*) AS Total FROM historialdispositivos";
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            rs.next();
            cont = rs.getString("Total");
            rs.close();
        
        } catch (SQLException e) {
            cont = "error";
        }
        return cont;
    }
    
    
     public static String contarTotalVisitasporFecha(){
        String cont;
        int a,d,m;
        a = jdtHistDesde.getDate().getYear() + 1900;
        m = jdtHistDesde.getDate().getMonth() + 1;
        d = jdtHistDesde.getDate().getDate();
            try {
            String mm,dm,cal,fq;
            if(m<10) mm="0"+m;
            else mm=String.valueOf(m);
            if(d<10) dm="0"+d;
            else dm=String.valueOf(d);
            fq=a+"-"+mm+"-"+dm;
            cal=fq.toString();
            String SQL ="SELECT COUNT(*) AS total FROM historialvisitas WHERE DATE(FECHAHORAVISITA) BETWEEN '" + cal + "' AND '" + cal + "'";
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            rs.next();
            cont = rs.getString("total");
            rs.close();
        
        } catch (SQLException e) {
            cont = "error";
        }
        return cont;
    }
     
     public static String contarTotalVisitasporFechaHasta(){
        String cont;
        int a,d,m,a1,m1,d1;
        a = jdtHistDesde.getDate().getYear() + 1900;
        m = jdtHistDesde.getDate().getMonth() + 1;
        d = jdtHistDesde.getDate().getDate();
        a1 = jdtHistHasta.getDate().getYear() + 1900;
        m1 = jdtHistHasta.getDate().getMonth() + 1;
        d1 = jdtHistHasta.getDate().getDate();
            try {
            String mm,dm,cal,fq;
            if(m<10) mm="0"+m;
            else mm=String.valueOf(m);
            if(d<10) dm="0"+d;
            else dm=String.valueOf(d);
            fq=a+"-"+mm+"-"+dm;
            cal=fq;
            String mmh,dmh,cal1,ff;
            if(m1<10) mmh = "0" + m1;
            else mmh = String.valueOf(m1);
            if(d1<10) dmh = "0" + d1;
            else dmh = String.valueOf(d1);
            ff = a1 + "-" + mmh + "-" + dmh;
            cal1=ff;
            if(cal.isEmpty() || cal1.isEmpty())    return cont="0";
            else {
            String SQL ="SELECT COUNT(*) AS total FROM historialvisitas WHERE DATE(FECHAHORAVISITA) BETWEEN '" + cal + "' AND '" + cal1 + "'";
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            rs.next();
            cont = rs.getString("total");
            rs.close();}
        
        } catch (SQLException e) {
            cont = "error";
        }
        return cont;
    }
    
     public static String contarTotalUnidoporFechaHasta(){
        String cont;
        int a,d,m,a1,m1,d1;
        a = jdtHistDesde.getDate().getYear() + 1900;
        m = jdtHistDesde.getDate().getMonth() + 1;
        d = jdtHistDesde.getDate().getDate();
        a1 = jdtHistHasta.getDate().getYear() + 1900;
        m1 = jdtHistHasta.getDate().getMonth() + 1;
        d1 = jdtHistHasta.getDate().getDate();
            try {
            String mm,dm,cal,fq;
            if(m<10) mm="0"+m;
            else mm=String.valueOf(m);
            if(d<10) dm="0"+d;
            else dm=String.valueOf(d);
            fq=a+"-"+mm+"-"+dm;
            cal=fq;
            String mmh,dmh,cal1,ff;
            if(m1<10) mmh = "0" + m1;
            else mmh = String.valueOf(m1);
            if(d1<10) dmh = "0" + d1;
            else dmh = String.valueOf(d1);
            ff = a1 + "-" + mmh + "-" + dmh;
            cal1=ff;
            if(cal.isEmpty() || cal1.isEmpty())    return cont="0";
            else {
              //  SELECT * FROM historialvisitas where substring(FECHAHORAVISITA,1,10)  >= '" + cal + "' AND substring(FECHAHORAVISITA,1,10) <='" + cal1 + "'UNION SELECT * FROM historialdispositivos where substring(FECHAVISITADISPOSITIVO,1,10) >= '" + cal + "' AND substring(FECHAVISITADISPOSITIVO,1,10) <='" + cal1 + "' ORDER BY IDHISTORIALVISITA DESC"; 
            //SELECT SUM(total) from ( SELECT COUNT(*) AS total FROM historialvisitas where substring(FECHAHORAVISITA,1,10) >= '" + cal + "' AND substring(FECHAHORAVISITA,1,10) <='" + cal1 + "' UNION SELECT COUNT(*) AS Total FROM historialdispositivos where substring(FECHAVISITADISPOSITIVO,1,10) >= '" + cal + "' AND substring(FECHAVISITADISPOSITIVO,1,10) <='" + cal1 + "')AS Total"; 
              String SQL ="SELECT SUM(total) from ("
                      + "SELECT COUNT(*) AS total FROM historialvisitas WHERE DATE(FECHAHORAVISITA) BETWEEN '" + cal + "' AND '" + cal1 + "' UNION SELECT COUNT(*) AS total FROM historialdispositivos WHERE DATE(FECHAVISITADISPOSITIVO) BETWEEN '" + cal + "' AND '" + cal1 + "')"
                      + "as total"; 
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            rs.next();
            cont = rs.getString("SUM(total)");
            rs.close();}
        
        } catch (SQLException e) {
            cont = "error";
        }
        return cont;
    }
     
     public static String contarTotalUnidoporFechaDesde(){
        String cont;
        int a,d,m;
        a = jdtHistDesde.getDate().getYear() + 1900;
        m = jdtHistDesde.getDate().getMonth() + 1;
        d = jdtHistDesde.getDate().getDate();
        try {
            String mm,dm,cal,fq;
            if(m<10) mm="0"+m;
            else mm=String.valueOf(m);
            if(d<10) dm="0"+d;
            else dm=String.valueOf(d);
            fq=a+"-"+mm+"-"+dm;
            cal=fq.toString();
            if(cal.isEmpty()) return cont="0";
            else {
             String SQL ="SELECT SUM(total) from ("
                      + "SELECT COUNT(*) AS total FROM historialvisitas WHERE DATE(FECHAHORAVISITA) BETWEEN '" + cal + "' AND '" + cal + "' UNION SELECT COUNT(*) AS total FROM historialdispositivos WHERE DATE(FECHAVISITADISPOSITIVO) BETWEEN '" + cal + "' AND '" + cal + "')"
                      + "as total"; 
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            rs.next();
            cont = rs.getString("SUM(total)");
            rs.close();}
        
        } catch (SQLException e) {
            cont = "error";
        }
        return cont;
    }
     
     public static String contarTotalVisitasDispositivosporFechaHasta(){
        String cont;
        int a,d,m,a1,m1,d1;
        a = jdtHistDesde.getDate().getYear() + 1900;
        m = jdtHistDesde.getDate().getMonth() + 1;
        d = jdtHistDesde.getDate().getDate();
        a1 = jdtHistHasta.getDate().getYear() + 1900;
        m1 = jdtHistHasta.getDate().getMonth() + 1;
        d1 = jdtHistHasta.getDate().getDate();
            try {
            String mm,dm,cal,fq;
            if(m<10) mm="0"+m;
            else mm=String.valueOf(m);
            if(d<10) dm="0"+d;
            else dm=String.valueOf(d);
            fq=a+"-"+mm+"-"+dm;
            cal=fq;
            String mmh,dmh,cal1,ff;
            if(m1<10) mmh = "0" + m1;
            else mmh = String.valueOf(m1);
            if(d1<10) dmh = "0" + d1;
            else dmh = String.valueOf(d1);
            ff = a1 + "-" + mmh + "-" + dmh;
            cal1=ff;
            if(cal.isEmpty() || cal1.isEmpty())    return cont="0";
            else {
            String SQL ="SELECT COUNT(*) AS total FROM historialdispositivos WHERE DATE(FECHAVISITADISPOSITIVO) BETWEEN '" + cal + "' AND '" + cal1 + "'";
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            rs.next();
            cont = rs.getString("total");
            rs.close();}
        
        } catch (SQLException e) {
            cont = "error";
        }
        return cont;
    }
     
    public static String contarTotalVisitasporFechadeDispositivo(){
        String cont;
        int adis,ddis,mdis;
        adis = jdtHistDesde.getDate().getYear() + 1900;
        mdis = jdtHistDesde.getDate().getMonth() + 1;
        ddis = jdtHistDesde.getDate().getDate();
            try {
            String mmdis,dmdis,caldis,fqdis;
            if(mdis<10) mmdis="0"+mdis;
            else mmdis=String.valueOf(mdis);
            if(ddis<10) dmdis="0"+ddis;
            else dmdis=String.valueOf(ddis);
            fqdis=adis+"-"+mmdis+"-"+dmdis;
            caldis=fqdis.toString();
            String SQL ="SELECT COUNT(*) AS total FROM historialdispositivos WHERE DATE(FECHAVISITADISPOSITIVO) BETWEEN '" + caldis + "' AND '" + caldis + "'";
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            rs.next();
            cont = rs.getString("total");
            rs.close();
        
        } catch (SQLException e) {
            cont = "error";
        }
        return cont;
    }
    
     
    public static String visitantesE(){
           String contEne;
           String var="-"+jcbAñoGraficoEstadistico.getSelectedItem().toString();
            try {
               String SQLE ="SELECT COUNT(*) AS Total FROM historialvisitas WHERE MONTH(FECHAHORAVISITA) = '01' AND YEAR(FECHAHORAVISITA) = '"+jcbAñoGraficoEstadistico.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQLE);
               rs.next();
               contEne = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               contEne = "error";
           }
           return contEne;
            
       }
    
     public static String visitantesF(){
           String contF;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialvisitas WHERE MONTH(FECHAHORAVISITA) = '02' AND YEAR(FECHAHORAVISITA) = '"+jcbAñoGraficoEstadistico.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               contF = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               contF = "error";
           }
           return contF;
       }
      public static String visitantesM(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialvisitas WHERE MONTH(FECHAHORAVISITA) = '03' AND YEAR(FECHAHORAVISITA) = '"+jcbAñoGraficoEstadistico.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
      public static String visitantesA(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialvisitas WHERE MONTH(FECHAHORAVISITA) = '04' AND YEAR(FECHAHORAVISITA) = '"+jcbAñoGraficoEstadistico.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
      public static String visitantesMayo(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialvisitas WHERE MONTH(FECHAHORAVISITA) = '05' AND YEAR(FECHAHORAVISITA) = '"+jcbAñoGraficoEstadistico.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
      public static String visitantesJ(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialvisitas WHERE MONTH(FECHAHORAVISITA) = '06' AND YEAR(FECHAHORAVISITA) = '"+jcbAñoGraficoEstadistico.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
    public static String visitantesJul(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialvisitas WHERE MONTH(FECHAHORAVISITA) = '07' AND YEAR(FECHAHORAVISITA) = '"+jcbAñoGraficoEstadistico.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
    public static String visitantesAg(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialvisitas WHERE MONTH(FECHAHORAVISITA) = '08' AND YEAR(FECHAHORAVISITA) = '"+jcbAñoGraficoEstadistico.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
    public static String visitantesSep(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialvisitas WHERE MONTH(FECHAHORAVISITA) = '09' AND YEAR(FECHAHORAVISITA) = '"+jcbAñoGraficoEstadistico.getSelectedItem().toString()+"'";           
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
    public static String visitantesOc(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialvisitas WHERE MONTH(FECHAHORAVISITA) = '10' AND YEAR(FECHAHORAVISITA) = '"+jcbAñoGraficoEstadistico.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
    public static String visitantesNov(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialvisitas WHERE MONTH(FECHAHORAVISITA) = '11' AND YEAR(FECHAHORAVISITA) = '"+jcbAñoGraficoEstadistico.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
    public static String visitantesDic(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialvisitas WHERE MONTH(FECHAHORAVISITA) = '12' AND YEAR(FECHAHORAVISITA) = '"+jcbAñoGraficoEstadistico.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
    
    
    
    //Contador de los dispositivos segun los meses
    
    public static String visitantesED(){
           String contEneD;
           String var="-"+jcbAñoGraficoEstadistico.getSelectedItem().toString();
            try {
               String SQLE ="SELECT COUNT(*) AS Total FROM historialdispositivos WHERE MONTH(FECHAVISITADISPOSITIVO) = '01' AND YEAR(FECHAVISITADISPOSITIVO) = '"+jcbAñoGraficoEstadistico.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQLE);
               rs.next();
               contEneD = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               contEneD = "error";
           }
           return contEneD;
       }
    
     public static String visitantesFD(){
           String contF;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialdispositivos WHERE MONTH(FECHAVISITADISPOSITIVO) = '02' AND YEAR(FECHAVISITADISPOSITIVO) = '"+jcbAñoGraficoEstadistico.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               contF = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               contF = "error";
           }
           return contF;
       }
      public static String visitantesMD(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialdispositivos WHERE MONTH(FECHAVISITADISPOSITIVO) = '03' AND YEAR(FECHAVISITADISPOSITIVO) = '"+jcbAñoGraficoEstadistico.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
      public static String visitantesAD(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialdispositivos WHERE MONTH(FECHAVISITADISPOSITIVO) = '04' AND YEAR(FECHAVISITADISPOSITIVO) = '"+jcbAñoGraficoEstadistico.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
      public static String visitantesMayoD(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialdispositivos WHERE MONTH(FECHAVISITADISPOSITIVO) = '05' AND YEAR(FECHAVISITADISPOSITIVO) = '"+jcbAñoGraficoEstadistico.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
      public static String visitantesJD(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialdispositivos WHERE MONTH(FECHAVISITADISPOSITIVO) = '06' AND YEAR(FECHAVISITADISPOSITIVO) = '"+jcbAñoGraficoEstadistico.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
    public static String visitantesJulD(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialdispositivos WHERE MONTH(FECHAVISITADISPOSITIVO) = '07' AND YEAR(FECHAVISITADISPOSITIVO) = '"+jcbAñoGraficoEstadistico.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
    public static String visitantesAgD(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialdispositivos WHERE MONTH(FECHAVISITADISPOSITIVO) = '08' AND YEAR(FECHAVISITADISPOSITIVO) = '"+jcbAñoGraficoEstadistico.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
    public static String visitantesSepD(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialdispositivos WHERE MONTH(FECHAVISITADISPOSITIVO) = '09' AND YEAR(FECHAVISITADISPOSITIVO) = '"+jcbAñoGraficoEstadistico.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
    public static String visitantesOcD(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialdispositivos WHERE MONTH(FECHAVISITADISPOSITIVO) = '10' AND YEAR(FECHAVISITADISPOSITIVO) = '"+jcbAñoGraficoEstadistico.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
    public static String visitantesNovD(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialdispositivos WHERE MONTH(FECHAVISITADISPOSITIVO) = '11' AND YEAR(FECHAVISITADISPOSITIVO) = '"+jcbAñoGraficoEstadistico.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
    public static String visitantesDicD(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialdispositivos WHERE MONTH(FECHAVISITADISPOSITIVO) = '12' AND YEAR(FECHAVISITADISPOSITIVO) = '"+jcbAñoGraficoEstadistico.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
    
    //Metodos de suma de registros por meses para el cuadro comparativo
    public static String visitantesEneroComp(){
           String contEne;
           //String var="-"+jcbAñoGrafi.getSelectedItem().toString();
            try {
               String SQLE ="SELECT COUNT(*) AS Total FROM historialvisitas WHERE MONTH(FECHAHORAVISITA) = '01' AND YEAR(FECHAHORAVISITA) = '"+jcbAñoGraficoEstadisticoComparativo.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQLE);
               rs.next();
               contEne = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               contEne = "error";
           }
           return contEne;
            
       }
    
     public static String visitantesFebrComp(){
           String contF;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialvisitas WHERE MONTH(FECHAHORAVISITA) = '02' AND YEAR(FECHAHORAVISITA) = '"+jcbAñoGraficoEstadisticoComparativo.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               contF = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               contF = "error";
           }
           return contF;
       }
      public static String visitantesMarComp(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialvisitas WHERE MONTH(FECHAHORAVISITA) = '03' AND YEAR(FECHAHORAVISITA) = '"+jcbAñoGraficoEstadisticoComparativo.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
      public static String visitantesAbrComp(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialvisitas WHERE MONTH(FECHAHORAVISITA) = '04' AND YEAR(FECHAHORAVISITA) = '"+jcbAñoGraficoEstadisticoComparativo.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
      public static String visitantesMayoComp(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialvisitas WHERE MONTH(FECHAHORAVISITA) = '05' AND YEAR(FECHAHORAVISITA) = '"+jcbAñoGraficoEstadisticoComparativo.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
      public static String visitantesJunComp(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialvisitas WHERE MONTH(FECHAHORAVISITA) = '06' AND YEAR(FECHAHORAVISITA) = '"+jcbAñoGraficoEstadisticoComparativo.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
    public static String visitantesJulComp(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialvisitas WHERE MONTH(FECHAHORAVISITA) = '07' AND YEAR(FECHAHORAVISITA) = '"+jcbAñoGraficoEstadisticoComparativo.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
    public static String visitantesAgosComp(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialvisitas WHERE MONTH(FECHAHORAVISITA) = '08' AND YEAR(FECHAHORAVISITA) = '"+jcbAñoGraficoEstadisticoComparativo.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
    public static String visitantesSepComp(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialvisitas WHERE MONTH(FECHAHORAVISITA) = '09' AND YEAR(FECHAHORAVISITA) = '"+jcbAñoGraficoEstadisticoComparativo.getSelectedItem().toString()+"'";           
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
    public static String visitantesOctComp(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialvisitas WHERE MONTH(FECHAHORAVISITA) = '10' AND YEAR(FECHAHORAVISITA) = '"+jcbAñoGraficoEstadisticoComparativo.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
    public static String visitantesNovComp(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialvisitas WHERE MONTH(FECHAHORAVISITA) = '11' AND YEAR(FECHAHORAVISITA) = '"+jcbAñoGraficoEstadisticoComparativo.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
    public static String visitantesDicComp(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialvisitas WHERE MONTH(FECHAHORAVISITA) = '12' AND YEAR(FECHAHORAVISITA) = '"+jcbAñoGraficoEstadisticoComparativo.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
    
    
    
    //Contador de los dispositivos segun los meses
    
    public static String visitantesEneDisComp(){
           String contEneD;
           //String var="-"+jcbAñoGraficoEstadistico.getSelectedItem().toString();
            try {
               String SQLE ="SELECT COUNT(*) AS Total FROM historialdispositivos WHERE MONTH(FECHAVISITADISPOSITIVO) = '01' AND YEAR(FECHAVISITADISPOSITIVO) = '"+jcbAñoGraficoEstadisticoComparativo.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQLE);
               rs.next();
               contEneD = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               contEneD = "error";
           }
           return contEneD;
       }
    
     public static String visitantesFebDisComp(){
           String contF;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialdispositivos WHERE MONTH(FECHAVISITADISPOSITIVO) = '02' AND YEAR(FECHAVISITADISPOSITIVO) = '"+jcbAñoGraficoEstadisticoComparativo.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               contF = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               contF = "error";
           }
           return contF;
       }
      public static String visitantesMarDisComp(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialdispositivos WHERE MONTH(FECHAVISITADISPOSITIVO) = '03' AND YEAR(FECHAVISITADISPOSITIVO) = '"+jcbAñoGraficoEstadisticoComparativo.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
      public static String visitantesAbrDisComp(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialdispositivos WHERE MONTH(FECHAVISITADISPOSITIVO) = '04' AND YEAR(FECHAVISITADISPOSITIVO) = '"+jcbAñoGraficoEstadisticoComparativo.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
      public static String visitantesMayoDisComp(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialdispositivos WHERE MONTH(FECHAVISITADISPOSITIVO) = '05' AND YEAR(FECHAVISITADISPOSITIVO) = '"+jcbAñoGraficoEstadisticoComparativo.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
      public static String visitantesJunDisComp(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialdispositivos WHERE MONTH(FECHAVISITADISPOSITIVO) = '06' AND YEAR(FECHAVISITADISPOSITIVO) = '"+jcbAñoGraficoEstadisticoComparativo.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
    public static String visitantesJulDisComp(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialdispositivos WHERE MONTH(FECHAVISITADISPOSITIVO) = '07' AND YEAR(FECHAVISITADISPOSITIVO) = '"+jcbAñoGraficoEstadisticoComparativo.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
    public static String visitantesAgosDisComp(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialdispositivos WHERE MONTH(FECHAVISITADISPOSITIVO) = '08' AND YEAR(FECHAVISITADISPOSITIVO) = '"+jcbAñoGraficoEstadisticoComparativo.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
    public static String visitantesSepDisComp(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialdispositivos WHERE MONTH(FECHAVISITADISPOSITIVO) = '09' AND YEAR(FECHAVISITADISPOSITIVO) = '"+jcbAñoGraficoEstadisticoComparativo.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
    public static String visitantesOcDisComp(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialdispositivos WHERE MONTH(FECHAVISITADISPOSITIVO) = '10' AND YEAR(FECHAVISITADISPOSITIVO) = '"+jcbAñoGraficoEstadisticoComparativo.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
    public static String visitantesNovDisComp(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialdispositivos WHERE MONTH(FECHAVISITADISPOSITIVO) = '10' AND YEAR(FECHAVISITADISPOSITIVO) = '"+jcbAñoGraficoEstadisticoComparativo.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
    public static String visitantesDicDisComp(){
           String cont;
               try {
               String SQL ="SELECT COUNT(*) AS Total FROM historialdispositivos WHERE MONTH(FECHAVISITADISPOSITIVO) = '12' AND YEAR(FECHAVISITADISPOSITIVO) = '"+jcbAñoGraficoEstadisticoComparativo.getSelectedItem().toString()+"'";          
               sent = con.createStatement();            
               ResultSet rs = sent.executeQuery(SQL);
               rs.next();
               cont = rs.getString("Total");         
               rs.close();      
           } catch (SQLException e) {
               cont = "error";
           }
           return cont;
       }
    
    
    public static DefaultTableModel LlenarTablaUsuarios(){
        try{
            //Muestra los usuarios existentes en la base de datos
            String titulos[] = {"ID", "TIPO USUARIO","NOMBRE","APELLIDO","CEDULA","CORREO ELECTRONICO","ESTADO"};
            //String SQL ="SELECT * FROM ingresos where CodigoParaiso Like '%"+txtBuscar.getText().toString().trim()+"%'AND ORDER BY Movimiento,Id,Fecha ASC"; 
            String SQLTU ="SELECT * FROM usuarios ORDER BY IDUSUARIO ASC"; 
            DefaultTableModel model = new DefaultTableModel(null, titulos);
            Statement sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQLTU);
            String[]fila=new String[7];
            while(rs.next()){
                fila[0] = rs.getString("IDUSUARIO");
                fila[1] = rs.getString("TIPOUSUARIO");
                fila[2] = rs.getString("NOMBRESUSUARIO");
                fila[3] = rs.getString("APELLIDOSUSUARIO");
                fila[4] = rs.getString("CEDULAUSUARIO");
                fila[5] = rs.getString("CORREOUSUARIO");
                fila[6] = rs.getString("ESTADOUSUARIO");
                model.addRow(fila);
                
            }
            rs.close();
            //jtUsuarios.removeAll();
            return model;
            //model.fireTableDataChanged();
            
        }catch(Exception e){
            return null;
        }
        
    }
    
    public static DefaultTableModel LlenarTablaUsuariosAdmin(){
        try{
            //Muestra los usuarios existentes en la base de datos
            String titulos[] = {"ID", "TIPO USUARIO","NOMBRE","APELLIDO","CONTRASEÑA","CEDULA","CORREO ELECTRONICO","ESTADO"};
            //String SQL ="SELECT * FROM ingresos where CodigoParaiso Like '%"+txtBuscar.getText().toString().trim()+"%'AND ORDER BY Movimiento,Id,Fecha ASC"; 
            String SQLTU ="SELECT * FROM usuarios ORDER BY IDUSUARIO ASC"; 
            DefaultTableModel model = new DefaultTableModel(null, titulos);
            Statement sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQLTU);
            String[]fila=new String[8];
            while(rs.next()){
                fila[0] = rs.getString("IDUSUARIO");
                fila[1] = rs.getString("TIPOUSUARIO");
                fila[2] = rs.getString("NOMBRESUSUARIO");
                fila[3] = rs.getString("APELLIDOSUSUARIO");
                fila[4] = rs.getString("CONTRASENAUSUARIO");                
                fila[5] = rs.getString("CEDULAUSUARIO");
                fila[6] = rs.getString("CORREOUSUARIO");
                fila[7] = rs.getString("ESTADOUSUARIO");
                model.addRow(fila);
                
            }
            rs.close();
            //jtUsuarios.removeAll();
            return model;
            //model.fireTableDataChanged();
            
        }catch(Exception e){
            return null;
        }
        
    }
    
    public static DefaultTableModel LlenarTablaHistorialVisita(){
        try{
            //Muestra los usuarios existentes en la base de datos
            String titulos[] = {"ID DE VISITA","VISITANTE", "FECHA Y HORA DE LA VISITA"};
            String SQLTH ="SELECT * FROM historialvisitas ORDER BY IDHISTORIALVISITA ASC"; 
            DefaultTableModel model = new DefaultTableModel(null, titulos);
            Statement sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQLTH);
            String[]fila=new String[3];
            while(rs.next()){
                fila[0] = rs.getString("IDHISTORIALVISITA");
                fila[1] = rs.getString("NOMBRESAPELLIDOSVISITANTE");
                fila[2] = rs.getString("FECHAHORAVISITA");
                model.addRow(fila);
                }
            rs.close();
            //jtUsuarios.removeAll();
            return model;
            //model.fireTableDataChanged();
            
        }catch(Exception e){
            return null;
        }
        
    }
    
    public static DefaultTableModel LlenarTablaHistorialUnido(){
        try{
            //Muestra los usuarios existentes en la base de datos
            String titulos[] = {"ID DE VISITA","VISITANTE", "FECHA Y HORA DE LA VISITA"};
            String SQLTH ="SELECT * FROM historialvisitas UNION SELECT * FROM historialdispositivos ORDER BY FECHAHORAVISITA ASC"; 
            DefaultTableModel model = new DefaultTableModel(null, titulos);
            Statement sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQLTH);
            String[]fila=new String[3];
            while(rs.next()){
                fila[0] = rs.getString("IDHISTORIALVISITA");
                fila[1] = rs.getString("NOMBRESAPELLIDOSVISITANTE");
                fila[2] = rs.getString("FECHAHORAVISITA");
                model.addRow(fila);
                }
            rs.close();
            //jtUsuarios.removeAll();
            return model;
            //model.fireTableDataChanged();
            
        }catch(Exception e){
            return null;
        }
        
    }
    
    public static DefaultTableModel LlenarTablaHistorialBusquedaUnido(){
        int a,m,d,a1,m1,d1;
        try{
            a = jdtHistDesde.getDate().getYear() + 1900;
            m = jdtHistDesde.getDate().getMonth() + 1;
            d = jdtHistDesde.getDate().getDate();
            String mm,dm,mmh,dmh,fq,cal,ff,cal1;
            if(m<10) mm = "0" + m;
            else mm = String.valueOf(m);
            if(d<10) dm = "0" + d;
            else dm = String.valueOf(d);
            fq = a + "-" + mm + "-" + dm;
            cal=fq.toString();
            a1 = jdtHistHasta.getDate().getYear() + 1900;
            m1 = jdtHistHasta.getDate().getMonth() + 1;
            d1 = jdtHistHasta.getDate().getDate();
            if(m1<10) mmh = "0" + m1;
            else mmh = String.valueOf(m1);
            if(d1<10) dmh = "0" + d1;
            else dmh = String.valueOf(d1);
            ff = a1 + "-" + mmh + "-" + dmh;
            cal1=ff;
            //String SQL ="SELECT * FROM historialvisitas where substring(FECHAHORAVISITA,1,10)  >= '" + cal + "' AND substring(FECHAHORAVISITA,1,10) <='" + cal1 + "'ORDER BY IDHISTORIALVISITA DESC";
            //Muestra los usuarios existentes en la base de datos
            String titulos[] = {"ID","VISITANTE", "FECHA Y HORA DE LA VISITA"};
            String SQLTH ="SELECT * FROM historialvisitas WHERE DATE(FECHAHORAVISITA) BETWEEN '" + cal + "' AND '" + cal1 + "' UNION SELECT * FROM historialdispositivos WHERE DATE(FECHAVISITADISPOSITIVO) BETWEEN '" + cal + "' AND '" + cal1 + "' ORDER BY FECHAHORAVISITA ASC"; 
            DefaultTableModel model = new DefaultTableModel(null, titulos);
            Statement sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQLTH);
            String[]fila=new String[3];
            while(rs.next()){
                fila[0] = rs.getString("IDHISTORIALVISITA");
                fila[1] = rs.getString("NOMBRESAPELLIDOSVISITANTE");
                fila[2] = rs.getString("FECHAHORAVISITA");
                model.addRow(fila);
                }
            rs.close();
            //jtUsuarios.removeAll();
            return model;
            //model.fireTableDataChanged();
            
        }catch(Exception e){
            return null;
        }
        
    }
    
    public static DefaultTableModel LlenarTablaHistorialBusquedaUnidoDesde(){
        int a,m,d;
        try{
            a = jdtHistDesde.getDate().getYear() + 1900;
            m = jdtHistDesde.getDate().getMonth() + 1;
            d = jdtHistDesde.getDate().getDate();
            String mm,dm,fq,cal;
            if(m<10) mm = "0" + m;
            else mm = String.valueOf(m);
            if(d<10) dm = "0" + d;
            else dm = String.valueOf(d);
            fq = a + "-" + mm + "-" + dm;
            cal=fq;
            String titulos[] = {"ID","VISITANTE", "FECHA Y HORA DE LA VISITA"};
            String SQLTH ="SELECT * FROM historialvisitas WHERE DATE(FECHAHORAVISITA) BETWEEN '" + cal + "' AND '" + cal + "' UNION SELECT * FROM historialdispositivos WHERE DATE(FECHAVISITADISPOSITIVO) BETWEEN '" + cal + "' AND '" + cal + "' ORDER BY FECHAHORAVISITA ASC"; 
            DefaultTableModel model = new DefaultTableModel(null, titulos);
            Statement sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQLTH);
            String[]fila=new String[3];
            while(rs.next()){
                fila[0] = rs.getString("IDHISTORIALVISITA");
                fila[1] = rs.getString("NOMBRESAPELLIDOSVISITANTE");
                fila[2] = rs.getString("FECHAHORAVISITA");
                model.addRow(fila);
                }
            rs.close();
            //jtUsuarios.removeAll();
            return model;
            //model.fireTableDataChanged();
            
        }catch(Exception e){
            return null;
        }
        
    }
    
    public static DefaultTableModel LlenarTablaHistorialVisitaconDispositivo(){
        try{
            //Muestra los visitantes existentes en la base de datos
            String titulos[] = {"ID DE VISITA","IDDISPOSITVO", "FECHA Y HORA DE LA VISITA"};
            String SQLTHD ="SELECT * FROM historialdispositivos ORDER BY IDHISTORIALDISPOSITIVOS DESC"; 
            DefaultTableModel model = new DefaultTableModel(null, titulos);
            Statement sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQLTHD);
            String[]fila=new String[3];
            while(rs.next()){
                fila[0] = rs.getString("IDHISTORIALDISPOSITIVOS");
                fila[1] = rs.getString("IDDISPOSITIVO");
                fila[2] = rs.getString("FECHAVISITADISPOSITIVO");
                model.addRow(fila);
                }
            rs.close();
            //jtUsuarios.removeAll();
            return model;
            //model.fireTableDataChanged();
            
        }catch(Exception e){
            return null;
        }
        
    }
    
    
    void Eliminar(){
        int fila = jtUsuarios.getSelectedRow();
        try {
            String SQL = "DELETE FROM usuarios WHERE IdUSUARIO=" + jtUsuarios.getValueAt(fila, 0);
            sent = con.createStatement();
            int n = sent.executeUpdate(SQL);
            if (n > 0){
                //JOptionPane.showMessageDialog(null, "Usuario eliminado correctamente ");
                jtUsuarios.setModel(LlenarTablaUsuarios());
                lblTotalUsuarios.setText(contarTotalU());
            }
            else JOptionPane.showMessageDialog(null, "Usuario no eliminado ");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: Debe seleccionar un usuario");
        }
    }
    
    void BuscarPorNombreUsuario (){
        if(UsuarioIngresado.parametroR.contains("Administrador/a")) {
            try{
             String titulos[] = {"ID", "TIPO USUARIO","NOMBRE","APELLIDO","CONTRASEÑA","CEDULA","CORREO ELECTRONICO","ESTADO"};

            //Consulta para la fecha de inicio a fecha de final
            String SQL = "SELECT *FROM usuarios WHERE NOMBRESUSUARIO Like '%"+txtBuscarPor.getText().toString().trim()+"%'ORDER BY NOMBRESUSUARIO ASC";

            model= new DefaultTableModel(null, titulos);
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            String[]fila=new String[8];
            while(rs.next()){
                fila[0] = rs.getString("IDUSUARIO");
                fila[1] = rs.getString("TIPOUSUARIO");
                fila[2] = rs.getString("NOMBRESUSUARIO");
                fila[3] = rs.getString("APELLIDOSUSUARIO");
                fila[4] = rs.getString("CONTRASENAUSUARIO");                
                fila[5] = rs.getString("CEDULAUSUARIO");
                fila[6] = rs.getString("CORREOUSUARIO");
                fila[7] = rs.getString("ESTADOUSUARIO");
                model.addRow(fila);
            }
            jtUsuarios.setModel(model);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error de Consulta..... :(");
        }
        }else{
        try{
            String titulos[] = {"ID", "TIPO USUARIO","NOMBRE","APELLIDO","CEDULA","CORREO ELECTRONICO","ESTADO"};

            //Consulta para la fecha de inicio a fecha de final
            String SQL = "SELECT *FROM usuarios WHERE NOMBRESUSUARIO Like '%"+txtBuscarPor.getText().toString().trim()+"%'ORDER BY NOMBRESUSUARIO ASC";

            model= new DefaultTableModel(null, titulos);
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            String[]fila=new String[7];
            while(rs.next()){
                fila[0] = rs.getString("IDUSUARIO");
                fila[1] = rs.getString("TIPOUSUARIO");
                fila[2] = rs.getString("NOMBRESUSUARIO");
                fila[3] = rs.getString("APELLIDOSUSUARIO");
                fila[4] = rs.getString("CEDULAUSUARIO");
                fila[5] = rs.getString("CORREOUSUARIO");
                fila[6] = rs.getString("ESTADOUSUARIO");
                model.addRow(fila);
            }
            jtUsuarios.setModel(model);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error de Consulta..... :(");
        }
        }
    }
    
    void BuscarPorApellidoUsuario (){
        if(UsuarioIngresado.parametroR.contains("Administrador/a")) {
try{
             String titulos[] = {"ID", "TIPO USUARIO","NOMBRE","APELLIDO","CONTRASEÑA","CEDULA","CORREO ELECTRONICO","ESTADO"};
            //Consulta para la fecha de inicio a fecha de final
            String SQL = "SELECT *FROM usuarios WHERE APELLIDOSUSUARIO Like '%"+txtBuscarPor.getText().toString().trim()+"%'ORDER BY NOMBRESUSUARIO ASC";
            model= new DefaultTableModel(null, titulos);
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            String[]fila=new String[8];
            while(rs.next()){
                fila[0] = rs.getString("IDUSUARIO");
                fila[1] = rs.getString("TIPOUSUARIO");
                fila[2] = rs.getString("NOMBRESUSUARIO");
                fila[3] = rs.getString("APELLIDOSUSUARIO");
                fila[4] = rs.getString("CONTRASENAUSUARIO");                
                fila[5] = rs.getString("CEDULAUSUARIO");
                fila[6] = rs.getString("CORREOUSUARIO");
                fila[7] = rs.getString("ESTADOUSUARIO");
                model.addRow(fila);
            }
            jtUsuarios.setModel(model);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error de Consulta..... :(");
        }
        }else{
        try{
            String titulos[] = {"ID", "TIPO USUARIO","NOMBRE","APELLIDO","CEDULA","CORREO ELECTRONICO","ESTADO"};
            //Consulta para la fecha de inicio a fecha de final
            String SQL = "SELECT *FROM usuarios WHERE APELLIDOSUSUARIO Like '%"+txtBuscarPor.getText().toString().trim()+"%'ORDER BY NOMBRESUSUARIO ASC";
            model= new DefaultTableModel(null, titulos);
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            String[]fila=new String[7];
            while(rs.next()){
                fila[0] = rs.getString("IDUSUARIO");
                fila[1] = rs.getString("TIPOUSUARIO");
                fila[2] = rs.getString("NOMBRESUSUARIO");
                fila[3] = rs.getString("APELLIDOSUSUARIO");
                fila[4] = rs.getString("CEDULAUSUARIO");
                fila[5] = rs.getString("CORREOUSUARIO");
                fila[6] = rs.getString("ESTADOUSUARIO");
                model.addRow(fila);
            }
            jtUsuarios.setModel(model);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error de Consulta..... :(");
        }
        }
    }
    
    void BuscarPorTipoUsuario (){
        if(UsuarioIngresado.parametroR.contains("Administrador/a")) {
try{
             String titulos[] = {"ID", "TIPO USUARIO","NOMBRE","APELLIDO","CONTRASEÑA","CEDULA","CORREO ELECTRONICO","ESTADO"};
            //Consulta para la fecha de inicio a fecha de final
            String SQL = "SELECT *FROM usuarios WHERE TIPOUSUARIO Like '%"+txtBuscarPor.getText().toString().trim()+"%'ORDER BY NOMBRESUSUARIO ASC";
            model= new DefaultTableModel(null, titulos);
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            String[]fila=new String[8];
            while(rs.next()){
                fila[0] = rs.getString("IDUSUARIO");
                fila[1] = rs.getString("TIPOUSUARIO");
                fila[2] = rs.getString("NOMBRESUSUARIO");
                fila[3] = rs.getString("APELLIDOSUSUARIO");
                fila[4] = rs.getString("CONTRASENAUSUARIO");                
                fila[5] = rs.getString("CEDULAUSUARIO");
                fila[6] = rs.getString("CORREOUSUARIO");
                fila[7] = rs.getString("ESTADOUSUARIO");
                model.addRow(fila);
            }
            jtUsuarios.setModel(model);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error de Consulta..... :(");
        }
        }else{
        try{
            String titulos[] = {"ID", "TIPO USUARIO","NOMBRE","APELLIDO","CEDULA","CORREO ELECTRONICO","ESTADO"};
            //Consulta para la fecha de inicio a fecha de final
            String SQL = "SELECT *FROM usuarios WHERE TIPOUSUARIO Like '%"+txtBuscarPor.getText().toString().trim()+"%'ORDER BY NOMBRESUSUARIO ASC";
            model= new DefaultTableModel(null, titulos);
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            String[]fila=new String[7];
            while(rs.next()){
                fila[0] = rs.getString("IDUSUARIO");
                fila[1] = rs.getString("TIPOUSUARIO");
                fila[2] = rs.getString("NOMBRESUSUARIO");
                fila[3] = rs.getString("APELLIDOSUSUARIO");
                fila[4] = rs.getString("CEDULAUSUARIO");
                fila[5] = rs.getString("CORREOUSUARIO");
                fila[6] = rs.getString("ESTADOUSUARIO");
                model.addRow(fila);
            }
            jtUsuarios.setModel(model);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error de Consulta..... :(");
        }
        }
    }
    
    void BuscarPorCedula (){
        
        this.txtBuscarPor.setEnabled(true);
        
        if(UsuarioIngresado.parametroR.contains("Administrador/a")) {
            
        try{
            String titulos[] = {"ID", "TIPO USUARIO","NOMBRE","APELLIDO","CONTRASEÑA","CEDULA","CORREO ELECTRONICO","ESTADO"};
            //Consulta para la fecha de inicio a fecha de final
            String SQL = "SELECT *FROM usuarios WHERE CEDULAUSUARIO Like '%"+txtBuscarPor.getText().toString().trim()+"%'ORDER BY NOMBRESUSUARIO ASC";
            model= new DefaultTableModel(null, titulos);
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            String[]fila=new String[8];
            while(rs.next()){
                fila[0] = rs.getString("IDUSUARIO");
                fila[1] = rs.getString("TIPOUSUARIO");
                fila[2] = rs.getString("NOMBRESUSUARIO");
                fila[3] = rs.getString("APELLIDOSUSUARIO");
                fila[4] = rs.getString("CONTRASENAUSUARIO");                
                fila[5] = rs.getString("CEDULAUSUARIO");
                fila[6] = rs.getString("CORREOUSUARIO");
                fila[7] = rs.getString("ESTADOUSUARIO");
                model.addRow(fila);
            }
            jtUsuarios.setModel(model);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error de Consulta..... :(");
        }
        }
        else{
        try{
            String titulos[] = {"ID", "TIPO USUARIO","NOMBRE","APELLIDO","CEDULA","CORREO ELECTRONICO","ESTADO"};
            //Consulta para la fecha de inicio a fecha de final
            String SQL = "SELECT *FROM usuarios WHERE CEDULAUSUARIO Like '%"+txtBuscarPor.getText().toString().trim()+"%'ORDER BY NOMBRESUSUARIO ASC";
            model= new DefaultTableModel(null, titulos);
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            String[]fila=new String[7];
            while(rs.next()){
                fila[0] = rs.getString("IDUSUARIO");
                fila[1] = rs.getString("TIPOUSUARIO");
                fila[2] = rs.getString("NOMBRESUSUARIO");
                fila[3] = rs.getString("APELLIDOSUSUARIO");
                fila[4] = rs.getString("CEDULAUSUARIO");
                fila[5] = rs.getString("CORREOUSUARIO");
                fila[6] = rs.getString("ESTADOUSUARIO");
                model.addRow(fila);
            }
            jtUsuarios.setModel(model);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error de Consulta..... :(");
        }
        }
    }
    
    void BuscarPorEstadoUsuario (){
        if(UsuarioIngresado.parametroR.contains("Administrador/a")) {
            try{
             String titulos[] = {"ID", "TIPO USUARIO","NOMBRE","APELLIDO","CONTRASEÑA","CEDULA","CORREO ELECTRONICO","ESTADO"};
            String SQL = "SELECT *FROM usuarios WHERE ESTADOUSUARIO = 1 ORDER BY NOMBRESUSUARIO ASC";
            model= new DefaultTableModel(null, titulos);
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            String[]fila=new String[8];
            while(rs.next()){
                fila[0] = rs.getString("IDUSUARIO");
                fila[1] = rs.getString("TIPOUSUARIO");
                fila[2] = rs.getString("NOMBRESUSUARIO");
                fila[3] = rs.getString("APELLIDOSUSUARIO");
                fila[4] = rs.getString("CONTRASENAUSUARIO");                
                fila[5] = rs.getString("CEDULAUSUARIO");
                fila[6] = rs.getString("CORREOUSUARIO");
                fila[7] = rs.getString("ESTADOUSUARIO");
                model.addRow(fila);
            }
        jtUsuarios.setModel(model);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error de Consulta..... :(");
        }
        }else{
        try{
            String titulos[] = {"ID", "TIPO USUARIO","NOMBRE","APELLIDO","CEDULA","CORREO ELECTRONICO","ESTADO"};
            String SQL = "SELECT *FROM usuarios WHERE ESTADOUSUARIO = 1 ORDER BY NOMBRESUSUARIO ASC";
            model= new DefaultTableModel(null, titulos);
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            String[]fila=new String[7];
            while(rs.next()){
                fila[0] = rs.getString("IDUSUARIO");
                fila[1] = rs.getString("TIPOUSUARIO");
                fila[2] = rs.getString("NOMBRESUSUARIO");
                fila[3] = rs.getString("APELLIDOSUSUARIO");
                fila[4] = rs.getString("CEDULAUSUARIO");
                fila[5] = rs.getString("CORREOUSUARIO");
                fila[6] = rs.getString("ESTADOUSUARIO");
                model.addRow(fila);
            }
        jtUsuarios.setModel(model);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error de Consulta..... :(");
        }
        }
    }
    
    
    void BuscarPorEstadoUsuarioInactivo (){
    if(UsuarioIngresado.parametroR.contains("Administrador/a")) {
        try{
             String titulos[] = {"ID", "TIPO USUARIO","NOMBRE","APELLIDO","CONTRASEÑA","CEDULA","CORREO ELECTRONICO","ESTADO"};
            String SQL = "SELECT *FROM usuarios WHERE ESTADOUSUARIO = 0 ORDER BY NOMBRESUSUARIO ASC";
            model= new DefaultTableModel(null, titulos);
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            String[]fila=new String[8];
            while(rs.next()){
                fila[0] = rs.getString("IDUSUARIO");
                fila[1] = rs.getString("TIPOUSUARIO");
                fila[2] = rs.getString("NOMBRESUSUARIO");
                fila[3] = rs.getString("APELLIDOSUSUARIO");
                fila[4] = rs.getString("CONTRASENAUSUARIO");                
                fila[5] = rs.getString("CEDULAUSUARIO");
                fila[6] = rs.getString("CORREOUSUARIO");
                fila[7] = rs.getString("ESTADOUSUARIO");
                model.addRow(fila);
            }
        jtUsuarios.setModel(model);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error de Consulta..... :(");
        }
    }else{
        try{
            String titulos[] = {"ID", "TIPO USUARIO","NOMBRE","APELLIDO","CEDULA","CORREO ELECTRONICO","ESTADO"};
            String SQL = "SELECT *FROM usuarios WHERE ESTADOUSUARIO = 0 ORDER BY NOMBRESUSUARIO ASC";
            model= new DefaultTableModel(null, titulos);
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            String[]fila=new String[7];
            while(rs.next()){
                fila[0] = rs.getString("IDUSUARIO");
                fila[1] = rs.getString("TIPOUSUARIO");
                fila[2] = rs.getString("NOMBRESUSUARIO");
                fila[3] = rs.getString("APELLIDOSUSUARIO");
                fila[4] = rs.getString("CEDULAUSUARIO");
                fila[5] = rs.getString("CORREOUSUARIO");
                fila[6] = rs.getString("ESTADOUSUARIO");
                model.addRow(fila);
            }
        jtUsuarios.setModel(model);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error de Consulta..... :(");
        }
                }
    }
    
    
    
    void BuscarPorFechaVisita (){
        try{
            String titulos[] = {"ID","VISITANTE","FECHA Y HORA DE LA VISITA"};
            a = jdtHistDesde.getDate().getYear() + 1900;
            m = jdtHistDesde.getDate().getMonth() + 1;
            d = jdtHistDesde.getDate().getDate();
            String mm,dm,cal;
            if(m<10) mm = "0" + m;
            else mm = String.valueOf(m);
            if(d<10) dm = "0" + d;
            else dm = String.valueOf(d);
            fq = a + "-" + mm + "-" + dm;
            cal=fq;
            String SQL ="SELECT * FROM historialvisitas WHERE DATE(FECHAHORAVISITA) BETWEEN '" + cal + "' AND '" + cal + "' ORDER BY IDHISTORIALVISITA ASC";
            System.out.println(SQL);
            model= new DefaultTableModel(null, titulos);
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            String[]fila=new String[3];
            while(rs.next()){
                fila[0] = rs.getString("IDHISTORIALVISITA");
                fila[1] = rs.getString("NOMBRESAPELLIDOSVISITANTE");
                fila[2] = rs.getString("FECHAHORAVISITA");
                model.addRow(fila);
            }
        jtHistorialVisita.setModel(model);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error de Consulta..... :(");
        }
    }
    
    void BuscarPorFechaVisitaDesdeHasta (){
        try{
            String titulos[] = {"ID","VISITANTE","FECHA DE LA VISITA"};
            a = jdtHistDesde.getDate().getYear() + 1900;
            m = jdtHistDesde.getDate().getMonth() + 1;
            d = jdtHistDesde.getDate().getDate();
            String mm,dm,mmh,dmh;
            if(m<10) mm = "0" + m;
            else mm = String.valueOf(m);
            if(d<10) dm = "0" + d;
            else dm = String.valueOf(d);
            fq = a + "-" + mm + "-" + dm;
            cal=fq.toString();
            a1 = jdtHistHasta.getDate().getYear() + 1900;
            m1 = jdtHistHasta.getDate().getMonth() + 1;
            d1 = jdtHistHasta.getDate().getDate();
            if(m1<10) mmh = "0" + m1;
            else mmh = String.valueOf(m1);
            if(d1<10) dmh = "0" + d1;
            else dmh = String.valueOf(d1);
            ff = a1 + "-" + mmh + "-" + dmh;
            cal1=ff.toString();
            String SQL ="SELECT * FROM historialvisitas WHERE DATE(FECHAHORAVISITA) BETWEEN '" + cal + "' AND '" + cal1 + "' ORDER BY IDHISTORIALVISITA DESC";
            System.out.println(SQL);
            model= new DefaultTableModel(null, titulos);
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            String[]fila=new String[3];
            while(rs.next()){
                fila[0] = rs.getString("IDHISTORIALVISITA");
                fila[1] = rs.getString("NOMBRESAPELLIDOSVISITANTE");
                fila[2] = rs.getString("FECHAHORAVISITA");
                model.addRow(fila);
            }
        jtHistorialVisita.setModel(model);
        }catch(Exception e){
            //JOptionPane.showMessageDialog(null,"Error de Consulta..... :(");
        }
    }
        
    void BuscarPorFechaVisitaDispositivoDesdeHasta (){
        try{
            String titulos[] = {"ID","VISITANTE","FECHA DE LA VISITA"};
            a = jdtHistDesde.getDate().getYear() + 1900;
            m = jdtHistDesde.getDate().getMonth() + 1;
            d = jdtHistDesde.getDate().getDate();
            String mm,dm,mmh,dmh;
            if(m<10) mm = "0" + m;
            else mm = String.valueOf(m);
            if(d<10) dm = "0" + d;
            else dm = String.valueOf(d);
            fq = a + "-" + mm + "-" + dm;
            cal=fq;
            a1 = jdtHistHasta.getDate().getYear() + 1900;
            m1 = jdtHistHasta.getDate().getMonth() + 1;
            d1 = jdtHistHasta.getDate().getDate();
            if(m1<10) mmh = "0" + m1;
            else mmh = String.valueOf(m1);
            if(d1<10) dmh = "0" + d1;
            else dmh = String.valueOf(d1);
            ff = a1 + "-" + mmh + "-" + dmh;
            cal1=ff;
            String SQL ="SELECT * FROM historialdispositivos WHERE DATE(FECHAVISITADISPOSITIVO) BETWEEN '" + cal + "' AND '" + cal1 + "' ORDER BY FECHAVISITADISPOSITIVO ASC";
            //String SQL ="SELECT * FROM historialvisitas where substring(FECHAHORAVISITA,1,10)  >= '" + cal + "' AND substring(FECHAHORAVISITA,1,10) <='" + cal1 + "'ORDER BY IDHISTORIALVISITA DESC";
            System.out.println(SQL);
            model= new DefaultTableModel(null, titulos);
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            String[]fila=new String[3];
            while(rs.next()){
                fila[0] = rs.getString("IDHISTORIALDISPOSITIVOS");
                fila[1] = rs.getString("IDDISPOSITIVO");
                fila[2] = rs.getString("FECHAVISITADISPOSITIVO");
                model.addRow(fila);
            }
        jtHistorialVisita.setModel(model);
        }catch(Exception e){
            //JOptionPane.showMessageDialog(null,"Error de Consulta..... :(");
        }
    }
    
    void BuscarPorFechaVisitaDispositivo(){
        try{
            String titulos[] = {"ID","VISITANTE","FECHA Y HORA DE LA VISITA"};
            int a = jdtHistDesde.getDate().getYear() + 1900;
            int m = jdtHistDesde.getDate().getMonth() + 1;
            int d = jdtHistDesde.getDate().getDate();
            String mm,dm,cal,fq;
            if(m<10) mm="0"+m;
            else mm=String.valueOf(m);
            if(d<10) dm="0"+d;
            else dm=String.valueOf(d);
            fq=a+"-"+mm+"-"+dm;
            cal=fq;
            String SQL ="SELECT * FROM historialdispositivos WHERE DATE(FECHAVISITADISPOSITIVO) BETWEEN '" + cal + "' AND '" + cal + "' ORDER BY IDHISTORIALDISPOSITIVOS ASC";
            System.out.println(SQL);
            model= new DefaultTableModel(null, titulos);
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            String[]fila=new String[3];
            while(rs.next()){
                fila[0] = rs.getString("IDHISTORIALDISPOSITIVOS");
                fila[1] = rs.getString("IDDISPOSITIVO");
                fila[2] = rs.getString("FECHAVISITADISPOSITIVO");
                model.addRow(fila);
            }
        jtHistorialVisita.setModel(model);
        }catch(Exception e){
            //JOptionPane.showMessageDialog(null,"Error de Consulta..... :(");
        }
    }
            
    public void centrarVentanaInterna (JDesktopPane desktopPane, JInternalFrame internalFrame){
        x = (desktopPane.getWidth() / 2) - internalFrame.getWidth() / 2; 
        y = (desktopPane.getHeight() / 2) - internalFrame.getHeight() / 2;
        if(internalFrame.isShowing()){
            internalFrame.setLocation(x, y);
        }else{
            desktopPane.add(internalFrame);
            internalFrame.setLocation(x, y);
            internalFrame.show();
        }
    }
        
    public static void centrarVentanaGestionCA (JInternalFrame internalFrame){
        jdeskGaleria.add(internalFrame);
        int width = jdeskGaleria.getWidth();
        int Height = jdeskGaleria.getHeight();
        internalFrame.setSize(width, Height);
        internalFrame.show();
    }
    
    void SeleccionarItemTablaU(java.awt.event.MouseEvent evt){
        DefaultTableModel modelo=(DefaultTableModel) jtUsuarios.getModel();
        id=String.valueOf(modelo.getValueAt(jtUsuarios.getSelectedRow(),0));
        rol=String.valueOf(modelo.getValueAt(jtUsuarios.getSelectedRow(),1));
        estado=String.valueOf(modelo.getValueAt(jtUsuarios.getSelectedRow(),6));
        lblNuevo.setText(id);
    }
    
    public static void Mostrar_Visualizador(JLabel Pantalla, String RutaDestino){
        try
        {
            Image capturarImgSoloLectura = ImageIO.read(new File(RutaDestino));
            Image obtenerImagen = capturarImgSoloLectura.getScaledInstance(Pantalla.getWidth(),Pantalla.getHeight(), Image.SCALE_SMOOTH);
            Icon iconoEscalado = new ImageIcon(obtenerImagen);
            Pantalla.setIcon(iconoEscalado);
        }
        catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
    
    void cerrarSesion(){
        Object [] opciones={"Aceptar","Cancelar"};
        int eleccion=JOptionPane.showOptionDialog(null,"Está seguro que desea cerrar sesión","Mensaje de Confirmación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,null,opciones,"Aceptar");
        if(eleccion==JOptionPane.YES_OPTION){
            Login frlog=new Login();
            frlog.show();
            dispose();
        }
    }
    
    void GuardarVisitante(){
        try {
            String SQL = "INSERT INTO historialvisitas(NOMBRESAPELLIDOSVISITANTE,FECHAHORAVISITA)"
                    + " VALUES(?,?)";
            PreparedStatement ps = con.prepareStatement(SQL);
            Date date = new Date();
            DateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String fecha = sdf.format(date);
            ps.setString(1, txtnombreVisitante.getText());
            ps.setString(2, fecha);
            int n = ps.executeUpdate();
            if (n > 0) {
                JOptionPane.showMessageDialog(this, "Registro exitoso");
                //actualizarVisitantes();
                jtHistorialVisita.setModel(LlenarTablaHistorialVisita());
                txtnombreVisitante.setText("");
                txtTotalVisitas.setText(String.valueOf((Integer.parseInt(contarTotalVisitas())+(Integer.parseInt(contarTotalVisitasDispositivos())))));
            } 
        } catch (SQLException e) {
            JOptionPane.showConfirmDialog(this, "Error: " + e.getMessage());
            System.out.println();
        }
    }
    
    void SeleccionarItemTablaVisitantes(java.awt.event.MouseEvent evt){
        DefaultTableModel modelo=(DefaultTableModel) jtHistorialVisita.getModel();
        idV=String.valueOf(modelo.getValueAt(jtHistorialVisita.getSelectedRow(),0));
        //categoria=String.valueOf(modelo.getValueAt(jtContenidosArticulos.getSelectedRow(),1));
        //imagenes = String.valueOf(modelo.getValueAt(jtContenidosArticulos.getSelectedRow(),5));
        
     }
    
    void SeleccionarItemTablaVisitantesDispositivo(java.awt.event.MouseEvent evt){
        //DefaultTableModel modelo=(DefaultTableModel) jtHistorialVisitaDispositivo.getModel();
       // idVD=String.valueOf(modelo.getValueAt(jtHistorialVisitaDispositivo.getSelectedRow(),0));
        
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
    
    void EstadisticaComparativa(){
        if(idAnioDis == 0){
            JOptionPane.showMessageDialog(this, "Debe de seleccionar un año");
            return;
        }
        else{
            lblTortaDis.setText("");
            JFreeChart barra;
            DefaultCategoryDataset datos;
            int ene=0,feb=0,mar=0,abr=0,ma=0,jun=0,jul=0,agos=0,sep=0,oct=0,nov=0,dic=0,total=0;
            int eneD=0,febD=0,marD=0,abrD=0,maD=0,junD=0,julD=0,agosD=0,sepD=0,octD=0,novD=0,dicD=0,totalD=0;
            ene=(Integer.valueOf(visitantesEneroComp()));
            eneD=(Integer.valueOf(visitantesEneDisComp()));
            feb=(Integer.valueOf(visitantesFebrComp()));
            febD=(Integer.valueOf(visitantesFebDisComp()));
            mar=(Integer.valueOf(visitantesMarComp()));
            marD=(Integer.valueOf(visitantesMarDisComp()));
            abr=(Integer.valueOf(visitantesAbrComp()));
            abrD=(Integer.valueOf(visitantesAbrDisComp()));
            ma=(Integer.valueOf(visitantesMayoComp()));
            maD=(Integer.valueOf(visitantesMayoDisComp()));
            jun=(Integer.valueOf(visitantesJunComp()));
            junD=(Integer.valueOf(visitantesJunDisComp()));
            jul=(Integer.valueOf(visitantesJulComp()));
            julD=(Integer.valueOf(visitantesJulDisComp()));
            agos=(Integer.valueOf(visitantesAgosComp()));
            agosD=(Integer.valueOf(visitantesAgosDisComp()));
            sep=(Integer.valueOf(visitantesSepComp()));
            sepD=(Integer.valueOf(visitantesSepDisComp()));
            oct=(Integer.valueOf(visitantesOctComp()));
            octD=(Integer.valueOf(visitantesOcDisComp()));
            nov=(Integer.valueOf(visitantesNovComp()));
            novD=(Integer.valueOf(visitantesNovDisComp()));
            dic=(Integer.valueOf(visitantesDicComp()));
            dicD=(Integer.valueOf(visitantesDicDisComp()));
            total=ene+feb+mar+abr+ma+jun+jul+sep+agos+oct+nov+dic;
            totalD=eneD+febD+marD+abrD+maD+junD+julD+sepD+agosD+octD+novD+dicD;
            //datos = new DefaultPieDataset(); 
            datos = new DefaultCategoryDataset(); 
            //datos.setValue(marD,"Dipositivos","Mar:"+mar+"-"+marD);
            //datos.setValue(mar,"Visitantes","Mar:"+mar+"-"+marD);
            datos.setValue(eneD,"Dipositivos","Ene");
            datos.setValue(ene,"Visitantes","Ene");
            datos.setValue(febD,"Dipositivos","Feb");
            datos.setValue(feb,"Visitantes","Feb");
            datos.setValue(marD,"Dipositivos","Mar");
            datos.setValue(mar,"Visitantes","Mar");
            datos.setValue(abrD,"Dipositivos","Ab");
            datos.setValue(abr,"Visitantes","Ab");
            datos.setValue(ma,"Dipositivos","May");
            datos.setValue(ma,"Visitantes","May");
            datos.setValue(junD,"Dipositivos","Jun");
            datos.setValue(jun,"Visitantes","Jun");
            datos.setValue(jul,"Dipositivos","Jul");
            datos.setValue(jul,"Visitantes","Jul");
            datos.setValue(agos,"Dipositivos","Ago");
            datos.setValue(agos,"Visitantes","Ago");
            datos.setValue(sep,"Dipositivos","Sep");
            datos.setValue(sep,"Visitantes","Sep");
            datos.setValue(oct,"Dipositivos","Oct");
            datos.setValue(oct,"Visitantes","Oct");
            datos.setValue(nov,"Dipositivos","Nov");
            datos.setValue(nov,"Visitantes","Nov");
            datos.setValue(dic,"Dipositivos","Dic");
            datos.setValue(dic,"Visitantes","Dic");

            barra = ChartFactory.createBarChart("Visitantes vs Dispositivos del año "+jcbAñoGraficoEstadisticoComparativo.getSelectedItem(), "Tipo de Visitante","Visitantes",datos,PlotOrientation.VERTICAL,true,true,false);
            barra.setBackgroundPaint(Color.cyan);
            barra.getTitle().setPaint(Color.black); 
            BufferedImage graficoBarra=barra.createBufferedImage(panelGraficoTortaDis.getWidth(), panelGraficoTortaDis.getHeight());
            CategoryPlot p = barra.getCategoryPlot(); 
            p.setRangeGridlinePaint(Color.red); 
            //barra=ChartFactory.createPieChart("Visitantes del Año : ",datos,true,true,true);
            //BufferedImage graficoTorta = barra.createBufferedImage(panelGraficoTorta.getWidth(), panelGraficoTorta.getHeight());
            lblTortaDis.setSize(panelGraficoTortaDis.getSize());
            //lblTorta.setIcon(new ImageIcon(graficoTorta));
            lblTortaDis.setIcon(new ImageIcon(graficoBarra));
            txtTotalVisitasVisitantesPorAñoComparativo.setText(String.valueOf(total));
            txtTotalVisitasDispositivosPorAñoCompartivo.setText(String.valueOf(totalD));
            panelGraficoTortaDis.updateUI();
            //JOptionPane.showMessageDialog(this,jcbAñoGraficoEstadistico.getSelectedItem());
            try {
                ChartUtilities.saveChartAsJPEG(new File(ValoresConstantes.DIRECTORIO_PRINCIPAL + "\\graficoEC.jpg"), barra, 1280, 720);
                //JOptionPane.showMessageDialog(this,jcbAñoGraficoEstadistico.getSelectedItem());
            } catch (IOException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    void EstadisticaVisitantes(){
        if(idAnio == 0){
            JOptionPane.showMessageDialog(this, "Debe de seleccionar un año");
            return;
        }
        else{
            lblTorta.setText("");
            JFreeChart barra;
            DefaultCategoryDataset datos;
            int ene=0,feb=0,mar=0,abr=0,ma=0,jun=0,jul=0,agos=0,sep=0,oct=0,nov=0,dic=0,total=0;
            ene=(Integer.valueOf(visitantesE()));
            feb=(Integer.valueOf(visitantesF()));
            mar=(Integer.valueOf(visitantesM()));
            abr=(Integer.valueOf(visitantesA()));
            ma=(Integer.valueOf(visitantesMayo()));
            jun=(Integer.valueOf(visitantesJ()));
            jul=(Integer.valueOf(visitantesJul()));
            agos=(Integer.valueOf(visitantesAg()));
            sep=(Integer.valueOf(visitantesSep()));
            oct=(Integer.valueOf(visitantesOc()));
            nov=(Integer.valueOf(visitantesNov()));
            dic=(Integer.valueOf(visitantesDic()));
            total=ene+feb+mar+abr+ma+jun+jul+sep+agos+oct+nov+dic;
            //datos = new DefaultPieDataset(); 
            datos = new DefaultCategoryDataset(); 
            datos.setValue(ene,"Enero: "+ene+" ","");
            datos.setValue(feb,"Febrero : "+feb+" ","");
            datos.setValue(mar,"Marzo : "+mar+" ","");
            datos.setValue(abr,"Abril : "+abr+" ","");
            datos.setValue(ma,"Mayo : "+ma+" ","");
            datos.setValue(jun,"Junio : "+jun+" ","");
            datos.setValue(jul,"Julio : "+ jul+" ","");
            datos.setValue(agos,"Agosto : "+agos+" ","");
            datos.setValue(sep,"Septiembre : "+ sep+" ","");
            datos.setValue(oct,"Octubre : "+oct+" ","");
            datos.setValue(nov,"Noviembre : "+nov+" ","");
            datos.setValue(dic,"Diciembre : "+dic+" ","");

            barra = ChartFactory.createBarChart3D("Visitantes del año "+jcbAñoGraficoEstadistico.getSelectedItem(), "Meses","Visitantes",datos,PlotOrientation.VERTICAL,true,true,true);
            BufferedImage graficoBarra=barra.createBufferedImage(panelGraficoTorta.getWidth(), panelGraficoTorta.getHeight());

            //barra=ChartFactory.createPieChart("Visitantes del Año : ",datos,true,true,true);
            //BufferedImage graficoTorta = barra.createBufferedImage(panelGraficoTorta.getWidth(), panelGraficoTorta.getHeight());
            lblTorta.setSize(panelGraficoTorta.getSize());
            //lblTorta.setIcon(new ImageIcon(graficoTorta));
            lblTorta.setIcon(new ImageIcon(graficoBarra));
            txtTotalVisitasPorAño.setText(String.valueOf(total));
            panelGraficoTorta.updateUI();
            try {
                ChartUtilities.saveChartAsJPEG(new File(ValoresConstantes.DIRECTORIO_PRINCIPAL + "\\graficoE.jpg"), barra, 1280, 720);
                //JOptionPane.showMessageDialog(this,jcbAñoGraficoEstadistico.getSelectedItem());
            } catch (IOException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    void EstadisticasDispositivos(){
        if(idAnio == 0){
            JOptionPane.showMessageDialog(this, "Debe seleccionar un año");
            return;
        }
        else{
            lblTorta.setText("");
            JFreeChart barra;
            DefaultCategoryDataset datos;
            int ene=0,feb=0,mar=0,abr=0,ma=0,jun=0,jul=0,agos=0,sep=0,oct=0,nov=0,dic=0,total=0;
            ene=(Integer.valueOf(visitantesED()));
            feb=(Integer.valueOf(visitantesFD()));
            mar=(Integer.valueOf(visitantesMD()));
            abr=(Integer.valueOf(visitantesAD()));
            ma=(Integer.valueOf(visitantesMayoD()));
            jun=(Integer.valueOf(visitantesJD()));
            jul=(Integer.valueOf(visitantesJulD()));
            agos=(Integer.valueOf(visitantesAgD()));
            sep=(Integer.valueOf(visitantesSepD()));
            oct=(Integer.valueOf(visitantesOcD()));
            nov=(Integer.valueOf(visitantesNovD()));
            dic=(Integer.valueOf(visitantesDicD()));
            total=ene+feb+mar+abr+ma+jun+jul+sep+agos+oct+nov+dic;
            datos = new DefaultCategoryDataset(); 
            datos.setValue(ene,"Enero : "+ene+" ","");
            datos.setValue(feb,"Febrero : "+feb+" ","");
            datos.setValue(mar,"Marzo : "+mar+" ","");
            datos.setValue(abr,"Abril : "+abr+" ","");
            datos.setValue(ma,"Mayo : "+ma+" ","");
            datos.setValue(jun,"Junio : "+jun+" ","");
            datos.setValue(jul,"Julio : "+ jul+" ","");
            datos.setValue(agos,"Agosto : "+agos+" ","");
            datos.setValue(sep,"Septiembre : "+ sep+" ","");
            datos.setValue(oct,"Octubre : "+oct+" ","");
            datos.setValue(nov,"Noviembre : "+nov+" ","");
            datos.setValue(dic,"Diciembre : "+dic+" ","");

            barra = ChartFactory.createBarChart3D("Dispositivos conectados al año "+jcbAñoGraficoEstadistico.getSelectedItem(), "Meses","Visitantes",datos,PlotOrientation.VERTICAL,true,true,true);
            BufferedImage graficoBarra=barra.createBufferedImage(panelGraficoTorta.getWidth(), panelGraficoTorta.getHeight());

            //barra=ChartFactory.createPieChart("Visitantes del Año : ",datos,true,true,true);
            //BufferedImage graficoTorta = barra.createBufferedImage(panelGraficoTorta.getWidth(), panelGraficoTorta.getHeight());
            lblTorta.setSize(panelGraficoTorta.getSize());
            //lblTorta.setIcon(new ImageIcon(graficoTorta));
            lblTorta.setIcon(new ImageIcon(graficoBarra));
            txtTotalVisitasPorAño.setText(String.valueOf(total));
            panelGraficoTorta.updateUI();
            //JOptionPane.showMessageDialog(this,jcbAñoGraficoEstadistico.getSelectedItem());
            try {
                ChartUtilities.saveChartAsJPEG(new File(ValoresConstantes.DIRECTORIO_PRINCIPAL + "\\graficoE.jpg"), barra, 1280, 720);
                //JOptionPane.showMessageDialog(this,jcbAñoGraficoEstadistico.getSelectedItem());
            } catch (IOException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btgSeleccion = new javax.swing.ButtonGroup();
        btgBuscarHistorialPor = new javax.swing.ButtonGroup();
        btgVerEstadisticas = new javax.swing.ButtonGroup();
        tabMenu = new javax.swing.JTabbedPane();
        jpPrincipal = new javax.swing.JPanel();
        PanelDerechosMuseo = new javax.swing.JPanel();
        jlJJ2016Principal = new javax.swing.JLabel();
        lblTerminosyCondicionesPrincipal = new javax.swing.JLabel();
        lblPoliticasdePrivacidadPrincipal = new javax.swing.JLabel();
        lblUsuarioyRolPrincipal = new javax.swing.JLabel();
        jdeskPrincipal = new javax.swing.JDesktopPane();
        imgMuseo1 = new javax.swing.JLabel();
        imgMuseo2 = new javax.swing.JLabel();
        jlGaleria = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtHistoriaMuseo = new javax.swing.JTextArea();
        imgMuseo3 = new javax.swing.JLabel();
        imgMuseo4 = new javax.swing.JLabel();
        imgMuseo5 = new javax.swing.JLabel();
        imgQrMuseo = new javax.swing.JLabel();
        btnEditarMuseo = new javax.swing.JButton();
        btnCancelarMuseo = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lblLimiteNombreMuseo = new javax.swing.JLabel();
        lblNombreMuseo = new javax.swing.JLabel();
        lblFundadorMuseo = new javax.swing.JLabel();
        lblLimiteFundadorMuseo = new javax.swing.JLabel();
        lblLimiteHistoriaMuseo = new javax.swing.JLabel();
        txtFundadorMuseo = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        txtNombreMuseo = new javax.swing.JTextField();
        Date date = new Date();
        SpinnerDateModel fecha = new SpinnerDateModel(date, null, null, Calendar.DATE);
        spnFecha = new javax.swing.JSpinner(fecha);
        jLabel44 = new javax.swing.JLabel();
        btnVideoMuseo = new javax.swing.JLabel();
        jlVideoMuseo = new javax.swing.JLabel();
        btnAudioMuseo = new javax.swing.JLabel();
        jlAudioMuseo = new javax.swing.JLabel();
        PanelRol = new javax.swing.JPanel();
        lblCerrarSesion5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jpUsuarios = new javax.swing.JPanel();
        jdeskusuarios = new javax.swing.JDesktopPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtUsuarios = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        btnNuevoUsuario = new javax.swing.JLabel();
        lblNuevo = new javax.swing.JLabel();
        btnActualizarUsuario = new javax.swing.JLabel();
        btnEliminarUsuario = new javax.swing.JLabel();
        btnBuscarUsuarios = new javax.swing.JLabel();
        jcbBuscarPor = new javax.swing.JComboBox<>();
        txtBuscarPor = new javax.swing.JTextField();
        rbtnActivo = new javax.swing.JRadioButton();
        rbtnInactivo = new javax.swing.JRadioButton();
        jLabel39 = new javax.swing.JLabel();
        lblTotalUsuarios = new javax.swing.JLabel();
        lblNuevoUsuario = new javax.swing.JLabel();
        lblActualizarUsuario = new javax.swing.JLabel();
        lblEliminarUsuario = new javax.swing.JLabel();
        lblBuscarUsuario = new javax.swing.JLabel();
        btnImprimirUsuarios = new javax.swing.JLabel();
        PanelRolUsuarios = new javax.swing.JPanel();
        lblCerrarSesion6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jlJJ2016Usuarios = new javax.swing.JLabel();
        lblTerminosyCondicionesUsuarios = new javax.swing.JLabel();
        lblPoliticasdePrivacidadUsuarios = new javax.swing.JLabel();
        lblUsuarioyRolUsuarios = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jdeskGaleria = new javax.swing.JDesktopPane();
        jPanel10 = new javax.swing.JPanel();
        jlJJ2016Galeria = new javax.swing.JLabel();
        lblTerminosyCondicionesGaleria = new javax.swing.JLabel();
        lblPoliticasdePrivacidadGaleria = new javax.swing.JLabel();
        lblUsuarioyRolGaleria = new javax.swing.JLabel();
        PanelRolUsuarios1 = new javax.swing.JPanel();
        lblCerrarSesion7 = new javax.swing.JLabel();
        lblTituloGaleria = new javax.swing.JLabel();
        btnGestionArticulos = new javax.swing.JButton();
        btnGestionCategoria = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jdeskContactanos = new javax.swing.JDesktopPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtHistorialVisita = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        jLabel3 = new javax.swing.JLabel();
        txtTotalVisitas = new javax.swing.JTextField();
        btnImprimirVisitantes = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel56 = new javax.swing.JLabel();
        txtnombreVisitante = new javax.swing.JTextField();
        btnNuevoVisitante = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel57 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblBuscarHistorialVisita = new javax.swing.JLabel();
        jrbFiltroVisitante = new javax.swing.JRadioButton();
        jLabel59 = new javax.swing.JLabel();
        jdtHistHasta = new com.toedter.calendar.JDateChooser();
        jrbFiltroDispositivo = new javax.swing.JRadioButton();
        jdtHistDesde = new com.toedter.calendar.JDateChooser();
        jrbFiltroTodo = new javax.swing.JRadioButton();
        jPanel7 = new javax.swing.JPanel();
        jlJJ2016Historial = new javax.swing.JLabel();
        lblTerminosyCondicionesContactanos = new javax.swing.JLabel();
        lblPoliticasdePrivacidadContactanos = new javax.swing.JLabel();
        lblUsuarioyRolContactanos = new javax.swing.JLabel();
        PanelRolUsuarios3 = new javax.swing.JPanel();
        lblCerrarSesion9 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jdeskAcercade = new javax.swing.JDesktopPane();
        jPanelEstadisticaVisitaImprimir = new javax.swing.JPanel();
        jcbAñoGraficoEstadistico = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        btnVerEstadisticaAnual = new javax.swing.JLabel();
        panelGraficoTorta = new javax.swing.JPanel();
        lblTorta = new javax.swing.JLabel();
        lblTotalPastel = new javax.swing.JLabel();
        txtTotalVisitasPorAño = new javax.swing.JTextField();
        btnImprimirEstadistica = new javax.swing.JLabel();
        jrbEstadisticasVisitantes = new javax.swing.JRadioButton();
        jrbEstadisticasDispositivos = new javax.swing.JRadioButton();
        jPanelEstadisticaDispositivoImprimir = new javax.swing.JPanel();
        btnImprimirEstadisticaDispositivos = new javax.swing.JLabel();
        lblTotalPastelDispositivos = new javax.swing.JLabel();
        txtTotalVisitasVisitantesPorAñoComparativo = new javax.swing.JTextField();
        jcbAñoGraficoEstadisticoComparativo = new javax.swing.JComboBox<>();
        btnVerEstadisticaAnualDispositivos = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        panelGraficoTortaDis = new javax.swing.JPanel();
        lblTortaDis = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtTotalVisitasDispositivosPorAñoCompartivo = new javax.swing.JTextField();
        PanelRolUsuarios8 = new javax.swing.JPanel();
        lblCerrarSesionAcerca = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jlJJ2016Acercade = new javax.swing.JLabel();
        lblTerminosyCondicionesAcerca = new javax.swing.JLabel();
        lblPoliticasdePrivacidadacerca = new javax.swing.JLabel();
        lblUsuarioyRolAcerca = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setUndecorated(true);

        tabMenu.setBackground(new java.awt.Color(0, 102, 153));
        tabMenu.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        tabMenu.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        jpPrincipal.setBackground(new java.awt.Color(0, 0, 0));

        PanelDerechosMuseo.setBackground(new java.awt.Color(0, 0, 0));

        jlJJ2016Principal.setForeground(new java.awt.Color(255, 255, 255));
        jlJJ2016Principal.setText("JJ 2016 Reservados  todos  los  derechos . ");
        jlJJ2016Principal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jlJJ2016Principal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlJJ2016PrincipalMouseClicked(evt);
            }
        });

        lblTerminosyCondicionesPrincipal.setForeground(new java.awt.Color(255, 51, 0));
        lblTerminosyCondicionesPrincipal.setText("Terminos y Condiciones");
        lblTerminosyCondicionesPrincipal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblTerminosyCondicionesPrincipal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTerminosyCondicionesPrincipalMouseClicked(evt);
            }
        });

        lblPoliticasdePrivacidadPrincipal.setForeground(new java.awt.Color(153, 204, 255));
        lblPoliticasdePrivacidadPrincipal.setText("|  Políticas de Privacidad");
        lblPoliticasdePrivacidadPrincipal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblPoliticasdePrivacidadPrincipal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPoliticasdePrivacidadPrincipalMouseClicked(evt);
            }
        });

        lblUsuarioyRolPrincipal.setForeground(new java.awt.Color(255, 0, 0));
        lblUsuarioyRolPrincipal.setText("jLabel1");
        lblUsuarioyRolPrincipal.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout PanelDerechosMuseoLayout = new javax.swing.GroupLayout(PanelDerechosMuseo);
        PanelDerechosMuseo.setLayout(PanelDerechosMuseoLayout);
        PanelDerechosMuseoLayout.setHorizontalGroup(
            PanelDerechosMuseoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelDerechosMuseoLayout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(jlJJ2016Principal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTerminosyCondicionesPrincipal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblPoliticasdePrivacidadPrincipal)
                .addGap(132, 132, 132)
                .addComponent(lblUsuarioyRolPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(84, 84, 84))
        );
        PanelDerechosMuseoLayout.setVerticalGroup(
            PanelDerechosMuseoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelDerechosMuseoLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(PanelDerechosMuseoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlJJ2016Principal)
                    .addComponent(lblTerminosyCondicionesPrincipal)
                    .addComponent(lblPoliticasdePrivacidadPrincipal)
                    .addComponent(lblUsuarioyRolPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jdeskPrincipal.setBackground(new java.awt.Color(204, 204, 255));

        imgMuseo1.setFont(new java.awt.Font("Century Schoolbook", 0, 14)); // NOI18N
        imgMuseo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/museo2.jpg"))); // NOI18N
        imgMuseo1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        imgMuseo1.setEnabled(false);
        imgMuseo1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imgMuseo1MouseClicked(evt);
            }
        });

        imgMuseo2.setFont(new java.awt.Font("Century Schoolbook", 0, 14)); // NOI18N
        imgMuseo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/museo1.jpg"))); // NOI18N
        imgMuseo2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        imgMuseo2.setEnabled(false);
        imgMuseo2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imgMuseo2MouseClicked(evt);
            }
        });

        jlGaleria.setFont(new java.awt.Font("Century Schoolbook", 1, 14)); // NOI18N
        jlGaleria.setText("Galería del Museo de la Escuela Isidro Ayora");

        txtHistoriaMuseo.setEditable(false);
        txtHistoriaMuseo.setColumns(20);
        txtHistoriaMuseo.setLineWrap(true);
        txtHistoriaMuseo.setRows(5);
        txtHistoriaMuseo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHistoriaMuseoKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(txtHistoriaMuseo);

        imgMuseo3.setFont(new java.awt.Font("Century Schoolbook", 0, 14)); // NOI18N
        imgMuseo3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/museo1.jpg"))); // NOI18N
        imgMuseo3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        imgMuseo3.setEnabled(false);
        imgMuseo3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imgMuseo3MouseClicked(evt);
            }
        });

        imgMuseo4.setFont(new java.awt.Font("Century Schoolbook", 0, 14)); // NOI18N
        imgMuseo4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/museo1.jpg"))); // NOI18N
        imgMuseo4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        imgMuseo4.setEnabled(false);
        imgMuseo4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imgMuseo4MouseClicked(evt);
            }
        });

        imgMuseo5.setFont(new java.awt.Font("Century Schoolbook", 0, 14)); // NOI18N
        imgMuseo5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/museo1.jpg"))); // NOI18N
        imgMuseo5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        imgMuseo5.setEnabled(false);
        imgMuseo5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imgMuseo5MouseClicked(evt);
            }
        });

        imgQrMuseo.setFont(new java.awt.Font("Century Schoolbook", 0, 14)); // NOI18N
        imgQrMuseo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/QR.png"))); // NOI18N

        btnEditarMuseo.setBackground(new java.awt.Color(0, 0, 0));
        btnEditarMuseo.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        btnEditarMuseo.setForeground(new java.awt.Color(255, 255, 255));
        btnEditarMuseo.setText("Editar");
        btnEditarMuseo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarMuseoActionPerformed(evt);
            }
        });

        btnCancelarMuseo.setBackground(new java.awt.Color(0, 0, 0));
        btnCancelarMuseo.setFont(new java.awt.Font("Trebuchet MS", 0, 18)); // NOI18N
        btnCancelarMuseo.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelarMuseo.setText("Cancelar");
        btnCancelarMuseo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarMuseoActionPerformed(evt);
            }
        });

        jPanel1.setOpaque(false);

        lblLimiteNombreMuseo.setText("Limite Exedido");

        lblNombreMuseo.setFont(new java.awt.Font("Nirmala UI", 1, 18)); // NOI18N
        lblNombreMuseo.setText("Nombre : ");

        lblFundadorMuseo.setFont(new java.awt.Font("Nirmala UI", 1, 18)); // NOI18N
        lblFundadorMuseo.setText("Fundador : ");

        lblLimiteFundadorMuseo.setText("Limite Exedido");

        lblLimiteHistoriaMuseo.setText("Limite Exedido");

        txtFundadorMuseo.setEditable(false);
        txtFundadorMuseo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFundadorMuseoKeyTyped(evt);
            }
        });

        jLabel46.setFont(new java.awt.Font("Nirmala UI", 1, 18)); // NOI18N
        jLabel46.setText("Historia : ");

        txtNombreMuseo.setEditable(false);
        txtNombreMuseo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreMuseoKeyTyped(evt);
            }
        });

        JSpinner.DateEditor formato = new JSpinner.DateEditor(spnFecha, "yyyy-MM-dd");
        spnFecha.setEditor(formato);
        spnFecha.setEnabled(false);

        jLabel44.setFont(new java.awt.Font("Nirmala UI", 1, 18)); // NOI18N
        jLabel44.setText("Fecha de fundación : ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(143, 143, 143)
                .addComponent(lblLimiteHistoriaMuseo, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(394, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblLimiteNombreMuseo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblLimiteFundadorMuseo, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(53, 53, 53))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel44)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(spnFecha, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblFundadorMuseo)
                                .addComponent(lblNombreMuseo))
                            .addGap(24, 24, 24)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtNombreMuseo, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
                                .addComponent(txtFundadorMuseo, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)))
                        .addComponent(jLabel46))
                    .addContainerGap(133, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(lblLimiteNombreMuseo)
                .addGap(34, 34, 34)
                .addComponent(lblLimiteFundadorMuseo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 90, Short.MAX_VALUE)
                .addComponent(lblLimiteHistoriaMuseo)
                .addGap(24, 24, 24))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblNombreMuseo, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNombreMuseo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblFundadorMuseo, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtFundadorMuseo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(spnFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(11, 11, 11)
                    .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        btnVideoMuseo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/video.png"))); // NOI18N
        btnVideoMuseo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVideoMuseo.setEnabled(false);
        btnVideoMuseo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnVideoMuseoMouseClicked(evt);
            }
        });

        jlVideoMuseo.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jlVideoMuseo.setText("               Video");
        jlVideoMuseo.setEnabled(false);

        btnAudioMuseo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/audio.png"))); // NOI18N
        btnAudioMuseo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAudioMuseo.setEnabled(false);
        btnAudioMuseo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAudioMuseoMouseClicked(evt);
            }
        });

        jlAudioMuseo.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jlAudioMuseo.setText("               Audio");
        jlAudioMuseo.setEnabled(false);

        jdeskPrincipal.setLayer(imgMuseo1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskPrincipal.setLayer(imgMuseo2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskPrincipal.setLayer(jlGaleria, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskPrincipal.setLayer(jScrollPane2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskPrincipal.setLayer(imgMuseo3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskPrincipal.setLayer(imgMuseo4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskPrincipal.setLayer(imgMuseo5, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskPrincipal.setLayer(imgQrMuseo, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskPrincipal.setLayer(btnEditarMuseo, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskPrincipal.setLayer(btnCancelarMuseo, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskPrincipal.setLayer(jPanel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskPrincipal.setLayer(btnVideoMuseo, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskPrincipal.setLayer(jlVideoMuseo, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskPrincipal.setLayer(btnAudioMuseo, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskPrincipal.setLayer(jlAudioMuseo, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jdeskPrincipalLayout = new javax.swing.GroupLayout(jdeskPrincipal);
        jdeskPrincipal.setLayout(jdeskPrincipalLayout);
        jdeskPrincipalLayout.setHorizontalGroup(
            jdeskPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdeskPrincipalLayout.createSequentialGroup()
                .addGroup(jdeskPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jdeskPrincipalLayout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addGroup(jdeskPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jdeskPrincipalLayout.createSequentialGroup()
                                .addComponent(imgMuseo1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(imgMuseo2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addGroup(jdeskPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jdeskPrincipalLayout.createSequentialGroup()
                                        .addComponent(jlGaleria)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(jdeskPrincipalLayout.createSequentialGroup()
                                        .addComponent(imgMuseo3, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(29, 29, 29)
                                        .addComponent(imgMuseo4, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(29, 29, 29)
                                        .addComponent(imgMuseo5, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jdeskPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jdeskPrincipalLayout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                                                .addGroup(jdeskPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jlVideoMuseo, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGroup(jdeskPrincipalLayout.createSequentialGroup()
                                                        .addGap(34, 34, 34)
                                                        .addComponent(btnVideoMuseo)))
                                                .addGap(31, 31, 31)
                                                .addComponent(btnEditarMuseo, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jdeskPrincipalLayout.createSequentialGroup()
                                                .addGroup(jdeskPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jdeskPrincipalLayout.createSequentialGroup()
                                                        .addGap(10, 10, 10)
                                                        .addComponent(jlAudioMuseo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGap(31, 31, 31))
                                                    .addGroup(jdeskPrincipalLayout.createSequentialGroup()
                                                        .addGap(46, 46, 46)
                                                        .addComponent(btnAudioMuseo)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                                .addComponent(btnCancelarMuseo))))))
                            .addComponent(jScrollPane2)))
                    .addGroup(jdeskPrincipalLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(imgQrMuseo, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(86, 86, 86))
        );
        jdeskPrincipalLayout.setVerticalGroup(
            jdeskPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdeskPrincipalLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jdeskPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(imgQrMuseo, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jdeskPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdeskPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jdeskPrincipalLayout.createSequentialGroup()
                            .addGap(32, 32, 32)
                            .addComponent(jlGaleria)
                            .addGap(126, 126, 126))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdeskPrincipalLayout.createSequentialGroup()
                            .addGroup(jdeskPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jdeskPrincipalLayout.createSequentialGroup()
                                    .addComponent(btnEditarMuseo)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnCancelarMuseo))
                                .addGroup(jdeskPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(imgMuseo3, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(imgMuseo2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(imgMuseo4, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(imgMuseo5, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addContainerGap()))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdeskPrincipalLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jdeskPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jdeskPrincipalLayout.createSequentialGroup()
                                .addGap(0, 25, Short.MAX_VALUE)
                                .addComponent(imgMuseo1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jdeskPrincipalLayout.createSequentialGroup()
                                .addComponent(btnVideoMuseo, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jlVideoMuseo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAudioMuseo, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jlAudioMuseo)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );

        PanelRol.setBackground(new java.awt.Color(0, 0, 0));

        lblCerrarSesion5.setFont(new java.awt.Font("Sylfaen", 1, 14)); // NOI18N
        lblCerrarSesion5.setForeground(new java.awt.Color(255, 0, 0));
        lblCerrarSesion5.setText("[Cerrar Sesión]");
        lblCerrarSesion5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCerrarSesion5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCerrarSesion5MouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("MUSEO DE LA ESCUELA ISIDRO AYORA");

        javax.swing.GroupLayout PanelRolLayout = new javax.swing.GroupLayout(PanelRol);
        PanelRol.setLayout(PanelRolLayout);
        PanelRolLayout.setHorizontalGroup(
            PanelRolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelRolLayout.createSequentialGroup()
                .addGap(230, 230, 230)
                .addComponent(jLabel1)
                .addGap(207, 207, 207)
                .addComponent(lblCerrarSesion5)
                .addGap(87, 87, 87))
        );
        PanelRolLayout.setVerticalGroup(
            PanelRolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelRolLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(PanelRolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCerrarSesion5))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jpPrincipalLayout = new javax.swing.GroupLayout(jpPrincipal);
        jpPrincipal.setLayout(jpPrincipalLayout);
        jpPrincipalLayout.setHorizontalGroup(
            jpPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelDerechosMuseo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jpPrincipalLayout.createSequentialGroup()
                .addComponent(jdeskPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(PanelRol, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jpPrincipalLayout.setVerticalGroup(
            jpPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpPrincipalLayout.createSequentialGroup()
                .addComponent(PanelRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jdeskPrincipal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelDerechosMuseo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tabMenu.addTab("Museo \"ISIDRO AYORA\"", new javax.swing.ImageIcon(getClass().getResource("/images/home.png")), jpPrincipal); // NOI18N

        jpUsuarios.setBackground(new java.awt.Color(0, 0, 0));

        jdeskusuarios.setBackground(new java.awt.Color(204, 204, 255));

        jtUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jtUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtUsuariosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtUsuarios);

        btnNuevoUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/mas.jpg"))); // NOI18N
        btnNuevoUsuario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNuevoUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNuevoUsuarioMouseClicked(evt);
            }
        });

        lblNuevo.setText("Nuevo");

        btnActualizarUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/actualizar.png"))); // NOI18N
        btnActualizarUsuario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActualizarUsuario.setMaximumSize(new java.awt.Dimension(84, 81));
        btnActualizarUsuario.setMinimumSize(new java.awt.Dimension(84, 81));
        btnActualizarUsuario.setPreferredSize(new java.awt.Dimension(84, 81));
        btnActualizarUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnActualizarUsuarioMouseClicked(evt);
            }
        });

        btnEliminarUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Eliminar1.png"))); // NOI18N
        btnEliminarUsuario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminarUsuario.setMaximumSize(new java.awt.Dimension(84, 81));
        btnEliminarUsuario.setMinimumSize(new java.awt.Dimension(84, 81));
        btnEliminarUsuario.setPreferredSize(new java.awt.Dimension(84, 81));
        btnEliminarUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEliminarUsuarioMouseClicked(evt);
            }
        });

        btnBuscarUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search.png"))); // NOI18N
        btnBuscarUsuarios.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscarUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBuscarUsuariosMouseClicked(evt);
            }
        });

        jcbBuscarPor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Seleccione una opcion--", "Cedula de Usuario", "Nombre de Usuario", "Apellido de Usuario", "Tipo de Usuario", "Estado de Usuario" }));
        jcbBuscarPor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbBuscarPorItemStateChanged(evt);
            }
        });
        jcbBuscarPor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jcbBuscarPorMouseClicked(evt);
            }
        });

        txtBuscarPor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarPorKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarPorKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscarPorKeyTyped(evt);
            }
        });

        rbtnActivo.setText("Activo");
        rbtnActivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnActivoActionPerformed(evt);
            }
        });

        rbtnInactivo.setText("Inactivo");
        rbtnInactivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnInactivoActionPerformed(evt);
            }
        });

        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setText("# USUARIOS : ");

        lblTotalUsuarios.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalUsuarios.setText("0");
        lblTotalUsuarios.setName("lblTotalUsuarios"); // NOI18N

        lblNuevoUsuario.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblNuevoUsuario.setText("Nuevo");

        lblActualizarUsuario.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblActualizarUsuario.setText("Actualizar");

        lblEliminarUsuario.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblEliminarUsuario.setText("Eliminar");

        lblBuscarUsuario.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblBuscarUsuario.setText("Buscar");

        btnImprimirUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/imprimir.png"))); // NOI18N
        btnImprimirUsuarios.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnImprimirUsuarios.setMaximumSize(new java.awt.Dimension(84, 81));
        btnImprimirUsuarios.setMinimumSize(new java.awt.Dimension(84, 81));
        btnImprimirUsuarios.setPreferredSize(new java.awt.Dimension(84, 81));
        btnImprimirUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnImprimirUsuariosMouseClicked(evt);
            }
        });

        jdeskusuarios.setLayer(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(btnNuevoUsuario, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(lblNuevo, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(btnActualizarUsuario, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(btnEliminarUsuario, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(btnBuscarUsuarios, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(jcbBuscarPor, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(txtBuscarPor, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(rbtnActivo, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(rbtnInactivo, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(jLabel39, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(lblTotalUsuarios, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(lblNuevoUsuario, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(lblActualizarUsuario, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(lblEliminarUsuario, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(lblBuscarUsuario, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(btnImprimirUsuarios, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jdeskusuariosLayout = new javax.swing.GroupLayout(jdeskusuarios);
        jdeskusuarios.setLayout(jdeskusuariosLayout);
        jdeskusuariosLayout.setHorizontalGroup(
            jdeskusuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdeskusuariosLayout.createSequentialGroup()
                .addGroup(jdeskusuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jdeskusuariosLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jdeskusuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdeskusuariosLayout.createSequentialGroup()
                                .addComponent(jLabel39)
                                .addGap(127, 127, 127)
                                .addComponent(lblTotalUsuarios))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdeskusuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 987, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jdeskusuariosLayout.createSequentialGroup()
                                    .addGap(9, 9, 9)
                                    .addGroup(jdeskusuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblNuevo)
                                        .addGroup(jdeskusuariosLayout.createSequentialGroup()
                                            .addComponent(btnNuevoUsuario)
                                            .addGap(25, 25, 25)
                                            .addComponent(btnActualizarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(25, 25, 25)
                                            .addComponent(btnEliminarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(25, 25, 25)
                                            .addComponent(btnBuscarUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(25, 25, 25)
                                            .addGroup(jdeskusuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(txtBuscarPor, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(jdeskusuariosLayout.createSequentialGroup()
                                                    .addComponent(jcbBuscarPor, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(rbtnActivo)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(rbtnInactivo)))))))
                            .addComponent(btnImprimirUsuarios, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jdeskusuariosLayout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(lblNuevoUsuario)
                        .addGap(73, 73, 73)
                        .addComponent(lblActualizarUsuario)
                        .addGap(57, 57, 57)
                        .addComponent(lblEliminarUsuario)
                        .addGap(68, 68, 68)
                        .addComponent(lblBuscarUsuario)))
                .addContainerGap(60, Short.MAX_VALUE))
        );
        jdeskusuariosLayout.setVerticalGroup(
            jdeskusuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdeskusuariosLayout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addComponent(lblNuevo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jdeskusuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jdeskusuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnNuevoUsuario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnActualizarUsuario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEliminarUsuario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnBuscarUsuarios, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jdeskusuariosLayout.createSequentialGroup()
                        .addGroup(jdeskusuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jcbBuscarPor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdeskusuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(rbtnActivo)
                                .addComponent(rbtnInactivo)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBuscarPor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jdeskusuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNuevoUsuario)
                    .addComponent(lblActualizarUsuario)
                    .addComponent(lblEliminarUsuario)
                    .addComponent(lblBuscarUsuario))
                .addGap(4, 4, 4)
                .addGroup(jdeskusuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39)
                    .addComponent(lblTotalUsuarios))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnImprimirUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );

        PanelRolUsuarios.setBackground(new java.awt.Color(0, 0, 0));

        lblCerrarSesion6.setFont(new java.awt.Font("Sylfaen", 1, 14)); // NOI18N
        lblCerrarSesion6.setForeground(new java.awt.Color(255, 0, 0));
        lblCerrarSesion6.setText("[Cerrar Sesión]");
        lblCerrarSesion6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCerrarSesion6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCerrarSesion6MouseClicked(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("USUARIOS");

        javax.swing.GroupLayout PanelRolUsuariosLayout = new javax.swing.GroupLayout(PanelRolUsuarios);
        PanelRolUsuarios.setLayout(PanelRolUsuariosLayout);
        PanelRolUsuariosLayout.setHorizontalGroup(
            PanelRolUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelRolUsuariosLayout.createSequentialGroup()
                .addGap(400, 400, 400)
                .addComponent(jLabel2)
                .addGap(377, 377, 377)
                .addComponent(lblCerrarSesion6)
                .addGap(87, 87, 87))
        );
        PanelRolUsuariosLayout.setVerticalGroup(
            PanelRolUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelRolUsuariosLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(PanelRolUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCerrarSesion6))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(0, 0, 0));

        jlJJ2016Usuarios.setForeground(new java.awt.Color(255, 255, 255));
        jlJJ2016Usuarios.setText("JJ 2016 Reservados  todos  los  derechos . ");
        jlJJ2016Usuarios.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jlJJ2016Usuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlJJ2016UsuariosMouseClicked(evt);
            }
        });

        lblTerminosyCondicionesUsuarios.setForeground(new java.awt.Color(255, 51, 0));
        lblTerminosyCondicionesUsuarios.setText("Terminos y Condiciones");
        lblTerminosyCondicionesUsuarios.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblTerminosyCondicionesUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTerminosyCondicionesUsuariosMouseClicked(evt);
            }
        });

        lblPoliticasdePrivacidadUsuarios.setForeground(new java.awt.Color(153, 204, 255));
        lblPoliticasdePrivacidadUsuarios.setText("|  Políticas de Privacidad");
        lblPoliticasdePrivacidadUsuarios.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblPoliticasdePrivacidadUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPoliticasdePrivacidadUsuariosMouseClicked(evt);
            }
        });

        lblUsuarioyRolUsuarios.setForeground(new java.awt.Color(255, 0, 0));
        lblUsuarioyRolUsuarios.setText("jLabel1");
        lblUsuarioyRolUsuarios.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(jlJJ2016Usuarios)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTerminosyCondicionesUsuarios)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblPoliticasdePrivacidadUsuarios)
                .addGap(132, 132, 132)
                .addComponent(lblUsuarioyRolUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblUsuarioyRolUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jlJJ2016Usuarios)
                        .addComponent(lblTerminosyCondicionesUsuarios)
                        .addComponent(lblPoliticasdePrivacidadUsuarios)))
                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout jpUsuariosLayout = new javax.swing.GroupLayout(jpUsuarios);
        jpUsuarios.setLayout(jpUsuariosLayout);
        jpUsuariosLayout.setHorizontalGroup(
            jpUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpUsuariosLayout.createSequentialGroup()
                .addComponent(jdeskusuarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(PanelRolUsuarios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jpUsuariosLayout.setVerticalGroup(
            jpUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpUsuariosLayout.createSequentialGroup()
                .addComponent(PanelRolUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jdeskusuarios)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tabMenu.addTab("         Usuarios      ", new javax.swing.ImageIcon(getClass().getResource("/images/usuarios.png")), jpUsuarios); // NOI18N

        jPanel3.setBackground(new java.awt.Color(34, 81, 122));

        jdeskGaleria.setBackground(new java.awt.Color(204, 204, 255));

        javax.swing.GroupLayout jdeskGaleriaLayout = new javax.swing.GroupLayout(jdeskGaleria);
        jdeskGaleria.setLayout(jdeskGaleriaLayout);
        jdeskGaleriaLayout.setHorizontalGroup(
            jdeskGaleriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 860, Short.MAX_VALUE)
        );
        jdeskGaleriaLayout.setVerticalGroup(
            jdeskGaleriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 551, Short.MAX_VALUE)
        );

        jPanel10.setBackground(new java.awt.Color(0, 0, 0));

        jlJJ2016Galeria.setForeground(new java.awt.Color(255, 255, 255));
        jlJJ2016Galeria.setText("JJ 2016 Reservados  todos  los  derechos . ");
        jlJJ2016Galeria.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jlJJ2016Galeria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlJJ2016GaleriaMouseClicked(evt);
            }
        });

        lblTerminosyCondicionesGaleria.setForeground(new java.awt.Color(255, 51, 0));
        lblTerminosyCondicionesGaleria.setText("Terminos y Condiciones");
        lblTerminosyCondicionesGaleria.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblTerminosyCondicionesGaleria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTerminosyCondicionesGaleriaMouseClicked(evt);
            }
        });

        lblPoliticasdePrivacidadGaleria.setForeground(new java.awt.Color(153, 204, 255));
        lblPoliticasdePrivacidadGaleria.setText("|  Políticas de Privacidad");
        lblPoliticasdePrivacidadGaleria.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblPoliticasdePrivacidadGaleria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPoliticasdePrivacidadGaleriaMouseClicked(evt);
            }
        });

        lblUsuarioyRolGaleria.setForeground(new java.awt.Color(255, 0, 0));
        lblUsuarioyRolGaleria.setText("jLabel1");
        lblUsuarioyRolGaleria.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(jlJJ2016Galeria)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTerminosyCondicionesGaleria)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblPoliticasdePrivacidadGaleria)
                .addGap(132, 132, 132)
                .addComponent(lblUsuarioyRolGaleria, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblUsuarioyRolGaleria, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jlJJ2016Galeria)
                        .addComponent(lblTerminosyCondicionesGaleria)
                        .addComponent(lblPoliticasdePrivacidadGaleria)))
                .addGap(25, 25, 25))
        );

        PanelRolUsuarios1.setBackground(new java.awt.Color(0, 0, 0));

        lblCerrarSesion7.setFont(new java.awt.Font("Sylfaen", 1, 14)); // NOI18N
        lblCerrarSesion7.setForeground(new java.awt.Color(255, 0, 0));
        lblCerrarSesion7.setText("[Cerrar Sesión]");
        lblCerrarSesion7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCerrarSesion7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCerrarSesion7MouseClicked(evt);
            }
        });

        lblTituloGaleria.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTituloGaleria.setForeground(new java.awt.Color(255, 255, 255));
        lblTituloGaleria.setText("GALERÍA");

        javax.swing.GroupLayout PanelRolUsuarios1Layout = new javax.swing.GroupLayout(PanelRolUsuarios1);
        PanelRolUsuarios1.setLayout(PanelRolUsuarios1Layout);
        PanelRolUsuarios1Layout.setHorizontalGroup(
            PanelRolUsuarios1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelRolUsuarios1Layout.createSequentialGroup()
                .addGap(421, 421, 421)
                .addComponent(lblTituloGaleria)
                .addGap(377, 377, 377)
                .addComponent(lblCerrarSesion7)
                .addGap(87, 87, 87))
        );
        PanelRolUsuarios1Layout.setVerticalGroup(
            PanelRolUsuarios1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelRolUsuarios1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(PanelRolUsuarios1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTituloGaleria, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCerrarSesion7))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        btnGestionArticulos.setBackground(new java.awt.Color(0, 0, 0));
        btnGestionArticulos.setForeground(new java.awt.Color(255, 255, 255));
        btnGestionArticulos.setText("Gestionar Artículo");
        btnGestionArticulos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGestionArticulosActionPerformed(evt);
            }
        });

        btnGestionCategoria.setBackground(new java.awt.Color(0, 0, 0));
        btnGestionCategoria.setForeground(new java.awt.Color(255, 255, 255));
        btnGestionCategoria.setText("     Gestionar Categoría");
        btnGestionCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGestionCategoriaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnGestionArticulos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGestionCategoria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jdeskGaleria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(PanelRolUsuarios1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(PanelRolUsuarios1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnGestionCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnGestionArticulos, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jdeskGaleria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tabMenu.addTab("       Galería", new javax.swing.ImageIcon(getClass().getResource("/images/galeria.png")), jPanel3); // NOI18N

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));

        jdeskContactanos.setBackground(new java.awt.Color(204, 204, 255));
        jdeskContactanos.setMaximumSize(new java.awt.Dimension(1063, 530));

        jtHistorialVisita.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jtHistorialVisita.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtHistorialVisitaMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jtHistorialVisita);

        jLabel3.setText("Total Visitas");

        txtTotalVisitas.setEditable(false);
        txtTotalVisitas.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalVisitas.setText("0");

        btnImprimirVisitantes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/imprimir.png"))); // NOI18N
        btnImprimirVisitantes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnImprimirVisitantes.setMaximumSize(new java.awt.Dimension(84, 81));
        btnImprimirVisitantes.setMinimumSize(new java.awt.Dimension(84, 81));
        btnImprimirVisitantes.setPreferredSize(new java.awt.Dimension(84, 81));
        btnImprimirVisitantes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnImprimirVisitantesMouseClicked(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel2.setOpaque(false);

        jLabel56.setText("Visitante");

        txtnombreVisitante.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtnombreVisitanteKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtnombreVisitanteKeyTyped(evt);
            }
        });

        btnNuevoVisitante.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/mas.jpg"))); // NOI18N
        btnNuevoVisitante.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNuevoVisitante.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNuevoVisitanteMouseClicked(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setText("Registro de Visitante");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtnombreVisitante, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnNuevoVisitante, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(79, 79, 79))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnNuevoVisitante, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel56)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtnombreVisitante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(39, 39, 39))
        );

        jPanel6.setBackground(new java.awt.Color(0, 0, 0));
        jPanel6.setForeground(new java.awt.Color(255, 255, 255));
        jPanel6.setOpaque(false);

        jLabel57.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel57.setText("Desde : ");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("Buscar por:");

        lblBuscarHistorialVisita.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search.png"))); // NOI18N
        lblBuscarHistorialVisita.setText("jLabel3");
        lblBuscarHistorialVisita.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblBuscarHistorialVisita.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblBuscarHistorialVisitaMouseClicked(evt);
            }
        });

        jrbFiltroVisitante.setText("Visitante");
        jrbFiltroVisitante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbFiltroVisitanteActionPerformed(evt);
            }
        });

        jLabel59.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel59.setText("Hasta :");

        jrbFiltroDispositivo.setText("Dispositivo");
        jrbFiltroDispositivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbFiltroDispositivoActionPerformed(evt);
            }
        });

        jrbFiltroTodo.setText("Todo");
        jrbFiltroTodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbFiltroTodoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jrbFiltroVisitante)
                            .addComponent(jrbFiltroDispositivo)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addComponent(jrbFiltroTodo)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jdtHistHasta, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jdtHistDesde, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(lblBuscarHistorialVisita, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(67, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jrbFiltroTodo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jrbFiltroVisitante)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jrbFiltroDispositivo))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel57))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jdtHistDesde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(11, 11, 11)
                                .addComponent(jLabel59)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                                .addComponent(jdtHistHasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(lblBuscarHistorialVisita, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        jdeskContactanos.setLayer(jScrollPane3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(jLabel3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(txtTotalVisitas, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(btnImprimirVisitantes, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(jPanel2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(jPanel6, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jdeskContactanosLayout = new javax.swing.GroupLayout(jdeskContactanos);
        jdeskContactanos.setLayout(jdeskContactanosLayout);
        jdeskContactanosLayout.setHorizontalGroup(
            jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdeskContactanosLayout.createSequentialGroup()
                .addGroup(jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jdeskContactanosLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 462, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jdeskContactanosLayout.createSequentialGroup()
                        .addGap(488, 488, 488)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotalVisitas, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(57, 57, 57)
                        .addComponent(btnImprimirVisitantes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jdeskContactanosLayout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 822, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(334, Short.MAX_VALUE))
        );
        jdeskContactanosLayout.setVerticalGroup(
            jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdeskContactanosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnImprimirVisitantes, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTotalVisitas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)))
                .addGap(33, 33, 33))
        );

        jPanel7.setBackground(new java.awt.Color(0, 0, 0));

        jlJJ2016Historial.setForeground(new java.awt.Color(255, 255, 255));
        jlJJ2016Historial.setText("JJ 2016 Reservados  todos  los  derechos . ");
        jlJJ2016Historial.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jlJJ2016Historial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlJJ2016HistorialMouseClicked(evt);
            }
        });

        lblTerminosyCondicionesContactanos.setForeground(new java.awt.Color(255, 51, 0));
        lblTerminosyCondicionesContactanos.setText("Terminos y Condiciones");
        lblTerminosyCondicionesContactanos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblTerminosyCondicionesContactanos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTerminosyCondicionesContactanosMouseClicked(evt);
            }
        });

        lblPoliticasdePrivacidadContactanos.setForeground(new java.awt.Color(153, 204, 255));
        lblPoliticasdePrivacidadContactanos.setText("|  Políticas de Privacidad");
        lblPoliticasdePrivacidadContactanos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblPoliticasdePrivacidadContactanos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPoliticasdePrivacidadContactanosMouseClicked(evt);
            }
        });

        lblUsuarioyRolContactanos.setForeground(new java.awt.Color(255, 0, 0));
        lblUsuarioyRolContactanos.setText("jLabel1");
        lblUsuarioyRolContactanos.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(jlJJ2016Historial)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTerminosyCondicionesContactanos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblPoliticasdePrivacidadContactanos)
                .addGap(132, 132, 132)
                .addComponent(lblUsuarioyRolContactanos, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblUsuarioyRolContactanos, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jlJJ2016Historial)
                        .addComponent(lblTerminosyCondicionesContactanos)
                        .addComponent(lblPoliticasdePrivacidadContactanos)))
                .addGap(25, 25, 25))
        );

        PanelRolUsuarios3.setBackground(new java.awt.Color(0, 0, 0));

        lblCerrarSesion9.setFont(new java.awt.Font("Sylfaen", 1, 14)); // NOI18N
        lblCerrarSesion9.setForeground(new java.awt.Color(255, 0, 0));
        lblCerrarSesion9.setText("[Cerrar Sesión]");
        lblCerrarSesion9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCerrarSesion9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCerrarSesion9MouseClicked(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("HISTORIAL DE VISITAS");

        javax.swing.GroupLayout PanelRolUsuarios3Layout = new javax.swing.GroupLayout(PanelRolUsuarios3);
        PanelRolUsuarios3.setLayout(PanelRolUsuarios3Layout);
        PanelRolUsuarios3Layout.setHorizontalGroup(
            PanelRolUsuarios3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelRolUsuarios3Layout.createSequentialGroup()
                .addGap(300, 300, 300)
                .addComponent(jLabel5)
                .addGap(321, 321, 321)
                .addComponent(lblCerrarSesion9)
                .addGap(87, 87, 87))
        );
        PanelRolUsuarios3Layout.setVerticalGroup(
            PanelRolUsuarios3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelRolUsuarios3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(PanelRolUsuarios3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCerrarSesion9))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(PanelRolUsuarios3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jdeskContactanos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(PanelRolUsuarios3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jdeskContactanos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tabMenu.addTab("        Historial de Visita", new javax.swing.ImageIcon(getClass().getResource("/images/visitas.png")), jPanel4); // NOI18N

        jPanel5.setBackground(new java.awt.Color(0, 0, 0));

        jdeskAcercade.setBackground(new java.awt.Color(204, 204, 255));

        jPanelEstadisticaVisitaImprimir.setOpaque(false);

        jcbAñoGraficoEstadistico.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2016", "2017" }));
        jcbAñoGraficoEstadistico.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbAñoGraficoEstadisticoItemStateChanged(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("Gráficos Estadísticos : ");

        btnVerEstadisticaAnual.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search.png"))); // NOI18N
        btnVerEstadisticaAnual.setText("jLabel3");
        btnVerEstadisticaAnual.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVerEstadisticaAnual.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnVerEstadisticaAnualMouseClicked(evt);
            }
        });

        panelGraficoTorta.setPreferredSize(new java.awt.Dimension(480, 308));

        lblTorta.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTorta.setText("Seleccione un año para ver la Estadistica");
        lblTorta.setPreferredSize(new java.awt.Dimension(480, 308));

        javax.swing.GroupLayout panelGraficoTortaLayout = new javax.swing.GroupLayout(panelGraficoTorta);
        panelGraficoTorta.setLayout(panelGraficoTortaLayout);
        panelGraficoTortaLayout.setHorizontalGroup(
            panelGraficoTortaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTorta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelGraficoTortaLayout.setVerticalGroup(
            panelGraficoTortaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGraficoTortaLayout.createSequentialGroup()
                .addComponent(lblTorta, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                .addContainerGap())
        );

        lblTotalPastel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalPastel.setText("Total Usuarios Anuales:");
        lblTotalPastel.setFocusable(false);
        lblTotalPastel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        txtTotalVisitasPorAño.setEditable(false);
        txtTotalVisitasPorAño.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalVisitasPorAño.setText("0");

        btnImprimirEstadistica.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/imprimir.png"))); // NOI18N
        btnImprimirEstadistica.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnImprimirEstadistica.setMaximumSize(new java.awt.Dimension(84, 81));
        btnImprimirEstadistica.setMinimumSize(new java.awt.Dimension(84, 81));
        btnImprimirEstadistica.setPreferredSize(new java.awt.Dimension(84, 81));
        btnImprimirEstadistica.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnImprimirEstadisticaMouseClicked(evt);
            }
        });

        jrbEstadisticasVisitantes.setText("Visitantes");

        jrbEstadisticasDispositivos.setText("Dispositivo");

        javax.swing.GroupLayout jPanelEstadisticaVisitaImprimirLayout = new javax.swing.GroupLayout(jPanelEstadisticaVisitaImprimir);
        jPanelEstadisticaVisitaImprimir.setLayout(jPanelEstadisticaVisitaImprimirLayout);
        jPanelEstadisticaVisitaImprimirLayout.setHorizontalGroup(
            jPanelEstadisticaVisitaImprimirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEstadisticaVisitaImprimirLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelEstadisticaVisitaImprimirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelEstadisticaVisitaImprimirLayout.createSequentialGroup()
                        .addComponent(panelGraficoTorta, javax.swing.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanelEstadisticaVisitaImprimirLayout.createSequentialGroup()
                        .addComponent(jrbEstadisticasVisitantes)
                        .addGap(18, 18, 18)
                        .addComponent(jrbEstadisticasDispositivos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jcbAñoGraficoEstadistico, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnVerEstadisticaAnual, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40))
                    .addGroup(jPanelEstadisticaVisitaImprimirLayout.createSequentialGroup()
                        .addGroup(jPanelEstadisticaVisitaImprimirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelEstadisticaVisitaImprimirLayout.createSequentialGroup()
                                .addGap(0, 97, Short.MAX_VALUE)
                                .addComponent(lblTotalPastel, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtTotalVisitasPorAño, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)
                                .addComponent(btnImprimirEstadistica, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelEstadisticaVisitaImprimirLayout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        jPanelEstadisticaVisitaImprimirLayout.setVerticalGroup(
            jPanelEstadisticaVisitaImprimirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEstadisticaVisitaImprimirLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addGroup(jPanelEstadisticaVisitaImprimirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnVerEstadisticaAnual, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcbAñoGraficoEstadistico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jrbEstadisticasVisitantes)
                    .addComponent(jrbEstadisticasDispositivos))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelGraficoTorta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanelEstadisticaVisitaImprimirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelEstadisticaVisitaImprimirLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(jPanelEstadisticaVisitaImprimirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTotalVisitasPorAño, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTotalPastel)))
                    .addComponent(btnImprimirEstadistica, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanelEstadisticaDispositivoImprimir.setOpaque(false);

        btnImprimirEstadisticaDispositivos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/imprimir.png"))); // NOI18N
        btnImprimirEstadisticaDispositivos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnImprimirEstadisticaDispositivos.setMaximumSize(new java.awt.Dimension(84, 81));
        btnImprimirEstadisticaDispositivos.setMinimumSize(new java.awt.Dimension(84, 81));
        btnImprimirEstadisticaDispositivos.setPreferredSize(new java.awt.Dimension(84, 81));
        btnImprimirEstadisticaDispositivos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnImprimirEstadisticaDispositivosMouseClicked(evt);
            }
        });

        lblTotalPastelDispositivos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalPastelDispositivos.setText("Total Anual:");
        lblTotalPastelDispositivos.setFocusable(false);
        lblTotalPastelDispositivos.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        txtTotalVisitasVisitantesPorAñoComparativo.setEditable(false);
        txtTotalVisitasVisitantesPorAñoComparativo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalVisitasVisitantesPorAñoComparativo.setText("0");

        jcbAñoGraficoEstadisticoComparativo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2016", "2017" }));
        jcbAñoGraficoEstadisticoComparativo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbAñoGraficoEstadisticoComparativoItemStateChanged(evt);
            }
        });

        btnVerEstadisticaAnualDispositivos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search.png"))); // NOI18N
        btnVerEstadisticaAnualDispositivos.setText("jLabel3");
        btnVerEstadisticaAnualDispositivos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVerEstadisticaAnualDispositivos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnVerEstadisticaAnualDispositivosMouseClicked(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setText("Gráfico Estadístico Comparativo ");

        lblTortaDis.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTortaDis.setText("Seleccione un año para ver la Estadistica de los Dispositivos que se han conectado");
        lblTortaDis.setPreferredSize(new java.awt.Dimension(480, 308));

        javax.swing.GroupLayout panelGraficoTortaDisLayout = new javax.swing.GroupLayout(panelGraficoTortaDis);
        panelGraficoTortaDis.setLayout(panelGraficoTortaDisLayout);
        panelGraficoTortaDisLayout.setHorizontalGroup(
            panelGraficoTortaDisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTortaDis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelGraficoTortaDisLayout.setVerticalGroup(
            panelGraficoTortaDisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTortaDis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jLabel11.setText("Visitantes ");

        jLabel12.setText("Dispositivos");

        txtTotalVisitasDispositivosPorAñoCompartivo.setEditable(false);
        txtTotalVisitasDispositivosPorAñoCompartivo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalVisitasDispositivosPorAñoCompartivo.setText("0");

        javax.swing.GroupLayout jPanelEstadisticaDispositivoImprimirLayout = new javax.swing.GroupLayout(jPanelEstadisticaDispositivoImprimir);
        jPanelEstadisticaDispositivoImprimir.setLayout(jPanelEstadisticaDispositivoImprimirLayout);
        jPanelEstadisticaDispositivoImprimirLayout.setHorizontalGroup(
            jPanelEstadisticaDispositivoImprimirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelEstadisticaDispositivoImprimirLayout.createSequentialGroup()
                .addGroup(jPanelEstadisticaDispositivoImprimirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelEstadisticaDispositivoImprimirLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jcbAñoGraficoEstadisticoComparativo, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btnVerEstadisticaAnualDispositivos, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29))
                    .addGroup(jPanelEstadisticaDispositivoImprimirLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanelEstadisticaDispositivoImprimirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(panelGraficoTortaDis, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanelEstadisticaDispositivoImprimirLayout.createSequentialGroup()
                                .addGroup(jPanelEstadisticaDispositivoImprimirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanelEstadisticaDispositivoImprimirLayout.createSequentialGroup()
                                        .addComponent(lblTotalPastelDispositivos, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtTotalVisitasVisitantesPorAñoComparativo, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(26, 26, 26)
                                        .addComponent(txtTotalVisitasDispositivosPorAñoCompartivo, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanelEstadisticaDispositivoImprimirLayout.createSequentialGroup()
                                        .addGap(106, 106, 106)
                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(55, 55, 55)
                                        .addComponent(jLabel12)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnImprimirEstadisticaDispositivos, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(10, 10, 10))
            .addGroup(jPanelEstadisticaDispositivoImprimirLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelEstadisticaDispositivoImprimirLayout.setVerticalGroup(
            jPanelEstadisticaDispositivoImprimirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelEstadisticaDispositivoImprimirLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanelEstadisticaDispositivoImprimirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelEstadisticaDispositivoImprimirLayout.createSequentialGroup()
                        .addGroup(jPanelEstadisticaDispositivoImprimirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelEstadisticaDispositivoImprimirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTotalVisitasVisitantesPorAñoComparativo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTotalPastelDispositivos)
                            .addComponent(txtTotalVisitasDispositivosPorAñoCompartivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelEstadisticaDispositivoImprimirLayout.createSequentialGroup()
                        .addGroup(jPanelEstadisticaDispositivoImprimirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnVerEstadisticaAnualDispositivos, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcbAñoGraficoEstadisticoComparativo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelGraficoTortaDis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnImprimirEstadisticaDispositivos, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jdeskAcercade.setLayer(jPanelEstadisticaVisitaImprimir, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jPanelEstadisticaDispositivoImprimir, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jdeskAcercadeLayout = new javax.swing.GroupLayout(jdeskAcercade);
        jdeskAcercade.setLayout(jdeskAcercadeLayout);
        jdeskAcercadeLayout.setHorizontalGroup(
            jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdeskAcercadeLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jPanelEstadisticaVisitaImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jPanelEstadisticaDispositivoImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jdeskAcercadeLayout.setVerticalGroup(
            jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdeskAcercadeLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanelEstadisticaVisitaImprimir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelEstadisticaDispositivoImprimir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        PanelRolUsuarios8.setBackground(new java.awt.Color(0, 0, 0));

        lblCerrarSesionAcerca.setFont(new java.awt.Font("Sylfaen", 1, 14)); // NOI18N
        lblCerrarSesionAcerca.setForeground(new java.awt.Color(255, 0, 0));
        lblCerrarSesionAcerca.setText("[Cerrar Sesión]");
        lblCerrarSesionAcerca.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCerrarSesionAcerca.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCerrarSesionAcercaMouseClicked(evt);
            }
        });

        jLabel48.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(255, 255, 255));
        jLabel48.setText("ACERCA DE");

        javax.swing.GroupLayout PanelRolUsuarios8Layout = new javax.swing.GroupLayout(PanelRolUsuarios8);
        PanelRolUsuarios8.setLayout(PanelRolUsuarios8Layout);
        PanelRolUsuarios8Layout.setHorizontalGroup(
            PanelRolUsuarios8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelRolUsuarios8Layout.createSequentialGroup()
                .addGap(421, 421, 421)
                .addComponent(jLabel48)
                .addGap(349, 349, 349)
                .addComponent(lblCerrarSesionAcerca)
                .addGap(87, 87, 87))
        );
        PanelRolUsuarios8Layout.setVerticalGroup(
            PanelRolUsuarios8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelRolUsuarios8Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(PanelRolUsuarios8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCerrarSesionAcerca))
                .addGap(25, 25, 25))
        );

        jPanel8.setBackground(new java.awt.Color(0, 0, 0));

        jlJJ2016Acercade.setForeground(new java.awt.Color(255, 255, 255));
        jlJJ2016Acercade.setText("JJ 2016 Reservados  todos  los  derechos . ");
        jlJJ2016Acercade.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jlJJ2016Acercade.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlJJ2016AcercadeMouseClicked(evt);
            }
        });

        lblTerminosyCondicionesAcerca.setForeground(new java.awt.Color(255, 51, 0));
        lblTerminosyCondicionesAcerca.setText("Terminos y Condiciones");
        lblTerminosyCondicionesAcerca.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblTerminosyCondicionesAcerca.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTerminosyCondicionesAcercaMouseClicked(evt);
            }
        });

        lblPoliticasdePrivacidadacerca.setForeground(new java.awt.Color(153, 204, 255));
        lblPoliticasdePrivacidadacerca.setText("|  Políticas de Privacidad");
        lblPoliticasdePrivacidadacerca.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblPoliticasdePrivacidadacerca.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPoliticasdePrivacidadacercaMouseClicked(evt);
            }
        });

        lblUsuarioyRolAcerca.setForeground(new java.awt.Color(255, 0, 0));
        lblUsuarioyRolAcerca.setText("jLabel1");
        lblUsuarioyRolAcerca.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(jlJJ2016Acercade)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTerminosyCondicionesAcerca)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblPoliticasdePrivacidadacerca)
                .addGap(132, 132, 132)
                .addComponent(lblUsuarioyRolAcerca, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblUsuarioyRolAcerca, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jlJJ2016Acercade)
                        .addComponent(lblTerminosyCondicionesAcerca)
                        .addComponent(lblPoliticasdePrivacidadacerca)))
                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelRolUsuarios8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jdeskAcercade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(PanelRolUsuarios8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jdeskAcercade)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tabMenu.addTab("        Estadísticas", new javax.swing.ImageIcon(getClass().getResource("/images/estadisticas.png")), jPanel5); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 1348, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 719, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        tabMenu.getAccessibleContext().setAccessibleName("Historial de Visita");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblPoliticasdePrivacidadPrincipalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPoliticasdePrivacidadPrincipalMouseClicked
        internalPoliticas = new jifrPoliticasdePrivacidad();
        centrarVentanaInterna(jdeskPrincipal, internalPoliticas);
        internalContactanos.dispose();
        internalTerminos.dispose();
    }//GEN-LAST:event_lblPoliticasdePrivacidadPrincipalMouseClicked

    private void lblTerminosyCondicionesPrincipalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTerminosyCondicionesPrincipalMouseClicked
        internalTerminos = new jifrTerminosyCondiciones();
        centrarVentanaInterna(jdeskPrincipal, internalTerminos);
        internalContactanos.dispose();
        internalPoliticas.dispose();
    }//GEN-LAST:event_lblTerminosyCondicionesPrincipalMouseClicked

    private void lblTerminosyCondicionesContactanosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTerminosyCondicionesContactanosMouseClicked
        internalTerminos = new jifrTerminosyCondiciones();
        centrarVentanaInterna(jdeskContactanos, internalTerminos);
        internalContactanos.dispose();
        internalPoliticas.dispose();
    }//GEN-LAST:event_lblTerminosyCondicionesContactanosMouseClicked

    private void lblPoliticasdePrivacidadContactanosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPoliticasdePrivacidadContactanosMouseClicked
        internalPoliticas = new jifrPoliticasdePrivacidad();
        centrarVentanaInterna(jdeskContactanos, internalPoliticas);
         internalContactanos.dispose();
        internalTerminos.dispose();
    }//GEN-LAST:event_lblPoliticasdePrivacidadContactanosMouseClicked

    private void lblTerminosyCondicionesAcercaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTerminosyCondicionesAcercaMouseClicked
        internalTerminos = new jifrTerminosyCondiciones();
        centrarVentanaInterna(jdeskAcercade, internalTerminos);
        internalContactanos.dispose();
        internalPoliticas.dispose();
    }//GEN-LAST:event_lblTerminosyCondicionesAcercaMouseClicked

    private void lblPoliticasdePrivacidadacercaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPoliticasdePrivacidadacercaMouseClicked
        internalPoliticas = new jifrPoliticasdePrivacidad();
        centrarVentanaInterna(jdeskAcercade, internalPoliticas);
        internalContactanos.dispose();
        internalTerminos.dispose();
    }//GEN-LAST:event_lblPoliticasdePrivacidadacercaMouseClicked

    private void lblTerminosyCondicionesUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTerminosyCondicionesUsuariosMouseClicked
        internalTerminos = new jifrTerminosyCondiciones();
        centrarVentanaInterna(jdeskusuarios, internalTerminos);
        internalContactanos.dispose();
        internalPoliticas.dispose();
    }//GEN-LAST:event_lblTerminosyCondicionesUsuariosMouseClicked

    private void lblPoliticasdePrivacidadUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPoliticasdePrivacidadUsuariosMouseClicked
        internalPoliticas = new jifrPoliticasdePrivacidad();
        centrarVentanaInterna(jdeskusuarios, internalPoliticas);
         internalContactanos.dispose();
        internalTerminos.dispose();
    }//GEN-LAST:event_lblPoliticasdePrivacidadUsuariosMouseClicked

    private void lblTerminosyCondicionesGaleriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTerminosyCondicionesGaleriaMouseClicked
        internalTerminos = new jifrTerminosyCondiciones();
        centrarVentanaInterna(jdeskGaleria, internalTerminos);
        internalContactanos.dispose();
        internalPoliticas.dispose();
    }//GEN-LAST:event_lblTerminosyCondicionesGaleriaMouseClicked

    private void lblPoliticasdePrivacidadGaleriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPoliticasdePrivacidadGaleriaMouseClicked
        internalPoliticas = new jifrPoliticasdePrivacidad();
        centrarVentanaInterna(jdeskGaleria, internalPoliticas);
         internalContactanos.dispose();
        internalTerminos.dispose();
    }//GEN-LAST:event_lblPoliticasdePrivacidadGaleriaMouseClicked

    private void jtUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtUsuariosMouseClicked
        SeleccionarItemTablaU(evt);
    }//GEN-LAST:event_jtUsuariosMouseClicked

    private void jcbBuscarPorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbBuscarPorItemStateChanged
        // TODO add your handling code here:
        buscar = jcbBuscarPor.getSelectedIndex();
        rbtnActivo.setSelected(false);
        rbtnInactivo.setSelected(false);
        LlenarTablaUsuarios();
    }//GEN-LAST:event_jcbBuscarPorItemStateChanged

    private void jcbBuscarPorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jcbBuscarPorMouseClicked
        // TODO add your handling code here:
        txtBuscarPor.setVisible(true);
    }//GEN-LAST:event_jcbBuscarPorMouseClicked

    private void txtBuscarPorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarPorKeyTyped
        // TODO add your handling code here:
        if(buscar==1){
            char car=evt.getKeyChar();
            if((car<'0' || car>'9')) evt.consume();
            int limite  = 10;
            if (txtBuscarPor.getText().length()== limite) evt.consume();
        }
    }//GEN-LAST:event_txtBuscarPorKeyTyped

    private void btnNuevoUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNuevoUsuarioMouseClicked
        is.setAccionBoton("Guardar");
        internalNuevoUsuario =new jifrNuevoUsuario();
        centrarVentanaInterna(jdeskusuarios, internalNuevoUsuario);
    }//GEN-LAST:event_btnNuevoUsuarioMouseClicked

    private void btnEliminarUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarUsuarioMouseClicked
        // TODO add your handling code here:
        Object [] opciones={"Aceptar","Cancelar"};
        if(!id.isEmpty()){
            int eleccion=JOptionPane.showOptionDialog(null,"Está seguro que desea eliminar","Eliminar",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,opciones,"Aceptar");
            if(eleccion==JOptionPane.YES_OPTION) Eliminar();
        }else JOptionPane.showMessageDialog(this, "No ha seleccionado un registro a eliminar");
    }//GEN-LAST:event_btnEliminarUsuarioMouseClicked

    private void btnActualizarUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnActualizarUsuarioMouseClicked
        if(!id.isEmpty()){
            is.setAccionBoton("Actualizar ");
            is.setIdUsuario(id);
            is.setRol(rol);
            is.setEstado(estado);
            jtUsuarios.setFocusable(false);
            jtUsuarios.clearSelection();
            internalNuevoUsuario = new jifrNuevoUsuario();
            centrarVentanaInterna(jdeskusuarios, internalNuevoUsuario);
        }else JOptionPane.showMessageDialog(this, "No ha seleccionado un registro a modificar");
    }//GEN-LAST:event_btnActualizarUsuarioMouseClicked

    private void btnBuscarUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarUsuariosMouseClicked
        // TODO add your handling code here:
        jcbBuscarPor.setVisible(true);
        txtBuscarPor.setEnabled(true);
        txtBuscarPor.requestFocus();
        rbtnActivo.setEnabled(true);
        rbtnInactivo.setEnabled(true);
        //
    }//GEN-LAST:event_btnBuscarUsuariosMouseClicked

    private void rbtnActivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnActivoActionPerformed
        // TODO add your handling code here:
        rbtnInactivo.setSelected(false);
        BuscarPorEstadoUsuario();
        txtBuscarPor.setText("");
    }//GEN-LAST:event_rbtnActivoActionPerformed

    private void rbtnInactivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnInactivoActionPerformed
        // TODO add your handling code here:
        rbtnActivo.setSelected(false);
        BuscarPorEstadoUsuarioInactivo();
        txtBuscarPor.setText("");
    }//GEN-LAST:event_rbtnInactivoActionPerformed

    private void btnEditarMuseoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarMuseoActionPerformed
        EditarQr();
    }//GEN-LAST:event_btnEditarMuseoActionPerformed

    private void imgMuseo1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imgMuseo1MouseClicked
        CargarImagen(imgMuseo1, 0);
        imagen[0] = ValoresConstantes.DIRECTORIO_PRINCIPAL + "\\MUSEO ISIDRO AYORA";
    }//GEN-LAST:event_imgMuseo1MouseClicked

    private void imgMuseo2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imgMuseo2MouseClicked
        CargarImagen(imgMuseo2, 1);
        imagen[1] = ValoresConstantes.DIRECTORIO_PRINCIPAL + "\\MUSEO ISIDRO AYORA";
       }//GEN-LAST:event_imgMuseo2MouseClicked

    private void imgMuseo3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imgMuseo3MouseClicked
        CargarImagen(imgMuseo3, 2);
        imagen[2] = ValoresConstantes.DIRECTORIO_PRINCIPAL + "\\MUSEO ISIDRO AYORA";
    }//GEN-LAST:event_imgMuseo3MouseClicked

    private void imgMuseo4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imgMuseo4MouseClicked
        CargarImagen(imgMuseo4, 3);
        imagen[3] = ValoresConstantes.DIRECTORIO_PRINCIPAL + "\\MUSEO ISIDRO AYORA";
    }//GEN-LAST:event_imgMuseo4MouseClicked

    private void imgMuseo5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imgMuseo5MouseClicked
        CargarImagen(imgMuseo5, 4);
        imagen[4] = ValoresConstantes.DIRECTORIO_PRINCIPAL + "\\MUSEO ISIDRO AYORA";
    }//GEN-LAST:event_imgMuseo5MouseClicked

    private void txtBuscarPorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarPorKeyReleased
        if(txtBuscarPor.getText().length()<1) 
        {
        if(UsuarioIngresado.parametroR.contains("Administrador/a"))LlenarTablaUsuariosAdmin();
         
        else LlenarTablaUsuarios();
        }
    }//GEN-LAST:event_txtBuscarPorKeyReleased

    private void txtBuscarPorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarPorKeyPressed
        if(txtBuscarPor.getText().length()>0){
            switch (buscar) {
                case 1:
                    BuscarPorCedula();
                    break;
                case 2:
                    BuscarPorNombreUsuario();
                    break;
                case 3:
                    BuscarPorApellidoUsuario();
                    break;
                case 4:
                    BuscarPorTipoUsuario();
                    break;
                case 5:
                    BuscarPorEstadoUsuario();
                    break;
                default:
                    JOptionPane.showMessageDialog(this,"Debe seleccionar un tipo de búsqueda");
                    break;
            }       
        }
        else{
            if(txtBuscarPor.getText().length()<1) 
        {
        if(UsuarioIngresado.parametroR.contains("Administrador/a"))LlenarTablaUsuariosAdmin();
         
        else LlenarTablaUsuarios();
        }
        }
    }//GEN-LAST:event_txtBuscarPorKeyPressed

    private void btnCancelarMuseoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarMuseoActionPerformed
        Limpiar();
        Deshabilitar();
        btnEditarMuseo.setText("Editar");
    }//GEN-LAST:event_btnCancelarMuseoActionPerformed

    private void txtNombreMuseoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreMuseoKeyTyped
        int limite  = 50;
        if (txtNombreMuseo.getText().length()== limite){ 
            evt.consume();
            lblLimiteNombreMuseo.setVisible(true);
            lblLimiteNombreMuseo.setText("Limite Exedido");
        }
        else{
                lblLimiteNombreMuseo.setVisible(false);
        }
    }//GEN-LAST:event_txtNombreMuseoKeyTyped

    private void txtHistoriaMuseoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHistoriaMuseoKeyTyped
        int limite  = 4000;
        if (txtHistoriaMuseo.getText().length()== limite){ 
            evt.consume();
            lblLimiteHistoriaMuseo.setVisible(true);
            lblLimiteHistoriaMuseo.setText("Limite Exedido");
        }
        else{
                lblLimiteHistoriaMuseo.setVisible(false);
        }
    }//GEN-LAST:event_txtHistoriaMuseoKeyTyped

    private void txtFundadorMuseoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFundadorMuseoKeyTyped
        int limite  = 50;
        if (txtFundadorMuseo.getText().length()== limite){ 
            evt.consume();
            lblLimiteFundadorMuseo.setVisible(true);
            lblLimiteFundadorMuseo.setText("Limite Exedido");
        }
        else{
                lblLimiteFundadorMuseo.setVisible(false);
        }
    }//GEN-LAST:event_txtFundadorMuseoKeyTyped

    private void lblCerrarSesion5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrarSesion5MouseClicked
        cerrarSesion();
    }//GEN-LAST:event_lblCerrarSesion5MouseClicked

    private void lblCerrarSesion6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrarSesion6MouseClicked
        cerrarSesion();
    }//GEN-LAST:event_lblCerrarSesion6MouseClicked

    private void lblCerrarSesion7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrarSesion7MouseClicked
        cerrarSesion();
    }//GEN-LAST:event_lblCerrarSesion7MouseClicked

    private void lblCerrarSesion9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrarSesion9MouseClicked
        cerrarSesion();
    }//GEN-LAST:event_lblCerrarSesion9MouseClicked

    private void lblCerrarSesionAcercaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrarSesionAcercaMouseClicked
        cerrarSesion();
    }//GEN-LAST:event_lblCerrarSesionAcercaMouseClicked

    private void jlJJ2016HistorialMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlJJ2016HistorialMouseClicked
        internalContactanos = new jifrContactanos();
        centrarVentanaInterna(jdeskContactanos, internalContactanos); 
        internalPoliticas.dispose();
        internalTerminos.dispose();
    }//GEN-LAST:event_jlJJ2016HistorialMouseClicked

    private void jlJJ2016PrincipalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlJJ2016PrincipalMouseClicked
        internalContactanos = new jifrContactanos();
        centrarVentanaInterna(jdeskPrincipal, internalContactanos);
        internalPoliticas.dispose();
        internalTerminos.dispose();
    }//GEN-LAST:event_jlJJ2016PrincipalMouseClicked

    private void jlJJ2016UsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlJJ2016UsuariosMouseClicked
        internalContactanos = new jifrContactanos();
        centrarVentanaInterna(jdeskusuarios, internalContactanos); 
        internalPoliticas.dispose();
        internalTerminos.dispose();
    }//GEN-LAST:event_jlJJ2016UsuariosMouseClicked

    private void jlJJ2016GaleriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlJJ2016GaleriaMouseClicked
        internalContactanos = new jifrContactanos();
        centrarVentanaInterna(jdeskGaleria, internalContactanos); 
        internalPoliticas.dispose();
        internalTerminos.dispose();
    }//GEN-LAST:event_jlJJ2016GaleriaMouseClicked

    private void jlJJ2016AcercadeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlJJ2016AcercadeMouseClicked
        internalContactanos = new jifrContactanos();
        centrarVentanaInterna(jdeskAcercade, internalContactanos); 
        internalPoliticas.dispose();
        internalTerminos.dispose();
    }//GEN-LAST:event_jlJJ2016AcercadeMouseClicked

    private void lblBuscarHistorialVisitaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBuscarHistorialVisitaMouseClicked
    //t.stop();
    
    //Cuando se selecciona el boton buscar y no hay fechas
    if(jrbFiltroTodo.isSelected() && jdtHistHasta.getDate() == null && jdtHistDesde.getDate() == null)
    {
        JOptionPane.showMessageDialog(null,"Escoja una fecha");
        }
    //Cuando se selecciona la opcion todo y no hay fecha final
    else if(jrbFiltroTodo.isSelected() && jdtHistHasta.getDate() == null) {
        jtHistorialVisita.setModel(LlenarTablaHistorialBusquedaUnidoDesde());
        txtTotalVisitas.setText(contarTotalUnidoporFechaDesde()); 
    } 
    //Cuando se selecciona la opcion todo y no hay fecha inicial
    else if(jrbFiltroTodo.isSelected() && jdtHistDesde.getDate() == null){
        JOptionPane.showMessageDialog(null,"Escoja una fecha inicial");
    }  
    //Cuando selecciona la opcion todo y fecha de inicion y final
    else if(jrbFiltroTodo.isSelected() && jdtHistHasta.getDate() != null && jdtHistDesde.getDate() != null){
        jtHistorialVisita.setModel(LlenarTablaHistorialBusquedaUnido());
        txtTotalVisitas.setText(contarTotalUnidoporFechaHasta());
    }
    
    //Cuando se selecciona la opcion de visitante y no hay fecha final
    if(jrbFiltroVisitante.isSelected() && jdtHistHasta.getDate() == null) {
        BuscarPorFechaVisita();
        txtTotalVisitas.setText(contarTotalVisitasporFecha());
    } 
    //Cuando se selecciona la opcion visitante y no hay fecha inicial
    else if(jrbFiltroVisitante.isSelected() && jdtHistDesde.getDate() == null) JOptionPane.showMessageDialog(null,"Escoja una fecha inicial");  
    //Cuando selecciona la opcion visitante y fecha de inicion y final
    else if(jrbFiltroVisitante.isSelected() && jdtHistHasta.getDate() != null && jdtHistDesde.getDate() != null){
        BuscarPorFechaVisitaDesdeHasta();
        txtTotalVisitas.setText(contarTotalVisitasporFechaHasta());
    }
    
    //Cuando selecciona la opcion dispositivo y no hay fecha final
    if(jrbFiltroDispositivo.isSelected() && jdtHistHasta.getDate() == null){
        BuscarPorFechaVisitaDispositivo();
        txtTotalVisitas.setText(contarTotalVisitasporFechadeDispositivo());
    }
    //Cuando se selecciona la opcion dispositivo y no hay fecha inicial
    else if(jrbFiltroDispositivo.isSelected() && jdtHistDesde.getDate() == null) JOptionPane.showMessageDialog(null,"Escoja una fecha inicial");  
    //Cuando selecciona la opcion dipositivo y fecha de inicion y final
    else if(jrbFiltroDispositivo.isSelected() && jdtHistHasta.getDate() != null && jdtHistDesde.getDate() != null) {
        BuscarPorFechaVisitaDispositivoDesdeHasta();
        txtTotalVisitas.setText(contarTotalVisitasDispositivosporFechaHasta());
    }
    }//GEN-LAST:event_lblBuscarHistorialVisitaMouseClicked

    private void btnImprimirUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnImprimirUsuariosMouseClicked
      if(UsuarioIngresado.parametroR.contains("Administrador/a")) {
            String a;
            try{
            a=JOptionPane.showInputDialog("Ingrese la contraseña");
            String SQL = "SELECT * FROM usuarios WHERE CONTRASENAUSUARIO ='"+a+"' AND TIPOUSUARIO = 'Administrador/a'"; 
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
                if(rs.next()){
                    ItemSeleccionado.accionBoton="Administrador";
                    ImprimirUsuarios impU=new ImprimirUsuarios();
                    impU.setVisible(true);
                }else
                {
                    JOptionPane.showMessageDialog(null, "Contraseña incorrecta");
                }
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Contraseña incorrecta");
                this.dispose();
            }
        }else{
            ItemSeleccionado.accionBoton="Usuario";
            ImprimirUsuarios impU=new ImprimirUsuarios();
            impU.setVisible(true);
        }
            
        
        
    }//GEN-LAST:event_btnImprimirUsuariosMouseClicked

    private void btnNuevoVisitanteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNuevoVisitanteMouseClicked
        if (txtnombreVisitante.getText().isEmpty()) JOptionPane.showMessageDialog(this,"Ingrese datos del visitante");
        else GuardarVisitante(); 
        
        
    }//GEN-LAST:event_btnNuevoVisitanteMouseClicked

    private void btnImprimirVisitantesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnImprimirVisitantesMouseClicked
        if(jdtHistDesde.getDate() != null){
            a = jdtHistDesde.getDate().getYear() + 1900;
            m = jdtHistDesde.getDate().getMonth() + 1;
            d = jdtHistDesde.getDate().getDate();
            String mm,dm,fq;
            if(m<10) mm = "0" + m;
            else mm = String.valueOf(m);
            if(d<10) dm = "0" + d;
            else dm = String.valueOf(d);
            fq = a + "-" + mm + "-" + dm;
            cal=fq;
        }
        if (jdtHistHasta.getDate() != null){
            a1 = jdtHistHasta.getDate().getYear() + 1900;
            m1 = jdtHistHasta.getDate().getMonth() + 1;
            d1 = jdtHistHasta.getDate().getDate();
            String mmh,dmh;
            if(m1<10) mmh = "0" + m1;
            else mmh = String.valueOf(m1);
            if(d1<10) dmh = "0" + d1;
            else dmh = String.valueOf(d1);
            ff = a1 + "-" + mmh + "-" + dmh;
            cal1=ff;
        }
        Object [] opciones={"Aceptar","Cancelar"};
        int eleccion=JOptionPane.showOptionDialog(null,"¿Desea imprimir la selección que escojio?","Mensaje de Confirmación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,null,opciones,"Aceptar");
        if(eleccion==JOptionPane.YES_OPTION){
            //Mensaje de impresion
            //Cuando se selecciona el boton buscar y no hay fechas
            if(jrbFiltroTodo.isSelected() && jdtHistHasta.getDate() == null && jdtHistDesde.getDate() == null)
            {
                imprimirOpcion=1;
                ItemSeleccionado.accionBoton =(String.valueOf(imprimirOpcion));
                ImprimirVisitantes iV = new ImprimirVisitantes();
                iV.setVisible(true);
            }
            //Cuando se selecciona la opcion todo y no hay fecha final
            else if(jrbFiltroTodo.isSelected() && jdtHistHasta.getDate() == null && jdtHistDesde.getDate() != null) {
                imprimirOpcion=2;
                ItemSeleccionado.accionBoton =(String.valueOf(imprimirOpcion));
                ItemSeleccionado.setFechaInicio(cal);
                ImprimirVisitantes iV = new ImprimirVisitantes();
                iV.setVisible(true);
                //jtHistorialVisita.setModel(LlenarTablaHistorialBusquedaUnidoDesde());
                //txtTotalVisitas.setText(contarTotalUnidoporFechaDesde()); 
            } 
            //Cuando se selecciona la opcion todo y no hay fecha inicial
            else if(jrbFiltroTodo.isSelected() && jdtHistDesde.getDate() == null){
                JOptionPane.showMessageDialog(null,"Escoja una fecha inicial");
                //imprimirOpcion=0;
            }  
            //Cuando selecciona la opcion todo y fecha de inicion y final
            else if(jrbFiltroTodo.isSelected() && jdtHistHasta.getDate() != null && jdtHistDesde.getDate() != null){
                imprimirOpcion=3;
                ItemSeleccionado.accionBoton =(String.valueOf(imprimirOpcion));
                ItemSeleccionado.setFechaInicio(cal);
                ItemSeleccionado.setFechaFinal(cal1);
                ImprimirVisitantes iV = new ImprimirVisitantes();
                iV.setVisible(true);
                //jtHistorialVisita.setModel(LlenarTablaHistorialBusquedaUnido());
                //txtTotalVisitas.setText(contarTotalUnidoporFechaHasta());
            }
            if(jrbFiltroVisitante.isSelected() && jdtHistHasta.getDate() == null && jdtHistDesde.getDate() == null)
            {
                imprimirOpcion=4;
                ItemSeleccionado.accionBoton =(String.valueOf(imprimirOpcion));
                ImprimirVisitantes iV = new ImprimirVisitantes();
                iV.setVisible(true);
            }
            //Cuando se selecciona la opcion de visitante y no hay fecha final
            else if(jrbFiltroVisitante.isSelected() && jdtHistHasta.getDate() == null && jdtHistDesde.getDate() != null) {
                imprimirOpcion=5;
                ItemSeleccionado.accionBoton =(String.valueOf(imprimirOpcion));
                ItemSeleccionado.setFechaInicio(cal);
                ImprimirVisitantes iV = new ImprimirVisitantes();
                iV.setVisible(true);
                //BuscarPorFechaVisita();
                //txtTotalVisitas.setText(contarTotalVisitasporFecha());
            } 
            //Cuando se selecciona la opcion visitante y no hay fecha inicial
            else if(jrbFiltroVisitante.isSelected() && jdtHistDesde.getDate() == null) 
                JOptionPane.showMessageDialog(null,"Escoja una fecha inicial");  
            //Cuando selecciona la opcion visitante y fecha de inicion y final
            else if(jrbFiltroVisitante.isSelected() && jdtHistHasta.getDate() != null && jdtHistDesde.getDate() != null){
                imprimirOpcion=6;
                ItemSeleccionado.accionBoton =(String.valueOf(imprimirOpcion));
                isV.setFechaInicio(cal);
                isV.setFechaFinal(cal1);
                ImprimirVisitantes iV = new ImprimirVisitantes();
                iV.setVisible(true);
            }
            
            if(jrbFiltroDispositivo.isSelected() && jdtHistHasta.getDate() == null && jdtHistDesde.getDate() == null)
            {
                imprimirOpcion=7;
                ItemSeleccionado.accionBoton =(String.valueOf(imprimirOpcion));
                ImprimirVisitantes iV = new ImprimirVisitantes();
                iV.setVisible(true);
            }
            //Cuando selecciona la opcion dispositivo y no hay fecha final
            else if(jrbFiltroDispositivo.isSelected() && jdtHistHasta.getDate() == null && jdtHistDesde.getDate() != null){
                imprimirOpcion=8;
                ItemSeleccionado.accionBoton =(String.valueOf(imprimirOpcion));
                ItemSeleccionado.setFechaInicio(cal);
                ImprimirVisitantes iV = new ImprimirVisitantes();
                iV.setVisible(true);
            }
            //Cuando se selecciona la opcion dispositivo y no hay fecha inicial
            else if(jrbFiltroDispositivo.isSelected() && jdtHistDesde.getDate() == null)
                JOptionPane.showMessageDialog(null,"Escoja una fecha inicial");  
            //Cuando selecciona la opcion dipositivo y fecha de inicion y final
            else if(jrbFiltroDispositivo.isSelected() && jdtHistHasta.getDate() != null && jdtHistDesde.getDate() != null) {
                imprimirOpcion=9;
                ItemSeleccionado.accionBoton =(String.valueOf(imprimirOpcion));
                ItemSeleccionado.setFechaInicio(cal);
                ItemSeleccionado.setFechaFinal(cal1);
                ImprimirVisitantes iV = new ImprimirVisitantes();
                iV.setVisible(true);
            }
        }

        
        
    }//GEN-LAST:event_btnImprimirVisitantesMouseClicked

    private void jtHistorialVisitaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtHistorialVisitaMouseClicked
        SeleccionarItemTablaVisitantes(evt);
         t.stop();        
    }//GEN-LAST:event_jtHistorialVisitaMouseClicked

    private void txtnombreVisitanteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnombreVisitanteKeyPressed
        LlenarTablaHistorialVisita();
    }//GEN-LAST:event_txtnombreVisitanteKeyPressed

    private void txtnombreVisitanteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnombreVisitanteKeyTyped
        int k = (int) evt.getKeyChar();
        if (k > 47 && k < 58) {
            evt.setKeyChar((char) evt.VK_CLEAR);
        }
    }//GEN-LAST:event_txtnombreVisitanteKeyTyped

    private void btnGestionArticulosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGestionArticulosActionPerformed
        internalGestionArticulo = new jifrGestionArticulos();
        centrarVentanaGestionCA(internalGestionArticulo);
    }//GEN-LAST:event_btnGestionArticulosActionPerformed

    private void btnGestionCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGestionCategoriaActionPerformed
        internalGestionCategoria = new jifrGestionCategoria();
        centrarVentanaGestionCA(internalGestionCategoria);
    }//GEN-LAST:event_btnGestionCategoriaActionPerformed

    private void jcbAñoGraficoEstadisticoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbAñoGraficoEstadisticoItemStateChanged
     idAnio = anios.get(jcbAñoGraficoEstadistico.getSelectedIndex()).getIdHistorialVisita();

    }//GEN-LAST:event_jcbAñoGraficoEstadisticoItemStateChanged

    private void btnVerEstadisticaAnualMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVerEstadisticaAnualMouseClicked
        if(jrbEstadisticasVisitantes.isSelected()) EstadisticaVisitantes();
        if(jrbEstadisticasDispositivos.isSelected())  EstadisticasDispositivos();
        if(!jrbEstadisticasDispositivos.isSelected() && !jrbEstadisticasVisitantes.isSelected()) JOptionPane.showMessageDialog(this,"Escoja un item de busqueda");
    }//GEN-LAST:event_btnVerEstadisticaAnualMouseClicked

    private void btnImprimirEstadisticaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnImprimirEstadisticaMouseClicked
        /*opimG=0;
        //btnImprimirEstadistica.setVisible(false);
        //Crea y devuelve un printerjob que se asocia con la impresora predeterminada
            //del sistema, si no hay, retorna NULL
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            //Se pasa la instancia del JFrame al PrinterJob
            printerJob.setPrintable(this);
            //muestra ventana de dialogo para imprimir
            if ( printerJob.printDialog())
            {
                try {
                    printerJob.print();
                    //btnImprimirEstadistica.setVisible(true);
                } catch (PrinterException ex) {
                System.out.println("Error:" + ex);
                }
            }*/    
        ItemSeleccionado.accionBoton = "E";
        if(jrbEstadisticasVisitantes.isSelected()) ItemSeleccionado.rol = "Vis";
        else ItemSeleccionado.rol = "Dis";
        ImprimirGraficosEstadisticos iGE = new ImprimirGraficosEstadisticos();
        iGE.setVisible(true);
    }//GEN-LAST:event_btnImprimirEstadisticaMouseClicked

    private void btnImprimirEstadisticaDispositivosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnImprimirEstadisticaDispositivosMouseClicked
        /*opimG=1;
        //btnImprimirEstadisticaDispositivos.setVisible(false);
        //Crea y devuelve un printerjob que se asocia con la impresora predeterminada
            //del sistema, si no hay, retorna NULL
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            //Se pasa la instancia del JFrame al PrinterJob
            printerJob.setPrintable(this);
            //muestra ventana de dialogo para imprimir
            if ( printerJob.printDialog())
            {
                try {
                    printerJob.print();
                    ///btnImprimirEstadisticaDispositivos.setVisible(true);
                } catch (PrinterException ex) {
                System.out.println("Error:" + ex);
                }
            }*/
        ItemSeleccionado.accionBoton = "EC";
        ImprimirGraficosEstadisticos iGE = new ImprimirGraficosEstadisticos();
        iGE.setVisible(true);
    }//GEN-LAST:event_btnImprimirEstadisticaDispositivosMouseClicked

    private void btnVideoMuseoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVideoMuseoMouseClicked
        CargarVideo(jlVideoMuseo, 1);
        video = ValoresConstantes.DIRECTORIO_PRINCIPAL+ "\\MUSEO ISIDRO AYORA";
    }//GEN-LAST:event_btnVideoMuseoMouseClicked

    private void btnAudioMuseoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAudioMuseoMouseClicked
        CargarAudio(jlAudioMuseo, 0);
        audio = ValoresConstantes.DIRECTORIO_PRINCIPAL + "\\MUSEO ISIDRO AYORA";
    }//GEN-LAST:event_btnAudioMuseoMouseClicked

    private void jrbFiltroVisitanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbFiltroVisitanteActionPerformed
        jrbFiltroDispositivo.setSelected(false);
        jtHistorialVisita.setModel(LlenarTablaHistorialVisita());  
        txtTotalVisitas.setText(contarTotalVisitas());     
        jdtHistDesde.setCalendar(null);
        jdtHistHasta.setCalendar(null);
    }//GEN-LAST:event_jrbFiltroVisitanteActionPerformed

    private void jrbFiltroDispositivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbFiltroDispositivoActionPerformed
        jrbFiltroVisitante.setSelected(false);
        jtHistorialVisita.setModel(LlenarTablaHistorialVisitaconDispositivo());
        txtTotalVisitas.setText(contarTotalVisitasDispositivos());
        jdtHistDesde.setCalendar(null);
        jdtHistHasta.setCalendar(null);
    }//GEN-LAST:event_jrbFiltroDispositivoActionPerformed

    private void btnVerEstadisticaAnualDispositivosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVerEstadisticaAnualDispositivosMouseClicked
        EstadisticaComparativa();
    }//GEN-LAST:event_btnVerEstadisticaAnualDispositivosMouseClicked

    private void jcbAñoGraficoEstadisticoComparativoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbAñoGraficoEstadisticoComparativoItemStateChanged
        idAnioDis = aniosd.get(jcbAñoGraficoEstadisticoComparativo.getSelectedIndex()).getIdhistorialvisitadispositivo();
    }//GEN-LAST:event_jcbAñoGraficoEstadisticoComparativoItemStateChanged

    private void jrbFiltroTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbFiltroTodoActionPerformed
        jrbFiltroDispositivo.setSelected(false);
        jrbFiltroVisitante.setSelected(false);
        jtHistorialVisita.setModel(LlenarTablaHistorialUnido());  
        txtTotalVisitas.setText(String.valueOf((Integer.parseInt(contarTotalVisitas())+(Integer.parseInt(contarTotalVisitasDispositivos())))));
        jdtHistDesde.setCalendar(null);
        jdtHistHasta.setCalendar(null);
    }//GEN-LAST:event_jrbFiltroTodoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Principal().setVisible(true);
        
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelDerechosMuseo;
    private javax.swing.JPanel PanelRol;
    private javax.swing.JPanel PanelRolUsuarios;
    private javax.swing.JPanel PanelRolUsuarios1;
    private javax.swing.JPanel PanelRolUsuarios3;
    private javax.swing.JPanel PanelRolUsuarios8;
    private javax.swing.ButtonGroup btgBuscarHistorialPor;
    private javax.swing.ButtonGroup btgSeleccion;
    private javax.swing.ButtonGroup btgVerEstadisticas;
    private javax.swing.JLabel btnActualizarUsuario;
    private javax.swing.JLabel btnAudioMuseo;
    private javax.swing.JLabel btnBuscarUsuarios;
    private javax.swing.JButton btnCancelarMuseo;
    private javax.swing.JButton btnEditarMuseo;
    private javax.swing.JLabel btnEliminarUsuario;
    private javax.swing.JButton btnGestionArticulos;
    private javax.swing.JButton btnGestionCategoria;
    private javax.swing.JLabel btnImprimirEstadistica;
    private javax.swing.JLabel btnImprimirEstadisticaDispositivos;
    private javax.swing.JLabel btnImprimirUsuarios;
    private javax.swing.JLabel btnImprimirVisitantes;
    private javax.swing.JLabel btnNuevoUsuario;
    private javax.swing.JLabel btnNuevoVisitante;
    private javax.swing.JLabel btnVerEstadisticaAnual;
    private javax.swing.JLabel btnVerEstadisticaAnualDispositivos;
    private javax.swing.JLabel btnVideoMuseo;
    public javax.swing.JLabel imgMuseo1;
    public javax.swing.JLabel imgMuseo2;
    public javax.swing.JLabel imgMuseo3;
    public javax.swing.JLabel imgMuseo4;
    public javax.swing.JLabel imgMuseo5;
    private javax.swing.JLabel imgQrMuseo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelEstadisticaDispositivoImprimir;
    private javax.swing.JPanel jPanelEstadisticaVisitaImprimir;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    public static javax.swing.JComboBox<String> jcbAñoGraficoEstadistico;
    public static javax.swing.JComboBox<String> jcbAñoGraficoEstadisticoComparativo;
    private javax.swing.JComboBox<String> jcbBuscarPor;
    public javax.swing.JDesktopPane jdeskAcercade;
    public javax.swing.JDesktopPane jdeskContactanos;
    public static javax.swing.JDesktopPane jdeskGaleria;
    public javax.swing.JDesktopPane jdeskPrincipal;
    public javax.swing.JDesktopPane jdeskusuarios;
    public static com.toedter.calendar.JDateChooser jdtHistDesde;
    public static com.toedter.calendar.JDateChooser jdtHistHasta;
    public static javax.swing.JLabel jlAudioMuseo;
    private javax.swing.JLabel jlGaleria;
    private javax.swing.JLabel jlJJ2016Acercade;
    private javax.swing.JLabel jlJJ2016Galeria;
    private javax.swing.JLabel jlJJ2016Historial;
    private javax.swing.JLabel jlJJ2016Principal;
    private javax.swing.JLabel jlJJ2016Usuarios;
    public static javax.swing.JLabel jlVideoMuseo;
    private javax.swing.JPanel jpPrincipal;
    private javax.swing.JPanel jpUsuarios;
    private javax.swing.JRadioButton jrbEstadisticasDispositivos;
    private javax.swing.JRadioButton jrbEstadisticasVisitantes;
    public static javax.swing.JRadioButton jrbFiltroDispositivo;
    private javax.swing.JRadioButton jrbFiltroTodo;
    public static javax.swing.JRadioButton jrbFiltroVisitante;
    public static javax.swing.JTable jtHistorialVisita;
    public static javax.swing.JTable jtUsuarios;
    private javax.swing.JLabel lblActualizarUsuario;
    private javax.swing.JLabel lblBuscarHistorialVisita;
    private javax.swing.JLabel lblBuscarUsuario;
    private javax.swing.JLabel lblCerrarSesion5;
    private javax.swing.JLabel lblCerrarSesion6;
    private javax.swing.JLabel lblCerrarSesion7;
    private javax.swing.JLabel lblCerrarSesion9;
    private javax.swing.JLabel lblCerrarSesionAcerca;
    private javax.swing.JLabel lblEliminarUsuario;
    private javax.swing.JLabel lblFundadorMuseo;
    private javax.swing.JLabel lblLimiteFundadorMuseo;
    private javax.swing.JLabel lblLimiteHistoriaMuseo;
    private javax.swing.JLabel lblLimiteNombreMuseo;
    private javax.swing.JLabel lblNombreMuseo;
    public javax.swing.JLabel lblNuevo;
    private javax.swing.JLabel lblNuevoUsuario;
    private javax.swing.JLabel lblPoliticasdePrivacidadContactanos;
    private javax.swing.JLabel lblPoliticasdePrivacidadGaleria;
    private javax.swing.JLabel lblPoliticasdePrivacidadPrincipal;
    private javax.swing.JLabel lblPoliticasdePrivacidadUsuarios;
    private javax.swing.JLabel lblPoliticasdePrivacidadacerca;
    private javax.swing.JLabel lblTerminosyCondicionesAcerca;
    private javax.swing.JLabel lblTerminosyCondicionesContactanos;
    private javax.swing.JLabel lblTerminosyCondicionesGaleria;
    private javax.swing.JLabel lblTerminosyCondicionesPrincipal;
    private javax.swing.JLabel lblTerminosyCondicionesUsuarios;
    private javax.swing.JLabel lblTituloGaleria;
    public static javax.swing.JLabel lblTorta;
    public static javax.swing.JLabel lblTortaDis;
    public static javax.swing.JLabel lblTotalPastel;
    public static javax.swing.JLabel lblTotalPastelDispositivos;
    public static javax.swing.JLabel lblTotalUsuarios;
    private javax.swing.JLabel lblUsuarioyRolAcerca;
    public static javax.swing.JLabel lblUsuarioyRolContactanos;
    public static javax.swing.JLabel lblUsuarioyRolGaleria;
    public static javax.swing.JLabel lblUsuarioyRolPrincipal;
    public static javax.swing.JLabel lblUsuarioyRolUsuarios;
    public static javax.swing.JPanel panelGraficoTorta;
    public static javax.swing.JPanel panelGraficoTortaDis;
    private javax.swing.JRadioButton rbtnActivo;
    private javax.swing.JRadioButton rbtnInactivo;
    private javax.swing.JSpinner spnFecha;
    private javax.swing.JTabbedPane tabMenu;
    private javax.swing.JTextField txtBuscarPor;
    private javax.swing.JTextField txtFundadorMuseo;
    private javax.swing.JTextArea txtHistoriaMuseo;
    private javax.swing.JTextField txtNombreMuseo;
    public javax.swing.JTextField txtTotalVisitas;
    private javax.swing.JTextField txtTotalVisitasDispositivosPorAñoCompartivo;
    private javax.swing.JTextField txtTotalVisitasPorAño;
    private javax.swing.JTextField txtTotalVisitasVisitantesPorAñoComparativo;
    private javax.swing.JTextField txtnombreVisitante;
    // End of variables declaration//GEN-END:variables

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException  {
        if(opimG==0){
        if (pageIndex == 0)
        {
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX()+50, pageFormat.getImageableY()+100);
            //g2d.scale(0.50,0.50); //Reducción de la impresión al 50%
            jPanelEstadisticaVisitaImprimir.printAll(graphics);
            //this.printAll(graphics);
            return PAGE_EXISTS;
        }
        else
            return NO_SUCH_PAGE;        
    }else{
            if (pageIndex == 0)
        {
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX()+50, pageFormat.getImageableY()+100);
            //g2d.scale(0.50,0.50); //Reducción de la impresión al 50%
            jPanelEstadisticaDispositivoImprimir.printAll(graphics);
            //this.printAll(graphics);
            return PAGE_EXISTS;
        }
        else
            return NO_SUCH_PAGE; 
        }
    
    }
    
}
