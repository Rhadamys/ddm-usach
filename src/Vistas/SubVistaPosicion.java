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
public class SubVistaPosicion extends BotonImagen {
    private int fila;
    private int columna;
    private int dueno = 0;
    
    public SubVistaPosicion(int fila, int columna){
        setImagenNormal("/Imagenes/Botones/casilla.png");
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

    public int getDueno() {
        return dueno;
    }

    public void setDueno(int dueno) {
        this.dueno = dueno;
        this.setImagenNormal("/Imagenes/Botones/casilla_j" + dueno + ".png");
    }
}
