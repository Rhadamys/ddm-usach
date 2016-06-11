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
    
    public void invocarCriatura(Posicion posicion, ArrayList<Dado> dados){
        posicion.setElemento(criaturaAInvocar);
        
        for(Dado dado: dados){
            if(criaturaAInvocar.equals(dado.getCriatura())){
                dado.setParaLanzar(false);
                break;
            }
        }
    }
    
    public ElementoEnCampo moverCriaturaSiguientePosicion(int posAct){
        ElementoEnCampo elementoAnterior = this.posicionesMovimiento.get(0).getElemento();
        
        this.posicionesMovimiento.get(posAct).setElemento(null);
        this.posicionesMovimiento.get(posAct + 1).setElemento(criaturaAMover);
        
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

    public int largoDelCamino() {
        return posicionesMovimiento.size() - 1;
    }

    public Criatura getCriaturaAMover() {
        return criaturaAMover;
    }
    
    public void finalizarMovimiento(){
        this.posicionesMovimiento.clear();
    }
}