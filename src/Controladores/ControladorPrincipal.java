/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.Jugador;
import Modelos.Usuario;
import Vistas.SubVistaCuadroDialogo;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author mam28
 */
public class ControladorPrincipal {
    private ControladorVistaPrincipal contVisPrin;
    private ControladorLogin contLog;
    private ControladorRegistro contReg;
    private ControladorMenuPrincipal contMenuPrin;
    private ControladorNuevaPartida contNuePar;
    private ControladorBatalla contBat;
    private Usuario usuarioActivo;
    private Font fuente;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        // Instancia a este controlador
        ControladorPrincipal contPrin = new ControladorPrincipal();
        contPrin.elementosPersonalizados();
        
        // Se instancian los otros controladores
        contPrin.contVisPrin = new ControladorVistaPrincipal(contPrin);
        contPrin.crearControladorLogin();
        contPrin.contLog.mostrarVistaLogin();;
    }
    
    /**
     * Define la fuente personalizada que se utilizará en la aplicación
     */
    public void elementosPersonalizados(){
        try {
            //create the font to use. Specify the size!
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src\\Fuentes\\pixel.ttf")).deriveFont(14f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src\\Fuentes\\pixel.ttf")));
            this.fuente = customFont;
        }catch(IOException | FontFormatException e){
            e.printStackTrace();
        }
        
    }

    /**
     * Devuelve la fuente personalizada de la aplicación
     * @return Tipografía personalizada
     */
    public Font getFuente() {
        return fuente;
    }
    
    /**
     * Crea una nueva instancia del controlador login
     */
    public void crearControladorLogin() {
        this.contLog = new ControladorLogin(this);
    }
    
    /**
     * Crea una nueva instancia del controlador registro
     */
    public void crearControladorRegistro(JInternalFrame quienLlama){
        this.contReg = new ControladorRegistro(this, quienLlama);
    }
    
    /**
     * Crea una nueva instancia del controlador de menú principal
     */
    public void crearControladorMenuPrincipal(){
        this.contMenuPrin = new ControladorMenuPrincipal(this);
    }
    
    /**
     * Crea una nueva instancia del controlador nueva partida
     */
    public void crearControladorNuevaPartida(){
        this.contNuePar = new ControladorNuevaPartida(this);
    }
    
    /**
     * Crea una nueva instancia del controlador batalla
     * @param jugadores Jugadores de la partida
     */
    public void crearControladorBatalla(ArrayList<Jugador> jugadores){
        this.contBat = new ControladorBatalla(this, jugadores);
    }
    
    /**
     * Devuelve el controlador de vista principal
     * @return Controlador de vista principal
     */
    public ControladorVistaPrincipal getContVisPrin() {
        return contVisPrin;
    }

    /**
     * Devuelve el controlador de login
     * @return Controlador de login
     */
    public ControladorLogin getContLog() {
        return contLog;
    }

    /**
     * Devuelve el controlador de registro
     * @return Controlador de registro
     */
    public ControladorRegistro getContReg() {
        return contReg;
    }

    /**
     * Devuelve el controlador de menú principal
     * @return Controlador de menú principal
     */
    public ControladorMenuPrincipal getContMenuPrin() {
        return contMenuPrin;
    }

    /**
     * Devuelve el controlador de nueva partida
     * @return Controlador de nueva partida
     */
    public ControladorNuevaPartida getContNuePar() {
        return contNuePar;
    }

    /**
     * Devuelve el controlador de batalla
     * @return Controlador de batalla
     */
    public ControladorBatalla getContBat() {
        return contBat;
    }
    
    /**
     * Devuelve el usuario activo de la aplicación (logueado)
     * @return Usuario activo de la aplicación
     */
    public Usuario getUsuarioActivo() {
        return usuarioActivo;
    }

    /**
     * Define el usuario activo de la aplicación (logueado)
     * @param usuarioActivo Instancia de usuario
     */
    public void setUsuarioActivo(Usuario usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
    }    
    
}
