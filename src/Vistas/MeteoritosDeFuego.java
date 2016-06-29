/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Otros.Constantes;
import Otros.PanelImagen;
import Otros.VistaPersonalizada;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author mam28
 */
public class MeteoritosDeFuego extends VistaPersonalizada {
    private final int[][] posMet = {};
    private final ArrayList<PanelImagen> pansMet;
    
    
    public MeteoritosDeFuego(){
        this.pansMet = new ArrayList();
        
        for(int[] coord: posMet){
            PanelImagen panMet = new PanelImagen("/Imagenes/Otros/meteorito.png");
            this.add(panMet);
            panMet.setSize(250, 125);
            panMet.setLocation(coord[0], coord[1]);
            this.pansMet.add(panMet);
        }

        MeteoritosDeFuego metDeFue = this;
        Timer timerAnimacion = new Timer();
        timerAnimacion.schedule(new TimerTask(){
            int tic = 0;
            
            @Override
            public void run(){
                for(PanelImagen panMet: pansMet){
                    panMet.setLocation(panMet.getX() + 2, panMet.getY() + 1);
                }
                tic++;
                
                if(tic == 800){
                    this.cancel();
                    timerAnimacion.cancel();
                }
                
                metDeFue.repaint();
            }
        }, 0, 5);
        
        this.setImagenFondo(Constantes.VACIO);
    }
}
