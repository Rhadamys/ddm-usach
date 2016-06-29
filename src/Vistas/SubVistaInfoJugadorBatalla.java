/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Otros.Constantes;
import Otros.PanelImagen;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 *
 * @author mam28
 */
public class SubVistaInfoJugadorBatalla extends PanelImagen{
    private final PanelImagen panelTurno;
    private final PanelImagen iconoJugador;
    private final JLabel nombreJugador;
    private final JProgressBar vidaJugador;
    private final PanelImagen ataque;
    private final PanelImagen magia;
    private final PanelImagen movimiento;
    private final PanelImagen trampa;
    private final JLabel puntosAtaque;
    private final JLabel puntosMagia;
    private final JLabel puntosMovimiento;
    private final JLabel puntosTrampa;
    private final ArrayList<JLabel> puntosTrampas;
    
    public SubVistaInfoJugadorBatalla(){
        this.setSize(140, 250);
        
        this.panelTurno = new PanelImagen();
        this.add(panelTurno);
        this.panelTurno.setSize(35, 35);
        this.panelTurno.setLocation(70, 45);
        
        this.nombreJugador = new JLabel();
        this.iconoJugador = new PanelImagen();
        this.vidaJugador = new JProgressBar();
        
        this.ataque = new PanelImagen(Constantes.ATAQUE);
        this.magia = new PanelImagen(Constantes.MAGIA);
        this.movimiento = new PanelImagen(Constantes.MOVIMIENTO);
        this.trampa = new PanelImagen(Constantes.TRAMPA);
        
        this.puntosAtaque = new JLabel("0");
        this.puntosMagia = new JLabel("0");
        this.puntosMovimiento = new JLabel("0");
        this.puntosTrampa = new JLabel("0");
        
        this.add(nombreJugador);
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
        
        this.nombreJugador.setSize(140, 30);
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
        
        this.ataque.setToolTipText("Puntos de ataque");
        this.magia.setToolTipText("Puntos de magia");
        this.movimiento.setToolTipText("Puntos de movimiento");
        this.trampa.setToolTipText("Puntos de trampa");
        
        this.puntosAtaque.setHorizontalAlignment(JLabel.CENTER);
        this.puntosAtaque.setVerticalAlignment(JLabel.CENTER);
        this.puntosAtaque.setForeground(Color.white);
        this.puntosAtaque.setFont(Constantes.FUENTE_14PX);
        
        this.puntosMagia.setHorizontalAlignment(JLabel.CENTER);
        this.puntosMagia.setVerticalAlignment(JLabel.CENTER);
        this.puntosMagia.setForeground(Color.white);
        this.puntosMagia.setFont(Constantes.FUENTE_14PX);
        
        this.puntosMovimiento.setHorizontalAlignment(JLabel.CENTER);
        this.puntosMovimiento.setVerticalAlignment(JLabel.CENTER);
        this.puntosMovimiento.setForeground(Color.white);
        this.puntosMovimiento.setFont(Constantes.FUENTE_14PX);
        
        this.puntosTrampa.setHorizontalAlignment(JLabel.CENTER);
        this.puntosTrampa.setVerticalAlignment(JLabel.CENTER);
        this.puntosTrampa.setForeground(Color.white);
        this.puntosTrampa.setFont(Constantes.FUENTE_14PX);
        
        this.nombreJugador.setHorizontalAlignment(JLabel.CENTER);
        this.nombreJugador.setVerticalAlignment(JLabel.CENTER);
        this.nombreJugador.setForeground(Color.white);
        this.nombreJugador.setFont(Constantes.FUENTE_14PX);
        
        this.vidaJugador.setBorder(null);
        this.vidaJugador.setForeground(Color.green);
        this.vidaJugador.setBackground(Color.yellow);
        
        this.puntosTrampas = new ArrayList();
        
        final int SEP = 65 / 4;
        String[] nombreTrampa = {"Trampa de oso", "Trampa para ladrones", "Renacer de los muertos"};
        for(int i = 1; i <= 3; i++){
            PanelImagen panelTrampa = new PanelImagen("/Imagenes/Otros/trampa_" + i + ".png");
            this.add(panelTrampa);
            panelTrampa.setSize(25, 25);
            panelTrampa.setLocation((SEP + 25) * (i - 1) + SEP, 195);
            panelTrampa.setToolTipText("<html>Cantidad disponible de:<br><b>" + nombreTrampa[i - 1] + "</b></html>");
            
            JLabel ptosTrampa = new JLabel("0");
            this.add(ptosTrampa);
            ptosTrampa.setSize(25, 20);
            ptosTrampa.setLocation((SEP + 25) * (i - 1) + SEP, 220);
            ptosTrampa.setHorizontalAlignment(JLabel.CENTER);
            ptosTrampa.setVerticalAlignment(JLabel.CENTER);
            ptosTrampa.setForeground(Color.white);
            ptosTrampa.setFont(Constantes.FUENTE_14PX);
            
            puntosTrampas.add(ptosTrampa);
        }
    }

    public JProgressBar getVidaJugador() {
        return vidaJugador;
    }
    
    public void setNombreJugador(String nombre){
        this.nombreJugador.setText(nombre);
    }
    
    public void setIconoJugador(String imagen){
        this.iconoJugador.setImagen(imagen);
    }
    
    public void setVidaMaximaJugador(int vidaMaximaJugador){
        this.vidaJugador.setMaximum(vidaMaximaJugador);
        this.vidaJugador.setValue(vidaMaximaJugador);
    }

    public void setVidaJugador(int vidaJugador) {
        this.vidaJugador.setValue(vidaJugador);
        this.setToolTipText(this.vidaJugador.getValue() + " / " + this.vidaJugador.getMaximum());
    }
    
    public void setPuntosAtaque(String puntosAtaque) {
        this.puntosAtaque.setText(puntosAtaque);
    }

    public void setPuntosMagia(String puntosMagia) {
        this.puntosMagia.setText(puntosMagia);
    }

    public void setPuntosMovimiento(String puntosMovimiento) {
        this.puntosMovimiento.setText(puntosMovimiento);
    }

    public void setPuntosTrampa(String puntosTrampa) {
        this.puntosTrampa.setText(puntosTrampa);
    }
    
    public void setPuntosTrampa(int i, int puntosTrampa){
        this.puntosTrampas.get(i).setText(String.valueOf(puntosTrampa));
    }
    
    /*
     * Actualiza la vista de información de jugador con la información actual del jugador.
    */
    public void actualizarVista(int pa, int pmag, int pm, int pt, int vida, int to, int tl, int trm, boolean esMiTurno){
        this.setPuntosAtaque(String.valueOf(pa));
        this.setPuntosMagia(String.valueOf(pmag));
        this.setPuntosMovimiento(String.valueOf(pm));
        this.setPuntosTrampa(String.valueOf(pt));
        this.setVidaJugador(vida);

        this.setPuntosTrampa(0, to);
        this.setPuntosTrampa(1, tl);
        this.setPuntosTrampa(2, trm);
        
        this.esMiTurno(esMiTurno);
    }
    
    public void esMiTurno(boolean esMiTurno){
        if(esMiTurno){
            this.panelTurno.setImagen(Constantes.TURNO_ACTUAL);
            this.panelTurno.setToolTipText("<html>Es el turno de <b>" + nombreJugador.getText() + "</b></html>");
        }else{
            this.panelTurno.setImagen(Constantes.VACIO);
            this.panelTurno.setToolTipText("");
        }
    }
}
