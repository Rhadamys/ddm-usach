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
import java.util.*;

/** @pdOid dae863aa-62cf-47e0-9a47-196272f29518 */
public class Criatura extends ElementoEnCampo {
    private int puntosVida;
    private int puntosAtaque;
    private int puntosDefensa;
    private final String nombreImagen;
    
    public Criatura(int puntosVida, int puntosAtaque, int puntosDefensa, String nombreImagen){
        this.puntosVida = puntosVida;
        this.puntosAtaque = puntosAtaque;
        this.puntosDefensa = puntosDefensa;
        this.nombreImagen = nombreImagen;
    }
    
    public static Criatura getCriatura(String claveCriatura){
        File archivoCriaturas = new File("src\\Otros\\criaturas.txt");
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
                                Integer.parseInt(infoCriatura[1]),
                                Integer.parseInt(infoCriatura[2]),
                                Integer.parseInt(infoCriatura[3]),
                                infoCriatura[0]);
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

    public int getPuntosVida() {
        return puntosVida;
    }

    public int getPuntosAtaque() {
        return puntosAtaque;
    }

    public int getPuntosDefensa() {
        return puntosDefensa;
    }

    public String getNombreImagen() {
        return nombreImagen;
    }
}