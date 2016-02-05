/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generadorqr;

import Modelos.ItemSeleccionado;
import Modelos.ValoresConstantes;
import db.mysql;
import java.sql.Connection;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
import java.awt.Image;
import java.io.File;
import java.sql.ResultSet;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Jess
 */
public class jifrGestionArticulos extends javax.swing.JInternalFrame {
DefaultTableModel model;
static Connection conn;
static Statement sent;   
String id = "", imagenes = "",categoria="";
ItemSeleccionado isA=new ItemSeleccionado();
String idA = "";
Integer buscar = 0;
jifrNuevoQr internalNuevoQr;


    public jifrGestionArticulos() {
        initComponents();
        if(conn == null) conn = mysql.getConnect();
        if (LlenarTablaArticulos() != null) jtContenidosArticulos.setModel(LlenarTablaArticulos());
        String rutaArt = getClass().getResource("/images/Mas.png").getPath();
        MostrarVisualizador(btnNuevosArticulos, rutaArt);
        rutaArt = getClass().getResource("/images/actualizar.png").getPath();
        MostrarVisualizador(btnActualizarArticulos, rutaArt);
        rutaArt = getClass().getResource("/images/Eliminar.png").getPath();
        MostrarVisualizador(btnEliminarArticulos, rutaArt);
        rutaArt = getClass().getResource("/images/search.png").getPath();
        MostrarVisualizador(btnBuscarArticulos, rutaArt);
    }
    
    public static DefaultTableModel LlenarTablaArticulos(){
        try{
            //String titulos[] = {"IdCategoria","Nombre","Descripcion","Editar","Eliminar"};
            String titulos[] = {"ID","CATEGORIA","NOMBRE","DESCRIPCION","IMAGEN UNO","IMAGEN DOS",
                "IMAGEN TRES","SONIDO","VIDEO","IMAGEN QR"};
            //String SQL ="SELECT * FROM ingresos where CodigoParaiso Like '%"+txtBuscar.getText().toString().trim()+"%'AND ORDER BY Movimiento,Id,Fecha ASC"; 
            String SQLTA ="SELECT a.IDARTICULO, c.NOMBRECATEGORIA, a.NOMBREARTICULO, a.DESCRIPCIONARTICULO, a.IMAGENUNOARTICULO, a.IMAGENDOSARTICULO, a.IMAGENTRESARTICULO, a.SONIDOARTICULO, a.VIDEOARTICULO, a.IMAGENQRARTICULO FROM articulos AS a INNER JOIN categorias AS c USING(IDCATEGORIA) ORDER BY a.IDARTICULO ASC"; 
            DefaultTableModel model = new DefaultTableModel(null, titulos);
            Statement sent = conn.createStatement();
            ResultSet rs = sent.executeQuery(SQLTA);
            String[]fila=new String[10];
            while(rs.next()){
                fila[0] = rs.getString("IDARTICULO");
                fila[1] = rs.getString("NOMBRECATEGORIA");
                fila[2] = rs.getString("NOMBREARTICULO");
                fila[3] = rs.getString("DESCRIPCIONARTICULO");
                fila[4] = rs.getString("IMAGENUNOARTICULO");
                fila[5] = rs.getString("IMAGENDOSARTICULO");
                fila[6] = rs.getString("IMAGENTRESARTICULO");
                fila[7] = rs.getString("SONIDOARTICULO");
                fila[8] = rs.getString("VIDEOARTICULO");
                fila[9] = rs.getString("IMAGENQRARTICULO");
                model.addRow(fila);
            }
            return model;
        }catch(Exception e){
            return null;
        }
    }

        
    void EliminarArticulos(){
        JOptionPane.showMessageDialog(null, "El artículo será eliminado");
        int fila = jtContenidosArticulos.getSelectedRow();
        try {
            String SQL = "DELETE FROM articulos WHERE IDARTICULO=" + idA;
            sent = conn.createStatement();
            int n = sent.executeUpdate(SQL);
            if (n > 0){
                JOptionPane.showMessageDialog(null, "Artículo eliminado correctamente ");
                File directorioPrincipalArticulo = new File(ValoresConstantes.directorioPrincipal + "\\" + jtContenidosArticulos.getValueAt(fila, 2));
                FileUtils.deleteDirectory(directorioPrincipalArticulo);
                LlenarTablaArticulos();
            }
            else JOptionPane.showMessageDialog(null, "Artículo no eliminado ");
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: Debe seleccionar un registro" );
        }
    }

