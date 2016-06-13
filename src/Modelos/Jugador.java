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
    protected ArrayList<Trampa> trampas;
        
    public static ArrayList<Jugador> getJugadores(ArrayList<Jugador> excluidos){
        try {
            return JugadorDAO.getJugadores(excluidos);
        } catch (SQLException ex) {
            return null;
        }
    }
    
    public ArrayList<Criatura> getCriaturasMuertas(){
        ArrayList<Criatura> criaturasMuertas = new ArrayList();
        for(Dado dado: getDados()){
            if(dado.getCriatura().getVida() <= 0){
                criaturasMuertas.add(dado.getCriatura());
            }
        }
        return criaturasMuertas;
    }
    
    public void reiniciar(){
        this.turno = new Turno();
        this.trampas = new ArrayList();
    }
          
    public int cantidadTrampas(){
        return this.trampas.size();
    }
    
    public void agregarTrampa(Trampa trampa){
        this.trampas.add(trampa);
    }
    
    public void eliminarTrampa(Trampa trampa){
        this.trampas.remove(trampa);
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

    public ArrayList<Trampa> getTrampas() {
        return trampas;
    }

    public void setEquipo(int equipo) {
        this.equipo = equipo;
    }

    public void setTerreno(Terreno terreno) {
        this.terreno = terreno;
    }
}