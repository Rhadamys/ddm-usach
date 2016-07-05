/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author mam28
 */
public class Torneo {
    private int partJugadas;
    private final ArrayList<Jugador> jugadores;
    
    public Torneo(ArrayList<Jugador> jugadores, String tipoTorneo){
        this.partJugadas = 0;
        this.jugadores = jugadores;
        
        for(Jugador jug: this.jugadores){
            jug.setPuntaje(0);
        }
    }
    
    public void actEstadisticas(Jugador ganador, ArrayList<Jugador> perdedores){
        partJugadas++;
        
        for(int i = 0; i < perdedores.size(); i++){
            Jugador perdedor = perdedores.get(i);
            perdedor.agregarPuntaje(10 * i);
        }
        
        ganador.agregarPuntaje((jugadores.size() - 1) * 10);
    }
    
    public int restaurarVidaJugador(Jugador ganador){
        int vidaMax = ganador.getJefeDeTerreno().getVidaMaxima();
        int vidaAct = ganador.getJefeDeTerreno().getVida();
        int vidaRest = vidaAct + vidaMax * 20 / 100;
        return vidaRest <= vidaMax ? vidaRest : vidaMax;
    }
    
    public ArrayList<Dado> agregarDadosGanador(){
        Jugador ganador = this.getGanador();
        
        ArrayList<Dado> dados = Dado.getDados();
        ArrayList<Dado> dadosGanados = new ArrayList<Dado>();

        int idx = 0;
        do{
            Jugador jugAct = this.getJugadores().get(idx);
            int nivelMaximo = jugAct instanceof Usuario ? 3: ((PersonajeNoJugable) jugAct).getNivel() + 1;

            Dado dadoGanado;
            do{
                dadoGanado = dados.get(new Random().nextInt(dados.size()));
            }while(dadoGanado.getNivel() > nivelMaximo);

            dadoGanado.setParaJugar(false);
            dadosGanados.add(dadoGanado);
            ganador.agregarDado(dadoGanado);
            dados.remove(dadoGanado);
            idx++;
        }while(idx < this.getJugadores().size());

        PuzzleDeDados.agregarDadosAlPuzzle(ganador.getId(), dadosGanados);
        
        return dadosGanados;
    }
    
    public ArrayList<Jugador> getClasificacion(){
        ArrayList<Jugador> clasificacion = new ArrayList<Jugador>();
        for(Jugador jug: this.jugadores){
            if(clasificacion.isEmpty()){
                clasificacion.add(jug);
            }else{
                boolean estaAgregado = false;
                for(Jugador jugClas: clasificacion){
                    if(jug.getPuntaje() > jugClas.getPuntaje()){
                        clasificacion.add(clasificacion.indexOf(jugClas), jug);
                        estaAgregado = true;
                        break;
                    }
                }
                
                if(!estaAgregado){
                    clasificacion.add(jug);
                }
            }
        }
        return clasificacion;
    }
    
    public Jugador getGanador(){
        Jugador ganador = jugadores.get(0);
        for(int i = 1; i < jugadores.size(); i++){
            Jugador jugAct = jugadores.get(i);
            if(jugAct.getPuntaje() > ganador.getPuntaje()){
                ganador = jugAct;
            }
        }
        return ganador;
    }

    public int getPartJugadas() {
        return partJugadas;
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }
    
}
