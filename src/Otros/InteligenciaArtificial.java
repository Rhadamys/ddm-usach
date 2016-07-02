/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Otros;

import Controladores.ControladorBatalla;
import Modelos.*;
import Vistas.SubVistaMensajePNJ;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author mam28
 */
public final class InteligenciaArtificial {
    private final ControladorBatalla contBat;
    private final PersonajeNoJugable pnj;
    private final Tablero tablero;
    private int numAccion;

    public InteligenciaArtificial(ControladorBatalla contBat, PersonajeNoJugable pnj){
        this.contBat = contBat;
        this.pnj = pnj;
        this.tablero = this.contBat.getTablero();
        
        this.numAccion = 0;
        this.pnj.setNumAtaques(0);
        this.aplicarAccion();
    }

    public void aplicarAccion(){
        this.contBat.finalizarAccion();
        switch(numAccion){
            case 0: lanzarDados();
                    break;

            case 1: invocarCriatura(pnj.getTurno().getDadosLanzados());
                    break;

            case 2: this.atacarEnemigo();
                    break;

            case 3: this.activarMagia();
                    break;

            case 4: this.moverCriatura();
                    break;

            case 5: this.colocarTrampa();
                    break;
                    
            case 100:   contBat.cambiarTurno();
                        break;
        }
    }

    public void decidirSiguienteAccion(){
        boolean estoyEnPeligro = this.pnj.estoyEnPeligro(tablero);
        if(this.pnj.numJugMenorVida(tablero) == -1){
            System.out.println("Finalizó la partida. He ganado!");
        }else{
            if(this.pnj.puedoAtacar() &&
                    ((this.contBat.sePuedeAtacar() && !estoyEnPeligro) ||
                    (estoyEnPeligro && this.estoyAlLadoDelObjetivo(this.pnj.posicionEnemigoPeligroso(tablero))))){
                numAccion = 2;
            }else if(estoyEnPeligro || (this.contBat.sePuedeMover() &&
                    this.pnj.estoyConectadoAlTerrenoJugMenorVida(tablero)) &&
                    !this.estoyAlLadoDelObjetivo(this.pnj.posJugMenorVida(tablero)) &&
                    this.pnj.puedoAtacar()){
                numAccion = 4;
            }else if(this.pnj.getNivel() == 3 &&
                    this.contBat.sePuedeActivarMagia() &&
                    this.pnj.puedoActivarMagia()){
                numAccion = 3;
            }else if(this.pnj.getNivel() > 1 &&
                    this.contBat.sePuedeColocarTrampa() &&
                    this.pnj.puedoPonerTrampa()){
                numAccion = 5;
            }else{
                numAccion = 100;
            }
            System.out.println("Número de acción elegida: " + this.numAccion);
            this.aplicarAccion();
        }
    }

    public void lanzarDados(){
        ArrayList<Dado> dados = new ArrayList();
        int cantidadDadosDisponibles = this.pnj.cantidadDadosDisponibles();
        int nivelDado = 1;
        for(int i = 0; i < cantidadDadosDisponibles; i++){
            if(dados.size() < 4 && nivelDado < 5){
                if(pnj.getDado(i).getNivel() == nivelDado &&
                        pnj.getDado(i).isParaLanzar()){
                    dados.add(pnj.getDado(i));
                    if(dados.size() == 3 && this.pnj.getNivel() > 1){
                        nivelDado++;
                    }
                }else if(i == cantidadDadosDisponibles - 1){
                    nivelDado++;
                    i = 0;
                }
            }else{
                break;
            }
        }

        this.contBat.lanzarDados(dados);

        Timer timerEspera = new Timer();
        timerEspera.schedule(new TimerTask(){
            @Override
            public void run(){
                if(contBat.cantidadCarasInvocacion() > 0){
                    contBat.realizarAcciones();
                    if(!contBat.getTablero().estaConectadoAlTerrenoDeOtros() ||
                            pnj.cantidadCriaturasInvocadas() == 0 ||
                            !pnj.estoyConectadoAlTerrenoJugMenorVida(tablero)){
                        numAccion = 1;
                        aplicarAccion();
                    }else{
                        if(new Random().nextBoolean()){
                            numAccion = 1;
                            aplicarAccion();
                        }else{
                            decidirSiguienteAccion();
                        }
                    }
                }else if(pnj.cantidadCriaturasInvocadas() > 0){
                    contBat.realizarAcciones();
                    decidirSiguienteAccion();
                }else{
                    contBat.acumularPuntos();
                    numAccion = 100;
                    aplicarAccion();
                }
                this.cancel();
                timerEspera.cancel();
            }
        }, 3400, 1);
    }

