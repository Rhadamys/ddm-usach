/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.Usuario;
import Vistas.VistaLogin;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author mam28
 */
public final class ControladorLogin {
    private final ControladorPrincipal contPrin;
    private final VistaLogin visLog;
    private Usuario usuario;
    
    /**
     * Inicializa una nueva instancia de ControladorLogin.
     * @param contPrin Controlador principal para comunicarse con otros controladores.
     */
    public ControladorLogin(ControladorPrincipal contPrin){
        this.contPrin = contPrin;
        
        this.visLog = new VistaLogin(this.contPrin.getFuente());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visLog);
        this.agregarListenersVistaLogin();
    }
    
    /**
     * Agrega listeners a los componentes de la vista login (visLog) y específica
     * las acciones que deben realizar al activarse dichos eventos.
     */
    public void agregarListenersVistaLogin(){
        /*
        * Agrega un MouseListener al label "Registrarse", así se podrán capturar
        * los eventos de mouse.
        */
        this.visLog.getRegistrarse().addMouseListener(new MouseAdapter(){
            // Cuando se haga clic sobre el label "Registrarse".
            @Override
            public void mouseClicked(MouseEvent e){
                // Se instancia un controlador de registro
                contPrin.crearControladorRegistro();
                // Se muestra la vista de registro
                contPrin.getContReg().mostrarVistaRegistro();
                // Se elimina la vista de login
                eliminarVistaLogin();
            }
        });
        
        /*
        * Agrega un MouseListener al botón "Ingresar", así se podrán capturar
        * los eventos de mouse.
        */
        this.visLog.getIngresar().addMouseListener(new MouseAdapter(){
            // Cuando se presione el botón de "Ingresar"
            @Override
            public void mouseClicked(MouseEvent e){
                // Se inicia el proceso de login
                iniciarSesion(visLog.getUsuario(), visLog.getPass());
            }
        });
        
        /*
        * Agrega un KeyListener a la caja de texto "Usuario", así se podrán
        * capturar los eventos de teclado.
        */
        this.visLog.getCajaUsuario().addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                // Si se presiona la tecla ENTER en la caja de usuario
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    // Se inicia el proceso de login
                    iniciarSesion(visLog.getUsuario(), visLog.getPass());
                }
            }
        });
        
        /*
        * Agrega un KeyListener a la caja de texto "Pass", así se podrán
        * capturar los eventos de teclado.
        */
        this.visLog.getCajaPass().addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                // Si se presiona la tecla ENTER en la caja de contraseña
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    // Se inicia el proceso de login
                    iniciarSesion(visLog.getUsuario(), visLog.getPass());
                }
            }
        });
    }
    
    /**
     * Se hace visible la vista en el JDesktopPane de la vista principal.
     */
    public void mostrarVistaLogin() {
        this.visLog.setVisible(true);
    }
    
    /**
     * Elimina la vista de este controlador. Esto provocará tmabién que el
     * controlador sea eliminado de la memoria.
     */
    public void eliminarVistaLogin() {
        this.visLog.dispose();
    }
    
    /**
     * Inicia el proceso de login, comprobando las entradas del usuario.
     * @param usuarioText Nombre de usuario ingresado en el campo de texto.
     * @param passText Contraseña ingresada en el campo de contraseña.
     */
    public void iniciarSesion(String usuarioText, String passText) {
        // Se comprueba que los campos estén completos
        if (this.visLog.comprobarCampos()){
            // Se comprubea si el usuario existe llamando al método estático de Usuario
            if (Usuario.existe(usuarioText)){ // Si existe
                // Se obtiene la instancia de Usuario del usuario ingresado
                this.usuario = Usuario.getUsuario(usuarioText);
                // Se comprueba que los datos ingresados sean correctos
                if (passText.equals(this.usuario.getPass())){
                    // Se asigna como el usuario actual al usuario obtenido
                    this.contPrin.setUsuarioActivo(this.usuario);
                    // Se instancia el controlador de menú principal
                    this.contPrin.crearControladorMenuPrincipal();
                    // Se muestra la vista de menú principal
                    this.contPrin.getContMenuPrin().mostrarVistaMenuPrincipal();
                    // Se elimina la vista de login
                    this.eliminarVistaLogin();
                }else{
                    this.visLog.usuarioCorrecto();
                    this.visLog.passErronea();
                    this.visLog.setMensaje("Contraseña incorrecta.");
                }
            }else{
                this.visLog.usuarioErroneo();
                this.visLog.passErronea();
                this.visLog.setMensaje("Usuario no existe.");
            }
        }else{
            this.visLog.setMensaje("Completa todos los campos.");
        }
    }
    
}
