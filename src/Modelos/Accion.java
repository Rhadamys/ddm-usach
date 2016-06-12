/***********************************************************************
 * Module:  Accion.java
 * Author:  mam28
 * Purpose: Defines the Class Accion
 ***********************************************************************/
package Modelos;

import java.util.*;

/** @pdOid 3209e44d-1fcb-494a-b232-5edd8db5452f */
public class Accion {
    private Criatura criaturaAInvocar;
    private Criatura criaturaAMover;
    private Criatura criaturaAtacante;
    private final ArrayList<Posicion> posicionesMovimiento;
    private int pasoActualMovimiento;
    private Trampa trampaAColocar;
    private ArrayList<Criatura> criaturasAfectadas;
    private ArrayList<Posicion> areaDeEfecto;

    public Accion(){
        this.posicionesMovimiento = new ArrayList();
    }
    
    public void invocarCriatura(Posicion posicion, ArrayList<Dado> dados){
        posicion.setElemento(criaturaAInvocar);
        
        for(Dado dado: dados){
            if(criaturaAInvocar.equals(dado.getCriatura())){
                dado.setParaLanzar(false);
                break;
            }
        }
    }
    
    public void moverCriatura(){
        Posicion posicionInicial = this.posicionesMovimiento.get(0);
        if(posicionInicial.trampaRespaldada()){
            posicionInicial.devolverTrampa();
        }else{
            posicionInicial.setElemento(null);
        }
        
        Posicion posicionFinal = this.posicionesMovimiento.get(pasoActualMovimiento);
        if(posicionFinal.getElemento() instanceof Trampa &&
           ((Trampa) posicionFinal.getElemento()).getDueno() == this.criaturaAMover.getDueno()){
            posicionFinal.respaldarTrampa();
        }
        
        posicionFinal.setElemento(criaturaAMover);
    }
    
    public void finalizarMovimiento(){
        this.moverCriatura();
        this.posicionesMovimiento.clear();
    }
    
    public void colocarTrampa(Posicion posicion, Jugador jugador){
        posicion.setElemento(trampaAColocar);
        jugador.getTurno().descontarPuntosTrampa(trampaAColocar.getCosto());
        jugador.eliminarTrampa(trampaAColocar);
    }
    
    public void atacarEnemigo(ElementoEnCampo elementoAtacado){
        if(elementoAtacado instanceof JefeDeTerreno){
            ((JefeDeTerreno) elementoAtacado).restarVida(this.criaturaAtacante.getAtaque());
        }else{
            int puntosAtaque = this.criaturaAtacante.getAtaque();
            int puntosDefensaEnemigo = ((Criatura) elementoAtacado).getDefensa();
            
            if(puntosDefensaEnemigo < puntosAtaque){
                ((Criatura) elementoAtacado).restarVida(puntosAtaque - puntosDefensaEnemigo);
            }
        }
    }
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con magias">  
    
    public void lluviaTorrencial(int quienActivo, Tablero tablero){
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                Posicion posAct = tablero.getPosicion(i, j);
                if(posAct.getElemento() instanceof Criatura &&
                   posAct.getElemento().getDueno() != quienActivo){
                    ((Criatura) posAct.getElemento()).setCostoMovimiento(2);
                }
            }
        }
    }
    
    public void desactivarLluviaTorrencial(Tablero tablero){
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                Posicion posAct = tablero.getPosicion(i, j);
                if(posAct.getElemento() instanceof Criatura){
                    ((Criatura) posAct.getElemento()).setCostoMovimiento(1);
                }
            }
        }
    }
    
    public void hierbasVenenosas(int quienActivo){
        for(Criatura criatura: this.criaturasAfectadas){
           criatura.restarVida(criatura.getVidaMaxima() * 20 / 100);
        }
    }
    
    public void meteoritosDeFuego(int quienActivo){
        for(Posicion posicion: this.areaDeEfecto){
            if(!(posicion.getElemento() instanceof Trampa) &&
               posicion.getElemento().getDueno() != quienActivo){
                if(posicion.getElemento() instanceof Criatura){
                    ((Criatura) posicion.getElemento()).restarVida(((Criatura) posicion.getElemento()).getVidaMaxima() * 30 / 100);
                }else{
                    ((JefeDeTerreno) posicion.getElemento()).restarVida(((JefeDeTerreno) posicion.getElemento()).getVidaMaxima() * 30 / 100);
                }
            }
        }
    }
    
    public void setAreaDeEfecto(ArrayList<Posicion> areaDeEfecto){
        this.areaDeEfecto = areaDeEfecto;
    }
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con el movimiento de criatura">  
    
    public ElementoEnCampo siguientePosicion(int posAct){
        this.pasoActualMovimiento = posAct;        
        return this.posicionesMovimiento.get(posAct).getElemento();
    }

    public void setCriaturaAInvocar(Criatura criaturaAInvocar) {
        this.criaturaAInvocar = criaturaAInvocar;
    }

    public Criatura getCriaturaAMover() {
        return criaturaAMover;
    }

    public void setCriaturaAMover(Criatura criaturaAMover) {
        this.criaturaAMover = criaturaAMover;
    }
       
    public void agregarPosicionAlCamino(Posicion posicion){
        this.posicionesMovimiento.add(posicion);
    }
    
    public void eliminarPosicionDelCamino(Posicion posicion){
        this.posicionesMovimiento.remove(posicion);
    }
    
    public Posicion getPosicionCamino(int i){
        return this.posicionesMovimiento.get(i);
    }

    public int largoDelCamino() {
        return posicionesMovimiento.size();
    }
    
    public boolean caminoContienePosicion(Posicion posicion){
        return this.posicionesMovimiento.contains(posicion);
    }
    
    public Posicion getUltimaPosicionAgregada(){
        return this.posicionesMovimiento.get(this.posicionesMovimiento.size() - 1);
    }

    public int getPasoActualMovimiento() {
        return pasoActualMovimiento;
    }
            
// </editor-fold>

    public void setTrampaAColocar(Trampa trampaAColocar) {
        this.trampaAColocar = trampaAColocar;
    }

    public void setCriaturaAtacante(Criatura criaturaAtacante) {
        this.criaturaAtacante = criaturaAtacante;
    }
    
}