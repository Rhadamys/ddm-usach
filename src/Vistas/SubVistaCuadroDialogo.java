/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Controladores.ControladorPrincipal;
import Otros.BotonImagen;
import Otros.Constantes;
import Otros.VistaPersonalizada;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;

/**
 *
 * @author mam28
 */
public class SubVistaCuadroDialogo extends VistaPersonalizada{ 
    private final JLabel labelMensaje;
    private final BotonImagen boton1;
    private BotonImagen boton2;
    private ControladorPrincipal contPrin;
    
    public SubVistaCuadroDialogo(String mensaje, String textoBoton){
        
        this.labelMensaje = new JLabel("<html><center>" + mensaje + "</center></html>");
        this.boton1 = new BotonImagen(Constantes.BTN_NORMAL);
        
        this.add(labelMensaje);
        this.add(boton1);
        
        this.boton1.setSize(200, 40);
        this.boton1.setLocation(300, 360);
        this.boton1.setText(textoBoton);
        
        SubVistaCuadroDialogo visMen = this;
        this.boton1.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                visMen.dispose();
            }
        });
        
        this.labelMensaje.setSize(580, 100);
        this.labelMensaje.setHorizontalAlignment(JLabel.CENTER);
        this.labelMensaje.setVerticalAlignment(JLabel.CENTER);
        this.labelMensaje.setLocation(110, 220);
        this.labelMensaje.setFont(Constantes.FUENTE_24PX);
        this.labelMensaje.setForeground(Color.white);
        
        this.setImagenFondo(Constantes.FONDO_CUADRO_DIALOGO);
    }
    
    public SubVistaCuadroDialogo(String mensaje, String textoBoton1, String textoBoton2){
        
        this.labelMensaje = new JLabel("<html><center>" + mensaje + "</center></html>");
        this.boton1 = new BotonImagen(Constantes.BTN_NORMAL);
        this.boton2 = new BotonImagen(Constantes.BTN_NORMAL);
        
        this.add(labelMensaje);
        this.add(boton1);
        this.add(boton2);
        
        this.boton1.setSize(200, 40);
        this.boton1.setLocation(170, 360);
        this.boton1.setText(textoBoton1);
        ;
        this.boton2.setSize(200, 40);
        this.boton2.setLocation(410, 360);
        this.boton2.setText(textoBoton2);
        
        this.labelMensaje.setSize(580, 100);
        this.labelMensaje.setHorizontalAlignment(JLabel.CENTER);
        this.labelMensaje.setVerticalAlignment(JLabel.CENTER);
        this.labelMensaje.setLocation(110, 220);
        this.labelMensaje.setFont(Constantes.FUENTE_24PX);
        this.labelMensaje.setForeground(Color.white);
        
        this.setImagenFondo(Constantes.FONDO_CUADRO_DIALOGO);
    }
    
    public BotonImagen getBoton1() {
        return boton1;
    }

    public BotonImagen getBoton2() {
        return boton2;
    }
    
    
}