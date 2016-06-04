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
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 *
 * @author mam28
 */
public class SubVistaCuadroDialogo extends JInternalFrame{    
    public static int mostrarMensaje(String mensaje, String textoBoton, ControladorPrincipal contPrin){
        SubVistaCuadroDialogo visMen = new SubVistaCuadroDialogo(mensaje, textoBoton, contPrin.getFuente());
        contPrin.getContVisPrin().getVisPrin().agregarVista(visMen);
        visMen.setVisible(true);
        
        synchronized(visMen){
            while(visMen.getRespuesta() == 0){
                try {
                    visMen.wait();
                } catch (InterruptedException ex) {
                    // Nada
                }
            }
        }
        
        return visMen.getRespuesta();
    }
    
    public static int mostrarMensaje(String mensaje, String textoBoton1, String textoBoton2, ControladorPrincipal contPrin){
        SubVistaCuadroDialogo visMen = new SubVistaCuadroDialogo(mensaje, textoBoton1, textoBoton2, contPrin.getFuente());
        contPrin.getContVisPrin().getVisPrin().agregarVista(visMen);
        visMen.setVisible(true);
        
        return visMen.getRespuesta();
    }
    
    private BotonImagen boton1;
    private BotonImagen boton2;
    private JLabel labelMensaje;
    private int respuesta;
    
    public SubVistaCuadroDialogo(String mensaje, String textoBoton, Font fuentePersonalizada){
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
        this.boton1.setFont(fuentePersonalizada);
        this.boton1.setForeground(Color.white);
        
        this.labelMensaje.setSize(580, 150);
        this.labelMensaje.setHorizontalAlignment(JLabel.CENTER);
        this.labelMensaje.setVerticalAlignment(JLabel.CENTER);
        this.labelMensaje.setLocation(110, 100);
        this.labelMensaje.setFont(fuentePersonalizada);
        this.labelMensaje.setForeground(Color.white);
        
        this.boton1.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                setRespuesta(1);
            }
        });
        
        PanelImagen panelFondo = new PanelImagen("/Imagenes/Fondos/fondo_seleccion_3.png");
        this.add(panelFondo);
        panelFondo.setSize(this.getSize());
    }
    
    public SubVistaCuadroDialogo(String mensaje, String textoBoton1, String textoBoton2, Font fuentePersonalizada){
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        this.setBorder(null);
        this.setOpaque(false);
        this.setSize(800, 600);
        this.setBackground(new Color(0,0,0,0));
        
        this.labelMensaje = new JLabel(mensaje);
        this.boton1 = new BotonImagen("/Imagenes/Botones/boton.png");
        this.boton2 = new BotonImagen("/Imagenes/Botones/boton.png");
        
        this.add(labelMensaje);
        this.add(boton1);
        this.add(boton2);
        
        this.boton1.setImagenSobre("/Imagenes/Botones/boton_sobre.png");
        this.boton1.setImagenPresionado("/Imagenes/Botones/boton_presionado.png");
        this.boton1.setSize(200, 30);
        this.boton1.setLocation(170, 480);
        this.boton1.setText(textoBoton1);
        this.boton1.setFont(fuentePersonalizada);
        this.boton1.setForeground(Color.white);
        
        this.boton2.setImagenSobre("/Imagenes/Botones/boton_sobre.png");
        this.boton2.setImagenPresionado("/Imagenes/Botones/boton_presionado.png");
        this.boton2.setSize(200, 30);
        this.boton2.setLocation(390, 480);
        this.boton2.setText(textoBoton2);
        this.boton2.setFont(fuentePersonalizada);
        this.boton2.setForeground(Color.white);
        
        this.labelMensaje.setSize(580, 150);
        this.labelMensaje.setHorizontalAlignment(JLabel.CENTER);
        this.labelMensaje.setVerticalAlignment(JLabel.CENTER);
        this.labelMensaje.setLocation(110, 100);
        this.labelMensaje.setFont(fuentePersonalizada);
        this.labelMensaje.setForeground(Color.white);
        
        this.boton1.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                setRespuesta(1);
            }
        });
        
        this.boton2.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                setRespuesta(2);
            }
        });
        
        PanelImagen panelFondo = new PanelImagen("/Imagenes/Fondos/fondo_seleccion_3.png");
        this.add(panelFondo);
        panelFondo.setSize(this.getSize());
    }
    
    public synchronized void setRespuesta(int respuesta){
        this.respuesta = respuesta;
        this.notify();
    }
    
    public synchronized int getRespuesta(){
        if(Integer.valueOf(0).equals(this.respuesta)){
            try {
                wait();
            } catch (InterruptedException ex) {
            }
        }
        
        return this.respuesta;
    }
}