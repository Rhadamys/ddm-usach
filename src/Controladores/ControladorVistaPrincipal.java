/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Vistas.VistaPrincipal;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;

/**
 *
 * @author mam28
 */
public final class ControladorVistaPrincipal {
    private VistaPrincipal visPrin;
    
    public ControladorVistaPrincipal(){
        this.visPrin = new VistaPrincipal();
        this.crearVistaPrincipal();
        visPrin.setVisible(true);
    }
    
    public void crearVistaPrincipal(){
        this.visPrin.setIconImage(new ImageIcon(getClass().getResource("/Imagenes/icono.png")).getImage());
    }
    
    public void agregarVista(JInternalFrame vista){
        this.visPrin.agregarVista(vista);
    }

    public VistaPrincipal getVisPrin() {
        return visPrin;
    }
}
