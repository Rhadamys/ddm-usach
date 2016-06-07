/***********************************************************************
 * Module:  Tablero.java
 * Author:  mam28
 * Purpose: Defines the Class Tablero
 ***********************************************************************/
package Modelos;

import Vistas.SubVistaPosicion;
import java.util.ArrayList;

public class Tablero {
    private final Posicion[][] posiciones;
    private ArrayList<Jugador> jugadores;
    private int turnoActual;
    private int accion;
    private int direccion;
    private int numDespliegue;
    
    public Tablero(){
        this.posiciones = new Posicion[15][15];
        this.turnoActual = 0;
        this.accion = 0;
        
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                posiciones[i][j] = new Posicion();
            }
        }
    }
        
    /**
     * Devuelve la forma del numDespliegue indicado por "numDespliegue".
     * @param numDespliegue Número del numDespliegue a obtener.
     * @param direccion Dirección del despligue.
     * @param botonActual Botón actual sobre el que se encuentra el mouse.
     * @return 
     */
    public int[][] getDespliegue(int fila, int columna){
        switch(numDespliegue){
            case 0: return getIdxDespliegueCruz(fila, columna, direccion);
            case 1: return getIdxDespliegueEscalera(fila, columna, direccion);
            case 2: return getIdxDespliegueT(fila, columna, direccion);
            case 3: return getIdxDespliegueS(fila, columna, direccion);
            case 4: return getIdxDespliegue4(fila, columna, direccion);
            case 5: return getIdxDespliegueR(fila, columna, direccion);
        }
        return null;
    }
    
    /**
     * Devuelve los índices de posiciones en el tablero que conforman el numDespliegue cruz.
     * @param botonActual Botón sobre el que se encuentra el mouse actualmente.
     * @param direccion Dirección del numDespliegue.
     * @return Despliegue cruz.
     */
    public int[][] getIdxDespliegueCruz(int fila, int columna, int direccion){
        int dirVer = direccion == 2 ? -1: 1;
        int dirHor = direccion == 3 ? -1: 1;
        
        switch(direccion){
            case 0: 
            case 2: int[][] idxDespliegueV = {
                        {fila + dirVer, columna},
                        {fila - dirVer, columna},
                        {fila - 2 * dirVer, columna},
                        {fila, columna + dirHor},
                        {fila, columna - dirHor},
                        {fila, columna}};
                    return idxDespliegueV;
            case 1: 
            case 3: int[][] idxDespliegueH = {
                        {fila, columna + dirHor},
                        {fila, columna - dirHor},
                        {fila, columna - 2 * dirHor},
                        {fila + dirVer, columna},
                        {fila - dirVer, columna},
                        {fila, columna}};
                    return idxDespliegueH;
        }
        
        return null;
    }
    
    /**
     * Devuelve los índices de posiciones en el tablero que conforman el numDespliegue escalera.
     * @param botonActual Botón sobre el que se encuentra el mouse actualmente.
     * @param direccion Dirección del numDespliegue.
     * @return Despliegue escalera.
     */
    public int[][] getIdxDespliegueEscalera(int fila, int columna, int direccion){
        int dirVer = direccion == 2 ? -1: 1;
        int dirHor = direccion == 3 ? -1: 1;
        
        switch(direccion){
            case 0: 
            case 2: int[][] idxDespliegueV = {
                        {fila + dirVer, columna - dirHor},
                        {fila, columna - dirHor},
                        {fila - dirVer, columna},
                        {fila - dirVer, columna + dirHor},
                        {fila - 2 * dirVer, columna + dirHor},
                        {fila, columna}};
                    return idxDespliegueV;
            case 1: 
            case 3: int[][] idxDespliegueH = {
                        {fila + dirVer, columna + dirHor},
                        {fila + dirVer, columna},
                        {fila, columna - dirHor},
                        {fila - dirVer, columna - dirHor},
                        {fila - dirVer, columna - 2 * dirHor},
                        {fila, columna}};
                    return idxDespliegueH;
        }
        
        return null;
    }
    
    /**
     * Devuelve los índices de posiciones en el tablero que conforman el numDespliegue T.
     * @param botonActual Botón sobre el que se encuentra el mouse actualmente.
     * @param direccion Dirección del numDespliegue.
     * @return Despliegue T.
     */
    public int[][] getIdxDespliegueT(int fila, int columna, int direccion){
        int dirVer = direccion == 2 ? -1: 1;
        int dirHor = direccion == 3 ? -1: 1;
        
        switch(direccion){
            case 0: 
            case 2: int[][] idxDespliegueV = {
                        {fila - 3 * dirVer, columna},
                        {fila - dirVer, columna},
                        {fila - 2 * dirVer, columna},
                        {fila, columna - dirHor},
                        {fila, columna + dirHor},
                        {fila, columna}};
                    return idxDespliegueV;
            case 1: 
            case 3: int[][] idxDespliegueH = {
                        {fila, columna - dirHor},
                        {fila, columna - 2 * dirHor},
                        {fila, columna - 3 * dirHor},
                        {fila + dirVer, columna},
                        {fila - dirVer, columna},
                        {fila, columna}};
                    return idxDespliegueH;
        }
        
        return null;
    }
    
    /**
     * Devuelve los índices de posiciones en el tablero que conforman el numDespliegue S.
     * @param botonActual Botón sobre el que se encuentra el mouse actualmente.
     * @param direccion Dirección del numDespliegue.
     * @return Despliegue S.
     */
    public int[][] getIdxDespliegueS(int fila, int columna, int direccion){
        int dirVer = direccion == 2 ? -1: 1;
        int dirHor = direccion == 3 ? -1: 1;
        
        switch(direccion){
            case 0: 
            case 2: int[][] idxDespliegueV = {
                        {fila, columna - dirHor},
                        {fila, columna - 2 * dirHor},
                        {fila + dirVer, columna - 2 * dirHor},
                        {fila, columna + dirHor},
                        {fila - dirVer, columna + dirHor},
                        {fila, columna}};
                    return idxDespliegueV;
            case 1: 
            case 3: int[][] idxDespliegueH = {
                        {fila - dirVer, columna},
                        {fila - 2 * dirVer, columna},
                        {fila - 2 * dirVer, columna - dirHor},
                        {fila + dirVer, columna},
                        {fila + dirVer, columna + dirHor},
                        {fila, columna}};
                    return idxDespliegueH;
        }
        
        return null;
    }
    
    /**
     * Devuelve los índices de posiciones en el tablero que conforman el numDespliegue 4.
     * @param botonActual Botón sobre el que se encuentra el mouse actualmente.
     * @param direccion Dirección del numDespliegue.
     * @return Despliegue 4.
     */
    public int[][] getIdxDespliegue4(int fila, int columna, int direccion){
        int dirVer = direccion == 2 ? -1: 1;
        int dirHor = direccion == 3 ? -1: 1;
        
        switch(direccion){
            case 0: 
            case 2: int[][] idxDespliegueV = {
                        {fila - dirVer, columna},
                        {fila - 2 * dirVer, columna},
                        {fila, columna - dirHor},
                        {fila + dirVer, columna - dirHor},
                        {fila + 2 * dirVer, columna - dirHor},
                        {fila, columna}};
                    return idxDespliegueV;
            case 1: 
            case 3: int[][] idxDespliegueH = {
                        {fila, columna + dirHor},
                        {fila, columna + 2 * dirHor},
                        {fila - dirVer, columna},
                        {fila - dirVer, columna - dirHor},
                        {fila - dirVer, columna - 2 * dirHor},
                        {fila, columna}};
                    return idxDespliegueH;
        }
        
        return null;
    }
    
    /**
     * Devuelve los índices de posiciones en el tablero que conforman el numDespliegue R.
     * @param botonActual Botón sobre el que se encuentra el mouse actualmente.
     * @param direccion Dirección del numDespliegue.
     * @return Despliegue R.
     */
    public int[][] getIdxDespliegueR(int fila, int columna, int direccion){
        int dirVer = direccion == 2 ? -1: 1;
        int dirHor = direccion == 3 ? -1: 1;
        
        switch(direccion){
            case 0: 
            case 2: int[][] idxDespliegueV = {
                        {fila - dirVer, columna},
                        {fila, columna + dirHor},
                        {fila, columna - dirHor},
                        {fila + dirVer, columna - dirHor},
                        {fila + dirVer, columna - 2 * dirHor},
                        {fila, columna}};
                    return idxDespliegueV;
            case 1: 
            case 3: int[][] idxDespliegueH = {
                        {fila - dirVer, columna},
                        {fila, columna - dirHor},
                        {fila + dirVer, columna},
                        {fila + dirVer, columna + dirHor},
                        {fila + 2 * dirVer, columna + dirHor},
                        {fila, columna}};
                    return idxDespliegueH;
        }
        
        return null;
    }
    
    /**
     * Comprueba que la posición actual del despliegue esté conectada al terreno
     * del jugador.
     * @param idxCasillas Índices de las posiciones en el tablero que conforman el despliegue.
     * @param jugador Jugador que está intentando invocar.
     * @return Verdadero o falso indicando si está conectado al terreno.
     */
    public boolean estaConectadoAlTerreno(int[][] idxCasillas, int jugador){
        for(int[] coord: idxCasillas){
            
            int[][] vecinos = {
                {coord[0] - 1, coord[1]},
                {coord[0] + 1, coord[1]},
                {coord[0], coord[1] + 1},
                {coord[0], coord[1] - 1}};
            
            for(int[] vecino: vecinos){
                try{
                    if(this.posiciones[vecino[0]][vecino[1]].getDueno() == jugador){
                       return true;
                    }
                }catch(Exception e){
                   return false;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Verifica si el terreno está disponible para invocar.
     * @param idxCasillas Índices de las posiciones en el tablero que conforman el despliegue.
     * @return Verdadedo o falso dependiendo de si está disponible el terreno.
     */
    public boolean estaDisponible(int[][] idxCasillas){
        for(int[] coord: idxCasillas){      
            try{
                if(this.posiciones[coord[0]][coord[1]].getDueno() != 0){
                    return false;
                }
            }catch(Exception e){
               return false;
            }
        }
        
        return true;
    }
    
    public void asignarCasilla(int[] idxCasilla, int jugador){
        this.posiciones[idxCasilla[0]][idxCasilla[1]].setDueno(jugador);
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

    public int getNumDespliegue() {
        return numDespliegue;
    }

    public Posicion[][] getPosiciones() {
        return posiciones;
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(ArrayList<Jugador> jugadores) {
        this.jugadores = jugadores;
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

    public void setNumDespliegue(int despliegue) {
        this.numDespliegue = despliegue;
    }
        
}