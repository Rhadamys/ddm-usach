/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.Dado;
import Otros.Constantes;
import Otros.Registro;
import Otros.Reproductor;
import Vistas.SubVistaCuadroDialogo;
import Vistas.VistaMenuPrincipal;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 *
 * @author mam28
 */
public final class ControladorMenuPrincipal {
    private final ControladorPrincipal contPrin;
    private final VistaMenuPrincipal visMenuPrin;
    private final SubVistaCuadroDialogo visMen;
    
    public ControladorMenuPrincipal(ControladorPrincipal contPrin){
        this.contPrin = contPrin;
        
        this.visMenuPrin = new VistaMenuPrincipal(this.contPrin.getUsuarioActivo());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visMenuPrin);
        this.agregarListenersVistaMenuPrincipal();
        
        this.visMen = new SubVistaCuadroDialogo("¿Deseas cerrar sesión?", "Si", "No");
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visMen);
        this.agregarListenersVistaMensaje();
        
        Reproductor.reproducir(Constantes.M_MENU_PRINCIPAL);
    }

    public VistaMenuPrincipal getVisMenuPrin() {
        return visMenuPrin;
    }
    
    /**
     * Agrega listeners a los componentes de la vista menú principal (visMenuPrin)
     * y específica las acciones que deben realizar al activarse dichos eventos.
     */
    public void agregarListenersVistaMenuPrincipal(){
        this.visMenuPrin.getNuevaPartida().addMouseListener(new MouseAdapter(){
            // Cuando se haga clic sobre el label "Volver atrás".
            @Override
            public void mouseReleased(MouseEvent e){
                nuevaPartida();
            }
        });
        
        this.visMenuPrin.getNuevoTorneo().addMouseListener(new MouseAdapter(){
            // Cuando se haga clic sobre el label "Volver atrás".
            @Override
            public void mouseReleased(MouseEvent e){
                nuevoTorneo();
            }
        });
        
        this.visMenuPrin.getInfoCriaturas().addMouseListener(new MouseAdapter(){
            // Cuando se haga clic sobre el label "Volver atrÃ¡s".
            @Override
            public void mouseReleased(MouseEvent e){
                infoDelJuego();
            }
        });
        
        this.visMenuPrin.getModificarPuzzle().addMouseListener(new MouseAdapter(){
            // Cuando se haga clic sobre el label "Volver atrás".
            @Override
            public void mouseReleased(MouseEvent e){
                if(sePuedeModificarPuzzle(contPrin.getUsuarioActivo().getDados())){
                    modificarPuzzle();
                }else{
                    mostrarMensaje("El jugador tiene 15 dados. No puede modificar su puzzle.");
                }
            }
        });
        
        this.visMenuPrin.getSalir().addMouseListener(new MouseAdapter(){
            // Cuando se haga clic sobre el label "Volver atrás".
            @Override
            public void mouseReleased(MouseEvent e){
                contPrin.getContVisPrin().mostrarMensajeSalir();
            }
        });
        
        this.visMenuPrin.getCerrarSesion().addMouseListener(new MouseAdapter(){
            // Cuando se haga clic sobre el label "Volver atrás".
            @Override
            public void mouseReleased(MouseEvent e){
                mostrarMensajeLogOut();
            }
        });
    }
    
    public void agregarListenersVistaMensaje(){
        this.visMen.getBoton1().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                logOut();
            }
        });
        
        this.visMen.getBoton2().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                visMen.setVisible(false);
            }
        });
    }
    
    /**
     * Hace visible la vista de menú principal en el JFrame principal.
     */
    public void mostrarVistaMenuPrincipal() {
        this.visMenuPrin.setVisible(true);
    }
    
    public void eliminarVistaMenuPrincipal(){
        this.visMen.dispose();
        this.visMenuPrin.dispose();
    }
    
    public void nuevaPartida(){
        this.contPrin.crearControladorNuevaPartida(this.visMenuPrin);
        this.contPrin.getContNuePar().mostrarVistaNuevaPartida();
        eliminarVistaMenuPrincipal();
        
        Registro.registrarAccion(Registro.NUEVA_PARTIDA, null);
    }
    
    public void nuevoTorneo(){
        this.contPrin.crearControladorTorneo();
        this.contPrin.getContTor().mostrarVistaNuevoTorneo();
        this.eliminarVistaMenuPrincipal();
    }
    
    public void infoDelJuego() {
        this.contPrin.crearControladorInfoCriaturas();
        this.contPrin.getContInfo().mostrarVistaInfoCriaturas();
        eliminarVistaMenuPrincipal();
        
        Registro.registrarAccion(Registro.INFO_CRIATURAS,
                this.contPrin.getUsuarioActivo().getNombreJugador());
    }
    
    public void logOut(){
        Registro.registrarAccion(Registro.LOGOUT, contPrin.getUsuarioActivo().getNombreJugador());
        
        this.contPrin.crearControladorLogin();
        this.contPrin.getContLog().mostrarVistaLogin();
        this.contPrin.setUsuarioActivo(null);
        eliminarVistaMenuPrincipal();
    }
    
    public void mostrarMensajeLogOut(){
        this.visMen.setVisible(true);
    }
    
    public boolean sePuedeModificarPuzzle(ArrayList<Dado> dados){
        return dados.size() > 15;
    }
    
    public void modificarPuzzle(){
        this.contPrin.crearControladorModificarPuzzle(this.contPrin.getUsuarioActivo());
        this.contPrin.getContModPuzz().mostrarVistaModificarPuzle();
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
}
