/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.Usuario;
import Otros.BotonImagen;
import Otros.Respuesta;
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
                try {
                    iniciarSesion(
                            visLog.getUsuario().getText(), 
                            String.valueOf(visLog.getPass().getPassword()));
                } catch (IOException ex) {
                    Logger.getLogger(ControladorLogin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        // Obtiene la caja de texto de "Usuario"
        JTextField usuarioCampo = this.visLog.getUsuario();
        
        /*
        * Agrega un KeyListener a la caja de texto "Usuario", así se podrán
        * capturar los eventos de teclado.
        */
        usuarioCampo.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                // Si se presiona la tecla ENTER
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    try {
                        iniciarSesion(
                                visLog.getUsuario().getText(),
                                String.valueOf(visLog.getPass().getPassword()));
                    } catch (IOException ex) {
                        Logger.getLogger(ControladorLogin.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        
        // Obtiene la caja de texto de "Pass"
        JPasswordField pass = this.visLog.getPass();
        
        /*
        * Agrega un KeyListener a la caja de texto "Pass", así se podrán
        * capturar los eventos de teclado.
        */
        pass.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                // Si se presiona la tecla ENTER
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    try {
                        iniciarSesion(
                                visLog.getUsuario().getText(),
                                String.valueOf(visLog.getPass().getPassword()));
                    } catch (IOException ex) {
                        Logger.getLogger(ControladorLogin.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }
    
    public void mostrarVistaLogin() {
        this.visLog.setVisible(true);
    }
    
    public void eliminarVistaLogin() {
        this.visLog.dispose();
    }
    
    /**
     * Inicia el proceso de login, comprobando las entradas del usuario.
     * @param usuarioText Nombre de usuario ingresado en el campo de texto.
     * @param passText Contraseña ingresada en el campo de contraseña.
     */
    public void iniciarSesion(String usuarioText, String passText) throws IOException {
        if (!"".equals(usuarioText) && !"".equals(passText)){
            Usuario consultaUsuario = Usuario.existe(usuarioText);
            if (consultaUsuario != null){
                this.usuario = consultaUsuario;
                if (this.usuario.validar(usuarioText, passText)){
                    this.contPrin.setUsuarioActivo(this.usuario);
                    this.contPrin.crearControladorMenuPrincipal();
                    this.contPrin.getContMenuPrin().mostrarVistaMenuPrincipal();
                }else{
                    this.setMensaje(Respuesta.PASS_INCORRECTA.getText());
                }
            }else{
                this.setMensaje(Respuesta.USUARIO_NO_EXISTE.getText());
            }
        }else{
            this.setMensaje(Respuesta.CAMPOS_VACIOS.getText());
        }
    }
    
    public VistaLogin getVisLog() {
        return visLog;
    }
    
    public void setMensaje(String mensaje){
        this.visLog.setMensaje(mensaje);
    }
    
    public void reiniciarCampos(){
        this.visLog.setUsuario("");
        this.visLog.setPass("");
    }
    
    public Usuario getUsuario() {
        return this.usuario;
    }
}
