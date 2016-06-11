/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.Jugador;
import ModelosDAO.JugadorDAO;
import Otros.BotonImagen;
import Otros.PanelImagen;
import Vistas.SubVistaCambiarJugador;
import Vistas.SubVistaCuadroDialogo;
import Vistas.SubVistaResumenJugador;
import Vistas.SubVistaSeleccionEquipos;
import Vistas.VistaNuevaPartida;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
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
    private int cantidadJugadores;
    
    ControladorNuevaPartida(ControladorPrincipal contPrin) {
        this.contPrin = contPrin;
                       
        this.visNuePar = new VistaNuevaPartida(this.contPrin.getFuente());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visNuePar);
        this.agregarListenersVistaNuevaPartida();
        
        this.jugadores = new ArrayList();
        this.cantidadJugadores = Jugador.getJugadores(this.jugadores).size();
        
        this.agregarJugador((Jugador) this.contPrin.getUsuarioActivo());
        this.agregarJugador(this.obtenerJugadorAleatorio());
    }
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con la vista de nueva partida">  
    
    /**
     * Agrega los listeners a los componentes de la vista de nueva partida y
     * define las acciones a realizar.
     */
    public void agregarListenersVistaNuevaPartida(){
        this.visNuePar.getAgregar().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(restanJugadores()){
                    agregarJugador(obtenerJugadorAleatorio());
                }else{
                    mostrarCuadroDialogo("No hay más jugadores registrados para agregar a la partida.");
                }
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
                registrarJugador();
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
     * Muestra la vista de nueva partida instanciada en este controlador.
     */
    public void mostrarVistaNuevaPartida(){
        this.visNuePar.setVisible(true);
    }
    
    /**
     * Agrega un jugador a la partida.
     * @param jug
     */
    public void agregarJugador(Jugador jug){
        if(this.jugadores.size() < 4){
            this.jugadores.add(jug);
            SubVistaResumenJugador visInfoJug = new SubVistaResumenJugador(jug, this.contPrin.getFuente());
            this.visNuePar.agregarVisResJug(visInfoJug);
            this.visNuePar.add(visInfoJug, 0);
            visInfoJug.setName(String.valueOf(this.jugadores.size() - 1));
            this.actualizarPosicionVistasResJug();

            this.agregarListenersVistaInfoJugador(visInfoJug);

            try{
                this.visNuePar.getVisSelEq().agregarJugador(jug);
                this.agregarListenersVistaSeleccionEquipos(
                        this.visNuePar.getVisSelEq().getIconosJugadores().size() - 1);
            }catch(Exception e){
                // Nada
            }

            this.cantidadJugadores--;
            this.actualizarPosicionVistasResJug();
        }else{
            this.mostrarCuadroDialogo("Máximo 4 jugadores.");
        }
    }
    
    /**
     * Se elimina un jugador de la partida.
     * @param i Índice del jugador a eliminar.
     */
    public void eliminarJugador(int i){
        if(i == 0){
            this.mostrarCuadroDialogo("No se puede eliminar a <b><i style=\"color:orange;\">" + 
                    this.jugadores.get(0).getNombreJugador() + "</i></b> de la partida porque es el" + 
                    " usuario activo en la aplicación.");
        }else if(this.jugadores.size() > 2){
            this.jugadores.remove(i);
            
            this.visNuePar.eliminarVisResJug(i);
            actualizarPosicionVistasResJug();
            
            try{
                this.visNuePar.getVisSelEq().eliminarJugador(i);
                if(this.jugadores.size() == 2){
                    this.enSolitario();
                }
            }catch(Exception e){
                // Nada
            }
            
            this.cantidadJugadores++;
            this.actualizarPosicionVistasResJug();
        }else{
            this.mostrarCuadroDialogo("Mínimo 2 jugadores.");
        }
    }
    
    /**
     * Obtiene un jugador aleatorio de entre los que no están actualmente en la partida.
     * @return Instancia de jugador.
     */
    public Jugador obtenerJugadorAleatorio(){
        try {
            ArrayList<Jugador> jugDisponibles = JugadorDAO.getJugadores(jugadores);
            Random rnd = new Random();
            return jugDisponibles.get(rnd.nextInt(jugDisponibles.size()));
        } catch (SQLException ex) {
            return null;
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
     * Instancia un nuevo controlador y vista registro para registrar un nuevo jugador.
     */
    public void registrarJugador(){
        contPrin.crearControladorRegistro(visNuePar);
        contPrin.getContReg().mostrarVistaRegistro();
    }
    
    /**
     * Comienza la partida con los jugadores agregados en la vista de nueva partida.
     */
    public void comenzarPartida(){
        this.contPrin.crearControladorBatalla(this.jugadores);
        this.contPrin.getContBat().mostrarVistaBatalla();
        this.contPrin.getContBat().iniciarJuego();
                
        visNuePar.dispose();
    }
    
    /**
     * Muestra un cuadro de diálogo con un mensaje.
     * @param mensaje Mensaje que se mostrará en el cuadro de diálogo.
     */
    public void mostrarCuadroDialogo(String mensaje){
        SubVistaCuadroDialogo visMen = new SubVistaCuadroDialogo(
                "<html><center>" +mensaje + "</center></html>", "Aceptar", this.contPrin.getFuente());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visMen);
        visMen.setVisible(true);
    }
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con la vista de resumen de jugador">
    
    /**
     * Agrega los listeners a los componentes de la vista de información de jugador
     * en la vista de nueva partida y define las acciones a realizar.
     * @param visInfoJug Vista a la que se agregarán los listeners.
     */
    public void agregarListenersVistaInfoJugador(SubVistaResumenJugador visInfoJug){
        visInfoJug.getCambiarJugador().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(restanJugadores()){
                    crearVistaCambiarJugador((SubVistaResumenJugador) e.getComponent().getParent());
                }else{
                    mostrarCuadroDialogo("No hay más jugadores registrados para realizar un cambio.");
                }
            }
        });
        
        visInfoJug.getEliminar().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                eliminarJugador(Integer.parseInt(((SubVistaResumenJugador)
                        e.getComponent().getParent()).getName()));
            }
        });
        
        visInfoJug.getModificarPuzle().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                
            }
        });
    }
    
