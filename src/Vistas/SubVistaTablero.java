/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Otros.PanelImagen;
import java.awt.GridLayout;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mam28
 */
public class SubVistaTablero extends PanelImagen{
    private final SubVistaPosicion[][] casillas;
    private SubVistaPosicion casillaActual;
    
    public SubVistaTablero(){
        this.casillas = new SubVistaPosicion[15][15];
        this.setSize(500, 500);
        this.setLayout(new GridLayout(15, 15));
        this.setBorder(null);
        
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                casillas[i][j] = new SubVistaPosicion(i, j, this.getWidth() / 15, this.getHeight() / 15);
                this.add(casillas[i][j]);
            }
        }
    }
    
    /**
     * Actualiza las casillas y muestra su imagen "normal".
     */
    public void actualizarCasillas(){
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                if(!casillas[i][j].isSelected()){
                    casillas[i][j].setImagenActual(0);
                }
            }
        }
    }
    
    /**
     * Reinicia la imagen de las casillas a su imagen "normal" y las deselecciona.
     */    
    public void reiniciarCasillas(){
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                casillas[i][j].deseleccionado();
            }
        }
    }
    
    public SubVistaPosicion getCasilla(int fila, int columna){
        try{
            return casillas[fila][columna];
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println("--- SE HA PRODUCIDO UN EXCEPCION ---");
            String msg = "Fuera de los límites del tablero.";
            Logger.getLogger(SubVistaTablero.class.getName()).log(Level.SEVERE, msg, e);
            return null;
        }
    }

    public SubVistaPosicion getBotonActual() {
        return casillaActual;
    }

    public void setBotonActual(SubVistaPosicion botonActual) {
        this.casillaActual = botonActual;
    }
    
    public void marcarCasilla(int[] idxCasilla, int jugador){
        this.casillas[idxCasilla[0]][idxCasilla[1]].setImagenNormal("/Imagenes/Botones/casilla_j" + jugador + ".png");
    }
}
