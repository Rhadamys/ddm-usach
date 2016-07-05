/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Otros.BotonImagen;
import Otros.Constantes;
import Otros.PanelImagen;
import Otros.Reproductor;
import Otros.VistaPersonalizada;

/**
 *
 * @author mam28
 */
public class SubVistaFinDelJuego extends VistaPersonalizada {
    public BotonImagen finalizarPartida;
    
    /**
     * Creates new form SubVistaFinDelJuego
     * @param numGanador Número del jugador o equipo que ganó.
     * @param esEnEquipos Indica si la partida es en equipos para saber si se mostrará
     * el gif de un jugador individual o de un equipo.
     */
    public SubVistaFinDelJuego(int numGanador, boolean esEnEquipos) {
        initComponents();
        
        PanelImagen panelAnimacion = new PanelImagen(esEnEquipos ? 
                Constantes.RUTA_OTROS + "ganador_equipo_" + numGanador + Constantes.EXT2:
                Constantes.RUTA_OTROS + "ganador_j" + numGanador + Constantes.EXT2);
        this.add(panelAnimacion);
        panelAnimacion.setSize(800, esEnEquipos ? 400 : 200);
        panelAnimacion.setLocation(0, esEnEquipos ? 100 : 200);
        
        this.finalizarPartida = new BotonImagen(Constantes.BTN_NORMAL);
        this.add(finalizarPartida);
        this.finalizarPartida.setSize(200, 40);
        this.finalizarPartida.setLocation(300, 540);
        this.finalizarPartida.setText("Finalizar partida");
        
        this.setImagenFondo(Constantes.VACIO_GRIS);
        
        Reproductor.reproducir(Constantes.M_GANADOR);
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 790, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 599, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public BotonImagen getFinalizarPartida() {
        return finalizarPartida;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
