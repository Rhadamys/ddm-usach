/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Vistas.VistaPrincipal;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author mam28
 */
public final class ControladorVistaPrincipal {
    private ControladorPrincipal contPrin;
    private VistaPrincipal visPrin;
    
    public ControladorVistaPrincipal(ControladorPrincipal contPrin){
        this.contPrin = contPrin;
        
        this.visPrin = new VistaPrincipal();
        this.visPrin.setVisible(true);
        this.agregarListenersVistaPrincipal();
    }
    
    public void agregarListenersVistaPrincipal(){
        this.visPrin.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                if(salir() == JOptionPane.YES_OPTION){
                    e.getWindow().dispose();
                }
            }
        });
    }
    
    /**
     * Muestra un mensaje con las opciones si / no solicitando al usuario que
     * confirme si desea salir de la aplicación.
     * @return Opcion elegida por el usuario en el JOptionPane. 
     */
    public int salir(){
        return JOptionPane.showConfirmDialog(
                null,
                "¿Estás seguro que deseas salir?",
                "Salir",
                JOptionPane.YES_NO_OPTION);
    }

    public VistaPrincipal getVisPrin() {
        return visPrin;
    }
}
