/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Otros.VistaPersonalizada;
import java.io.File;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

/**
 *
 * @author mam28
 */
public class VistaIntroduccion extends VistaPersonalizada {
    private JFXPanel panel;
    private WebView webView;
    
    /**
     * Creates new form VistaIntroduccion
     */
    public VistaIntroduccion() {
        initComponents();

        panel = new JFXPanel();
        this.add(panel);
        panel.setSize(this.getSize());
        
        Platform.runLater(() -> {
            webView = new WebView();
            panel.setScene(new Scene(webView));
            
            File html = new File("src/Imagenes/Intro/intro.html");
            webView.getEngine().load(html.toURI().toString());
        
            this.setVisible(true);
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 394, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 274, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void cerrarVista(){
        Platform.runLater(() -> {
            webView.getEngine().load("");
        });
        this.dispose();
    }

    public JFXPanel getPanel() {
        return panel;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
