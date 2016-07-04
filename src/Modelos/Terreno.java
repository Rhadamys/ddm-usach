/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author mam28
 */
public class Terreno {
    private final ArrayList<Posicion> posiciones;
    
    public Terreno(){
        this.posiciones = new ArrayList<Posicion>();
    }

    public ArrayList<Posicion> getPosiciones() {
        return posiciones;
    }
    
    public Posicion getPosicionAnterior(Posicion posAct){
        return this.posiciones.get(this.posiciones.indexOf(posAct) - 1);
    }
    
    public void agregarPosicion(Posicion posicion){
        this.posiciones.add(posicion);
    }
    
    public boolean contienePosicion(Posicion posicion){
        return this.posiciones.contains(posicion);
    }
    
}