    void SeleccionarItemTablaCA(java.awt.event.MouseEvent evt){
        DefaultTableModel modelo=(DefaultTableModel) jtContenidosArticulos.getModel();
        idA=String.valueOf(modelo.getValueAt(jtContenidosArticulos.getSelectedRow(),0));
        categoria=String.valueOf(modelo.getValueAt(jtContenidosArticulos.getSelectedRow(),1));
        imagenes = String.valueOf(modelo.getValueAt(jtContenidosArticulos.getSelectedRow(),4));
        MostrarVisualizador(lblVistaPreviaImagen1, imagenes);
        imagenes = String.valueOf(modelo.getValueAt(jtContenidosArticulos.getSelectedRow(),5));
        if(!imagenes.isEmpty()) MostrarVisualizador(lblVistaPreviaImagen2, imagenes);
        else lblVistaPreviaImagen2.setIcon(null);
        imagenes = String.valueOf(modelo.getValueAt(jtContenidosArticulos.getSelectedRow(),6));
        if(!imagenes.isEmpty()) MostrarVisualizador(lblVistaPreviaImagen3, imagenes);
        else lblVistaPreviaImagen3.setIcon(null);
        imagenes = String.valueOf(modelo.getValueAt(jtContenidosArticulos.getSelectedRow(),9));
        if(!imagenes.isEmpty()) MostrarVisualizador(lblVistaPreviaImagen4, imagenes);
        else lblVistaPreviaImagen4.setIcon(null);
    }
    
    void BuscarPorNombreArticulo (){
        try{
            //Consulta para la fecha de inicio a fecha de final
            String titulos[] = {"ID","NOMBRE","DESCRIPCION","IMAGEN UNO","IMAGEN DOS","IMAGEN TRES","SONIDO","VIDEO","CODIGO","IMAGEN QR"};
            String SQL = "SELECT *FROM articulos WHERE NOMBREARTICULO Like '%"+txtBuscarArticulo.getText().toString().trim()+"%'ORDER BY NOMBREARTICULO ASC";
            model = new DefaultTableModel(null, titulos);
            sent = conn.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            String[]fila=new String[10];
            while(rs.next()){
                fila[0] = rs.getString("IDARTICULO");
                fila[1] = rs.getString("NOMBREARTICULO");
                fila[2] = rs.getString("DESCRIPCIONARTICULO");
                fila[3] = rs.getString("IMAGENUNOARTICULO");
                fila[4] = rs.getString("IMAGENDOSARTICULO");
                fila[5] = rs.getString("IMAGENTRESARTICULO");
                fila[6] = rs.getString("SONIDOARTICULO");
                fila[7] = rs.getString("VIDEOARTICULO");
                fila[8] = rs.getString("CODIGOQRARTICULO");
                fila[9] = rs.getString("IMAGENQRARTICULO");
                model.addRow(fila);
            }

            jtContenidosArticulos.setModel(model);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error de Consulta..... :(");
        }
    }
    
     public void centrarVentanaNuevoQr ( jifrNuevoQr internalFrameNuevoQr){
        int x=(Principal.jdeskGaleria.getWidth()/2)-internalFrameNuevoQr.getWidth()/2; 
        int y=(Principal.jdeskGaleria.getHeight()/2)-internalFrameNuevoQr.getHeight()/2;
        if(internalFrameNuevoQr.isShowing()){
            internalFrameNuevoQr.setLocation(x, y);
        }else{
            Principal.jdeskGaleria.add(internalFrameNuevoQr);
            internalFrameNuevoQr.setLocation(x, y);
            internalFrameNuevoQr.show();
        }
    }
     
    public static void MostrarVisualizador(JLabel Pantalla, String RutaDestino){
        try
        {
            Image capturarImgSoloLectura = ImageIO.read(new File(RutaDestino));
            Image obtenerImagen = capturarImgSoloLectura.getScaledInstance(Pantalla.getPreferredSize().width, Pantalla.getPreferredSize().height - 10, Image.SCALE_SMOOTH);
            Icon iconoEscalado = new ImageIcon(obtenerImagen);
            Pantalla.setIcon(iconoEscalado);
        }
        catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jlNuevaCategoria = new javax.swing.JLabel();
        jlCategorias = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtContenidosArticulos = new javax.swing.JTable();
        btnActualizarArticulos = new javax.swing.JLabel();
        btnEliminarArticulos = new javax.swing.JLabel();
        btnBuscarArticulos = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        lblVistaPreviaImagen1 = new javax.swing.JLabel();
        lblVistaPreviaImagen2 = new javax.swing.JLabel();
        lblVistaPreviaImagen3 = new javax.swing.JLabel();
        lblVistaPreviaImagen4 = new javax.swing.JLabel();
        rbtnBuscarPorCategoria = new javax.swing.JRadioButton();
        rbtnBuscarPorNombre = new javax.swing.JRadioButton();
        txtBuscarArticulo = new javax.swing.JTextField();
        btnNuevosArticulos = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jlNuevaCategoria.setForeground(new java.awt.Color(255, 255, 255));
        jlNuevaCategoria.setText("Nuevo");

        jlCategorias.setFont(new java.awt.Font("Nirmala UI", 1, 18)); // NOI18N
        jlCategorias.setText("Galeria de Piezas de Arte ");

        jtContenidosArticulos.setModel(new javax.swing.table.DefaultTableModel(
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
        jtContenidosArticulos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtContenidosArticulosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtContenidosArticulos);

        btnActualizarArticulos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/actualizar.png"))); // NOI18N
        btnActualizarArticulos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActualizarArticulos.setMaximumSize(new java.awt.Dimension(84, 81));
        btnActualizarArticulos.setMinimumSize(new java.awt.Dimension(84, 81));
        btnActualizarArticulos.setPreferredSize(new java.awt.Dimension(84, 81));
        btnActualizarArticulos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnActualizarArticulosMouseClicked(evt);
            }
        });

