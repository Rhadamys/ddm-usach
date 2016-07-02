/***********************************************************************
 * Module:  PersonajeNoJugable.java
 * Author:  mam28
 * Purpose: Defines the Class PersonajeNoJugable
 ***********************************************************************/
package Modelos;

import java.util.ArrayList;
import java.util.Random;

public class PersonajeNoJugable extends Jugador {
    private int nivel;
    private int numMagia;
    private int numTrampa;
    private int numAtaques;
    
    public PersonajeNoJugable(
            int id,
            String nombreJugador,
            JefeDeTerreno jefe,
            PuzzleDeDados puzzle,
            int partJug,
            int partGan){
        
        this.id = id;
        this.nombreJugador = nombreJugador;
        this.jefeDeTerreno = jefe;
        this.puzzle = puzzle;
        this.nivel = 2;
        this.partJug = partJug;
        this.partGan = partGan;
    }

    public void reiniciar(){
        Random rnd = new Random();
        this.numMagia = rnd.nextInt(3) + 1;
        this.numTrampa = rnd.nextInt(3) + 1;
    }
    
    public int numJugMenorVida(Tablero tablero){
        int numJugMenorVida = -1;
        
        for(int i = 0; i < tablero.cantidadJugadores(true); i++){
            if(i != this.getNumJug() - 1){
                Jugador jugAct = tablero.getJugador(i);
                if(!tablero.estaEnPerdedores(jugAct) && (
                        numJugMenorVida == -1 || jugAct.getJefeDeTerreno().getVida() <
                        tablero.getJugador(numJugMenorVida - 1).getJefeDeTerreno().getVida())){

                    numJugMenorVida = i + 1;
                }
            }
        }

        return numJugMenorVida;
    }

    public Posicion posJugMenorVida(Tablero tablero){
        return tablero.getJugador(this.numJugMenorVida(tablero) - 1).getMiPosicion();
    }
    
    public boolean estoyConectadoAlTerrenoJugMenorVida(Tablero tablero){
        Terreno terrenoJugMenorVida = tablero.getJugador(this.numJugMenorVida(tablero) - 1).getTerreno();
        ArrayList<Integer> numJugConectados = new ArrayList();
        
        for(Posicion posJug: this.getTerreno().getPosiciones()){
            for(int[] coord: tablero.getIdxVecinos(posJug)){
                try{
                    Posicion posAct = tablero.getPosicion(coord[0], coord[1]);
                    if(terrenoJugMenorVida.contienePosicion(posAct)){
                        return true;
                    }else if(posAct.getDueno() != 0 && posAct.getDueno() != this.getNumJug() &&
                            !numJugConectados.contains(posAct.getDueno())){
                        numJugConectados.add(posAct.getDueno());
                    }
                }catch(Exception e){
                    // Nada
                }
            }
        }
        
        ArrayList<Integer> numJugConectadosOtroJug = new ArrayList();
        
        for(Posicion posJug: terrenoJugMenorVida.getPosiciones()){
            for(int[] coord: tablero.getIdxVecinos(posJug)){
                try{
                    Posicion posAct = tablero.getPosicion(coord[0], coord[1]);
                    if(posAct.getDueno() != 0 && posAct.getDueno() != this.getNumJug() &&
                            !numJugConectadosOtroJug.contains(posAct.getDueno())){
                        numJugConectadosOtroJug.add(posAct.getDueno());
                    }
                }catch(Exception e){
                    // Nada
                }
            }
        }
        
        for(int jugConectado: numJugConectados){
            if(numJugConectadosOtroJug.contains(jugConectado)){
                return true;
            }
        }
        
        return false;
    }
    
    public boolean estoyEnPeligro(Tablero tablero){
        return this.posicionEnemigoPeligroso(tablero) != null;
    }
    
    public Posicion posicionEnemigoPeligroso(Tablero tablero){
        for(int[] vecino: tablero.getIdxVecinos(this.getMiPosicion())){
            try{
                Posicion posVecino = tablero.getPosicion(vecino[0], vecino[1]);
                if(posVecino.getElemento() instanceof Criatura &&
                        posVecino.getElemento().getDueno() != this.getNumJug()){
                    return posVecino;
                }
            }catch(Exception e){
                // Nada
            }
        }
        
        return null;
    }

    public int getNumMagia() {
        return numMagia;
    }

    public int getNumTrampa() {
        return numTrampa;
    }
    
    public Trampa getTrampa(int numTrampa){
        for(Trampa trampa: this.trampas){
            if(trampa.getNumTrampa() == numTrampa){
                return trampa;
            }
        }
        
        return null;
    }
    
    public boolean puedoAtacar(){
        switch(this.nivel){
            case 1: return numAtaques < 3;
            case 2: return numAtaques < 5;
            default: return true;
        }
    }
    
    public boolean puedoActivarMagia(){
        switch(numMagia){
            case 1: return this.turno.getPuntosMagia() >= 10;
            case 2: return this.turno.getPuntosMagia() >= 15;
            default: return this.turno.getPuntosMagia() >= 30;
        }
    }
    
    public boolean puedoPonerTrampa(){
        System.out.println("Puntos trampa: " + this.turno.getPuntosTrampa());
        switch(numTrampa){
            case 1: return !this.trampas.isEmpty() && this.turno.getPuntosTrampa() >= 10;
            case 2: return !this.trampas.isEmpty() && this.turno.getPuntosTrampa() >= 15;
            default: return !this.trampas.isEmpty() && this.turno.getPuntosTrampa() >= 30;
        }
    }
    
    public void cambiarNumMagia(){
        int nuevoNumMagia;
        do{
            nuevoNumMagia = new Random().nextInt(3) + 1;
        }while(nuevoNumMagia == numMagia);
        
        this.numMagia = nuevoNumMagia;
    }
    
    public void cambiarNumTrampa(){
        if(!this.trampas.isEmpty()){
            int nuevoNumTrampa;
            boolean encontreTrampa = false;
            do{
                nuevoNumTrampa = new Random().nextInt(3) + 1;
                if(this.getTrampa(nuevoNumTrampa) != null){
                    encontreTrampa = true;
                }
            }while(!encontreTrampa);

            this.numTrampa = nuevoNumTrampa;
        }
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getNumAtaques() {
        return numAtaques;
    }

    public void setNumAtaques(int numAtaques) {
        this.numAtaques = numAtaques;
    }
    
}