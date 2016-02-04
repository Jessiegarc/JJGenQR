package generadorqr;

import Modelos.ItemSeleccionado;
import Modelos.UsuarioIngresado;
import db.mysql;
import java.awt.Image;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class Principal extends javax.swing.JFrame {
    DefaultTableModel model;
    static Connection con;
    static Statement sent;
    jifrTerminosyCondiciones internalTerminos;
    jifrPoliticasdePrivacidad internalPoliticas;
    jifrNuevoUsuario internalNuevoUsuario;
    jifrGestionCategoria internalGestionCategoria;
    jifrGestionArticulos internalGestionArticulo;
    ItemSeleccionado is=new ItemSeleccionado();
    String id = "", rol = "", estado = "";
    Integer buscar = 0;
    
    public Principal() {
        initComponents();
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        //tabMenu.setBackgroundAt(0,Color.BLACK);
        if(con == null) con = mysql.getConnect();
        lblNuevo.setVisible(false);
        lblUsuarioyRolPrincipal.setText("Bienvenid@ " + UsuarioIngresado.parametroU+" tu rol es de " + UsuarioIngresado.parametroR);
        lblUsuarioyRolUsuarios.setText("Bienvenid@ " + UsuarioIngresado.parametroU+" tu rol es de " + UsuarioIngresado.parametroR);
        lblUsuarioyRolGaleria.setText("Bienvenid@ " + UsuarioIngresado.parametroU+" tu rol es de " + UsuarioIngresado.parametroR);
        lblUsuarioyRolContactanos.setText("Bienvenid@ " + UsuarioIngresado.parametroU+" tu rol es de " + UsuarioIngresado.parametroR);
        lblUsuarioyRolAcerca.setText("Bienvenid@ " + UsuarioIngresado.parametroU+" tu rol es de " + UsuarioIngresado.parametroR);
        
        String Ruta=getClass().getResource("/images/Mas.png").getPath();
        Mostrar_Visualizador(btnNuevoUsuario, Ruta);
        String Ruta1=getClass().getResource("/images/actualizar.png").getPath();
        Mostrar_Visualizador(btnActualizar, Ruta1);
        String Ruta2=getClass().getResource("/images/Eliminar.png").getPath();
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
        
        
        
        String rutaContactanosJJ=getClass().getResource("/images/contactanos.jpg").getPath();
        Mostrar_Visualizador(lblImgContactanosJJ, rutaContactanosJJ);
        
        
        txtBuscarPor.setEnabled(false);
        rbtnActivo.setEnabled(false);
        rbtnInactivo.setEnabled(false);
        jcbBuscarPor.setVisible(false);
        LlenarTablaUsuarios();
    }
    
    void LlenarTablaUsuarios(){
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
            jtUsuarios.setModel(model);
            //model.fireTableDataChanged();
        }catch(Exception e){
        }
    }
    
    void Eliminar(){
        int fila = jtUsuarios.getSelectedRow();
        try {
            String SQL = "DELETE FROM usuarios WHERE IdUSUARIO=" + jtUsuarios.getValueAt(fila, 0);
            sent = con.createStatement();
            int n = sent.executeUpdate(SQL);
            if (n > 0){
                JOptionPane.showMessageDialog(null, "Usuario eliminado correctamente ");
                LlenarTablaUsuarios();
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
    
    public void centrarVentanaInternaTerminosPrincipal ( jifrTerminosyCondiciones internalFrame){
        int x=(jdeskPrincipal.getWidth()/2)-internalFrame.getWidth()/2; 
        int y=(jdeskPrincipal.getHeight()/2)-internalFrame.getHeight()/2;
        if(internalFrame.isShowing()){
            internalFrame.setLocation(x, y);
        }else{
            jdeskPrincipal.add(internalFrame);
            internalFrame.setLocation(x, y);
            internalFrame.show();
        }
    }
    
    
    
    public void centrarVentanaInternaPoliticasPrincipal ( jifrPoliticasdePrivacidad internalFrameP){
        int x=(jdeskPrincipal.getWidth()/2)-internalFrameP.getWidth()/2; 
        int y=(jdeskPrincipal.getHeight()/2)-internalFrameP.getHeight()/2;
        if(internalFrameP.isShowing()){
            internalFrameP.setLocation(x, y);
        }else{
            jdeskPrincipal.add(internalFrameP);
            internalFrameP.setLocation(x, y);
            internalFrameP.show();
        }
    }
    
    
    public void centrarVentanaInternaTerminosUsuarios ( jifrTerminosyCondiciones internalFrame){
        int x=(jdeskusuarios.getWidth()/2)-internalFrame.getWidth()/2; 
        int y=(jdeskusuarios.getHeight()/2)-internalFrame.getHeight()/2;
        if(internalFrame.isShowing()){
            internalFrame.setLocation(x, y);
        }else{
            jdeskusuarios.add(internalFrame);
            internalFrame.setLocation(x, y);
            internalFrame.show();
        }
    }
    
    
    
    public void centrarVentanaInternaPoliticasUsuarios ( jifrPoliticasdePrivacidad internalFrameP){
        int x=(jdeskusuarios.getWidth()/2)-internalFrameP.getWidth()/2; 
        int y=(jdeskusuarios.getHeight()/2)-internalFrameP.getHeight()/2;
        if(internalFrameP.isShowing()){
            internalFrameP.setLocation(x, y);
        }else{
            jdeskusuarios.add(internalFrameP);
            internalFrameP.setLocation(x, y);
            internalFrameP.show();
        }
    }
    
    
    public void centrarVentanaInternaTerminosGaleria ( jifrTerminosyCondiciones internalFrame){
        int x=(jdeskGaleria.getWidth()/2)-internalFrame.getWidth()/2; 
        int y=(jdeskGaleria.getHeight()/2)-internalFrame.getHeight()/2;
        if(internalFrame.isShowing()){
            internalFrame.setLocation(x, y);
        }else{
            jdeskGaleria.add(internalFrame);
            internalFrame.setLocation(x, y);
            internalFrame.show();
        }
    }
    
    
    
    public void centrarVentanaInternaPoliticasGaleria ( jifrPoliticasdePrivacidad internalFrameP){
        int x=(jdeskGaleria.getWidth()/2)-internalFrameP.getWidth()/2; 
        int y=(jdeskGaleria.getHeight()/2)-internalFrameP.getHeight()/2;
        if(internalFrameP.isShowing()){
            internalFrameP.setLocation(x, y);
        }else{
            jdeskGaleria.add(internalFrameP);
            internalFrameP.setLocation(x, y);
            internalFrameP.show();
        }
    }
    
     
    public void centrarVentanaInternaTerminosContactanos ( jifrTerminosyCondiciones internalFrame){
        int x=(jdeskContactanos.getWidth()/2)-internalFrame.getWidth()/2; 
        int y=(jdeskContactanos.getHeight()/2)-internalFrame.getHeight()/2;
        if(internalFrame.isShowing()){
            internalFrame.setLocation(x, y);
        }else{
            jdeskContactanos.add(internalFrame);
            internalFrame.setLocation(x, y);
            internalFrame.show();
        }
    }
    
    
    
    public void centrarVentanaInternaPoliticasContactanos ( jifrPoliticasdePrivacidad internalFrameP){
        int x=(jdeskContactanos.getWidth()/2)-internalFrameP.getWidth()/2; 
        int y=(jdeskContactanos.getHeight()/2)-internalFrameP.getHeight()/2;
        if(internalFrameP.isShowing()){
            internalFrameP.setLocation(x, y);
        }else{
            jdeskContactanos.add(internalFrameP);
            internalFrameP.setLocation(x, y);
            internalFrameP.show();
        }
    }
     
    public void centrarVentanaInternaTerminosAcerca ( jifrTerminosyCondiciones internalFrame){
        int x=(jdeskAcercade.getWidth()/2)-internalFrame.getWidth()/2; 
        int y=(jdeskAcercade.getHeight()/2)-internalFrame.getHeight()/2;
        if(internalFrame.isShowing()){
            internalFrame.setLocation(x, y);
        }else{
            jdeskAcercade.add(internalFrame);
            internalFrame.setLocation(x, y);
            internalFrame.show();
        }
    }
    
    
    
    public void centrarVentanaInternaPoliticasAcerca ( jifrPoliticasdePrivacidad internalFrameP){
        int x=(jdeskAcercade.getWidth()/2)-internalFrameP.getWidth()/2; 
        int y=(jdeskAcercade.getHeight()/2)-internalFrameP.getHeight()/2;
        if(internalFrameP.isShowing()){
            internalFrameP.setLocation(x, y);
        }else{
            jdeskAcercade.add(internalFrameP);
            internalFrameP.setLocation(x, y);
            internalFrameP.show();
        }
    }
    
     public void centrarVentanaInternaNuevoUsuario ( jifrNuevoUsuario internalFrameNuevoU){
        int x=(jdeskusuarios.getWidth()/2)-internalFrameNuevoU.getWidth()/2; 
        int y=(jdeskusuarios.getHeight()/2)-internalFrameNuevoU.getHeight()/2;
        if(internalFrameNuevoU.isShowing()){
            internalFrameNuevoU.setLocation(x, y);
        }else{
            jdeskusuarios.add(internalFrameNuevoU);
            internalFrameNuevoU.setLocation(x, y);
            internalFrameNuevoU.show();
        }
    }
    
    public void centrarVentanaGestionCategoria ( jifrGestionCategoria internalFrame){
        /*int x=(jdeskGaleria.getWidth()/2)-internalFrame.getWidth()/2; 
        int y=(jdeskGaleria.getHeight()/2)-internalFrame.getHeight()/2;
        
        if(internalFrame.isShowing()){
            internalFrame.setLocation(x, y);
        }else{
            jdeskGaleria.add(internalFrame);
            internalFrame.setLocation(x, y);       
            internalFrame.show();
        }*/
        jdeskGaleria.add(internalFrame);
        int width = jdeskGaleria.getWidth();
        int Height = jdeskGaleria.getHeight();
        internalFrame.setSize(width, Height);
        internalFrame.show();
    }
    
    public void centrarVentanaGestionArticulo( jifrGestionArticulos internalFrame){
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
    
     
     
    private Principal(String principal) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

        tabMenu = new javax.swing.JTabbedPane();
        jpPrincipal = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jlJJ2016 = new javax.swing.JLabel();
        lblTerminosyCondicionesPrincipal = new javax.swing.JLabel();
        lblPoliticasdePrivacidadPrincipal = new javax.swing.JLabel();
        lblUsuarioyRolPrincipal = new javax.swing.JLabel();
        jdeskPrincipal = new javax.swing.JDesktopPane();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        imgMuseo1 = new javax.swing.JLabel();
        imgMuseo2 = new javax.swing.JLabel();
        jlGaleria = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblCerrarSesion = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jdeskusuarios = new javax.swing.JDesktopPane();
        jPanel9 = new javax.swing.JPanel();
        jlJJ2019 = new javax.swing.JLabel();
        lblTerminosyCondicionesUsuarios = new javax.swing.JLabel();
        lblPoliticasdePrivacidadUsuarios = new javax.swing.JLabel();
        lblUsuarioyRolUsuarios = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtUsuarios = new javax.swing.JTable();
        lblCerrarSesion1 = new javax.swing.JLabel();
        btnNuevoUsuario = new javax.swing.JLabel();
        lblNuevo = new javax.swing.JLabel();
        btnActualizar = new javax.swing.JLabel();
        btnEliminar = new javax.swing.JLabel();
        btnBuscarUsuarios = new javax.swing.JLabel();
        jcbBuscarPor = new javax.swing.JComboBox<>();
        txtBuscarPor = new javax.swing.JTextField();
        rbtnActivo = new javax.swing.JRadioButton();
        rbtnInactivo = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        jdeskGaleria = new javax.swing.JDesktopPane();
        jPanel10 = new javax.swing.JPanel();
        jlJJ2020 = new javax.swing.JLabel();
        lblTerminosyCondicionesGaleria = new javax.swing.JLabel();
        lblPoliticasdePrivacidadGaleria = new javax.swing.JLabel();
        lblUsuarioyRolGaleria = new javax.swing.JLabel();
        lblCerrarSesion2 = new javax.swing.JLabel();
        btnGestionCategoria = new javax.swing.JLabel();
        btnGestionArticulos = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jdeskContactanos = new javax.swing.JDesktopPane();
        jPanel7 = new javax.swing.JPanel();
        jlJJ2017 = new javax.swing.JLabel();
        lblTerminosyCondicionesContactanos = new javax.swing.JLabel();
        lblPoliticasdePrivacidadContactanos = new javax.swing.JLabel();
        lblUsuarioyRolContactanos = new javax.swing.JLabel();
        lblImgContactanosJJ = new javax.swing.JLabel();
        lblCerrarSesion3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jdeskAcercade = new javax.swing.JDesktopPane();
        jPanel8 = new javax.swing.JPanel();
        jlJJ2018 = new javax.swing.JLabel();
        lblTerminosyCondicionesAcerca = new javax.swing.JLabel();
        lblPoliticasdePrivacidadacerca = new javax.swing.JLabel();
        lblUsuarioyRolAcerca = new javax.swing.JLabel();
        lblCerrarSesion4 = new javax.swing.JLabel();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        tabMenu.setBackground(new java.awt.Color(0, 0, 0));
        tabMenu.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        tabMenu.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        jpPrincipal.setBackground(new java.awt.Color(204, 204, 204));

        jPanel6.setBackground(new java.awt.Color(0, 0, 0));

        jlJJ2016.setForeground(new java.awt.Color(255, 255, 255));
        jlJJ2016.setText("JJ 2016 Reservados  todos  los  derechos . ");

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

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(jlJJ2016)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTerminosyCondicionesPrincipal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblPoliticasdePrivacidadPrincipal)
                .addGap(132, 132, 132)
                .addComponent(lblUsuarioyRolPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlJJ2016)
                            .addComponent(lblTerminosyCondicionesPrincipal)
                            .addComponent(lblPoliticasdePrivacidadPrincipal))
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(lblUsuarioyRolPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        jdeskPrincipal.setBackground(new java.awt.Color(204, 204, 255));

        jLabel1.setFont(new java.awt.Font("Nirmala UI", 1, 24)); // NOI18N
        jLabel1.setText("MUSEO \"ISIDRO AYORA\"");

        jLabel2.setFont(new java.awt.Font("Century Schoolbook", 0, 14)); // NOI18N
        jLabel2.setText("Museo, que lleva el nombre de \"Jorgue Gallegos Cruz\", honrando asi la  memoria del amigo,  compañero y director,  que se debe al aporte de los");

        jLabel3.setFont(new java.awt.Font("Century Schoolbook", 0, 14)); // NOI18N
        jLabel3.setText("maestros que lo integran. Cuenta con las siguientes secciones: Zoología, Botánica, Numismática, Arqueología, Mineralogía, Folklore, Maquetas ");

        jLabel4.setFont(new java.awt.Font("Century Schoolbook", 0, 14)); // NOI18N
        jLabel4.setText("Tecnológias relacionadas con la ciencia y la industria de la provincia.");

        imgMuseo1.setFont(new java.awt.Font("Century Schoolbook", 0, 14)); // NOI18N
        imgMuseo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/museo2.jpg"))); // NOI18N

        imgMuseo2.setFont(new java.awt.Font("Century Schoolbook", 0, 14)); // NOI18N
        imgMuseo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/museo1.jpg"))); // NOI18N

        jlGaleria.setFont(new java.awt.Font("Century Schoolbook", 1, 14)); // NOI18N
        jlGaleria.setText("Galería del Museo de la Escuela Isidro Ayora");

        jLabel5.setFont(new java.awt.Font("Century Schoolbook", 0, 14)); // NOI18N
        jLabel5.setText("El Compromiso de los Profesores de la Escuela Isidro Ayora y el Acto de inauguración se lo fijó para el 15 de mayo de 1963, coincidiendo con el día");

        jLabel6.setFont(new java.awt.Font("Century Schoolbook", 0, 14)); // NOI18N
        jLabel6.setText("de San Isidro, la constancia de este importante aconteciimiento quedó grabado para siempre.");

        lblCerrarSesion.setFont(new java.awt.Font("Sylfaen", 1, 14)); // NOI18N
        lblCerrarSesion.setForeground(new java.awt.Color(255, 0, 0));
        lblCerrarSesion.setText("[Cerrar Sesión]");
        lblCerrarSesion.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCerrarSesion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCerrarSesionMouseClicked(evt);
            }
        });

        jdeskPrincipal.setLayer(jLabel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskPrincipal.setLayer(jLabel2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskPrincipal.setLayer(jLabel3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskPrincipal.setLayer(jLabel4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskPrincipal.setLayer(imgMuseo1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskPrincipal.setLayer(imgMuseo2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskPrincipal.setLayer(jlGaleria, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskPrincipal.setLayer(jLabel5, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskPrincipal.setLayer(jLabel6, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskPrincipal.setLayer(lblCerrarSesion, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jdeskPrincipalLayout = new javax.swing.GroupLayout(jdeskPrincipal);
        jdeskPrincipal.setLayout(jdeskPrincipalLayout);
        jdeskPrincipalLayout.setHorizontalGroup(
            jdeskPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdeskPrincipalLayout.createSequentialGroup()
                .addGroup(jdeskPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jdeskPrincipalLayout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addGroup(jdeskPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jdeskPrincipalLayout.createSequentialGroup()
                                .addComponent(imgMuseo1, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(146, 146, 146)
                                .addComponent(imgMuseo2))
                            .addGroup(jdeskPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 924, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jdeskPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 939, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jdeskPrincipalLayout.createSequentialGroup()
                        .addGap(342, 342, 342)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jdeskPrincipalLayout.createSequentialGroup()
                        .addGap(333, 333, 333)
                        .addComponent(jlGaleria)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdeskPrincipalLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblCerrarSesion)
                .addGap(98, 98, 98))
        );
        jdeskPrincipalLayout.setVerticalGroup(
            jdeskPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdeskPrincipalLayout.createSequentialGroup()
                .addComponent(lblCerrarSesion)
                .addGap(19, 19, 19)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jlGaleria)
                .addGap(18, 18, 18)
                .addGroup(jdeskPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(imgMuseo1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(imgMuseo2))
                .addGap(32, 32, 32)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addContainerGap(117, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jpPrincipalLayout = new javax.swing.GroupLayout(jpPrincipal);
        jpPrincipal.setLayout(jpPrincipalLayout);
        jpPrincipalLayout.setHorizontalGroup(
            jpPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jdeskPrincipal)
        );
        jpPrincipalLayout.setVerticalGroup(
            jpPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpPrincipalLayout.createSequentialGroup()
                .addComponent(jdeskPrincipal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tabMenu.addTab("Museo \"ISIDRO AYORA\"", new javax.swing.ImageIcon(getClass().getResource("/images/home.png")), jpPrincipal); // NOI18N

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));

        jdeskusuarios.setBackground(new java.awt.Color(204, 204, 255));

        jPanel9.setBackground(new java.awt.Color(0, 0, 0));

        jlJJ2019.setForeground(new java.awt.Color(255, 255, 255));
        jlJJ2019.setText("JJ 2016 Reservados  todos  los  derechos . ");

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

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(jlJJ2019)
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
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlJJ2019)
                            .addComponent(lblTerminosyCondicionesUsuarios)
                            .addComponent(lblPoliticasdePrivacidadUsuarios))
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addComponent(lblUsuarioyRolUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

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

        lblCerrarSesion1.setFont(new java.awt.Font("Sylfaen", 1, 14)); // NOI18N
        lblCerrarSesion1.setForeground(new java.awt.Color(255, 0, 0));
        lblCerrarSesion1.setText("[Cerrar Sesión]");
        lblCerrarSesion1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCerrarSesion1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCerrarSesion1MouseClicked(evt);
            }
        });

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

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/eliminar.jpg"))); // NOI18N
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

        jdeskusuarios.setLayer(jPanel9, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(lblCerrarSesion1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(btnNuevoUsuario, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(lblNuevo, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(btnActualizar, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(btnEliminar, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(btnBuscarUsuarios, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(jcbBuscarPor, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(txtBuscarPor, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(rbtnActivo, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(rbtnInactivo, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jdeskusuariosLayout = new javax.swing.GroupLayout(jdeskusuarios);
        jdeskusuarios.setLayout(jdeskusuariosLayout);
        jdeskusuariosLayout.setHorizontalGroup(
            jdeskusuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jdeskusuariosLayout.createSequentialGroup()
                .addGroup(jdeskusuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jdeskusuariosLayout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(btnNuevoUsuario)
                        .addGap(29, 29, 29)
                        .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(btnBuscarUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addGroup(jdeskusuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtBuscarPor, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jdeskusuariosLayout.createSequentialGroup()
                                .addComponent(jcbBuscarPor, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(rbtnActivo)
                                .addGap(18, 18, 18)
                                .addComponent(rbtnInactivo))))
                    .addGroup(jdeskusuariosLayout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(lblNuevo))
                    .addGroup(jdeskusuariosLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 987, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdeskusuariosLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblCerrarSesion1)
                .addGap(67, 67, 67))
        );
        jdeskusuariosLayout.setVerticalGroup(
            jdeskusuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdeskusuariosLayout.createSequentialGroup()
                .addComponent(lblCerrarSesion1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblNuevo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 96, Short.MAX_VALUE)
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
                .addGap(65, 65, 65)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jdeskusuarios)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jdeskusuarios)
        );

        tabMenu.addTab("         Usuarios      ", new javax.swing.ImageIcon(getClass().getResource("/images/usuarios.png")), jPanel2); // NOI18N

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
            .addGap(0, 600, Short.MAX_VALUE)
        );

        jPanel10.setBackground(new java.awt.Color(0, 0, 0));

        jlJJ2020.setForeground(new java.awt.Color(255, 255, 255));
        jlJJ2020.setText("JJ 2016 Reservados  todos  los  derechos . ");

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

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(jlJJ2020)
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
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlJJ2020)
                            .addComponent(lblTerminosyCondicionesGaleria)
                            .addComponent(lblPoliticasdePrivacidadGaleria))
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addComponent(lblUsuarioyRolGaleria, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        lblCerrarSesion2.setFont(new java.awt.Font("Sylfaen", 1, 14)); // NOI18N
        lblCerrarSesion2.setForeground(new java.awt.Color(255, 0, 0));
        lblCerrarSesion2.setText("[Cerrar Sesión]");
        lblCerrarSesion2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCerrarSesion2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCerrarSesion2MouseClicked(evt);
            }
        });

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblCerrarSesion2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(103, 103, 103))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCerrarSesion2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnGestionCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnGestionArticulos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jdeskGaleria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tabMenu.addTab("       Galería", new javax.swing.ImageIcon(getClass().getResource("/images/galeria.png")), jPanel3); // NOI18N

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));

        jdeskContactanos.setBackground(new java.awt.Color(204, 204, 255));

        jPanel7.setBackground(new java.awt.Color(0, 0, 0));

        jlJJ2017.setForeground(new java.awt.Color(255, 255, 255));
        jlJJ2017.setText("JJ 2016 Reservados  todos  los  derechos . ");

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

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(jlJJ2017)
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
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlJJ2017)
                            .addComponent(lblTerminosyCondicionesContactanos)
                            .addComponent(lblPoliticasdePrivacidadContactanos))
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(lblUsuarioyRolContactanos, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        lblImgContactanosJJ.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/nosotras.png"))); // NOI18N

        lblCerrarSesion3.setFont(new java.awt.Font("Sylfaen", 1, 14)); // NOI18N
        lblCerrarSesion3.setForeground(new java.awt.Color(153, 0, 0));
        lblCerrarSesion3.setText("[Cerrar Sesión]");
        lblCerrarSesion3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCerrarSesion3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCerrarSesion3MouseClicked(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel7.setText("Nombre:");

        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setText("Josselyn Karina Carrillo Betancourt");

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel10.setText("Correo:");

        jLabel9.setForeground(new java.awt.Color(51, 51, 51));
        jLabel9.setText("joselin@hotmail.com");

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel11.setText("Dirección");

        jLabel12.setForeground(new java.awt.Color(51, 51, 51));
        jLabel12.setText("Latacunga");

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel13.setText("Teléfono:");

        jLabel14.setForeground(new java.awt.Color(51, 51, 51));
        jLabel14.setText("09835656545");

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel15.setText("Blog:");

        jLabel16.setForeground(new java.awt.Color(51, 51, 51));
        jLabel16.setText("jcarrillo.blogspot.com");

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel17.setText("Nombre:");

        jLabel18.setBackground(new java.awt.Color(51, 51, 51));
        jLabel18.setForeground(new java.awt.Color(51, 51, 51));
        jLabel18.setText("Jessica Rocío Guanoluiza Arcos");

        jLabel19.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel19.setText("Correo:");

        jLabel20.setBackground(new java.awt.Color(51, 51, 51));
        jLabel20.setForeground(new java.awt.Color(51, 51, 51));
        jLabel20.setText("jessica.guanoluiza@gmail.com");

        jLabel21.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel21.setText("Dirección");

        jLabel22.setBackground(new java.awt.Color(51, 51, 51));
        jLabel22.setForeground(new java.awt.Color(51, 51, 51));
        jLabel22.setText("Aloag- Machachi");

        jLabel23.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel23.setText("Teléfono");

        jLabel24.setBackground(new java.awt.Color(51, 51, 51));
        jLabel24.setForeground(new java.awt.Color(51, 51, 51));
        jLabel24.setText("0992609899");

        jLabel25.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel25.setText("Blog");

        jLabel26.setBackground(new java.awt.Color(51, 51, 51));
        jLabel26.setForeground(new java.awt.Color(51, 51, 51));
        jLabel26.setText("jessiearcs@blogspot.com");

        jdeskContactanos.setLayer(jPanel7, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(lblImgContactanosJJ, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(lblCerrarSesion3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(jLabel7, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(jLabel8, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(jLabel10, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(jLabel9, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(jLabel11, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(jLabel12, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(jLabel13, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(jLabel14, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(jLabel15, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(jLabel16, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(jLabel17, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(jLabel18, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(jLabel19, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(jLabel20, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(jLabel21, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(jLabel22, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(jLabel23, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(jLabel24, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(jLabel25, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(jLabel26, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jdeskContactanosLayout = new javax.swing.GroupLayout(jdeskContactanos);
        jdeskContactanos.setLayout(jdeskContactanosLayout);
        jdeskContactanosLayout.setHorizontalGroup(
            jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jdeskContactanosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jdeskContactanosLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(20, 20, 20)
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jdeskContactanosLayout.createSequentialGroup()
                            .addComponent(jLabel7)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8))
                        .addGroup(jdeskContactanosLayout.createSequentialGroup()
                            .addComponent(jLabel11)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(20, 20, 20))
                        .addGroup(jdeskContactanosLayout.createSequentialGroup()
                            .addGroup(jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel13)
                                .addComponent(jLabel15))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(28, 28, 28))))
                .addGap(34, 34, 34)
                .addComponent(lblImgContactanosJJ, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addComponent(jLabel23)
                    .addComponent(jLabel25)
                    .addComponent(jLabel19)
                    .addComponent(jLabel17))
                .addGap(22, 22, 22)
                .addGroup(jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdeskContactanosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblCerrarSesion3)
                .addGap(176, 176, 176))
        );
        jdeskContactanosLayout.setVerticalGroup(
            jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdeskContactanosLayout.createSequentialGroup()
                .addComponent(lblCerrarSesion3)
                .addGroup(jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jdeskContactanosLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblImgContactanosJJ, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(81, 81, 81))
                    .addGroup(jdeskContactanosLayout.createSequentialGroup()
                        .addGap(194, 194, 194)
                        .addGroup(jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(28, 28, 28)
                        .addGroup(jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10))
                        .addGap(28, 28, 28)
                        .addGroup(jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11))
                        .addGap(32, 32, 32)
                        .addGroup(jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13))
                        .addGap(31, 31, 31)
                        .addGroup(jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(244, 244, 244))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdeskContactanosLayout.createSequentialGroup()
                        .addGap(200, 200, 200)
                        .addGroup(jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(33, 33, 33)
                        .addGroup(jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(35, 35, 35)
                        .addGroup(jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel21))
                        .addGap(34, 34, 34)
                        .addGroup(jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel23))
                        .addGap(26, 26, 26)
                        .addGroup(jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel25))
                        .addGap(229, 229, 229)))
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jdeskContactanos)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jdeskContactanos)
        );

        tabMenu.addTab("        Contáctanos", new javax.swing.ImageIcon(getClass().getResource("/images/contactanos.png")), jPanel4); // NOI18N

        jPanel5.setBackground(new java.awt.Color(0, 0, 0));

        jdeskAcercade.setBackground(new java.awt.Color(204, 204, 255));

        jPanel8.setBackground(new java.awt.Color(0, 0, 0));

        jlJJ2018.setForeground(new java.awt.Color(255, 255, 255));
        jlJJ2018.setText("JJ 2016 Reservados  todos  los  derechos . ");

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

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(jlJJ2018)
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
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlJJ2018)
                            .addComponent(lblTerminosyCondicionesAcerca)
                            .addComponent(lblPoliticasdePrivacidadacerca))
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(lblUsuarioyRolAcerca, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        lblCerrarSesion4.setFont(new java.awt.Font("Sylfaen", 1, 14)); // NOI18N
        lblCerrarSesion4.setForeground(new java.awt.Color(255, 0, 0));
        lblCerrarSesion4.setText("[Cerrar Sesión]");
        lblCerrarSesion4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCerrarSesion4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCerrarSesion4MouseClicked(evt);
            }
        });

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
        jdeskAcercade.setLayer(lblCerrarSesion4, javax.swing.JLayeredPane.DEFAULT_LAYER);
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdeskAcercadeLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblCerrarSesion4)
                .addGap(39, 39, 39))
        );
        jdeskAcercadeLayout.setVerticalGroup(
            jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdeskAcercadeLayout.createSequentialGroup()
                .addComponent(lblCerrarSesion4)
                .addGap(39, 39, 39)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jdeskAcercade)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jdeskAcercade)
        );

        tabMenu.addTab("        Acerca de", new javax.swing.ImageIcon(getClass().getResource("/images/info.png")), jPanel5); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabMenu)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 719, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        tabMenu.getAccessibleContext().setAccessibleName("Galería");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblPoliticasdePrivacidadPrincipalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPoliticasdePrivacidadPrincipalMouseClicked
       if(!(internalPoliticas instanceof jifrPoliticasdePrivacidad)){
            internalPoliticas =new jifrPoliticasdePrivacidad();
        }
        centrarVentanaInternaPoliticasPrincipal(internalPoliticas);
    }//GEN-LAST:event_lblPoliticasdePrivacidadPrincipalMouseClicked

    private void lblTerminosyCondicionesPrincipalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTerminosyCondicionesPrincipalMouseClicked
        if(!(internalTerminos instanceof jifrTerminosyCondiciones)){
            internalTerminos =new jifrTerminosyCondiciones();
        }
        centrarVentanaInternaTerminosPrincipal(internalTerminos);
    }//GEN-LAST:event_lblTerminosyCondicionesPrincipalMouseClicked

    private void lblTerminosyCondicionesContactanosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTerminosyCondicionesContactanosMouseClicked
        if(!(internalTerminos instanceof jifrTerminosyCondiciones)){
            internalTerminos =new jifrTerminosyCondiciones();
        }
        centrarVentanaInternaTerminosContactanos(internalTerminos);
    }//GEN-LAST:event_lblTerminosyCondicionesContactanosMouseClicked

    private void lblPoliticasdePrivacidadContactanosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPoliticasdePrivacidadContactanosMouseClicked
         if(!(internalPoliticas instanceof jifrPoliticasdePrivacidad)){
            internalPoliticas =new jifrPoliticasdePrivacidad();
        }
        centrarVentanaInternaPoliticasContactanos(internalPoliticas);
    }//GEN-LAST:event_lblPoliticasdePrivacidadContactanosMouseClicked

    private void lblTerminosyCondicionesAcercaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTerminosyCondicionesAcercaMouseClicked
         if(!(internalTerminos instanceof jifrTerminosyCondiciones)){
            internalTerminos =new jifrTerminosyCondiciones();
        }
        centrarVentanaInternaTerminosAcerca(internalTerminos);
    }//GEN-LAST:event_lblTerminosyCondicionesAcercaMouseClicked

    private void lblPoliticasdePrivacidadacercaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPoliticasdePrivacidadacercaMouseClicked
        if(!(internalPoliticas instanceof jifrPoliticasdePrivacidad)){
            internalPoliticas =new jifrPoliticasdePrivacidad();
        }
        centrarVentanaInternaPoliticasAcerca(internalPoliticas);
    }//GEN-LAST:event_lblPoliticasdePrivacidadacercaMouseClicked

    private void lblTerminosyCondicionesUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTerminosyCondicionesUsuariosMouseClicked
         if(!(internalTerminos instanceof jifrTerminosyCondiciones)){
            internalTerminos =new jifrTerminosyCondiciones();
        }
        centrarVentanaInternaTerminosUsuarios(internalTerminos);
    }//GEN-LAST:event_lblTerminosyCondicionesUsuariosMouseClicked

    private void lblPoliticasdePrivacidadUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPoliticasdePrivacidadUsuariosMouseClicked
        if(!(internalPoliticas instanceof jifrPoliticasdePrivacidad)){
            internalPoliticas =new jifrPoliticasdePrivacidad();
        }
        centrarVentanaInternaPoliticasUsuarios(internalPoliticas);
    }//GEN-LAST:event_lblPoliticasdePrivacidadUsuariosMouseClicked

    private void lblTerminosyCondicionesGaleriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTerminosyCondicionesGaleriaMouseClicked
         if(!(internalTerminos instanceof jifrTerminosyCondiciones)){
            internalTerminos =new jifrTerminosyCondiciones();
        }
        centrarVentanaInternaTerminosGaleria(internalTerminos);
    }//GEN-LAST:event_lblTerminosyCondicionesGaleriaMouseClicked

    private void lblPoliticasdePrivacidadGaleriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPoliticasdePrivacidadGaleriaMouseClicked
        if(!(internalPoliticas instanceof jifrPoliticasdePrivacidad)){
            internalPoliticas =new jifrPoliticasdePrivacidad();
        }
        centrarVentanaInternaPoliticasGaleria(internalPoliticas);
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

    private void txtBuscarPorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarPorKeyPressed
        // TODO add your handling code here:
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
                JOptionPane.showMessageDialog(this,"Debe seleccionar un tipo de usuario");
                break;
        }
    }//GEN-LAST:event_txtBuscarPorKeyPressed

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
        centrarVentanaInternaNuevoUsuario(internalNuevoUsuario);
    }//GEN-LAST:event_btnNuevoUsuarioMouseClicked

    private void btnEliminarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarMouseClicked
        // TODO add your handling code here:
        Object [] opciones={"Aceptar","Cancelar"};
        if(!id.isEmpty()){
            int eleccion=JOptionPane.showOptionDialog(null,"Está seguro que desea eliminar","Eliminar",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,opciones,"Aceptar");
            if(eleccion==JOptionPane.YES_OPTION)  Eliminar();
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
            centrarVentanaInternaNuevoUsuario(internalNuevoUsuario);
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

    private void lblCerrarSesionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrarSesionMouseClicked
        cerrarSesion();
    }//GEN-LAST:event_lblCerrarSesionMouseClicked

    private void lblCerrarSesion1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrarSesion1MouseClicked
        cerrarSesion();
    }//GEN-LAST:event_lblCerrarSesion1MouseClicked

    private void lblCerrarSesion2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrarSesion2MouseClicked
        cerrarSesion();
    }//GEN-LAST:event_lblCerrarSesion2MouseClicked

    private void lblCerrarSesion3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrarSesion3MouseClicked
        cerrarSesion();
    }//GEN-LAST:event_lblCerrarSesion3MouseClicked

    private void lblCerrarSesion4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrarSesion4MouseClicked
        cerrarSesion();
    }//GEN-LAST:event_lblCerrarSesion4MouseClicked

    private void btnGestionCategoriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGestionCategoriaMouseClicked
        if(!(internalGestionCategoria instanceof jifrGestionCategoria)){
            internalGestionCategoria =new jifrGestionCategoria();
        }
        centrarVentanaGestionCategoria(internalGestionCategoria);
        
    }//GEN-LAST:event_btnGestionCategoriaMouseClicked

    private void btnGestionArticulosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGestionArticulosMouseClicked
         if(!(internalGestionArticulo instanceof jifrGestionArticulos)){
            internalGestionArticulo =new jifrGestionArticulos();
        }
        centrarVentanaGestionArticulo(internalGestionArticulo);
     
    }//GEN-LAST:event_btnGestionArticulosMouseClicked

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
    private javax.swing.JLabel btnActualizar;
    private javax.swing.JLabel btnBuscarUsuarios;
    private javax.swing.JLabel btnEliminar;
    private javax.swing.JLabel btnGestionArticulos;
    private javax.swing.JLabel btnGestionCategoria;
    private javax.swing.JLabel btnNuevoUsuario;
    private javax.swing.JLabel imgMuseo1;
    private javax.swing.JLabel imgMuseo2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> jcbBuscarPor;
    public javax.swing.JDesktopPane jdeskAcercade;
    public javax.swing.JDesktopPane jdeskContactanos;
    public static javax.swing.JDesktopPane jdeskGaleria;
    public javax.swing.JDesktopPane jdeskPrincipal;
    public javax.swing.JDesktopPane jdeskusuarios;
    private javax.swing.JLabel jlGaleria;
    private javax.swing.JLabel jlJJ2016;
    private javax.swing.JLabel jlJJ2017;
    private javax.swing.JLabel jlJJ2018;
    private javax.swing.JLabel jlJJ2019;
    private javax.swing.JLabel jlJJ2020;
    private javax.swing.JPanel jpPrincipal;
    private javax.swing.JTable jtUsuarios;
    private javax.swing.JLabel lblAcercaDeAnimalito;
    private javax.swing.JLabel lblAcercaDeAppVersionNetbeans;
    private javax.swing.JLabel lblAcercaDeAppVersionandroid;
    private javax.swing.JLabel lblAcercaDeLaAplicacion;
    private javax.swing.JLabel lblCerrarSesion;
    private javax.swing.JLabel lblCerrarSesion1;
    private javax.swing.JLabel lblCerrarSesion2;
    private javax.swing.JLabel lblCerrarSesion3;
    private javax.swing.JLabel lblCerrarSesion4;
    private javax.swing.JLabel lblImgContactanosJJ;
    private javax.swing.JLabel lblNuevo;
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
    private javax.swing.JTabbedPane tabMenu;
    private javax.swing.JTextField txtBuscarPor;
    // End of variables declaration//GEN-END:variables
}
