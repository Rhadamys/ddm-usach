/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BD.Conection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Veronica
 */
public class Conection 
{
    private static final String SERVIDOR = "localhost";
    private static final String PUERTO = "1527";
    private static final String NOMBRE_BD = "grupo2";
    private static final String USUARIO = "grupo2";
    private static final String PASSWORD = "grupo2";
    public static final String URL_CONEXION = "jdbc:derby://" + SERVIDOR + ":"+ PUERTO +"/"+ NOMBRE_BD +";user="+ USUARIO +";password=" + PASSWORD;
    
    private Connection conection  = null;
    
    public boolean conectar() throws SQLException
    {
        this.conection  = DriverManager.getConnection(URL_CONEXION);
        
        return this.conection != null;
    }
    
    public void desconectar() throws SQLException
    {
        if(this.conection  != null)
        {
            this.conection.close();
        }
    }
    
    public Statement crearConsulta() throws SQLException
    {
        if(this.conection != null)
        {
            return this.conection.createStatement();
        }
        else
        {
            return null;
        }
    }
}
