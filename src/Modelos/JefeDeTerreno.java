/***********************************************************************
 * Module:  JefeDeTerreno.java
 * Author:  mam28
 * Purpose: Defines the Class JefeDeTerreno
 ***********************************************************************/
package Modelos;

import ModelosDAO.JefeDeTerrenoDAO;
import java.sql.SQLException;
import java.util.ArrayList;

/** @pdOid ba01c964-70b9-429b-9412-0cfa461bb9c0 */
public class JefeDeTerreno extends ElementoEnCampo {
    private final int idJefe;
    private int vida;
    private final int vidaMaxima;
    private final double incVida;
    private final double incAtaque;
    private final double incDefensa;
    
    public JefeDeTerreno(
            int idJefe,
            String nombre, 
            String descHabilidad,
            String nombreImagen,
            int puntosVida,
            double incVida,
            double incAtaque,
            double incDefensa){
        
        this.idJefe = idJefe;
        this.nomArchivoImagen = nombreImagen;
        this.nombre = nombre;
        this.descripcion = descHabilidad;
        this.vida = puntosVida;
        this.vidaMaxima = puntosVida;
        this.incVida = incVida;
        this.incAtaque = incAtaque;
        this.incDefensa = incDefensa;
    }
    
    public static ArrayList<JefeDeTerreno> getJefes(){
        try {
            return JefeDeTerrenoDAO.getJefes();
        } catch (SQLException ex) {
            return null;
        }
    }
    
    public void reiniciar(int dueno){
        this.vida = this.vidaMaxima;
        this.dueno = dueno;
    }
    
    public void setVida(int vida){
        this.vida = vida;
    }
    
    public void restarVida(int vida){
        this.vida -= vida;
    }

    public int getIdJefe() {
        return idJefe;
    }

    public int getVida() {
        return vida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public double getIncVida() {
        return incVida;
    }

    public double getIncAtaque() {
        return incAtaque;
    }

    public double getIncDefensa() {
        return incDefensa;
    }
    
}