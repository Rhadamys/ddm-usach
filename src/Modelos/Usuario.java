/***********************************************************************
 * Module:  Usuario.java
 * Author:  mam28
 * Purpose: Defines the Class Usuario
 ***********************************************************************/
package Modelos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/** @pdOid 5fdf1593-9417-4518-a690-8bb149f077b5 */
public class Usuario extends Jugador {
    private String pass;
    
    public Usuario(String username, String pass, JefeDeTerreno jefe, ArrayList<Dado> dados){
        this.tipoJugador = "Humano";
        this.nombreJugador = username;
        this.pass = pass;
        this.dados = dados;
        this.jefeDeTerreno = jefe;
    }
    
    /**
     * Comprueba si el usuario indicado existe en los registros
     * @param usuario Nombre de usuario que se buscará en los registros
     * @return true | existe - false | no existe
     */
    public static boolean existe(String usuario) {
        File archivoUsuarios = new File("src\\Otros\\usuarios.txt");
        
        try {
            FileReader archivo = new FileReader(archivoUsuarios);
            BufferedReader lector = new BufferedReader(archivo); 
            
            try {
                // Se lee la primera línea de los registros
                String linea = lector.readLine();
                
                // Hasta el final del archivo
                while(linea != null){
                    // Si la línea actual contiene el nombre de usuario
                    if(linea.contains(usuario + ";")){
                        // Se cierra el archivo y devuelve true indicando que existe
                        lector.close();
                        archivo.close();
                        return true;
                    }else{
                        // Sino, lee la siguiente línea
                        linea = lector.readLine(); 
                    }              
                }

                // Si se llega al final del archivo, entonces no existe
                lector.close();
                archivo.close();
                return false;
            } catch (IOException ex) {
                // Si ocurre una excepción, se indica que el usuario no existe
                return false;
            }
            
        } catch (FileNotFoundException ex) {
            // Si ocurre una excepción, se indica que el usuario no existe
            return false;
        }
        
    }
    
    /**
     * Entrega la instancia de Usuario del usuario indicado
     * @param usuario Usuario que se buscará en los registros
     * @return Instancia de Usuario
     */
    public static Usuario getUsuario(String usuario){
        File archivoUsuarios = new File("src\\Otros\\usuarios.txt");
        
        try {
            FileReader archivo = new FileReader(archivoUsuarios);
            BufferedReader lector = new BufferedReader(archivo); 
            
            try {
                // Se lee la primera línea de los registros
                String linea = lector.readLine();
                
                // Hasta el final del archivo
                while(linea != null){
                    // Si la línea actual contiene el nombre del usuario
                    if(linea.contains(usuario + ";")){
                        // Se obtiene un Array con los datos del usuario
                        String[] infoUsuario = linea.split(";");

                        // Se obtienen los dados del usuario
                        ArrayList<Dado> dados = new ArrayList();
                        for(int i = 3; i < infoUsuario.length; i++){
                            dados.add(Dado.getDado(infoUsuario[i]));
                        }

                        lector.close();
                        archivo.close();
                        
                        // Se retorna la instancia del usuario
                        return new Usuario(infoUsuario[0], infoUsuario[1],
                                JefeDeTerreno.getJefe(infoUsuario[2]), dados);
                    }    

                    linea = lector.readLine();           
                }

                lector.close();
                archivo.close();
                return null;
            } catch (IOException ex) {
                // Si ocurre una excepción, se retorna null
                return null;
            }
            
        } catch (FileNotFoundException ex) {
            // Si ocurre una excepción, se retorna null
            return null;
        }
    }

    /**
     * Devuelve el nombre de usuario de esta instancia de Usuario
     * @return Nombre de usuario
     */
    public String getUsername() {
        return nombreJugador;
    }

    /**
     * Devuelve la contraseña de esta instancia de usuario
     * @return Contraseña
     */
    public String getPass() {
        return pass;
    }
    
}