/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Otros;

import Controladores.ControladorBatalla;
import Modelos.Criatura;
import Modelos.Dado;
import Modelos.JefeDeTerreno;
import Modelos.Jugador;
import Modelos.PersonajeNoJugable;
import Modelos.Posicion;
import Modelos.Terreno;
import Modelos.Trampa;
import java.util.ArrayList;
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
    private int numAccion;

    public InteligenciaArtificial(ControladorBatalla contBat, PersonajeNoJugable pnj){
        this.contBat = contBat;
        this.pnj = pnj;
        
        this.numAccion = 0;
        this.aplicarAccion();
    }

    public void aplicarAccion(){
        this.contBat.getVisBat().deshabilitarBotones();
        switch(numAccion){
            case 0: lanzarDados();
                    break;

            case 1: invocarCriatura(pnj.getTurno().getDadosLanzados());
                    break;

            case 2: this.atacarEnemigo();
                    break;

            case 3: if(this.contBat.sePuedeActivarMagia()){
                
                    }else{
                        this.decidirSiguienteAccion();
                    }
                    break;

            case 4: this.moverCriatura();
                    break;

            case 5: if(this.contBat.sePuedeColocarTrampa()){
                
                    }else{
                        this.decidirSiguienteAccion();
                    }
                    break;
                    
            case 100:   contBat.cambiarTurno();
                        break;
        }
    }

    public void decidirSiguienteAccion(){
        boolean estoyEnPeligro = this.estoyEnPeligro();
        if(this.numJugMenorVida() == -1){
            System.out.println("Finalizó la partida. He ganado!");
        }else{
            if((this.contBat.sePuedeAtacar() && !estoyEnPeligro) ||
                    (estoyEnPeligro && this.estoyAlLadoDelObjetivo(this.posicionEnemigoPeligroso()))){
                numAccion = 2;
            }else if(estoyEnPeligro || (this.contBat.sePuedeMover() &&
                    this.estaConectadoAlTerrenoJugMenorVida()) &&
                    !this.estoyAlLadoDelObjetivo(this.posJugMenorVida())){
                numAccion = 4;
            }else{
                numAccion = 100;
            }
            System.out.println("Número de acción escogido: " + this.numAccion);
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
                }else if(i == cantidadDadosDisponibles){
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
                            contBat.getTablero().cantidadCriaturasInvocadas(pnj.getNumJug()) == 0 ||
                            !estaConectadoAlTerrenoJugMenorVida()){
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
                }else if(contBat.getTablero().cantidadCriaturasInvocadas(pnj.getNumJug()) > 0){
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
            if(criaturasDisponibles.get(i).getNivel() > criaturaAInvocar.getNivel()){
                criaturaAInvocar = criaturasDisponibles.get(i);
            }
        }

        int numJugMenorVida = this.numJugMenorVida();
        this.contBat.getAccion().setCriaturaAInvocar(criaturaAInvocar);

        switch(criaturaAInvocar.getNivel()){
            case 1:
            case 2: Posicion posMasCercana = this.obtenerPosicionMasCercana(numJugMenorVida);
                    Posicion sigPosMasCercana = null;

                    for(int i = 0; i < 6; i++){
                        sigPosMasCercana = null;

                        do{
                            sigPosMasCercana = this.sigPosMasCercana(posMasCercana, this.posJugMenorVida(), false, null);

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
            case 3: break;
            case 4: break;
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
                double distAct = this.distanciaALaPosicion(posAct, this.posJugMenorVida());

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
        this.contBat.comprobarEnemigoSeleccionado(this.contBat.getVisBat().getTablero().getCasilla(
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
    }

    public void moverCriatura(){
        boolean estoyEnPeligro = this.estoyEnPeligro();
        
        Posicion posAcercar = null;
        if(estoyEnPeligro){
            System.out.println("Elijo la posición del enemigo peligroso");
            posAcercar = this.posicionEnemigoPeligroso();
        }else{
            System.out.println("Elijo la posición del jugador con menor vida");
            posAcercar = this.posJugMenorVida();
        }
        
        Posicion posCriMasCercana = null;
        double distancia = 1000;

        // Determinar la criatura aliada más cercana al objetivo
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                Posicion posAct = this.contBat.getTablero().getPosicion(i, j);
                double distanciaPosAct = this.distanciaALaPosicion(posAct, posAcercar);
                
                if(distanciaPosAct < distancia &&
                        posAct.getElemento() instanceof Criatura &&
                        posAct.getElemento().getDueno() == this.pnj.getNumJug() &&
                        !this.chocoConAliado(posAct, posAcercar)){

                    System.out.println("Encontré una nueva posición más cercana");
                    posCriMasCercana = posAct;
                    distancia = distanciaPosAct;
                }
            }
        }
        
        this.contBat.getAccion().setCriaturaAMover((Criatura) posCriMasCercana.getElemento());
        int largoCamino = 0;
        
        ArrayList<Posicion> caminoAlJefe = this.camino(posCriMasCercana, posAcercar, false);
        if(caminoAlJefe != null){
            System.out.println("Llego al jefe directamente");
            this.marcarCamino(caminoAlJefe);
            largoCamino = caminoAlJefe.size();
        }else{
            ArrayList<Posicion> caminoEnemigoCercano = this.caminoEnemigoCercano(posCriMasCercana);
            if(!estoyEnPeligro && caminoEnemigoCercano != null &&
                    3 * this.contBat.getAccion().getCriaturaAMover().getCostoMovimiento() <=
                    this.pnj.getTurno().getPuntosMovimiento()){
                System.out.println("Llego a un enemigo a 3 casillas");
                this.marcarCamino(caminoEnemigoCercano);
                largoCamino = caminoEnemigoCercano.size();
            }else{
                System.out.println("Me acerco al jefe");
                ArrayList<Posicion> acercarAlJefe = this.camino(posCriMasCercana, posAcercar, true);
                this.marcarCamino(acercarAlJefe);
                largoCamino = acercarAlJefe.size();
            }
        }
        
        this.contBat.moverCriatura();
        
        Timer timerEspera = new Timer();
        timerEspera.schedule(new TimerTask(){
            @Override
            public void run(){
                decidirSiguienteAccion();
                this.cancel();
                timerEspera.cancel();
            }
        }, (largoCamino - 1) * 300 + 500, 1);
    }

// <editor-fold defaultstate="collapsed" desc="Otros métodos">

    public int numJugMenorVida(){
        int numJugMenorVida = -1;
        
        for(int i = 0; i < this.contBat.getTablero().cantidadJugadores(true); i++){
            if(i != this.pnj.getNumJug() - 1){
                Jugador jugAct = this.contBat.getTablero().getJugador(i);
                if(!this.contBat.getTablero().estaEnPerdedores(jugAct) && (
                        numJugMenorVida == -1 || jugAct.getJefeDeTerreno().getVida() <
                        this.contBat.getTablero().getJugador(numJugMenorVida - 1).getJefeDeTerreno().getVida())){

                    numJugMenorVida = i + 1;
                }
            }
        }

        return numJugMenorVida;
    }

    public Posicion posJugMenorVida(){
        return this.contBat.getTablero().getJugador(this.numJugMenorVida() - 1).getMiPosicion();
    }
    
    public Posicion obtenerPosicionMasCercana(int numJug){
        Posicion posMasCercana = null;
        double distancia = 1000;
        for(Posicion posAct: this.pnj.getTerreno().getPosiciones()){
            double distanciaDesdeEstaPos = this.distanciaALaPosicion(posAct, this.posJugMenorVida());
            if(distanciaDesdeEstaPos < distancia){
                posMasCercana = posAct;
                distancia = distanciaDesdeEstaPos;
            }
        }

        return posMasCercana;
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
    
    public ArrayList<Posicion> camino(Posicion posCri, Posicion posDest, boolean soloAcercamiento){
        Posicion posAct = posCri;
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
                        distancia = 0;
                        break;
                    }else if(distanciaVecino < distancia &&
                            posVecino.getDueno() != 0 &&
                            (posVecino.getElemento() == null ||
                            posVecino.getElemento() instanceof Trampa) &&
                            !camino.contains(posVecino)){
                        posAct = posVecino;
                        distancia = distanciaVecino;
                    }else if(posVecino.getElemento() instanceof Criatura &&
                            posVecino.getElemento().getDueno() != this.pnj.getNumJug()){
                        return camino;
                    }
                }catch(Exception e){
                    // Nada
                }
            }
            
            if(distancia == 1000){
                if(soloAcercamiento){
                    return camino;
                }else{
                    return null;
                }
            }else if(!llegueAlObjetivo){
                camino.add(posAct);
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
    
    public boolean estaConectadoAlTerrenoJugMenorVida(){
        Terreno terrenoJugMenorVida = this.contBat.getTablero().getJugador(this.numJugMenorVida() - 1).getTerreno();
        ArrayList<Integer> numJugConectados = new ArrayList();
        
        for(Posicion posJug: this.pnj.getTerreno().getPosiciones()){
            for(int[] coord: this.contBat.getTablero().getIdxVecinos(posJug)){
                try{
                    Posicion posAct = this.contBat.getTablero().getPosicion(coord[0], coord[1]);
                    if(terrenoJugMenorVida.contienePosicion(posAct)){
                        return true;
                    }else if(posAct.getDueno() != 0 && posAct.getDueno() != this.pnj.getNumJug() &&
                            !numJugConectados.contains(posAct.getDueno())){
                        numJugConectados.add(posAct.getDueno());
                    }
                }catch(Exception e){
                    // Nada
                }
            }
        }
        
        ArrayList<Integer> numJugConectadosOtroJug = new ArrayList();
        
        for(Posicion posJug: terrenoJugMenorVida.getPosiciones()){
            for(int[] coord: this.contBat.getTablero().getIdxVecinos(posJug)){
                try{
                    Posicion posAct = this.contBat.getTablero().getPosicion(coord[0], coord[1]);
                    if(posAct.getDueno() != 0 && posAct.getDueno() != this.pnj.getNumJug() &&
                            !numJugConectadosOtroJug.contains(posAct.getDueno())){
                        numJugConectadosOtroJug.add(posAct.getDueno());
                    }
                }catch(Exception e){
                    // Nada
                }
            }
        }
        
        for(int jugConectado: numJugConectados){
            if(numJugConectadosOtroJug.contains(jugConectado)){
                return true;
            }
        }
        
        return false;
    }
    
    public boolean estoyEnPeligro(){
        return this.posicionEnemigoPeligroso() != null;
    }
    
    public Posicion posicionEnemigoPeligroso(){
        for(int[] vecino: this.contBat.getTablero().getIdxVecinos(this.pnj.getMiPosicion())){
            try{
                Posicion posVecino = this.contBat.getTablero().getPosicion(vecino[0], vecino[1]);
                if(posVecino.getElemento() instanceof Criatura &&
                        posVecino.getElemento().getDueno() != this.pnj.getNumJug()){
                    return posVecino;
                }
            }catch(Exception e){
                // Nada
            }
        }
        
        return null;
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
    
// </editor-fold>
}
