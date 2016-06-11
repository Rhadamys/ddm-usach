/***********************************************************************
 * Module:  Jugador.java
 * Author:  mam28
 * Purpose: Defines the Class Jugador
 ***********************************************************************/
package Modelos;

import ModelosDAO.JugadorDAO;
import java.sql.SQLException;
import java.util.*;

/** @pdOid cbe19438-3292-497c-bc58-a1da1e9c38b6 */
public abstract class Jugador {
    protected Turno turno;
    protected PuzzleDeDados puzzle;
    protected String nombreJugador;
    protected JefeDeTerreno jefeDeTerreno;
    protected Terreno terreno;
    protected int equipo;
    
    public Jugador(){
        this.turno = new Turno();
    }
    
    public static ArrayList<Jugador> getJugadores(ArrayList<Jugador> excluidos){
        try {
            return JugadorDAO.getJugadores(excluidos);
        } catch (SQLException ex) {
            return null;
        }
    }
    
    public ArrayList<Dado> getDados(){
        return puzzle.getDados();
    }
    
    public Dado getDado(int i) {
        return puzzle.getDado(i);
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public JefeDeTerreno getJefeDeTerreno() {
        return jefeDeTerreno;
    }

    public Terreno getTerreno() {
        return terreno;
    }

    public int getEquipo() {
        return equipo;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setEquipo(int equipo) {
        this.equipo = equipo;
    }

    public void setTerreno(Terreno terreno) {
        this.terreno = terreno;
    }
          
}