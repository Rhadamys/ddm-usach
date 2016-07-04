/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelosDAO;

import BD.Conection.Conection;
import Modelos.Dado;
import Modelos.JefeDeTerreno;
import Modelos.Jugador;
import Modelos.PersonajeNoJugable;
import Modelos.PuzzleDeDados;
import Modelos.Usuario;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Veronica
 */
public class JugadorDAO {
    
    /**
     * Entrega una lista con todos los jugadores registrados en la base de datos.
     * @return Lista de jugadores.
     * @throws SQLException 
     */
    public static ArrayList<Jugador> getJugadores() throws SQLException {
        Conection conection = new Conection();
        if(conection.conectar()){
            String consulta = "SELECT * FROM JUGADOR";
            Statement stmt = conection.crearConsulta();

            if(stmt != null){
                ResultSet resultados = stmt.executeQuery(consulta);

                ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
                while(resultados.next()){
                    int id = resultados.getInt(1);
                    String user = resultados.getString(2);
                    String password = resultados.getString(3);
                    boolean esHumano = resultados.getBoolean(4);
                    int jefeDeTerreno = resultados.getInt(5);
                    int partJug = resultados.getInt(6);
                    int partGan = resultados.getInt(7);

                    PuzzleDeDados puzzleJugador = PuzzleDeDadosDAO.getPuzzle(id);

                    JefeDeTerreno jefeJugador = JefeDeTerrenoDAO.getJefe(jefeDeTerreno);

                    for(Dado dado: puzzleJugador.getDados()){
                        dado.getCriatura().aumentarAtaque((int) (((double) dado.getCriatura().getAtaque()) * jefeJugador.getIncAtaque()));
                        dado.getCriatura().aumentarDefensa((int) (((double) dado.getCriatura().getDefensa()) * jefeJugador.getIncDefensa()));
                        dado.getCriatura().aumentarVidaMaxima((int) (((double) dado.getCriatura().getVida()) * jefeJugador.getIncVida()));
                    }

                    if(esHumano){
                        jugadores.add(new Usuario(id, user, password, jefeJugador, puzzleJugador, partJug, partGan));
                    }else{
                        jugadores.add(new PersonajeNoJugable(id, user, jefeJugador, puzzleJugador, partJug, partGan));
                    }
                }

                resultados.close();
                stmt.close();
                conection.desconectar();

                return jugadores;
            }

            conection.desconectar();
            return null;
        }
        else
        {
            return null;
        }
    }
    
    public static void actualizarPartidaGanada (int idJugador, int partidasGanadas)throws SQLException{
        Conection conection = new Conection();
        if(conection.conectar()){
            String consulta = "UPDATE JUGADOR SET PARTIDAS_GANADAS="+partidasGanadas+ "WHERE ID_JUGADOR ="+ idJugador;
            Statement stmtPartGanadas = conection.crearConsulta();
            stmtPartGanadas.executeUpdate(consulta);
            stmtPartGanadas.close();
            conection.desconectar();
        }
    }
    
    public static void actualizarPartidaJugada (int idJugador, int partidasJugadas) throws SQLException{
        Conection conection = new Conection();
        if(conection.conectar()){
            String consulta = "UPDATE JUGADOR SET PARTIDAS_JUGADAS=" + partidasJugadas + "WHERE ID_JUGADOR ="+ idJugador;
            Statement stmtPartJugadas = conection.crearConsulta();
            stmtPartJugadas.executeUpdate(consulta);
            stmtPartJugadas.close();
            conection.desconectar();
        }
    }
}
