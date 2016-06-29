package Otros;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;

public class Reproductor {
    private static final ReproductorMusica REPRODUCTOR = new ReproductorMusica();
    private static final ReproductorEfecto REP_EFECTOS = new ReproductorEfecto();
    
    public static void reproducir(String[] lista){
        REPRODUCTOR.reproducir(lista);
    }
    
    public static void siguiente() throws BasicPlayerException{
        REPRODUCTOR.siguiente();
    }
    
    public static void pausar(){
        REPRODUCTOR.pausar();
    }
    
    public static void continuar(){
        REPRODUCTOR.continuar();
    }
    
    public static void definirOrdenAleatorio(){
        REPRODUCTOR.definirOrdenAleatorio();
    }
    
    public static void finalizarReproductor(){
        REPRODUCTOR.finalizarReproductor();
    }
    
    public static void volumenMusica(double volumen) {
        REPRODUCTOR.setVolumen(volumen);
    }
    
    public static void reproducirEfecto(String efecto){
        REP_EFECTOS.reproducirEfecto(efecto);
    }
}

class ReproductorMusica implements BasicPlayerListener{
    private final BasicPlayer reproductor;
    private final BasicController controlRep;
    private String[] listaReproduccion;
    private int actual;
    private int[] ordenReproduccion;
    
    public ReproductorMusica(){
        this.reproductor = new BasicPlayer();
        this.controlRep = (BasicController) this.reproductor;
        
        this.actual = 0;
        this.reproductor.addBasicPlayerListener(this);
    }
    
    public void reproducir(String[] lista){
        try {
            if(reproductor.getStatus() == BasicPlayer.PLAYING){
                controlRep.stop();
            }
            
            listaReproduccion = lista;
            definirOrdenAleatorio();
            
            siguiente();
        } catch(Exception e) {
            System.out.print("-------Error----- | " + e.getMessage());
        }
    }
    
    public void siguiente() throws BasicPlayerException{
        controlRep.open(new File(Constantes.RUTA_MUSICA + listaReproduccion[ordenReproduccion[actual]] + Constantes.EXT_M));
        actual = actual == listaReproduccion.length - 1 ? 0 : actual + 1;
        controlRep.play();
    }
    
    public void pausar(){
        try {
            controlRep.pause();
        } catch (BasicPlayerException ex) {
            // Nada
        }
    }
    
    public void continuar(){
        try {
            controlRep.resume();
        } catch (BasicPlayerException ex) {
            // Nada
        }
    }
    
    public void definirOrdenAleatorio(){
        actual = 0;
        ordenReproduccion = new int[listaReproduccion.length];
        
        ArrayList<Integer> orden = new ArrayList();
        Random rnd = new Random();
        
        for(int i = 0; i < ordenReproduccion.length; i++){
            int numAudio = 0;
            do{
                numAudio = rnd.nextInt(listaReproduccion.length);
            }while(orden.contains(numAudio));
            
            orden.add(numAudio);
        }
        
        for(int i = 0; i < ordenReproduccion.length; i++){
            ordenReproduccion[i] = orden.get(i);
        }
    }
    
    public void finalizarReproductor(){
        try {
            controlRep.stop();
        } catch (BasicPlayerException ex) {
            // Nada
        }
    }
    
    public void setVolumen(double volumen){
        try {
            this.controlRep.setGain(volumen);
        } catch (BasicPlayerException ex) {
            // Nada
        }
    }

    @Override
    public void opened(Object o, Map map){
    }

    @Override
    public void progress(int i, long l, byte[] bytes, Map map) {
    }

    @Override
    public void stateUpdated(BasicPlayerEvent bpe) {
        if(bpe.getCode() == BasicPlayerEvent.EOM){
            try {
                siguiente();
            } catch (BasicPlayerException ex) {
                // Nada
            }
        }
    }

    @Override
    public void setController(BasicController bc) {
    }
}

class ReproductorEfecto {
    private final BasicPlayer reproductor;
    private final BasicController controlRep;
    
    public ReproductorEfecto(){
        this.reproductor = new BasicPlayer();
        this.controlRep = (BasicController) this.reproductor;
    }
    
    public void reproducirEfecto(String efecto){
        try {
            reproductor.open(new File(efecto));
            reproductor.play();
        } catch (Exception ex) {
            // Nada
        }
    }
    
    public void setVolumen(double volumen){
        try {
            this.controlRep.setGain(volumen);
        } catch (BasicPlayerException ex) {
            // Nada
        }
    }
}