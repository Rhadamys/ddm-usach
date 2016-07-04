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
    private Posicion posReemplazo;
    private Criatura criaturaAReemplazar;
    
    public Trampa(int numTrampa, int dueno){
        this.numTrampa = numTrampa;
        this.dueno = dueno;
        
        switch(numTrampa){
            case 1: this.nombre = "Trampa de osos";
                    this.descripcion = "<html><p align =\"jutify\">Inmoviliza durante 3 turnos a la criatura enemiga que caiga en esta trampa.</p></html>";
                    this.nomArchivoImagen = "trampa_1";
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
        accion.modificarCaminoTrampaParaLadrones();
    }
    
    public void renacerDeLosMuertos(Criatura criRes, Jugador duenoCriReemp){   
        duenoCriReemp.quitarDadoDelPuzzle((Criatura) posReemplazo.getElemento());
        duenoCriReemp.devolverDadoAlPuzzle(criRes);
        posReemplazo.setElemento(criRes);
    }

    public int getNumTrampa() {
        return numTrampa;
    }

    public int getCosto() {
        return costo;
    }

    public Posicion getPosicionReemplazo() {
        return posReemplazo;
    }

    public Criatura getCriaturaAReemplazar() {
        return criaturaAReemplazar;
    }

    public void setPosicionReemplazo(Posicion posicionReemplazo) {
        this.posReemplazo = posicionReemplazo;
    }

    public void setCriaturaAReemplazar(Criatura criaturaAReemplazar) {
        this.criaturaAReemplazar = criaturaAReemplazar;
    }
}