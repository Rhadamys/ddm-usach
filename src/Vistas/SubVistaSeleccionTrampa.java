/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Modelos.Trampa;
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
public class SubVistaSeleccionTrampa extends javax.swing.JInternalFrame {
    private final ArrayList<BotonImagen> panelesTrampas;
    private final ArrayList<Trampa> trampas;

    /**
     * Creates new form SubVistaSeleccionTrampa
     * @param fuente Fuente que se utilizará en esta vista.
     * @param trampas Trampas del jugador.
     * @param puntosTrampa Puntos de trampa que tiene el jugador.
     */
    public SubVistaSeleccionTrampa(Font fuente, ArrayList<Trampa> trampas, int puntosTrampa) {
        initComponents();
        
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        
        this.setBorder(null);
        this.setLayout(null);
        this.setOpaque(false);
        this.setBackground(new Color(0,0,0,0));
        
        this.panelesTrampas = new ArrayList();
        this.trampas = trampas;
        
        final int N_COLUMNAS = 3;
        final int LADO = 100;
        final int SEP = (640 - N_COLUMNAS * LADO) / (N_COLUMNAS + 1);
        final int MARCO = 20;
        int columna = 0;
        int fila = -1;
        
        for (Trampa trampa: trampas){
            if(puntosTrampa >= trampa.getCosto()){
                fila = columna == 0 ? ++fila : fila;

                BotonImagen marcoTrampa = new BotonImagen("/Imagenes/vacio.png");
                this.add(marcoTrampa);
                marcoTrampa.setSize(LADO + MARCO, LADO + MARCO);
                marcoTrampa.setImagenSobre("/Imagenes/Otros/marco_seleccion.png");
                marcoTrampa.setLocation((SEP + LADO) * columna + SEP - MARCO / 2 + 80, (SEP + LADO) * fila + SEP + 60 - MARCO / 2);

                PanelImagen iconoTrampa = new PanelImagen("/Imagenes/Botones/"
                        + trampa.getNomArchivoImagen() + ".png");
                this.add(iconoTrampa);
                iconoTrampa.setSize(LADO, LADO);
                iconoTrampa.setLocation((SEP + LADO) * columna + SEP + 80, (SEP + LADO) * fila + SEP + 60);

                panelesTrampas.add(marcoTrampa);

                columna = columna == (N_COLUMNAS - 1)? 0: ++columna;
            }
        }
        
        this.titulo.setFont(new Font(fuente.getName(), Font.TRUETYPE_FONT, 24));
        
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

        titulo = new javax.swing.JLabel();

        setBackground(new java.awt.Color(51, 51, 51));
        setMaximumSize(new java.awt.Dimension(800, 600));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));
        getContentPane().setLayout(null);

        titulo.setFont(new java.awt.Font("Consolas", 0, 24)); // NOI18N
        titulo.setForeground(new java.awt.Color(255, 255, 255));
        titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titulo.setText("Selecciona la trampa que deseas colocar.");
        getContentPane().add(titulo);
        titulo.setBounds(0, 10, 790, 40);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public BotonImagen getPanelTrampa(int i){
        return this.panelesTrampas.get(i);
    }
    
    public int cantidadTrampas(){
        return this.panelesTrampas.size();
    }
    
    public Trampa getTrampa(BotonImagen panelTrampa){
        return this.trampas.get(this.panelesTrampas.indexOf(panelTrampa));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel titulo;
    // End of variables declaration//GEN-END:variables
}
