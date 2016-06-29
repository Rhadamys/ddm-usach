/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.Jugador;
import Modelos.Usuario;
import Otros.Constantes;
import Otros.Registro;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JInternalFrame;

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
    private ControladorModificarPuzzle contModPuzz;
    private ControladorInfoCriaturas contInfo;
    private ControladorBatalla contBat;
    private Usuario usuarioActivo;
    
    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Registro.iniciarRegistro();
        
        // Instancia a este controlador
        ControladorPrincipal contPrin = new ControladorPrincipal();
        contPrin.elementosPersonalizados();
        
        // Se instancian los otros controladores
        contPrin.contVisPrin = new ControladorVistaPrincipal(contPrin);
        contPrin.crearControladorLogin();
        contPrin.contLog.mostrarVistaLogin();
    }
    
    /**
     * Define la fuente personalizada que se utilizará en la aplicación
     */
    public void elementosPersonalizados(){
        try {
            //create the font to use. Specify the size!
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/Fuentes/pixel.ttf")).deriveFont(14f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/Fuentes/pixel.ttf")));
            Constantes.FUENTE_14PX = customFont;
            Constantes.FUENTE_18PX = new Font(customFont.getName(), Font.TRUETYPE_FONT, 18);
            Constantes.FUENTE_24PX = new Font(customFont.getName(), Font.TRUETYPE_FONT, 24);
            Constantes.FUENTE_36PX = new Font(customFont.getName(), Font.TRUETYPE_FONT, 36);
        }catch(IOException | FontFormatException e){
            // Nada
        }
    }
    
    /**
     * Crea una nueva instancia del controlador login
     */
    public void crearControladorLogin() {
        this.contLog = new ControladorLogin(this);
    }
    
    /**
     * Crea una nueva instancia del controlador registro
     * @param quienLlama Vista desde la que se "llama" al controlador de registro
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
     * Crea una nueva instancia del controlador mofidicar puzzle
     * @param usuario Usuario que modificará el puzzle de dados.
     */
    public void crearControladorModificarPuzzle(Usuario usuario){
        this.contModPuzz = new ControladorModificarPuzzle(this, usuario);
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
     * Devuelve el controlador de modificar puzzle
     * @return Controlador de nueva partida
     */
    public ControladorModificarPuzzle getContModPuzz() {
        return contModPuzz;
    }
    
    /**
     * Devuelve el controlador de infoCrituras
     * @return Controlador de infoCrituras
     */
    public ControladorInfoCriaturas getContInfo(){
        return contInfo;
    }
    
    /**
     * Crea una nueva instancia del controlador infoCriaturas
     */
    public void crearControladorInfoCriaturas(){
        this.contInfo = new ControladorInfoCriaturas (this);
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
