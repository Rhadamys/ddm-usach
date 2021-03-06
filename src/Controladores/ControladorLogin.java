/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.Usuario;
import Otros.Constantes;
import Otros.Registro;
import Otros.Reproductor;
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
    
    /**
     * Inicializa una nueva instancia de ControladorLogin.
     * @param contPrin Controlador principal para comunicarse con otros controladores.
     */
    public ControladorLogin(ControladorPrincipal contPrin){
        this.contPrin = contPrin;
        
        this.visLog = new VistaLogin();
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visLog);
        this.agregarListenersVistaLogin();
        
        Reproductor.reproducir(Constantes.M_LOGIN);
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
            public void mouseReleased(MouseEvent e){
                registrarse();
            }
        });
        
        /*
        * Agrega un MouseListener al botón "Ingresar", así se podrán capturar
        * los eventos de mouse.
        */
        this.visLog.getIngresar().addMouseListener(new MouseAdapter(){
            // Cuando se presione el botón de "Ingresar"
            @Override
            public void mouseReleased(MouseEvent e){
                // Se inicia el proceso de login
                comprobarLogIn(visLog.getUsuario(), visLog.getPass());
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
                    comprobarLogIn(visLog.getUsuario(), visLog.getPass());
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
                    comprobarLogIn(visLog.getUsuario(), visLog.getPass());
                }
            }
        });
        
        this.visLog.getSalir().addMouseListener(new MouseAdapter(){
            // Cuando se presione el botón de "Ingresar"
            @Override
            public void mouseReleased(MouseEvent e){
                contPrin.getContVisPrin().mostrarMensajeSalir();
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
    public void comprobarLogIn(String usuarioText, String passText) {
        // Se comprueba que los campos estén completos
        if (this.visLog.comprobarCampos()){
            // Se obtiene la instancia de Usuario del usuario ingresado
            Usuario usuario = Usuario.getUsuario(usuarioText);
            if(usuario != null){
                // Se comprueba que los datos ingresados sean correctos
                if (passText.equals(usuario.getPass())){
                    this.iniciarSesion(usuario);
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
    
    /**
     * Loguea al usuario en la aplicación.
     * @param usuario Usuario que se loguea.
     */
    public void iniciarSesion(Usuario usuario) {
        // Se asigna como el usuario actual al usuario obtenido
        this.contPrin.setUsuarioActivo(usuario);
        // Se instancia el controlador de menú principal
        this.contPrin.crearControladorMenuPrincipal();
        // Se muestra la vista de menú principal
        this.contPrin.getContMenuPrin().mostrarVistaMenuPrincipal();
        // Se elimina la vista de login
        this.eliminarVistaLogin();

        Registro.registrarAccion(Registro.LOGIN, usuario.getNombreJugador());
    }
    
    public void registrarse(){
        // Se instancia un controlador de registro
        contPrin.crearControladorRegistro(visLog);
        // Se muestra la vista de registro
        contPrin.getContReg().mostrarVistaRegistro();
        // Se elimina la vista de login
        eliminarVistaLogin();
}
}
