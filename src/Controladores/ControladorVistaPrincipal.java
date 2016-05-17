/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Vistas.VistaPrincipal;
import javax.swing.JInternalFrame;

/**
 *
 * @author mam28
 */
public class ControladorVistaPrincipal {
    private VistaPrincipal visPrin;
    
    public ControladorVistaPrincipal(){
        this.visPrin = new VistaPrincipal();
        visPrin.setVisible(true);
    }
    
    public void agregarVista(JInternalFrame vista){
        this.visPrin.agregarVista(vista);
    }

    public VistaPrincipal getVisPrin() {
        return visPrin;
    }
}
