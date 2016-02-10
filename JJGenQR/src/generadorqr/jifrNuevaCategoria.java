/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generadorqr;

import Modelos.ItemSeleccionado;
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
public class jifrNuevaCategoria extends javax.swing.JInternalFrame {
Connection conn;
Statement sent;
String accion;
ItemSeleccionado cs = new ItemSeleccionado();
jifrGestionCategoria internalGestionCategoria;

    public jifrNuevaCategoria() {
        initComponents();
        txtNombreCategoria.requestFocus();
        conn = mysql.getConnect();
        lblIdCategoria.setVisible(false);
        lbllNuevaCategoria.setText("Nueva Categoria");
        txtNombreCategoria.setText("");
        txtDescripcionCategoria.setText("");
        accion=ItemSeleccionado.accionBoton;
        btnAceptar.setText(accion);
        try{
            //Muestra los usuarios existentes en la base de datos
            if(accion.contains("Actualizar")){
                lbllNuevaCategoria.setText(accion + " Nueva Categoría");
                lblIdCategoria.setText("ID de la Categoria: \t\t" + ItemSeleccionado.idCategoria);
                lblIdCategoria.setVisible(true);
                String SQLTC ="SELECT * FROM categorias WHERE IDCATEGORIA = " + ItemSeleccionado.idCategoria; 
                sent = conn.createStatement();
                ResultSet rs = sent.executeQuery(SQLTC);
                rs.next();
                txtNombreCategoria.setText(rs.getString("NOMBRECATEGORIA"));
                txtDescripcionCategoria.setText(rs.getString("DESCRIPCIONCATEGORIA"));
                rs.close();
            }
        }
        catch(Exception e){
        }
    }

    void Limpiar(){
        cs.setAccionBoton("");
        cs.setIdCategoria("");
    }
    
    void GuardarCategoria(){
        try {
            //Ingreso en nuevo usuario
            if(btnAceptar.getText().contains("Guardar")){
                if (txtNombreCategoria.getText().trim().isEmpty() || txtDescripcionCategoria.getText().trim().isEmpty())
                    JOptionPane.showMessageDialog(this, "Ingrese Los Campos Obligatorios");
                else{        
                    try {
                        String SQL = "INSERT INTO categorias(NOMBRECATEGORIA,DESCRIPCIONCATEGORIA)"
                                + " VALUES(?,?)";
                        PreparedStatement ps = conn.prepareStatement(SQL);
                        ps.setString(1, txtNombreCategoria.getText());
                        ps.setString(2, txtDescripcionCategoria.getText());
                        int n = ps.executeUpdate();
                        if (n > 0) {
                            JOptionPane.showMessageDialog(this, "Categoria creada Correctamente");
                            internalGestionCategoria = new jifrGestionCategoria();
                            Principal.centrarVentanaGestionCA(internalGestionCategoria);
                            dispose();
                        } 
                    } catch (SQLException e) {
                        JOptionPane.showConfirmDialog(this, "Error: " + e.getMessage());
                        System.out.println();
                    }
                }
            }
            else{
                if (txtNombreCategoria.getText().trim().isEmpty() || txtDescripcionCategoria.getText().trim().isEmpty())
                    JOptionPane.showMessageDialog(this, "Ingrese Los Campos Obligatorios");
                else{
                    String SQL = "UPDATE categorias SET NOMBRECATEGORIA = ?, DESCRIPCIONCATEGORIA = ? WHERE IDCATEGORIA = " + ItemSeleccionado.idCategoria;
                    PreparedStatement ps = conn.prepareStatement(SQL);
                    ps.setString(1, txtNombreCategoria.getText());
                    ps.setString(2, txtDescripcionCategoria.getText());
                    int n = ps.executeUpdate();
                    if (n > 0) {
                        JOptionPane.showMessageDialog(this, "Categoria actualizada Correctamente");
                        dispose();
                        internalGestionCategoria = new jifrGestionCategoria();
                        Principal.centrarVentanaGestionCA(internalGestionCategoria);
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showConfirmDialog(null, "Error: " + e.getMessage());
            //System.out.println();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lbllNuevaCategoria = new javax.swing.JLabel();
        jlNombreCategoria = new javax.swing.JLabel();
        jlDescripcionCategoria = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDescripcionCategoria = new javax.swing.JTextArea();
        txtNombreCategoria = new javax.swing.JTextField();
        jlCamposObligatorios = new javax.swing.JLabel();
        btnCancelar = new javax.swing.JButton();
        btnAceptar = new javax.swing.JButton();
        lblIdCategoria = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(2, 32, 62));

        lbllNuevaCategoria.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbllNuevaCategoria.setForeground(new java.awt.Color(255, 255, 255));
        lbllNuevaCategoria.setText("Nueva Categoria");

        jlNombreCategoria.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jlNombreCategoria.setForeground(new java.awt.Color(255, 255, 255));
        jlNombreCategoria.setText("Nombre");

        jlDescripcionCategoria.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jlDescripcionCategoria.setForeground(new java.awt.Color(255, 255, 255));
        jlDescripcionCategoria.setText("Descripcion");

        txtDescripcionCategoria.setColumns(20);
        txtDescripcionCategoria.setRows(5);
        jScrollPane2.setViewportView(txtDescripcionCategoria);

        jlCamposObligatorios.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jlCamposObligatorios.setForeground(new java.awt.Color(204, 51, 0));
        jlCamposObligatorios.setText("Todos los campos son obligatorios ... !");

        btnCancelar.setBackground(new java.awt.Color(153, 204, 255));
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnAceptar.setBackground(new java.awt.Color(153, 204, 255));
        btnAceptar.setText("Guardar");
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });

        lblIdCategoria.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblIdCategoria.setForeground(new java.awt.Color(255, 255, 255));
        lblIdCategoria.setText("lblIdCategoria");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(147, 147, 147)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbllNuevaCategoria)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtNombreCategoria)
                        .addGap(25, 25, 25))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(91, 91, 91)
                .addComponent(jlCamposObligatorios)
                .addContainerGap(101, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblIdCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlDescripcionCategoria)
                            .addComponent(jlNombreCategoria)
                            .addComponent(jScrollPane2))
                        .addContainerGap(25, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(73, 73, 73)
                    .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(242, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lbllNuevaCategoria)
                .addGap(3, 3, 3)
                .addComponent(lblIdCategoria)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlNombreCategoria)
                    .addComponent(txtNombreCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(jlDescripcionCategoria)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jlCamposObligatorios)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(461, Short.MAX_VALUE)
                    .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(26, 26, 26)))
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

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        Limpiar();
        dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        GuardarCategoria();
        Limpiar();
    }//GEN-LAST:event_btnAceptarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel jlCamposObligatorios;
    private javax.swing.JLabel jlDescripcionCategoria;
    private javax.swing.JLabel jlNombreCategoria;
    private javax.swing.JLabel lblIdCategoria;
    private javax.swing.JLabel lbllNuevaCategoria;
    private javax.swing.JTextArea txtDescripcionCategoria;
    private javax.swing.JTextField txtNombreCategoria;
    // End of variables declaration//GEN-END:variables
}
