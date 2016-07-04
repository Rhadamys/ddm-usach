/***********************************************************************
 * Module:  Tablero.java
 * Author:  mam28
 * Purpose: Defines the Class Tablero
 ***********************************************************************/
package Modelos;

import Otros.Registro;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tablero {
    private final ArrayList<Jugador> perdedores;
    private final ArrayList<Jugador> jugadores;
    private final Posicion[][] posiciones;
    private int turnoActual;
    private final int[] ordenTurnos;
    private int numAccion;
    private int direccion;
    private int numDespliegue;
    private Trampa trampaActivada;
    private final boolean enEquipos;
    private final boolean torneo;
    
    public Tablero(ArrayList<Jugador> jugadores, boolean esEnEquipos, boolean esTorneo){
        this.posiciones = new Posicion[15][15];
        this.turnoActual = 0;
        this.numAccion = 0;
        this.enEquipos = esEnEquipos;
        this.torneo = esTorneo;
        
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                posiciones[i][j] = new Posicion(i, j);
            }
        }
        
        this.jugadores = jugadores;
        this.perdedores = new ArrayList<Jugador>();        
        this.ordenTurnos = new int[this.jugadores.size()];
        
        ArrayList<Integer> orden = new ArrayList<Integer>();
        Random rnd = new Random();
        for(int i = 0; i < this.ordenTurnos.length; i++){
            int numJug = 0;
            do{
                numJug = rnd.nextInt(this.jugadores.size());
            }while(orden.contains(numJug));
            
            orden.add(numJug);
        }
        
        for(int i = 0; i < this.ordenTurnos.length; i++){
            this.ordenTurnos[i] = orden.get(i);
        }       
        
    }
    
    /**
     * Aplica las magias que se hayan activado.
     * @param accion Accion para activar las magias.
     */
    public void aplicarMagias(Accion accion){
        // Las magias son un Array de enteros
        // [ <Numero de magia>, <Turnos restantes>, <Numero del jugador que la activó> , <Costo de la trampa> ]
        
        ArrayList<int[]> magiasActivadas = accion.getMagiasActivadas();
        
        if(!magiasActivadas.isEmpty()){
            for(int[] magia: magiasActivadas){
                switch(magia[0]){
                    case 1: if(magia[1] == 3){
                                accion.lluviaTorrencial(magia[2], this);
                            }
                            break;
                    case 2: accion.hierbasVenenosas(magia[2]);
                            break;
                    default:    accion.meteoritosDeFuego(magia[2], this);
                                break;
                }

                if(magia[0] == 1){
                    // Dura 3 turnos de cada jugador
                    if(this.getTurnoActual() + 1 == magia[2]){
                        magia[1]--;
                    }
                }else{
                    // Dura 3 turnos del juego
                    magia[1]--;
                }

                if(magia[1] == 0){
                    accion.desactivarMagia(magia[0], this);
                }
            }
        }
    }
    
    /**
     * Cambiar el número del turno.
     */
    public void cambiarTurno(){
        do{
            this.turnoActual = turnoActual == this.jugadores.size() - 1 ? 0 : this.turnoActual + 1;
        }while(this.estaEnPerdedores(this.getJugadorActual()));
    }
        
    /**
     * Devuelve la forma del numDespliegue indicado por "numDespliegue".
     * @param fila
     * @param columna
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
     * @param fila Fila dentro del tablero de la posición actual en la que se encuentra el mouse.
     * @param columna Columna dentro del tablero de la posición actual en la que se encuentra el mouse.
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
     * @param fila Fila dentro del tablero de la posición actual en la que se encuentra el mouse.
     * @param columna Columna dentro del tablero de la posición actual en la que se encuentra el mouse.
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
     * @param fila Fila dentro del tablero de la posición actual en la que se encuentra el mouse.
     * @param columna Columna dentro del tablero de la posición actual en la que se encuentra el mouse.
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
     * @param fila Fila dentro del tablero de la posición actual en la que se encuentra el mouse.
     * @param columna Columna dentro del tablero de la posición actual en la que se encuentra el mouse.
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
     * @param fila Fila dentro del tablero de la posición actual en la que se encuentra el mouse.
     * @param columna Columna dentro del tablero de la posición actual en la que se encuentra el mouse.
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
     * @param fila Fila dentro del tablero de la posición actual en la que se encuentra el mouse.
     * @param columna Columna dentro del tablero de la posición actual en la que se encuentra el mouse.
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
     * Devuelve los índices de las posiciones vecinas a la posición indicada.
     * @param posicion Posición para la cual se obtendrán los vecinos.
     * @return Arreglo de índices.
     */
    public int[][] getIdxVecinos(Posicion posicion){
        int[][] vecinos = {{posicion.getFila() - 1, posicion.getColumna()},
                           {posicion.getFila() + 1, posicion.getColumna()},
                           {posicion.getFila(), posicion.getColumna() - 1},
                           {posicion.getFila(), posicion.getColumna() + 1}};
        
        return vecinos;
    }
    
    /**
     * Comprueba que la posición actual del despliegue esté conectada al terreno
     * del jugador.
     * @param idxCasillas Índices de las posiciones en el tablero que conforman el despliegue.
     * @param numJug Jugador que está intentando invocar.
     * @return Verdadero o falso indicando si está conectado al terreno.
     */
    public boolean estaConectadoAlTerreno(int[][] idxCasillas, int numJug){
        for(int[] coord: idxCasillas){
            Posicion posAct = this.getPosicion(coord[0], coord[1]);
            if(posAct != null){
                for(int[] vecino: getIdxVecinos(posAct)){
                    Posicion posVecino = this.posiciones[vecino[0]][vecino[1]];
                    if(posVecino != null && posVecino.getDueno() == numJug){
                       return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * Comprueba si el terreno del jugador actual está conectado al terreno de otros
     * jugadores.
     * @return Verdadero si está conectado al terreno de otros jugadores.
     */
    public boolean estaConectadoAlTerrenoDeOtros(){
        for(Posicion posJug: this.getJugadorActual().getTerreno().getPosiciones()){
            for(int[] coord: this.getIdxVecinos(posJug)){
                Posicion posAct = this.getPosicion(coord[0], coord[1]);
                if(posAct != null && posAct.getDueno() != 0 &&
                        posAct.getDueno() != this.getJugadorActual().getNumJug()){
                    return true;
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
            Posicion posAct = this.posiciones[coord[0]][coord[1]];
            if(posAct != null && posAct.getDueno() != 0){
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Asigna una casilla a un jugador y la agrega a su terreno.
     * @param idxCasilla
     * @param numJug 
     */
    public void asignarPosicion(int[] idxCasilla, int numJug){
        Posicion posicion = this.posiciones[idxCasilla[0]][idxCasilla[1]];
        posicion.setDueno(numJug);
        this.getJugador(numJug - 1).getTerreno().agregarPosicion(posicion);
    }
    
    /**
     * Comprueba si los jugadores restantes son todos PNJs.
     * @return Verdadero si sólo quedan PNJs en la partida.
     */
    public boolean soloQuedanPNJs(){
        for(Jugador jug: this.jugadores){
            if(!this.perdedores.contains(jug) &&
                jug instanceof Usuario){
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Obtiene la cantidad de jugadores.
     * @param todos Verdadero si se desea obtener el total de jugadores. Falso
     * si sólo se desea obtener la cantidad restante de jugadores en la partida.
     * @return Cantidad de jugadores.
     */
    public int cantidadJugadores(boolean todos){
        int numJugadores = 0;
        for(Jugador jugador: this.jugadores){
            if(todos == true || (todos == false && !this.estaEnPerdedores(jugador))){
                numJugadores++;
            }
        }
        return numJugadores;
    }
    
    /**
     * Devuelve la cantidad de jugadores que poseen criaturas invocadas en el
     * terreno.
     * @return Cantidad de jugadores que tienen criaturas invocadas.
     */
    public int cantidadJugadoresTienenCriaturas(){
        int cantidadJugadoresTienenCriaturas = 0;
        for(Jugador jugador: this.jugadores){
            if(!estaEnPerdedores(jugador) && jugador.cantidadCriaturasInvocadas() > 0){
                    cantidadJugadoresTienenCriaturas++;
            }
        }
        return cantidadJugadoresTienenCriaturas;
    }
    
    /**
     * Devuelve el total de invocaciones realizadas por los otros jugadores (Que
     * no sea el jugador del turno actual).
     * @return Cantidad de invocaciones de los otros jugadores.
     */
    public int cantidadInvOtrosJugadores(){
        int numInvOtrosJugadores = 0;
        for(Jugador jug: this.jugadores){
            if(jug != this.getJugadorActual()){
                if(enEquipos &&
                        jug.getEquipo() != this.getJugadorActual().getEquipo()){
                    numInvOtrosJugadores += jug.cantidadCriaturasInvocadas();
                }else if(!enEquipos){
                    numInvOtrosJugadores += jug.cantidadCriaturasInvocadas();
                }
            }
        }
        
        return numInvOtrosJugadores;
    }
    
    /**
     * Agrega un perdedor.
     * @param numJug Número del jugador que perdió.
     */
    public void agregarPerdedor(int numJug){
        this.perdedores.add(this.jugadores.get(numJug - 1));
        
        Registro.registrarAccion(Registro.JUGADOR_PIERDE,
                this.jugadores.get(numJug - 1).getNombreJugador());
    }
    
    /**
     * Comprueba si el jugador señalado está en la lista de perdedores.
     * @param jugador Jugador que se consultará.
     * @return Verdadero si el jugador está en la lista de perdedores.
     */
    public boolean estaEnPerdedores(Jugador jugador){
        return this.perdedores.contains(jugador);
    }
    
    /**
     * Devuelve la posición que ocupa el elemento señalado.
     * @param elemento Elemento que se buscará en el tablero.
     * @return Posición del elemento en el tablero.
     */
    public Posicion getPosElem(ElementoEnCampo elemento){
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                Posicion posAct = this.posiciones[i][j];
                if(posAct != null && posAct.getElemento().equals(elemento)){
                    return posAct;
                }
            }
        }
        
        return null;
    }
    
// <editor-fold defaultstate="collapsed" desc="Getters && Setters">  

    public int getTurnoActual() {
        return this.ordenTurnos[this.turnoActual];
    }

    public ArrayList<Jugador> getPerdedores() {
        return perdedores;
    }

    public int getNumAccion() {
        return numAccion;
    }

    public int getDireccion() {
        return direccion;
    }

    public int getNumDespliegue() {
        return numDespliegue;
    }
    
    public Posicion getPosicion(int fila, int columna){
        try{
            return posiciones[fila][columna];
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println("--- SE HA PRODUCIDO UN EXCEPCION ---");
            String msg = "Fuera de los límites del tablero.";
            Logger.getLogger(Tablero.class.getName()).log(Level.SEVERE, msg, e);
            return null;
        }
    }

    public Jugador getJugador(int i) {
        return jugadores.get(i);
    }
    
    public Jugador getJugadorActual() {
        return jugadores.get(this.getTurnoActual());
    }

    public Trampa getTrampaActivada() {
        return trampaActivada;
    }
    
    public void setNumAccion(int accion) {
        this.numAccion = accion;
    }

    public void setDireccion(int direccion) {
        this.direccion = direccion;
    }

    public void setNumDespliegue(int despliegue) {
        this.numDespliegue = despliegue;
    }

    public void setTurnoActual(int turnoActual) {
        this.turnoActual = turnoActual;
    }

    public void setTrampaActivada(Trampa trampaActivada) {
        this.trampaActivada = trampaActivada;
    }

    public boolean isEnEquipos() {
        return enEquipos;
    }

    public boolean isTorneo() {
        return torneo;
    }
    
// </editor-fold>
    
}