        btnEliminarArticulos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/eliminar.jpg"))); // NOI18N
        btnEliminarArticulos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminarArticulos.setMaximumSize(new java.awt.Dimension(84, 81));
        btnEliminarArticulos.setMinimumSize(new java.awt.Dimension(84, 81));
        btnEliminarArticulos.setPreferredSize(new java.awt.Dimension(84, 81));
        btnEliminarArticulos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEliminarArticulosMouseClicked(evt);
            }
        });

        btnBuscarArticulos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search.png"))); // NOI18N
        btnBuscarArticulos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscarArticulos.setMaximumSize(new java.awt.Dimension(84, 81));
        btnBuscarArticulos.setMinimumSize(new java.awt.Dimension(84, 81));
        btnBuscarArticulos.setPreferredSize(new java.awt.Dimension(84, 81));
        btnBuscarArticulos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBuscarArticulosMouseClicked(evt);
            }
        });

        jLabel1.setText("Imagenes");

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        lblVistaPreviaImagen1.setMaximumSize(new java.awt.Dimension(104, 140));
        lblVistaPreviaImagen1.setMinimumSize(new java.awt.Dimension(104, 140));
        lblVistaPreviaImagen1.setPreferredSize(new java.awt.Dimension(104, 140));

        lblVistaPreviaImagen2.setMaximumSize(new java.awt.Dimension(104, 140));
        lblVistaPreviaImagen2.setMinimumSize(new java.awt.Dimension(104, 140));
        lblVistaPreviaImagen2.setPreferredSize(new java.awt.Dimension(104, 140));

        lblVistaPreviaImagen3.setMaximumSize(new java.awt.Dimension(104, 140));
        lblVistaPreviaImagen3.setMinimumSize(new java.awt.Dimension(104, 140));
        lblVistaPreviaImagen3.setPreferredSize(new java.awt.Dimension(104, 140));

        lblVistaPreviaImagen4.setMaximumSize(new java.awt.Dimension(104, 140));
        lblVistaPreviaImagen4.setMinimumSize(new java.awt.Dimension(104, 140));
        lblVistaPreviaImagen4.setPreferredSize(new java.awt.Dimension(104, 140));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblVistaPreviaImagen1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(lblVistaPreviaImagen2, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(lblVistaPreviaImagen3, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblVistaPreviaImagen4, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(274, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblVistaPreviaImagen1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblVistaPreviaImagen2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblVistaPreviaImagen3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblVistaPreviaImagen4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rbtnBuscarPorCategoria.setText("Categoría");
        rbtnBuscarPorCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnBuscarPorCategoriaActionPerformed(evt);
            }
        });

        rbtnBuscarPorNombre.setText("Nombre");
        rbtnBuscarPorNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnBuscarPorNombreActionPerformed(evt);
            }
        });

        txtBuscarArticulo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarArticuloKeyPressed(evt);
            }
        });

        btnNuevosArticulos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Mas.png"))); // NOI18N
        btnNuevosArticulos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNuevosArticulos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNuevosArticulosMouseClicked(evt);
            }
        });

        jLabel2.setText("Qr");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(372, 372, 372)
                        .addComponent(jlCategorias))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel1)
                                .addGap(352, 352, 352)
                                .addComponent(jLabel2))
                            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 641, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(btnNuevosArticulos)
                                .addGap(47, 47, 47)
                                .addComponent(btnActualizarArticulos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)
                                .addComponent(btnEliminarArticulos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36)
                                .addComponent(btnBuscarArticulos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(55, 55, 55)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(rbtnBuscarPorCategoria)
                                    .addComponent(rbtnBuscarPorNombre))
                                .addGap(18, 18, 18)
                                .addComponent(txtBuscarArticulo, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(78, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(0, 428, Short.MAX_VALUE)
                    .addComponent(jlNuevaCategoria)
                    .addGap(0, 429, Short.MAX_VALUE)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jlCategorias)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEliminarArticulos, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnActualizarArticulos, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBuscarArticulos, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNuevosArticulos, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(rbtnBuscarPorCategoria)
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addComponent(txtBuscarArticulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)))
                        .addComponent(rbtnBuscarPorNombre)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(0, 165, Short.MAX_VALUE)
                    .addComponent(jlNuevaCategoria)
                    .addGap(0, 166, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 888, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 436, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtContenidosArticulosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtContenidosArticulosMouseClicked
        // TODO add your handling code here:
        SeleccionarItemTablaCA(evt);
    }//GEN-LAST:event_jtContenidosArticulosMouseClicked

    private void btnActualizarArticulosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnActualizarArticulosMouseClicked
        // TODO add your handling code here:
        if(!idA.isEmpty()){
            isA.setAccionBoton("Actualizar");
            isA.setIdArticulo(idA);
            isA.setIdCategoria(categoria);
            jifrNuevoQr frnu=new jifrNuevoQr();
            frnu.show();
        }else JOptionPane.showMessageDialog(this, "No ha seleccionado un registro a modificar");
    }//GEN-LAST:event_btnActualizarArticulosMouseClicked

    private void btnEliminarArticulosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarArticulosMouseClicked
        // TODO add your handling code here:
        Object [] opciones={"Aceptar","Cancelar"};
        if(!idA.isEmpty()){
            int eleccion=JOptionPane.showOptionDialog(null,"Está seguro que desea eliminar","Eliminar",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,opciones,"Aceptar");
            if(eleccion==JOptionPane.YES_OPTION)  EliminarArticulos();
        }else JOptionPane.showMessageDialog(this, "No ha seleccionado un registro a eliminar");

    }//GEN-LAST:event_btnEliminarArticulosMouseClicked

    private void btnBuscarArticulosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarArticulosMouseClicked
        // TODO add your handling code here:
        rbtnBuscarPorCategoria.setVisible(true);
        rbtnBuscarPorNombre.setVisible(true);
        txtBuscarArticulo.setVisible(true);
        txtBuscarArticulo.requestFocus();
    }//GEN-LAST:event_btnBuscarArticulosMouseClicked

    private void rbtnBuscarPorCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnBuscarPorCategoriaActionPerformed
        // TODO add your handling code here:
        //BuscarPorNombreCategoria();
        txtBuscarArticulo.setText("");
    }//GEN-LAST:event_rbtnBuscarPorCategoriaActionPerformed

    private void rbtnBuscarPorNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnBuscarPorNombreActionPerformed
        // TODO add your handling code here:
        rbtnBuscarPorCategoria.setSelected(false);
        txtBuscarArticulo.setText("");
    }//GEN-LAST:event_rbtnBuscarPorNombreActionPerformed

    private void txtBuscarArticuloKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarArticuloKeyPressed
        // TODO add your handling code here:
        if(rbtnBuscarPorNombre.isSelected()) BuscarPorNombreArticulo();

    }//GEN-LAST:event_txtBuscarArticuloKeyPressed

    private void btnNuevosArticulosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNuevosArticulosMouseClicked
        // TODO add your handling code here:
        if(!(internalNuevoQr instanceof jifrNuevoQr)){
            internalNuevoQr = new jifrNuevoQr();
            isA.setAccionBoton("Guardar");
        //jifrNuevoQr nca = new jifrNuevoQr();
        //nca.show();
        }
        centrarVentanaNuevoQr(internalNuevoQr);
    }//GEN-LAST:event_btnNuevosArticulosMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnActualizarArticulos;
    private javax.swing.JLabel btnBuscarArticulos;
    private javax.swing.JLabel btnEliminarArticulos;
    private javax.swing.JLabel btnNuevosArticulos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jlCategorias;
    private javax.swing.JLabel jlNuevaCategoria;
    public static javax.swing.JTable jtContenidosArticulos;
    private javax.swing.JLabel lblVistaPreviaImagen1;
    private javax.swing.JLabel lblVistaPreviaImagen2;
    private javax.swing.JLabel lblVistaPreviaImagen3;
    private javax.swing.JLabel lblVistaPreviaImagen4;
    private javax.swing.JRadioButton rbtnBuscarPorCategoria;
    private javax.swing.JRadioButton rbtnBuscarPorNombre;
    private javax.swing.JTextField txtBuscarArticulo;
    // End of variables declaration//GEN-END:variables
}
