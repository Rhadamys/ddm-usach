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
    protected int vida;
    protected int ataque;
    protected int defensa;

    public String getNomArchivoImagen() {
        return nomArchivoImagen;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getVida() {
        return vida;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getDefensa() {
        return defensa;
    }
    
    
}