/***********************************************************************
 * Module:  Turno.java
 * Author:  mam28
 * Purpose: Defines the Class Turno
 ***********************************************************************/
package Modelos;

import java.util.*;

/** @pdOid 0c090683-057e-4bf3-89eb-8e7d6d254309 */
public class Turno {
    protected int puntosAtaque;
    protected int puntosMagia;
    protected int puntosMovimiento;
    protected int puntosTrampa;
    private int[] resultadoLanzamiento;
    private ArrayList<Dado> dadosLanzados;
    
    public Turno(){
        this.puntosAtaque = 0;
        this.puntosMagia = 0;
        this.puntosMovimiento = 0;
        this.puntosTrampa = 0;
    }
    
    public void lanzarDados(ArrayList<Dado> dados){
        Random rnd = new Random();
        int[] caras = new int[dados.size()];
        
        for(int i = 0; i < dados.size(); i++){
            caras[i] = dados.get(i).getCaras()[rnd.nextInt(5)];
        }
        
        this.resultadoLanzamiento = caras;
        this.dadosLanzados = dados;
    }
    
    public void acumularPuntos(){
        for(int cara: this.resultadoLanzamiento){
            switch(cara){
                case 1: this.puntosAtaque++;
                        break;
                case 3: this.puntosMagia++;
                        break;
                case 4: this.puntosMovimiento++;
                        break;
                case 5: this.puntosTrampa++;
                        break;
            }
        }
    }

    public int[] getResultadoLanzamiento() {
        return resultadoLanzamiento;
    }
    
    public int cantidadCarasInvocacion(){
        int cantidadInvocacion = 0;
        for(int cara: this.getResultadoLanzamiento()){
            if(cara == 2){
                cantidadInvocacion++;
            }
        }
        
        return cantidadInvocacion;
    }

    public int getPuntosAtaque() {
        return puntosAtaque;
    }

    public int getPuntosMagia() {
        return puntosMagia;
    }

    public int getPuntosMovimiento() {
        return puntosMovimiento;
    }

    public int getPuntosTrampa() {
        return puntosTrampa;
    }

    public ArrayList<Dado> getDadosLanzados() {
        return dadosLanzados;
    }
    
    public void descontarPuntoAtaque(){
        this.puntosAtaque--;
    }
    
    public void descontarPuntosMagia(int puntos){
        this.puntosMagia -= puntos;
    }
    
    public void descontarPuntosMovimiento(int puntos){
        this.puntosMovimiento -= puntos;
    }
    
    public void descontarPuntosTrampa(int puntos){
        this.puntosTrampa -= puntos;
    }
}