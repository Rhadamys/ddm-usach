/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.Usuario;
import Otros.BotonImagen;
import Vistas.VistaLogin;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

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
        
        this.visLog = new VistaLogin(this.contPrin.getFuentePersonalizada());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visLog);
        this.agregarListenersVistaLogin();
    }
    
    /**
     * Agrega listeners a los componentes de la vista login (visLog) y específica
     * las acciones que deben realizar al activarse dichos eventos.
     */
    public void agregarListenersVistaLogin(){
        // Obtiene el label de "Registrarse"
        JLabel registrarse = this.visLog.getRegistrarse();
        
        /*
        * Agrega un MouseListener al label "Registrarse", así se podrán capturar
        * los eventos de mouse.
        */
        registrarse.addMouseListener(new MouseAdapter(){
            // Cuando se haga clic sobre el label "Registrarse".
            @Override
            public void mouseClicked(MouseEvent e){
                contPrin.crearControladorRegistro();
                contPrin.getContReg().mostrarVistaRegistro();
                eliminarVistaLogin();
            }
        });
        
        // Obtiene el botón de "Ingrear"
        BotonImagen ingresar = this.visLog.getIngresar();
        
        /*
        * Agrega un MouseListener al botón "Ingresar", así se podrán capturar
        * los eventos de mouse.
        */
        ingresar.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                iniciarSesion(visLog.getUsuario(), visLog.getPass());
            }
        });
        
        // Obtiene la caja de texto de "Usuario"
        JTextField usuarioCampo = this.visLog.getCajaUsuario();
        
        /*
        * Agrega un KeyListener a la caja de texto "Usuario", así se podrán
        * capturar los eventos de teclado.
        */
        usuarioCampo.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                // Si se presiona la tecla ENTER
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    iniciarSesion(visLog.getUsuario(), visLog.getPass());
                }
            }
        });
        
        // Obtiene la caja de texto de "Pass"
        JPasswordField pass = this.visLog.getCajaPass();
        
        /*
        * Agrega un KeyListener a la caja de texto "Pass", así se podrán
        * capturar los eventos de teclado.
        */
        pass.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                // Si se presiona la tecla ENTER
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
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
        if (comprobarCampos()){
            try {
                Usuario consultaUsuario = Usuario.existe(usuarioText);
                if (consultaUsuario != null){
                    this.usuario = consultaUsuario;
                    if (this.usuario.validar(usuarioText, passText)){
                        this.contPrin.setUsuarioActivo(this.usuario);
                        this.contPrin.crearControladorMenuPrincipal();
                        this.contPrin.getContMenuPrin().mostrarVistaMenuPrincipal();
                        this.eliminarVistaLogin();
                    }else{
                        this.usuarioCorrecto();
                        this.passErronea();
                        this.setMensaje("Contraseña incorrecta.");
                    }
                }else{
                    this.usuarioErroneo();
                    this.passErronea();
                    this.setMensaje("Usuario no existe.");
                }
            } catch (IOException ex) {
                Logger.getLogger(ControladorLogin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            this.setMensaje("Completa todos los campos.");
        }
    }
    
    /**
     * Comprueba que se hayan llenado todos los campos requeridos.
     * @return True si los campos se han llenado, false en caso contrario.
     */
    public boolean comprobarCampos(){
        if("".equals(this.visLog.getUsuario())){
            this.usuarioErroneo();
        }else{
            this.visLog.getCajaUsuario().setImagenActual(0);
        }

        if("".equals(this.visLog.getPass())){
            this.passErronea();
        }
        
        return !"".equals(this.visLog.getUsuario()) && !"".equals(this.visLog.getPass());
    }
    
    /**
     * Pinta de rojo el fondo de la caja de texto de usuario, indicando que se debe
     * ingresar un usuario en el campo.
     */
    public void usuarioErroneo(){
        this.visLog.getCajaUsuario().setImagenActual(2);
    }
    
    /**
     * Pinta de verde el fondo de la caja de texto de usuario, indicando que el
     * usuario ingresado existe en los registros.
     */
    public void usuarioCorrecto(){
        this.visLog.getCajaUsuario().setImagenActual(1);
    }
    
    /**
     * Pinta de rojo el fondo de la caja de contraseña, indicando que la contraseña
     * ingresada es incorrecta para el usuario o no se ha ingresado una contraseña
     * válida.
     */
    public void passErronea(){
        this.visLog.getCajaPass().setImagenActual(2);
    }
    
    /**
     * Establece el texto del JLabel mensaje indicando, por ejemplo, que se
     * deben ingresar todos los campos requeridos.
     * @param mensaje Mensaje que se mostrará en el JLabel.
     */
    public void setMensaje(String mensaje){
        this.visLog.setMensaje(mensaje);
    }
}
