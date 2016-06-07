/***********************************************************************
 * Module:  Posicion.java
 * Author:  mam28
 * Purpose: Defines the Class Posicion
 ***********************************************************************/
package Modelos;

import java.util.*;

/** @pdOid 86acb453-8a93-4d0f-b73a-4646653b4f7c */
public class Posicion {
    private ElementoEnCampo elemento;
    private int dueno;
    
    public Posicion(){
        this.dueno = 0;
    }

    public int getDueno() {
        return dueno;
    }

    public void setDueno(int dueno) {
        this.dueno = dueno;
    }

    public void setElemento(ElementoEnCampo elemento) {
        this.elemento = elemento;
    }
        
}