/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Modelos.Jugador;
import Otros.PanelImagen;
import Otros.VistaPersonalizada;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 *
 * @author mam28
 */
public class SubVistaSeleccionEquipos extends VistaPersonalizada{
    private final PanelImagen marcoIzq;
    private final PanelImagen marcoDer;
    private final PanelImagen equipo1;
    private final PanelImagen equipo2;
    private final ArrayList<Jugador> jugadores;
    private final ArrayList<PanelImagen> iconosJugadores;
    
    public SubVistaSeleccionEquipos(){
        this.setSize(460, 100);
        
        this.marcoIzq = new PanelImagen("/Imagenes/Fondos/marco_e1.png");
        this.marcoDer = new PanelImagen("/Imagenes/Fondos/marco_e2.png");
        this.equipo1 = new PanelImagen("/Imagenes/Fondos/fondo_e1.png");
        this.equipo2 = new PanelImagen("/Imagenes/Fondos/fondo_e2.png");
        
        this.add(marcoIzq);
        this.add(marcoDer);
        this.add(equipo1);
        this.add(equipo2);
        
        this.marcoIzq.setSize(50, 100);
        this.marcoIzq.setLocation(0, 0);
        
        this.marcoDer.setSize(50, 100);
        this.marcoDer.setLocation(410, 0);
        
        this.equipo1.setLocation(50, 0);
        
        this.jugadores = new ArrayList();
        this.iconosJugadores = new ArrayList();
    }
    
    public void agregarJugador(Jugador jug){        
        int i = this.iconosJugadores.size();
        
        this.jugadores.add(jug);
        
        this.iconosJugadores.add(new PanelImagen("/Imagenes/Jefes/" +
                jug.getJefeDeTerreno().getNomArchivoImagen() + ".png"));
        this.iconosJugadores.get(i).setToolTipText(
            jug.getNombreJugador());
        this.iconosJugadores.get(i).setSize(60, 60);
        
        if(this.equipo2.getComponentCount() < 
                this.equipo1.getComponentCount()){
            this.equipo2.add(this.iconosJugadores.get(i));
            this.jugadores.get(i).setEquipo(2);
        }else{
            this.equipo1.add(this.iconosJugadores.get(i));
            this.jugadores.get(i).setEquipo(1);
        }
        
        this.actualizarEquipos();
    }
    
    public void eliminarJugador(int i){
        PanelImagen contenedor = (PanelImagen) this.iconosJugadores.get(i).getParent();
        
        contenedor.remove(this.iconosJugadores.get(i));
        this.iconosJugadores.remove(i);
        
        if(contenedor.equals(this.equipo1) && contenedor.getComponentCount() == 0){
            this.cambiarEquipo(this.equipo2.getComponent(0));
        }else if(contenedor.equals(this.equipo2) && contenedor.getComponentCount() == 0){
            this.cambiarEquipo(this.equipo1.getComponent(0));
        }
        
        this.jugadores.remove(i);
        
        this.actualizarEquipos();
    }
    
    public void cambiarEquipo(Component jugador){
        if(jugador.getParent().equals(this.equipo1)){
            if(this.equipo1.getComponentCount() > 1){
                this.equipo2.add(jugador);
                this.equipo1.remove(jugador);
                this.jugadores.get(iconosJugadores.indexOf(jugador)).setEquipo(2);
            }
        }else{
            if(this.equipo2.getComponentCount() > 1){
                this.equipo1.add(jugador);
                this.equipo2.remove(jugador);
                this.jugadores.get(iconosJugadores.indexOf(jugador)).setEquipo(1);
            }
        }
        
        this.actualizarEquipos();
    }
    
    public void actualizarIconos(){
        for(int i = 0; i < this.jugadores.size(); i++){
            this.iconosJugadores.get(i).setImagen("/Imagenes/Jefes/" + 
                    this.jugadores.get(i).getJefeDeTerreno().getNomArchivoImagen() + ".png");
            this.iconosJugadores.get(i).setToolTipText(this.jugadores.get(i).getNombreJugador());
        }
    }
    
    public void actualizarEquipos(){
        int cantidadE1 = this.equipo1.getComponentCount();
        int cantidadE2 = this.equipo2.getComponentCount();
        
        if(cantidadE1 == 1 && cantidadE2 == 1){
            this.equipo1.setSize(180, 100);
            this.equipo2.setSize(180, 100);
        }else if(cantidadE1 == 1 && cantidadE2 < 3){
            this.equipo1.setSize(100, 100);
            this.equipo2.setSize(260, 100);
        }else if(cantidadE2 == 1 && cantidadE1 < 3){
            this.equipo1.setSize(260, 100);
            this.equipo2.setSize(100, 100);
        }else{
            this.equipo1.setSize(cantidadE1 * 80 + 20, 100);
            this.equipo2.setSize(cantidadE2 * 80 + 20, 100);
        }
        
        this.equipo2.setLocation(50 + this.equipo1.getWidth(), 0);
        
        for(int i = 0; i < cantidadE1; i++){
            this.equipo1.getComponent(i).setLocation(80 * i + 20, 20);
        }
        
        for(int i = 0; i < cantidadE2; i++){
            this.equipo2.getComponent(i).setLocation(80 * i + 20, 20);
        }
        
        this.equipo1.repaint();
        this.equipo2.repaint();
    }

    public ArrayList<PanelImagen> getIconosJugadores() {
        return iconosJugadores;
    }

    public PanelImagen getEquipo1() {
        return equipo1;
    }

    public PanelImagen getEquipo2() {
        return equipo2;
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }
    
}
