/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Otros;

import java.awt.Color;
import javax.swing.JProgressBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author mam28
 */
public final class BarraVida extends JProgressBar implements ChangeListener{

    public BarraVida(){
        this.setBorder(null);
        this.setBackground(Color.darkGray);
        this.colorearBarraVida();
        this.addChangeListener(this);
    }
    
    public void colorearBarraVida(){
        int porcentaje = this.getValue() * 100 / this.getMaximum();
        if(porcentaje > 50){
            this.setForeground(Color.green);
        }else if(porcentaje > 30){
            this.setForeground(Color.yellow);
        }else if(porcentaje > 15){
            this.setForeground(Color.orange);
        }else{
            this.setForeground(Color.red);
        }
    }
    
    @Override
    public void stateChanged(ChangeEvent ce) {
        colorearBarraVida();
    }
}