// </editor-fold>
  
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con la vista de selección de equipos">  
    
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
                this.mostrarCuadroDialogo("Se necesitan mínimo 3 jugadores para<br>formar equipos.");
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
    }
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con la vista para cambiar un jugador">
        
    /**
     * Actualiza esta vista para reordenar los elementos en ella de acuerdo a la
     * cantidad de jugadores actual definidos para la partida.
     */
    public void actualizarPosicionVistasResJug(){
        for(int i = 0; i < this.jugadores.size(); i++){
            this.visNuePar.getVisResJug(i).setLocation(
                    this.visNuePar.getPosVisResJug()[i][0], 
                    this.visNuePar.getPosVisResJug()[i][1]);
        }
    }
            
    /**
     * Instancia una vista para cambiar algún jugador en la vista nueva partida.
     * @param quienCambia Índice del jugador que cambia en la partida.
     */
    public void crearVistaCambiarJugador(SubVistaResumenJugador quienCambia){
        this.visNuePar.setVisCamJug(new SubVistaCambiarJugador(this.contPrin.getFuente(), this.jugadores));
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visNuePar.getVisCamJug());
        
        for(int i = 0; i < this.visNuePar.getVisCamJug().getIconosJugadores().size(); i++){
            this.agregarListenersVistaCambiarJugador(i);
        }
        
        this.visNuePar.getVisCamJug().setName(String.valueOf(quienCambia.getName()));
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
        this.visNuePar.getVisResJug(quienCambia).actualizarInfoJug(this.visNuePar.getVisCamJug().getJugadores().get(porQuienCambia));
        
        try{
            this.visNuePar.getVisSelEq().getJugadores().set(quienCambia, this.visNuePar.getVisCamJug().getJugadores().get(porQuienCambia));
            this.visNuePar.getVisSelEq().actualizarIconos();
        }catch(Exception e){
            // Nada
        }
        
        this.visNuePar.getVisCamJug().dispose();
    }
    
// </editor-fold>
    
    public boolean restanJugadores(){
        if(cantidadJugadores > 0){
            return true;
        }else{
            return false;
        }
    }
    
}
