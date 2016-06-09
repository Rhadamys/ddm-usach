/***********************************************************************
 * Module:  Terreno.java
 * Author:  mam28
 * Purpose: Defines the Class Terreno
 ***********************************************************************/
package Modelos;

import java.util.*;

/** @pdOid 07aa83ed-8603-48f6-9dc8-14f936273e65 */
public class Terreno {
    private final ArrayList<Posicion> posiciones;
    
    public Terreno(){
        this.posiciones = new ArrayList();
    }
    
    public void agregarCasilla(Posicion posicion){
        this.posiciones.add(posicion);
    }
    
    public int cantidadCriaturasInvocadas(){
        int cantidadCriaturas = 0;
        for(Posicion posicion: posiciones){
            if(posicion.getElemento() != null &&
               posicion.getElemento() instanceof Criatura){
                cantidadCriaturas++;
            }
        }
        return cantidadCriaturas;
    }
    
    public boolean contienePosicion(Posicion posicion){
        return this.posiciones.contains(posicion);
    }
}