/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Otros.BotonImagen;

/**
 *
 * @author mam28
 */
public class CompPosicion extends BotonImagen {
    private int fila;
    private int columna;
    
    public CompPosicion(int fila, int columna){
        setImagenFuera("/Imagenes/Botones/casilla.png");
        setImagenSobre("/Imagenes/Botones/casilla_j1.png");
        setImagenPresionado("/Imagenes/Botones/boton_cuadrado_presionado.png");
        setImagenActual(0);
        
        this.fila = fila;
        this.columna = columna;
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
    
}