    public void invocarCriatura(ArrayList<Dado> dadosLanzados){
        ArrayList<Criatura> criaturasDisponibles = this.contBat.criaturasQueSePuedenInvocar(dadosLanzados);
        Criatura criaturaAInvocar = criaturasDisponibles.get(0);
        for(int i = 1; i < criaturasDisponibles.size(); i++){
            Criatura criAct = criaturasDisponibles.get(i);
            if(criAct.getNivel() > criaturaAInvocar.getNivel()){
                criaturaAInvocar = criAct;
            }
        }

        switch(criaturaAInvocar.getNivel()){
            case 1:
            case 2: 
                int numJugMenorVida = this.pnj.numJugMenorVida(tablero);
                this.contBat.getAccion().setCriaturaAInvocar(criaturaAInvocar);
                Posicion posMasCercana = this.getPosMasCercana(numJugMenorVida);
                Posicion sigPosMasCercana = null;

                for(int i = 0; i < 6; i++){
                    sigPosMasCercana = null;

                    do{
                        sigPosMasCercana = this.sigPosMasCercana(posMasCercana, this.pnj.posJugMenorVida(tablero), false, null);

                        if(sigPosMasCercana == null){
                            posMasCercana = this.pnj.getTerreno().getPosicionAnterior(posMasCercana);
                        }

                    }while(sigPosMasCercana == null);

                    int[] coord = {sigPosMasCercana.getFila(), sigPosMasCercana.getColumna()};
                    this.contBat.asignarCasilla(coord, this.pnj.getNumJug());

                    posMasCercana = sigPosMasCercana;

                    try{
                        this.contBat.getVisBat().getTablero().reiniciarCasillas();
                        Reproductor.reproducirEfecto(Constantes.MARCA_CAMINO);
                        Thread.sleep(500);
                    }catch(Exception e){
                        // Nada
                    }
                }

                this.contBat.getAccion().invocarCriatura(sigPosMasCercana, dadosLanzados);

                try{
                    this.contBat.revisarCasillas();
                    Thread.sleep(1000);
                }catch(Exception e){
                    // Nada
                }

                break;
            case 3: 
            case 4: 
                Posicion posCriNivel2MasCercana = this.getPosCriNivel(criaturaAInvocar.getNivel());
                posCriNivel2MasCercana.setElemento(criaturaAInvocar);
                this.contBat.revisarCasillas();
                break;
        }
        
        this.decidirSiguienteAccion();
    }

