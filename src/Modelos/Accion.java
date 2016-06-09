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
    private ArrayList<Posicion> posicionesMovimiento;

    public void invocarCriatura(Posicion posicion){
        posicion.setElemento(criaturaAInvocar);
    }
    
    public void moverCriatura(){
        this.posicionesMovimiento.clear();
    }

    public void setCriaturaAInvocar(Criatura criaturaAInvocar) {
        this.criaturaAInvocar = criaturaAInvocar;
    }

    public void setCriaturaAMover(Criatura criaturaAMover) {
        this.criaturaAMover = criaturaAMover;
    }
       
    public void agregarPosicionMovimiento(Posicion posicion){
        this.posicionesMovimiento.add(posicion);
    }
    
    public boolean caminoContienePosicion(Posicion posicion){
        return this.posicionesMovimiento.contains(posicion);
    }
}