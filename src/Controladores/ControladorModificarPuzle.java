/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Vistas.VistaModificarPuzle;

/**
 *
 * @author mam28
 */
public class ControladorModificarPuzle {
    private final ControladorPrincipal contPrin;
    private final VistaModificarPuzle visModPuz;
    
    public ControladorModificarPuzle(ControladorPrincipal contPrin){
        this.contPrin = contPrin;
        
        this.visModPuz = new VistaModificarPuzle(contPrin.getFuente());
    }
    
    public void mostrarVistaModificarPuzle(){
        this.visModPuz.setVisible(true);
    }
    
    public void eliminarVistaModificarPuzle(){
        this.visModPuz.dispose();
    }
}