    public void atacarEnemigo(){
        double distancia = 1000;
        Criatura criaturaAtacante = null;
        Posicion elementoAtacado = null;

        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                Posicion posAct = this.contBat.getTablero().getPosicion(i, j);
                double distAct = this.distanciaALaPosicion(posAct, this.pnj.posJugMenorVida(tablero));

                if(posAct.getElemento() instanceof Criatura &&
                   posAct.getElemento().getDueno() == this.contBat.getTablero().getTurnoActual() + 1 &&
                   distAct < distancia){

                    for(int[] vecino: this.contBat.getTablero().getIdxVecinos(posAct)){
                        try{
                            Posicion posVec = this.contBat.getTablero().getPosicion(vecino[0], vecino[1]);

                            if((posVec.getElemento() instanceof JefeDeTerreno ||
                               posVec.getElemento() instanceof Criatura) &&
                               posVec.getElemento().getDueno() != this.contBat.getTablero().getTurnoActual() + 1){
                                distancia = distAct;
                                criaturaAtacante = (Criatura) posAct.getElemento();
                                elementoAtacado = posVec;

                                if(elementoAtacado.getElemento() instanceof JefeDeTerreno){
                                    break;
                                }
                            }
                        }catch(Exception e){
                            // Nada
                        }
                    }
                }
            }
        }

        this.contBat.getAccion().setCriaturaAtacante(criaturaAtacante);
        this.contBat.comprobarEnemigoSelec(this.contBat.getVisBat().getTablero().getCasilla(
            elementoAtacado.getFila(), elementoAtacado.getColumna()));

        Timer timerEspera = new Timer();
        timerEspera.schedule(new TimerTask(){
            @Override
            public void run(){
                decidirSiguienteAccion();
                this.cancel();
                timerEspera.cancel();
            }
        }, 1500, 1);
        
        this.pnj.setNumAtaques(this.pnj.getNumAtaques() + 1);
    }

    public void moverCriatura(){
        boolean estoyEnPeligro = this.pnj.estoyEnPeligro(tablero);
        
        Posicion posAcercar;
        if(estoyEnPeligro){
            System.out.println("Elijo la posición del enemigo peligroso");
            posAcercar = this.pnj.posicionEnemigoPeligroso(tablero);
        }else{
            System.out.println("Elijo la posición del jugador con menor vida");
            posAcercar = this.pnj.posJugMenorVida(tablero);
        }

        // Determinar la criatura aliada más cercana al objetivo
        Posicion posCriMasCercana = this.getPosCriMasCercana(posAcercar);
        
        this.contBat.getAccion().setCriaturaAMover((Criatura) posCriMasCercana.getElemento());
        
        ArrayList<Posicion> caminoAlJefe = this.camino(posCriMasCercana, posAcercar, false);
        if(caminoAlJefe != null){
            System.out.println("Llego al jefe directamente");
            this.marcarCamino(caminoAlJefe);
        }else{
            ArrayList<Posicion> caminoEnemigoCercano = this.caminoEnemigoCercano(posCriMasCercana);
            if(!estoyEnPeligro && caminoEnemigoCercano != null &&
                    3 * this.contBat.getAccion().getCriaturaAMover().getCostoMovimiento() <=
                    this.pnj.getTurno().getPuntosMovimiento()){
                System.out.println("Llego a un enemigo a 3 casillas");
                this.marcarCamino(caminoEnemigoCercano);
            }else{
                System.out.println("Me acerco al jefe");
                ArrayList<Posicion> acercarAlJefe = this.camino(posCriMasCercana, posAcercar, true);
                this.marcarCamino(acercarAlJefe);
            }
        }
        
        this.contBat.moverCriatura();
        
        Timer timerEspera = new Timer();
        timerEspera.schedule(new TimerTask(){
            int tic = 1;
            boolean seActivaraTrampa = false;
            int numTrampaQueSeActivara;
            
            @Override
            public void run(){
                Posicion posSig = contBat.getAccion().getPosicionSiguiente();
                if(posSig == null){
                    this.cancel();
                    timerEspera.cancel();
                    decidirSiguienteAccion();
                }else if(posSig.getElemento() instanceof Trampa &&
                        (((Trampa) posSig.getElemento()).getNumTrampa() == 3 && posSig.getElemento().getDueno() == pnj.getNumJug() ||
                        ((Trampa) posSig.getElemento()).getNumTrampa() != 3 && posSig.getElemento().getDueno() != pnj.getNumJug())){
                    seActivaraTrampa = true;
                    numTrampaQueSeActivara = ((Trampa) posSig.getElemento()).getNumTrampa();
                }else if(seActivaraTrampa){
                    this.cancel();
                    timerEspera.cancel();
                    
                    switch (numTrampaQueSeActivara) {
                        case 2:
                            Timer timerEspera = new Timer();
                            timerEspera.schedule(new TimerTask(){
                                boolean seActivaraTrampa = false;
                                int numTrampaQueSeActivara;
                                
                                @Override
                                public void run(){
                                    this.cancel();
                                    timerEspera.cancel();
                                    
                                    decidirSigAc();
                                }
                            }, 500, 1);
                            break;
                        case 3:
                            revivirCriatura();
                            break;
                        default:
                            decidirSiguienteAccion();
                            break;
                    }
                }
                tic++;
            }
            
            public void decidirSigAc(){
                decidirSiguienteAccion();
            }
        }, 500, 300);
    }
    
    public void activarMagia(){
        int numMagia = this.pnj.getNumMagia();
        System.out.println("Número de magia a activar: " + numMagia);
        
        ArrayList<int[]> magiasActivadas = this.contBat.getAccion().getMagiasActivadas();
        for(int[] magia: magiasActivadas){
            if(magia[0] == numMagia){
                this.pnj.cambiarNumMagia();
                this.decidirSiguienteAccion();
                return;
            }
        }
        
        if(numMagia == 2 && this.contBat.getTablero().cantidadInvOtrosJugadores() < 3){
            this.pnj.cambiarNumMagia();
            this.decidirSiguienteAccion();
            return;
        }
        
        switch(numMagia){
            case 2: ArrayList<Criatura> enemigos = new ArrayList();
            
                    for(int i = 0; i < 15; i++){
                        for(int j = 0; j < 15; j++){
                            Posicion posAct = this.contBat.getTablero().getPosicion(i, j);
                            if(posAct.getElemento() instanceof Criatura &&
                                    posAct.getElemento().getDueno() != this.pnj.getNumJug()){
                                enemigos.add((Criatura) posAct.getElemento());
                            }
                        }
                    }
                    
                    int i = 0;
                    do{
                        int idx = new Random().nextInt(enemigos.size());
                        this.contBat.getAccion().agregarCriaturaAfectada(enemigos.get(idx));
                        enemigos.remove(idx);
                        i++;
                    }while(i < 3);
                    
                    break;
            case 3:    ArrayList<Posicion> areaDeEfecto = new ArrayList();
                        for(int x = 2; x < 13; x++){
                            for(int y = 2; y < 13; y++){
                                areaDeEfecto.add(this.tablero.getPosicion(x, y));
                            }
                        }
                        
                        this.contBat.getAccion().setAreaDeEfecto(areaDeEfecto);
        }
        
        this.contBat.getAccion().activarMagia(numMagia, this.pnj);
        
        HashMap<String, String> infoMagia = this.contBat.getAccion().getInfoMagia(numMagia);
        
        this.pnj.cambiarNumMagia();
        this.pnj.getTurno().descontarPuntosMagia(Integer.valueOf(infoMagia.get("Costo")));
        
        this.mostrarMensaje("He activado la magia " + infoMagia.get("Nombre"));
        Timer timerEspera = new Timer();
        timerEspera.schedule(new TimerTask(){
            @Override
            public void run(){
                decidirSiguienteAccion();
                this.cancel();
                timerEspera.cancel();
            }
        }, 5000, 1);
    }
    
    public void colocarTrampa(){
        ArrayList<Posicion> posDisponibles = new ArrayList();
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                Posicion posAct = this.contBat.getTablero().getPosicion(i, j);
                if(posAct.getElemento() == null &&
                    posAct.getDueno() != 0){
                        posDisponibles.add(posAct);
                }
            }
        }
        
        Trampa trampaAColocar = this.pnj.getTrampa(this.pnj.getNumTrampa());
        this.contBat.getAccion().setTrampaAColocar(trampaAColocar);
        
        Posicion posicion = posDisponibles.get(new Random().nextInt(posDisponibles.size()));
        this.contBat.getAccion().colocarTrampa(posicion, this.pnj);
        
        this.mostrarMensaje("He colocado la trampa " + trampaAColocar.getNombre() + ". " +
                (trampaAColocar.getNumTrampa() == 3 ?
                        "Hummm... ¿Cuál criatura reviviré?" :
                        "Espero que caigas en ella."));
        
        this.pnj.cambiarNumTrampa();
        
        Timer timerEspera = new Timer();
        timerEspera.schedule(new TimerTask(){
            @Override
            public void run(){
                decidirSiguienteAccion();
                this.cancel();
                timerEspera.cancel();
            }
        }, 5000, 1);
    }

