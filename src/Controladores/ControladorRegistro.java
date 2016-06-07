/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.Dado;
import Modelos.JefeDeTerreno;
import Modelos.Usuario;
import Otros.BotonImagen;
import Vistas.SubVistaCuadroDialogo;
import Vistas.SubVistaSeleccionarJefe;
import Vistas.VistaLogin;
import Vistas.VistaRegistro;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author mam28
 */
public class ControladorRegistro {
    private final ControladorPrincipal contPrin;
    private final JInternalFrame quienLlama;
    private final VistaRegistro visReg;
    private final SubVistaSeleccionarJefe visSelJef;
    private JefeDeTerreno jefe;
    
    /**
     * Inicializa una nueva instancia de controlador de registro.
     * @param contPrin Controlador principal para comunicarse con otros controladores.
     * @param quienLlama Vista que llama a este controlador.
     */
    public ControladorRegistro(ControladorPrincipal contPrin, JInternalFrame quienLlama){
        this.contPrin = contPrin;
        
        this.visReg = new VistaRegistro(this.contPrin.getFuente());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visReg);
        this.agregarListenersVistaRegistro();
        
        this.visSelJef = new SubVistaSeleccionarJefe(this.contPrin.getFuente());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visSelJef);
        
        for(int i = 0; i < this.visSelJef.getPanelesJefes().size(); i++){
            this.agregarListenersVistaSeleccionarJefe(i);
        }
        
        this.quienLlama = quienLlama;
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
                cerrarVistaRegistro();
            }
        });
        
        /*
        * Agrega un MouseListener al botón "Seleccionar jefe", así se podrán
        * capturar los evento de mouse.
        */
        this.visReg.getSeleccionarJefe().addMouseListener(new MouseAdapter(){
            // Cuando se haga clic sobre el botón "Seleccionar jefe"
            @Override
            public void mouseClicked(MouseEvent e){
                // Se muestra la vista de selección de jefe de terreno
                visSelJef.toFront();
                visSelJef.setVisible(true);
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
    
    public void agregarListenersVistaSeleccionarJefe(int i){
        this.visSelJef.getPanelesJefes().get(i).addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){
                visSelJef.mostrarInformacionJefe(visSelJef.getPanelesJefes().indexOf((BotonImagen) e.getComponent()));
            }

            @Override
            public void mouseExited(MouseEvent e){
                visSelJef.borrarCampos();
            }

            @Override
            public void mouseClicked(MouseEvent e){
                setJefe(JefeDeTerreno.getJefe(visSelJef.getJefes().get(visSelJef.getPanelesJefes().indexOf((BotonImagen) e.getComponent())).getNomArchivoImagen()));
                visSelJef.setVisible(false);
            }
        });
    }
    
    /**
     * Establece el jefe de terreno elegido por el usuario en la vista de
     * selección de jefe para realizar el registro.
     * @param jefe Jefe de terreno seleccionado.
     */
    public void setJefe(JefeDeTerreno jefe) {
        this.jefe = jefe;
        this.visReg.getIconoJefe().setImagen("/Imagenes/Jefes/" + jefe.getNomArchivoImagen() + ".png");
        this.visReg.getIconoJefe().setToolTipText(jefe.getNombre());
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
        if(this.visReg.comprobarCampos()){
            // Se comprueba que el usuario no exista previamente.
            if(!Usuario.existe(usuario)){
                this.visReg.usuarioCorrecto();
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
                    escritor.println(usuario + ";" + pass + ";" + jefe.getNomArchivoImagen() + lineaDados);

                    escritor.close();

                    this.cerrarVistaRegistro();
                    
                    SubVistaCuadroDialogo visMen = new SubVistaCuadroDialogo(
                            "<html><center>Registro exitoso. Ahora volverás a la<br>"
                            + "vista anterior.</center></html>",
                            "Aceptar", this.contPrin.getFuente(), -1);
                    this.contPrin.getContVisPrin().getVisPrin().agregarVista(visMen);
                    visMen.setVisible(true);
                } catch (IOException ex) {
                    this.visReg.setMensaje("Error interno de la aplicación.");
                }
            }else{
                this.visReg.setMensaje("Usuario ya existe");
                this.visReg.usuarioErroneo();
            }
        }
    }
    
    public ArrayList<Dado> asignarDados(){
        ArrayList<Dado> dados = new ArrayList();
        int nivelCriatura = 1;
        
        for(int i = 0; i < 15; i++){
            if(i < 8){
                nivelCriatura = 1;
            }else if(i < 12){
                nivelCriatura = 2;
            }else if(i < 14){
                nivelCriatura = 3;
            }else{
                nivelCriatura = 4;
            }
            
            dados.add(Dado.getDado(nivelCriatura));
        }
        
        return dados;
    }
    
    public void cerrarVistaRegistro(){
        if(quienLlama instanceof VistaLogin){
            // Se instancia el controlador de login
            this.contPrin.crearControladorLogin();
            // Se muestra la vista de login
            this.contPrin.getContLog().mostrarVistaLogin();
        }
        this.contPrin.getContVisPrin().getVisPrin().eliminarVista(visSelJef);
        this.visSelJef.dispose();
        // Se elimina la vista de registro
        this.contPrin.getContVisPrin().getVisPrin().eliminarVista(visReg);
        this.visReg.dispose();
    }
}
