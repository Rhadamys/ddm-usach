/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Otros;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;


/**
 *
 * @author mam28
 */
public class Registro {
    private static FileWriter archivoEscritura;
    private static BufferedWriter objetoEscritor;
    private static PrintWriter escritor;
    
    // Login y registro
    public static final int LOGIN = 1;
    public static final int REGISTRO = 2;
    public static final int LOGOUT = 3;
    
    // Menú principal
    public static final int NUEVA_PARTIDA = 100;
    public static final int MODIFICAR_PUZZLE = 101;
    public static final int INFO_CRIATURAS = 102;
    public static final int NUEVO_TORNEO = 103;
    
    // Nueva partida
    public static final int AGREGAR_JUGADOR = 300;
    public static final int ELIMINAR_JUGADOR = 301;
    public static final int EN_EQUIPOS = 302;
    public static final int EN_SOLITARIO = 303;
    
    // Batalla
    public static final int COMENZANDO_BATALLA = 200;
    public static final int TURNO = 201;
    public static final int INVOCACION = 210;
    public static final int ATAQUE = 211;
    public static final int MOVIMIENTO = 212;
    public static final int COLOCAR_TRAMPA = 213;
    public static final int ACTIVACION_TRAMPA = 214;
    public static final int ACTIVAR_MAGIA = 215;
    public static final int JUGADOR_PIERDE = 216;
    public static final int JUGADOR_GANA = 217;
    
    public static void iniciarRegistro() throws FileNotFoundException, IOException{
        archivoEscritura = new FileWriter(new File("src/Otros/registro.txt"), true);
        objetoEscritor = new BufferedWriter(archivoEscritura);
        escritor = new PrintWriter(objetoEscritor);
        
        escritor.println("==========      INICIO DEL REGISTRO      ==========");
    }
    
    public static void registrarAccion(int numAccion, String argumentos){
        String[] args = argumentos != null ? argumentos.split(";") : null;
        
        switch(numAccion){
            // Login y registro
            case LOGIN: 
                registrar(args[0] + " ha iniciado sesión");
                break;
                        
            case LOGOUT:    
                registrar(args[0] + " ha cerrado sesión");
                break;
                            
            case REGISTRO:  
                registrar("Se ha registrado a " + args[0]);
                break;
                            
            // Menú principal
            case NUEVA_PARTIDA: 
                registrar("Creando una nueva partida");
                break;
                                
            case NUEVO_TORNEO:  
                registrar("Creando un nuevo torneo");
                break;
                                
            case MODIFICAR_PUZZLE:  
                registrar(args[0] + " está modificando su puzzle de dados");
                break;
                                    
            case INFO_CRIATURAS:    
                registrar(args[0] + " está viendo información de las criaturas del juego");
                break;
                                    
            // Nueva partida
            case AGREGAR_JUGADOR: 
                registrar(args[0] + " se ha unido a la partida");
                break;
                                    
            case ELIMINAR_JUGADOR: 
                registrar(args[0] + " ha sido removido de la partida");
                break;
                         
            case EN_EQUIPOS:    
                registrar("Se ha cambiado a modo batalla en equipos");
                break;
                                
            case EN_SOLITARIO:  
                registrar("Se ha cambiado a modo batalla en solitario");
                break;
            
            // Batalla
            case COMENZANDO_BATALLA:    
                registrar("¡La batalla ha comenzado!");
                break;
                                        
            case TURNO: 
                registrar("Es el turno de " + args[0]); 
                break;
                        
            case INVOCACION:    
                registrar(args[0] + " ha invocado un " + args[1]);
                break;
                                
            case ATAQUE:    
                registrar(args[0] + " ha atacado a " + args[1] + " con la criatura " + args[2]);
                break;
                            
            case MOVIMIENTO:    
                registrar(args[0] + " ha movido la criatura " + args[1]);
                break;
                                
            case COLOCAR_TRAMPA:    
                registrar(args[0] + " ha colocado la trampa " + args[1] + " sobre el terreno");
                break;
                                    
            case ACTIVACION_TRAMPA: 
                if(args[0].equals(args[2])){
                    registrar(args[0] + " ha activado la trampa Renacer de los Muertos. " +
                            (Boolean.getBoolean(args[1]) ? 
                                    "Ahora puede seleccionar la criatura que desee revivir." :
                                    "Sin embargo, no tiene criaturas muertas que pueda revivir."));
                }else{
                    registrar(args[0] + " ha activado la trampa " + args[1] + " del jugador " + args[2]);
                }
                break;
                                    
            case ACTIVAR_MAGIA: 
                registrar(args[0] + " ha activado la magia " + args[1]);
                break;
            
            case JUGADOR_PIERDE:    
                registrar(args[0] + " ha sido derrotado");
                break;
         
            case JUGADOR_GANA:  
                registrar("¡" + args[0] + " ha ganado la partida!");
                break;
        }
    }
    
    private static void registrar(String mensaje){
        Calendar calendario = Calendar.getInstance();
        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        int mes = calendario.get(Calendar.MONTH) + 1;
        int anio = calendario.get(Calendar.YEAR);
        int hora = calendario.get(Calendar.HOUR_OF_DAY);
        int min = calendario.get(Calendar.MINUTE);
        int seg = calendario.get(Calendar.SECOND);
        
        String cadDia = dia >= 10 ? "" + dia : "0" + dia;
        String cadMes = mes >= 10 ? "" + mes : "0" + mes;
        String cadHora = hora >= 10 ? "" + hora: "0" + hora;
        String cadMin = min >= 10 ? "" + min: "0" + min;
        String cadSeg = seg >= 10 ? "" + seg: "0" + seg;
        
        escritor.println(cadDia + "/" + cadMes + "/" + anio + "::" + cadHora + 
                ":" + cadMin + ":" + cadSeg + "=" + mensaje);
    }
    
    public static void cerrarRegistro() throws IOException{
        escritor.println("==========      FINAL DEL REGISTRO       ==========");
        escritor.println("");
        escritor.close();
        objetoEscritor.close();
        archivoEscritura.close();
    }
}
