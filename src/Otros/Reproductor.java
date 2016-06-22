package Otros;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;

public class Reproductor implements BasicPlayerListener{
    private static final BasicPlayer REPRODUCTOR = new BasicPlayer();
    private static final BasicPlayer REP_EFECTOS = new BasicPlayer();
    private static String[] listaReproduccion;
    private static int actual = 0;
    private static int[] ordenReproduccion;
    public static boolean loop = true;
    
    public static void reproducir(String[] lista){
        try {
            if(REPRODUCTOR.getStatus() == BasicPlayer.PLAYING){
                REPRODUCTOR.stop();
            }
            
            listaReproduccion = lista;
            definirOrdenAleatorio();
            loop = true;
            
            siguiente();
        } catch(Exception e) {
            System.out.print("-------Error-----" + e.getMessage());
        }
    }
    
    public static void siguiente() throws BasicPlayerException{
        REPRODUCTOR.open(new File(Constantes.RUTA_MUSICA + listaReproduccion[ordenReproduccion[actual]] + Constantes.EXT_M));
        actual = actual == listaReproduccion.length - 1 ? 0 : actual + 1;
        REPRODUCTOR.play();
    }
    
    public static void pausar(){
        try {
            REPRODUCTOR.pause();
        } catch (BasicPlayerException ex) {
            // Nada
        }
    }
    
    public static void continuar(){
        try {
            REPRODUCTOR.resume();
        } catch (BasicPlayerException ex) {
            // Nada
        }
    }
    
    public static void definirOrdenAleatorio(){
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
    
    public static void finalizarReproductor(){
        try {
            REPRODUCTOR.stop();
        } catch (BasicPlayerException ex) {
            // Nada
        }
    }
    
    public static void reproducirEfecto(String efecto){
        try {
            REP_EFECTOS.open(new File(efecto));
            REP_EFECTOS.play();
        } catch (BasicPlayerException ex) {
            // Nada
        }
    }
    
    public static void agregarListener(){
        REPRODUCTOR.addBasicPlayerListener(new Reproductor());
    }

    @Override
    public void opened(Object o, Map map) {
    }

    @Override
    public void progress(int i, long l, byte[] bytes, Map map) {
    }

    @Override
    public void stateUpdated(BasicPlayerEvent bpe) {
        if(bpe.getCode() == BasicPlayerEvent.EOM && loop){
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