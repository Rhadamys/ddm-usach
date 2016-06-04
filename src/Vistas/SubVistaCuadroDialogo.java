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
    private final ControladorPrincipal contPrin;
    private final JLabel labelMensaje;
    private final BotonImagen boton1;
    private BotonImagen boton2;
    
    public SubVistaCuadroDialogo(
            String mensaje,
            String textoBoton,
            Font fuente,
            ControladorPrincipal contPrin,
            int accion){
        
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        
        this.setBorder(null);
        this.setOpaque(false);
        this.setSize(800, 600);
        this.setBackground(new Color(0,0,0,0));
        this.contPrin = contPrin;
        
        this.labelMensaje = new JLabel(mensaje);
        this.boton1 = new BotonImagen("/Imagenes/Botones/boton.png");
        
        this.add(labelMensaje);
        this.add(boton1);
        
        this.boton1.setImagenSobre("/Imagenes/Botones/boton_sobre.png");
        this.boton1.setImagenPresionado("/Imagenes/Botones/boton_presionado.png");
        this.boton1.setSize(200, 40);
        this.boton1.setLocation(300, 320);
        this.boton1.setText(textoBoton);
        this.boton1.setFont(fuente);
        this.boton1.setForeground(Color.white);
        
        this.labelMensaje.setSize(580, 100);
        this.labelMensaje.setHorizontalAlignment(JLabel.CENTER);
        this.labelMensaje.setVerticalAlignment(JLabel.CENTER);
        this.labelMensaje.setLocation(110, 220);
        this.labelMensaje.setFont(new Font(fuente.getName(), Font.TRUETYPE_FONT, 24));
        this.labelMensaje.setForeground(Color.white);
        
        this.boton1.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                realizarAccion(accion, 1);
            }
        });
        
        PanelImagen panelFondo = new PanelImagen("/Imagenes/Fondos/fondo_mensaje.png");
        this.add(panelFondo);
        panelFondo.setSize(this.getSize());
    }
    
    public SubVistaCuadroDialogo(
            String mensaje,
            String textoBoton1,
            String textoBoton2,
            Font fuente,
            ControladorPrincipal contPrin, 
            int accion){
        
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        
        this.setBorder(null);
        this.setOpaque(false);
        this.setSize(800, 600);
        this.setBackground(new Color(0,0,0,0));
        this.contPrin = contPrin;
        
        this.labelMensaje = new JLabel(mensaje);
        this.boton1 = new BotonImagen("/Imagenes/Botones/boton.png");
        this.boton2 = new BotonImagen("/Imagenes/Botones/boton.png");
        
        this.add(labelMensaje);
        this.add(boton1);
        this.add(boton2);
        
        this.boton1.setImagenSobre("/Imagenes/Botones/boton_sobre.png");
        this.boton1.setImagenPresionado("/Imagenes/Botones/boton_presionado.png");
        this.boton1.setSize(200, 40);
        this.boton1.setLocation(170, 320);
        this.boton1.setText(textoBoton1);
        this.boton1.setFont(fuente);
        this.boton1.setForeground(Color.white);
        
        this.boton2.setImagenSobre("/Imagenes/Botones/boton_sobre.png");
        this.boton2.setImagenPresionado("/Imagenes/Botones/boton_presionado.png");
        this.boton2.setSize(200, 40);
        this.boton2.setLocation(410, 320);
        this.boton2.setText(textoBoton2);
        this.boton2.setFont(fuente);
        this.boton2.setForeground(Color.white);
        
        this.labelMensaje.setSize(580, 100);
        this.labelMensaje.setHorizontalAlignment(JLabel.CENTER);
        this.labelMensaje.setVerticalAlignment(JLabel.CENTER);
        this.labelMensaje.setLocation(110, 220);
        this.labelMensaje.setFont(new Font(fuente.getName(), Font.TRUETYPE_FONT, 24));
        this.labelMensaje.setForeground(Color.white);
        
        this.boton1.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                realizarAccion(accion, 1);
            }
        });
        
        this.boton2.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                realizarAccion(accion, 2);
            }
        });
        
        PanelImagen panelFondo = new PanelImagen("/Imagenes/Fondos/fondo_mensaje.png");
        this.add(panelFondo);
        panelFondo.setSize(this.getSize());
    }
    
    public void realizarAccion(int accion, int respuesta){
        switch(accion){
            case 0: if(respuesta == 1){
                        this.contPrin.getContVisPrin().getVisPrin().dispose();
                    }else{
                        this.setVisible(false);
                    }
                    break;
            case 1: if(respuesta == 1){
                        this.contPrin.getContMenuPrin().getVisMenuPrin().dispose();
                        this.contPrin.crearControladorLogin();
                        this.contPrin.getContLog().mostrarVistaLogin();
                    }else{
                        this.setVisible(false);
                    }
                    break;
            case 2: if(respuesta == 1){
                        this.contPrin.getContBat().getVisBat().dispose();
                        this.contPrin.getContBat().getVisPausBat().dispose();
                        this.contPrin.crearControladorMenuPrincipal();
                        this.contPrin.getContMenuPrin().mostrarVistaMenuPrincipal();
                    }else{
                        this.setVisible(false);
                    }
                    break;
            default: this.dispose();
        }
    }
}