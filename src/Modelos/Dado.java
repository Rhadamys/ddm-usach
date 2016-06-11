/***********************************************************************
 * Module:  Dado.java
 * Author:  mam28
 * Purpose: Defines the Class Dado
 ***********************************************************************/
package Modelos;

/** @pdOid 62ea5a63-657a-44c3-a1da-852adcc35713 */
public class Dado {
    private final Criatura criatura;
    private final int nivel;
    private final int[] caras;
    private boolean paraJugar;
    private boolean paraLanzar;
    
    public Dado(Criatura criatura, int nivel, int[] caras){
        this.criatura = criatura;
        this.nivel = nivel;
        this.caras = caras;
        this.paraLanzar = true;
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

    public boolean isParaJugar() {
        return paraJugar;
    }

    public boolean isParaLanzar() {
        return paraLanzar;
    }

    public void setParaJugar(boolean paraJugar) {
        this.paraJugar = paraJugar;
    }

    public void setParaLanzar(boolean paraLanzar) {
        this.paraLanzar = paraLanzar;
    }
    
}