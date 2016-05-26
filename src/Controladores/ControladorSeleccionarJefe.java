/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.JefeDeTerreno;
import Otros.BotonImagen;
import Vistas.VistaSeleccionarJefe;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 *
 * @author mam28
 */
public class ControladorSeleccionarJefe {
    private final ControladorPrincipal contPrin;
    private final VistaSeleccionarJefe visSelJef;
    private final ArrayList<JefeDeTerreno> jefes;
    
    public ControladorSeleccionarJefe(ControladorPrincipal contPrin) {
        this.contPrin = contPrin;        
        this.jefes = JefeDeTerreno.getJefes();
        
        this.visSelJef = new VistaSeleccionarJefe(
                this.contPrin.getFuente(),
                this.jefes);
        
        this.agregarListenersVistaSeleccionarJefe();
        
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visSelJef);
    }
    
    public void agregarListenersVistaSeleccionarJefe(){
        for(BotonImagen boton: this.visSelJef.getBotones()){
            boton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e){
                    JefeDeTerreno jefe = jefes.get(Integer.parseInt(boton.getName()));
                    visSelJef.setNombre(jefe.getNombre());
                    visSelJef.setHabilidad(jefe.getHabilidad());
                }
                
                @Override
                public void mouseExited(MouseEvent e){
                    visSelJef.setNombre("");
                    visSelJef.setHabilidad("");
                }
                
                @Override
                public void mouseClicked(MouseEvent e){
                    JefeDeTerreno jefe = jefes.get(Integer.parseInt(boton.getName()));
                    contPrin.getContReg().setJefe(jefe);
                    contPrin.getContReg().getVisReg().setIconoJefe("/Imagenes/Jefes/" 
                            + jefe.getClave() + ".png");
                    visSelJef.dispose();
                }
            });
        }
    }

    public VistaSeleccionarJefe getVisSelJef() {
        return visSelJef;
    } 
    
    public void mostrarVistaSeleccionarJefe(){
        this.visSelJef.show();
    }
    
    public void ocultarVistaSeleccionarJefe(){
        this.visSelJef.hide();
    }   
}
