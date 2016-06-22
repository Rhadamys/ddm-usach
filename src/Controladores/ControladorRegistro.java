/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.JefeDeTerreno;
import Modelos.Usuario;
import Otros.BotonImagen;
import Vistas.SubVistaCuadroDialogo;
import Vistas.SubVistaInfoElemento;
import Vistas.SubVistaSeleccionarJefe;
import Vistas.VistaLogin;
import Vistas.VistaNuevaPartida;
import Vistas.VistaRegistro;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JInternalFrame;

/**
 *
 * @author mam28
 */
public final class ControladorRegistro {
    private final ControladorPrincipal contPrin;
    private final JInternalFrame quienLlama;
    private final VistaRegistro visReg;
    private final SubVistaSeleccionarJefe visSelJef;
    private JefeDeTerreno jefe;
    private Timer timerVisInfoEl;
    
    /**
     * Inicializa una nueva instancia de controlador de registro.
     * @param contPrin Controlador principal para comunicarse con otros controladores.
     * @param quienLlama Vista que llama a este controlador.
     */
    public ControladorRegistro(ControladorPrincipal contPrin, JInternalFrame quienLlama){
        this.contPrin = contPrin;
        
        this.visReg = new VistaRegistro();
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visReg);
        this.agregarListenersVistaRegistro();
        
        this.visSelJef = new SubVistaSeleccionarJefe();
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visSelJef);
        
        for(int i = 0; i < this.visSelJef.cantidadJefes(); i++){
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
            public void mouseReleased(MouseEvent e){
                cerrarVistaRegistro(false);
            }
        });
        
        /*
        * Agrega un MouseListener al botón "Seleccionar jefe", así se podrán
        * capturar los evento de mouse.
        */
        this.visReg.getSeleccionarJefe().addMouseListener(new MouseAdapter(){
            // Cuando se haga clic sobre el botón "Seleccionar jefe"
            @Override
            public void mouseReleased(MouseEvent e){
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
            public void mouseReleased(MouseEvent e){
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
        this.visSelJef.getPanelJefe(i).addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){
                mostrarVistaInfoJefe((BotonImagen) e.getComponent());
                posicionarVistaInfoCriatura(e.getX());
            }

            @Override
            public void mouseExited(MouseEvent e){
                ocultarVisInfoJefe();
            }

            @Override
            public void mouseReleased(MouseEvent e){
                setJefe((BotonImagen) e.getComponent());
                visSelJef.setVisible(false);
            }
        });
    }
    
    /**
     * Establece el jefe de terreno elegido por el usuario en la vista de
     * selección de jefe para realizar el registro.
     * @param panelJefe Jefe de terreno seleccionado.
     */
    public void setJefe(BotonImagen panelJefe) {
        this.jefe = this.visSelJef.getJefe(panelJefe);
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
            this.visReg.usuarioCorrecto();                
            try {
                if(Usuario.registrarUsuario(usuario, pass, this.jefe)){
                    this.cerrarVistaRegistro(true);
                    this.mostrarMensaje("Registro exitoso. Ahora volverás a la vista anterior.");
                }else{
                    this.visReg.setMensaje("Usuario ya existe");
                    this.visReg.usuarioErroneo();
                }
            } catch (SQLException ex) {
                this.mostrarMensaje("No se pudo completar el registro. Inténtalo nuevamente.");
            }
        }
    }
    
    public void cerrarVistaRegistro(boolean seCompletoRegistro){
        if(quienLlama instanceof VistaLogin){
            // Se instancia el controlador de login
            this.contPrin.crearControladorLogin();
            // Se muestra la vista de login
            this.contPrin.getContLog().mostrarVistaLogin();
        }else if(quienLlama instanceof VistaNuevaPartida && seCompletoRegistro){
           this.contPrin.getContNuePar().incrementarCantidadJugadores();
        }
        this.visSelJef.dispose();
        // Se elimina la vista de registro
        this.visReg.dispose();
    }
    
    public void mostrarVistaInfoJefe(BotonImagen panelJefe){
        this.visSelJef.setVisInfoEl(new SubVistaInfoElemento(
                this.visSelJef.getJefe(panelJefe)));
        
        timerVisInfoEl = new Timer();
        timerVisInfoEl.schedule(new TimerTask(){
            @Override
            public void run(){
                visSelJef.getVisInfoEl().setVisible(true);
                timerVisInfoEl.cancel();
            }
        }, 1500, 10);
    }
    
    public void posicionarVistaInfoCriatura(int x){
        this.visSelJef.getVisInfoEl().setLocation(x > 400 ? 10: 540, 100);
        this.visSelJef.repaint();
    }
    
    public void ocultarVisInfoJefe(){
        try{
            timerVisInfoEl.cancel();
        }catch(Exception e){
            // Nada
        }
        
        try{
            visSelJef.getVisInfoEl().setVisible(false);
        }catch(Exception e){
            // Nada
        }
    }
    
    /**
     * Muestra un cuadro de diálogo con un mensaje.
     * @param mensaje Mensaje que se mostrará en el cuadro de diálogo.
     */
    public void mostrarMensaje(String mensaje){
        SubVistaCuadroDialogo visMen = new SubVistaCuadroDialogo(
                "<html><center>" +mensaje + "</center></html>", "Aceptar");
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visMen);
        visMen.setVisible(true);
    }
}
