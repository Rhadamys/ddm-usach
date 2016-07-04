/***********************************************************************
 * Module:  JefeDeTerreno.java
 * Author:  mam28
 * Purpose: Defines the Class JefeDeTerreno
 ***********************************************************************/
package ModelosDAO;

import BD.Conection.Conection;
import Modelos.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/** @pdOid ba01c964-70b9-429b-9412-0cfa461bb9c0 */
public class JefeDeTerrenoDAO {
    public static ArrayList<JefeDeTerreno> getJefes() throws SQLException {
        Conection conection = new Conection();
        if(conection.conectar()){
            String consulta = "SELECT * FROM JEFEDETERRENO";
            Statement stmt = conection.crearConsulta();

            if(stmt != null){
                ResultSet resultados = stmt.executeQuery(consulta);

                ArrayList<JefeDeTerreno> jefes = new ArrayList<JefeDeTerreno>();
                while(resultados.next()){
                 int idJefe = resultados.getInt(1);
                    String nombre = resultados.getString(2);
                    String informacion = resultados.getString(3);
                    String imagenJ = resultados.getString(4);
                    int vida = resultados.getInt(5);
                    double incVida = resultados.getBigDecimal(6).doubleValue();
                    double incAtaque = resultados.getBigDecimal(7).doubleValue();
                    double incDefensa = resultados.getBigDecimal(8).doubleValue();

                    jefes.add(new JefeDeTerreno(idJefe, nombre, informacion, imagenJ,
                            vida, incVida, incAtaque, incDefensa));
                }

                return jefes;
            }
        }

        return null;  
    }
    
    public static JefeDeTerreno getJefe(int id) throws SQLException{
        Conection conection = new Conection();
        if(conection.conectar()){
            String consulta = "SELECT * FROM JEFEDETERRENO WHERE ID_JEFEDETERRENO = " + id;
            Statement stmt = conection.crearConsulta();

            if(stmt != null){
                ResultSet resultados = stmt.executeQuery(consulta);
                resultados.next();
                
                int idJefe = resultados.getInt(1);
                String nombre = resultados.getString(2);
                String informacion = resultados.getString(3);
                String imagenJ = resultados.getString(4);
                int vida = resultados.getInt(5);
                double incVida = resultados.getBigDecimal(6).doubleValue();
                double incAtaque = resultados.getBigDecimal(7).doubleValue();
                double incDefensa = resultados.getBigDecimal(8).doubleValue();
                
                return new JefeDeTerreno(idJefe, nombre, informacion, imagenJ,
                            vida, incVida, incAtaque, incDefensa);
            }
        }

        return null;  
    }
}