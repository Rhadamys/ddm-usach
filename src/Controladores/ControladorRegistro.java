/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.Dado;
import Modelos.JefeDeTerreno;
import Modelos.Usuario;
import Vistas.VistaRegistro;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
        
        this.visReg = new VistaRegistro(this.contPrin.getFuente());
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
        /*
        * Agrega un MouseListener al botón "Volver atrás", así se podrán
        * capturar los evento de mouse.
        */
        this.visReg.getVolver().addMouseListener(new MouseAdapter(){
            // Cuando se haga clic sobre el botón "Volver atrás".
            @Override
            public void mouseClicked(MouseEvent e){
                // Se instancia el controlador de login
                contPrin.crearControladorLogin();
                // Se muestra la vista de login
                contPrin.getContLog().mostrarVistaLogin();
                // Se elimina la vista de registro
                eliminarVistaRegistro();
            }
        });
        
        /*
        * Agrega un MouseListener al botón "Seleccionar jefe", así se podrán
        * capturar los evento de mouse.
        */
        this.visReg.getSeleccionarJefe().addMouseListener(new MouseAdapter(){
            // Cuando se haga clic sobre el botón "Jefe"
            @Override
            public void mouseClicked(MouseEvent e){
                // Se instancia el controlador seleccionar jefe de terreno
                contPrin.crearControladorSeleccionarJefe();
            }
        });
        
        /*
        * Agrega un MouseListener al botón de "Registrarse", así se podrán
        * capturar los evento de mouse.
        */
        this.visReg.getRegistrarse().addMouseListener(new MouseAdapter(){
            // Cuando se haga clic sobre el label "Volver atrás".
            @Override
            public void mouseClicked(MouseEvent e){
                // Se inicia el proceso de registro de usuario
                registrarUsuario(
                            visReg.getUsuario(), 
                            visReg.getPass(), 
                            visReg.getPassRepetida(),
                            jefe);
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
     */
    public void registrarUsuario(String usuario, String pass, String passRepetida, JefeDeTerreno jefe) {
        // Se comprueba que los campos estén completos (escritos)
        if(!"".equals(usuario) && !"".equals(pass) && !"".equals(passRepetida)){
            // Se comprueba que el largo del nombre de usuario sea por lo menos de 5 caracteres
            if(usuario.length() >= 5){
                // Se comprueba que el largo de la contraseña sea por lo menos de 5 caracteres
                if(pass.length() >= 5){
                    // Se comprueba que el usuario no exista previamente en los registros
                    if(!Usuario.existe(usuario)){
                        // Se comprueba que el usuario haya elegido un jefe de terreno
                        if(jefe != null){
                            // Finalmente, se comprueba que ambas contraseñas calcen
                            if(pass.equals(passRepetida)){
                                try {
                                    File archivoUsuario = new File("src\\Otros\\usuarios.txt");
                                    PrintWriter escritor;
                                    escritor = new PrintWriter(new FileWriter(archivoUsuario, true));

                                    // Se asignan los dados al jugador (aleatoriamente)
                                    ArrayList<Dado> dados = this.asignarDados();

                                    // Esto es para agregar a los registros del archivo
                                    String lineaDados = "";
                                    for(Dado dado: dados){
                                        lineaDados += ";" + dado.getClave();
                                    }

                                    // Se registra al usuario en el archivo
                                    escritor.println(usuario + ";" + pass + ";" + jefe.getClave() + lineaDados);

                                    escritor.close();

                                    JOptionPane.showMessageDialog(null, "Registro exitoso.");

                                    // Se instancia el controlador de login
                                    this.contPrin.crearControladorLogin();
                                    // Se muestra la vista de login
                                    this.contPrin.getContLog().mostrarVistaLogin();
                                    // Se elimina la vista de registro
                                    this.eliminarVistaRegistro();
                                } catch (IOException ex) {
                                    this.visReg.setMensaje("Error interno de la aplicación.");
                                }
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
    
    public ArrayList<Dado> asignarDados(){
        ArrayList<Dado> dados = new ArrayList();
        int nivelCriatura = 1;
        
        for(int i = 0; i < 15; i++){
            dados.add(Dado.getDado(nivelCriatura));
            
            if(i < 8){
                nivelCriatura = 1;
            }else if(i < 12){
                nivelCriatura = 2;
            }else if(i < 14){
                nivelCriatura = 3;
            }else{
                nivelCriatura = 4;
            }
        }
        
        return dados;
    }
}
