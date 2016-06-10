/***********************************************************************
 * Module:  Criatura.java
 * Author:  mam28
 * Purpose: Defines the Class Criatura
 ***********************************************************************/
package Modelos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/** @pdOid dae863aa-62cf-47e0-9a47-196272f29518 */
public class Criatura extends ElementoEnCampo {
    private final int nivel;
    private int vida;
    private final int ataque;
    private final int defensa;
    
    public Criatura(
            String nombre,
            int puntosVida,
            int puntosAtaque,
            int puntosDefensa,
            int nivel,
            String nombreArchivoImagen,
            String descripcion){
        
        this.nombre = nombre;
        this.vida = puntosVida;
        this.ataque = puntosAtaque;
        this.defensa = puntosDefensa;
        this.nivel = nivel;
        this.nomArchivoImagen = nombreArchivoImagen;
        this.descripcion = descripcion;
    }
    
    public static Criatura getCriatura(String claveCriatura){
        File archivoCriaturas = new File("src/Otros/criaturas.txt");
        FileReader archivo;
        try {
            archivo = new FileReader(archivoCriaturas);
            BufferedReader lector = new BufferedReader(archivo);

            try {                
                String linea = lector.readLine();
                
                while(linea != null){
                    if(linea.contains(claveCriatura + ";")){
                        String[] infoCriatura = linea.split(";");
                        return new Criatura(
                                infoCriatura[1],
                                Integer.parseInt(infoCriatura[2]),
                                Integer.parseInt(infoCriatura[3]),
                                Integer.parseInt(infoCriatura[4]),
                                Integer.parseInt(infoCriatura[5]),
                                infoCriatura[0],
                                infoCriatura[6]);
                    }
                    
                    linea = lector.readLine();
                }
                
            } catch (IOException ex) {
                return null;
            }
        } catch (FileNotFoundException ex) {
            return null;
        }
        
        return null;
    }

    public int getNivel() {
        return nivel;
    }

    public int getVida() {
        return vida;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getDefensa() {
        return defensa;
    }
    
}