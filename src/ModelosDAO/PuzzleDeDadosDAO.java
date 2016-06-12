/***********************************************************************
 * Module:  PuzleDeDados.java
 * Author:  mam28
 * Purpose: Defines the Class PuzleDeDados
 ***********************************************************************/
package ModelosDAO;

import BD.Conection.Conection;
import Modelos.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/** @pdOid 859bcc3d-e128-4446-8809-e9f0961ece7a */
public class PuzzleDeDadosDAO {
    
    public static PuzzleDeDados getPuzzle(int idJugador) throws SQLException{
        Conection conection = new Conection();
        if(conection.conectar()){
            String consulta = "SELECT * FROM PUZZLEDEDADOS WHERE ID_JUGADOR = " + idJugador;
            Statement stmtPuzzle = conection.crearConsulta();        

            if(stmtPuzzle != null){
                ResultSet resultadosPuzzle = stmtPuzzle.executeQuery(consulta);            

                ArrayList<Dado> dadosJugador = new ArrayList();
                while(resultadosPuzzle.next()){
                    int idRegPuzzDado = resultadosPuzzle.getInt(1);
                    int idDadoPuzzle = resultadosPuzzle.getInt(3);
                    boolean paraJugar = resultadosPuzzle.getBoolean(4);
                    
                    Dado dadoPuzzle = DadoDAO.getDado(idDadoPuzzle);
                    dadoPuzzle.setIdRegPuzzDado(idRegPuzzDado);
                    dadoPuzzle.setParaJugar(paraJugar);

                    dadosJugador.add(dadoPuzzle);
                }

                resultadosPuzzle.close();
                stmtPuzzle.close();
                conection.desconectar();
                
                return new PuzzleDeDados(dadosJugador);
            }else{
                conection.desconectar();
                return null;
            }
        }else{
            return null;
        }
    }
    
    public static void crearPuzzleJugador(int idJugador) throws SQLException{   
        Conection conection = new Conection();
        if(conection.conectar()){
            int nivelDado = 1;
            for(int i = 0; i < 15; i++){
                if(i < 8){
                    nivelDado = 1;
                }else if(i < 12){
                    nivelDado = 2;
                }else if(i < 14){
                    nivelDado = 3;
                }else{
                    nivelDado = 4;
                }

                String consulta = "SELECT ID_DADO FROM GRUPO2.DADO WHERE NIVEL = " + nivelDado;
                Statement stmtDado = conection.crearConsulta();

                if(stmtDado != null){
                    ResultSet resultados = stmtDado.executeQuery(consulta);

                    ArrayList<Integer> idDados = new ArrayList();
                    while(resultados.next()){
                        idDados.add(resultados.getInt(1));
                    }

                    consulta = "INSERT INTO GRUPO2.PUZZLEDEDADOS (ID_JUGADOR, ID_DADO, PARAJUGAR) VALUES (" + idJugador + ", "
                            + idDados.get(new Random().nextInt(idDados.size())) + ", true)";                    
                    Statement stmtPuzzle = conection.crearConsulta();

                    if(stmtPuzzle != null){
                        stmtPuzzle.executeUpdate(consulta);
                        stmtPuzzle.close();
                    }

                    stmtDado.close();
                    resultados.close();
                }
            }
        }          
    }
    
    public static void actualizarPuzzleJugador(int idJugador, ArrayList<Dado> dados) throws SQLException{
        Conection conection = new Conection();
        if(conection.conectar()){
            for(Dado dado: dados){
                String consulta = "UPDATE PUZZLEDEDADOS SET PARAJUGAR = " + dado.isParaJugar() + " WHERE " +
                        "ID_JUGADOR = " + idJugador + " AND ID_REGISTRODADO = " + dado.getIdRegPuzzDado() + " AND " +
                        "ID_DADO = " + dado.getId();
                Statement stmtPuzzle = conection.crearConsulta();    

                if(stmtPuzzle != null){
                    stmtPuzzle.executeUpdate(consulta);
                    stmtPuzzle.close();
                }
            }
            
            conection.desconectar();
        }
    }
}