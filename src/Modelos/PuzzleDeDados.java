/***********************************************************************
 * Module:  PuzleDeDados.java
 * Author:  mam28
 * Purpose: Defines the Class PuzleDeDados
 ***********************************************************************/
package Modelos;

import java.util.*;

/** @pdOid 859bcc3d-e128-4446-8809-e9f0961ece7a */
public class PuzzleDeDados {
    private ArrayList<Dado> dados;
    
    public PuzzleDeDados(ArrayList<Dado> dados){
        this.dados = dados;
    }

    public ArrayList<Dado> getDados() {
        return dados;
    }

    public Dado getDado(int i){
        return dados.get(i);
    }
    
    public void agregarDado(Dado dado){
        dados.add(dado);
    }
}