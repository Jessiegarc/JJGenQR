/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generadorqr;

import Modelos.ItemSeleccionado;
import Modelos.UsuarioIngresado;
import Modelos.ValoresConstantes;
import com.mysql.jdbc.StringUtils;
import db.Categorias;
import db.ConexionBase;
import db.mysql;
import java.awt.Color;
import java.sql.Connection;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
import java.awt.Image;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
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
int idCategoria = 0;
Vector<Categorias> categorias;
DefaultComboBoxModel mdlC;


    public jifrGestionArticulos() {
        initComponents();
        if(conn == null) conn = mysql.getConnect();
        if (LlenarTablaArticulos() != null) jtContenidosArticulos.setModel(LlenarTablaArticulos());
        btgSeleccion.add(rbtnBuscarPorCategoria);
        btgSeleccion.add(rbtnBuscarPorNombre);
        rbtnBuscarPorCategoria.setVisible(false);
        rbtnBuscarPorNombre.setVisible(false);
        
        contarTotalE();
        sumarTotalA();
        lblEtiquetaPreviewImagenes.setVisible(false);
        lblEtiquetaPreviewQr.setVisible(false);
        jcbBuscarQrCategoría.setVisible(false);
        if(UsuarioIngresado.parametroR.contains("Consultor/a")) {
            btnNuevosArticulos.setVisible(false);
            lblNuevoArticulo.setVisible(false);
            btnActualizarArticulos.setVisible(false);
            lblActualizarA.setVisible(false);
            btnEliminarArticulos.setVisible(false);
            lblEliminarArticulo.setVisible(false);
            lblBuscarArticulo.setVisible(false);
        }
        
        String SQLC="SELECT IDCATEGORIA,NOMBRECATEGORIA,DESCRIPCIONCATEGORIA FROM categorias";
        mdlC= new DefaultComboBoxModel(ConexionBase.leerDatosVector1(SQLC));
        categorias = ConexionBase.leerDatosVector1(SQLC);
        this.jcbBuscarQrCategoría.setModel(mdlC);
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
            String SQL ="SELECT SUM(CANTIDADARTICULO) AS Suma FROM articulos where IDCATEGORIA="+ItemSeleccionado.idCategoria;
            sent = conn.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            lblTotalArticulos.setText("");
            lblTotaEspecies.setText("");
            while(rs.next()){
                lblTotalArticulos.setText(rs.getString("Suma"));
                lblTotaEspecies.setText("1");
            }
        } catch (SQLException e) {
            lblTotalArticulos.setText("null");
        }
    } 
    
    void sumarTotalABuscados(){
        try {
            String SQL ="SELECT SUM(CANTIDADARTICULO) AS Suma FROM articulos where NOMBREARTICULO Like '%"+txtBuscarArticulo.getText().toString().trim()+"%'";
            sent = conn.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            lblTotalArticulos.setText("");
            while(rs.next()){
                lblTotalArticulos.setText(rs.getString("Suma"));
            }
        } catch (SQLException e) {
            lblTotalArticulos.setText("null");
        }
    }
    
    void sumarTotalABuscadosE(){
        try {
            String SQL ="SELECT COUNT(*) AS Suma FROM articulos where NOMBREARTICULO Like '%"+txtBuscarArticulo.getText().toString().trim()+"%'GROUP BY IDCATEGORIA ORDER BY NOMBREARTICULO ASC";
            sent = conn.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            lblTotaEspecies.setText("");
            while(rs.next()) {
                //lblTotalArticulos.setText(rs.getString("Suma"));
                lblTotaEspecies.setText(String.valueOf(rs.getInt("Suma")));
            }
        } catch (SQLException e) {
            lblTotalArticulos.setText("null");
        }
    }
       
    void contarTotalESeleccionada(){
        try {
            String SQL ="SELECT SUM(*) as SUM from articulos where IDCATEGORIA="+ItemSeleccionado.idCategoria;
            sent = conn.createStatement();
            ResultSet rs = sent.executeQuery(SQL);
            lblTotalArticulos.setText("");
            lblTotaEspecies.setText("");
            while(rs.next()){
                lblTotalArticulos.setText(String.valueOf(rs.getInt("SUM")));
                lblTotaEspecies.setText("1");
            }
        } catch (SQLException e) {
            lblTotalArticulos.setText("null");
        }
    }  
    
    public static DefaultTableModel LlenarTablaArticulos(){
        try{
            String titulos[] = {"ID","CATEGORIA","NOMBRE","CANTIDAD","DESCRIPCION","IMG 1","IMG 2",
                "IMG 3","SONIDO","VIDEO","IMAGEN QR"};
            String SQLTA ="SELECT a.IDARTICULO, c.NOMBRECATEGORIA, a.NOMBREARTICULO,a.CANTIDADARTICULO, a.DESCRIPCIONARTICULO, a.IMAGENUNOARTICULO, a.IMAGENDOSARTICULO, a.IMAGENTRESARTICULO, a.SONIDOARTICULO, a.VIDEOARTICULO, a.IMAGENQRARTICULO FROM articulos AS a INNER JOIN categorias AS c USING(IDCATEGORIA) ORDER BY a.IDARTICULO, c.NOMBRECATEGORIA, a.NOMBREARTICULO ASC"; 
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
    
    public static DefaultTableModel LlenarTablaArticulosporCategoría(){
        try{
            String titulos[] = {"ID","CATEGORIA","NOMBRE","CANTIDAD","DESCRIPCION","IMG 1","IMG 2",
                "IMG 3","SONIDO","VIDEO","IMAGEN QR"};
            String SQLTA ="SELECT a.IDARTICULO, c.NOMBRECATEGORIA, a.NOMBREARTICULO,a.CANTIDADARTICULO, a.DESCRIPCIONARTICULO, a.IMAGENUNOARTICULO, a.IMAGENDOSARTICULO, a.IMAGENTRESARTICULO, a.SONIDOARTICULO, a.VIDEOARTICULO, a.IMAGENQRARTICULO FROM articulos AS a INNER JOIN categorias AS c USING(IDCATEGORIA) WHERE c.IDCATEGORIA = "+ItemSeleccionado.idCategoria+" ORDER BY a.IDARTICULO ASC"; 
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
            String titulos[] = {"ID","CATEGORIA","NOMBRE","CANTIDAD","DESCRIPCION","IMG 1","IMG 2","IMG 3","SONIDO","VIDEO","IMAGEN QR"};
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
            String titulos[] = {"ID","CATEGORIA","NOMBRE","CANTIDAD","DESCRIPCION","IMG 1","IMG 2","IMG 3","SONIDO","VIDEO","IMAGEN QR"};
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
        jtContenidosArticulos = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        btnActualizarArticulos = new javax.swing.JLabel();
        btnEliminarArticulos = new javax.swing.JLabel();
        btnBuscarArticulos = new javax.swing.JLabel();
        rbtnBuscarPorCategoria = new javax.swing.JRadioButton();
        rbtnBuscarPorNombre = new javax.swing.JRadioButton();
        txtBuscarArticulo = new javax.swing.JTextField();
        btnNuevosArticulos = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblTotaEspecies = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblTotalArticulos = new javax.swing.JLabel();
        lblNuevoArticulo = new javax.swing.JLabel();
        lblActualizarA = new javax.swing.JLabel();
        lblEliminarArticulo = new javax.swing.JLabel();
        lblBuscarArticulo = new javax.swing.JLabel();
        jcbBuscarQrCategoría = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblVistaPreviaImagen1 = new javax.swing.JLabel();
        lblVistaPreviaImagen2 = new javax.swing.JLabel();
        lblVistaPreviaImagen3 = new javax.swing.JLabel();
        lblVistaPreviaImagen4 = new javax.swing.JLabel();
        lblEtiquetaPreviewImagenes = new javax.swing.JLabel();
        lblEtiquetaPreviewQr = new javax.swing.JLabel();
        btnImprimir = new javax.swing.JLabel();

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jlCategorias.setFont(new java.awt.Font("Nirmala UI", 1, 18)); // NOI18N
        jlCategorias.setText("Galería de Piezas de Arte ");

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

        btnActualizarArticulos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/actualizarjif.png"))); // NOI18N
        btnActualizarArticulos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActualizarArticulos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnActualizarArticulosMouseClicked(evt);
            }
        });

        btnEliminarArticulos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/eliminarjif.png"))); // NOI18N
        btnEliminarArticulos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminarArticulos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEliminarArticulosMouseClicked(evt);
            }
        });

        btnBuscarArticulos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/searchjif.png"))); // NOI18N
        btnBuscarArticulos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
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
        txtBuscarArticulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarArticuloActionPerformed(evt);
            }
        });
        txtBuscarArticulo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarArticuloKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarArticuloKeyReleased(evt);
            }
        });

        btnNuevosArticulos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Mas.png"))); // NOI18N
        btnNuevosArticulos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNuevosArticulos.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                btnNuevosArticulosMouseDragged(evt);
            }
        });
        btnNuevosArticulos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNuevosArticulosMouseClicked(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("# Especies : ");

        lblTotaEspecies.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblTotaEspecies.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotaEspecies.setText("0");

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

        jcbBuscarQrCategoría.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbBuscarQrCategoría.setEnabled(false);
        jcbBuscarQrCategoría.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbBuscarQrCategoríaItemStateChanged(evt);
            }
        });
        jcbBuscarQrCategoría.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbBuscarQrCategoríaActionPerformed(evt);
            }
        });

        jLabel1.setText("Categoría");

        jLabel2.setText("Nombre");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

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

        lblEtiquetaPreviewImagenes.setText("Imágenes");

        lblEtiquetaPreviewQr.setText("Qr");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(lblVistaPreviaImagen1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblVistaPreviaImagen3, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblVistaPreviaImagen2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(108, 108, 108)
                        .addComponent(lblVistaPreviaImagen4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(184, 184, 184)
                        .addComponent(lblEtiquetaPreviewImagenes)
                        .addGap(314, 314, 314)
                        .addComponent(lblEtiquetaPreviewQr)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblEtiquetaPreviewImagenes)
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER, false)
                            .addComponent(lblVistaPreviaImagen2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(lblVistaPreviaImagen1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblVistaPreviaImagen3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblEtiquetaPreviewQr)
                        .addGap(0, 0, 0)
                        .addComponent(lblVistaPreviaImagen4, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(23, 23, Short.MAX_VALUE))
        );

        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/imprimirjif.png"))); // NOI18N
        btnImprimir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnImprimir.setMaximumSize(new java.awt.Dimension(84, 81));
        btnImprimir.setMinimumSize(new java.awt.Dimension(84, 81));
        btnImprimir.setPreferredSize(new java.awt.Dimension(84, 81));
        btnImprimir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnImprimirMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(btnNuevosArticulos)
                        .addGap(20, 20, 20)
                        .addComponent(btnActualizarArticulos)
                        .addGap(20, 20, 20)
                        .addComponent(btnEliminarArticulos)
                        .addGap(20, 20, 20)
                        .addComponent(btnBuscarArticulos))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(lblNuevoArticulo)
                        .addGap(65, 65, 65)
                        .addComponent(lblActualizarA)
                        .addGap(60, 60, 60)
                        .addComponent(lblEliminarArticulo)
                        .addGap(60, 60, 60)
                        .addComponent(lblBuscarArticulo)))
                .addGap(75, 75, 75)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(63, 63, 63)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtBuscarArticulo, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                            .addComponent(jcbBuscarQrCategoría, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotaEspecies, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotalArticulos, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(310, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(rbtnBuscarPorCategoria)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(116, 116, 116)
                                .addComponent(jlCategorias))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rbtnBuscarPorNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 633, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(18, 18, 18))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jlCategorias))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rbtnBuscarPorCategoria)
                        .addComponent(rbtnBuscarPorNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jcbBuscarQrCategoría, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBuscarArticulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(lblTotaEspecies)
                            .addComponent(jLabel4)
                            .addComponent(lblTotalArticulos))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnImprimirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnImprimirMouseClicked
        /*if(jcbBuscarQrCategoría.getSelectedIndex() != 0){
            Object [] opciones={"ACEPTAR", "CANCELAR"};
            int eleccion = JOptionPane.showOptionDialog(this, "¿Esta seguro de imprimir los QR por categoria", "Imprimir",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, opciones, "Aceptar");
            if(eleccion==JOptionPane.YES_OPTION){
                ItemSeleccionado.accionBoton = "ImprimirXCategoria";
                ImprimirQRs iqr = new ImprimirQRs();
                iqr.setVisible(true);
            }
        }*/
        
            Object [] opciones={"TODOS LOS QR", "QR UNICO","POR CATEGORÍA"};
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
            else {
                 if(jcbBuscarQrCategoría.getSelectedIndex() != 0){
                ItemSeleccionado.accionBoton = "ImprimirXCategoria";
                ImprimirQRs iqr = new ImprimirQRs();
                iqr.setVisible(true);
                 }
                 else JOptionPane.showMessageDialog(this, "Seleccione una categoría");
            }
        
        LimpiarTablaEImagenes();
    }//GEN-LAST:event_btnImprimirMouseClicked

    private void btnNuevosArticulosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNuevosArticulosMouseClicked
        isA.setAccionBoton("Guardar");
        internalNuevoQr = new jifrNuevoQr();
        centrarVentanaNuevoQr(internalNuevoQr);
        btnNuevosArticulos.setBackground(Color.red);
    }//GEN-LAST:event_btnNuevosArticulosMouseClicked

    private void rbtnBuscarPorNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnBuscarPorNombreActionPerformed
        txtBuscarArticulo.setText("");
        txtBuscarArticulo.setEnabled(true);
        txtBuscarArticulo.requestFocus();
        txtBuscarArticulo.setVisible(true);
        String SQLC="SELECT IDCATEGORIA,NOMBRECATEGORIA,DESCRIPCIONCATEGORIA FROM categorias";
        mdlC= new DefaultComboBoxModel(ConexionBase.leerDatosVector1(SQLC));
        categorias = ConexionBase.leerDatosVector1(SQLC);
        this.jcbBuscarQrCategoría.setModel(mdlC);
    }//GEN-LAST:event_rbtnBuscarPorNombreActionPerformed

    private void rbtnBuscarPorCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnBuscarPorCategoriaActionPerformed
        txtBuscarArticulo.setText("");
        jcbBuscarQrCategoría.setVisible(true);
        jcbBuscarQrCategoría.setEnabled(true);
    }//GEN-LAST:event_rbtnBuscarPorCategoriaActionPerformed

    private void btnBuscarArticulosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarArticulosMouseClicked
        // TODO add your handling code here:
        rbtnBuscarPorCategoria.setEnabled(true);
        rbtnBuscarPorNombre.setEnabled(true);
        txtBuscarArticulo.setText("");
        txtBuscarArticulo.setEnabled(true);
        txtBuscarArticulo.requestFocus();
        txtBuscarArticulo.setVisible(true);
        //jcbBuscarQrCategoría.setSelectedIndex(0);
        String SQLC="SELECT IDCATEGORIA,NOMBRECATEGORIA,DESCRIPCIONCATEGORIA FROM categorias";
        mdlC= new DefaultComboBoxModel(ConexionBase.leerDatosVector1(SQLC));
        categorias = ConexionBase.leerDatosVector1(SQLC);
        this.jcbBuscarQrCategoría.setModel(mdlC);
        txtBuscarArticulo.setText("");
        jcbBuscarQrCategoría.setVisible(true);
        jcbBuscarQrCategoría.setEnabled(true);
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
        BuscarPorNombreArticulo();
        sumarTotalABuscadosE();
        sumarTotalABuscados();
        String SQLC="SELECT IDCATEGORIA,NOMBRECATEGORIA,DESCRIPCIONCATEGORIA FROM categorias";
        mdlC= new DefaultComboBoxModel(ConexionBase.leerDatosVector1(SQLC));
        categorias = ConexionBase.leerDatosVector1(SQLC);
        this.jcbBuscarQrCategoría.setModel(mdlC);
        /*if(rbtnBuscarPorNombre.isSelected()) BuscarPorNombreArticulo();
        else BuscarPorCategoriaArticulo();*/
    }//GEN-LAST:event_txtBuscarArticuloKeyReleased

    private void btnNuevosArticulosMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNuevosArticulosMouseDragged
        btnNuevosArticulos.setBackground(Color.red);
    }//GEN-LAST:event_btnNuevosArticulosMouseDragged

    private void jcbBuscarQrCategoríaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbBuscarQrCategoríaItemStateChanged
        idCategoria = categorias.get(jcbBuscarQrCategoría.getSelectedIndex()).getIdCategoria();
        ItemSeleccionado.idCategoria = String.valueOf(idCategoria);
        if(idCategoria == 0){
            JOptionPane.showMessageDialog(this, "Debe de seleccionar una categoría");
            return;
        } else {
            jtContenidosArticulos.setModel(LlenarTablaArticulosporCategoría());
            contarTotalESeleccionada();
        }
    }//GEN-LAST:event_jcbBuscarQrCategoríaItemStateChanged

    private void txtBuscarArticuloKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarArticuloKeyPressed
        //jcbBuscarQrCategoría.setSelectedIndex(0);
       
        String SQLC="SELECT IDCATEGORIA,NOMBRECATEGORIA,DESCRIPCIONCATEGORIA FROM categorias";
        mdlC= new DefaultComboBoxModel(ConexionBase.leerDatosVector1(SQLC));
        categorias = ConexionBase.leerDatosVector1(SQLC);
        this.jcbBuscarQrCategoría.setModel(mdlC);
    }//GEN-LAST:event_txtBuscarArticuloKeyPressed

    private void txtBuscarArticuloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarArticuloActionPerformed
         String SQLC="SELECT IDCATEGORIA,NOMBRECATEGORIA,DESCRIPCIONCATEGORIA FROM categorias";
        mdlC= new DefaultComboBoxModel(ConexionBase.leerDatosVector1(SQLC));
        categorias = ConexionBase.leerDatosVector1(SQLC);
        this.jcbBuscarQrCategoría.setModel(mdlC);
    }//GEN-LAST:event_txtBuscarArticuloActionPerformed

    private void jcbBuscarQrCategoríaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbBuscarQrCategoríaActionPerformed
        // TODO add your handling code here:
        txtBuscarArticulo.setText("");
        sumarTotalA();
    }//GEN-LAST:event_jcbBuscarQrCategoríaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btgSeleccion;
    public static javax.swing.JLabel btnActualizarArticulos;
    private javax.swing.JLabel btnBuscarArticulos;
    public static javax.swing.JLabel btnEliminarArticulos;
    private javax.swing.JLabel btnImprimir;
    public static javax.swing.JLabel btnNuevosArticulos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    public static javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> jcbBuscarQrCategoría;
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
