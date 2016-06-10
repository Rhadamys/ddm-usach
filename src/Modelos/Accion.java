/***********************************************************************
 * Module:  Accion.java
 * Author:  mam28
 * Purpose: Defines the Class Accion
 ***********************************************************************/
package Modelos;

import java.util.*;

/** @pdOid 3209e44d-1fcb-494a-b232-5edd8db5452f */
public class Accion {
    private Criatura criaturaAInvocar;
    private Criatura criaturaAMover;
    private final ArrayList<Posicion> posicionesMovimiento;

    public Accion(){
        this.posicionesMovimiento = new ArrayList();
    }
    
    public void invocarCriatura(Posicion posicion){
        posicion.setElemento(criaturaAInvocar);
    }
    
    public ElementoEnCampo moverCriaturaSiguientePosicion(){
        ElementoEnCampo elementoAnterior = this.posicionesMovimiento.get(0).getElemento();
        
        this.posicionesMovimiento.get(0).setElemento(null);
        this.posicionesMovimiento.get(1).setElemento(criaturaAMover);
        this.posicionesMovimiento.remove(0);
        
        return elementoAnterior;
    }

    public void setCriaturaAInvocar(Criatura criaturaAInvocar) {
        this.criaturaAInvocar = criaturaAInvocar;
    }

    public void setCriaturaAMover(Criatura criaturaAMover) {
        this.criaturaAMover = criaturaAMover;
    }
       
    public void agregarPosicionAlCamino(Posicion posicion){
        this.posicionesMovimiento.add(posicion);
    }
    
    public void eliminarPosicionDelCamino(Posicion posicion){
        this.posicionesMovimiento.remove(posicion);
    }
    
    public Posicion obtenerPosicionCamino(int i){
        return this.posicionesMovimiento.get(i);
    }
    
    public boolean caminoContienePosicion(Posicion posicion){
        return this.posicionesMovimiento.contains(posicion);
    }
    
    public Posicion obtenerUltimaPosicionAgregada(){
        return this.posicionesMovimiento.get(this.posicionesMovimiento.size() - 1);
    }

    public int getLargoDelCamino() {
        return posicionesMovimiento.size();
    }

    public Criatura getCriaturaAMover() {
        return criaturaAMover;
    }
    
    public void finalizarMovimiento(){
        this.posicionesMovimiento.clear();
    }
}