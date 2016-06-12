/***********************************************************************
 * Module:  Usuario.java
 * Author:  mam28
 * Purpose: Defines the Class Usuario
 ***********************************************************************/
package Modelos;

import ModelosDAO.UsuarioDAO;
import java.sql.SQLException;
import java.util.ArrayList;

/** @pdOid 5fdf1593-9417-4518-a690-8bb149f077b5 */
public class Usuario extends Jugador {
    private final int id;
    private final String pass;
    
    public Usuario(int id, String username, String pass, JefeDeTerreno jefe, PuzzleDeDados puzzle){
        this.id = id;
        this.nombreJugador = username;
        this.pass = pass;
        this.puzzle = puzzle;
        this.jefeDeTerreno = jefe;
    }
    
    /**
     * Entrega la instancia de Usuario del usuario indicado
     * @param usuario Usuario que se buscará en los registros
     * @return Instancia de Usuario
     */
    public static Usuario getUsuario(String usuario){
        try {
            return UsuarioDAO.getUsuario(usuario);
        } catch (SQLException ex) {
            return null;
        }
    }
    
    /**
     * Registra un usuario en la base de datos.
     * @param usuario Nombre de usuario.
     * @param pass Contraseña.
     * @param jefe Jefe de terreno elegido por el usuario.
     * @return Falso si el usuario ya existe en la base de datos.
     * @throws SQLException 
     */
    public static boolean registrarUsuario(String usuario, String pass, JefeDeTerreno jefe) throws SQLException{
        return UsuarioDAO.registrarUsuario(usuario, pass, jefe);
    }

    /**
     * Devuelve el nombre de usuario de esta instancia de Usuario
     * @return Nombre de usuario
     */
    public String getUsername() {
        return nombreJugador;
    }

    /**
     * Devuelve la contraseña de esta instancia de usuario
     * @return Contraseña
     */
    public String getPass() {
        return pass;
    }

    public int getId() {
        return id;
    }    
    
}