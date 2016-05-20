/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import java.util.ArrayList;

/**
 *
 * @author Veronica
 */
public class ConjuntoDeDatos{
    public ArrayList<Modelologin> conjuntoUsuarios;
    
    public ConjuntoDeDatos(){
        this.conjuntoUsuarios = new ArrayList<>();
    }
    
    public boolean agregar(Modelologin m){
        return this.conjuntoUsuarios.add(m);
    }
    
    
}
