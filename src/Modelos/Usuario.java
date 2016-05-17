/***********************************************************************
 * Module:  Usuario.java
 * Author:  mam28
 * Purpose: Defines the Class Usuario
 ***********************************************************************/
package Modelos;

import java.util.*;

/** @pdOid 5fdf1593-9417-4518-a690-8bb149f077b5 */
public class Usuario extends Jugador {
    private String username;
    private String pass;
    
    public Usuario(String username, String pass){
        this.username = username;
        this.pass = pass;
    }
}