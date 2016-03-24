package generadorqr;

import Modelos.ItemSeleccionado;
import Modelos.UsuarioIngresado;
import Modelos.ValoresConstantes;
import com.mysql.jdbc.StringUtils;
import db.mysql;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
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


public final class Principal extends javax.swing.JFrame {
    DefaultTableModel model;
    static Connection con;
    static Statement sent;
    File fichero;
    jifrTerminosyCondiciones internalTerminos;
    jifrPoliticasdePrivacidad internalPoliticas;
    jifrNuevoUsuario internalNuevoUsuario;
    jifrGestionCategoria internalGestionCategoria;
    jifrGestionArticulos internalGestionArticulo;
    jifrContactanos internalContactanos;
    ItemSeleccionado is=new ItemSeleccionado();
    String id = "", rol = "", estado = "",fq;
    Integer buscar = 0, x = 0, y = 0;
    String imagenQR = "", codigoImagenQR = "";
    String[] imagen = {"", "", "", "", ""}, tempImagen = {"", "", "", "", ""}, tempNombreArchivo = {"", "", "", "", ""}, tempRutaActual = {"", "", "", "", ""};
    private Timer t;
    private ActionListener al;
        
    
    public Principal() {
        initComponents();
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        btgSeleccion.add(rbtnActivo);
        btgSeleccion.add(rbtnInactivo);
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
        Mostrar_Visualizador(btnActualizar, Ruta1);
        String Ruta2=getClass().getResource("/images/Eliminar1.png").getPath();
        Mostrar_Visualizador(btnEliminar, Ruta2);
        String Ruta3=getClass().getResource("/images/search.png").getPath();
        Mostrar_Visualizador(btnBuscarUsuarios, Ruta3);
        
        
        String rutaAA=getClass().getResource("/images/acercade.jpg").getPath();
        Mostrar_Visualizador(lblAcercaDeAnimalito, rutaAA);
        String rutaAApp=getClass().getResource("/images/appjqr.png").getPath();
        Mostrar_Visualizador(lblAcercaDeLaAplicacion, rutaAApp);
        String rutaAVN=getClass().getResource("/images/netbeans.png").getPath();
        Mostrar_Visualizador(lblAcercaDeAppVersionNetbeans, rutaAVN);
        String rutaAVA=getClass().getResource("/images/androidstudio.png").getPath();
        Mostrar_Visualizador(lblAcercaDeAppVersionandroid, rutaAVA);
        
        String rutaContactanosJJ=getClass().getResource("/images/search.png").getPath();
        Mostrar_Visualizador(lblBuscarHistorialVisita, rutaContactanosJJ);
        String rutaHistorial=getClass().getResource("/images/Eliminar1.png").getPath();
        Mostrar_Visualizador(lblEliminarHistorialVisita, rutaHistorial);
        
        
        txtBuscarPor.setEnabled(false);
        rbtnActivo.setEnabled(false);
        rbtnInactivo.setEnabled(false);
        jcbBuscarPor.setVisible(false);
        jtUsuarios.setModel(LlenarTablaUsuarios());
        jtHistorialVisita.setModel(LlenarTablaHistorialVisita());
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
        lblTotalUsuarios.setText(contarTotalU());
        txtTotalVisitas.setText(contarTotalVisitas());
        Limpiar();
        
        
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
                    //Actualizar usuario
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
                    String SQL = "UPDATE museo SET NOMBREMUSEO = ?, FECHAFUNDACIONMUSEO = ?, FUNDADORMUSEO = ?, HISTORIAMUSEO = ?, "
                        + "IMAGENUNOMUSEO = ?, IMAGENDOSMUSEO = ?, IMAGENTRESMUSEO = ?, IMAGENCUATROMUSEO = ?, IMAGENCINCOMUSEO = ? "
                           + " WHERE IDMUSEO = 1";
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
                }       
            }
        }    
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
    
    public static DefaultTableModel LlenarTablaUsuarios(){
        try{
            //Muestra los usuarios existentes en la base de datos
            String titulos[] = {"IDUSUARIO", "TIPO DE USUARIO","NOMBRE","APELLIDO","CEDULA","CORREO DEL USUARIO","ESTADO DE USUARIO"};
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
    
    public static DefaultTableModel LlenarTablaHistorialVisita(){
        try{
            //Muestra los usuarios existentes en la base de datos
            String titulos[] = {"ID DE VISITA", "FECHA Y HORA DE LA VISITA","ID DEL DISPOSITIVO"};
            //String SQL ="SELECT * FROM ingresos where CodigoParaiso Like '%"+txtBuscar.getText().toString().trim()+"%'AND ORDER BY Movimiento,Id,Fecha ASC"; 
            String SQLTH ="SELECT * FROM historialvisitas ORDER BY FECHAHORAVISITA DESC"; 
            DefaultTableModel model = new DefaultTableModel(null, titulos);
            Statement sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQLTH);
            String[]fila=new String[3];
            while(rs.next()){
                fila[0] = rs.getString("IDHISTORIALVISITA");
                fila[1] = rs.getString("FECHAHORAVISITA");
                fila[2] = rs.getString("IDDISPOSITIVO");
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
        try{
            String titulos[] = {"IDUSUARIO", "TIPO DE USUARIO","NOMBRE","APELLIDO","CEDULA","CORREO DEL USUARIO","ESTADO DE USUARIO"};

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
    
    void BuscarPorApellidoUsuario (){
        try{
            String titulos[] = {"IDUSUARIO", "TIPO DE USUARIO","NOMBRE","APELLIDO","CEDULA","CORREO DEL USUARIO","ESTADO DE USUARIO"};
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
    
    void BuscarPorTipoUsuario (){
        try{
            String titulos[] = {"IDUSUARIO", "TIPO DE USUARIO","NOMBRE","APELLIDO","CEDULA","CORREO DEL USUARIO","ESTADO DE USUARIO"};
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
    
    void BuscarPorCedula (){
        this.txtBuscarPor.setEnabled(true);
        try{
            String titulos[] = {"IDUSUARIO", "TIPO DE USUARIO","NOMBRE","APELLIDO","CEDULA","CORREO DEL USUARIO","ESTADO DE USUARIO"};
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
    
    void BuscarPorEstadoUsuario (){
        try{
            String titulos[] = {"IDUSUARIO", "TIPO DE USUARIO","NOMBRE","APELLIDO","CEDULA","CORREO DEL USUARIO","ESTADO DE USUARIO"};
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
    
    
    void BuscarPorEstadoUsuarioInactivo (){
        try{
            String titulos[] = {"IDUSUARIO", "TIPO DE USUARIO","NOMBRE","APELLIDO","CEDULA","CORREO DEL USUARIO","ESTADO DE USUARIO"};
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
    
    
    void BuscarPorFechaVisita (){
        try{
            String titulos[] = {"ID DE VISITA", "FECHA Y HORA DE LA VISITA","ID DEL DISPOSITIVO"};
            int a = jdtHistDesde.getDate().getYear() + 1900;
            int m = jdtHistDesde.getDate().getMonth() + 1;
            int d = jdtHistDesde.getDate().getDate();
            //String SQL ="SELECT * FROM ingresos where CodigoParaiso Like '%"+txtBuscar.getText().toString().trim()+"%'AND ORDER BY Movimiento,Id,Fecha ASC"; 
            //String SQLTH ="SELECT * FROM historialvisitas WHERE FECHAHORA VISITA = "++"ORDER BY FECHAHORAVISITA ASC"; 
            
            //String SQL = "SELECT *FROM usuarios WHERE ESTADOUSUARIO = 0 ORDER BY NOMBRESUSUARIO ASC";
            fq = d + "-" + m + "-" + a;
            String SQL ="SELECT * FROM historialvisitas where FECHAHORAVISITA Like '%"+fq+"%'" ;
            //System.out.println("Fecha en formato yyyy/MM/dd:  "+ new SimpleDateFormat("yyyy-MM-dd").format("FECHAHORAVISITA"));
            model= new DefaultTableModel(null, titulos);
            sent = con.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            String[]fila=new String[3];
            while(rs.next()){
                fila[0] = rs.getString("IDHISTORIALVISITA");
                fila[1] = new SimpleDateFormat("yyyy-MM-dd").format(rs.getString("FECHAHORAVISITA"));
                fila[2] = rs.getString("IDDISPOSITIVO");
                model.addRow(fila);
            }
        jtHistorialVisita.setModel(model);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error de Consulta..... :(");
        }
    }
    
    void EliminarVisita(){
        int fila = jtHistorialVisita.getSelectedRow();
        try {
            String SQL = "DELETE FROM historialvisitas WHERE IDHISTORIALVISITA=" + jtHistorialVisita.getValueAt(fila, 0);
            sent = con.createStatement();
            int n = sent.executeUpdate(SQL);
            if (n > 0){
                //JOptionPane.showMessageDialog(null, "Usuario eliminado correctamente ");
                jtHistorialVisita.setModel(LlenarTablaHistorialVisita());
                txtTotalVisitas.setText(contarTotalVisitas());
            }
            else JOptionPane.showMessageDialog(null, "Visita no eliminada ");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: Debe seleccionar un registro de visita");
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
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btgSeleccion = new javax.swing.ButtonGroup();
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
        PanelRol = new javax.swing.JPanel();
        lblCerrarSesion5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jpUsuarios = new javax.swing.JPanel();
        jdeskusuarios = new javax.swing.JDesktopPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtUsuarios = new javax.swing.JTable();
        btnNuevoUsuario = new javax.swing.JLabel();
        lblNuevo = new javax.swing.JLabel();
        btnActualizar = new javax.swing.JLabel();
        btnEliminar = new javax.swing.JLabel();
        btnBuscarUsuarios = new javax.swing.JLabel();
        jcbBuscarPor = new javax.swing.JComboBox<>();
        txtBuscarPor = new javax.swing.JTextField();
        rbtnActivo = new javax.swing.JRadioButton();
        rbtnInactivo = new javax.swing.JRadioButton();
        jLabel39 = new javax.swing.JLabel();
        lblTotalUsuarios = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
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
        btnGestionCategoria = new javax.swing.JLabel();
        btnGestionArticulos = new javax.swing.JLabel();
        PanelRolUsuarios1 = new javax.swing.JPanel();
        lblCerrarSesion7 = new javax.swing.JLabel();
        lblTituloGaleria = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jdeskContactanos = new javax.swing.JDesktopPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtHistorialVisita = new javax.swing.JTable();
        lblBuscarHistorialVisita = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtTotalVisitas = new javax.swing.JTextField();
        jdtHistDesde = new com.toedter.calendar.JDateChooser();
        lblEliminarHistorialVisita = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
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
        jPanel8 = new javax.swing.JPanel();
        jlJJ2016Acercade = new javax.swing.JLabel();
        lblTerminosyCondicionesAcerca = new javax.swing.JLabel();
        lblPoliticasdePrivacidadacerca = new javax.swing.JLabel();
        lblUsuarioyRolAcerca = new javax.swing.JLabel();
        losQr = new javax.swing.JLabel();
        lblAcercaDeAnimalito = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        lblAcercaDeLaAplicacion = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        losQr3 = new javax.swing.JLabel();
        losQr2 = new javax.swing.JLabel();
        lblAcercaDeAppVersionNetbeans = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        lblAcercaDeAppVersionandroid = new javax.swing.JLabel();
        PanelRolUsuarios8 = new javax.swing.JPanel();
        lblCerrarSesionAcerca = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setUndecorated(true);

        tabMenu.setBackground(new java.awt.Color(0, 0, 0));
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
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jdeskPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(btnEditarMuseo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(btnCancelarMuseo, javax.swing.GroupLayout.Alignment.TRAILING)))))
                            .addComponent(jScrollPane2)))
                    .addGroup(jdeskPrincipalLayout.createSequentialGroup()
                        .addGap(0, 61, Short.MAX_VALUE)
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                        .addComponent(imgMuseo1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        btnActualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/actualizar.png"))); // NOI18N
        btnActualizar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActualizar.setMaximumSize(new java.awt.Dimension(84, 81));
        btnActualizar.setMinimumSize(new java.awt.Dimension(84, 81));
        btnActualizar.setPreferredSize(new java.awt.Dimension(84, 81));
        btnActualizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnActualizarMouseClicked(evt);
            }
        });

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Eliminar1.png"))); // NOI18N
        btnEliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminar.setMaximumSize(new java.awt.Dimension(84, 81));
        btnEliminar.setMinimumSize(new java.awt.Dimension(84, 81));
        btnEliminar.setPreferredSize(new java.awt.Dimension(84, 81));
        btnEliminar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEliminarMouseClicked(evt);
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

        jLabel49.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel49.setText("Nuevo");

        jLabel50.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel50.setText("Actualizar");

        jLabel51.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel51.setText("Eliminar");

        jLabel52.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel52.setText("Buscar");

        jdeskusuarios.setLayer(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(btnNuevoUsuario, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(lblNuevo, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(btnActualizar, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(btnEliminar, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(btnBuscarUsuarios, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(jcbBuscarPor, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(txtBuscarPor, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(rbtnActivo, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(rbtnInactivo, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(jLabel39, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(lblTotalUsuarios, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(jLabel49, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(jLabel50, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(jLabel51, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(jLabel52, javax.swing.JLayeredPane.DEFAULT_LAYER);

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
                                            .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(25, 25, 25)
                                            .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                                                    .addComponent(rbtnInactivo)))))))))
                    .addGroup(jdeskusuariosLayout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(jLabel49)
                        .addGap(73, 73, 73)
                        .addComponent(jLabel50)
                        .addGap(57, 57, 57)
                        .addComponent(jLabel51)
                        .addGap(68, 68, 68)
                        .addComponent(jLabel52)))
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
                        .addComponent(btnActualizar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(jLabel49)
                    .addComponent(jLabel50)
                    .addComponent(jLabel51)
                    .addComponent(jLabel52))
                .addGap(4, 4, 4)
                .addGroup(jdeskusuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39)
                    .addComponent(lblTotalUsuarios))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(133, 133, 133))
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

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));

        jdeskGaleria.setBackground(new java.awt.Color(204, 204, 255));

        javax.swing.GroupLayout jdeskGaleriaLayout = new javax.swing.GroupLayout(jdeskGaleria);
        jdeskGaleria.setLayout(jdeskGaleriaLayout);
        jdeskGaleriaLayout.setHorizontalGroup(
            jdeskGaleriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 836, Short.MAX_VALUE)
        );
        jdeskGaleriaLayout.setVerticalGroup(
            jdeskGaleriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 563, Short.MAX_VALUE)
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
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblUsuarioyRolGaleria, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jlJJ2016Galeria)
                        .addComponent(lblTerminosyCondicionesGaleria)
                        .addComponent(lblPoliticasdePrivacidadGaleria)))
                .addGap(25, 25, 25))
        );

        btnGestionCategoria.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        btnGestionCategoria.setForeground(new java.awt.Color(255, 255, 255));
        btnGestionCategoria.setText("     Gestionar Categoría");
        btnGestionCategoria.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnGestionCategoria.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGestionCategoria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnGestionCategoriaMouseClicked(evt);
            }
        });

        btnGestionArticulos.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        btnGestionArticulos.setForeground(new java.awt.Color(255, 255, 255));
        btnGestionArticulos.setText("     Gestionar Artículo");
        btnGestionArticulos.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnGestionArticulos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGestionArticulos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnGestionArticulosMouseClicked(evt);
            }
        });

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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGestionCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGestionArticulos, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jdeskGaleria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(PanelRolUsuarios1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(PanelRolUsuarios1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(btnGestionCategoria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnGestionArticulos, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jdeskGaleria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tabMenu.addTab("       Galería", new javax.swing.ImageIcon(getClass().getResource("/images/galeria.png")), jPanel3); // NOI18N

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));

        jdeskContactanos.setBackground(new java.awt.Color(204, 204, 255));

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
        jScrollPane3.setViewportView(jtHistorialVisita);

        lblBuscarHistorialVisita.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search.png"))); // NOI18N
        lblBuscarHistorialVisita.setText("jLabel3");
        lblBuscarHistorialVisita.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblBuscarHistorialVisita.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblBuscarHistorialVisitaMouseClicked(evt);
            }
        });

        jLabel3.setText("Total Visitas");

        txtTotalVisitas.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalVisitas.setText("0");

        lblEliminarHistorialVisita.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Eliminar1.png"))); // NOI18N
        lblEliminarHistorialVisita.setText("jLabel3");
        lblEliminarHistorialVisita.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblEliminarHistorialVisita.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblEliminarHistorialVisitaMouseClicked(evt);
            }
        });

        jLabel4.setText("Buscar");

        jLabel6.setText("Eliminar");

        jdeskContactanos.setLayer(jScrollPane3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(lblBuscarHistorialVisita, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(jLabel3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(txtTotalVisitas, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(jdtHistDesde, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(lblEliminarHistorialVisita, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(jLabel4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(jLabel6, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jdeskContactanosLayout = new javax.swing.GroupLayout(jdeskContactanos);
        jdeskContactanos.setLayout(jdeskContactanosLayout);
        jdeskContactanosLayout.setHorizontalGroup(
            jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdeskContactanosLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jdeskContactanosLayout.createSequentialGroup()
                        .addComponent(jdtHistDesde, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblBuscarHistorialVisita, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblEliminarHistorialVisita, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jdeskContactanosLayout.createSequentialGroup()
                            .addGap(598, 598, 598)
                            .addComponent(jLabel3)
                            .addGap(54, 54, 54)
                            .addComponent(txtTotalVisitas, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 944, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(66, 66, 66))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdeskContactanosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(61, 61, 61)
                .addComponent(jLabel6)
                .addGap(79, 79, 79))
        );
        jdeskContactanosLayout.setVerticalGroup(
            jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdeskContactanosLayout.createSequentialGroup()
                .addGroup(jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jdeskContactanosLayout.createSequentialGroup()
                        .addGroup(jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6))
                        .addGap(1, 1, 1)
                        .addGroup(jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblBuscarHistorialVisita, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jdtHistDesde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdeskContactanosLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(lblEliminarHistorialVisita, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 436, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtTotalVisitas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(jdeskContactanos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabMenu.addTab("        Historial de Visita", new javax.swing.ImageIcon(getClass().getResource("/images/visitas.png")), jPanel4); // NOI18N

        jPanel5.setBackground(new java.awt.Color(0, 0, 0));

        jdeskAcercade.setBackground(new java.awt.Color(204, 204, 255));

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
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblUsuarioyRolAcerca, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jlJJ2016Acercade)
                        .addComponent(lblTerminosyCondicionesAcerca)
                        .addComponent(lblPoliticasdePrivacidadacerca)))
                .addGap(25, 25, 25))
        );

        losQr.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        losQr.setForeground(new java.awt.Color(204, 0, 0));
        losQr.setText("Los Qr");

        lblAcercaDeAnimalito.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/acercade.jpg"))); // NOI18N

        jLabel27.setFont(new java.awt.Font("Arial Black", 0, 11)); // NOI18N
        jLabel27.setText("El  Museo de la Escuela Isidro Ayora ");

        jLabel28.setFont(new java.awt.Font("Arial Black", 0, 11)); // NOI18N
        jLabel28.setText("existe  una  variedad en Galerías de ");

        jLabel29.setFont(new java.awt.Font("Arial Black", 0, 11)); // NOI18N
        jLabel29.setText("arte,  los mismos  que gracias a los ");

        jLabel30.setFont(new java.awt.Font("Arial Black", 0, 11)); // NOI18N
        jLabel30.setText("Qr estas piezas de arte contarán con");

        jLabel31.setFont(new java.awt.Font("Arial Black", 0, 11)); // NOI18N
        jLabel31.setText("toda su infromacion la misma que ");

        jLabel32.setFont(new java.awt.Font("Arial Black", 0, 11)); // NOI18N
        jLabel32.setText("estará accesible al público.");

        lblAcercaDeLaAplicacion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/appjqr.png"))); // NOI18N

        jLabel33.setFont(new java.awt.Font("Arial Black", 0, 11)); // NOI18N
        jLabel33.setText("JJQR  y  el  LectorJJQR  se basan en");

        jLabel34.setFont(new java.awt.Font("Arial Black", 0, 11)); // NOI18N
        jLabel34.setText("software libre desarrollado por Joss&");

        jLabel35.setFont(new java.awt.Font("Arial Black", 0, 11)); // NOI18N
        jLabel35.setText("Jess, la misma a contado con la guía");

        jLabel36.setFont(new java.awt.Font("Arial Black", 0, 11)); // NOI18N
        jLabel36.setText("del Ing.  F.  Viscaíno y el apoyo de la");

        jLabel37.setFont(new java.awt.Font("Arial Black", 0, 11)); // NOI18N
        jLabel37.setText("Escuela \"Isidro Ayora\"  y  su  planta");

        jLabel38.setFont(new java.awt.Font("Arial Black", 0, 11)); // NOI18N
        jLabel38.setText("docente como admiistradora.");

        losQr3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        losQr3.setForeground(new java.awt.Color(204, 0, 0));
        losQr3.setText("La Aplicacion");

        losQr2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        losQr2.setForeground(new java.awt.Color(204, 0, 0));
        losQr2.setText("JJQR 0.1");

        lblAcercaDeAppVersionNetbeans.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/netbeans.png"))); // NOI18N

        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(204, 0, 0));
        jLabel41.setText("LectorJJQR");

        jLabel42.setFont(new java.awt.Font("Arial Black", 0, 11)); // NOI18N
        jLabel42.setText("Product Version: Android Estudio 1.5.1 ");

        jLabel45.setFont(new java.awt.Font("Arial Black", 0, 11)); // NOI18N
        jLabel45.setText("Product Version: NetBeans 8.0.2 ");

        lblAcercaDeAppVersionandroid.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/androidstudio.png"))); // NOI18N

        jdeskAcercade.setLayer(jPanel8, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(losQr, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(lblAcercaDeAnimalito, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel27, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel28, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel29, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel30, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel31, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel32, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(lblAcercaDeLaAplicacion, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel33, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel34, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel35, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel36, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel37, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel38, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(losQr3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(losQr2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(lblAcercaDeAppVersionNetbeans, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel41, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel42, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel45, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(lblAcercaDeAppVersionandroid, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jdeskAcercadeLayout = new javax.swing.GroupLayout(jdeskAcercade);
        jdeskAcercade.setLayout(jdeskAcercadeLayout);
        jdeskAcercadeLayout.setHorizontalGroup(
            jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jdeskAcercadeLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jdeskAcercadeLayout.createSequentialGroup()
                        .addGroup(jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(lblAcercaDeAnimalito, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel27))
                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(78, 78, 78)
                        .addGroup(jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblAcercaDeLaAplicacion, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblAcercaDeAppVersionNetbeans, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel41, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addComponent(jLabel42, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jdeskAcercadeLayout.createSequentialGroup()
                        .addComponent(losQr)
                        .addGap(292, 292, 292)
                        .addComponent(losQr3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(losQr2))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jdeskAcercadeLayout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jdeskAcercadeLayout.createSequentialGroup()
                        .addGroup(jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel29)
                            .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblAcercaDeAppVersionandroid, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(142, 142, 142))
        );
        jdeskAcercadeLayout.setVerticalGroup(
            jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdeskAcercadeLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(losQr)
                    .addComponent(losQr2)
                    .addComponent(losQr3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jdeskAcercadeLayout.createSequentialGroup()
                        .addGroup(jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblAcercaDeAnimalito, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblAcercaDeLaAplicacion, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(63, 63, 63)
                        .addGroup(jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8))
                    .addGroup(jdeskAcercadeLayout.createSequentialGroup()
                        .addComponent(lblAcercaDeAppVersionNetbeans, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47)
                        .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblAcercaDeAppVersionandroid, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jdeskAcercadeLayout.createSequentialGroup()
                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jdeskAcercadeLayout.createSequentialGroup()
                        .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jdeskAcercadeLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelRolUsuarios8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jdeskAcercade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(PanelRolUsuarios8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addComponent(jdeskAcercade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tabMenu.addTab("        Acerca de", new javax.swing.ImageIcon(getClass().getResource("/images/info.png")), jPanel5); // NOI18N

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

    private void btnEliminarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarMouseClicked
        // TODO add your handling code here:
        Object [] opciones={"Aceptar","Cancelar"};
        if(!id.isEmpty()){
            int eleccion=JOptionPane.showOptionDialog(null,"Está seguro que desea eliminar","Eliminar",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,opciones,"Aceptar");
            if(eleccion==JOptionPane.YES_OPTION) Eliminar();
        }else JOptionPane.showMessageDialog(this, "No ha seleccionado un registro a eliminar");
    }//GEN-LAST:event_btnEliminarMouseClicked

    private void btnActualizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnActualizarMouseClicked
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
    }//GEN-LAST:event_btnActualizarMouseClicked

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

    private void btnGestionCategoriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGestionCategoriaMouseClicked
        internalGestionCategoria = new jifrGestionCategoria();
        centrarVentanaGestionCA(internalGestionCategoria);
        //lblTituloGaleria.setText("Categorías");
    }//GEN-LAST:event_btnGestionCategoriaMouseClicked

    private void btnGestionArticulosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGestionArticulosMouseClicked
        internalGestionArticulo = new jifrGestionArticulos();
        centrarVentanaGestionCA(internalGestionArticulo);
    }//GEN-LAST:event_btnGestionArticulosMouseClicked

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
        if(txtBuscarPor.getText().length()<1) LlenarTablaUsuarios();
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
        int limite  = 2000;
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
    BuscarPorFechaVisita();
    
    }//GEN-LAST:event_lblBuscarHistorialVisitaMouseClicked

    private void lblEliminarHistorialVisitaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblEliminarHistorialVisitaMouseClicked
    EliminarVisita();
    }//GEN-LAST:event_lblEliminarHistorialVisitaMouseClicked

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
    private javax.swing.ButtonGroup btgSeleccion;
    private javax.swing.JLabel btnActualizar;
    private javax.swing.JLabel btnBuscarUsuarios;
    private javax.swing.JButton btnCancelarMuseo;
    private javax.swing.JButton btnEditarMuseo;
    private javax.swing.JLabel btnEliminar;
    private javax.swing.JLabel btnGestionArticulos;
    private javax.swing.JLabel btnGestionCategoria;
    private javax.swing.JLabel btnNuevoUsuario;
    public javax.swing.JLabel imgMuseo1;
    public javax.swing.JLabel imgMuseo2;
    public javax.swing.JLabel imgMuseo3;
    public javax.swing.JLabel imgMuseo4;
    public javax.swing.JLabel imgMuseo5;
    private javax.swing.JLabel imgQrMuseo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JComboBox<String> jcbBuscarPor;
    public javax.swing.JDesktopPane jdeskAcercade;
    public javax.swing.JDesktopPane jdeskContactanos;
    public static javax.swing.JDesktopPane jdeskGaleria;
    public javax.swing.JDesktopPane jdeskPrincipal;
    public javax.swing.JDesktopPane jdeskusuarios;
    private com.toedter.calendar.JDateChooser jdtHistDesde;
    private javax.swing.JLabel jlGaleria;
    private javax.swing.JLabel jlJJ2016Acercade;
    private javax.swing.JLabel jlJJ2016Galeria;
    private javax.swing.JLabel jlJJ2016Historial;
    private javax.swing.JLabel jlJJ2016Principal;
    private javax.swing.JLabel jlJJ2016Usuarios;
    private javax.swing.JPanel jpPrincipal;
    private javax.swing.JPanel jpUsuarios;
    public static javax.swing.JTable jtHistorialVisita;
    public static javax.swing.JTable jtUsuarios;
    private javax.swing.JLabel lblAcercaDeAnimalito;
    private javax.swing.JLabel lblAcercaDeAppVersionNetbeans;
    private javax.swing.JLabel lblAcercaDeAppVersionandroid;
    private javax.swing.JLabel lblAcercaDeLaAplicacion;
    private javax.swing.JLabel lblBuscarHistorialVisita;
    private javax.swing.JLabel lblCerrarSesion5;
    private javax.swing.JLabel lblCerrarSesion6;
    private javax.swing.JLabel lblCerrarSesion7;
    private javax.swing.JLabel lblCerrarSesion9;
    private javax.swing.JLabel lblCerrarSesionAcerca;
    private javax.swing.JLabel lblEliminarHistorialVisita;
    private javax.swing.JLabel lblFundadorMuseo;
    private javax.swing.JLabel lblLimiteFundadorMuseo;
    private javax.swing.JLabel lblLimiteHistoriaMuseo;
    private javax.swing.JLabel lblLimiteNombreMuseo;
    private javax.swing.JLabel lblNombreMuseo;
    public javax.swing.JLabel lblNuevo;
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
    public static javax.swing.JLabel lblTotalUsuarios;
    private javax.swing.JLabel lblUsuarioyRolAcerca;
    private javax.swing.JLabel lblUsuarioyRolContactanos;
    private javax.swing.JLabel lblUsuarioyRolGaleria;
    private javax.swing.JLabel lblUsuarioyRolPrincipal;
    private javax.swing.JLabel lblUsuarioyRolUsuarios;
    private javax.swing.JLabel losQr;
    private javax.swing.JLabel losQr2;
    private javax.swing.JLabel losQr3;
    private javax.swing.JRadioButton rbtnActivo;
    private javax.swing.JRadioButton rbtnInactivo;
    private javax.swing.JSpinner spnFecha;
    private javax.swing.JTabbedPane tabMenu;
    private javax.swing.JTextField txtBuscarPor;
    private javax.swing.JTextField txtFundadorMuseo;
    private javax.swing.JTextArea txtHistoriaMuseo;
    private javax.swing.JTextField txtNombreMuseo;
    private javax.swing.JTextField txtTotalVisitas;
    // End of variables declaration//GEN-END:variables
}
