/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.Jugador;
import Otros.BotonImagen;
import Otros.PanelImagen;
import Vistas.SubVistaCambiarJugador;
import Vistas.SubVistaResumenJugador;
import Vistas.SubVistaSeleccionEquipos;
import Vistas.VistaNuevaPartida;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author mam28
 */
public class ControladorNuevaPartida {
    private final ControladorPrincipal contPrin;
    private final VistaNuevaPartida visNuePar;
    private final ArrayList<Jugador> jugadores;
    private SubVistaCambiarJugador visCamJug;
    
    ControladorNuevaPartida(ControladorPrincipal contPrin) {
        this.contPrin = contPrin;
               
        this.visNuePar = new VistaNuevaPartida(this.contPrin.getFuente());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visNuePar);
        this.agregarListenersVistaNuevaPartida();
        
        this.jugadores = new ArrayList();
        
        this.agregarJugador(this.obtenerJugadorAleatorio());
        this.agregarJugador(this.obtenerJugadorAleatorio());
    }
    
    public Jugador obtenerJugadorAleatorio(){
        ArrayList<Jugador> jugDisponibles = Jugador.getJugadores(jugadores);
        Random rnd = new Random();
        return jugDisponibles.get(rnd.nextInt(jugDisponibles.size()));
    }
    
    /**
     * Muestra la vista de nueva partida instanciada en este controlador.
     */
    public void mostrarVistaNuevaPartida(){
        this.visNuePar.setVisible(true);
    }
    
    /**
     * Agrega los listeners a los componentes de la vista de nueva partida y
     * define las acciones a realizar.
     */
    public void agregarListenersVistaNuevaPartida(){
        this.visNuePar.getAgregar().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                agregarJugador(obtenerJugadorAleatorio());
            }
        });
        
        this.visNuePar.getEnEquipos().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                enEquipos();
            }
        });
        
        this.visNuePar.getVolver().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                volver();
            }
        });
        
        this.visNuePar.getRegistrar().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                contPrin.crearControladorRegistro(visNuePar);
                contPrin.getContReg().mostrarVistaRegistro();
            }
        });
        
        this.visNuePar.getComenzar().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                comenzarPartida();
            }
        });
    }
    
    /**
     * Agrega los listeners a los componentes de la vista de información de jugador
     * en la vista de nueva partida y define las acciones a realizar.
     * @param visInfoJug Vista a la que se agregarán los listeners.
     */
    public void agregarListenersVistaInfoJugador(SubVistaResumenJugador visInfoJug){
        visInfoJug.getCambiarJugador().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                crearVistaCambiarJugador((PanelImagen) e.getComponent().getParent());
            }
        });
        
        visInfoJug.getEliminar().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                eliminarJugador(visNuePar.getVistasInfoJug().indexOf((SubVistaResumenJugador)
                        e.getComponent().getParent()));
            }
        });
    }
            
    public void crearVistaCambiarJugador(PanelImagen quienCambia){
        this.visCamJug = new SubVistaCambiarJugador(this.contPrin.getFuente(), this.jugadores);
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visCamJug);
        
        for(int i = 0; i < this.visCamJug.getIconosJugadores().size(); i++){
            this.agregarListenersVistaCambiarJugador(i);
        }
        
        this.visCamJug.setName(String.valueOf(this.visNuePar.getVistasInfoJug().indexOf(quienCambia)));
        this.visCamJug.setVisible(true);
    }
    
    public void agregarListenersVistaCambiarJugador(int i){
        this.visCamJug.getIconosJugadores().get(i).addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                cambiarJugador(Integer.parseInt(
                        visCamJug.getName()),
                        visCamJug.getIconosJugadores().indexOf((BotonImagen) e.getComponent()));
            }
            
            @Override
            public void mouseEntered(MouseEvent e){
                visCamJug.mostrarInformacionJugador(
                        visCamJug.getIconosJugadores().indexOf((BotonImagen) e.getComponent()));
            }
            
            @Override
            public void mouseExited(MouseEvent e){
                visCamJug.borrarCampos();
            }
        });
        
        this.visCamJug.getVolver().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                visCamJug.dispose();
            }
        });
    }
    
    public void cambiarJugador(int quienCambia, int porQuienCambia){
        this.jugadores.set(quienCambia, this.visCamJug.getJugadores().get(porQuienCambia));
        this.visNuePar.getVistasInfoJug().get(quienCambia).actualizarInfoJug(this.visCamJug.getJugadores().get(porQuienCambia));
        this.visCamJug.dispose();
    }
    
    /**
     * Agrega un jugador a la partida.
     * @param jug Jugador que se agregará.
     */
    public void agregarJugador(Jugador jug){
        if(this.visNuePar.getVistasInfoJug().size() < 4){
            this.jugadores.add(jug);
            this.visNuePar.agregarVistaInfoJugador(jug);
            this.agregarListenersVistaInfoJugador(this.visNuePar.getVistasInfoJug()
                    .get(this.visNuePar.getVistasInfoJug().size() - 1));

            try{
                this.visNuePar.getVisSelEq().agregarJugador(jug);
                this.agregarListenersVistaSeleccionEquipos(
                        this.visNuePar.getVisSelEq().getIconosJugadores().size() - 1);
            }catch(Exception e){
                // Nada
            }
        }else{
            this.visNuePar.setMensaje("Máximo 4 jugadores.");
        }
    }
    
    /**
     * Se elimina un jugador de la partida.
     * @param i Índice del jugador a eliminar.
     */
    public void eliminarJugador(int i){
        if(this.visNuePar.getVistasInfoJug().size() > 2){
            this.jugadores.remove(i);
            this.visNuePar.eliminarVisInfoJug(i);
            
            try{
                this.visNuePar.getVisSelEq().eliminarJugador(i);
            }catch(Exception e){
                // Nada
            }
        }else{
            this.visNuePar.setMensaje("Mínimo 2 jugadores.");
        }
    }
    
    /**
     * Crea la "vista" de selección de equipos y le agrega los jugadores que se
     * encuentran actualmente en la vista de nueva partida.
     */
    public void crearVistaSeleccionEquipos(){
        this.visNuePar.setVisSelEq(new SubVistaSeleccionEquipos());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visNuePar.getVisSelEq());
        this.visNuePar.add(this.visNuePar.getVisSelEq(), 0);
        this.visNuePar.getVisSelEq().setLocation(280, 475);
        this.visNuePar.getVisSelEq().setVisible(true);
        
        for(int i = 0; i < this.jugadores.size(); i++){
            this.visNuePar.getVisSelEq().agregarJugador(this.jugadores.get(i));
            this.agregarListenersVistaSeleccionEquipos(i);
        }
    }
    
    /**
     * Agrega los listeners a un jugador en la vista de selección de equipos.
     * @param i Índice del componente al que se agregarán los listeners.
     */
    public void agregarListenersVistaSeleccionEquipos(int i){
        this.visNuePar.getVisSelEq().getIconosJugadores().get(i).addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                visNuePar.getVisSelEq().cambiarEquipo(e.getComponent());
            }
        });
    }
    
    /**
     * Cambia el estado actual de la partida, determinando si la partida es o no
     * en equipos.
     */
    public void enEquipos(){
        try{
            this.visNuePar.getVisSelEq().dispose();
            this.visNuePar.setVisSelEq(null);
            
            for(Jugador jug: this.jugadores){
                jug.setEquipo(0);
            }
        }catch(Exception e){
            this.crearVistaSeleccionEquipos();
        }
    }

    /**
     * Vuelve a la vista de menú principal.
     */
    public void volver(){
        this.visNuePar.dispose();
        contPrin.crearControladorMenuPrincipal();
        contPrin.getContMenuPrin().mostrarVistaMenuPrincipal();
    }
    
    /**
     * Comienza la partida con los jugadores agregados en la vista de nueva partida.
     */
    public void comenzarPartida(){
        this.contPrin.crearControladorBatalla(this.jugadores);
        this.contPrin.getContBat().mostrarVistaBatalla();
        
        visNuePar.dispose();
    }
}
