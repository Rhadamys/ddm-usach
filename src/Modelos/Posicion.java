/***********************************************************************
 * Module:  Posicion.java
 * Author:  mam28
 * Purpose: Defines the Class Posicion
 ***********************************************************************/
package Modelos;

public class Posicion {
    private final int fila;
    private final int columna;
    private ElementoEnCampo elemento;
    private Trampa respaldoTrampa;
    private int dueno;
    
    public Posicion(int fila, int columna){
        this.dueno = 0;
        this.fila = fila;
        this.columna = columna;
    }

    public void respaldarTrampa(){
        this.respaldoTrampa = (Trampa) this.elemento;
    }
    
    public void devolverTrampa(){
        this.elemento = this.respaldoTrampa;
        this.respaldoTrampa = null;
    }
    
    public boolean trampaRespaldada(){
        return this.respaldoTrampa != null;
    }
    
    public int getDueno() {
        return dueno;
    }

    public ElementoEnCampo getElemento() {
        return elemento;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setDueno(int dueno) {
        this.dueno = dueno;
    }

    public void setElemento(ElementoEnCampo elemento) {
        this.elemento = elemento;
    }
        
}