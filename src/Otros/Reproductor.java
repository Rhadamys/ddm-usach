package Otros;

import java.io.File;
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
    
    public static void reproducirEfecto(String efecto){
        REP_EFECTOS.reproducirEfecto(efecto);
    }
    
    public static int getStatusMusica(){
        return REPRODUCTOR.getStatus();
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
        
        try {
            this.controlRep.setGain(0.8);
        } catch (BasicPlayerException e) {
            System.out.println("--- SE HA PRODUCIDO UN EXCEPCION ---");
            String msg = "No se pudo ajustar el volumen del reproductor.";
            Logger.getLogger(Reproductor.class.getName()).log(Level.SEVERE, msg, e);
        }
    }
    
    public void reproducir(String[] lista){
        try {
            if(reproductor.getStatus() == BasicPlayer.PLAYING){
                controlRep.stop();
            }
            
            listaReproduccion = lista;
            definirOrdenAleatorio();
            
            siguiente();
        } catch(BasicPlayerException e) {
            System.out.println("--- SE HA PRODUCIDO UN EXCEPCION ---");
            String msg = "Error al intentar reproducir el siguiente archivo de audio.";
            Logger.getLogger(Reproductor.class.getName()).log(Level.SEVERE, msg, e);
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
        } catch (BasicPlayerException e) {
            System.out.println("--- SE HA PRODUCIDO UN EXCEPCION ---");
            String msg = "Error al intentar pausar la reproducción.";
            Logger.getLogger(Reproductor.class.getName()).log(Level.SEVERE, msg, e);
        }
    }
    
    public void continuar(){
        try {
            controlRep.resume();
        } catch (BasicPlayerException e) {
            System.out.println("--- SE HA PRODUCIDO UN EXCEPCION ---");
            String msg = "Error al intentar reanudar la reproducción.";
            Logger.getLogger(Reproductor.class.getName()).log(Level.SEVERE, msg, e);
        }
    }
    
    public void definirOrdenAleatorio(){
        actual = 0;
        ordenReproduccion = new int[listaReproduccion.length];
        
        ArrayList<Integer> orden = new ArrayList<Integer>();
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
        } catch (BasicPlayerException e) {
            System.out.println("--- SE HA PRODUCIDO UN EXCEPCION ---");
            String msg = "Error al detener la reproducción.";
            Logger.getLogger(Reproductor.class.getName()).log(Level.SEVERE, msg, e);
        }
    }
    
    public int getStatus(){
        return this.reproductor.getStatus();
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
            } catch (BasicPlayerException e) {
                System.out.println("--- SE HA PRODUCIDO UN EXCEPCION ---");
                String msg = "Error al intentar reproducir el siguiente archivo de audio.";
                Logger.getLogger(Reproductor.class.getName()).log(Level.SEVERE, msg, e);
            }
        }
    }

    @Override
    public void setController(BasicController bc) {
    }
}

class ReproductorEfecto {
    private final ArrayList<BasicPlayer> efectos;
    private final String[] listaEfectos = Constantes.EFECTOS;
    
    public ReproductorEfecto(){
        this.efectos = new ArrayList<BasicPlayer>();
        
        int idx = 0;
        final int cantEfectos = listaEfectos.length;
        do{
            this.efectos.add(new BasicPlayer());
            try {
                this.efectos.get(idx).open(new File(listaEfectos[idx]));
            } catch (BasicPlayerException e) {
                System.out.println("--- SE HA PRODUCIDO UN EXCEPCION ---");
                String msg = "Error al abrir el archivo de audio especificado.";
                Logger.getLogger(Reproductor.class.getName()).log(Level.SEVERE, msg, e);
            }
            idx++;
        }while(idx < cantEfectos);
    }
    
    public void reproducirEfecto(String efecto){
        for(int i = 0; i < listaEfectos.length; i++){
            if(listaEfectos[i].equals(efecto)){
                try {
                    this.efectos.get(i).play();
                } catch (BasicPlayerException e) {
                    System.out.println("--- SE HA PRODUCIDO UN EXCEPCION ---");
                    String msg = "No se ha podido reproducir el efecto.";
                    Logger.getLogger(Reproductor.class.getName()).log(Level.SEVERE, msg, e);
                }
            }
        }
    }
    
}