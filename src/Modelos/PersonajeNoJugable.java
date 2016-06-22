/***********************************************************************
 * Module:  PersonajeNoJugable.java
 * Author:  mam28
 * Purpose: Defines the Class PersonajeNoJugable
 ***********************************************************************/
package Modelos;

public class PersonajeNoJugable extends Jugador {
    private int numAccion;
    private boolean lanceDados = false;
    
    public PersonajeNoJugable(String nombreJugador, JefeDeTerreno jefe, PuzzleDeDados puzzle){
        this.nombreJugador = nombreJugador;
        this.jefeDeTerreno = jefe;
        this.puzzle = puzzle;
        this.numAccion = 0;
    }

    public int getNumAccion() {
        return numAccion;
    }

    public void setNumAccion(int accion) {
        this.numAccion = accion;
    }

    public boolean isLanceDados() {
        return lanceDados;
    }

    public void setLanceDados(boolean lanceDados) {
        this.lanceDados = lanceDados;
    }
}