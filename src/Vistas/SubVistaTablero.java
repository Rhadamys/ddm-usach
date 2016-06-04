/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Otros.PanelImagen;
import java.awt.GridLayout;
import java.util.ArrayList;

/**
 *
 * @author mam28
 */
public class SubVistaTablero extends PanelImagen{
    private SubVistaPosicion[][] posiciones;
    private SubVistaPosicion botonActual;
    
    public SubVistaTablero(){
        this.posiciones = new SubVistaPosicion[15][15];
        this.setSize(500, 500);
        this.setLayout(new GridLayout(15, 15));
        this.setBorder(null);
    }

    public SubVistaPosicion[][] getPosiciones() {
        return posiciones;
    }    

    public SubVistaPosicion getBotonActual() {
        return botonActual;
    }

    public void setBotonActual(SubVistaPosicion botonActual) {
        this.botonActual = botonActual;
    }
    
    public void setPosiciones(SubVistaPosicion[][] posiciones) {
        this.posiciones = posiciones;
    }
    
    /**
     * Reinicia la imagen de las casillas a su imagen "normal".
     */
    public void reiniciarCasillas(){
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                posiciones[i][j].setImagenActual(0);
            }
        }
    }
    
    /**
     * Devuelve los botones que conforman el despliegue cruz.
     * @param botonActual Botón sobre el que se encuentra el mouse actualmente.
     * @param direccion Dirección del despliegue.
     * @return Despliegue cruz.
     */
    public ArrayList<SubVistaPosicion> getDespliegueCruz(SubVistaPosicion botonActual, int direccion){
        int columna = botonActual.getColumna();
        int fila = botonActual.getFila();
        int dirVer = direccion == 2 ? -1: 1;
        int dirHor = direccion == 3 ? -1: 1;
        
        ArrayList<SubVistaPosicion> despliegue = new ArrayList();
        
        try{
            switch(direccion){
                case 0: 
                case 2: despliegue.add(this.posiciones[fila + dirVer][columna]);
                        despliegue.add(this.posiciones[fila - dirVer][columna]);
                        despliegue.add(this.posiciones[fila - 2 * dirVer][columna]);
                        despliegue.add(this.posiciones[fila][columna + dirHor]);
                        despliegue.add(this.posiciones[fila][columna - dirHor]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
                case 1: 
                case 3: despliegue.add(this.posiciones[fila][columna + dirHor]);
                        despliegue.add(this.posiciones[fila][columna - dirHor]);
                        despliegue.add(this.posiciones[fila][columna - 2 * dirHor]);
                        despliegue.add(this.posiciones[fila + dirVer][columna]);
                        despliegue.add(this.posiciones[fila - dirVer][columna]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
            }
        }catch(Exception e){
            return null;
        }
        
        return despliegue;
    }
    
    /**
     * Devuelve los botones que conforman el despliegue escalera.
     * @param botonActual Botón sobre el que se encuentra el mouse actualmente.
     * @param direccion Dirección del despliegue.
     * @return Despliegue escalera.
     */
    public ArrayList<SubVistaPosicion> getDespliegueEscalera(SubVistaPosicion botonActual, int direccion){
        int columna = botonActual.getColumna();
        int fila = botonActual.getFila();
        int dirVer = direccion == 2 ? -1: 1;
        int dirHor = direccion == 3 ? -1: 1;
        
        ArrayList<SubVistaPosicion> despliegue = new ArrayList();
        
        try{
            switch(direccion){
                case 0: 
                case 2: despliegue.add(this.posiciones[fila + dirVer][columna - dirHor]);
                        despliegue.add(this.posiciones[fila][columna - dirHor]);
                        despliegue.add(this.posiciones[fila - dirVer][columna]);
                        despliegue.add(this.posiciones[fila - dirVer][columna + dirHor]);
                        despliegue.add(this.posiciones[fila - 2 * dirVer][columna + dirHor]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
                case 1: 
                case 3: despliegue.add(this.posiciones[fila + dirVer][columna + dirHor]);
                        despliegue.add(this.posiciones[fila + dirVer][columna]);
                        despliegue.add(this.posiciones[fila][columna - dirHor]);
                        despliegue.add(this.posiciones[fila - dirVer][columna - dirHor]);
                        despliegue.add(this.posiciones[fila - dirVer][columna - 2 * dirHor]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
            }
        }catch(Exception e){
            return null;
        }
        
        return despliegue;
    }
    
    /**
     * Devuelve los botones que conforman el despliegue T.
     * @param botonActual Botón sobre el que se encuentra el mouse actualmente.
     * @param direccion Dirección del despliegue.
     * @return Despliegue T.
     */
    public ArrayList<SubVistaPosicion> getDespliegueT(SubVistaPosicion botonActual, int direccion){
        int columna = botonActual.getColumna();
        int fila = botonActual.getFila();
        int dirVer = direccion == 2 ? -1: 1;
        int dirHor = direccion == 3 ? -1: 1;
        
        ArrayList<SubVistaPosicion> despliegue = new ArrayList();
        
        try{
            switch(direccion){
                case 0: 
                case 2: despliegue.add(this.posiciones[fila - 3 * dirVer][columna]);
                        despliegue.add(this.posiciones[fila - dirVer][columna]);
                        despliegue.add(this.posiciones[fila - 2 * dirVer][columna]);
                        despliegue.add(this.posiciones[fila][columna - dirHor]);
                        despliegue.add(this.posiciones[fila][columna + dirHor]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
                case 1: 
                case 3: despliegue.add(this.posiciones[fila][columna - dirHor]);
                        despliegue.add(this.posiciones[fila][columna - 2 * dirHor]);
                        despliegue.add(this.posiciones[fila][columna - 3 * dirHor]);
                        despliegue.add(this.posiciones[fila + dirVer][columna]);
                        despliegue.add(this.posiciones[fila - dirVer][columna]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
            }
        }catch(Exception e){
            return null;
        }
        
        return despliegue;
    }
    
    /**
     * Devuelve los botones que conforman el despliegue S.
     * @param botonActual Botón sobre el que se encuentra el mouse actualmente.
     * @param direccion Dirección del despliegue.
     * @return Despliegue S.
     */
    public ArrayList<SubVistaPosicion> getDespliegueS(SubVistaPosicion botonActual, int direccion){
        int columna = botonActual.getColumna();
        int fila = botonActual.getFila();
        int dirVer = direccion == 2 ? -1: 1;
        int dirHor = direccion == 3 ? -1: 1;
        
        ArrayList<SubVistaPosicion> despliegue = new ArrayList();
        
        try{
            switch(direccion){
                case 0: despliegue.add(this.posiciones[fila][columna - 1]);
                        despliegue.add(this.posiciones[fila][columna - 2]);
                        despliegue.add(this.posiciones[fila + 1][columna - 2]);
                        despliegue.add(this.posiciones[fila][columna + 1]);
                        despliegue.add(this.posiciones[fila - 1][columna + 1]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
                case 1: despliegue.add(this.posiciones[fila - 1][columna]);
                        despliegue.add(this.posiciones[fila - 2][columna]);
                        despliegue.add(this.posiciones[fila - 2][columna - 1]);
                        despliegue.add(this.posiciones[fila + 1][columna]);
                        despliegue.add(this.posiciones[fila + 1][columna + 1]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
                case 2: despliegue.add(this.posiciones[fila][columna - 1]);
                        despliegue.add(this.posiciones[fila][columna - 2]);
                        despliegue.add(this.posiciones[fila - 1][columna - 2]);
                        despliegue.add(this.posiciones[fila][columna + 1]);
                        despliegue.add(this.posiciones[fila + 1][columna + 1]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
                case 3: despliegue.add(this.posiciones[fila - 1][columna]);
                        despliegue.add(this.posiciones[fila - 2][columna]);
                        despliegue.add(this.posiciones[fila - 2][columna + 1]);
                        despliegue.add(this.posiciones[fila + 1][columna]);
                        despliegue.add(this.posiciones[fila + 1][columna - 1]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
            }
        }catch(Exception e){
            return null;
        }
        
        return despliegue;
    }
    
    /**
     * Devuelve los botones que conforman el despliegue 4.
     * @param botonActual Botón sobre el que se encuentra el mouse actualmente.
     * @param direccion Dirección del despliegue.
     * @return Despliegue 4.
     */
    public ArrayList<SubVistaPosicion> getDespliegue4(SubVistaPosicion botonActual, int direccion){
        int columna = botonActual.getColumna();
        int fila = botonActual.getFila();
        int dirVer = direccion == 2 ? -1: 1;
        int dirHor = direccion == 3 ? -1: 1;
        
        ArrayList<SubVistaPosicion> despliegue = new ArrayList();
        
        try{
            switch(direccion){
                case 0: 
                case 2: despliegue.add(this.posiciones[fila - dirVer][columna]);
                        despliegue.add(this.posiciones[fila - 2 * dirVer][columna]);
                        despliegue.add(this.posiciones[fila][columna - dirHor]);
                        despliegue.add(this.posiciones[fila + dirVer][columna - dirHor]);
                        despliegue.add(this.posiciones[fila + 2 * dirVer][columna - dirHor]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
                case 1: 
                case 3: despliegue.add(this.posiciones[fila][columna + dirHor]);
                        despliegue.add(this.posiciones[fila][columna + 2 * dirHor]);
                        despliegue.add(this.posiciones[fila - dirVer][columna]);
                        despliegue.add(this.posiciones[fila - dirVer][columna - dirHor]);
                        despliegue.add(this.posiciones[fila - dirVer][columna - 2 * dirHor]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
            }
        }catch(Exception e){
            return null;
        }
        
        return despliegue;
    }
    
    /**
     * Devuelve los botones que conforman el despliegue R.
     * @param botonActual Botón sobre el que se encuentra el mouse actualmente.
     * @param direccion Dirección del despliegue.
     * @return Despliegue R.
     */
    public ArrayList<SubVistaPosicion> getDespliegueR(SubVistaPosicion botonActual, int direccion){
        int columna = botonActual.getColumna();
        int fila = botonActual.getFila();
        int dirVer = direccion == 2 ? -1: 1;
        int dirHor = direccion == 3 ? -1: 1;
        
        ArrayList<SubVistaPosicion> despliegue = new ArrayList();
        
        try{
            switch(direccion){
                case 0: 
                case 2: despliegue.add(this.posiciones[fila - dirVer][columna]);
                        despliegue.add(this.posiciones[fila][columna + dirHor]);
                        despliegue.add(this.posiciones[fila][columna - dirHor]);
                        despliegue.add(this.posiciones[fila + dirVer][columna - dirHor]);
                        despliegue.add(this.posiciones[fila + dirVer][columna - 2 * dirHor]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
                case 1: 
                case 3: despliegue.add(this.posiciones[fila - dirVer][columna]);
                        despliegue.add(this.posiciones[fila][columna - dirHor]);
                        despliegue.add(this.posiciones[fila + dirVer][columna]);
                        despliegue.add(this.posiciones[fila + dirVer][columna + dirHor]);
                        despliegue.add(this.posiciones[fila + 2 * dirVer][columna + dirHor]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
            }
        }catch(Exception e){
            return null;
        }
    
        return despliegue;
    }
    
    /**
     * Verifica si el terreno está disponible para invocar.
     * @param despliegue Despliegue de dados.
     * @return Verdadedo o falso dependiendo de si está disponible el terreno.
     */
    public boolean estaDisponible(ArrayList<SubVistaPosicion> despliegue){
        for(SubVistaPosicion casilla: despliegue){
            if(casilla.getDueno() != 0){
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Comprueba que la posición actual del despliegue esté conectada al terreno
     * del jugador.
     * @param despliegue Despliegue de dados.
     * @param jugador Jugador que está intentando invocar.
     * @return Verdadero o falso indicando si está conectado al terreno.
     */
    public boolean estaConectadoAlTerreno(ArrayList<SubVistaPosicion> despliegue, int jugador){
        for(SubVistaPosicion posicion: despliegue){
            int x = posicion.getFila();
            int y = posicion.getColumna();
            
            try{
                if(this.posiciones[x - 1][y].getDueno() == jugador){
                   return true;
                }
            }catch(Exception e){
               // Nada
            }
            
            try{
                if(this.posiciones[x + 1][y].getDueno() == jugador){
                   return true;
                }
            }catch(Exception e){
               // Nada
            }
            
            try{
                if(this.posiciones[x][y - 1].getDueno() == jugador){
                   return true;
                }
            }catch(Exception e){
               // Nada
            }
            
            try{
                if(this.posiciones[x][y + 1].getDueno() == jugador){
                   return true;
                }
            }catch(Exception e){
               // Nada
            }
        }
        
        return false;
    }
}
