/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Otros;

import java.util.ArrayList;

/**
 *
 * @author JavaDonkey
 * @param <Objeto>
 */
public class ListaCircular<Objeto> extends ArrayList<Objeto>{
    private int actual = 0;
    
    public void next(){
        this.actual++;
    }
    
    public Objeto getNext(){
        Objeto objeto = get((actual + 1) % size());
        return objeto;
    }
    
    public Objeto getActual(){
        return get(actual % size());
    }
    
    public int getIdx(){
        return actual;
    }
    
    public void setIdx(int i){
        if(i < 0){
            this.actual = size() - i;
        }else if(i >= size()){
            this.actual = size();
        }else{
            this.actual = i;
        }
    }
}
