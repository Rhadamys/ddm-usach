/***********************************************************************
 * Module:  PuzleDeDados.java
 * Author:  mam28
 * Purpose: Defines the Class PuzleDeDados
 ***********************************************************************/
package Modelos;

import ModelosDAO.PuzzleDeDadosDAO;
import java.sql.SQLException;
import java.util.*;

public class PuzzleDeDados {
    private final ArrayList<Dado> dados;
    
    public PuzzleDeDados(ArrayList<Dado> dados){
        this.dados = dados;
    }
    
    public static boolean actualizarPuzzleJugador(int idJugador, ArrayList<Dado> dados){
        try {
            PuzzleDeDadosDAO.actualizarPuzzleJugador(idJugador, dados);
            return true;
        } catch (SQLException ex) {
            return false;
        }
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