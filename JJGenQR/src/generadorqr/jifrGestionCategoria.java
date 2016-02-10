/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generadorqr;

import Modelos.ItemSeleccionado;
import db.mysql;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class jifrGestionCategoria extends javax.swing.JInternalFrame {
DefaultTableModel model;
static Connection conn;
static Statement sent;
ItemSeleccionado isC=new ItemSeleccionado();
String idC = "";
jifrNuevaCategoria internalNuevaCategoria;
    /**
     * Creates new form jifrGestionCategoria
     */
    public jifrGestionCategoria() {
        initComponents();
        if(conn == null) conn = mysql.getConnect();
        if (LlenarTablaCategorias() != null) jtCategorias.setModel(LlenarTablaCategorias());
        String rutaCat = getClass().getResource("/images/Mas.png").getPath();
        MostrarVisualizador(btnNuevaCategoria, rutaCat);
        rutaCat = getClass().getResource("/images/actualizar.png").getPath();
        MostrarVisualizador(btnActualizarCategoria, rutaCat);
        rutaCat = getClass().getResource("/images/Eliminar.png").getPath();
        MostrarVisualizador(btnEliminarCategoria, rutaCat);
        rutaCat = getClass().getResource("/images/search.png").getPath();
        MostrarVisualizador(btnBuscarCategoria, rutaCat);
    }
    
    
    public static void MostrarVisualizador(JLabel Pantalla, String RutaDestino){
        try{
            Image capturarImgSoloLectura = ImageIO.read(new File(RutaDestino));
            Image obtenerImagen = capturarImgSoloLectura.getScaledInstance(Pantalla.getPreferredSize().width, Pantalla.getPreferredSize().height - 10, Image.SCALE_SMOOTH);
            Icon iconoEscalado = new ImageIcon(obtenerImagen);
            Pantalla.setIcon(iconoEscalado);
        }
        catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
 
    public static DefaultTableModel LlenarTablaCategorias(){
        try{
            String titulos[] = {"Id","Nombre","Descripcion"};
            String SQLTC ="SELECT * FROM categorias ORDER BY NOMBRECATEGORIA ASC"; 
            DefaultTableModel model = new DefaultTableModel(null, titulos);
            Statement sent = conn.createStatement();
            ResultSet rs = sent.executeQuery(SQLTC);
            String[]fila=new String[3];
            while(rs.next()){
                fila[0] = rs.getString("IDCATEGORIA");
                fila[1] = rs.getString("NOMBRECATEGORIA");
                fila[2] = rs.getString("DESCRIPCIONCATEGORIA");
                model.addRow(fila);
            }
            return model;
        }catch(Exception e){
            return null;
        }
    }
    
    void SeleccionarItemTablaC(java.awt.event.MouseEvent evt){
        DefaultTableModel modelo=(DefaultTableModel) jtCategorias.getModel();
        idC=String.valueOf(modelo.getValueAt(jtCategorias.getSelectedRow(),0));
    }
    
    void EliminarCategoria(){
        int fila = jtCategorias.getSelectedRow();
        try {
            String SQL = "DELETE FROM categorias WHERE IDCATEGORIA=" + jtCategorias.getValueAt(fila, 0);
            sent = conn.createStatement();
            int n = sent.executeUpdate(SQL);
            if (n > 0){
                JOptionPane.showMessageDialog(this, "Categoria eliminada correctamente ");
                jtCategorias.setModel(LlenarTablaCategorias());
            }
            else JOptionPane.showMessageDialog(this, "Categoria no eliminado ");
        }catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: Debe Seleccionar un registro " );
        }
    }
    
    void BuscarPorNombreCategoria (){
        try{
            String titulos[] = {"IDCATEGORIA", "NOMBRECATEGORIA","DESCRIPCIONCATEGORIA"};
            //Consulta para la fecha de inicio a fecha de final
            String SQL = "SELECT *FROM categorias WHERE NOMBRECATEGORIA Like '%"+txtBuscarContenidos.getText().toString().trim()+"%'ORDER BY NOMBRECATEGORIA ASC";
            model= new DefaultTableModel(null, titulos);
            sent = conn.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            String[]fila=new String[3];
            while(rs.next()){
                fila[0] = rs.getString("IDCATEGORIA");
                fila[1] = rs.getString("NOMBRECATEGORIA");
                fila[2] = rs.getString("DESCRIPCIONCATEGORIA");
                model.addRow(fila);
            }
            jtCategorias.setModel(model);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error de Consulta..... :(");
        }
    }
          
    public void centrarVentanaNuevaCategoria ( jifrNuevaCategoria internalFrameNuevaCategoria){
        int x=(Principal.jdeskGaleria.getWidth()/2)-internalFrameNuevaCategoria.getWidth()/2; 
        int y=(Principal.jdeskGaleria.getHeight()/2)-internalFrameNuevaCategoria.getHeight()/2;
        if(internalFrameNuevaCategoria.isShowing()){
            internalFrameNuevaCategoria.setLocation(x, y);
        }else{
            Principal.jdeskGaleria.add(internalFrameNuevaCategoria);
            internalFrameNuevaCategoria.setLocation(x, y);
            internalFrameNuevaCategoria.show();
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jlNuevaCategoria = new javax.swing.JLabel();
        jlCategorias = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtCategorias = new javax.swing.JTable();
        btnActualizarCategoria = new javax.swing.JLabel();
        btnEliminarCategoria = new javax.swing.JLabel();
        btnBuscarCategoria = new javax.swing.JLabel();
        txtBuscarContenidos = new javax.swing.JTextField();
        btnNuevaCategoria = new javax.swing.JLabel();

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jlNuevaCategoria.setForeground(new java.awt.Color(255, 255, 255));
        jlNuevaCategoria.setText("Nuevo");

        jlCategorias.setFont(new java.awt.Font("Nirmala UI", 1, 18)); // NOI18N
        jlCategorias.setText("Categorias");

        jtCategorias.setModel(new javax.swing.table.DefaultTableModel(
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
        jtCategorias.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtCategoriasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtCategorias);

        btnActualizarCategoria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/actualizar.png"))); // NOI18N
        btnActualizarCategoria.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActualizarCategoria.setMaximumSize(new java.awt.Dimension(84, 81));
        btnActualizarCategoria.setMinimumSize(new java.awt.Dimension(84, 81));
        btnActualizarCategoria.setPreferredSize(new java.awt.Dimension(84, 81));
        btnActualizarCategoria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnActualizarCategoriaMouseClicked(evt);
            }
        });

        btnEliminarCategoria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Eliminar.png"))); // NOI18N
        btnEliminarCategoria.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminarCategoria.setMaximumSize(new java.awt.Dimension(84, 81));
        btnEliminarCategoria.setMinimumSize(new java.awt.Dimension(84, 81));
        btnEliminarCategoria.setPreferredSize(new java.awt.Dimension(84, 81));
        btnEliminarCategoria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEliminarCategoriaMouseClicked(evt);
            }
        });

        btnBuscarCategoria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search.png"))); // NOI18N
        btnBuscarCategoria.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscarCategoria.setMaximumSize(new java.awt.Dimension(84, 81));
        btnBuscarCategoria.setMinimumSize(new java.awt.Dimension(84, 81));
        btnBuscarCategoria.setPreferredSize(new java.awt.Dimension(84, 81));
        btnBuscarCategoria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBuscarCategoriaMouseClicked(evt);
            }
        });

        txtBuscarContenidos.setEnabled(false);
        txtBuscarContenidos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarContenidosKeyPressed(evt);
            }
        });

        btnNuevaCategoria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Mas.png"))); // NOI18N
        btnNuevaCategoria.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNuevaCategoria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNuevaCategoriaMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(btnNuevaCategoria)
                        .addGap(18, 18, 18)
                        .addComponent(btnActualizarCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminarCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnBuscarCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtBuscarContenidos, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(372, 372, 372)
                        .addComponent(jlCategorias))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 828, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(292, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(0, 549, Short.MAX_VALUE)
                    .addComponent(jlNuevaCategoria)
                    .addGap(0, 550, Short.MAX_VALUE)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jlCategorias)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEliminarCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(txtBuscarContenidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnNuevaCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnActualizarCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jlNuevaCategoria)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 53, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtCategoriasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtCategoriasMouseClicked
        SeleccionarItemTablaC(evt);
    }//GEN-LAST:event_jtCategoriasMouseClicked
   
    private void btnActualizarCategoriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnActualizarCategoriaMouseClicked
        if(!idC.isEmpty()){
            isC.setAccionBoton("Actualizar");
            isC.setIdCategoria(idC);
            jtCategorias.setFocusable(false);
            jtCategorias.clearSelection();
            internalNuevaCategoria = new jifrNuevaCategoria();
            centrarVentanaNuevaCategoria(internalNuevaCategoria);
        }else JOptionPane.showMessageDialog(this, "No ha seleccionado una categoria a modificar");
    }//GEN-LAST:event_btnActualizarCategoriaMouseClicked

    private void btnEliminarCategoriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarCategoriaMouseClicked
        Object[] opciones={"Aceptar", "Cancelar"};
        if(!idC.isEmpty()){
            int eleccion=JOptionPane.showOptionDialog(this, "Está seguro que desea eliminar", "Eliminar",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, opciones, "Aceptar");
            if(eleccion==JOptionPane.YES_OPTION)  EliminarCategoria();
        } else JOptionPane.showMessageDialog(this, "No ha seleccionado un registro a eliminar");
    }//GEN-LAST:event_btnEliminarCategoriaMouseClicked

    private void btnBuscarCategoriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarCategoriaMouseClicked
        txtBuscarContenidos.setEnabled(true);
        txtBuscarContenidos.requestFocus();
    }//GEN-LAST:event_btnBuscarCategoriaMouseClicked

    private void txtBuscarContenidosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarContenidosKeyPressed
        BuscarPorNombreCategoria();
    }//GEN-LAST:event_txtBuscarContenidosKeyPressed

    private void btnNuevaCategoriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNuevaCategoriaMouseClicked
        isC.setAccionBoton("Guardar");
        internalNuevaCategoria =new jifrNuevaCategoria();
        centrarVentanaNuevaCategoria(internalNuevaCategoria);
    }//GEN-LAST:event_btnNuevaCategoriaMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnActualizarCategoria;
    private javax.swing.JLabel btnBuscarCategoria;
    private javax.swing.JLabel btnEliminarCategoria;
    private javax.swing.JLabel btnNuevaCategoria;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jlCategorias;
    private javax.swing.JLabel jlNuevaCategoria;
    public static javax.swing.JTable jtCategorias;
    private javax.swing.JTextField txtBuscarContenidos;
    // End of variables declaration//GEN-END:variables
}
