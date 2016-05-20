/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Otros;

/**
 *
 * @author mam28
 */
public enum Respuesta {
    CAMPOS_VACIOS("Completa todos los campos."),
    USUARIO_NO_EXISTE("Usuario no existe."),
    PASS_INCORRECTA("Contraseña incorrecta."),
    USUARIO_LOGUEADO("");
    
    private String mensaje;
    Respuesta(String mensaje){
        this.mensaje = mensaje;
    }
    
    public String getText(){
        return mensaje;
    }
}
