/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Modelos.Dado;
import Modelos.Jugador;
import Otros.BotonImagen;
import Otros.PanelImagen;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JLabel;

/**
 *
 * @author mam28
 */
public class SubVistaResumenJugador extends PanelImagen {
    private final JLabel nombreJugador;
    private final JLabel tipoJugador;
    private final PanelImagen iconoJugador;
    private ArrayList<Dado> dadosJugador;
    private final BotonImagen modificarPuzle;
    private final BotonImagen cambiarJugador;
    private final BotonImagen eliminar;
    
    public SubVistaResumenJugador(Jugador jugador, Font fuentePersonalizada){
        initComponents();
        
        this.setLayout(null);
        this.setBorder(null);
        this.setSize(340, 170);
        this.dadosJugador = jugador.getDados();
        
        this.nombreJugador = new JLabel(jugador.getNombreJugador());
        this.tipoJugador = new JLabel(jugador.getTipoJugador());
        this.iconoJugador = new PanelImagen("/Imagenes/Jefes/" +
                jugador.getJefeDeTerreno().getClave() + ".png");
        this.cambiarJugador = new BotonImagen("/Imagenes/Botones/boton.png");
        this.modificarPuzle = new BotonImagen("/Imagenes/Botones/boton.png");
        this.eliminar = new BotonImagen("/Imagenes/Botones/boton_redondo.png");
        
        this.add(nombreJugador);
        this.add(tipoJugador);
        this.add(iconoJugador);
        this.add(cambiarJugador);
        this.add(modificarPuzle);
        this.add(eliminar);
        
        this.iconoJugador.setSize(120, 120);
        this.iconoJugador.setLocation(20, 25);
        
        this.nombreJugador.setSize(200, 20);
        this.nombreJugador.setLocation(150, 25);
        this.nombreJugador.setForeground(Color.white);
        this.nombreJugador.setFont(new Font(fuentePersonalizada.getName(), 
                Font.TRUETYPE_FONT, 20));
        
        this.tipoJugador.setSize(200, 10);
        this.tipoJugador.setLocation(150,50);
        this.tipoJugador.setForeground(Color.white);
        this.tipoJugador.setFont(fuentePersonalizada);
        
        this.cambiarJugador.setText("Cambiar jugador");
        this.cambiarJugador.setSize(160, 30);
        this.cambiarJugador.setLocation(160, 75);
        this.cambiarJugador.setForeground(Color.white);
        this.cambiarJugador.setImagenSobre("/Imagenes/Botones/boton_sobre.png");
        this.cambiarJugador.setImagenPresionado("/Imagenes/Botones/boton_presionado.png");
        this.cambiarJugador.setFont(fuentePersonalizada);
        
        this.modificarPuzle.setText("Modificar puzle");
        this.modificarPuzle.setSize(160, 30);
        this.modificarPuzle.setLocation(160, 115);
        this.modificarPuzle.setForeground(Color.white);
        this.modificarPuzle.setImagenSobre("/Imagenes/Botones/boton_sobre.png");
        this.modificarPuzle.setImagenPresionado("/Imagenes/Botones/boton_presionado.png");
        this.modificarPuzle.setFont(fuentePersonalizada);
        
        this.eliminar.setSize(30, 30);
        this.eliminar.setLocation(290, 20);
        this.eliminar.setImagenSobre("/Imagenes/Botones/boton_redondo_sobre.png");
        this.eliminar.setImagenPresionado("/Imagenes/Botones/boton_redondo_presionado.png");
        this.eliminar.setLayout(null);
        
        PanelImagen iconoEliminar = new PanelImagen("/Imagenes/Otros/eliminar.png");
        eliminar.add(iconoEliminar);
        iconoEliminar.setSize(eliminar.getWidth() / 2, eliminar.getHeight() / 2);
        iconoEliminar.setLocation((eliminar.getWidth() - iconoEliminar.getWidth()) / 2, 
                (eliminar.getHeight() - iconoEliminar.getHeight()) / 2);
        
        this.setImagen("/Imagenes/Fondos/fondo_azul_transparente.png");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    public void actualizarInfoJug(Jugador jugador){
        this.dadosJugador = jugador.getDados();
        this.iconoJugador.setImagen("/Imagenes/Jefes/" + jugador.getJefeDeTerreno().getClave() + ".png");
        this.nombreJugador.setText(jugador.getNombreJugador());
        this.tipoJugador.setText(jugador.getTipoJugador());
    }

    public JLabel getNombreJugador() {
        return nombreJugador;
    }

    public JLabel getTipoJugador() {
        return tipoJugador;
    }

    public PanelImagen getIconoJugador() {
        return iconoJugador;
    }

    public ArrayList<Dado> getDadosJugador() {
        return dadosJugador;
    }

    public BotonImagen getModificarPuzle() {
        return modificarPuzle;
    }

    public BotonImagen getCambiarJugador() {
        return cambiarJugador;
    }

    public BotonImagen getEliminar() {
        return eliminar;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}