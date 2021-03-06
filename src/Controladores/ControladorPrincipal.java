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
import Otros.Reproductor;
import Vistas.VistaIntroduccion;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import javafx.stage.Stage;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;

/**
 *
 * @author mam28
 */
public class ControladorPrincipal extends Application{
    private ControladorVistaPrincipal contVisPrin;
    private ControladorLogin contLog;
    private ControladorRegistro contReg;
    private ControladorMenuPrincipal contMenuPrin;
    private ControladorNuevaPartida contNuePar;
    private ControladorTorneo contTor;
    private ControladorModificarPuzzle contModPuzz;
    private ControladorInfoCriaturas contInfo;
    private ControladorBatalla contBat;
    private Usuario usuarioActivo;
    
    @Override
    public void start(Stage primaryStage) {
        // Nada
    }
    
    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Registro.iniciarRegistro();
        
        // Instancia a este controlador
        ControladorPrincipal contPrin = new ControladorPrincipal();
        contPrin.elementosPersonalizados();
        contPrin.contVisPrin = new ControladorVistaPrincipal(contPrin);
        
        contPrin.mostrarVistaIntro();
    }
    
    /**
     * Muestra el video de introducción del juego y agrega los listeners a la vista
     * para poder saltar la intro al presionar cualquier tecla.
     */
    public void mostrarVistaIntro(){
        VistaIntroduccion visIntro = new VistaIntroduccion();
        contVisPrin.getVisPrin().agregarVista(visIntro);
        visIntro.getPanel().requestFocus();

        Timer timerAinmacion = new Timer();
        timerAinmacion.schedule(new TimerTask(){
            @Override
            public void run(){
                mostrarLogin();
                visIntro.cerrarVista();
                this.cancel();
                timerAinmacion.cancel();
            }
        }, 51000, 1);

        visIntro.getPanel().addKeyListener(new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent e){
                mostrarLogin();
                visIntro.cerrarVista();
                timerAinmacion.cancel();
            }
        });
    }
    
    /**
     * Instancia y muestra la vista de login.
     */
    public void mostrarLogin(){
        this.crearControladorLogin();
        this.contLog.mostrarVistaLogin();
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
            
            Toolkit toolKit = Toolkit.getDefaultToolkit();
            Constantes.CUR_NORMAL = toolKit.createCustomCursor(new ImageIcon(getClass().getResource(
                "/Imagenes/Cursores/normal.png")).getImage(), new Point(2,2), "normal");
            Constantes.CUR_JUG_1 = toolKit.createCustomCursor(new ImageIcon(getClass().getResource(
                "/Imagenes/Cursores/jug_1.png")).getImage(), new Point(2,2), "jug_1");
            Constantes.CUR_JUG_2 = toolKit.createCustomCursor(new ImageIcon(getClass().getResource(
                "/Imagenes/Cursores/jug_2.png")).getImage(), new Point(2,2), "jug_2");
            Constantes.CUR_JUG_3 = toolKit.createCustomCursor(new ImageIcon(getClass().getResource(
                "/Imagenes/Cursores/jug_3.png")).getImage(), new Point(2,2), "jug_3");
            Constantes.CUR_JUG_4 = toolKit.createCustomCursor(new ImageIcon(getClass().getResource(
                "/Imagenes/Cursores/jug_4.png")).getImage(), new Point(2,2), "jug_4");
            
            Constantes.CURSOR = Constantes.CUR_NORMAL;
        }catch(IOException | FontFormatException e){
            System.out.println("*** SE HA PRODUCIDO UN ERROR *** Información:  " + e);
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
     * @param quienLlama Vista desde la que se "llama" a este controlador
     */
    public void crearControladorNuevaPartida(JInternalFrame quienLlama){
        this.contNuePar = new ControladorNuevaPartida(this, quienLlama);
    }
    
    /**
     * Crea una nueva instancia del controlador nuevo torneo
     */
    public void crearControladorTorneo(){
        this.contTor = new ControladorTorneo(this);
    }
    
    /**
     * Crea una nueva instancia del controlador mofidicar puzzle
     * @param usuario Usuario que modificará el puzzle de dados.
     */
    public void crearControladorModificarPuzzle(Usuario usuario){
        this.contModPuzz = new ControladorModificarPuzzle(this, usuario);
    }
    
    /**
     * Crea una nueva instancia del controlador infoCriaturas
     */
    public void crearControladorInfoCriaturas(){
        this.contInfo = new ControladorInfoCriaturas (this);
    }
    
    /**
     * Crea una nueva instancia del controlador batalla
     * @param jugadores Jugadores de la partida
     * @param esEnEquipos Indica si la partida es en equipos.
     * @param esTorneo Indica si la partida pertenece a un torneo.
     */
    public void crearControladorBatalla(ArrayList<Jugador> jugadores, boolean esEnEquipos, boolean esTorneo){
        this.contBat = new ControladorBatalla(this, jugadores, esEnEquipos, esTorneo);
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
     * Devuelve el controlador de torneo
     * @return Controlador de nueva partida
     */
    public ControladorTorneo getContTor() {
        return contTor;
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
