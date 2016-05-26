/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Vistas.VistaMenuPrincipal;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author mam28
 */
public final class ControladorMenuPrincipal {
    private final ControladorPrincipal contPrin;
    private final VistaMenuPrincipal visMenuPrin;
    
    public ControladorMenuPrincipal(ControladorPrincipal contPrin){
        this.contPrin = contPrin;
        
        this.visMenuPrin = new VistaMenuPrincipal(this.contPrin.getFuente());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visMenuPrin);
        this.setMensajeBienvenida(this.contPrin.getUsuarioActivo().getUsername());
        this.agregarListenersVistaMenuPrincipal();
    }

    public VistaMenuPrincipal getVisMenuPrin() {
        return visMenuPrin;
    }
    
    public void setMensajeBienvenida(String nombreUsuario) {
        this.visMenuPrin.setMensajeBienvenida(nombreUsuario);
    }
    
    /**
     * Agrega listeners a los componentes de la vista menú principal (visMenuPrin)
     * y específica las acciones que deben realizar al activarse dichos eventos.
     */
    public void agregarListenersVistaMenuPrincipal(){
        this.visMenuPrin.getNuevaPartida().addMouseListener(new MouseAdapter(){
            // Cuando se haga clic sobre el label "Volver atrás".
            @Override
            public void mouseClicked(MouseEvent e){
                nuevaPartida();
            }
        });
        
        this.visMenuPrin.getSalir().addMouseListener(new MouseAdapter(){
            // Cuando se haga clic sobre el label "Volver atrás".
            @Override
            public void mouseClicked(MouseEvent e){
                contPrin.salir();
            }
        });
    }
    
    /**
     * Hace visible la vista de menú principal en el JFrame principal.
     */
    public void mostrarVistaMenuPrincipal() {
        this.visMenuPrin.setVisible(true);
    }
    
    public void nuevaPartida(){
        this.contPrin.crearControladorNuevaPartida();
        this.contPrin.getContNuePar().mostrarVistaNuevaPartida();
        this.visMenuPrin.setVisible(false);
    }
    
}
