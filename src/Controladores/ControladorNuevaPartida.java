/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Vistas.VistaNuevaPartida;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author mam28
 */
public class ControladorNuevaPartida {
    private final ControladorPrincipal contPrin;
    private VistaNuevaPartida visNuePar;
    
    ControladorNuevaPartida(ControladorPrincipal contPrin) {
        this.contPrin = contPrin;
               
        this.visNuePar = new VistaNuevaPartida(this.contPrin.getFuente());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visNuePar);
        this.agregarListenersVistaNuevaPartida();
    }
    
    public void mostrarVistaNuevaPartida(){
        this.visNuePar.setVisible(true);
    }
    
    public void agregarListenersVistaNuevaPartida(){
        this.visNuePar.getAgregar().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                contPrin.crearControladorBatalla();
                contPrin.getContBat().mostrarVistaBatalla();
                visNuePar.dispose();
//                visNuePar.agregarVistaInfoJugador(contPrin.getUsuarioActivo());
            }
        });
    }
    
}
