/***********************************************************************
 * Module:  Jugador.java
 * Author:  mam28
 * Purpose: Defines the Class Jugador
 ***********************************************************************/
package Modelos;

import BD.Conection.Conection;
import ModelosDAO.JugadorDAO;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/** @pdOid cbe19438-3292-497c-bc58-a1da1e9c38b6 */
public abstract class Jugador {
    protected int id;
    protected Turno turno;
    protected PuzzleDeDados puzzle;
    protected String nombreJugador;
    protected JefeDeTerreno jefeDeTerreno;
    protected int equipo;
    protected ArrayList<Trampa> trampas;
    protected Terreno terreno;
    protected int numJug;
    protected int partJug;
    protected int partGan;
    
    public static ArrayList<Jugador> getJugadores(){
        try {
            return JugadorDAO.getJugadores();
        } catch (SQLException ex) {
            // Nada
            return null;
        }
    }
    
    public static void actualizarPartidaGanada (int idJugador, int partidasGanadas) {
        try {
            JugadorDAO.actualizarPartidaGanada(idJugador, partidasGanadas);
        } catch (SQLException ex) {
            // Nada
        }
    }
    
    public static void actualizarPartidaJugada (int idJugador, int partidasJugadas) {
        try {
            JugadorDAO.actualizarPartidaJugada(idJugador, partidasJugadas);
        } catch (SQLException ex) {
            // Nada
        }
    }
    
    public int cantidadCriaturasInvocadas(){
        int cantidadCriaturas = 0;
        for(Dado dado: this.puzzle.getDados()){
            if(dado.isParaJugar() && !dado.isParaLanzar() &&
                    dado.getCriatura().getVida() > 0){
                cantidadCriaturas++;
            }
        }
        
        return cantidadCriaturas;
    }
    
    public boolean haInvocadoCriaturasDelNivel(int nivel){
        for(Dado dado: this.puzzle.getDados()){
            if(dado.isParaJugar() && !dado.isParaLanzar() &&
                    dado.getCriatura().getNivel() == nivel){
                return true;
            }
        }
        return false;
    }
    
    public void devolverDadoAlPuzzle(Criatura criatura){
        this.puzzle.devolverDado(criatura);
    }
    
    public void quitarDadoDelPuzzle(Criatura criatura){
        this.puzzle.quitarDado(criatura);
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
    
    public void reiniciar(int numJug){
        this.numJug = numJug;
        this.turno = new Turno();
        this.trampas = new ArrayList();
        this.jefeDeTerreno.reiniciar(numJug);
        
        // Le asigna dos trampa de cada tipo a este jugador
        this.trampas = new ArrayList();
        for(int j = 1; j <= 3; j++){
            // j: Número de trampa;
            this.agregarTrampa(new Trampa(j, numJug));
            this.agregarTrampa(new Trampa(j, numJug));
        }
            
        // Reinicia los valores de los dados y sus criaturas
        for(Dado dado: this.getDados()){
            dado.setParaLanzar(true);
            dado.getCriatura().reiniciar(numJug);
        }
        
        if(this instanceof PersonajeNoJugable){
            ((PersonajeNoJugable) this).reiniciar();
        }
    }
          
    public int cantidadTrampas(){
        return this.trampas.size();
    }
    
    public int cantidadTrampaDeOso(){
        int cantidadTrampas = 0;
        for(Trampa trampa: this.trampas){
            if(trampa.getNumTrampa() == 1){
                cantidadTrampas++;
            }
        }
        return cantidadTrampas;
    }
    
    public int cantidadTrampaParaLadrones(){
        int cantidadTrampas = 0;
        for(Trampa trampa: this.trampas){
            if(trampa.getNumTrampa() == 2){
                cantidadTrampas++;
            }
        }
        return cantidadTrampas;
    }
    
    public int cantidadRenacerDeLosMuertos(){
        int cantidadTrampas = 0;
        for(Trampa trampa: this.trampas){
            if(trampa.getNumTrampa() == 3){
                cantidadTrampas++;
            }
        }
        return cantidadTrampas;
    }
    
    public void agregarTrampa(Trampa trampa){
        this.trampas.add(trampa);
    }
    
    public void eliminarTrampa(Trampa trampa){
        this.trampas.remove(trampa);
    }
    
// <editor-fold defaultstate="collapsed" desc="Getters && Setters">  
    
    public ArrayList<Dado> getDados(){
        return puzzle.getDados();
    }
    
    public Dado getDado(int i) {
        return puzzle.getDado(i);
    }
    
    public void agregarDado(Dado dado){
        this.puzzle.agregarDado(dado);
    }
    
    public int cantidadDadosDisponibles(){
        int cantidadDados = 0;
        for(Dado dado: this.puzzle.getDados()){
            if(dado.isParaLanzar()){
                cantidadDados++;
            }
        }
        return cantidadDados;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public JefeDeTerreno getJefeDeTerreno() {
        return jefeDeTerreno;
    }

    public int getEquipo() {
        return equipo;
    }

    public Turno getTurno() {
        return turno;
    }

    public Terreno getTerreno() {
        return terreno;
    }

    public int getNumJug() {
        return numJug;
    }

    public ArrayList<Trampa> getTrampas() {
        return trampas;
    }
    
    public Posicion getMiPosicion(){
        return this.getTerreno().getPosiciones().get(0);
    }

    public void setEquipo(int equipo) {
        this.equipo = equipo;
    }

    public void setTerreno(Terreno terreno) {
        this.terreno = terreno;
    }

    public int getId() {
        return id;
    }

    public int getPartJug() {
        return partJug;
    }

    public void aumPartJug() {
        this.partJug++;
    }

    public int getPartGan() {
        return partGan;
    }

    public void aumPartGan() {
        this.partGan++;
    }
    
// </editor-fold>
}