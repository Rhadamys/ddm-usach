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
    private String clave;
    private String nombre;
    private String habilidad;
    private int puntosVida;
    
    public JefeDeTerreno(
            String clave, 
            String nombre, 
            String habilidad, 
            int puntosVida){
        
        this.clave = clave;
        this.nombre = nombre;
        this.habilidad = habilidad;
        this.puntosVida = puntosVida;
    }
    
    public static ArrayList getJefes() throws FileNotFoundException, IOException {
        ArrayList<HashMap<String, String>> jefes = new ArrayList();
        
        File archivoJefes = new File("src\\Otros\\jefes.txt");
        FileReader archivo = new FileReader(archivoJefes);
        BufferedReader lector = new BufferedReader(archivo);
        
        String linea = lector.readLine();
        while (linea != null){
            if(linea.equals("<nuevo>")){
                HashMap<String, String> jefe = new HashMap<>();
                jefe.put("Clave", lector.readLine());
                jefe.put("Nombre", lector.readLine());
                jefe.put("Habilidad", lector.readLine());
                jefe.put("Puntos de vida", lector.readLine());
                jefes.add(jefe);
                
                linea = lector.readLine();
            }
        }
        
        return jefes;
    }

    public String getClave() {
        return clave;
    }
    
    public String getNombre() {
        return nombre;
    }

    public String getHabilidad() {
        return habilidad;
    }

    public int getPuntosVida() {
        return puntosVida;
    }
    
}