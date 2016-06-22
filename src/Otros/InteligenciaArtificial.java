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
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author mam28
 */
public class InteligenciaArtificial {
    private final ControladorBatalla contBat;
    private final PersonajeNoJugable pnj;
    private final Timer timerAcciones;
    
    public InteligenciaArtificial(ControladorBatalla contBat, PersonajeNoJugable pnj){
        this.contBat = contBat;
        this.pnj = pnj;
        this.pnj.setNumAccion(0);
        
        this.timerAcciones = new Timer();        
        this.timerAcciones.schedule(new TimerTask(){
            @Override
            public void run(){
                switch(pnj.getNumAccion()){
                    case 0: lanzarDados();
                            pnj.setNumAccion(-1);
                            break;
                    case 1: invocarCriatura(pnj.getTurno().getDadosLanzados());
                            pnj.setNumAccion(-1);
                            break;
                    case 100:   contBat.cambiarTurno();
                                this.cancel();
                                timerAcciones.cancel();
                                break;
                    default:    break;
                }
            }
        }, 0, 200);
    }
    
    public void lanzarDados(){        
        ArrayList<Dado> dados = new ArrayList();
        int cantidadDadosDisponibles = this.pnj.cantidadDadosDisponibles();
        int nivelDado = 1;
        for(int i = 0; i < cantidadDadosDisponibles; i++){
            if(dados.size() < 4 && nivelDado < 5){
                if(pnj.getDado(i).getNivel() == nivelDado){
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
                    pnj.setNumAccion(1);
                }else{
                    contBat.acumularPuntos();
                    pnj.setNumAccion(100);
                }
                this.cancel();
                timerEspera.cancel();
            }
        }, 3500, 1);
    }
    
    public void invocarCriatura(ArrayList<Dado> dadosLanzados){
        ArrayList<Criatura> criaturasDisponibles = this.contBat.criaturasQueSePuedenInvocar(dadosLanzados);
        Criatura criaturaAInvocar = criaturasDisponibles.get(0);
        for(int i = 1; i < criaturasDisponibles.size(); i++){
            if(criaturasDisponibles.get(i).getNivel() > criaturaAInvocar.getNivel()){
                criaturaAInvocar = criaturasDisponibles.get(i);
            }
        }
        
        Jugador conMenorVida = this.contBat.getTablero().getJugador(0);
        int numJugMenorVida = 1;
        for(int i = 1; i < this.contBat.getTablero().cantidadJugadores(); i++){
            Jugador jugActual = this.contBat.getTablero().getJugador(i);
            if(!jugActual.equals(this.pnj) &&
               jugActual.getJefeDeTerreno().getVida() < conMenorVida.getJefeDeTerreno().getVida()){
                conMenorVida = jugActual;
                numJugMenorVida = i + 1;
            }
        }
        
        switch(criaturaAInvocar.getNivel()){
            case 1:
            case 2: Posicion posMasCercana = this.obtenerPosicionMasCercana(this.contBat.getTablero().getTurnoActual() + 1);
                    break;
            case 3: break;
            case 4: break;
        }
    }
    
    public Posicion obtenerPosicionMasCercana(int numJug){
        Posicion posMasCercana = null;
        double distancia = 1000;
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                Posicion posAct = this.contBat.getTablero().getPosicion(i, j);
                double distanciaDesdeEstaPos = this.distanciaAlJugador(posAct, numJug);
                if(distanciaDesdeEstaPos < distancia){
                    posMasCercana = posAct;
                }
            }
        }
        
        return posMasCercana;
    }
    
    public double distanciaAlJugador(Posicion posAct, int numJug){
        int[] coord = Constantes.POS_JUG_TAB[numJug];
        return Math.sqrt(Math.pow((double) (coord[0] - posAct.getFila()), 2.0) +
                         Math.pow((double) (coord[1] - posAct.getColumna()), 2.0));
    }
    
    public int direccionJugador(int numJug){
        int direccionJug;
        switch(numJug){
            case 1: direccionJug = 2;
                    break;
            case 2: direccionJug = 0;
                    break;
            case 3: direccionJug = 1;
                    break;
            default: direccionJug = 3;
        }
        return direccionJug;
    }
    
//    public int[][] despliegueDado(Posicion posMasCercana, int direccionJug){
//        Random rnd = new Random();
//        
//        this.contBat.getTablero().setDireccion(direccionJug);
//        this.contBat.getTablero().setNumDespliegue(rnd.nextInt(6));
//        
//                
//    }
}
