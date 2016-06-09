/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Otros.PanelImagen;
import java.awt.GridLayout;

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
        
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                posiciones[i][j] = new SubVistaPosicion(i, j);
                this.add(posiciones[i][j]);
            }
        }
    }
    
    /**
     * Reinicia la imagen de las casillas a su imagen "normal".
     */
    public void actualizarCasillas(){
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                if(posiciones[i][j].isSelected()){
                    posiciones[i][j].setImagenActual(3);
                }else{
                    posiciones[i][j].setImagenActual(0);
                }
            }
        }
    }
    
    public void reiniciarCasillas(){
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                posiciones[i][j].setSelected(false);
            }
        }
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
    
    public void marcarCasilla(int[] idxCasilla, int jugador, String imagenCasilla){
        this.posiciones[idxCasilla[0]][idxCasilla[1]].setImagenNormal("/Imagenes/Botones/casilla_j" + jugador + ".png");
        this.posiciones[idxCasilla[0]][idxCasilla[1]].setImagenIconoElemento(imagenCasilla);
    }
}
