/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generadorqr;

import Modelos.ItemSeleccionado;
import Modelos.ValoresConstantes;
import com.mysql.jdbc.StringUtils;
import db.mysql;
import java.sql.Connection;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
import java.awt.Image;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public final class jifrGestionArticulos extends javax.swing.JInternalFrame {
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
        btgSeleccion.add(rbtnBuscarPorCategoria);
        btgSeleccion.add(rbtnBuscarPorNombre);
        String rutaArt = getClass().getResource("/images/Mas.png").getPath();
        MostrarVisualizador(btnNuevosArticulos, rutaArt);
        rutaArt = getClass().getResource("/images/actualizar.png").getPath();
        MostrarVisualizador(btnActualizarArticulos, rutaArt);
        rutaArt = getClass().getResource("/images/Eliminar.png").getPath();
        MostrarVisualizador(btnEliminarArticulos, rutaArt);
        rutaArt = getClass().getResource("/images/search.png").getPath();
        MostrarVisualizador(btnBuscarArticulos, rutaArt);
        rutaArt = getClass().getResource("/images/imprimir.png").getPath();
        MostrarVisualizador(btnImprimir, rutaArt);
        contarTotalE();
        sumarTotalA();
        lblEtiquetaPreviewImagenes.setVisible(false);
        lblEtiquetaPreviewQr.setVisible(false);
    }
    
    void contarTotalE(){
        try {
            String SQL ="SELECT COUNT(*) AS Total FROM articulos";
            sent = conn.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            while(rs.next()){
            lblTotaEspecies.setText(rs.getString("Total"));
        }
           } catch (SQLException e) {
            lblTotaEspecies.setText("null");
         }
        }  
    
    void sumarTotalA(){
        try {
            String SQL ="SELECT SUM(CANTIDADARTICULO) AS Suma FROM articulos";
            sent = conn.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            while(rs.next()){
            lblTotalArticulos.setText(rs.getString("Suma"));
        }
           } catch (SQLException e) {
            lblTotalArticulos.setText("null");
         }
        } 
    
    public static DefaultTableModel LlenarTablaArticulos(){
        try{
            String titulos[] = {"ID","CATEGORIA","NOMBRE","CANTIDAD","DESCRIPCION","IMAGEN UNO","IMAGEN DOS",
                "IMAGEN TRES","SONIDO","VIDEO","IMAGEN QR"};
            String SQLTA ="SELECT a.IDARTICULO, c.NOMBRECATEGORIA, a.NOMBREARTICULO,a.CANTIDADARTICULO, a.DESCRIPCIONARTICULO, a.IMAGENUNOARTICULO, a.IMAGENDOSARTICULO, a.IMAGENTRESARTICULO, a.SONIDOARTICULO, a.VIDEOARTICULO, a.IMAGENQRARTICULO FROM articulos AS a INNER JOIN categorias AS c USING(IDCATEGORIA) ORDER BY a.IDARTICULO ASC"; 
            DefaultTableModel model = new DefaultTableModel(null, titulos);
            Statement sent = conn.createStatement();
            ResultSet rs = sent.executeQuery(SQLTA);
            String[]fila=new String[11];
            while(rs.next()){
                fila[0] = rs.getString("IDARTICULO");
                fila[1] = rs.getString("NOMBRECATEGORIA");
                fila[2] = rs.getString("NOMBREARTICULO");
                fila[3] = rs.getString("CANTIDADARTICULO");
                fila[4] = rs.getString("DESCRIPCIONARTICULO");
                fila[5] = rs.getString("IMAGENUNOARTICULO");
                fila[6] = rs.getString("IMAGENDOSARTICULO");
                fila[7] = rs.getString("IMAGENTRESARTICULO");
                fila[8] = rs.getString("SONIDOARTICULO");
                fila[9] = rs.getString("VIDEOARTICULO");
                fila[10] = rs.getString("IMAGENQRARTICULO");
                model.addRow(fila);
            }
            return model;
        }catch(Exception e){
            return null;
        }
    }
  
    void EliminarArticulos(){
        int fila = jtContenidosArticulos.getSelectedRow();
        try {
            String SQL = "DELETE FROM articulos WHERE IDARTICULO=" + idA;
            sent = conn.createStatement();
            int n = sent.executeUpdate(SQL);
            if (n > 0){
                JOptionPane.showMessageDialog(this, "Artículo eliminado correctamente ");
                File directorioPrincipalArticulo = new File(ValoresConstantes.DIRECTORIO_PRINCIPAL + "\\" + jtContenidosArticulos.getValueAt(fila, 2));
                FileUtils.deleteDirectory(directorioPrincipalArticulo);
                jifrGestionArticulos internalGA = new jifrGestionArticulos();
                Principal.centrarVentanaGestionCA(internalGA);
            }
            else JOptionPane.showMessageDialog(this, "Artículo no eliminado ");
        }catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: Debe seleccionar un registro" );
        }
    }

    void SeleccionarItemTablaCA(java.awt.event.MouseEvent evt){
        DefaultTableModel modelo=(DefaultTableModel) jtContenidosArticulos.getModel();
        idA=String.valueOf(modelo.getValueAt(jtContenidosArticulos.getSelectedRow(),0));
        categoria=String.valueOf(modelo.getValueAt(jtContenidosArticulos.getSelectedRow(),1));
        imagenes = String.valueOf(modelo.getValueAt(jtContenidosArticulos.getSelectedRow(),5));
        MostrarVisualizador(lblVistaPreviaImagen1, imagenes);
        imagenes = String.valueOf(modelo.getValueAt(jtContenidosArticulos.getSelectedRow(),6));
        if(!StringUtils.isNullOrEmpty(imagenes) && !imagenes.contains("null")) MostrarVisualizador(lblVistaPreviaImagen2, imagenes);
        else lblVistaPreviaImagen2.setIcon(null);
        imagenes = String.valueOf(modelo.getValueAt(jtContenidosArticulos.getSelectedRow(),7));
        if(!StringUtils.isNullOrEmpty(imagenes) && !imagenes.contains("null")) MostrarVisualizador(lblVistaPreviaImagen3, imagenes);
        else lblVistaPreviaImagen3.setIcon(null);
        imagenes = String.valueOf(modelo.getValueAt(jtContenidosArticulos.getSelectedRow(),10));
        if(!StringUtils.isNullOrEmpty(imagenes) && !imagenes.contains("null")) MostrarVisualizador(lblVistaPreviaImagen4, imagenes);
        else lblVistaPreviaImagen4.setIcon(null);
        lblEtiquetaPreviewImagenes.setVisible(true);
        lblEtiquetaPreviewQr.setVisible(true);
    }
    
    void BuscarPorCategoriaArticulo (){
        try{
            String titulos[] = {"ID","CATEGORIA","NOMBRE","CANTIDAD","DESCRIPCION","IMAGEN UNO","IMAGEN DOS","IMAGEN TRES","SONIDO","VIDEO","IMAGEN QR"};
            String SQL ="SELECT a.IDARTICULO, c.NOMBRECATEGORIA, a.NOMBREARTICULO,a.CANTIDADARTICULO, a.DESCRIPCIONARTICULO, a.IMAGENUNOARTICULO, a.IMAGENDOSARTICULO, a.IMAGENTRESARTICULO, a.SONIDOARTICULO, a.VIDEOARTICULO, a.IMAGENQRARTICULO "
                    + "FROM articulos AS a INNER JOIN categorias AS c USING(IDCATEGORIA) WHERE c.NOMBRECATEGORIA Like '"+txtBuscarArticulo.getText().toString().trim()+"%'ORDER BY c.NOMBRECATEGORIA ASC"; 
            model = new DefaultTableModel(null, titulos);
            sent = conn.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            String[]fila=new String[11];
            while(rs.next()){
                fila[0] = rs.getString("IDARTICULO");
                fila[1] = rs.getString("NOMBRECATEGORIA");
                fila[2] = rs.getString("NOMBREARTICULO");
                fila[3]=  rs.getString("CANTIDADARTICULO");
                fila[4] = rs.getString("DESCRIPCIONARTICULO");
                fila[5] = rs.getString("IMAGENUNOARTICULO");
                fila[6] = rs.getString("IMAGENDOSARTICULO");
                fila[7] = rs.getString("IMAGENTRESARTICULO");
                fila[8] = rs.getString("SONIDOARTICULO");
                fila[9] = rs.getString("VIDEOARTICULO");
                fila[10] = rs.getString("IMAGENQRARTICULO");
                model.addRow(fila);
            }
            jtContenidosArticulos.setModel(model);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error de Consulta..... :(");
        }
    }
    
    void BuscarPorNombreArticulo (){
        try{
            String titulos[] = {"ID","CATEGORIA","NOMBRE","CANTIDAD","DESCRIPCION","IMAGEN UNO","IMAGEN DOS","IMAGEN TRES","SONIDO","VIDEO","IMAGEN QR"};
            String SQL ="SELECT a.IDARTICULO, c.NOMBRECATEGORIA, a.NOMBREARTICULO,a.CANTIDADARTICULO, a.DESCRIPCIONARTICULO, a.IMAGENUNOARTICULO, a.IMAGENDOSARTICULO, a.IMAGENTRESARTICULO, a.SONIDOARTICULO, a.VIDEOARTICULO, a.IMAGENQRARTICULO "
                    + "FROM articulos AS a INNER JOIN categorias AS c USING(IDCATEGORIA) WHERE a.NOMBREARTICULO Like '"+txtBuscarArticulo.getText().toString().trim()+"%'ORDER BY c.NOMBRECATEGORIA ASC"; 
            model = new DefaultTableModel(null, titulos);
            sent = conn.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            String[]fila=new String[11];
            while(rs.next()){
                fila[0] = rs.getString("IDARTICULO");
                fila[1] = rs.getString("NOMBRECATEGORIA");
                fila[2] = rs.getString("NOMBREARTICULO");
                fila[3] = rs.getString("CANTIDADARTICULO");
                fila[4] = rs.getString("DESCRIPCIONARTICULO");
                fila[5] = rs.getString("IMAGENUNOARTICULO");
                fila[6] = rs.getString("IMAGENDOSARTICULO");
                fila[7] = rs.getString("IMAGENTRESARTICULO");
                fila[8] = rs.getString("SONIDOARTICULO");
                fila[9] = rs.getString("VIDEOARTICULO");
                fila[10] = rs.getString("IMAGENQRARTICULO");
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
    
    public void LimpiarTablaEImagenes(){
        jtContenidosArticulos.setFocusable(false);
        jtContenidosArticulos.clearSelection();
        lblVistaPreviaImagen1.setIcon(null);
        lblVistaPreviaImagen2.setIcon(null);
        lblVistaPreviaImagen3.setIcon(null);
        lblVistaPreviaImagen4.setIcon(null);
        idA = "";
        lblEtiquetaPreviewImagenes.setVisible(false);
        lblEtiquetaPreviewQr.setVisible(false);
    }
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btgSeleccion = new javax.swing.ButtonGroup();
        jPanel5 = new javax.swing.JPanel();
        jlCategorias = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtContenidosArticulos = new javax.swing.JTable();
        btnActualizarArticulos = new javax.swing.JLabel();
        btnEliminarArticulos = new javax.swing.JLabel();
        btnBuscarArticulos = new javax.swing.JLabel();
        rbtnBuscarPorCategoria = new javax.swing.JRadioButton();
        rbtnBuscarPorNombre = new javax.swing.JRadioButton();
        txtBuscarArticulo = new javax.swing.JTextField();
        btnNuevosArticulos = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblEtiquetaPreviewImagenes = new javax.swing.JLabel();
        lblEtiquetaPreviewQr = new javax.swing.JLabel();
        lblVistaPreviaImagen2 = new javax.swing.JLabel();
        lblVistaPreviaImagen1 = new javax.swing.JLabel();
        lblVistaPreviaImagen3 = new javax.swing.JLabel();
        lblVistaPreviaImagen4 = new javax.swing.JLabel();
        lblTotaEspecies = new javax.swing.JLabel();
        btnImprimir = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblTotalArticulos = new javax.swing.JLabel();
        lblNuevoArticulo = new javax.swing.JLabel();
        lblActualizarA = new javax.swing.JLabel();
        lblEliminarArticulo = new javax.swing.JLabel();
        lblBuscarArticulo = new javax.swing.JLabel();

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

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

        btnEliminarArticulos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Eliminar1.png"))); // NOI18N
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

        rbtnBuscarPorCategoria.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        rbtnBuscarPorCategoria.setSelected(true);
        rbtnBuscarPorCategoria.setText("Categoría");
        rbtnBuscarPorCategoria.setEnabled(false);
        rbtnBuscarPorCategoria.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        rbtnBuscarPorCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnBuscarPorCategoriaActionPerformed(evt);
            }
        });

        rbtnBuscarPorNombre.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        rbtnBuscarPorNombre.setText("Nombre");
        rbtnBuscarPorNombre.setEnabled(false);
        rbtnBuscarPorNombre.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        rbtnBuscarPorNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnBuscarPorNombreActionPerformed(evt);
            }
        });

        txtBuscarArticulo.setEnabled(false);
        txtBuscarArticulo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarArticuloKeyReleased(evt);
            }
        });

        btnNuevosArticulos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Mas.png"))); // NOI18N
        btnNuevosArticulos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNuevosArticulos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNuevosArticulosMouseClicked(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("# Especies : ");

        lblEtiquetaPreviewImagenes.setText("Imagenes");

        lblEtiquetaPreviewQr.setText("Qr");

        lblVistaPreviaImagen2.setMaximumSize(new java.awt.Dimension(104, 140));
        lblVistaPreviaImagen2.setMinimumSize(new java.awt.Dimension(104, 140));
        lblVistaPreviaImagen2.setPreferredSize(new java.awt.Dimension(104, 140));

        lblVistaPreviaImagen1.setMaximumSize(new java.awt.Dimension(104, 140));
        lblVistaPreviaImagen1.setMinimumSize(new java.awt.Dimension(104, 140));
        lblVistaPreviaImagen1.setPreferredSize(new java.awt.Dimension(104, 140));

        lblVistaPreviaImagen3.setMaximumSize(new java.awt.Dimension(104, 140));
        lblVistaPreviaImagen3.setMinimumSize(new java.awt.Dimension(104, 140));
        lblVistaPreviaImagen3.setPreferredSize(new java.awt.Dimension(104, 140));

        lblVistaPreviaImagen4.setMaximumSize(new java.awt.Dimension(104, 140));
        lblVistaPreviaImagen4.setMinimumSize(new java.awt.Dimension(104, 140));
        lblVistaPreviaImagen4.setPreferredSize(new java.awt.Dimension(104, 140));

        lblTotaEspecies.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblTotaEspecies.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotaEspecies.setText("0");

        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/imprimir.png"))); // NOI18N
        btnImprimir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnImprimir.setMaximumSize(new java.awt.Dimension(84, 81));
        btnImprimir.setMinimumSize(new java.awt.Dimension(84, 81));
        btnImprimir.setPreferredSize(new java.awt.Dimension(84, 81));
        btnImprimir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnImprimirMouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("# Articulos : ");

        lblTotalArticulos.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblTotalArticulos.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalArticulos.setText("0");

        lblNuevoArticulo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblNuevoArticulo.setText("Nuevo");

        lblActualizarA.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblActualizarA.setText("Actualizar");

        lblEliminarArticulo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblEliminarArticulo.setText("Eliminar");

        lblBuscarArticulo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblBuscarArticulo.setText("Buscar");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 782, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlCategorias))
                .addGap(18, 18, 18))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(218, 218, 218)
                .addComponent(lblEtiquetaPreviewImagenes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblEtiquetaPreviewQr)
                .addGap(255, 255, 255))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(btnNuevosArticulos)
                                .addGap(42, 42, 42)
                                .addComponent(btnActualizarArticulos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(42, 42, 42)
                                .addComponent(btnEliminarArticulos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(42, 42, 42)
                                .addComponent(btnBuscarArticulos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(63, 63, 63)
                                .addComponent(lblNuevoArticulo)
                                .addGap(80, 80, 80)
                                .addComponent(lblActualizarA)
                                .addGap(69, 69, 69)
                                .addComponent(lblEliminarArticulo)
                                .addGap(85, 85, 85)
                                .addComponent(lblBuscarArticulo)))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(rbtnBuscarPorNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rbtnBuscarPorCategoria))
                                .addGap(32, 32, 32)
                                .addComponent(txtBuscarArticulo, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTotaEspecies, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTotalArticulos, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(lblVistaPreviaImagen1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblVistaPreviaImagen2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblVistaPreviaImagen3, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(94, 94, 94)
                        .addComponent(lblVistaPreviaImagen4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)
                        .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jlCategorias)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblNuevoArticulo)
                                    .addComponent(lblActualizarA)
                                    .addComponent(lblEliminarArticulo)
                                    .addComponent(lblBuscarArticulo))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                                        .addGap(18, 18, 18)
                                        .addComponent(rbtnBuscarPorNombre))
                                    .addComponent(txtBuscarArticulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(lblTotaEspecies)
                                    .addComponent(jLabel4)
                                    .addComponent(lblTotalArticulos))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31))
                            .addComponent(lblVistaPreviaImagen4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblEtiquetaPreviewImagenes)
                            .addComponent(lblEtiquetaPreviewQr))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblVistaPreviaImagen1, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                            .addComponent(lblVistaPreviaImagen2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblVistaPreviaImagen3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnImprimirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnImprimirMouseClicked
        Object [] opciones={"TODOS LOS QR", "QR UNICO", "CANCELAR"};
        int eleccion = JOptionPane.showOptionDialog(this, "Escoja el modo de impresión", "Imprimir",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE, null, opciones, "Aceptar");
        if(eleccion==JOptionPane.YES_OPTION){
            ItemSeleccionado.accionBoton = "ImprimirTotal";
            ImprimirQRs iqr = new ImprimirQRs();
            iqr.setVisible(true);
        } else if(eleccion == JOptionPane.NO_OPTION) {
            if(!idA.isEmpty()){
                ItemSeleccionado.accionBoton = "ImprimirParcial";
                isA.setIdArticulo(idA);
                ImprimirQRs iqr = new ImprimirQRs();
                iqr.setVisible(true);
            }else JOptionPane.showMessageDialog(this, "Busque y Seleccione un registro para imprimir");
        }
        LimpiarTablaEImagenes();
    }//GEN-LAST:event_btnImprimirMouseClicked

    private void btnNuevosArticulosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNuevosArticulosMouseClicked
        isA.setAccionBoton("Guardar");
        internalNuevoQr = new jifrNuevoQr();
        centrarVentanaNuevoQr(internalNuevoQr);
    }//GEN-LAST:event_btnNuevosArticulosMouseClicked

    private void rbtnBuscarPorNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnBuscarPorNombreActionPerformed
        txtBuscarArticulo.setText("");
    }//GEN-LAST:event_rbtnBuscarPorNombreActionPerformed

    private void rbtnBuscarPorCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnBuscarPorCategoriaActionPerformed
        txtBuscarArticulo.setText("");
    }//GEN-LAST:event_rbtnBuscarPorCategoriaActionPerformed

    private void btnBuscarArticulosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarArticulosMouseClicked
        // TODO add your handling code here:
        rbtnBuscarPorCategoria.setEnabled(true);
        rbtnBuscarPorNombre.setEnabled(true);
        txtBuscarArticulo.setEnabled(true);
        txtBuscarArticulo.requestFocus();
    }//GEN-LAST:event_btnBuscarArticulosMouseClicked

    private void btnEliminarArticulosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarArticulosMouseClicked
        // TODO add your handling code here:
        Object [] opciones={"Aceptar","Cancelar"};
        if(!idA.isEmpty()){
            int eleccion=JOptionPane.showOptionDialog(this, "Está seguro que desea eliminar", "Eliminar",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,opciones,"Aceptar");
            if(eleccion==JOptionPane.YES_OPTION) EliminarArticulos();
        }else JOptionPane.showMessageDialog(this, "No ha seleccionado un registro a eliminar");
    }//GEN-LAST:event_btnEliminarArticulosMouseClicked

    private void btnActualizarArticulosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnActualizarArticulosMouseClicked
        // TODO add your handling code here:
        if(!idA.isEmpty()){
            isA.setAccionBoton("Actualizar");
            isA.setIdArticulo(idA);
            isA.setIdCategoria(categoria);
            LimpiarTablaEImagenes();
            internalNuevoQr = new jifrNuevoQr();
            centrarVentanaNuevoQr(internalNuevoQr);
        }else JOptionPane.showMessageDialog(this, "No ha seleccionado un registro a modificar");
    }//GEN-LAST:event_btnActualizarArticulosMouseClicked

    private void jtContenidosArticulosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtContenidosArticulosMouseClicked
        // TODO add your handling code here:
        SeleccionarItemTablaCA(evt);
    }//GEN-LAST:event_jtContenidosArticulosMouseClicked

    private void txtBuscarArticuloKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarArticuloKeyReleased
        if(rbtnBuscarPorNombre.isSelected()) BuscarPorNombreArticulo();
        else BuscarPorCategoriaArticulo();
    }//GEN-LAST:event_txtBuscarArticuloKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btgSeleccion;
    private javax.swing.JLabel btnActualizarArticulos;
    private javax.swing.JLabel btnBuscarArticulos;
    private javax.swing.JLabel btnEliminarArticulos;
    private javax.swing.JLabel btnImprimir;
    private javax.swing.JLabel btnNuevosArticulos;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jlCategorias;
    public static javax.swing.JTable jtContenidosArticulos;
    private javax.swing.JLabel lblActualizarA;
    private javax.swing.JLabel lblBuscarArticulo;
    private javax.swing.JLabel lblEliminarArticulo;
    public javax.swing.JLabel lblEtiquetaPreviewImagenes;
    public javax.swing.JLabel lblEtiquetaPreviewQr;
    private javax.swing.JLabel lblNuevoArticulo;
    private javax.swing.JLabel lblTotaEspecies;
    private javax.swing.JLabel lblTotalArticulos;
    private javax.swing.JLabel lblVistaPreviaImagen1;
    private javax.swing.JLabel lblVistaPreviaImagen2;
    private javax.swing.JLabel lblVistaPreviaImagen3;
    private javax.swing.JLabel lblVistaPreviaImagen4;
    private javax.swing.JRadioButton rbtnBuscarPorCategoria;
    private javax.swing.JRadioButton rbtnBuscarPorNombre;
    private javax.swing.JTextField txtBuscarArticulo;
    // End of variables declaration//GEN-END:variables
}
