/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Vistas.VistaNuevaPartida;

/**
 *
 * @author mam28
 */
public class ControladorNuevaPartida {
    private final ControladorPrincipal contPrin;
    private VistaNuevaPartida visNuePar;
    
    ControladorNuevaPartida(ControladorPrincipal contPrin) {
        this.contPrin = contPrin;
        
        this.visNuePar = new VistaNuevaPartida(this.contPrin.getFuentePersonalizada());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visNuePar);
    }
    
    public void mostrarVistaNuevaPartida(){
        this.visNuePar.setVisible(true);
    }
    
}
