/***********************************************************************
 * Module:  Dado.java
 * Author:  mam28
 * Purpose: Defines the Class Dado
 ***********************************************************************/
package ModelosDAO;

import BD.Conection.Conection;
import Modelos.Criatura;
import Modelos.Dado;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** @pdOid 62ea5a63-657a-44c3-a1da-852adcc35713 */
public class DadoDAO {
    
    public static Dado getDado(int id) throws SQLException{
        Conection conection = new Conection();
        if(conection.conectar()){
            String consulta = "SELECT * FROM DADO WHERE ID_DADO = " + id;
            Statement stmtDado = conection.crearConsulta();

            if(stmtDado != null){
                ResultSet resultadosDado = stmtDado.executeQuery(consulta);
                resultadosDado.next();
                
                int idCriaturaDado = resultadosDado.getInt(2);
                int nivelDado = resultadosDado.getInt(3);
                int cara1 = resultadosDado.getInt(4);
                int cara2 = resultadosDado.getInt(5);
                int cara3 = resultadosDado.getInt(6);
                int cara4 = resultadosDado.getInt(7);
                int cara5 = resultadosDado.getInt(8);
                int cara6 = resultadosDado.getInt(9);

                int[] caras = {cara1, cara2, cara3, cara4, cara5, cara6};

                Criatura criaturaDado = CriaturaDAO.getCriatura(idCriaturaDado);
                             
                resultadosDado.close();
                stmtDado.close();
                conection.desconectar();
                
                return new Dado(criaturaDado, nivelDado, caras); 
            }else{
                conection.desconectar();
                return null;
            }
        }else{
            return null;
        }
    }
    
    public static Dado getDadoNivel(int nivel) throws SQLException {
        Conection conection = new Conection();
        if(conection.conectar()){
            String consulta = "SELECT * FROM DADO WHERE NIVEL = " + nivel;
            Statement stmtDado = conection.crearConsulta();

            if(stmtDado != null){
                ResultSet resultadosDado = stmtDado.executeQuery(consulta);
                resultadosDado.next();
                
                int idCriaturaDado = resultadosDado.getInt(2);
                int nivelDado = resultadosDado.getInt(3);
                int cara1 = resultadosDado.getInt(4);
                int cara2 = resultadosDado.getInt(5);
                int cara3 = resultadosDado.getInt(6);
                int cara4 = resultadosDado.getInt(7);
                int cara5 = resultadosDado.getInt(8);
                int cara6 = resultadosDado.getInt(9);

                int[] caras = {cara1, cara2, cara3, cara4, cara5, cara6};

                Criatura criaturaDado = CriaturaDAO.getCriatura(idCriaturaDado);
                
                resultadosDado.close();
                stmtDado.close();
                conection.desconectar();
                
                return new Dado(criaturaDado, nivelDado, caras); 
            }else{
                conection.desconectar();
                return null;
            }
        }else{
            return null;
        }
    }
}