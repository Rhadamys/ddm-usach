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

/** @pdOid 5fdf1593-9417-4518-a690-8bb149f077b5 */
public class Usuario extends Jugador {
    private String username;
    private String pass;
    private int intentos;
    
    public static Usuario existe(String usuario) throws IOException{
        File archivoUsuarios = new File("src\\Otros\\usuarios.txt");
        
        try {
            FileReader archivo = new FileReader(archivoUsuarios);
            BufferedReader lector = new BufferedReader(archivo); 
            
            String linea = lector.readLine();
            while(linea != null){
                if(linea.contains(usuario + ";")){
                    String[] infoUsuario = linea.split(";");
                    lector.close();
                    archivo.close();
                    return new Usuario(infoUsuario[0], infoUsuario[1]);
                }    
                
                linea = lector.readLine();           
            }
            
            lector.close();
            archivo.close();
            return null;
            
        } catch (FileNotFoundException ex) {
            return null;
        }
    }
    
    public Usuario(String username, String pass){
        this.username = username;
        this.pass = pass;
    }

    public String getUsername() {
        return username;
    }
    
    public boolean validar(String usuario, String pass){
        return usuario.equals(this.username) && pass.equals(this.pass);
    }
}