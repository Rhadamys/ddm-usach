/***********************************************************************
 * Module:  Tablero.java
 * Author:  mam28
 * Purpose: Defines the Class Tablero
 ***********************************************************************/
package Modelos;

import Vistas.CompPosicion;
import Vistas.CompTablero;

public class Tablero {
    private CompTablero tablero;
    private CompPosicion[][] posiciones;
    private int turnoActual;
    private int accion;
    private int direccion;
    private int despliegue;
    
    public Tablero(){
        this.turnoActual = 0;
        this.accion = 0;
    }

    public int getTurnoActual() {
        return turnoActual;
    }

    public int getAccion() {
        return accion;
    }

    public int getDireccion() {
        return direccion;
    }

    public int getDespliegue() {
        return despliegue;
    }

    public void setTurnoActual(int turnoActual) {
        this.turnoActual = turnoActual;
    }

    public void setAccion(int accion) {
        this.accion = accion;
    }

    public void setDireccion(int direccion) {
        this.direccion = direccion;
    }

    public void setDespliegue(int despliegue) {
        this.despliegue = despliegue;
    }
    
    
}