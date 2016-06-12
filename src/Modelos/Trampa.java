/***********************************************************************
 * Module:  Trampa.java
 * Author:  mam28
 * Purpose: Defines the Class Trampa
 ***********************************************************************/
package Modelos;

import java.util.*;

/** @pdOid 9b3c2866-1ea2-46d3-9d96-37ebdc9439bc */
public class Trampa extends ElementoEnCampo {
    private final int numTrampa;
    private final int costo;
    private Posicion posicionReemplazo;
    private Criatura criaturaAReemplazar;
    
    public Trampa(int numTrampa, int dueno){
        this.numTrampa = numTrampa;
        this.dueno = dueno;
        
        switch(numTrampa){
            case 1: this.nombre = "Trampa de osos";
                    this.descripcion = "<html><p align =\"jutify\">Inmoviliza durante 3 turnos a la criatura enemiga que caiga en esta trampa.</p></html>";
                    this.nomArchivoImagen = "/trampa_1";
                    this.costo = 10;
                    break;
            case 2: this.nombre = "Trampa para ladrones";
                    this.descripcion = "<html><p align =\"jutify\">Inflige daño a la criatura enemiga que caiga en esta trampa y la hace retroceder una casilla.</p></html>";
                    this.nomArchivoImagen = "trampa_2";
                    this.costo = 15;
                    break;
            default:    this.nombre = "Renacer de los muertos";
                        this.descripcion = "<html><p align =\"jutify\">Intercambia una criatura enemiga que caiga en esta trampa por una de tus criaturas que hayan muerto en combate.</p></html>";
                        this.nomArchivoImagen = "trampa_3";
                        this.costo = 30;
                        break;
        }
    }
    
    public void trampaDeOsos(Accion accion){
        accion.getCriaturaAMover().setTurnosInmovilizada(3);
    }
    
    public void trampaParaLadrones(Accion accion){
        accion.getCriaturaAMover().restarVida(accion.getCriaturaAMover().getVida() * 10 / 100);
        
        ArrayList<Posicion> posiciones = new ArrayList();
        int pasoActual = accion.getPasoActualMovimiento();
        posiciones.add(accion.getPosicionCamino(pasoActual));
        posiciones.add(accion.getPosicionCamino(pasoActual - 1));
        
        accion.finalizarMovimiento();
        
        accion.agregarPosicionAlCamino(posiciones.get(0));
        accion.agregarPosicionAlCamino(posiciones.get(1));
    }
    
    public void renacerDeLosMuertos(Criatura criaturaResucitada, Jugador duenoCriaturaReemplazada){        
        for(Dado dado: duenoCriaturaReemplazada.getDados()){
            if(dado.getCriatura().equals(this.criaturaAReemplazar)){
                dado.setParaLanzar(true);
                break;
            }
        }
        
        criaturaResucitada.reiniciar(criaturaResucitada.getDueno());
        posicionReemplazo.setElemento(criaturaResucitada);
    }

    public int getNumTrampa() {
        return numTrampa;
    }

    public int getCosto() {
        return costo;
    }

    public Posicion getPosicionReemplazo() {
        return posicionReemplazo;
    }

    public Criatura getCriaturaAReemplazar() {
        return criaturaAReemplazar;
    }

    public void setPosicionReemplazo(Posicion posicionReemplazo) {
        this.posicionReemplazo = posicionReemplazo;
    }

    public void setCriaturaAReemplazar(Criatura criaturaAReemplazar) {
        this.criaturaAReemplazar = criaturaAReemplazar;
    }
}