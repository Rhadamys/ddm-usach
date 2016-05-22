/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Otros.BotonImagen;
import Otros.PanelImagen;
import java.awt.Color;
import java.awt.Font;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 *
 * @author mam28
 */
public class VistaNuevaPartida extends javax.swing.JInternalFrame {
    private BotonImagen agregar = new BotonImagen("/Imagenes/Botones/boton_cuadrado.png");
    
    /**
     * Creates new form VistaNuevaPartida
     * @param fuentePersonalizada Fuente que se utilizará en esta vista.
     */
    public VistaNuevaPartida(Font fuentePersonalizada) {
        initComponents();
        
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        
        this.add(agregar);
        agregar.setImagenSobre("/Imagenes/Botones/boton_cuadrado_sobre.png");
        agregar.setImagenPresionado("/Imagenes/Botones/boton_cuadrado_presionado.png");
        agregar.setSize(100, 100);
        agregar.setLocation(40, 475);
        agregar.setForeground(Color.orange);
        
        PanelImagen panelFondo = new PanelImagen("/Imagenes/Fondos/fondo_seleccion.png");
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
        getContentPane().setLayout(null);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public BotonImagen getAgregar() {
        return agregar;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
