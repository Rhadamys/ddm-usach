/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.Dado;
import Modelos.Jugador;
import Modelos.PersonajeNoJugable;
import Modelos.PuzzleDeDados;
import Modelos.Torneo;
import Modelos.Usuario;
import Otros.Constantes;
import Otros.Reproductor;
import Vistas.SubVistaCuadroDialogo;
import Vistas.VistaNuevoTorneo;
import Vistas.VistaResumenTorneo;
import Vistas.VistaTorneoFinalizado;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author mam28
 */
public final class ControladorTorneo {
    private final ControladorPrincipal contPrin;
    private final VistaNuevoTorneo visNueTor;
    private VistaResumenTorneo visResTor;
    private VistaTorneoFinalizado visTorFin;
    private String tipoTorneo;
    private Torneo torneo;
    
    public ControladorTorneo(ControladorPrincipal contPrin){
        this.contPrin = contPrin;
        
        this.visNueTor = new VistaNuevoTorneo();
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visNueTor);
        
        this.agregarListenersVistaNuevoTorneo();
        
        Reproductor.reproducir(Constantes.M_NUEVO_TORNEO);
    }
    
    public void agregarListenersVistaNuevoTorneo(){
        this.visNueTor.getVolver().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                volverMenuPrincipal();
            }
        });
        
        this.visNueTor.getTodosContraTodos().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                nuevoTorneo("Todos contra todos");
            }
        });
        
        this.visNueTor.getSobreviviente().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                nuevoTorneo("Sobreviviente");
            }
        });
    }
    
    public void mostrarVistaNuevoTorneo(){
        this.visNueTor.setVisible(true);
    }
    
    public void volverMenuPrincipal(){
        this.contPrin.crearControladorMenuPrincipal();
        this.contPrin.getContMenuPrin().mostrarVistaMenuPrincipal();
        
        if(visNueTor != null){
            this.visNueTor.dispose();
        }
        
        if(visTorFin != null){
            this.visTorFin.dispose();
        }
    }
    
    public void nuevoTorneo(String nombreTorneo){
        this.tipoTorneo = nombreTorneo;
        this.visNueTor.setName(nombreTorneo);
        this.contPrin.crearControladorNuevaPartida(visNueTor);
        this.contPrin.getContNuePar().mostrarVistaNuevaPartida();
        this.visNueTor.dispose();
    }
        
    public void crearTorneo(ArrayList<Jugador> jugadores){
        this.torneo = new Torneo(jugadores, this.tipoTorneo);
    }
    
    public void finalizarPartidaTorneo(Jugador ganador, ArrayList<Jugador> perdedores){
        this.torneo.actEstadisticas(ganador, perdedores);
            
        if(this.tipoTorneo.equals("Todos contra todos")){
            
            this.visResTor = new VistaResumenTorneo(
                    this.torneo.getClasificacion(),
                    this.torneo.getPartJugadas());

            this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visResTor);
            this.visResTor.setVisible(true);
            
            this.agregarListenersVistaResumenTorneo();
        }else{
            if(ganador instanceof Usuario && this.torneo.getPartJugadas() < 3){
                int vidaRest = this.torneo.restaurarVidaJugador(ganador);
                
                this.iniciarSiguienteBatalla();
                
                if(vidaRest > 0){
                    this.contPrin.getContBat().getTablero().getJugador(0).getJefeDeTerreno().restarVida(vidaRest);
                }

                this.mostrarMensaje("Se te ha restaurado un 20% de la vida.<br>¡Suerte!");
            }else if(this.torneo.getPartJugadas() == 3){
                this.finalizarTorneo();
            }else{
                this.volverMenuPrincipal();
                this.mostrarMensaje("Te han ganado. Vuelve a intentarlo cuando quieras.");
            }
        }
    }
    
    public void iniciarSiguienteBatalla(){
        if(visResTor != null){
            this.visResTor.dispose();
        }
        
        this.contPrin.crearControladorBatalla(this.torneo.getJugadores(), false, true);
        this.contPrin.getContBat().iniciarJuego();
    }
    
    public void finalizarTorneo(){
        if(visResTor != null){
            this.visResTor.dispose();
        }
        
        Jugador ganador = this.torneo.getGanador();
        if(ganador instanceof Usuario){
            ArrayList<Dado> dadosGanados = this.torneo.agregarDadosGanador();

            this.visTorFin = new VistaTorneoFinalizado(ganador, dadosGanados);
            this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visTorFin);
            this.visTorFin.setVisible(true);
        }else{
            this.visTorFin = new VistaTorneoFinalizado(ganador);
            this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visTorFin);
            this.visTorFin.setVisible(true);
        }
        
        this.agregarListenersVistaTorneoFinalizado();
    }
    
    public void agregarListenersVistaResumenTorneo(){
        this.visResTor.getSiguientePartida().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                if(e.getComponent().isEnabled()){
                    iniciarSiguienteBatalla();
                }
            }
        });
        
        this.visResTor.getTerminarTorneo().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                if(torneo.getPartJugadas() == 3){
                    finalizarTorneo();
                }else{
                    mostrarMensajeFinalizarTorneo("No se obtendrán recompensas en el punto actual, ¿deseas finalizar el torneo de todos modos?");
                }
            }
        });
    }
    
    public void agregarListenersVistaTorneoFinalizado(){
        this.visTorFin.getAceptar().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                volverMenuPrincipal();
            }
        });
    }
        
    /**
     * Muestra un cuadro de diálogo con un mensaje.
     * @param mensaje Mensaje que se mostrará en el cuadro de diálogo.
     */
    public void mostrarMensaje(String mensaje){
        SubVistaCuadroDialogo visMen = new SubVistaCuadroDialogo(
                "<html><center>" +mensaje + "</center></html>", "Aceptar");
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visMen);
        visMen.setVisible(true);
    }
    
    /**
     * Muestra un cuadro diálogo solicitando al usuario que confirme si desea finalizar
     * el torneo.
     * @param mensaje Mensaje que se mostrará en el cuadro de diálogo.
     */
    public void mostrarMensajeFinalizarTorneo(String mensaje){
        SubVistaCuadroDialogo visMen = new SubVistaCuadroDialogo(mensaje, "Si", "No");
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visMen);
        visMen.setVisible(true);
        
        visMen.getBoton1().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                volverMenuPrincipal();
            }
        });
        
        visMen.getBoton2().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                visMen.dispose();
            }
        });
    }
    
}
