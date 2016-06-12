/***********************************************************************
 * Module:  ElementoEnCampo.java
 * Author:  mam28
 * Purpose: Defines the Class ElementoEnCampo
 ***********************************************************************/
package Modelos;

/** @pdOid cf49c08b-f401-44d9-873a-b7ba18edb1d9 */
public abstract class ElementoEnCampo {
    int dueno;
    protected String nomArchivoImagen;
    protected String nombre;
    protected String descripcion;

    public String getNomArchivoImagen() {
        return nomArchivoImagen;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }
    
    public int getDueno(){
        return this.dueno;
    }
    
    public void setDueno(int dueno){
        this.dueno = dueno;
    }
}