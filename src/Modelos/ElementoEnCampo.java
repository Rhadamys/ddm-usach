/***********************************************************************
 * Module:  ElementoEnCampo.java
 * Author:  mam28
 * Purpose: Defines the Class ElementoEnCampo
 ***********************************************************************/
package Modelos;

import java.util.*;

/** @pdOid cf49c08b-f401-44d9-873a-b7ba18edb1d9 */
public abstract class ElementoEnCampo {
    protected String nomArchivoImagen;
    protected String nombre;
    protected String descripcion;
    protected int puntosVida;
    protected int puntosAtaque;
    protected int puntosDefensa;

    public String getNomArchivoImagen() {
        return nomArchivoImagen;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getPuntosVida() {
        return puntosVida;
    }

    public int getPuntosAtaque() {
        return puntosAtaque;
    }

    public int getPuntosDefensa() {
        return puntosDefensa;
    }
    
    
}