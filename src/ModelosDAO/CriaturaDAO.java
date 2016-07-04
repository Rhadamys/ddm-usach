/***********************************************************************
 * Module:  Criatura.java
 * Author:  mam28
 * Purpose: Defines the Class Criatura
 ***********************************************************************/
package ModelosDAO;

import BD.Conection.Conection;
import Modelos.Criatura;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/** @pdOid dae863aa-62cf-47e0-9a47-196272f29518 */
public class CriaturaDAO { 
    
    public static Criatura getCriatura(int id) throws SQLException{
        Conection conection = new Conection();
        if(conection.conectar()){
            String consulta = "SELECT * FROM CRIATURA WHERE ID_CRIATURA = " + id;
            Statement stmtCriatura = conection.crearConsulta();
            
            if(stmtCriatura != null){
                ResultSet resultadosCriatura = stmtCriatura.executeQuery(consulta);
                resultadosCriatura.next();

                String nombre = resultadosCriatura.getString(2);
                String informacion = resultadosCriatura.getString(3);
                String imagenC = resultadosCriatura.getString(4);
                int vida = resultadosCriatura.getInt(5);
                int ataque = resultadosCriatura.getInt(6);
                int defensa = resultadosCriatura.getInt(7);
                int nivelCriatura = resultadosCriatura.getInt(8);

                resultadosCriatura.close();
                stmtCriatura.close();
                conection.desconectar();
                
                return new Criatura(nombre, vida, ataque, defensa, nivelCriatura,
                        imagenC, informacion);
            }else{
                conection.desconectar();
                return null;
            }
        }else{
            return null;
        }
    }

    public static ArrayList<Criatura> getCriaturas() throws SQLException {
        Conection conection = new Conection();
        if(conection.conectar()){
            String consulta = "SELECT * FROM CRIATURA";
            Statement stmt = conection.crearConsulta();

            if(stmt != null){
                ResultSet resultadosCriatura = stmt.executeQuery(consulta);

                ArrayList<Criatura> criaturas = new ArrayList<Criatura>();
                while(resultadosCriatura.next()){
                        String nombre = resultadosCriatura.getString(2);
                        String informacion = resultadosCriatura.getString(3);
                        String imagenC = resultadosCriatura.getString(4);
                        int vida = resultadosCriatura.getInt(5);
                        int ataque = resultadosCriatura.getInt(6);
                        int defensa = resultadosCriatura.getInt(7);
                        int nivelCriatura = resultadosCriatura.getInt(8);
                        
                        criaturas.add(new Criatura (nombre, vida, ataque, defensa, nivelCriatura,
                        imagenC, informacion));
                }
                
                stmt.close();
                conection.desconectar();
                return criaturas;
            }
        }
        return null;
    }
    
}