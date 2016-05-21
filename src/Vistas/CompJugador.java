/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Otros.PanelImagen;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 *
 * @author mam28
 */
public class CompJugador extends PanelImagen{
    private PanelImagen iconoJugador;
    private JLabel etiqueta;
    private JProgressBar vidaJugador;
    private PanelImagen ataque;
    private PanelImagen magia;
    private PanelImagen movimiento;
    private PanelImagen trampa;
    private JLabel puntosAtaque;
    private JLabel puntosMagia;
    private JLabel puntosMovimiento;
    private JLabel puntosTrampa;
    
    public CompJugador(Font fuentePersonalizada){
        this.setLayout(null);
        this.setSize(140, 190);
        
        this.setImagen("/Imagenes/Fondos/fondo_jugador.png");
        
        this.etiqueta = new JLabel("Nombre jugador");
        this.iconoJugador = new PanelImagen("/Imagenes/Jefes/jefe1.png");
        this.vidaJugador = new JProgressBar();
        
        this.ataque = new PanelImagen("/Imagenes/Botones/ataque.png");
        this.magia = new PanelImagen("/Imagenes/Botones/magia.png");
        this.movimiento = new PanelImagen("/Imagenes/Botones/movimiento.png");
        this.trampa = new PanelImagen("/Imagenes/Botones/trampa.png");
        
        this.puntosAtaque = new JLabel("0");
        this.puntosMagia = new JLabel("0");
        this.puntosMovimiento = new JLabel("0");
        this.puntosTrampa = new JLabel("0");
        
        this.add(etiqueta);
        this.add(iconoJugador);
        this.add(vidaJugador);
        this.add(ataque);
        this.add(magia);
        this.add(movimiento);
        this.add(trampa);
        this.add(puntosAtaque);
        this.add(puntosMagia);
        this.add(puntosMovimiento);
        this.add(puntosTrampa);
        
        this.etiqueta.setSize(140, 30);
        this.iconoJugador.setSize(80, 80);
        this.vidaJugador.setSize(80, 10);
        this.ataque.setSize(25, 25);
        this.magia.setSize(25, 25);
        this.movimiento.setSize(25, 25);
        this.trampa.setSize(25, 25);
        this.puntosAtaque.setSize(25, 20);
        this.puntosMagia.setSize(25, 20);
        this.puntosMovimiento.setSize(25, 20);
        this.puntosTrampa.setSize(25, 20);
        
        this.iconoJugador.setLocation(30, 40);
        this.vidaJugador.setLocation(30,120);
        this.ataque.setLocation(8, 140);
        this.magia.setLocation(41, 140);
        this.movimiento.setLocation(74, 140);
        this.trampa.setLocation(107, 140);
        this.puntosAtaque.setLocation(8, 165);
        this.puntosMagia.setLocation(41, 165);
        this.puntosMovimiento.setLocation(74, 165);
        this.puntosTrampa.setLocation(107, 165);
        
        this.puntosAtaque.setHorizontalAlignment(JLabel.CENTER);
        this.puntosAtaque.setVerticalAlignment(JLabel.CENTER);
        this.puntosAtaque.setForeground(Color.white);
        this.puntosAtaque.setFont(fuentePersonalizada);
        
        this.puntosMagia.setHorizontalAlignment(JLabel.CENTER);
        this.puntosMagia.setVerticalAlignment(JLabel.CENTER);
        this.puntosMagia.setForeground(Color.white);
        this.puntosMagia.setFont(fuentePersonalizada);
        
        this.puntosMovimiento.setHorizontalAlignment(JLabel.CENTER);
        this.puntosMovimiento.setVerticalAlignment(JLabel.CENTER);
        this.puntosMovimiento.setForeground(Color.white);
        this.puntosMovimiento.setFont(fuentePersonalizada);
        
        this.puntosTrampa.setHorizontalAlignment(JLabel.CENTER);
        this.puntosTrampa.setVerticalAlignment(JLabel.CENTER);
        this.puntosTrampa.setForeground(Color.white);
        this.puntosTrampa.setFont(fuentePersonalizada);
        
        this.etiqueta.setHorizontalAlignment(JLabel.CENTER);
        this.etiqueta.setVerticalAlignment(JLabel.CENTER);
        this.etiqueta.setForeground(Color.white);
        this.etiqueta.setFont(fuentePersonalizada);
        
        this.vidaJugador.setBorder(null);
        this.vidaJugador.setValue(50);
        this.vidaJugador.setForeground(Color.green);
        this.vidaJugador.setBackground(Color.yellow);
    }
}
