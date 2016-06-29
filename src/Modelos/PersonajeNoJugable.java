/***********************************************************************
 * Module:  PersonajeNoJugable.java
 * Author:  mam28
 * Purpose: Defines the Class PersonajeNoJugable
 ***********************************************************************/
package Modelos;

public class PersonajeNoJugable extends Jugador {
    
    public PersonajeNoJugable(String nombreJugador, JefeDeTerreno jefe, PuzzleDeDados puzzle){
        this.nombreJugador = nombreJugador;
        this.jefeDeTerreno = jefe;
        this.puzzle = puzzle;
    }
    
}