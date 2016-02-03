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
        lblUsuarioyRol.setText("Bienvenid@ " + UsuarioIngresado.parametroU+" tu rol es de " + UsuarioIngresado.parametroR);
        lblUsuarioyRol1.setText("Bienvenid@ " + UsuarioIngresado.parametroU+" tu rol es de " + UsuarioIngresado.parametroR);
        lblUsuarioyRol2.setText("Bienvenid@ " + UsuarioIngresado.parametroU+" tu rol es de " + UsuarioIngresado.parametroR);
        lblUsuarioyRol3.setText("Bienvenid@ " + UsuarioIngresado.parametroU+" tu rol es de " + UsuarioIngresado.parametroR);
        lblUsuarioyRol4.setText("Bienvenid@ " + UsuarioIngresado.parametroU+" tu rol es de " + UsuarioIngresado.parametroR);
        String Ruta=getClass().getResource("/images/Mas.png").getPath();
        Mostrar_Visualizador(btnNuevoUsuario, Ruta);
        String Ruta1=getClass().getResource("/images/actualizar.png").getPath();
        Mostrar_Visualizador(btnActualizar, Ruta1);
        String Ruta2=getClass().getResource("/images/Eliminar.png").getPath();
        Mostrar_Visualizador(btnEliminar, Ruta2);
        String Ruta3=getClass().getResource("/images/search.png").getPath();
        Mostrar_Visualizador(btnBuscarUsuarios, Ruta3);
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
    
    public void centrarVentanaInterna ( jifrTerminosyCondiciones internalFrame){
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
    
    
    
    public void centrarVentanaInternaPoliticas ( jifrPoliticasdePrivacidad internalFrameP){
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
    
    
     void SeleccionarItemTablaU(java.awt.event.MouseEvent evt){
        DefaultTableModel modelo=(DefaultTableModel) jtUsuarios.getModel();
        id=String.valueOf(modelo.getValueAt(jtUsuarios.getSelectedRow(),0));
        rol=String.valueOf(modelo.getValueAt(jtUsuarios.getSelectedRow(),1));
        estado=String.valueOf(modelo.getValueAt(jtUsuarios.getSelectedRow(),6));
        lblNuevo.setText(id);
    }
    
     void Politicas(){
        if(!(internalPoliticas instanceof jifrPoliticasdePrivacidad)){
            internalPoliticas =new jifrPoliticasdePrivacidad();
            //lblPoliticasdePrivacidad.setEnabled(false);
        }
        centrarVentanaInternaPoliticas(internalPoliticas);
     }
     
     void Terminos(){
        if(!(internalTerminos instanceof jifrTerminosyCondiciones)){
            internalTerminos =new jifrTerminosyCondiciones();
        }
        centrarVentanaInterna(internalTerminos);
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
        lblTerminosyCondiciones = new javax.swing.JLabel();
        lblPoliticasdePrivacidad = new javax.swing.JLabel();
        lblUsuarioyRol = new javax.swing.JLabel();
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
        lblTerminosyCondiciones3 = new javax.swing.JLabel();
        lblPoliticasdePrivacidad3 = new javax.swing.JLabel();
        lblUsuarioyRol3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jlUsuarios = new javax.swing.JLabel();
        lblNuevo = new javax.swing.JLabel();
        btnNuevoUsuario = new javax.swing.JLabel();
        btnActualizar = new javax.swing.JLabel();
        btnEliminar = new javax.swing.JLabel();
        btnBuscarUsuarios = new javax.swing.JLabel();
        jcbBuscarPor = new javax.swing.JComboBox<>();
        txtBuscarPor = new javax.swing.JTextField();
        rbtnActivo = new javax.swing.JRadioButton();
        rbtnInactivo = new javax.swing.JRadioButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtUsuarios = new javax.swing.JTable();
        lblCerrarSesion1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jdeskGaleria = new javax.swing.JDesktopPane();
        jPanel10 = new javax.swing.JPanel();
        jlJJ2020 = new javax.swing.JLabel();
        lblTerminosyCondiciones4 = new javax.swing.JLabel();
        lblPoliticasdePrivacidad4 = new javax.swing.JLabel();
        lblUsuarioyRol4 = new javax.swing.JLabel();
        lblCerrarSesion2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jdeskContactanos = new javax.swing.JDesktopPane();
        jPanel7 = new javax.swing.JPanel();
        jlJJ2017 = new javax.swing.JLabel();
        lblTerminosyCondiciones1 = new javax.swing.JLabel();
        lblPoliticasdePrivacidad1 = new javax.swing.JLabel();
        lblUsuarioyRol1 = new javax.swing.JLabel();
        jPanelJess = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        lblImgJJ = new javax.swing.JLabel();
        jPanelJoss = new javax.swing.JPanel();
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
        lblCerrarSesion3 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jdeskAcercade = new javax.swing.JDesktopPane();
        jPanel8 = new javax.swing.JPanel();
        jlJJ2018 = new javax.swing.JLabel();
        lblTerminosyCondiciones2 = new javax.swing.JLabel();
        lblPoliticasdePrivacidad2 = new javax.swing.JLabel();
        lblUsuarioyRol2 = new javax.swing.JLabel();
        lblCerrarSesion4 = new javax.swing.JLabel();
        losQr = new javax.swing.JLabel();
        lblAcercaDe = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        lblAcercaDeAppImg = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        losQr3 = new javax.swing.JLabel();
        losQr2 = new javax.swing.JLabel();
        lblAcercaDeAppVersion = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        lblAcercaDeAppVersion1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        tabMenu.setBackground(new java.awt.Color(0, 0, 0));
        tabMenu.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        tabMenu.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        jpPrincipal.setBackground(new java.awt.Color(204, 204, 204));

        jPanel6.setBackground(new java.awt.Color(0, 0, 0));

        jlJJ2016.setForeground(new java.awt.Color(255, 255, 255));
        jlJJ2016.setText("JJ 2016 Reservados  todos  los  derechos . ");

        lblTerminosyCondiciones.setForeground(new java.awt.Color(255, 51, 0));
        lblTerminosyCondiciones.setText("Terminos y Condiciones");
        lblTerminosyCondiciones.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblTerminosyCondiciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTerminosyCondicionesMouseClicked(evt);
            }
        });

        lblPoliticasdePrivacidad.setForeground(new java.awt.Color(153, 204, 255));
        lblPoliticasdePrivacidad.setText("|  Políticas de Privacidad");
        lblPoliticasdePrivacidad.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblPoliticasdePrivacidad.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPoliticasdePrivacidadMouseClicked(evt);
            }
        });

        lblUsuarioyRol.setForeground(new java.awt.Color(255, 0, 0));
        lblUsuarioyRol.setText("jLabel1");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(jlJJ2016)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTerminosyCondiciones)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblPoliticasdePrivacidad)
                .addGap(132, 132, 132)
                .addComponent(lblUsuarioyRol, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                            .addComponent(lblTerminosyCondiciones)
                            .addComponent(lblPoliticasdePrivacidad))
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(lblUsuarioyRol, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        lblCerrarSesion.setForeground(new java.awt.Color(153, 0, 0));
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

        lblTerminosyCondiciones3.setForeground(new java.awt.Color(255, 51, 0));
        lblTerminosyCondiciones3.setText("Terminos y Condiciones");
        lblTerminosyCondiciones3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblTerminosyCondiciones3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTerminosyCondiciones3MouseClicked(evt);
            }
        });

        lblPoliticasdePrivacidad3.setForeground(new java.awt.Color(153, 204, 255));
        lblPoliticasdePrivacidad3.setText("|  Políticas de Privacidad");
        lblPoliticasdePrivacidad3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblPoliticasdePrivacidad3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPoliticasdePrivacidad3MouseClicked(evt);
            }
        });

        lblUsuarioyRol3.setForeground(new java.awt.Color(255, 0, 0));
        lblUsuarioyRol3.setText("jLabel1");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(jlJJ2019)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTerminosyCondiciones3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblPoliticasdePrivacidad3)
                .addGap(132, 132, 132)
                .addComponent(lblUsuarioyRol3, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(387, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlJJ2019)
                            .addComponent(lblTerminosyCondiciones3)
                            .addComponent(lblPoliticasdePrivacidad3))
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addComponent(lblUsuarioyRol3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(2004, 664));
        jPanel1.setMinimumSize(new java.awt.Dimension(2004, 664));

        jlUsuarios.setFont(new java.awt.Font("Nirmala UI", 1, 18)); // NOI18N
        jlUsuarios.setText("Usuarios");

        lblNuevo.setText("Nuevo");

        btnNuevoUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/mas.jpg"))); // NOI18N
        btnNuevoUsuario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNuevoUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNuevoUsuarioMouseClicked(evt);
            }
        });

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(122, 122, 122)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(lblNuevo))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnNuevoUsuario)
                        .addGap(29, 29, 29)
                        .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(btnBuscarUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtBuscarPor, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jcbBuscarPor, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(rbtnActivo)
                                .addGap(18, 18, 18)
                                .addComponent(rbtnInactivo))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1901, 1901, 1901)
                        .addComponent(jlUsuarios))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblNuevo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnNuevoUsuario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnActualizar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEliminar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBuscarUsuarios, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jcbBuscarPor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(rbtnActivo)
                                .addComponent(rbtnInactivo)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBuscarPor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(85, 85, 85)
                .addComponent(jlUsuarios)
                .addContainerGap(446, Short.MAX_VALUE))
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
        lblCerrarSesion1.setForeground(new java.awt.Color(153, 0, 0));
        lblCerrarSesion1.setText("[Cerrar Sesión]");
        lblCerrarSesion1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCerrarSesion1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCerrarSesion1MouseClicked(evt);
            }
        });

        jdeskusuarios.setLayer(jPanel9, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(jPanel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskusuarios.setLayer(lblCerrarSesion1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jdeskusuariosLayout = new javax.swing.GroupLayout(jdeskusuarios);
        jdeskusuarios.setLayout(jdeskusuariosLayout);
        jdeskusuariosLayout.setHorizontalGroup(
            jdeskusuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jdeskusuariosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 956, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdeskusuariosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblCerrarSesion1)
                .addGap(476, 476, 476))
            .addGroup(jdeskusuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jdeskusuariosLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1405, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jdeskusuariosLayout.setVerticalGroup(
            jdeskusuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdeskusuariosLayout.createSequentialGroup()
                .addComponent(lblCerrarSesion1)
                .addGap(142, 142, 142)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jdeskusuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jdeskusuariosLayout.createSequentialGroup()
                    .addGap(37, 37, 37)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 639, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(38, Short.MAX_VALUE)))
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

        jPanel10.setBackground(new java.awt.Color(0, 0, 0));

        jlJJ2020.setForeground(new java.awt.Color(255, 255, 255));
        jlJJ2020.setText("JJ 2016 Reservados  todos  los  derechos . ");

        lblTerminosyCondiciones4.setForeground(new java.awt.Color(255, 51, 0));
        lblTerminosyCondiciones4.setText("Terminos y Condiciones");
        lblTerminosyCondiciones4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblTerminosyCondiciones4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTerminosyCondiciones4MouseClicked(evt);
            }
        });

        lblPoliticasdePrivacidad4.setForeground(new java.awt.Color(153, 204, 255));
        lblPoliticasdePrivacidad4.setText("|  Políticas de Privacidad");
        lblPoliticasdePrivacidad4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblPoliticasdePrivacidad4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPoliticasdePrivacidad4MouseClicked(evt);
            }
        });

        lblUsuarioyRol4.setForeground(new java.awt.Color(255, 0, 0));
        lblUsuarioyRol4.setText("jLabel1");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(jlJJ2020)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTerminosyCondiciones4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblPoliticasdePrivacidad4)
                .addGap(132, 132, 132)
                .addComponent(lblUsuarioyRol4, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                            .addComponent(lblTerminosyCondiciones4)
                            .addComponent(lblPoliticasdePrivacidad4))
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addComponent(lblUsuarioyRol4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        lblCerrarSesion2.setFont(new java.awt.Font("Sylfaen", 1, 14)); // NOI18N
        lblCerrarSesion2.setForeground(new java.awt.Color(153, 0, 0));
        lblCerrarSesion2.setText("[Cerrar Sesión]");
        lblCerrarSesion2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCerrarSesion2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCerrarSesion2MouseClicked(evt);
            }
        });

        jdeskGaleria.setLayer(jPanel10, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskGaleria.setLayer(lblCerrarSesion2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jdeskGaleriaLayout = new javax.swing.GroupLayout(jdeskGaleria);
        jdeskGaleria.setLayout(jdeskGaleriaLayout);
        jdeskGaleriaLayout.setHorizontalGroup(
            jdeskGaleriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdeskGaleriaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblCerrarSesion2)
                .addGap(116, 116, 116))
        );
        jdeskGaleriaLayout.setVerticalGroup(
            jdeskGaleriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdeskGaleriaLayout.createSequentialGroup()
                .addComponent(lblCerrarSesion2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 627, Short.MAX_VALUE)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jdeskGaleria)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jdeskGaleria)
        );

        tabMenu.addTab("       Galería", new javax.swing.ImageIcon(getClass().getResource("/images/galeria.png")), jPanel3); // NOI18N

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));

        jdeskContactanos.setBackground(new java.awt.Color(204, 204, 255));

        jPanel7.setBackground(new java.awt.Color(0, 0, 0));

        jlJJ2017.setForeground(new java.awt.Color(255, 255, 255));
        jlJJ2017.setText("JJ 2016 Reservados  todos  los  derechos . ");

        lblTerminosyCondiciones1.setForeground(new java.awt.Color(255, 51, 0));
        lblTerminosyCondiciones1.setText("Terminos y Condiciones");
        lblTerminosyCondiciones1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblTerminosyCondiciones1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTerminosyCondiciones1MouseClicked(evt);
            }
        });

        lblPoliticasdePrivacidad1.setForeground(new java.awt.Color(153, 204, 255));
        lblPoliticasdePrivacidad1.setText("|  Políticas de Privacidad");
        lblPoliticasdePrivacidad1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblPoliticasdePrivacidad1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPoliticasdePrivacidad1MouseClicked(evt);
            }
        });

        lblUsuarioyRol1.setForeground(new java.awt.Color(255, 0, 0));
        lblUsuarioyRol1.setText("jLabel1");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(jlJJ2017)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTerminosyCondiciones1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblPoliticasdePrivacidad1)
                .addGap(132, 132, 132)
                .addComponent(lblUsuarioyRol1, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(132, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlJJ2017)
                            .addComponent(lblTerminosyCondiciones1)
                            .addComponent(lblPoliticasdePrivacidad1))
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(lblUsuarioyRol1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        jPanelJess.setBackground(new java.awt.Color(204, 204, 255));
        jPanelJess.setForeground(new java.awt.Color(204, 255, 255));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel7.setText("Nombre:");

        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setText("Josselyn Karina Carrillo Betancourt");

        jLabel9.setForeground(new java.awt.Color(51, 51, 51));
        jLabel9.setText("joselin@hotmail.com");

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel10.setText("Correo:");

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel11.setText("Dirección");

        jLabel12.setForeground(new java.awt.Color(51, 51, 51));
        jLabel12.setText("Latacunga");

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel13.setText("Teléfono");

        jLabel14.setForeground(new java.awt.Color(51, 51, 51));
        jLabel14.setText("09835656545");

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel15.setText("Blog");

        jLabel16.setForeground(new java.awt.Color(51, 51, 51));
        jLabel16.setText("jcarrillo.blogspot.com");

        javax.swing.GroupLayout jPanelJessLayout = new javax.swing.GroupLayout(jPanelJess);
        jPanelJess.setLayout(jPanelJessLayout);
        jPanelJessLayout.setHorizontalGroup(
            jPanelJessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelJessLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelJessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jLabel7)
                    .addComponent(jLabel10)
                    .addComponent(jLabel13)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelJessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelJessLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanelJessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(75, 75, 75))
                    .addGroup(jPanelJessLayout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(36, 36, 36))
        );
        jPanelJessLayout.setVerticalGroup(
            jPanelJessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelJessLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(jPanelJessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(26, 26, 26)
                .addGroup(jPanelJessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10))
                .addGap(26, 26, 26)
                .addGroup(jPanelJessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11))
                .addGap(26, 26, 26)
                .addGroup(jPanelJessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13))
                .addGap(27, 27, 27)
                .addGroup(jPanelJessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15))
                .addGap(185, 185, 185))
        );

        lblImgJJ.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/nosotras.png"))); // NOI18N

        jPanelJoss.setBackground(new java.awt.Color(204, 204, 255));

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

        javax.swing.GroupLayout jPanelJossLayout = new javax.swing.GroupLayout(jPanelJoss);
        jPanelJoss.setLayout(jPanelJossLayout);
        jPanelJossLayout.setHorizontalGroup(
            jPanelJossLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelJossLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelJossLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addComponent(jLabel17)
                    .addComponent(jLabel19)
                    .addComponent(jLabel23)
                    .addComponent(jLabel25))
                .addGap(18, 18, 18)
                .addGroup(jPanelJossLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelJossLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanelJossLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE))
                        .addGap(75, 75, 75))
                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelJossLayout.createSequentialGroup()
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelJossLayout.setVerticalGroup(
            jPanelJossLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelJossLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(jPanelJossLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(26, 26, 26)
                .addGroup(jPanelJossLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel19))
                .addGap(26, 26, 26)
                .addGroup(jPanelJossLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel21))
                .addGap(26, 26, 26)
                .addGroup(jPanelJossLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel23))
                .addGap(27, 27, 27)
                .addGroup(jPanelJossLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel25))
                .addGap(185, 185, 185))
        );

        lblCerrarSesion3.setFont(new java.awt.Font("Sylfaen", 1, 14)); // NOI18N
        lblCerrarSesion3.setForeground(new java.awt.Color(153, 0, 0));
        lblCerrarSesion3.setText("[Cerrar Sesión]");
        lblCerrarSesion3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCerrarSesion3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCerrarSesion3MouseClicked(evt);
            }
        });

        jdeskContactanos.setLayer(jPanel7, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(jPanelJess, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(lblImgJJ, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(jPanelJoss, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskContactanos.setLayer(lblCerrarSesion3, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jdeskContactanosLayout = new javax.swing.GroupLayout(jdeskContactanos);
        jdeskContactanos.setLayout(jdeskContactanosLayout);
        jdeskContactanosLayout.setHorizontalGroup(
            jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jdeskContactanosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelJess, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblImgJJ, javax.swing.GroupLayout.PREFERRED_SIZE, 466, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanelJoss, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdeskContactanosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblCerrarSesion3)
                .addGap(252, 252, 252))
        );
        jdeskContactanosLayout.setVerticalGroup(
            jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdeskContactanosLayout.createSequentialGroup()
                .addComponent(lblCerrarSesion3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 132, Short.MAX_VALUE)
                .addGroup(jdeskContactanosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblImgJJ, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelJoss, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelJess, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(81, 81, 81)
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

        lblTerminosyCondiciones2.setForeground(new java.awt.Color(255, 51, 0));
        lblTerminosyCondiciones2.setText("Terminos y Condiciones");
        lblTerminosyCondiciones2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblTerminosyCondiciones2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTerminosyCondiciones2MouseClicked(evt);
            }
        });

        lblPoliticasdePrivacidad2.setForeground(new java.awt.Color(153, 204, 255));
        lblPoliticasdePrivacidad2.setText("|  Políticas de Privacidad");
        lblPoliticasdePrivacidad2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblPoliticasdePrivacidad2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPoliticasdePrivacidad2MouseClicked(evt);
            }
        });

        lblUsuarioyRol2.setForeground(new java.awt.Color(255, 0, 0));
        lblUsuarioyRol2.setText("jLabel1");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(jlJJ2018)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTerminosyCondiciones2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblPoliticasdePrivacidad2)
                .addGap(132, 132, 132)
                .addComponent(lblUsuarioyRol2, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                            .addComponent(lblTerminosyCondiciones2)
                            .addComponent(lblPoliticasdePrivacidad2))
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(lblUsuarioyRol2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        lblCerrarSesion4.setFont(new java.awt.Font("Sylfaen", 1, 14)); // NOI18N
        lblCerrarSesion4.setForeground(new java.awt.Color(153, 0, 0));
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

        lblAcercaDe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/acercade.jpg"))); // NOI18N

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

        lblAcercaDeAppImg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/jjqr.png"))); // NOI18N

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

        lblAcercaDeAppVersion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/version.png"))); // NOI18N

        jLabel41.setFont(new java.awt.Font("Arial Black", 0, 11)); // NOI18N
        jLabel41.setText("LectorJJQR");

        jLabel42.setFont(new java.awt.Font("Arial Black", 0, 11)); // NOI18N
        jLabel42.setText("Product Version: Eclipse ");

        jLabel43.setFont(new java.awt.Font("Arial Black", 0, 11)); // NOI18N
        jLabel43.setText("API: 14  ");

        jLabel45.setFont(new java.awt.Font("Arial Black", 0, 11)); // NOI18N
        jLabel45.setText("Product Version: NetBeans ");

        jLabel44.setFont(new java.awt.Font("Arial Black", 0, 11)); // NOI18N
        jLabel44.setText("AndroidVersion: 4.0");

        jLabel46.setFont(new java.awt.Font("Arial Black", 0, 11)); // NOI18N
        jLabel46.setText("IDE 8.0.2 (Build 201411181905) ");

        lblAcercaDeAppVersion1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/androidstudio1.png"))); // NOI18N

        jdeskAcercade.setLayer(jPanel8, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(lblCerrarSesion4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(losQr, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(lblAcercaDe, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel27, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel28, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel29, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel30, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel31, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel32, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(lblAcercaDeAppImg, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel33, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel34, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel35, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel36, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel37, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel38, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(losQr3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(losQr2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(lblAcercaDeAppVersion, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel41, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel42, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel43, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel45, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel44, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(jLabel46, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jdeskAcercade.setLayer(lblAcercaDeAppVersion1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jdeskAcercadeLayout = new javax.swing.GroupLayout(jdeskAcercade);
        jdeskAcercade.setLayout(jdeskAcercadeLayout);
        jdeskAcercadeLayout.setHorizontalGroup(
            jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jdeskAcercadeLayout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jdeskAcercadeLayout.createSequentialGroup()
                        .addGroup(jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblAcercaDe, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel27)
                            .addComponent(jLabel28)
                            .addComponent(jLabel29)
                            .addComponent(jLabel30)
                            .addComponent(jLabel31)
                            .addComponent(jLabel32))
                        .addGap(62, 62, 62)
                        .addGroup(jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblAcercaDeAppImg, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel33)
                                .addComponent(jLabel34)
                                .addComponent(jLabel35)
                                .addComponent(jLabel36)
                                .addComponent(jLabel37)
                                .addComponent(jLabel38)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdeskAcercadeLayout.createSequentialGroup()
                                .addGroup(jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel41)
                                    .addComponent(jLabel42)
                                    .addComponent(jLabel43)
                                    .addComponent(jLabel45)
                                    .addComponent(jLabel44)
                                    .addComponent(jLabel46))
                                .addGap(56, 56, 56))
                            .addComponent(lblAcercaDeAppVersion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblAcercaDeAppVersion1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jdeskAcercadeLayout.createSequentialGroup()
                        .addComponent(losQr)
                        .addGap(274, 274, 274)
                        .addComponent(losQr3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(losQr2)))
                .addGap(142, 142, 142))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdeskAcercadeLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblCerrarSesion4)
                .addGap(199, 199, 199))
        );
        jdeskAcercadeLayout.setVerticalGroup(
            jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdeskAcercadeLayout.createSequentialGroup()
                .addComponent(lblCerrarSesion4)
                .addGap(39, 39, 39)
                .addGroup(jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(losQr3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(losQr)
                        .addComponent(losQr2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAcercaDe, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAcercaDeAppImg, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jdeskAcercadeLayout.createSequentialGroup()
                        .addComponent(lblAcercaDeAppVersion)
                        .addGap(18, 18, 18)
                        .addComponent(lblAcercaDeAppVersion1)))
                .addGap(39, 39, 39)
                .addGroup(jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jdeskAcercadeLayout.createSequentialGroup()
                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jdeskAcercadeLayout.createSequentialGroup()
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jdeskAcercadeLayout.createSequentialGroup()
                        .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jdeskAcercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jdeskAcercadeLayout.createSequentialGroup()
                                .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27))
                            .addComponent(jLabel45, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
            .addComponent(tabMenu, javax.swing.GroupLayout.DEFAULT_SIZE, 1319, Short.MAX_VALUE)
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

    private void lblPoliticasdePrivacidadMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPoliticasdePrivacidadMouseClicked
        Politicas();
    }//GEN-LAST:event_lblPoliticasdePrivacidadMouseClicked

    private void lblTerminosyCondicionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTerminosyCondicionesMouseClicked
        Terminos();
    }//GEN-LAST:event_lblTerminosyCondicionesMouseClicked

    private void lblTerminosyCondiciones1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTerminosyCondiciones1MouseClicked
       Terminos();
    }//GEN-LAST:event_lblTerminosyCondiciones1MouseClicked

    private void lblPoliticasdePrivacidad1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPoliticasdePrivacidad1MouseClicked
        Politicas();
    }//GEN-LAST:event_lblPoliticasdePrivacidad1MouseClicked

    private void lblTerminosyCondiciones2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTerminosyCondiciones2MouseClicked
        Terminos();
    }//GEN-LAST:event_lblTerminosyCondiciones2MouseClicked

    private void lblPoliticasdePrivacidad2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPoliticasdePrivacidad2MouseClicked
        Politicas();
    }//GEN-LAST:event_lblPoliticasdePrivacidad2MouseClicked

    private void lblTerminosyCondiciones3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTerminosyCondiciones3MouseClicked
        Terminos();
    }//GEN-LAST:event_lblTerminosyCondiciones3MouseClicked

    private void lblPoliticasdePrivacidad3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPoliticasdePrivacidad3MouseClicked
        Politicas();
    }//GEN-LAST:event_lblPoliticasdePrivacidad3MouseClicked

    private void lblTerminosyCondiciones4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTerminosyCondiciones4MouseClicked
        Terminos();
    }//GEN-LAST:event_lblTerminosyCondiciones4MouseClicked

    private void lblPoliticasdePrivacidad4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPoliticasdePrivacidad4MouseClicked
        Politicas();
    }//GEN-LAST:event_lblPoliticasdePrivacidad4MouseClicked

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
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
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
    private javax.swing.JPanel jPanelJess;
    private javax.swing.JPanel jPanelJoss;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> jcbBuscarPor;
    public javax.swing.JDesktopPane jdeskAcercade;
    public javax.swing.JDesktopPane jdeskContactanos;
    public javax.swing.JDesktopPane jdeskGaleria;
    public javax.swing.JDesktopPane jdeskPrincipal;
    public javax.swing.JDesktopPane jdeskusuarios;
    private javax.swing.JLabel jlGaleria;
    private javax.swing.JLabel jlJJ2016;
    private javax.swing.JLabel jlJJ2017;
    private javax.swing.JLabel jlJJ2018;
    private javax.swing.JLabel jlJJ2019;
    private javax.swing.JLabel jlJJ2020;
    private javax.swing.JLabel jlUsuarios;
    private javax.swing.JPanel jpPrincipal;
    private javax.swing.JTable jtUsuarios;
    private javax.swing.JLabel lblAcercaDe;
    private javax.swing.JLabel lblAcercaDeAppImg;
    private javax.swing.JLabel lblAcercaDeAppVersion;
    private javax.swing.JLabel lblAcercaDeAppVersion1;
    private javax.swing.JLabel lblCerrarSesion;
    private javax.swing.JLabel lblCerrarSesion1;
    private javax.swing.JLabel lblCerrarSesion2;
    private javax.swing.JLabel lblCerrarSesion3;
    private javax.swing.JLabel lblCerrarSesion4;
    private javax.swing.JLabel lblImgJJ;
    private javax.swing.JLabel lblNuevo;
    private javax.swing.JLabel lblPoliticasdePrivacidad;
    private javax.swing.JLabel lblPoliticasdePrivacidad1;
    private javax.swing.JLabel lblPoliticasdePrivacidad2;
    private javax.swing.JLabel lblPoliticasdePrivacidad3;
    private javax.swing.JLabel lblPoliticasdePrivacidad4;
    private javax.swing.JLabel lblTerminosyCondiciones;
    private javax.swing.JLabel lblTerminosyCondiciones1;
    private javax.swing.JLabel lblTerminosyCondiciones2;
    private javax.swing.JLabel lblTerminosyCondiciones3;
    private javax.swing.JLabel lblTerminosyCondiciones4;
    private javax.swing.JLabel lblUsuarioyRol;
    private javax.swing.JLabel lblUsuarioyRol1;
    private javax.swing.JLabel lblUsuarioyRol2;
    private javax.swing.JLabel lblUsuarioyRol3;
    private javax.swing.JLabel lblUsuarioyRol4;
    private javax.swing.JLabel losQr;
    private javax.swing.JLabel losQr2;
    private javax.swing.JLabel losQr3;
    private javax.swing.JRadioButton rbtnActivo;
    private javax.swing.JRadioButton rbtnInactivo;
    private javax.swing.JTabbedPane tabMenu;
    private javax.swing.JTextField txtBuscarPor;
    // End of variables declaration//GEN-END:variables
}
