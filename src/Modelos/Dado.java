/***********************************************************************
 * Module:  Dado.java
 * Author:  mam28
 * Purpose: Defines the Class Dado
 ***********************************************************************/
package Modelos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/** @pdOid 62ea5a63-657a-44c3-a1da-852adcc35713 */
public class Dado {
    private final Criatura criatura;
    private final int nivel;
    private final int[] caras;
    private final String clave;
    
    public Dado(Criatura criatura, int nivel, int[] caras, String clave){
        this.criatura = criatura;
        this.nivel = nivel;
        this.caras = caras;
        this.clave = clave;
    }
    
    public static Dado getDado(String claveDado){
        File archivoDados = new File("src/Otros/dados.txt");
        FileReader archivo;
        try {
            archivo = new FileReader(archivoDados);
            BufferedReader lector = new BufferedReader(archivo);

            try {
                String linea = lector.readLine();
                
                while(linea != null){
                    if(linea.startsWith(claveDado)){
                        String[] infoDado = linea.split(";");
                        
                        int[] caras = {
                            Integer.parseInt(infoDado[3]),
                            Integer.parseInt(infoDado[4]),
                            Integer.parseInt(infoDado[5]),
                            Integer.parseInt(infoDado[6]),
                            Integer.parseInt(infoDado[7]),
                            Integer.parseInt(infoDado[8])};
                        
                        lector.close();
                        archivo.close();
                        
                        return new Dado(
                                Criatura.getCriatura(infoDado[1]),
                                Integer.parseInt(infoDado[2]),
                                caras,
                                infoDado[0]);
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
    
    public static Dado getDado(int nivelDado) {
        File archivoDados = new File("src/Otros/dados.txt");
        FileReader archivo;
        try {
            archivo = new FileReader(archivoDados);
            BufferedReader lector = new BufferedReader(archivo);

            try {
                String linea = lector.readLine();
                
                ArrayList<String[]> lineasDado = new ArrayList();
                
                while(linea != null){
                    String[] infoDado = linea.split(";");
                    
                    if(Integer.valueOf(infoDado[2]) == nivelDado){
                        lineasDado.add(infoDado);
                    }
                    
                    linea = lector.readLine();
                }
                
                String[] infoDado = lineasDado.get(new Random().nextInt(lineasDado.size()));
                
                int[] caras = {
                    Integer.parseInt(infoDado[3]),
                    Integer.parseInt(infoDado[4]),
                    Integer.parseInt(infoDado[5]),
                    Integer.parseInt(infoDado[6]),
                    Integer.parseInt(infoDado[7]),
                    Integer.parseInt(infoDado[8])};

                lector.close();
                archivo.close();

                return new Dado(
                        Criatura.getCriatura(infoDado[1]),
                        Integer.parseInt(infoDado[2]),
                        caras,
                        infoDado[0]);
                
            } catch (IOException ex) {
                return null;
            }
        } catch (FileNotFoundException ex) {
            return null;
        }
    }

    public Criatura getCriatura() {
        return criatura;
    }

    public int getNivel() {
        return nivel;
    }

    public int[] getCaras() {
        return caras;
    }

    public String getClave() {
        return clave;
    }
    
}