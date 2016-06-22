/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Otros.BotonCheckImagen;
import Otros.Constantes;
import Otros.PanelImagen;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author mam28
 */
public class SubVistaPosicion extends BotonCheckImagen {
    private final PanelImagen iconoElemento;
    private final int fila;
    private final int columna;
    
    public SubVistaPosicion(int fila, int columna, int ancho, int alto){
        this.setSize(ancho, alto);
        
        setImagenNormal(Constantes.CASILLA);
        setImagenSelNormal(Constantes.CASILLA_SELECCIONADA);
        setImagenActual(0);
        
        this.fila = fila;
        this.columna = columna;
        this.setLayout(null);
        
        this.iconoElemento = new PanelImagen();
        this.iconoElemento.setSize(this.getWidth() * 80 / 100, this.getHeight() * 80 / 100);
        this.iconoElemento.setLocation(
                (this.getWidth() - this.iconoElemento.getWidth()) / 2,
                (this.getHeight() - this.iconoElemento.getHeight()) / 2);
        
        this.add(this.iconoElemento);
    }
    
    public void casillaCorrecta(){
        this.setImagenSobre(Constantes.CASILLA_CORRECTA);
        this.setImagenActual(1);
    }
    
    public void casillaIncorrecta(){
        this.setImagenSobre(Constantes.CASILLA_INCORRECTA);
        this.setImagenActual(1);
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }
    
    public void setImagenElemento(String imagen){
        this.iconoElemento.setImagen(imagen);
    }
    
    public void parpadearIcono(){
        this.iconoElemento.setVisible(!this.iconoElemento.isVisible());
    }
}
