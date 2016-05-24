/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Controladores.ControladorPrincipal;
import Otros.BotonImagen;
import Otros.PanelImagen;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 *
 * @author mam28
 */
public class CompMensaje extends Thread{
    public static int mostrarMensaje(String mensaje, String textoBoton, ControladorPrincipal contPrin){
        VistaMensaje visMen = new VistaMensaje(mensaje, textoBoton);
        contPrin.getContVisPrin().getVisPrin().agregarVista(visMen);
        visMen.setVisible(true);
        
        synchronized(visMen){
            while(visMen.isVisible()){
                try {
                    visMen.wait(0);
                } catch (InterruptedException ex) {
                    // Nada
                }
            }
        }
        
        visMen.dispose();
        return visMen.getRespuesta();
    }
}

class VistaMensaje extends JInternalFrame {
    private BotonImagen boton1;
    private BotonImagen boton2;
    private JLabel labelMensaje;
    private int respuesta = 10;
    
    public VistaMensaje(String mensaje, String textoBoton){
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        this.setBorder(null);
        this.setOpaque(false);
        this.setSize(800, 600);
        this.setBackground(new Color(0,0,0,0));
        
        this.labelMensaje = new JLabel(mensaje);
        this.boton1 = new BotonImagen("/Imagenes/Botones/boton.png");
        
        this.add(labelMensaje);
        this.add(boton1);
        
        this.boton1.setImagenSobre("/Imagenes/Botones/boton_sobre.png");
        this.boton1.setImagenPresionado("/Imagenes/Botones/boton_presionado.png");
        this.boton1.setSize(200, 30);
        this.boton1.setLocation(300, 480);
        this.boton1.setText(textoBoton);
        
        this.labelMensaje.setSize(580, 150);
        this.labelMensaje.setHorizontalAlignment(JLabel.CENTER);
        this.labelMensaje.setVerticalAlignment(JLabel.CENTER);
        this.labelMensaje.setLocation(110, 100);
        
        this.boton1.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                setRespuesta(0);
            }
        });
        
        PanelImagen panelFondo = new PanelImagen("/Imagenes/Fondos/fondo_seleccion_3.png");
        this.add(panelFondo);
        panelFondo.setSize(this.getSize());
    }
    
    public void setRespuesta(int respuesta){
        synchronized(this){
            this.respuesta = respuesta;
            this.setVisible(false);
            notify();
        }
    }
    
    public int getRespuesta(){
        return this.respuesta;
    }
}