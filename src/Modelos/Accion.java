/***********************************************************************
 * Module:  Accion.java
 * Author:  mam28
 * Purpose: Defines the Class Accion
 ***********************************************************************/
package Modelos;

import Otros.Constantes;
import Otros.Registro;
import Otros.Reproductor;
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
    private final ArrayList<HashMap<String, String>> infoMagias;

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
        
        this.infoMagias = new ArrayList();
        HashMap<String, String> lluviaTorrencial = new HashMap();
        lluviaTorrencial.put("Nombre", "Lluvia torrencial");
        lluviaTorrencial.put("NombreArchivoImagen", "magia_1");
        lluviaTorrencial.put("Descripcion", "<html><p align=\"justify\">Hace que las criaturas enemigas gasten dos unidades de movimiento por cada cuadro que deseen desplazarse durante los próximos 3 turnos del juego.</p></html>");
        lluviaTorrencial.put("Costo", "10");
        infoMagias.add(lluviaTorrencial);
        
        HashMap<String, String> hierbasVenenosas = new HashMap();
        hierbasVenenosas.put("Nombre", "Hierbas venenosas");
        hierbasVenenosas.put("NombreArchivoImagen", "magia_2");
        hierbasVenenosas.put("Descripcion", "<html><p align=\"justify\">El jugador puede seleccionar a 3 criaturas oponentes, durante los próximos 3 turnos estas criaturas recibirán un daño igual al 20% de la vida máxima que estas posean.</p></html>");
        hierbasVenenosas.put("Costo", "15");
        infoMagias.add(hierbasVenenosas);
        
        HashMap<String, String> meteoritosDeFuego = new HashMap();
        meteoritosDeFuego.put("Nombre", "Meteoritos de fuego");
        meteoritosDeFuego.put("NombreArchivoImagen", "magia_3");
        meteoritosDeFuego.put("Descripcion", "<html><p align=\"justify\">El jugador selecciona un lugar del terreno, dentro de un radio de 5 cuadros del terreno, cualquier criatura enemiga que esté ubicada en esta sección recibirá un daño de 30% de la vida máxima que posea. Este efecto dura 3 turnos.</p></html>");
        meteoritosDeFuego.put("Costo", "30");
        infoMagias.add(meteoritosDeFuego);
    }
    
    public void invocarCriatura(Posicion posicion, ArrayList<Dado> dados){
        posicion.setElemento(criaturaAInvocar);
        
        // Si está activada la magia "Lluvia torrencial"
        if(this.magias.get(0)[1] != 0 &&
                this.magias.get(0)[2] != criaturaAInvocar.getDueno()){
            criaturaAInvocar.setCostoMovimiento(2);
        }
        
        for(Dado dado: dados){
            if(criaturaAInvocar.equals(dado.getCriatura())){
                dado.setParaLanzar(false);
                break;
            }
        }
        
        Reproductor.reproducirEfecto(Constantes.E_INVOCACION);
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
        
        if(posAct.getElemento() instanceof Trampa &&
                (((Trampa) posAct.getElemento()).getNumTrampa() == 3 && posAct.getElemento().getDueno() != this.criaturaAMover.getDueno() ||
                ((Trampa) posAct.getElemento()).getNumTrampa() != 3 && posAct.getElemento().getDueno() == this.criaturaAMover.getDueno())){
            posAct.respaldarTrampa();
        }
        posAct.setElemento(this.criaturaAMover);
        
        return elemento;
    }
    
    public void colocarTrampa(Posicion posicion, Jugador jugador){
        posicion.setElemento(trampaAColocar);
        jugador.getTurno().descontarPuntosTrampa(trampaAColocar.getCosto());
        jugador.eliminarTrampa(trampaAColocar);
        
        Registro.registrarAccion(Registro.COLOCAR_TRAMPA,
                jugador.getNombreJugador() + ";" +
                trampaAColocar.getNombre());
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
    
    public void activarMagia(int numMagia, Jugador quienActiva){
        this.magias.get(numMagia - 1)[1] = 3;
        this.magias.get(numMagia - 1)[2] = quienActiva.getNumJug();
        
        Registro.registrarAccion(Registro.ACTIVAR_MAGIA,
                quienActiva.getNombreJugador() + ";" +
                this.getInfoMagia(numMagia).get("Nombre"));
    }
    
    public void desactivarMagia(int numMagia, Tablero tablero){
        this.magias.get(numMagia - 1)[2] = 0;
        
        switch (numMagia) {
            case 1:
                for(int i = 0; i < 15; i++){
                    for(int j = 0; j < 15; j++){
                        Posicion posAct = tablero.getPosicion(i, j);
                        if(posAct.getElemento() instanceof Criatura){
                            ((Criatura) posAct.getElemento()).setCostoMovimiento(1);
                        }
                    }
                }
                break;
            case 2:
                this.criaturasAfectadas.clear();
                break;
            case 3:
                this.areaDeEfecto.clear();
                break;
            default:
                break;
        }
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
    
    public Posicion getPosicionSiguiente(){
        try{
            return this.posicionesMovimiento.get(pasoActualMovimiento + 1);
        }catch(Exception e){
            return null;
        }
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

    public Criatura getCriaturaAInvocar() {
        return criaturaAInvocar;
    }

    public ArrayList<HashMap<String, String>> getInfoMagias() {
        return infoMagias;
    }
    
    public HashMap<String, String> getInfoMagia(int numMagia){
        return this.infoMagias.get(numMagia - 1);
    }
}