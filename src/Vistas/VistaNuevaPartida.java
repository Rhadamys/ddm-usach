/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Modelos.Jugador;
import Otros.BotonCheckImagen;
import Otros.BotonImagen;
import Otros.PanelImagen;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 *
 * @author mam28
 */
public class VistaNuevaPartida extends javax.swing.JInternalFrame {
    private SubVistaSeleccionEquipos visSelEq;
    private final ArrayList<SubVistaResumenJugador> vistasResJug;
    private final BotonImagen agregar;
    private final BotonCheckImagen enEquipos;
    private final BotonImagen volver;
    private final BotonImagen registrar;
    private final BotonImagen comenzar;
    private final JLabel mensaje;
    private final Font fuente;
    private final int[][] posVisResJug = 
    {{50, 90}, {410, 90}, {50, 275}, {410, 275}};
    
    /**
     * Creates new form VistaNuevaPartida
     * @param fuente Fuente que se utilizará en esta vista.
     */
    public VistaNuevaPartida(Font fuente) {
        initComponents();
        
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        
        this.vistasResJug = new ArrayList();
        this.fuente = fuente;
        
        this.agregar = new BotonImagen("/Imagenes/Botones/boton_redondo.png");
        this.add(agregar);
        
        agregar.setImagenSobre("/Imagenes/Botones/boton_redondo_sobre.png");
        agregar.setImagenPresionado("/Imagenes/Botones/boton_redondo_presionado.png");
        agregar.setSize(100, 100);
        agregar.setLocation(40, 475);
        agregar.setLayout(null);
        
        PanelImagen iconoAgregar = new PanelImagen("/Imagenes/Otros/cruz.png");
        agregar.add(iconoAgregar);
        iconoAgregar.setSize(agregar.getWidth() / 2, agregar.getHeight() / 2);
        iconoAgregar.setLocation((agregar.getWidth() - iconoAgregar.getWidth()) / 2, 
                (agregar.getHeight() - iconoAgregar.getHeight()) / 2);
        
        this.enEquipos = new BotonCheckImagen("/Imagenes/Botones/boton_redondo.png");
        this.add(enEquipos);
        
        enEquipos.setImagenSobre("/Imagenes/Botones/boton_redondo_sobre.png");
        enEquipos.setImagenPresionado("/Imagenes/Botones/boton_redondo_presionado.png");
        enEquipos.setImagenSelNormal("/Imagenes/Botones/boton_redondo_sel.png");
        enEquipos.setImagenSelSobre("/Imagenes/Botones/boton_redondo_sel_sobre.png");
        enEquipos.setImagenSelPresionado("/Imagenes/Botones/boton_redondo_sel_presionado.png");
        enEquipos.setSize(100, 100);
        enEquipos.setLocation(160, 475);
        enEquipos.setLayout(null);
        
        PanelImagen iconoEnEquipos = new PanelImagen("/Imagenes/Otros/equipo.png");
        enEquipos.add(iconoEnEquipos);
        iconoEnEquipos.setSize(enEquipos.getWidth() / 2, enEquipos.getHeight() / 2);
        iconoEnEquipos.setLocation((enEquipos.getWidth() - iconoEnEquipos.getWidth()) / 2, 
                (enEquipos.getHeight() - iconoEnEquipos.getHeight()) / 2);
                
        this.volver = new BotonImagen("/Imagenes/Botones/atras.png");
        this.add(this.volver);
        this.volver.setImagenSobre("/Imagenes/Botones/atras_sobre.png");
        this.volver.setImagenPresionado("/Imagenes/Botones/atras_presionado.png");
        this.volver.setLocation(35, 35);
        this.volver.setSize(40, 40);   
                
        this.registrar = new BotonImagen("/Imagenes/Botones/boton.png");
        this.add(this.registrar);
        this.registrar.setImagenSobre("/Imagenes/Botones/boton_sobre.png");
        this.registrar.setImagenPresionado("/Imagenes/Botones/boton_presionado.png");
        this.registrar.setLocation(85, 35);
        this.registrar.setSize(160, 40);     
        this.registrar.setText("Registrar jugador");
        this.registrar.setForeground(Color.white);
        this.registrar.setFont(fuente);
                
        this.comenzar = new BotonImagen("/Imagenes/Botones/boton.png");
        this.add(this.comenzar);
        this.comenzar.setImagenSobre("/Imagenes/Botones/boton_sobre.png");
        this.comenzar.setImagenPresionado("/Imagenes/Botones/boton_presionado.png");
        this.comenzar.setLocation(255, 35);
        this.comenzar.setSize(160, 40);     
        this.comenzar.setText("Comenzar partida");
        this.comenzar.setForeground(Color.white);
        this.comenzar.setFont(fuente);
        
        this.mensaje = new JLabel("");
        this.add(mensaje);
        this.mensaje.setFont(fuente);
        this.mensaje.setForeground(Color.white);
        this.mensaje.setSize(520, 40);
        this.mensaje.setLocation(245, 35);
        this.mensaje.setHorizontalAlignment(JLabel.CENTER);
        this.mensaje.setVerticalAlignment(JLabel.CENTER);
        
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

    /**
     * Agrega una "vista" de información de jugador a la vista de nueva partida.
     * Esta vista es un resumen del jugador.
     * @param jugador Jugador para el cual se crea la vista.
     */    
    public void agregarVistaInfoJugador(Jugador jugador){
        if(vistasResJug.size() < 4){
            SubVistaResumenJugador visInfoJug = new SubVistaResumenJugador(jugador, this.fuente);
            vistasResJug.add(visInfoJug);
            this.add(visInfoJug, 0);
            this.actualizarVista();
        }else{
            this.setMensaje("Máximo 4 jugadores.");
        }
    }
    
    /**
     * Elimina una vista de información de jugador.
     * @param i Índice de la vista a eliminar.
     */
    public void eliminarVisInfoJug(int i){
        if(vistasResJug.size() > 2){
            vistasResJug.get(i).setVisible(false);
            vistasResJug.remove(i);
            actualizarVista();
        }else{
            setMensaje("Mínimo 2 jugadores.");
        }
    }
    
    /**
     * Actualiza esta vista para reordenar los elementos en ella de acuerdo a la
     * cantidad de jugadores actual definidos para la partida.
     */
    public void actualizarVista(){
        for(SubVistaResumenJugador infoJug: vistasResJug){
            infoJug.setLocation(posVisResJug[vistasResJug.indexOf(infoJug)][0], 
                    posVisResJug[vistasResJug.indexOf(infoJug)][1]);
        }
    }
    
    public BotonImagen getAgregar() {
        return agregar;
    }

    public BotonCheckImagen getEnEquipos() {
        return enEquipos;
    }

    public BotonImagen getVolver() {
        return volver;
    }

    public BotonImagen getRegistrar() {
        return registrar;
    }

    public BotonImagen getComenzar() {
        return comenzar;
    }

    public ArrayList<SubVistaResumenJugador> getVistasInfoJug() {
        return vistasResJug;
    }

    public SubVistaSeleccionEquipos getVisSelEq() {
        return visSelEq;
    }

    public void setVisSelEq(SubVistaSeleccionEquipos visSelEq) {
        this.visSelEq = visSelEq;
    }
    
    public void setMensaje(String mensaje){
        this.mensaje.setText(mensaje);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}