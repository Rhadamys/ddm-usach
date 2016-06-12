/***********************************************************************
 * Module:  PersonajeNoJugable.java
 * Author:  mam28
 * Purpose: Defines the Class PersonajeNoJugable
 ***********************************************************************/
package Modelos;

import java.util.*;

/** @pdOid 82b5fee8-ed44-4a39-930f-95d7ca70c2e0 */
public class PersonajeNoJugable extends Jugador {
    public PersonajeNoJugable(String nombreJugador, JefeDeTerreno jefe, PuzzleDeDados puzzle){
        this.nombreJugador = nombreJugador;
        this.jefeDeTerreno = jefe;
        this.puzzle = puzzle;
    }
}