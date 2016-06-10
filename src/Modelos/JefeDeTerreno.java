/***********************************************************************
 * Module:  JefeDeTerreno.java
 * Author:  mam28
 * Purpose: Defines the Class JefeDeTerreno
 ***********************************************************************/
package Modelos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/** @pdOid ba01c964-70b9-429b-9412-0cfa461bb9c0 */
public class JefeDeTerreno extends ElementoEnCampo {
    private final int habilidad;
    private final int vida;
    
    public JefeDeTerreno(
            String nombre, 
            int habilidad,
            String descHabilidad,
            int puntosVida,
            String nombreImagen){
        
        this.nomArchivoImagen = nombreImagen;
        this.nombre = nombre;
        this.habilidad = habilidad;
        this.descripcion = descHabilidad;
        this.vida = puntosVida;
    }
    
    public static ArrayList getJefes() {
        File archivoJefes = new File("src/Otros/jefes.txt");
        FileReader archivo;
        try {
            archivo = new FileReader(archivoJefes);
            BufferedReader lector = new BufferedReader(archivo);
            try {
                String linea = lector.readLine();
                
                ArrayList<JefeDeTerreno> jefes = new ArrayList();
                while (linea != null){
                    String[] infoJefe = linea.split(";");
                    jefes.add(new JefeDeTerreno(
                            infoJefe[1],
                            Integer.parseInt(infoJefe[2]),
                            infoJefe[3],
                            Integer.parseInt(infoJefe[4]),
                            infoJefe[0]));
                    
                    linea = lector.readLine();
                }
                
                return jefes;
            } catch (IOException ex) {
                return null;
            }
        } catch (FileNotFoundException ex) {
            return null;
        }
    }
    
    public static JefeDeTerreno getJefe(String claveJefe){
        File archivoJefes = new File("src/Otros/jefes.txt");
        FileReader archivo;
        try {
            archivo = new FileReader(archivoJefes);
            BufferedReader lector = new BufferedReader(archivo);
            try {
                String linea = lector.readLine();
                while (linea != null){
                    if(linea.startsWith(claveJefe + ";")){
                        String[] infoJefe = linea.split(";");
                        
                        return new JefeDeTerreno(
                            infoJefe[1],
                            Integer.parseInt(infoJefe[2]),
                            infoJefe[3],
                            Integer.parseInt(infoJefe[4]),
                            infoJefe[0]);
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

    public int getHabilidad() {
        return habilidad;
    }

    public int getVida() {
        return vida;
    }
}