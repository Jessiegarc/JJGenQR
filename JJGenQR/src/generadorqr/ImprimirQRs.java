package generadorqr;

import Modelos.InformacionImprimirQR;
import Modelos.ItemSeleccionado;
import Modelos.ValoresConstantes;
import java.awt.BorderLayout;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import org.icepdf.ri.common.MyAnnotationCallback;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

public class ImprimirQRs extends javax.swing.JFrame {
    private static final String RUTA_TEMPORAL = ValoresConstantes.DIRECTORIO_PRINCIPAL + "\\temporalQr.pdf";
    SwingController controlador;
    InformacionImprimirQR iiqr = new InformacionImprimirQR();
    
    public ImprimirQRs() {
        initComponents();
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        LinkedList info;
        info = iiqr.getInformacion();
        try 
        { 
            JasperReport reporte;
            reporte = (JasperReport) JRLoader.loadObject(new File(getClass().getResource("/Modelos/ImprimirQR/imprimirImagenQR.jasper").getPath()));
            Map parametros = new HashMap();
            parametros.put("imagen", getClass().getResource("/images/imagenFondo.jpg").getPath());
            JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, parametros, new JRBeanCollectionDataSource(info));
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE, new File(RUTA_TEMPORAL));
            exporter.exportReport();
        } 
        catch(JRException e) 
        { 
            JOptionPane.showMessageDialog(null, e.getMessage(),"Error al generar PDF", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public void detener() {
	if (controlador != null) {
		controlador.closeDocument();
	}

    }

    public void destruir() {
	if (controlador != null) {
		controlador.dispose();
		controlador = null;
	}
	jpVisualizador.removeAll();
    }

    public void dibujarGIU(){
	controlador = new SwingController();
	SwingViewBuilder factory = new SwingViewBuilder(controlador);
	controlador.getDocumentViewController().setAnnotationCallback(new MyAnnotationCallback(controlador.getDocumentViewController()));
        MyAnnotationCallback myAnnotationCallback = new MyAnnotationCallback(controlador.getDocumentViewController());
	controlador.getDocumentViewController().setAnnotationCallback(myAnnotationCallback);
        jpVisualizador.setLayout(new BorderLayout());
	JScrollPane scroll = new JScrollPane(factory.buildViewerPanel(),JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jpVisualizador.add(scroll, BorderLayout.CENTER);   
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpVisualizador = new javax.swing.JPanel();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        javax.swing.GroupLayout jpVisualizadorLayout = new javax.swing.GroupLayout(jpVisualizador);
        jpVisualizador.setLayout(jpVisualizadorLayout);
        jpVisualizadorLayout.setHorizontalGroup(
            jpVisualizadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jpVisualizadorLayout.setVerticalGroup(
            jpVisualizadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpVisualizador, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpVisualizador, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        dibujarGIU();
        controlador.openDocument(RUTA_TEMPORAL);
        jpVisualizador.updateUI();
    }//GEN-LAST:event_formWindowOpened

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        detener();
        destruir();
        ItemSeleccionado.accionBoton = "";
        ItemSeleccionado.idArticulo = "";
    }//GEN-LAST:event_formWindowClosed
    
    public static void main(String args[]) {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ImprimirQRs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ImprimirQRs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ImprimirQRs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ImprimirQRs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        java.awt.EventQueue.invokeLater(() -> {
            new ImprimirQRs().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jpVisualizador;
    // End of variables declaration//GEN-END:variables
}
