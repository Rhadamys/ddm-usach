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
    
    public static Usuario existe(String usuario) throws IOException{
        File archivoUsuarios = new File("src\\Otros\\usuarios.txt");
        
        try {
            FileReader archivo = new FileReader(archivoUsuarios);
            BufferedReader lector = new BufferedReader(archivo); 
            
            String linea = lector.readLine();
            while(linea != null){
                if(linea.contains(usuario + ";")){
                    String[] infoUsuario = linea.split(";");
                    
                    ArrayList<Dado> dados = new ArrayList();
                    for(int i = 3; i < infoUsuario.length; i++){
                        dados.add(Dado.getDado(infoUsuario[i]));
                    }
                    
                    lector.close();
                    archivo.close();
                    return new Usuario(infoUsuario[0], infoUsuario[1],
                            JefeDeTerreno.getJefe(infoUsuario[2]), dados);
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
    
    public boolean validar(String usuario, String pass){
        return usuario.equals(this.nombreJugador) && pass.equals(this.pass);
    }

    public String getUsername() {
        return nombreJugador;
    }

    public String getPass() {
        return pass;
    }

    public ArrayList<Dado> getDados() {
        return dados;
    }
    
}