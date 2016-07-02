/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Otros.Constantes;
import Otros.PanelImagen;
import Otros.Reproductor;
import Otros.VistaPersonalizada;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author mam28
 */
public class MeteoritosDeFuego extends VistaPersonalizada {
    
    public MeteoritosDeFuego(){
        Reproductor.reproducirEfecto(Constantes.METEORITOS);
        
        Timer timerAnimacion = new Timer();
        timerAnimacion.schedule(new TimerTask(){
            int tic = 0;
            
            @Override
            public void run(){
                repaint();
                tic++;
                
                if(tic == 3000){
                    dispose();
                    this.cancel();
                    timerAnimacion.cancel();
                }
            }
        }, 0, 1);
        
        this.setImagenFondo("/Imagenes/Otros/magia_3.gif");
    }
}
