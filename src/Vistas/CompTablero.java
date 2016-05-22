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
public class CompTablero extends PanelImagen{
    private CompPosicion[][] posiciones;
    
    public CompTablero(){
        this.setSize(500, 500);
        this.setLayout(new GridLayout(15, 15));
        this.setBorder(null);
    }

    public CompPosicion[][] getPosiciones() {
        return posiciones;
    }

    public void setPosiciones(CompPosicion[][] posiciones) {
        this.posiciones = posiciones;
    }
    
    public void reiniciarCasillas(){
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                posiciones[i][j].setImagenActual(0);
            }
        }
    }
    
    public ArrayList<CompPosicion> getDespliegueCruz(CompPosicion botonActual, int direccion){
        int columna = botonActual.getColumna();
        int fila = botonActual.getFila();
        
        ArrayList<CompPosicion> despliegue = new ArrayList();
        
        try{
            switch(direccion){
                case 0: despliegue.add(this.posiciones[fila + 1][columna]);
                        despliegue.add(this.posiciones[fila - 1][columna]);
                        despliegue.add(this.posiciones[fila - 2][columna]);
                        despliegue.add(this.posiciones[fila][columna - 1]);
                        despliegue.add(this.posiciones[fila][columna + 1]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
                case 1: despliegue.add(this.posiciones[fila][columna - 1]);
                        despliegue.add(this.posiciones[fila][columna - 2]);
                        despliegue.add(this.posiciones[fila][columna + 1]);
                        despliegue.add(this.posiciones[fila + 1][columna]);
                        despliegue.add(this.posiciones[fila - 1][columna]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
                case 2: despliegue.add(this.posiciones[fila - 1][columna]);
                        despliegue.add(this.posiciones[fila + 1][columna]);
                        despliegue.add(this.posiciones[fila + 2][columna]);
                        despliegue.add(this.posiciones[fila][columna - 1]);
                        despliegue.add(this.posiciones[fila][columna + 1]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
                case 3: despliegue.add(this.posiciones[fila][columna - 1]);
                        despliegue.add(this.posiciones[fila][columna + 1]);
                        despliegue.add(this.posiciones[fila][columna + 2]);
                        despliegue.add(this.posiciones[fila - 1][columna]);
                        despliegue.add(this.posiciones[fila + 1][columna]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
            }
        }catch(Exception e){
            return null;
        }
        
        return despliegue;
    }
    
    public ArrayList<CompPosicion> getDespliegueEscalera(CompPosicion botonActual, int direccion){
        int columna = botonActual.getColumna();
        int fila = botonActual.getFila();
        
        ArrayList<CompPosicion> despliegue = new ArrayList();
        
        try{
            switch(direccion){
                case 0: despliegue.add(this.posiciones[fila - 1][columna + 1]);
                        despliegue.add(this.posiciones[fila][columna + 1]);
                        despliegue.add(this.posiciones[fila + 1][columna]);
                        despliegue.add(this.posiciones[fila + 1][columna - 1]);
                        despliegue.add(this.posiciones[fila + 2][columna - 1]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
                case 1: despliegue.add(this.posiciones[fila + 1][columna + 1]);
                        despliegue.add(this.posiciones[fila + 1][columna]);
                        despliegue.add(this.posiciones[fila][columna - 1]);
                        despliegue.add(this.posiciones[fila - 1][columna - 1]);
                        despliegue.add(this.posiciones[fila - 1][columna - 2]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
                case 2: despliegue.add(this.posiciones[fila + 1][columna + 1]);
                        despliegue.add(this.posiciones[fila][columna + 1]);
                        despliegue.add(this.posiciones[fila - 1][columna]);
                        despliegue.add(this.posiciones[fila - 1][columna - 1]);
                        despliegue.add(this.posiciones[fila - 2][columna - 1]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
                case 3: despliegue.add(this.posiciones[fila + 1][columna - 1]);
                        despliegue.add(this.posiciones[fila + 1][columna]);
                        despliegue.add(this.posiciones[fila][columna + 1]);
                        despliegue.add(this.posiciones[fila - 1][columna + 1]);
                        despliegue.add(this.posiciones[fila - 1][columna + 2]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
            }
        }catch(Exception e){
            return null;
        }
        
        return despliegue;
    }
    
    public ArrayList<CompPosicion> getDespliegueT(CompPosicion botonActual, int direccion){
        int columna = botonActual.getColumna();
        int fila = botonActual.getFila();
        
        ArrayList<CompPosicion> despliegue = new ArrayList();
        
        try{
            switch(direccion){
                case 0: despliegue.add(this.posiciones[fila - 3][columna]);
                        despliegue.add(this.posiciones[fila - 1][columna]);
                        despliegue.add(this.posiciones[fila - 2][columna]);
                        despliegue.add(this.posiciones[fila][columna - 1]);
                        despliegue.add(this.posiciones[fila][columna + 1]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
                case 1: despliegue.add(this.posiciones[fila][columna - 1]);
                        despliegue.add(this.posiciones[fila][columna - 2]);
                        despliegue.add(this.posiciones[fila][columna - 3]);
                        despliegue.add(this.posiciones[fila + 1][columna]);
                        despliegue.add(this.posiciones[fila - 1][columna]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
                case 2: despliegue.add(this.posiciones[fila + 3][columna]);
                        despliegue.add(this.posiciones[fila + 1][columna]);
                        despliegue.add(this.posiciones[fila + 2][columna]);
                        despliegue.add(this.posiciones[fila][columna - 1]);
                        despliegue.add(this.posiciones[fila][columna + 1]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
                case 3: despliegue.add(this.posiciones[fila][columna + 3]);
                        despliegue.add(this.posiciones[fila][columna + 1]);
                        despliegue.add(this.posiciones[fila][columna + 2]);
                        despliegue.add(this.posiciones[fila - 1][columna]);
                        despliegue.add(this.posiciones[fila + 1][columna]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
            }
        }catch(Exception e){
            return null;
        }
        
        return despliegue;
    }
    
    public ArrayList<CompPosicion> getDespliegueS(CompPosicion botonActual, int direccion){
        int columna = botonActual.getColumna();
        int fila = botonActual.getFila();
        
        ArrayList<CompPosicion> despliegue = new ArrayList();
        
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
    
    public ArrayList<CompPosicion> getDespliegue4(CompPosicion botonActual, int direccion){
        int columna = botonActual.getColumna();
        int fila = botonActual.getFila();
        
        ArrayList<CompPosicion> despliegue = new ArrayList();
        
        try{
            switch(direccion){
                case 0: despliegue.add(this.posiciones[fila - 1][columna]);
                        despliegue.add(this.posiciones[fila - 2][columna]);
                        despliegue.add(this.posiciones[fila][columna - 1]);
                        despliegue.add(this.posiciones[fila + 1][columna - 1]);
                        despliegue.add(this.posiciones[fila + 2][columna - 1]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
                case 1: despliegue.add(this.posiciones[fila][columna + 1]);
                        despliegue.add(this.posiciones[fila][columna + 2]);
                        despliegue.add(this.posiciones[fila - 1][columna]);
                        despliegue.add(this.posiciones[fila - 1][columna - 1]);
                        despliegue.add(this.posiciones[fila - 1][columna - 2]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
                case 2: despliegue.add(this.posiciones[fila - 1][columna]);
                        despliegue.add(this.posiciones[fila - 2][columna]);
                        despliegue.add(this.posiciones[fila][columna + 1]);
                        despliegue.add(this.posiciones[fila + 1][columna + 1]);
                        despliegue.add(this.posiciones[fila + 2][columna + 1]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
                case 3: despliegue.add(this.posiciones[fila][columna + 1]);
                        despliegue.add(this.posiciones[fila][columna + 2]);
                        despliegue.add(this.posiciones[fila + 1][columna]);
                        despliegue.add(this.posiciones[fila + 1][columna - 1]);
                        despliegue.add(this.posiciones[fila + 1][columna - 2]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
            }
        }catch(Exception e){
            return null;
        }
        
        return despliegue;
    }
    
    public ArrayList<CompPosicion> getDespliegueR(CompPosicion botonActual, int direccion){
        int columna = botonActual.getColumna();
        int fila = botonActual.getFila();
        
        ArrayList<CompPosicion> despliegue = new ArrayList();
        try{
            switch(direccion){
                case 0: despliegue.add(this.posiciones[fila - 1][columna]);
                        despliegue.add(this.posiciones[fila][columna + 1]);
                        despliegue.add(this.posiciones[fila][columna - 1]);
                        despliegue.add(this.posiciones[fila + 1][columna - 1]);
                        despliegue.add(this.posiciones[fila + 1][columna - 2]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
                case 1: despliegue.add(this.posiciones[fila - 1][columna]);
                        despliegue.add(this.posiciones[fila][columna - 1]);
                        despliegue.add(this.posiciones[fila + 1][columna]);
                        despliegue.add(this.posiciones[fila + 1][columna + 1]);
                        despliegue.add(this.posiciones[fila + 2][columna + 1]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
                case 2: despliegue.add(this.posiciones[fila + 1][columna]);
                        despliegue.add(this.posiciones[fila][columna + 1]);
                        despliegue.add(this.posiciones[fila][columna - 1]);
                        despliegue.add(this.posiciones[fila - 1][columna - 1]);
                        despliegue.add(this.posiciones[fila - 1][columna - 2]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
                case 3: despliegue.add(this.posiciones[fila - 1][columna]);
                        despliegue.add(this.posiciones[fila][columna + 1]);
                        despliegue.add(this.posiciones[fila + 1][columna]);
                        despliegue.add(this.posiciones[fila + 1][columna - 1]);
                        despliegue.add(this.posiciones[fila + 2][columna - 1]);
                        despliegue.add(this.posiciones[fila][columna]);
                        break;
            }
        }catch(Exception e){
            return null;
        }
    
        return despliegue;
    }
}
