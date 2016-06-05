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
import Vistas.SubVistaCuadroDialogo;
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
                eliminarJugador(visNuePar.getVistasResJug().indexOf((SubVistaResumenJugador)
                        e.getComponent().getParent()));
            }
        });
        
        visInfoJug.getModificarPuzle().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                
            }
        });
    }
        
    /**
     * Actualiza esta vista para reordenar los elementos en ella de acuerdo a la
     * cantidad de jugadores actual definidos para la partida.
     */
    public void actualizarPosicionVistasInfoJug(){
        for(int i = 0; i < this.visNuePar.getVistasResJug().size(); i++){
            this.visNuePar.getVistasResJug().get(i).setLocation(
                    this.visNuePar.getPosVisResJug()[i][0], 
                    this.visNuePar.getPosVisResJug()[i][1]);
        }
    }
            
    /**
     * Instancia una vista para cambiar algún jugador en la vista nueva partida.
     * @param quienCambia Índice del jugador que cambia en la partida.
     */
    public void crearVistaCambiarJugador(PanelImagen quienCambia){
        this.visNuePar.setVisCamJug(new SubVistaCambiarJugador(this.contPrin.getFuente(), this.jugadores));
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visNuePar.getVisCamJug());
        
        for(int i = 0; i < this.visNuePar.getVisCamJug().getIconosJugadores().size(); i++){
            this.agregarListenersVistaCambiarJugador(i);
        }
        
        this.visNuePar.getVisCamJug().setName(String.valueOf(this.visNuePar.getVistasResJug().indexOf(quienCambia)));
        this.visNuePar.getVisCamJug().setVisible(true);
    }
    
    /**
     * Agrega listeners a un icono de jugador en la vista de cambiar jugador.
     * @param i Índice del icono en la vista de cambiar jugador.
     */
    public void agregarListenersVistaCambiarJugador(int i){
        this.visNuePar.getVisCamJug().getIconosJugadores().get(i).addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                cambiarJugador(Integer.parseInt(
                        visNuePar.getVisCamJug().getName()),
                        visNuePar.getVisCamJug().getIconosJugadores().indexOf((BotonImagen) e.getComponent()));
            }
            
            @Override
            public void mouseEntered(MouseEvent e){
                visNuePar.getVisCamJug().mostrarInformacionJugador(
                        visNuePar.getVisCamJug().getIconosJugadores().indexOf((BotonImagen) e.getComponent()));
            }
            
            @Override
            public void mouseExited(MouseEvent e){
                visNuePar.getVisCamJug().borrarCampos();
            }
        });
        
        this.visNuePar.getVisCamJug().getVolver().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                visNuePar.getVisCamJug().dispose();
            }
        });
    }
    
    /**
     * Intercambia a los jugadores.
     * @param quienCambia Índice del jugador que cambia en la vista nueva partida.
     * @param porQuienCambia Índice del jugador seleccionado en la vista de cambiar jugador.
     */
    public void cambiarJugador(int quienCambia, int porQuienCambia){
        this.jugadores.set(quienCambia, this.visNuePar.getVisCamJug().getJugadores().get(porQuienCambia));
        this.visNuePar.getVistasResJug().get(quienCambia).actualizarInfoJug(this.visNuePar.getVisCamJug().getJugadores().get(porQuienCambia));
        
        try{
            this.visNuePar.getVisSelEq().getJugadores().set(quienCambia, this.visNuePar.getVisCamJug().getJugadores().get(porQuienCambia));
            this.visNuePar.getVisSelEq().actualizarIconos();
        }catch(Exception e){
            // Nada
        }
        
        this.visNuePar.getVisCamJug().dispose();
    }
    
    /**
     * Agrega un jugador a la partida.
     * @param jug Jugador que se agregará.
     */
    public void agregarJugador(Jugador jug){
        if(this.jugadores.size() < 4){
            this.jugadores.add(jug);
            
            SubVistaResumenJugador visInfoJug = new SubVistaResumenJugador(jug, this.contPrin.getFuente());
            this.visNuePar.getVistasResJug().add(visInfoJug);
            this.visNuePar.add(this.visNuePar.getVistasResJug().get(this.visNuePar.getVistasResJug().size() - 1), 0);
            this.actualizarPosicionVistasInfoJug();
        
            this.agregarListenersVistaInfoJugador(this.visNuePar.getVistasResJug()
                    .get(this.visNuePar.getVistasResJug().size() - 1));

            try{
                this.visNuePar.getVisSelEq().agregarJugador(jug);
                this.agregarListenersVistaSeleccionEquipos(
                        this.visNuePar.getVisSelEq().getIconosJugadores().size() - 1);
            }catch(Exception e){
                // Nada
            }
            
            this.actualizarPosicionVistasInfoJug();
        }else{
            this.mostrarMensaje("Máximo 4 jugadores.");
        }
    }
    
    /**
     * Se elimina un jugador de la partida.
     * @param i Índice del jugador a eliminar.
     */
    public void eliminarJugador(int i){
        if(this.visNuePar.getVistasResJug().size() > 2){
            this.jugadores.remove(i);
            
            this.visNuePar.getVistasResJug().get(i).setVisible(false);
            this.visNuePar.getVistasResJug().remove(i);
            actualizarPosicionVistasInfoJug();
            
            try{
                this.visNuePar.getVisSelEq().eliminarJugador(i);
                if(this.jugadores.size() == 2){
                    this.enSolitario();
                }
            }catch(Exception e){
                // Nada
            }
            
            this.actualizarPosicionVistasInfoJug();
        }else{
            this.mostrarMensaje("Mínimo 2 jugadores.");
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
            this.enSolitario();
        }catch(Exception e){
            if(this.jugadores.size() >= 3){
                this.crearVistaSeleccionEquipos();
            }else{
                this.mostrarMensaje("<html><center>Se necesitan mínimo 3 jugadores para<br>formar equipos.</center></html>");
                this.visNuePar.getEnEquipos().setSelected(false);
            }
        }
    }
    
    /**
     * Cambia el estado de la partida a partida en solitario.
     */
    public void enSolitario(){
        this.visNuePar.getVisSelEq().dispose();
        this.visNuePar.setVisSelEq(null);

        for(Jugador jug: this.jugadores){
            jug.setEquipo(0);
        }
        
        this.visNuePar.getEnEquipos().setSelected(false);
        this.visNuePar.getEnEquipos().setImagenActual(0);
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
    
    public void mostrarMensaje(String mensaje){
        SubVistaCuadroDialogo visMen = new SubVistaCuadroDialogo(
                mensaje, "Aceptar", this.contPrin.getFuente(), -1);
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visMen);
        visMen.setVisible(true);
    }
}
