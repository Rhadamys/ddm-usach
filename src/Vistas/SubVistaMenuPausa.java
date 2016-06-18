/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Otros.BotonImagen;
import Otros.Constantes;
import Otros.VistaPersonalizada;

/**
 *
 * @author mam28
 */
public class SubVistaMenuPausa extends VistaPersonalizada {
    private final BotonImagen continuarPartida;
    private final BotonImagen volverMenuPrincipal;
    private final BotonImagen salirAplicacion;

    /**
     * Creates new form VistaPausaBatalla
     */
    public SubVistaMenuPausa() {
        initComponents();
        
        this.continuarPartida = new BotonImagen(Constantes.BTN_NORMAL);
        this.volverMenuPrincipal = new BotonImagen(Constantes.BTN_NORMAL);
        this.salirAplicacion = new BotonImagen(Constantes.BTN_NORMAL);
        
        this.add(continuarPartida);
        this.add(volverMenuPrincipal);
        this.add(salirAplicacion);
        
        this.continuarPartida.setSize(300, 50);
        this.continuarPartida.setLocation(250, 205);
        this.continuarPartida.setText("Continuar partida");
        
        this.volverMenuPrincipal.setSize(300, 50);
        this.volverMenuPrincipal.setLocation(250, 275);
        this.volverMenuPrincipal.setText("Volver al menú principal");
        this.salirAplicacion.setSize(300, 50);
        this.salirAplicacion.setLocation(250, 345);
        this.salirAplicacion.setText("Salir de la aplicación");
    
        this.setImagenFondo(Constantes.FONDO_MENU_PAUSA);
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

    public BotonImagen getContinuarPartida() {
        return continuarPartida;
    }

    public BotonImagen getVolverMenuPrincipal() {
        return volverMenuPrincipal;
    }

    public BotonImagen getSalirAplicacion() {
        return salirAplicacion;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
