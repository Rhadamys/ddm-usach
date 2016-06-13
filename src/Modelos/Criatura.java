/***********************************************************************
 * Module:  Criatura.java
 * Author:  mam28
 * Purpose: Defines the Class Criatura
 ***********************************************************************/
package Modelos;

/** @pdOid dae863aa-62cf-47e0-9a47-196272f29518 */
public class Criatura extends ElementoEnCampo {
    private final int nivel;
    private int vida;
    private int vidaMaxima;
    private final int vidaPorDefecto;
    private int ataque;
    private final int ataquePorDefecto;
    private int defensa;
    private final int defensaPorDefecto;
    private int turnosInmovilizada;
    private int costoMovimiento;
    private int turnosCostoMovInc;
    
    public Criatura(
            String nombre,
            int puntosVida,
            int puntosAtaque,
            int puntosDefensa,
            int nivel,
            String nombreArchivoImagen,
            String descripcion){
        
        this.nombre = nombre;
        this.vida = puntosVida;
        this.vidaMaxima = puntosVida;
        this.vidaPorDefecto = puntosVida;
        this.ataque = puntosAtaque;
        this.ataquePorDefecto = puntosAtaque;
        this.defensa = puntosDefensa;
        this.defensaPorDefecto = puntosDefensa;
        this.nivel = nivel;
        this.nomArchivoImagen = nombreArchivoImagen;
        this.descripcion = descripcion;
        this.turnosInmovilizada = 0;
        this.costoMovimiento = 1;
        this.turnosCostoMovInc = 0;
    }
    
    public void reiniciar(int dueno){
        this.dueno = dueno;
        this.vida = this.vidaMaxima;
    }
    
    public void restarVida(int vida){
        this.vida -= vida;
    }
    
    public void aumentarVidaMaxima(int vida){
        this.vidaMaxima += vida;
        this.vida = vidaMaxima;
        this.turnosInmovilizada = 0;
    }
    
    public void aumentarAtaque(int aumento){
        this.ataque += aumento;
    }
    
    public void aumentarDefensa(int aumento){
        this.defensa += aumento;
    }
    
    public void disminuirTurnosInmovilizada(){
        if(turnosInmovilizada != 0){
            this.turnosInmovilizada--;
        }
    }
    
    public void disminuirTurnosCostoMovInc(){
        if(turnosCostoMovInc != 0){
            this.turnosCostoMovInc--;
            if(turnosCostoMovInc == 0){
                this.costoMovimiento = 1;
            }
        }
    }
    
    public int getNivel() {
        return nivel;
    }

    public int getVida() {
        return vida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getVidaPorDefecto() {
        return vidaPorDefecto;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getAtaquePorDefecto() {
        return ataquePorDefecto;
    }

    public int getDefensa() {
        return defensa;
    }

    public int getDefensaPorDefecto() {
        return defensaPorDefecto;
    }

    public int getTurnosInmovilizada() {
        return turnosInmovilizada;
    }

    public int getCostoMovimiento() {
        return costoMovimiento;
    }

    public int getTurnosCostoMovInc() {
        return turnosCostoMovInc;
    }

    public void setTurnosInmovilizada(int inmovilizada) {
        this.turnosInmovilizada = inmovilizada;
    }

    public void setCostoMovimiento(int costoMovimiento) {
        this.costoMovimiento = costoMovimiento;
    }

    public void setTurnosCostoMovInc(int turnosCostoMovInc) {
        this.turnosCostoMovInc = turnosCostoMovInc;
    }

}