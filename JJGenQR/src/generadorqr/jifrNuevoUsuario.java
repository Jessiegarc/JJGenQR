/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generadorqr;

import Modelos.ItemSeleccionado;
import Modelos.Validate;
import db.mysql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author Jess
 */
public class jifrNuevoUsuario extends javax.swing.JInternalFrame {
Connection conn;
Statement sent;
String accion;
String cedula,valCorreo,ok="\u2714";
Boolean tipoUsuario, estadoUsuario;
Validate val=new Validate();
    /**
     * Creates new form jifrNuevoUsuario
     */
    public jifrNuevoUsuario() {
        initComponents();
    }
    
    void IniciarComponentes(){
        if(this.isDisplayable()){
            //this.setLocationRelativeTo(null);   //Se ordena que la interfaz se ubique en el centro de la pantalla
            conn = mysql.getConnect();
            lblIdUsuario.setVisible(false);
            accion=ItemSeleccionado.accionBoton;
            btnGuardarNuevoUsuario.setText(accion);
            try{
                //Muestra los usuarios existentes en la base de datos
                if(accion.contains("Actualizar")){
                    jlContraseña.setVisible(false);
                    jlContraseña1.setVisible(false);
                    txtContraseñaUsuario.setVisible(false);
                    txtRepetirContraseñaUsuario.setVisible(false);
                    lblNuevoUsuario.setText(accion + "Usuario");
                    lblIdUsuario.setVisible(true);
                    lblIdUsuario.setText("ID del Usuario: \t\t" + ItemSeleccionado.idUsuario);
                    String SQLTU ="SELECT * FROM usuarios WHERE IDUSUARIO = " + ItemSeleccionado.idUsuario; 
                    sent = conn.createStatement();
                    ResultSet rs = sent.executeQuery(SQLTU);
                    rs.next();
                    txtNombreUsuario.setText(rs.getString("NOMBRESUSUARIO"));
                    txtApellidoUsuario.setText(rs.getString("APELLIDOSUSUARIO"));
                    jcbTipodeUsuario.setSelectedItem(rs.getString("TIPOUSUARIO"));
                    txtCedula.setText(rs.getString("CEDULAUSUARIO"));
                    txtCorreo.setText(rs.getString("CORREOUSUARIO"));
                    cedula=rs.getString("CEDULAUSUARIO");
                    if(!Validate.validadorDeCedula(cedula)) lblCedulaSinGuion.setText("Cédula incorrecta");
                    else lblCedulaSinGuion.setText(ok);
                    valCorreo=rs.getString("CORREOUSUARIO");
                    if(!Validate.validateEmail(valCorreo)) lblValidadorCorreo.setText("Correo incorrecto");
                    else lblValidadorCorreo.setText(ok);
                    if(rs.getBoolean("ESTADOUSUARIO") == true) jcbEstadoUsuario.setSelectedItem("Activo");
                    else jcbEstadoUsuario.setSelectedItem("Inactivo");
                    rs.close();
                } else Limpiar();
            }
            catch(Exception e){

            }
        }
    }
    
    void ValidarLetras(java.awt.event.KeyEvent evt){
        int k = (int) evt.getKeyChar();
        if (k > 47 && k < 58) {
            evt.setKeyChar((char) evt.VK_CLEAR);
        }
    }
    
    void validacionCorreo(){
        boolean status=Validate.validateEmail(txtCorreo.getText());
        if(status) lblCorreo.setText("Email Valid");
        else JOptionPane.showMessageDialog(rootPane, "Email inValid");
    }

    void Limpiar(){
        lblIdUsuario.setText("");
        txtNombreUsuario.setText("");
        txtApellidoUsuario.setText("");
        jcbTipodeUsuario.setSelectedIndex(0);
        txtCedula.setText("");
        txtCorreo.setText("");
        jcbEstadoUsuario.setSelectedIndex(0);
        txtContraseñaUsuario.setText("");
        txtRepetirContraseñaUsuario.setText("");
        jlContraseña.setVisible(true);
        jlContraseña1.setVisible(true);
        txtContraseñaUsuario.setVisible(true);
        txtRepetirContraseñaUsuario.setVisible(true);
        lblNuevoUsuario.setText("Nuevo Usuario");
        txtNombreUsuario.requestFocus();
    }
    
