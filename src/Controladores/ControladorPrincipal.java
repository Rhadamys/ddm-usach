/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.Usuario;
import Vistas.CompMensaje;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
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
    private ControladorSeleccionarJefe contSelJef;
    private ControladorNuevaPartida contNuePar;
    private ControladorBatalla contBat;
    private Usuario usuarioActivo;
    private Font fuentePersonalizada;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        // Instancia a este controlador
        ControladorPrincipal contPrin = new ControladorPrincipal();
        contPrin.crearFuentePersonalizada();
        
        // Se instancian los otros controladores
        contPrin.contVisPrin = new ControladorVistaPrincipal(contPrin);
        contPrin.crearControladorLogin();
        contPrin.contLog.mostrarVistaLogin();
    }
    
    /**
     * Muestra un mensaje con las opciones si / no solicitando al usuario que
     * confirme si desea salir de la aplicación.
     * @return Opcion elegida por el usuario en el JOptionPane. 
     */
    public int salir(){
//        return JOptionPane.showConfirmDialog(
//                null,
//                "¿Estás seguro que deseas salir?",
//                "Salir",
//                JOptionPane.YES_NO_OPTION);
        return CompMensaje.mostrarMensaje("Hola", "Adiós", this);
    }
     
    public void crearFuentePersonalizada(){
        try {
            //create the font to use. Specify the size!
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src\\Fuentes\\pixel.ttf")).deriveFont(14f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src\\Fuentes\\pixel.ttf")));
            this.fuentePersonalizada = customFont;
        }catch(IOException | FontFormatException e){
            e.printStackTrace();
        }
    }

    public Font getFuentePersonalizada() {
        return fuentePersonalizada;
    }
    
    public void crearControladorLogin() {
        this.contLog = new ControladorLogin(this);
    }
    
    public void crearControladorRegistro(){
        this.contReg = new ControladorRegistro(this);
    }
    
    /**
     * Crea una nueva instancia del controlador de menú principal en el
     * controlador principal.
     */
    public void crearControladorMenuPrincipal(){
        this.contMenuPrin = new ControladorMenuPrincipal(this);
    }
    
    public void crearControladorSeleccionarJefe() throws IOException{
        this.contSelJef = new ControladorSeleccionarJefe(this);
    }
    
    public void crearControladorNuevaPartida(){
        this.contNuePar = new ControladorNuevaPartida(this);
    }
    
    public void crearControladorBatalla(){
        this.contBat = new ControladorBatalla(this);
    }
    
    public ControladorVistaPrincipal getContVisPrin() {
        return contVisPrin;
    }

    public ControladorLogin getContLog() {
        return contLog;
    }

    public ControladorRegistro getContReg() {
        return contReg;
    }

    public ControladorMenuPrincipal getContMenuPrin() {
        return contMenuPrin;
    }

    public ControladorSeleccionarJefe getContSelJef() {
        return contSelJef;
    }

    public ControladorNuevaPartida getContNuePar() {
        return contNuePar;
    }

    public ControladorBatalla getContBat() {
        return contBat;
    }
    
    public Usuario getUsuarioActivo() {
        return usuarioActivo;
    }

    public void setUsuarioActivo(Usuario usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
    }    
    
}
