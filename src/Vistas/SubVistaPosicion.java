/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Otros.BotonCheckImagen;
import Otros.PanelImagen;

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
        
        setImagenNormal("/Imagenes/Botones/casilla.png");
        setImagenSobre("/Imagenes/Botones/casilla.png");
        setImagenPresionado("/Imagenes/Botones/casilla.png");
        setImagenSelNormal("/Imagenes/Botones/casilla_seleccionada.png");
        setImagenSelSobre("/Imagenes/Botones/casilla_seleccionada.png");
        setImagenSelPresionado("/Imagenes/Botones/casilla_seleccionada.png");
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
    
    public void setImagenIconoElemento(String imagen){
        this.iconoElemento.setImagen(imagen);
    }
}