    void Guardar(){
        try {
            //Ingreso en nuevo usuario
            if(btnGuardarNuevoUsuario.getText().contains("Guardar")){
                if(jcbTipodeUsuario.getSelectedIndex()!=0 && jcbEstadoUsuario.getSelectedIndex()!=0 ){
                    if (txtNombreUsuario.getText().trim().isEmpty() || txtApellidoUsuario.getText().trim().isEmpty()|| txtContraseñaUsuario.getText().trim().isEmpty()|| txtRepetirContraseñaUsuario.getText().trim().isEmpty()|| txtCedula.getText().trim().isEmpty()|| txtCorreo.getText().trim().isEmpty() ){
                        JOptionPane.showMessageDialog(null, "Ingrese Los Campos Obligatorios");
                        return;
                    }
                    if(txtCedula.getText().length()<10){ 
                        JOptionPane.showMessageDialog(rootPane,"La cedula debe contener 10 digitos");
                        txtCedula.requestFocus();
                        return;
                    }
                    if(!Validate.validadorDeCedula(cedula)){ 
                        JOptionPane.showMessageDialog(this,"Verifique..! La cédula es incorrecta");
                        txtCedula.requestFocus();
                        return;
                    }
                    if(!Validate.validateEmail(valCorreo)) {
                        JOptionPane.showMessageDialog(this,"Verifique..! El correo es incorrecto");
                        txtCorreo.requestFocus();
                        return;
                    }
                    if(txtContraseñaUsuario.getText().length()<8) JOptionPane.showMessageDialog(rootPane,"La contraseña debe contener al menos 8 caracteres");
                    else if(txtContraseñaUsuario.getText().equals(txtRepetirContraseñaUsuario.getText().trim())){
                        String SQL = "INSERT INTO usuarios(TIPOUSUARIO, NOMBRESUSUARIO, APELLIDOSUSUARIO, CONTRASENAUSUARIO, "
                                + "CEDULAUSUARIO, CORREOUSUARIO, ESTADOUSUARIO)"
                                      + " VALUES(?,?,?,?,?,?,?)";
                        PreparedStatement ps = conn.prepareStatement(SQL);
                        ps.setString(1, ItemSeleccionado.rol);
                        ps.setString(2, txtNombreUsuario.getText());
                        ps.setString(3, txtApellidoUsuario.getText());
                        ps.setString(4, txtContraseñaUsuario.getText());
                        ps.setString(5, txtCedula.getText());
                        ps.setString(6, txtCorreo.getText());
                        ps.setBoolean(7, ItemSeleccionado.estado);
                        int n = ps.executeUpdate();
                        if (n > 0) {
                            JOptionPane.showMessageDialog(null, "Nuevo Usuario creado Correctamente");
                            dispose();
                        }
                    }
                    else JOptionPane.showMessageDialog(null, "La contraseña debe coincidir");
                } else JOptionPane.showMessageDialog(null, "Faltan datos por validar");
            } else {
                if(jcbTipodeUsuario.getSelectedIndex()!=0 && jcbEstadoUsuario.getSelectedIndex()!=0 ){
                    if(txtCedula.getText().length()<10){ 
                        JOptionPane.showMessageDialog(rootPane,"La cedula debe contener 10 digitos");
                        return;
                    }
                    if(!Validate.validadorDeCedula(cedula)){ 
                        JOptionPane.showMessageDialog(this,"Verifique..! La cédula es incorrecta");
                        return;
                    }
                    if(!Validate.validateEmail(valCorreo)) {
                         JOptionPane.showMessageDialog(this,"Verifique..! El correo es incorrecto");
                        return;
                     }
                    if (txtNombreUsuario.getText().trim().isEmpty() || txtApellidoUsuario.getText().trim().isEmpty() || txtCedula.getText().trim().isEmpty() || txtCorreo.getText().trim().isEmpty() )
                        JOptionPane.showMessageDialog(null, "Ingrese Los Campos Obligatorios");
                    else{
                        String SQL = "UPDATE usuarios SET TIPOUSUARIO = ?, NOMBRESUSUARIO = ?, APELLIDOSUSUARIO = ?, "
                            + "CEDULAUSUARIO = ?, CORREOUSUARIO = ?, ESTADOUSUARIO = ? WHERE IDUSUARIO = " + ItemSeleccionado.idUsuario;
                        PreparedStatement ps = conn.prepareStatement(SQL);
                        ps.setString(1, ItemSeleccionado.rol);
                        ps.setString(2, txtNombreUsuario.getText());
                        ps.setString(3, txtApellidoUsuario.getText());
                        ps.setString(4, txtCedula.getText());
                        ps.setString(5, txtCorreo.getText());
                        ps.setBoolean(6, ItemSeleccionado.estado);
                        int n = ps.executeUpdate();
                        if (n > 0) {
                            JOptionPane.showMessageDialog(null, "Usuario actualizado Correctamente");
                            dispose();
                        }
                    }
                } else JOptionPane.showMessageDialog(null, "Faltan datos por validar");
            }
        } catch (SQLException e) {
            JOptionPane.showConfirmDialog(null, "Error: " + e.getMessage());
            //System.out.println();
        }
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
        lblNuevoUsuario = new javax.swing.JLabel();
        jlNombreUsuario = new javax.swing.JLabel();
        jlApellidoUsuario = new javax.swing.JLabel();
        jlEstado = new javax.swing.JLabel();
        jlTipoUsuario = new javax.swing.JLabel();
        txtNombreUsuario = new javax.swing.JTextField();
        txtApellidoUsuario = new javax.swing.JTextField();
        jcbEstadoUsuario = new javax.swing.JComboBox();
        jcbTipodeUsuario = new javax.swing.JComboBox();
        jlContraseña = new javax.swing.JLabel();
        txtContraseñaUsuario = new javax.swing.JPasswordField();
        btnGuardarNuevoUsuario = new javax.swing.JButton();
        btnCancelarNuevoUsuario = new javax.swing.JButton();
        jlContraseña1 = new javax.swing.JLabel();
        txtRepetirContraseñaUsuario = new javax.swing.JPasswordField();
        lblCedula = new javax.swing.JLabel();
        txtCedula = new javax.swing.JTextField();
        lblCorreo = new javax.swing.JLabel();
        txtCorreo = new javax.swing.JTextField();
        jlCamposObligatorios = new javax.swing.JLabel();
        lblCedulaSinGuion = new javax.swing.JLabel();
        lblIdUsuario = new javax.swing.JLabel();
        lblValidadorCorreo = new javax.swing.JLabel();

        setClosable(true);
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameActivated(evt);
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jPanel1.setBackground(new java.awt.Color(81, 28, 28));

        lblNuevoUsuario.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblNuevoUsuario.setForeground(new java.awt.Color(255, 255, 255));
        lblNuevoUsuario.setText("Nuevo Usuario");

        jlNombreUsuario.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jlNombreUsuario.setForeground(new java.awt.Color(255, 255, 255));
        jlNombreUsuario.setText("Nombre");

        jlApellidoUsuario.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jlApellidoUsuario.setForeground(new java.awt.Color(255, 255, 255));
        jlApellidoUsuario.setText("Apellido");

        jlEstado.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jlEstado.setForeground(new java.awt.Color(255, 255, 255));
        jlEstado.setText("Estado");

        jlTipoUsuario.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jlTipoUsuario.setForeground(new java.awt.Color(255, 255, 255));
        jlTipoUsuario.setText("Tipo de Usuario");

        txtNombreUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreUsuarioKeyTyped(evt);
            }
        });

        txtApellidoUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtApellidoUsuarioKeyTyped(evt);
            }
        });

        jcbEstadoUsuario.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "--Seleccione opcion--", "Activo", "Inactivo" }));
        jcbEstadoUsuario.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbEstadoUsuarioItemStateChanged(evt);
            }
        });

        jcbTipodeUsuario.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "--Seleccione opcion--", "Administrador/a", "Secretario/a", "Consultor/a", " ", " " }));
        jcbTipodeUsuario.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbTipodeUsuarioItemStateChanged(evt);
            }
        });

        jlContraseña.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jlContraseña.setForeground(new java.awt.Color(255, 255, 255));
        jlContraseña.setText("Contraseña");

        txtContraseñaUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtContraseñaUsuarioKeyTyped(evt);
            }
        });

        btnGuardarNuevoUsuario.setBackground(new java.awt.Color(0, 0, 0));
        btnGuardarNuevoUsuario.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnGuardarNuevoUsuario.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardarNuevoUsuario.setText("Guardar");
        btnGuardarNuevoUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarNuevoUsuarioActionPerformed(evt);
            }
        });

        btnCancelarNuevoUsuario.setBackground(new java.awt.Color(0, 0, 0));
        btnCancelarNuevoUsuario.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancelarNuevoUsuario.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelarNuevoUsuario.setText("Cancelar");
        btnCancelarNuevoUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarNuevoUsuarioActionPerformed(evt);
            }
        });

        jlContraseña1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jlContraseña1.setForeground(new java.awt.Color(255, 255, 255));
        jlContraseña1.setText("Repetir Contraseña");

        txtRepetirContraseñaUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRepetirContraseñaUsuarioKeyTyped(evt);
            }
        });

        lblCedula.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCedula.setForeground(new java.awt.Color(255, 255, 255));
        lblCedula.setText("Cedula");

        txtCedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCedulaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCedulaKeyTyped(evt);
            }
        });

        lblCorreo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCorreo.setForeground(new java.awt.Color(255, 255, 255));
        lblCorreo.setText("Correo Electrónico");

        txtCorreo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCorreoKeyReleased(evt);
            }
        });

        jlCamposObligatorios.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jlCamposObligatorios.setForeground(new java.awt.Color(204, 51, 0));
        jlCamposObligatorios.setText("Todos los campos son obligatorios ... !");

        lblCedulaSinGuion.setForeground(new java.awt.Color(204, 51, 0));
        lblCedulaSinGuion.setText("Ingrese la CI sin guion");

        lblIdUsuario.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblIdUsuario.setForeground(new java.awt.Color(255, 255, 255));

        lblValidadorCorreo.setForeground(new java.awt.Color(204, 51, 0));
        lblValidadorCorreo.setText("Ingrese un email válido");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblIdUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(88, 88, 88)
                                .addComponent(btnGuardarNuevoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnCancelarNuevoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jlTipoUsuario)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jcbTipodeUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblCedula, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jlApellidoUsuario)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtApellidoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(190, 190, 190)
                                .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblCorreo, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jlContraseña)
                                    .addComponent(jlContraseña1)
                                    .addComponent(jlEstado))
                                .addGap(56, 56, 56)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtRepetirContraseñaUsuario)
                                    .addComponent(txtContraseñaUsuario)
                                    .addComponent(jcbEstadoUsuario, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(139, 139, 139)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jlCamposObligatorios)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jlNombreUsuario)
                                .addGap(134, 134, 134)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblNuevoUsuario)
                                    .addComponent(txtNombreUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblCedulaSinGuion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblValidadorCorreo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(lblNuevoUsuario)
                .addGap(27, 27, 27)
                .addComponent(lblIdUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jlNombreUsuario)
                    .addComponent(txtNombreUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jlApellidoUsuario)
                    .addComponent(txtApellidoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlTipoUsuario)
                    .addComponent(jcbTipodeUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblCedula)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblCedulaSinGuion)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCorreo)
                    .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblValidadorCorreo))
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlEstado)
                    .addComponent(jcbEstadoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jlContraseña)
                    .addComponent(txtContraseñaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jlContraseña1)
                    .addComponent(txtRepetirContraseñaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jlCamposObligatorios)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardarNuevoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelarNuevoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCorreoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCorreoKeyReleased
        int limite  = 75;
        if (txtCorreo.getText().length()== limite) evt.consume();
        valCorreo=txtCorreo.getText().toString();
        if(!Validate.validateEmail(valCorreo)) lblValidadorCorreo.setText("Correo erróneo");
        else lblValidadorCorreo.setText(ok);
    }//GEN-LAST:event_txtCorreoKeyReleased

    private void txtCedulaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaKeyTyped
        char car=evt.getKeyChar();
        if((car<'0' || car>'9')) evt.consume();
        int limite  = 10;
        if (txtCedula.getText().length()==limite) evt.consume();
    }//GEN-LAST:event_txtCedulaKeyTyped

    private void txtCedulaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaKeyReleased
        if (txtCedula.getText().length()==10){
            cedula=txtCedula.getText().toString();
            if(!Validate.validadorDeCedula(cedula)) lblCedulaSinGuion.setText("Cédula incorrecta");
            else lblCedulaSinGuion.setText(ok);
        }
    }//GEN-LAST:event_txtCedulaKeyReleased

    private void btnCancelarNuevoUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarNuevoUsuarioActionPerformed
        // TODO add your handling code here:
        this.dispose();
        //validacionCorreo();
    }//GEN-LAST:event_btnCancelarNuevoUsuarioActionPerformed

    private void btnGuardarNuevoUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarNuevoUsuarioActionPerformed
        Guardar();
    }//GEN-LAST:event_btnGuardarNuevoUsuarioActionPerformed

    private void jcbTipodeUsuarioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbTipodeUsuarioItemStateChanged
        // TODO add your handling code here:
        //Verifica que posicion del combobox del tipo de usuarios se esta escojiendo
        if(accion.contains("Actualizar")){
            Integer indice = jcbTipodeUsuario.getSelectedIndex();
            switch (indice) {
                case 1:
                ItemSeleccionado.rol="Administrador/a";
                break;
                case 2:
                ItemSeleccionado.rol="Secretario/a";
                break;
                case 3:
                ItemSeleccionado.rol="Consultor/a";
                break;
                default:
                JOptionPane.showMessageDialog(this,"Debe seleccionar un tipo de usuario");
                break;
            }
        }
    }//GEN-LAST:event_jcbTipodeUsuarioItemStateChanged

    private void jcbEstadoUsuarioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbEstadoUsuarioItemStateChanged
        //Verifica que posicion del combobox del estado de usuarios se esta escojiendo
        Integer indice = jcbEstadoUsuario.getSelectedIndex();
        switch (indice) {
            case 1:
            ItemSeleccionado.estado=true;
            break;
            case 2:
            ItemSeleccionado.estado=false;
            break;
            default:
            JOptionPane.showMessageDialog(this,"Debe seleccionar un estado de usuario");
        }
    }//GEN-LAST:event_jcbEstadoUsuarioItemStateChanged

    private void txtApellidoUsuarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellidoUsuarioKeyTyped
        ValidarLetras(evt); //LLamando al evento para ingresar solo letras
        int limite  = 30;
        if (txtApellidoUsuario.getText().length()== limite)              evt.consume();
    }//GEN-LAST:event_txtApellidoUsuarioKeyTyped

    private void txtNombreUsuarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreUsuarioKeyTyped
        ValidarLetras(evt); //LLamando al evento para ingresar solo letras
        int limite  = 30;
        if (txtNombreUsuario.getText().length()== limite)              evt.consume();
    }//GEN-LAST:event_txtNombreUsuarioKeyTyped

    private void txtRepetirContraseñaUsuarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRepetirContraseñaUsuarioKeyTyped
        int limite  = 15;
        if (txtRepetirContraseñaUsuario.getText().length()== limite) evt.consume();
    }//GEN-LAST:event_txtRepetirContraseñaUsuarioKeyTyped

    private void txtContraseñaUsuarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtContraseñaUsuarioKeyTyped
        int limite  = 15;
        if (txtContraseñaUsuario.getText().length()== limite)   evt.consume();
    }//GEN-LAST:event_txtContraseñaUsuarioKeyTyped

    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameActivated
        IniciarComponentes();
    }//GEN-LAST:event_formInternalFrameActivated


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelarNuevoUsuario;
    private javax.swing.JButton btnGuardarNuevoUsuario;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JComboBox jcbEstadoUsuario;
    private javax.swing.JComboBox jcbTipodeUsuario;
    private javax.swing.JLabel jlApellidoUsuario;
    private javax.swing.JLabel jlCamposObligatorios;
    private javax.swing.JLabel jlContraseña;
    private javax.swing.JLabel jlContraseña1;
    private javax.swing.JLabel jlEstado;
    private javax.swing.JLabel jlNombreUsuario;
    private javax.swing.JLabel jlTipoUsuario;
    private javax.swing.JLabel lblCedula;
    private javax.swing.JLabel lblCedulaSinGuion;
    private javax.swing.JLabel lblCorreo;
    private javax.swing.JLabel lblIdUsuario;
    private javax.swing.JLabel lblNuevoUsuario;
    private javax.swing.JLabel lblValidadorCorreo;
    private javax.swing.JTextField txtApellidoUsuario;
    private javax.swing.JTextField txtCedula;
    private javax.swing.JPasswordField txtContraseñaUsuario;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtNombreUsuario;
    private javax.swing.JPasswordField txtRepetirContraseñaUsuario;
    // End of variables declaration//GEN-END:variables
}