// <editor-fold defaultstate="collapsed" desc="Otros métodos">
    
    public Posicion getPosMasCercana(int numJug){
        Posicion posMasCercana = null;
        double distancia = 1000;
        for(Posicion posAct: this.pnj.getTerreno().getPosiciones()){
            double distanciaDesdeEstaPos = this.distanciaALaPosicion(posAct, this.pnj.posJugMenorVida(tablero));
            if(distanciaDesdeEstaPos < distancia){
                posMasCercana = posAct;
                distancia = distanciaDesdeEstaPos;
            }
        }

        return posMasCercana;
    }
    
    public Posicion getPosCriMasCercana(Posicion posAcercar){
        Posicion posCriMasCercana = null;
        double distancia = 1000;
        
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                Posicion posAct = this.contBat.getTablero().getPosicion(i, j);
                double distanciaPosAct = this.distanciaALaPosicion(posAct, posAcercar);
                
                if(distanciaPosAct < distancia &&
                        posAct.getElemento() instanceof Criatura &&
                        posAct.getElemento().getDueno() == this.pnj.getNumJug() &&
                        !this.chocoConAliado(posAct, posAcercar) &&
                        ((Criatura) posAct.getElemento()).getTurnosInmovilizada() == 0){

                    System.out.println("Encontré una nueva posición más cercana");
                    posCriMasCercana = posAct;
                    distancia = distanciaPosAct;
                }
            }
        }
        
        return posCriMasCercana;
    }

    public double distanciaALaPosicion(Posicion posAct, Posicion posDest){
        return Math.sqrt(Math.pow((double) (posDest.getFila() - posAct.getFila()), 2.0) +
                         Math.pow((double) (posDest.getColumna() - posAct.getColumna()), 2.0));
    }

    public Posicion sigPosMasCercana(Posicion posMasCercana, Posicion posDest, boolean seaTerreno, Posicion posAnt){
        Posicion sigPosMasCercana = null;
        double distancia = 1000;

        for(int[] vecino: this.contBat.getTablero().getIdxVecinos(posMasCercana)){
            try{
                Posicion posVecino = this.contBat.getTablero().getPosicion(vecino[0], vecino[1]);
                double distanciaVecino = this.distanciaALaPosicion(posVecino, posDest);
                if(distanciaVecino < distancia && !posVecino.equals(posAnt)){
                    if(seaTerreno == false && posVecino.getDueno() == 0){
                        distancia = distanciaVecino;
                        sigPosMasCercana = posVecino;
                    }else if(seaTerreno == true && posVecino.getDueno() != 0){
                        if(sigPosMasCercana == null){
                            distancia = distanciaVecino;
                            sigPosMasCercana = posVecino;
                        }else if(sigPosMasCercana.getDueno() == posDest.getDueno() &&
                                posVecino.getDueno() == posDest.getDueno()){
                            distancia = distanciaVecino;
                            sigPosMasCercana = posVecino;
                        }else if(sigPosMasCercana.getDueno() != posDest.getDueno()){
                            distancia = distanciaVecino;
                            sigPosMasCercana = posVecino;
                        }
                    }
                }
            }catch(Exception e){
                // Nada
            }
        }

        return sigPosMasCercana;
    }
    
    public void marcarCamino(ArrayList<Posicion> camino){
        for(Posicion posAct: camino){
            this.contBat.getVisBat().getTablero().getCasilla(posAct.getFila(), posAct.getColumna()).seleccionado();
            this.contBat.getAccion().agregarPosicionAlCamino(posAct);
            Reproductor.reproducirEfecto(Constantes.SELECCION);
            
            try{
                Thread.sleep(500);
            }catch(Exception e){
                // Nada
            }
        }
    }
    
    public ArrayList<Posicion> camino(Posicion posCri, Posicion posDest, boolean soloAcercamiento){
        Posicion posAct = posCri;
        ArrayList<Posicion> camino = new ArrayList();
        ArrayList<Posicion> posRevisadas = new ArrayList();
        boolean llegueAlObjetivo = false;
        boolean choqueConEnemigo = false;
        
        camino.add(posAct);
        
        do{
            double distancia = 1000;
            
            for(int[] vecino: this.contBat.getTablero().getIdxVecinos(posAct)){
                try{
                    Posicion posVecino = this.contBat.getTablero().getPosicion(vecino[0], vecino[1]);
                    double distanciaVecino = this.distanciaALaPosicion(posVecino, posDest);
                    
                    if(posVecino.equals(posDest)){
                        llegueAlObjetivo = true;
                        distancia = 0;
                        break;
                    }else if(distanciaVecino < distancia &&
                            !camino.contains(posVecino) &&
                            !posRevisadas.contains(posVecino)){
                        if(posVecino.getDueno() != 0 &&
                                (posVecino.getElemento() == null ||
                                posVecino.getElemento() instanceof Trampa)){
                            posAct = posVecino;
                            distancia = distanciaVecino;
                        }else if(posVecino.getElemento() instanceof Criatura &&
                                posVecino.getElemento().getDueno() != this.pnj.getNumJug()){
                            choqueConEnemigo = true;
                        }
                    }
                }catch(Exception e){
                    // Nada
                }
            }
            
            if(distancia == 1000){
                if(soloAcercamiento){
                    if(choqueConEnemigo){
                        return camino;
                    }else{
                        camino.clear();
                        posAct = posCri;
                        camino.add(posAct);
                    }
                }else{
                    return null;
                }
            }else if(!llegueAlObjetivo){
                camino.add(posAct);
                posRevisadas.add(posAct);
                if(camino.size() - 1 * this.contBat.getAccion().getCriaturaAMover().getCostoMovimiento() ==
                        this.pnj.getTurno().getPuntosMovimiento()){
                    return camino;
                }else if(camino.size() - 1 * this.contBat.getAccion().getCriaturaAMover().getCostoMovimiento() >
                        this.pnj.getTurno().getPuntosMovimiento()){
                    camino.remove(camino.size() - 1);
                    return camino;
                }
            }
        }while(!llegueAlObjetivo);
        
        return camino;
    }
    
    public ArrayList<Posicion> caminoEnemigoCercano(Posicion posCriMasCercana){
        ArrayList<Posicion> posElemCercanos = new ArrayList();
        double distancia = 1000;
        
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                Posicion posAct = this.contBat.getTablero().getPosicion(i, j);
                double distanciaPosAct = this.distanciaALaPosicion(posCriMasCercana, posAct);
                
                if((posAct.getElemento() instanceof Criatura ||
                        posAct.getElemento() instanceof JefeDeTerreno) &&
                        posAct.getElemento().getDueno() != this.pnj.getNumJug() &&
                        distanciaPosAct <= distancia){
                    
                    posElemCercanos.add(0, posAct);
                    distancia = distanciaPosAct;
                    
                    if(posElemCercanos.size() == 4){
                        posElemCercanos.remove(3);
                    }
                }
            }
        }
        
        for(Posicion posElemCercano: posElemCercanos){
            ArrayList<Posicion> camino = this.camino(posCriMasCercana, posElemCercano, false);
            if(camino != null && camino.size() <= 4){
                return camino;
            }
        }
        
        return null;
    }
    
    public boolean chocoConAliado(Posicion posCriAct, Posicion posDest){
        Posicion posAct = posCriAct;
        ArrayList<Posicion> camino = new ArrayList();
        boolean llegueAlObjetivo = false;
        
        camino.add(posAct);
        
        do{
            double distancia = 1000;
            
            for(int[] vecino: this.contBat.getTablero().getIdxVecinos(posAct)){
                try{
                    Posicion posVecino = this.contBat.getTablero().getPosicion(vecino[0], vecino[1]);
                    double distanciaVecino = this.distanciaALaPosicion(posVecino, posDest);
                    
                    if(posVecino.equals(posDest)){
                        llegueAlObjetivo = true;
                    }else if(distanciaVecino < distancia &&
                            posVecino.getDueno() != 0 &&
                            (posVecino.getElemento() == null ||
                            posVecino.getElemento() instanceof Trampa) &&
                            !camino.contains(posVecino)){
                        posAct = posVecino;
                        distancia = distanciaVecino;
                    }
                }catch(Exception e){
                    // Nada
                }
            }
            
            if(distancia == 1000){
                return posAct.getElemento() instanceof Criatura &&
                        posAct.getElemento().getDueno() == this.pnj.getNumJug();
            }else if(!llegueAlObjetivo){
                camino.add(posAct);
            }
        }while(!llegueAlObjetivo);
        
        return false;
    }
    
    public boolean estoyAlLadoDelObjetivo(Posicion posObj){
        for(int[] vecino: this.contBat.getTablero().getIdxVecinos(posObj)){
            try{
                Posicion posAct = this.contBat.getTablero().getPosicion(vecino[0], vecino[1]);
                if(posAct.getElemento() instanceof Criatura &&
                        posAct.getElemento().getDueno() == this.pnj.getNumJug()){
                    return true;
                }
            }catch(Exception e){
                // Nada
            }
        }
        return false;
    }
    
    public void mostrarMensaje(String mensaje){
        SubVistaMensajePNJ visMen = new SubVistaMensajePNJ(this.pnj, mensaje);
        this.contBat.getContPrin().getContVisPrin().getVisPrin().agregarVista(visMen);
        visMen.setVisible(true);
    }
    
    public void revivirCriatura(){
        Criatura criARevivir = null;
        for(Dado dado: this.pnj.getDados()){
            if(!dado.isParaLanzar() &&
                    dado.getCriatura().getVida() <= 0){
                if(criARevivir == null){
                    criARevivir = dado.getCriatura();
                }else if(dado.getCriatura().getNivel() > criARevivir.getNivel()){
                    criARevivir = dado.getCriatura();
                }
            }
        }
        
        Posicion posCriAReemplazar = this.getPosCriMasCercana(this.pnj.posJugMenorVida(this.contBat.getTablero()));
        this.pnj.quitarDadoDelPuzzle((Criatura) posCriAReemplazar.getElemento());
        posCriAReemplazar.setElemento(criARevivir);
        this.pnj.devolverDadoAlPuzzle(criARevivir);
        
        this.contBat.revisarCasillas();
        
        this.decidirSiguienteAccion();
    }
    
    public Posicion getPosCriNivel(int nivel){
        Posicion posCri = null;
        double distancia = 1000;
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                Posicion posAct = this.contBat.getTablero().getPosicion(i, j);
                double distanciaPosAct = this.distanciaALaPosicion(posAct, this.pnj.posJugMenorVida(tablero));
                if(posAct.getElemento() instanceof Criatura &&
                        posAct.getElemento().getDueno() == this.pnj.getNumJug() &&
                        ((Criatura) posAct.getElemento()).getNivel() == nivel &&
                        distanciaPosAct < distancia){
                    distancia = distanciaPosAct;
                    posCri = posAct;
                }
            }
        }
        
        return posCri;
    }
    
// </editor-fold>
}
