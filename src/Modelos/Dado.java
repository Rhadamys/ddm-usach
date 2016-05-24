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
    private String clave;
    private Criatura criatura;
    private String[] caras;
    
    public Dado(String clave, Criatura criatura, String[] caras){
        this.clave = clave;
        this.criatura = criatura;
        this.caras = caras;
    }
    
    public static Dado getDado(String claveDado){
        File archivoDados = new File("src\\Otros\\dados.txt");
        FileReader archivo;
        try {
            archivo = new FileReader(archivoDados);
            BufferedReader lector = new BufferedReader(archivo);

            try {
                String linea = lector.readLine();
                
                while(linea != null){
                    if(linea.startsWith(claveDado)){
                        String[] infoDado = linea.split(";");
                        
                        String[] caras = {infoDado[2], infoDado[3], infoDado[4],
                            infoDado[5], infoDado[6], infoDado[7]};
                        
                        return new Dado(infoDado[0], Criatura.getCriatura(infoDado[1]), caras);
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
    
    public static Dado getDado(int nivelCriatura) {
        File archivoDados = new File("src\\Otros\\dados.txt");
        FileReader archivo;
        try {
            archivo = new FileReader(archivoDados);
            BufferedReader lector = new BufferedReader(archivo);

            try {
                String linea = lector.readLine();
                
                ArrayList<String> dados = new ArrayList();
                while(linea != null){
                    dados.add(linea);
                    linea = lector.readLine();
                }
                
                Random rnd = new Random();
                while(true){
                    String lineaDado = dados.get(rnd.nextInt(dados.size()));
                    String[] infoDado = lineaDado.split(";");

                    Criatura criatura = Criatura.getCriatura(infoDado[1]);

                    if(criatura.getNivel() == nivelCriatura){
                        String[] caras = {infoDado[2],infoDado[3], infoDado[4],
                            infoDado[5], infoDado[6], infoDado[7]};

                        return new Dado(infoDado[0], criatura, caras);
                    }
                }
            } catch (IOException ex) {
                return null;
            }
        } catch (FileNotFoundException ex) {
            return null;
        }
        
    }

    public String getClave() {
        return clave;
    }

    public Criatura getCriatura() {
        return criatura;
    }

    public String[] getCaras() {
        return caras;
    }
    
}