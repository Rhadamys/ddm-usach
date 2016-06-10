/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Modelos.Criatura;
import Otros.BotonImagen;
import Otros.PanelImagen;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 *
 * @author mam28
 */
public class SubVistaSeleccionCriatura extends javax.swing.JInternalFrame {
    private final ArrayList<BotonImagen> panelesCriaturas;
    private final ArrayList<Criatura> criaturas;
    
    /**
     * Creates new form SubVistaSeleccionCriatura
     * @param criaturas
     * @param fuente
     */
    public SubVistaSeleccionCriatura(ArrayList<Criatura> criaturas, Font fuente) {
        initComponents();
        
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        
        this.setBorder(null);
        this.setLayout(null);
        this.setOpaque(false);
        this.setBackground(new Color(0,0,0,0));
        
        this.criaturas = criaturas;
        this.panelesCriaturas = new ArrayList();
        
        final int N_COLUMNAS = criaturas.size();
        final int LADO = 100;
        final int SEP = (640 - N_COLUMNAS * LADO) / (N_COLUMNAS + 1);
        final int MARCO = 20;
        int columna = 0;
        int fila = -1;
        
        for (Criatura criatura: criaturas){
            fila = columna == 0 ? ++fila : fila;
            
            BotonImagen marcoCriatura = new BotonImagen("/Imagenes/vacio.png");
            this.add(marcoCriatura);
            marcoCriatura.setSize(LADO + MARCO, LADO + MARCO);
            marcoCriatura.setImagenSobre("/Imagenes/Otros/marco_seleccion.png");
            marcoCriatura.setLocation((SEP + LADO) * columna + SEP - MARCO / 2 + 80, (this.getHeight() - LADO - MARCO) / 2);
            
            PanelImagen iconoCriatura = new PanelImagen("/Imagenes/Criaturas/"
                    + criatura.getNomArchivoImagen() + ".png");
            this.add(iconoCriatura);
            iconoCriatura.setSize(LADO, LADO);
            iconoCriatura.setLocation((SEP + LADO) * columna + SEP + 80, (this.getHeight() - LADO) / 2);
            
            panelesCriaturas.add(marcoCriatura);

            columna = columna == (N_COLUMNAS - 1)? 0: ++columna;
        }
        
        PanelImagen panelFondo = new PanelImagen("/Imagenes/Fondos/fondo_seleccion_3.png");
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
        setMaximumSize(new java.awt.Dimension(800, 600));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));
        getContentPane().setLayout(null);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public BotonImagen getPanelCriatura(int i) {
        return panelesCriaturas.get(i);
    } 
    
    public Criatura getCriatura(BotonImagen panelCriatura){
        return this.criaturas.get(this.panelesCriaturas.indexOf(panelCriatura));
    }
    
    public int getCantidadCriaturas(){
        return this.panelesCriaturas.size();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}