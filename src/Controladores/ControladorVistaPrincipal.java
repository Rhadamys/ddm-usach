/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Otros.Reproductor;
import Vistas.SubVistaCuadroDialogo;
import Vistas.VistaPrincipal;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author mam28
 */
public final class ControladorVistaPrincipal {
    private final ControladorPrincipal contPrin;
    private final VistaPrincipal visPrin;
    private final SubVistaCuadroDialogo visMen;
    
    public ControladorVistaPrincipal(ControladorPrincipal contPrin){
        this.contPrin = contPrin;
        
        this.visPrin = new VistaPrincipal();
        this.visPrin.setVisible(true);
        
        this.visMen = new SubVistaCuadroDialogo("¿Deseas salir de la aplicación?", "Si", "No");
        this.visPrin.agregarVista(visMen);
        this.agregarListenersVistaMensaje();
    }

    public VistaPrincipal getVisPrin() {
        return visPrin;
    }
    
    /**
     * Muestra un mensaje con las opciones si / no solicitando al usuario que
     * confirme si desea mostrarMensajeSalir de la aplicación.
     */    
    public void mostrarMensajeSalir(){
        this.visMen.setVisible(true);
    }
    
    public void salir(){
        Reproductor.finalizarReproductor();
        this.contPrin.getContVisPrin().getVisPrin().dispose();
    }
    
    public void agregarListenersVistaMensaje() {
        this.visMen.getBoton1().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                salir();
            }
        });
        
        this.visMen.getBoton2().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                visMen.setVisible(false);
            }
        });
    }
}
