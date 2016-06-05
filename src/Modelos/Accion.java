/***********************************************************************
 * Module:  Accion.java
 * Author:  mam28
 * Purpose: Defines the Class Accion
 ***********************************************************************/
package Modelos;

import java.util.*;

/** @pdOid 3209e44d-1fcb-494a-b232-5edd8db5452f */
public class Accion {
    private int accion;
    
    public static String[] lanzarDados(ArrayList<Dado> dados){
        Random rnd = new Random();
        String[] caras = new String[dados.size()];
        
        for(Dado dado: dados){
            caras[dados.indexOf(dado)] = dado.getCaras()[rnd.nextInt(5)];
        }
    
        return caras;
    }
}