/***********************************************************************
 * Module:  Usuario.java
 * Author:  mam28
 * Purpose: Defines the Class Usuario
 ***********************************************************************/
package ModelosDAO;

import BD.Conection.Conection;
import Modelos.Dado;
import Modelos.JefeDeTerreno;
import Modelos.PuzzleDeDados;
import Modelos.Usuario;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UsuarioDAO {   
   
    public static Usuario getUsuario(String usuario) throws SQLException{
        Conection conection = new Conection();
        if(conection.conectar()){
            String consulta = "SELECT * FROM JUGADOR WHERE NOMBREJUGADOR = '" + usuario + "'";
            Statement stmt = conection.crearConsulta();
            
            if(stmt != null){
                ResultSet resultados = stmt.executeQuery(consulta);
                resultados.next();
                
                int id = resultados.getInt(1);
                String user = resultados.getString(2);
                String password = resultados.getString(3);
                int jefeDeTerreno = resultados.getInt(5);

                PuzzleDeDados puzzleJugador = PuzzleDeDadosDAO.getPuzzle(id);

                JefeDeTerreno jefeJugador = JefeDeTerrenoDAO.getJefe(jefeDeTerreno);
                
                for(Dado dado: puzzleJugador.getDados()){
                    dado.getCriatura().aumentarAtaque((int) (((double) dado.getCriatura().getAtaque()) * jefeJugador.getIncAtaque()));
                    dado.getCriatura().aumentarDefensa((int) (((double) dado.getCriatura().getDefensa()) * jefeJugador.getIncDefensa()));
                    dado.getCriatura().aumentarVidaMaxima((int) (((double) dado.getCriatura().getVida()) * jefeJugador.getIncVida()));
                }

                resultados.close();
                stmt.close();
                conection.desconectar();

                return new Usuario(user, password, jefeJugador, puzzleJugador);
            }else{            
                conection.desconectar();
                return null;
            }
        }
        else
        {
            return null;
        }
            
     }
                  
    public static void registrarUsuario(String usuario, String pass, JefeDeTerreno jefe) throws SQLException{
        Conection conection = new Conection();
        if(conection.conectar()){
            String consulta = "INSERT INTO GRUPO2.JUGADOR (NOMBREJUGADOR, PASSUSUARIO, ESHUMANO, ID_JEFEDETERRENO)" + 
                    " VALUES ('" + usuario + "', '" + pass + "', true, " + jefe.getIdJefe() + ")";
            Statement stmt = conection.crearConsulta();
            
            if(stmt != null){
                stmt.executeUpdate(consulta);
                stmt.close();
            }
            
            consulta = "SELECT ID_JUGADOR FROM GRUPO2.JUGADOR WHERE NOMBREJUGADOR = '" + usuario + "'";
            stmt = conection.crearConsulta();
            
            if(stmt != null){
                ResultSet resultados = stmt.executeQuery(consulta);
                resultados.next();
                
                int idUsuario = resultados.getInt(1);
                
                PuzzleDeDadosDAO.crearPuzzleJugador(idUsuario);
                
                stmt.close();
                resultados.close();
            }
            
            conection.desconectar();
        }
    }
}