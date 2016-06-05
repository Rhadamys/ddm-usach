/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Otros.BotonImagen;
import Otros.PanelImagen;

/**
 *
 * @author mam28
 */
public class SubVistaPosicion extends BotonImagen {
    private final PanelImagen iconoElemento;
    private final int fila;
    private final int columna;
    
    public SubVistaPosicion(int fila, int columna){
        setImagenNormal("/Imagenes/Botones/casilla.png");
        setImagenActual(0);
        
        this.fila = fila;
        this.columna = columna;
        this.setLayout(null);
        
        this.iconoElemento = new PanelImagen();
        this.add(this.iconoElemento);
        this.iconoElemento.setSize(this.getSize());
    }
    
    @Override
    public void agregarListeners(){
        // Los listeners se definen en el controlador.
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public PanelImagen getIconoElemento() {
        return iconoElemento;
    }
}
