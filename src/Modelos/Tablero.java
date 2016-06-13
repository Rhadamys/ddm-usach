/***********************************************************************
 * Module:  Tablero.java
 * Author:  mam28
 * Purpose: Defines the Class Tablero
 ***********************************************************************/
package Modelos;

import java.util.ArrayList;

public class Tablero {
    private final Posicion[][] posiciones;
    private ArrayList<Jugador> jugadores;
    private ArrayList<Jugador> perdedores;
    private int turnoActual;
    private int numAccion;
    private int direccion;
    private int numDespliegue;
    private Trampa trampaActivada;
    
    public Tablero(){
        this.posiciones = new Posicion[15][15];
        this.turnoActual = 0;
        this.numAccion = 0;
        
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                posiciones[i][j] = new Posicion(i, j);
            }
        }
        
        this.perdedores = new ArrayList();        
    }
    
    /**
     * Aplica las magias que se hayan activado.
     * @param accion Accion para activar las magias.
     */
    public void aplicarMagias(Accion accion){
        // Las magias son un Array de enteros
        // [ <Numero de magia>, <Turnos restantes>, <Numero del jugador que la activó> ]
        
        ArrayList<int[]> magiasActivadas = accion.getMagiasActivadas();
        
        if(!magiasActivadas.isEmpty()){
            for(int[] magia: magiasActivadas){
                switch(magia[0]){
                    case 1: accion.lluviaTorrencial(magia[2], this);
                            break;
                    case 2: accion.hierbasVenenosas(magia[2]);
                            break;
                    default:    accion.meteoritosDeFuego(magia[2]);
                                break;
                }

                if(magia[0] == 1){
                    if(this.turnoActual + 1 == magia[2]){
                        magia[1]--;
                    }
                }else{
                    magia[1]--;
                }

                if(magia[1] == 0){
                    accion.desactivarMagia(magia[0]);
                }
            }
        }
    }
    
    /**
     * Cambiar el número del turno.
     */
    public void cambiarTurno(){
        do{
            this.turnoActual++;
        }while(this.getJugadorActual().getJefeDeTerreno().getVida() <= 0);
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
     * @param jugador Jugador que está intentando invocar.
     * @return Verdadero o falso indicando si está conectado al terreno.
     */
    public boolean estaConectadoAlTerreno(int[][] idxCasillas, int jugador){
        for(int[] coord: idxCasillas){
            for(int[] vecino: getIdxVecinos(this.getPosicion(coord[0], coord[1]))){
                try{
                    if(this.posiciones[vecino[0]][vecino[1]].getDueno() == jugador){
                       return true;
                    }
                }catch(Exception e){
                    // Nada
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
                // Nada
            }
        }
        
        return true;
    }
    
    /**
     * Asigna una casilla a un jugador y la agrega a su terreno.
     * @param idxCasilla
     * @param jugador 
     */
    public void asignarCasilla(int[] idxCasilla, int jugador){
        this.posiciones[idxCasilla[0]][idxCasilla[1]].setDueno(jugador);
        this.getJugadorActual().getTerreno().agregarCasilla(this.posiciones[idxCasilla[0]][idxCasilla[1]]);
    }
    
    public void agregarPerdedor(int numJug){
        this.perdedores.add(this.jugadores.get(numJug));
    }

    public int getTurnoActual() {
        return this.turnoActual % this.jugadores.size();
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
        }catch(Exception e){
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
    
    public int cantidadJugadores(){
        int numJugadores = 0;
        for(Jugador jugador: this.jugadores){
            if(jugador.getJefeDeTerreno().getVida() > 0){
                numJugadores++;
            }
        }
        return numJugadores;
    }

    public void setJugadores(ArrayList<Jugador> jugadores) {
        this.jugadores = jugadores;
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
    
}