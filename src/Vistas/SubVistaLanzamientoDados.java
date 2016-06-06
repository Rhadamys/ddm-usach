/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Modelos.Dado;
import Otros.PanelImagen;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 *
 * @author mam28
 */
public class SubVistaLanzamientoDados extends javax.swing.JInternalFrame {
    
    /**
     * Creates new form CompLanzamientoDados
     * @param caras
     * @param dados
     */
    public SubVistaLanzamientoDados(int[] caras, ArrayList<Dado> dados) {
        initComponents();
        
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        
        this.setLayout(null);
        this.setBorder(null);
        this.setOpaque(false);
        this.setBackground(new Color(0,0,0,0));
        
        final int ANCHO = 180;
        final int ALTO = ANCHO * 2;
        final int SEP = (800 - ANCHO * dados.size()) / (dados.size() + 1);
        
        for(int i = 0; i < dados.size(); i++){
            PanelImagen dado = new PanelImagen("/Imagenes/Dados/dado_" + dados.get(i).getNivel() 
                    + "_" + caras[i] + ".gif");
            this.add(dado);
            dado.setSize(ANCHO, ALTO);
            dado.setLocation((SEP + ANCHO) * i + SEP, (this.getHeight() - ALTO) / 2);
        }
        
        PanelImagen panelFondo = new PanelImagen("/Imagenes/Fondos/fondo_lanzamiento_dados.png");
        this.add(panelFondo);
        panelFondo.setSize(this.getSize());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(51, 51, 51));
        setBorder(null);
        setMaximumSize(new java.awt.Dimension(800, 600));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 806, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 602, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
