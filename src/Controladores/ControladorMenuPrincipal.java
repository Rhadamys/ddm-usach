/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Otros.BotonImagen;
import Vistas.VistaMenuPrincipal;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author mam28
 */
public final class ControladorMenuPrincipal {
    private final ControladorPrincipal contPrin;
    private final VistaMenuPrincipal visMenuPrin;
    
    public ControladorMenuPrincipal(ControladorPrincipal contPrin){
        this.contPrin = contPrin;
        
        this.visMenuPrin = new VistaMenuPrincipal(this.contPrin.getFuentePersonalizada());
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
        // Obtiene el botón "Nueva partida"
        BotonImagen nuevaPartida = this.visMenuPrin.getNuevaPartida();
        nuevaPartida.addMouseListener(new MouseAdapter(){
            // Cuando se haga clic sobre el label "Volver atrás".
            @Override
            public void mouseClicked(MouseEvent e){
                nuevaPartida();
            }
        });
        
        // Obtiene el botón "Salir"
        BotonImagen salir = this.visMenuPrin.getSalir();
        salir.addMouseListener(new MouseAdapter(){
            // Cuando se haga clic sobre el label "Volver atrás".
            @Override
            public void mouseClicked(MouseEvent e){
                salir();
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
    
    public void salir(){
        int opcion = this.preguntarSalir();
        if (opcion == JOptionPane.YES_OPTION){
            this.contPrin.getContVisPrin().getVisPrin().dispose();
        }
    }
    
    /**
     * Muestra un mensaje con las opciones si / no solicitando al usuario que
     * confirme si desea salir de la aplicación.
     * @return Opcion elegida por el usuario en el JOptionPane. 
     */
    public int preguntarSalir(){
        int opcion = JOptionPane.showConfirmDialog(
                null, 
                "¿Estás seguro que deseas salir?",
                "Salir",
                JOptionPane.YES_NO_OPTION);
        
        return opcion;
    }
    
}
