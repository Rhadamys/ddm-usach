/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Modelos.Trampa;
import Otros.BotonImagen;
import Otros.Constantes;
import Otros.PanelImagen;
import Otros.VistaPersonalizada;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 *
 * @author mam28
 */
public class SubVistaSeleccionTrampa extends VistaPersonalizada implements MouseListener{
    private final ArrayList<BotonImagen> panelesTrampas;
    private final ArrayList<Trampa> trampas;
    private final BotonImagen volver;

    /**
     * Creates new form SubVistaSeleccionTrampa
     * @param trampas Trampas del jugador.
     * @param puntosTrampa Puntos de trampa que tiene el jugador.
     */
    public SubVistaSeleccionTrampa(ArrayList<Trampa> trampas, int puntosTrampa) {
        initComponents();
        
        this.panelesTrampas = new ArrayList();
        this.trampas = trampas;
        
        this.volver = new BotonImagen(Constantes.BTN_ATRAS);
        this.add(this.volver);
        this.volver.setLocation(20, 20);
        this.volver.setSize(50, 50);
        
        this.volver.addMouseListener(this);
        
        final int N_COLUMNAS = 3;
        final int LADO = 100;
        final int SEP = (640 - N_COLUMNAS * LADO) / (N_COLUMNAS + 1);
        final int MARCO = 20;
        int columna = 0;
        int fila = -1;
        
        for (Trampa trampa: trampas){
            if(puntosTrampa >= trampa.getCosto()){
                fila = columna == 0 ? ++fila : fila;

                BotonImagen marcoTrampa = new BotonImagen(Constantes.BTN_MARCO);
                this.add(marcoTrampa);
                marcoTrampa.setSize(LADO + MARCO, LADO + MARCO);
                marcoTrampa.setLocation((SEP + LADO) * columna + SEP - MARCO / 2 + 80, (SEP + LADO) * fila + SEP + 60 - MARCO / 2);

                PanelImagen iconoTrampa = new PanelImagen(Constantes.RUTA_BOTONES
                        + trampa.getNomArchivoImagen() + Constantes.EXT1);
                this.add(iconoTrampa);
                iconoTrampa.setSize(LADO, LADO);
                iconoTrampa.setLocation((SEP + LADO) * columna + SEP + 80, (SEP + LADO) * fila + SEP + 60);

                panelesTrampas.add(marcoTrampa);

                columna = columna == (N_COLUMNAS - 1)? 0: ++columna;
            }
        }
        
        this.titulo.setFont(Constantes.FUENTE_24PX);
        
        this.setImagenFondo(Constantes.FONDO_SELECCION_3);
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

    @Override
    public void mouseClicked(MouseEvent me) {
        this.dispose();
    }

    @Override
    public void mousePressed(MouseEvent me) {
        // Nada
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        // Nada
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        // Nada
    }

    @Override
    public void mouseExited(MouseEvent me) {
        // Nada
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel titulo;
    // End of variables declaration//GEN-END:variables
}