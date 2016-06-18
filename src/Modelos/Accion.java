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
    private ArrayList<Posicion> posicionesMovimiento;
    private int pasoActualMovimiento;
    private Trampa trampaAColocar;
    private final ArrayList<Criatura> criaturasAfectadas;
    private ArrayList<Posicion> areaDeEfecto;
    private final ArrayList<int[]> magias;

    public Accion(){
        this.posicionesMovimiento = new ArrayList();
        
        // Las magias son un Array de enteros
        // [ <Numero de magia>, <Turnos restantes>, <Numero del jugador que la activó> , <Costo de la trampa> ]
        this.magias = new ArrayList();
        int[] costos = {10, 15, 30};
        for(int i = 1; i <= 3; i++){
            int[] magia = {i, 0, 0, costos[i - 1]};
            magias.add(magia);
        }
        
        this.areaDeEfecto = new ArrayList();
        this.criaturasAfectadas = new ArrayList();
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
    
    public ElementoEnCampo moverCriatura(int pasoActual){
        this.pasoActualMovimiento = pasoActual;
        
        Posicion posAnt = this.posicionesMovimiento.get(pasoActual - 1);
        Posicion posAct = this.posicionesMovimiento.get(pasoActual);
        ElementoEnCampo elemento = posAct.getElemento();
        
        if(posAnt.trampaRespaldada()){
            posAnt.devolverTrampa();
        }else{
            posAnt.setElemento(null);
        }
        
        if(posAct.getElemento() instanceof Trampa){
            posAct.respaldarTrampa();
        }
        posAct.setElemento(this.criaturaAMover);
        
        return elemento;
    }
    
    public void colocarTrampa(Posicion posicion, Jugador jugador){
        posicion.setElemento(trampaAColocar);
        jugador.getTurno().descontarPuntosTrampa(trampaAColocar.getCosto());
        jugador.eliminarTrampa(trampaAColocar);
    }
    
    public int atacarEnemigo(ElementoEnCampo elementoAtacado){
        if(elementoAtacado instanceof JefeDeTerreno){
            ((JefeDeTerreno) elementoAtacado).restarVida(this.criaturaAtacante.getAtaque());
            return ((JefeDeTerreno) elementoAtacado).getVida();
        }else{
            int puntosAtaque = this.criaturaAtacante.getAtaque();
            int puntosDefensaEnemigo = ((Criatura) elementoAtacado).getDefensa();
            
            if(puntosDefensaEnemigo < puntosAtaque){
                ((Criatura) elementoAtacado).restarVida(puntosAtaque - puntosDefensaEnemigo);
            }else{
                criaturaAtacante.restarVida(puntosDefensaEnemigo - puntosAtaque);
            }
            return ((Criatura) elementoAtacado).getVida();
        }
    }
    
    public void lluviaTorrencial(int quienActivo, Tablero tablero){
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                Posicion posAct = tablero.getPosicion(i, j);
                if(posAct.getElemento() instanceof Criatura &&
                   posAct.getElemento().getDueno() != quienActivo){
                    ((Criatura) posAct.getElemento()).setCostoMovimiento(2);
                    ((Criatura) posAct.getElemento()).setTurnosCostoMovInc(3);
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
            if(posicion.getElemento() != null &&
               posicion.getElemento() instanceof Criatura &&
               posicion.getElemento().getDueno() != quienActivo){
                ((Criatura) posicion.getElemento()).restarVida(((Criatura) posicion.getElemento()).getVidaMaxima() * 30 / 100);
            }
        }
    }
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con magias">  
    
    public ArrayList<int[]> getMagias(){
        return this.magias;
    }
    
    /**
     * Devuelve las magias disponibles.
     * @return Magias.
     */
    public ArrayList<int[]> getMagiasDisponibles(){
        ArrayList<int[]> magiasDisponibles = new ArrayList();
        for(int[] magia: magias){
            if(magia[1] == 0){
                magiasDisponibles.add(magia);
            }
        }
        return magiasDisponibles;
    }
    
    public ArrayList<int[]> getMagiasActivadas(){
        ArrayList<int[]> magiasActivadas = new ArrayList();
        for(int[] magia: magias){
            if(magia[2] != 0){
                magiasActivadas.add(magia);
            }
        }
        return magiasActivadas;
    }
    
    public int cantidadMagiasActivadas(){
        return this.getMagiasActivadas().size();
    }
    
    public void activarMagia(int numMagia, int quienActiva){
        this.magias.get(numMagia - 1)[2] = quienActiva;
        this.magias.get(numMagia - 1)[1] = 3;
    }
    
    public void desactivarMagia(int numMagia){
        this.magias.get(numMagia - 1)[2] = 0;
    }
    
    public void agregarCriaturaAfectada(Criatura criatura){
        this.criaturasAfectadas.add(criatura);
    }
    
    public void quitarCriaturaAfectada(Criatura criatura){
        this.criaturasAfectadas.remove(criatura);
    }
    
    public void reiniciarMagia2(){
        this.criaturasAfectadas.clear();
    }
    
    public int cantidadCriaturasAfectadas(){
        return this.criaturasAfectadas.size();
    }
    
    public void setAreaDeEfecto(ArrayList<Posicion> areaDeEfecto){
        this.areaDeEfecto = areaDeEfecto;
    }
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con el movimiento de criatura">

    public void setCriaturaAInvocar(Criatura criaturaAInvocar) {
        this.criaturaAInvocar = criaturaAInvocar;
    }

    public Criatura getCriaturaAMover() {
        return criaturaAMover;
    }

    public void setCriaturaAMover(Criatura criaturaAMover) {
        this.criaturaAMover = criaturaAMover;
        this.posicionesMovimiento = new ArrayList();
    }
       
    public void agregarPosicionAlCamino(Posicion posicion){
        this.posicionesMovimiento.add(posicion);
    }
    
    public void eliminarPosicionDelCamino(Posicion posicion){
        this.posicionesMovimiento.remove(posicion);
    }

    public int largoDelCamino() {
        return posicionesMovimiento.size();
    }
    
    public boolean caminoContienePosicion(Posicion posicion){
        return this.posicionesMovimiento.contains(posicion);
    }
    
    public Posicion getPosicionActual(){
        return this.posicionesMovimiento.get(pasoActualMovimiento);
    }
    
    public Posicion getPosicionAnterior(){
        return this.posicionesMovimiento.get(pasoActualMovimiento - 1);
    }
    
    public Posicion getUltimaPosicionAgregada(){
        return this.posicionesMovimiento.get(this.posicionesMovimiento.size() - 1);
    }
    
    public ArrayList<Posicion> getPosicionesMovimiento(){
        return this.posicionesMovimiento;
    }
            
// </editor-fold>

    public void setTrampaAColocar(Trampa trampaAColocar) {
        this.trampaAColocar = trampaAColocar;
    }
    
    public void modificarCaminoTrampaParaLadrones(){
        ArrayList<Posicion> nuevoCamino = new ArrayList();
        nuevoCamino.add(this.posicionesMovimiento.get(pasoActualMovimiento));
        nuevoCamino.add(this.posicionesMovimiento.get(pasoActualMovimiento - 1));
        
        this.posicionesMovimiento = nuevoCamino;
    }

    public Criatura getCriaturaAtacante() {
        return criaturaAtacante;
    }

    public void setCriaturaAtacante(Criatura criaturaAtacante) {
        this.criaturaAtacante = criaturaAtacante;
    }
    
}