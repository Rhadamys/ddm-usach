/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.JefeDeTerreno;
import Modelos.Usuario;
import Otros.BotonImagen;
import Vistas.VistaRegistro;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author mam28
 */
public class ControladorRegistro {
    private final ControladorPrincipal contPrin;
    private VistaRegistro visReg;
    private JefeDeTerreno jefe;
    
    /**
     * Inicializa una nueva instancia de controlador de registro.
     * @param contPrin Controlador principal para comunicarse con otros controladores.
     */
    public ControladorRegistro(ControladorPrincipal contPrin){
        this.contPrin = contPrin;
        
        this.visReg = new VistaRegistro(this.contPrin.getFuentePersonalizada());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visReg);
        this.agregarListenersVistaRegistro();
    }

    /**
     * Método que devuelve la vista de registro generada en este controlador.
     * @return Vista de registro del controlador.
     */
    public VistaRegistro getVisReg() {
        return visReg;
    }
    
    /**
     * Agrega listeners a los componentes de la vista registro (visReg) y
     * específica las acciones que deben realizar al activarse dichos eventos.
     */
    public void agregarListenersVistaRegistro(){
        // Obtiene el botón de "Volver atrás"
        JButton volverReg = this.visReg.getVolver();
        
        /*
        * Agrega un MouseListener al botón "Volver atrás", así se podrán
        * capturar los evento de mouse.
        */
        volverReg.addMouseListener(new MouseAdapter(){
            // Cuando se haga clic sobre el label "Volver atrás".
            @Override
            public void mouseClicked(MouseEvent e){
                contPrin.crearControladorLogin();
                contPrin.getContLog().mostrarVistaLogin();
                eliminarVistaRegistro();
            }
        });
        
        // Obtiene el botón de "Seleccionar jefe de terreno".
        BotonImagen seleccionarJefe = this.visReg.getSeleccionarJefe();
        
        /*
        * Agrega un MouseListener al botón "Seleccionar jefe", así se podrán
        * capturar los evento de mouse.
        */
        seleccionarJefe.addMouseListener(new MouseAdapter(){
            // Cuando se haga clic sobre el label "Volver atrás".
            @Override
            public void mouseClicked(MouseEvent e){
                try {
                    contPrin.crearControladorSeleccionarJefe();
                } catch (IOException ex) {
                    Logger.getLogger(ControladorRegistro.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        // Obtiene el botón de "Registrarse"
        BotonImagen registrarse = this.visReg.getRegistrarse();
        
        registrarse.addMouseListener(new MouseAdapter(){
            // Cuando se haga clic sobre el label "Volver atrás".
            @Override
            public void mouseClicked(MouseEvent e){
                try {
                    registrarUsuario(
                            visReg.getUsuario(), 
                            visReg.getPass(), 
                            visReg.getPassRepetida(),
                            jefe);
                } catch (IOException ex) {
                    Logger.getLogger(ControladorRegistro.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    /**
     * Hace visible la vista de registro en el JFrame principal.
     */
    public void mostrarVistaRegistro(){
        this.visReg.setVisible(true);
    }
    
    /**
     * Oculta la vista de registro en el JFrame principal.
     */
    public void eliminarVistaRegistro(){
        //this.visReg.setVisible(false);
        this.visReg.dispose();
    }
    
    /**
     * Establece el jefe de terreno elegido por el usuario en la vista de
     * selección de jefe para realizar el registro.
     * @param jefe Jefe de terreno seleccionado.
     */
    public void setJefe(JefeDeTerreno jefe) {
        this.jefe = jefe;
    }
    
    /**
     * Proceso de registro de usuario. Realiza comprobaciones previas, como que
     * se hayan completado los campos, y que las contraseñas coincidan.
     * @param usuario Usuario ingresado.
     * @param pass Contraseña ingresada.
     * @param passRepetida Confirmación de contraseña.
     * @param jefe Jefe de terreno seleccionado.
     * @throws IOException Ignorará las excepciones de tipo IOException.
     */
    public void registrarUsuario(String usuario, String pass, String passRepetida, JefeDeTerreno jefe) throws IOException{
        if(!"".equals(usuario) && !"".equals(pass) && !"".equals(passRepetida)){
            if(usuario.length() >= 5){
                if(pass.length() >= 5){
                    if(Usuario.existe(usuario) == null){
                        if(jefe != null){
                            if(pass.equals(passRepetida)){
                                File archivoUsuario = new File("src\\Otros\\usuarios.txt");
                                PrintWriter escritor = new PrintWriter(new FileWriter(archivoUsuario, true));
                                escritor.println(usuario + ";" + pass + ";" + jefe.getClave());
                                escritor.close();

                                JOptionPane.showMessageDialog(null, "Registro exitoso.");

                                this.contPrin.crearControladorLogin();
                                this.contPrin.getContLog().mostrarVistaLogin();
                                this.eliminarVistaRegistro();
                            }else{
                                this.visReg.setMensaje("Las contraseñas no coinciden.");
                            }
                        }else{
                            this.visReg.setMensaje("Selecciona un jefe de terreno.");
                        }
                    }else{
                        this.visReg.setMensaje("Usuario ya existe");
                    }
                }else{
                     this.visReg.setMensaje("La contraseña debe tener por lo menos 5 caracteres.");
                }
            }else{
                this.visReg.setMensaje("El usuario debe tener por lo menos 5 caracteres.");
            }
        }else{
            this.visReg.setMensaje("Completa todos los campos.");
        }
    }
}
