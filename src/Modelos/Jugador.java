/***********************************************************************
 * Module:  Jugador.java
 * Author:  mam28
 * Purpose: Defines the Class Jugador
 ***********************************************************************/
package Modelos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/** @pdOid cbe19438-3292-497c-bc58-a1da1e9c38b6 */
public abstract class Jugador {
    protected ArrayList<Dado> dados;
    protected String nombreJugador;
    protected String tipoJugador;
    protected JefeDeTerreno jefeDeTerreno;
    protected int equipo;
    
    public static ArrayList<Jugador> getJugadores(){
        ArrayList<Jugador> jugadores = new ArrayList();
        
        File archivoUsuarios = new File("src\\Otros\\usuarios.txt");
        
        try {
            FileReader archivo = new FileReader(archivoUsuarios);
            BufferedReader lector = new BufferedReader(archivo); 
            
            try {
                // Se lee la primera línea de los registros
                String linea = lector.readLine();
                
                // Hasta el final del archivo
                while(linea != null){
                    // Se obtiene un Array con los datos del jugador
                    String[] infoJugador = linea.split(";");

                    // Se obtienen los dados del jugador
                    ArrayList<Dado> dados = new ArrayList();
                    for(int i = 3; i < infoJugador.length; i++){
                        dados.add(Dado.getDado(infoJugador[i]));
                    }

                    // Se agrega el jugador a la lista
                    jugadores.add(new Usuario(infoJugador[0], infoJugador[1],
                                JefeDeTerreno.getJefe(infoJugador[2]), dados));
                    linea = lector.readLine();           
                }

                lector.close();
                archivo.close();
            } catch (IOException ex) {
                // Si ocurre una excepción, se retorna null
                return null;
            }
            
        } catch (FileNotFoundException ex) {
            // Si ocurre una excepción, se retorna null
            return null;
        }
        
        return jugadores;
    }
    
    public static ArrayList<Jugador> getJugadores(ArrayList<Jugador> excluidos){
        ArrayList<Jugador> jugadores = new ArrayList();
        
        File archivoUsuarios = new File("src\\Otros\\usuarios.txt");
        
        try {
            FileReader archivo = new FileReader(archivoUsuarios);
            BufferedReader lector = new BufferedReader(archivo); 
            
            try {
                // Se lee la primera línea de los registros
                String linea = lector.readLine();
                
                // Hasta el final del archivo
                while(linea != null){
                    // Se obtiene un Array con los datos del jugador
                    String[] infoJugador = linea.split(";");
                    
                    boolean excluido = false;
                    for(Jugador jugador: excluidos){
                        if(jugador.getNombreJugador().equals(infoJugador[0])){
                            excluido = true;
                            break;
                        }
                    }
                    
                    if(!excluido){
                        // Se obtienen los dados del jugador
                        ArrayList<Dado> dados = new ArrayList();
                        for(int i = 3; i < infoJugador.length; i++){
                            dados.add(Dado.getDado(infoJugador[i]));
                        }

                        // Se agrega el jugador a la lista
                        jugadores.add(new Usuario(infoJugador[0], infoJugador[1],
                                    JefeDeTerreno.getJefe(infoJugador[2]), dados)); 
                    } 
                    
                    linea = lector.readLine();         
                }

                lector.close();
                archivo.close();
            } catch (IOException ex) {
                // Si ocurre una excepción, se retorna null
                return null;
            }
            
        } catch (FileNotFoundException ex) {
            // Si ocurre una excepción, se retorna null
            return null;
        }
        
        return jugadores;
    }

    public ArrayList<Dado> getDados() {
        return dados;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public String getTipoJugador() {
        return tipoJugador;
    }

    public JefeDeTerreno getJefeDeTerreno() {
        return jefeDeTerreno;
    }

    public void setEquipo(int equipo) {
        this.equipo = equipo;
    }
    
}