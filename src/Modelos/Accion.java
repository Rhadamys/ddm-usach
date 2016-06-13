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
    private final ArrayList<int[]> magias;

    public Accion(){
        this.posicionesMovimiento = new ArrayList();
        
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
    
    public int atacarEnemigo(ElementoEnCampo elementoAtacado){
        if(elementoAtacado instanceof JefeDeTerreno){
            ((JefeDeTerreno) elementoAtacado).restarVida(this.criaturaAtacante.getAtaque());
            return ((JefeDeTerreno) elementoAtacado).getVida();
        }else{
            int puntosAtaque = this.criaturaAtacante.getAtaque();
            int puntosDefensaEnemigo = ((Criatura) elementoAtacado).getDefensa();
            
            if(puntosDefensaEnemigo < puntosAtaque){
                ((Criatura) elementoAtacado).restarVida(puntosAtaque - puntosDefensaEnemigo);
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
        if(numMagia == 1){
            this.magias.get(numMagia - 1)[1] = 1;
        }else{
            this.magias.get(numMagia - 1)[1] = 3;
        }
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
    
    public int cantidadCriaturasAfectadas(){
        return this.criaturasAfectadas.size();
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