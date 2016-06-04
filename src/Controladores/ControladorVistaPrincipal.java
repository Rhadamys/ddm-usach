/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Vistas.SubVistaCuadroDialogo;
import Vistas.VistaPrincipal;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author mam28
 */
public final class ControladorVistaPrincipal {
    private final ControladorPrincipal contPrin;
    private final VistaPrincipal visPrin;
    private final SubVistaCuadroDialogo visMenSalir;
    
    public ControladorVistaPrincipal(ControladorPrincipal contPrin){
        this.contPrin = contPrin;
        
        this.visPrin = new VistaPrincipal();
        this.visPrin.setVisible(true);
        this.agregarListenersVistaPrincipal();
        
        this.visMenSalir = new SubVistaCuadroDialogo(
                "¿Deseas salir de la aplicación?",
                "Si", "No", this.contPrin.getFuente(), this.contPrin, 0);
        this.visPrin.agregarVista(visMenSalir);
    }
    
    public void agregarListenersVistaPrincipal(){
        this.visPrin.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                salir();
            }
        });
    }

    public VistaPrincipal getVisPrin() {
        return visPrin;
    }
    
    /**
     * Muestra un mensaje con las opciones si / no solicitando al usuario que
     * confirme si desea salir de la aplicación.
     */
    public void salir(){
        this.visMenSalir.setVisible(true);
    }
}
