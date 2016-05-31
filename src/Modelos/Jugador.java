/***********************************************************************
 * Module:  Jugador.java
 * Author:  mam28
 * Purpose: Defines the Class Jugador
 ***********************************************************************/
package Modelos;

import java.util.*;

/** @pdOid cbe19438-3292-497c-bc58-a1da1e9c38b6 */
public abstract class Jugador {
    protected ArrayList<Dado> dados;
    protected String nombreJugador;
    protected String tipoJugador;
    protected JefeDeTerreno jefeDeTerreno;
    protected int equipo;

    public ArrayList<Dado> getDados() {
        return dados;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public String getTipoJugador() {
        return tipoJugador;
    }

    public JefeDeTerreno getJefeDeTerreno() {
        return jefeDeTerreno;
    }

    public void setEquipo(int equipo) {
        this.equipo = equipo;
    }
    
}