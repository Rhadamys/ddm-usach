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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author mam28
 */
public class ControladorSeleccionarJefe {
    private final ControladorPrincipal contPrin;
    private final VistaSeleccionarJefe visSelJef;
    
    public ControladorSeleccionarJefe(ControladorPrincipal contPrin) throws IOException{
        this.contPrin = contPrin;
        
        this.visSelJef = new VistaSeleccionarJefe(
                this.contPrin.getFuentePersonalizada(),
                JefeDeTerreno.getJefes());
        this.agregarListenersVistaSeleccionarJefe();
        
        this.contPrin.getContVisPrin().agregarVista(this.visSelJef);
    }
    
    public void agregarListenersVistaSeleccionarJefe(){
        ArrayList<BotonImagen> botones = this.visSelJef.getBotones();
        for(BotonImagen boton: botones){
            boton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e){
                    HashMap<String, String> jefe = visSelJef.getJefe(Integer.parseInt(boton.getName()));
                    visSelJef.setNombre(jefe.get("Nombre"));
                    visSelJef.setHabilidad(jefe.get("Habilidad"));
                }
                
                @Override
                public void mouseExited(MouseEvent e){
                    visSelJef.setNombre("");
                    visSelJef.setHabilidad("");
                }
                
                @Override
                public void mouseClicked(MouseEvent e){
                    HashMap<String, String> jefe = visSelJef.getJefe(Integer.parseInt(boton.getName()));
                    contPrin.getContReg().setJefe(new JefeDeTerreno(
                            jefe.get("Clave"),
                            jefe.get("Nombre"),
                            jefe.get("Habilidad"), 
                            Integer.parseInt(jefe.get("Puntos de vida"))));
                    contPrin.getContReg().getVisReg().setIconoJefe("/Imagenes/Jefes/" 
                            + jefe.get("Clave") + ".png");
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
