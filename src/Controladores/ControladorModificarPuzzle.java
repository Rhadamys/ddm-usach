/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.Dado;
import Modelos.PuzzleDeDados;
import Modelos.Usuario;
import Otros.BotonCheckImagen;
import Otros.Constantes;
import Otros.Registro;
import Otros.Reproductor;
import Vistas.SubVistaCuadroDialogo;
import Vistas.SubVistaInfoElemento;
import Vistas.VistaModificarPuzzle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author mam28
 */
public final class ControladorModificarPuzzle {
    private final ControladorPrincipal contPrin;
    private final VistaModificarPuzzle visModPuzz;
    private SubVistaInfoElemento visInfoDado;
    private Timer timerVisInfoDado;
    private final Usuario usuario;
    
    public ControladorModificarPuzzle(ControladorPrincipal contPrin, Usuario usuario){
        this.contPrin = contPrin;
        
        this.visModPuzz = new VistaModificarPuzzle(usuario.getDados());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visModPuzz);
        
        this.usuario = usuario;
        
        this.agregarListenersPanelesDadosPuzzle();
        this.agregarListenersPanelesDadosNoEnPuzzle();
        this.agregarListenersVistaModificarPuzzle();
        
        Registro.registrarAccion(Registro.MODIFICAR_PUZZLE, usuario.getNombreJugador());
    }
    
    public void mostrarVistaModificarPuzle(){
        this.visModPuzz.setVisible(true);
    }
    
    public void eliminarVistaModificarPuzle(){
        this.visModPuzz.dispose();
    }
    
    public void agregarListenersPanelesDadosPuzzle(){
        for(int i = 0; i < this.visModPuzz.cantidadDadosPuzzle(); i++){
            this.visModPuzz.getPanelDadoPuzzle(i).addMouseListener(new MouseAdapter(){
                @Override
                public void mouseReleased(MouseEvent e){
                    visModPuzz.verificarPanelDadoPuzzle((BotonCheckImagen) e.getComponent());
                }
                
                @Override
                public void mouseEntered(MouseEvent e){
                    mostrarVistaInfoDado(visModPuzz.getDadoEnPuzzle((BotonCheckImagen) e.getComponent()),
                            e.getComponent().getX());
                }
                
                @Override
                public void mouseExited(MouseEvent e){
                    ocultarVistaInfoDado();
                }
            });
        }
    }
    
    public void agregarListenersPanelesDadosNoEnPuzzle(){
        for(int i = 0; i < this.visModPuzz.cantidadDadosNoEnPuzzle(); i++){
            this.visModPuzz.getPanelDadoNoEnPuzzle(i).addMouseListener(new MouseAdapter(){
                @Override
                public void mouseReleased(MouseEvent e){
                    visModPuzz.verificarPanelDadoNoEnPuzzle((BotonCheckImagen) e.getComponent());
                }
                
                @Override
                public void mouseEntered(MouseEvent e){
                    mostrarVistaInfoDado(visModPuzz.getDadoNoEnPuzzle((BotonCheckImagen) e.getComponent()),
                            e.getComponent().getX());
                }
                
                @Override
                public void mouseExited(MouseEvent e){
                    ocultarVistaInfoDado();
                }
            });
        }
    }
    
    public void agregarListenersVistaModificarPuzzle(){
        this.visModPuzz.getIntercambiarDados().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                if(sePuedenIntercambiar()){
                    visModPuzz.intercambiarDados();
                }else{
                    Reproductor.reproducirEfecto(Constantes.ERROR);
                    mostrarMensaje("Los dados no son del mismo nivel o no has seleccionado un dado en cada región.");
                }
            }
        });
        
        this.visModPuzz.getGuardarCambios().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                guardarCambios();
            }
        });
        
        this.visModPuzz.getVolver().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                eliminarVistaModificarPuzle();
            }
        });
    }
    
    public boolean sePuedenIntercambiar(){
        Dado dadoASacar = this.visModPuzz.getDadoPuzzleSeleccionado();
        Dado dadoAColocar = this.visModPuzz.getDadoNoEnPuzzleSeleccionado();
        
        return !(dadoASacar == null || dadoAColocar == null || dadoASacar.getNivel() != dadoAColocar.getNivel());
    }
    
    public void guardarCambios(){
        if(PuzzleDeDados.actualizarPuzzleJugador(usuario.getId(), visModPuzz.getPuzzleModificado())){
            this.visModPuzz.puzzleGuardado();
            this.mostrarMensaje("Se han guardado los cambios.");
        }else{
            this.mostrarMensaje("Ha ocurrido un error. Inténtalo nuevamente.");
        }
    }
    
    public void mostrarMensaje(String mensaje){
        SubVistaCuadroDialogo visMen = new SubVistaCuadroDialogo("<html><center>" + mensaje + "</center></html>",
                "Aceptar");
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visMen);
        visMen.setVisible(true);
    }
    
    /**
     * Muestra la vista de información del dado señalado.
     * @param dado Dado para el que se creará la vista.
     * @param x Posición x del botón en la vista (en pixeles).
     */
    public void mostrarVistaInfoDado(Dado dado, int x){
        // Instancia una nueva vista de información de elemento.
        this.visInfoDado = new SubVistaInfoElemento(dado);
        
        // Agrega la vista a la vista batalla.
        this.visModPuzz.add(this.visInfoDado, 0);

        // Crea un timer para hacer visible la vista luego de 1 segundo.
        this.timerVisInfoDado = new Timer();
        this.timerVisInfoDado.schedule(new TimerTask(){
            @Override
            public void run(){
                visInfoDado.setVisible(true);
                visInfoDado.setLocation(x > 400 ? 10: 540, 100);
                contPrin.getContVisPrin().getVisPrin().repaint();
                
                this.cancel();
                timerVisInfoDado.cancel();
            }
        }, 1000, 1);
    }
    
    /**
     * Oculta la vista de información de elemento. Esto se produce cuando el mouse
     * sale del botón.
     */
    public void ocultarVistaInfoDado(){
        if(this.visInfoDado != null){
            try{
                // Se cancela el timer que mostrará la vista (en caso de que esté
                // programado que se muestre la vista).
                this.timerVisInfoDado.cancel();
            }catch(Exception e){
                System.out.println("*** SE HA PRODUCIDO UN ERROR *** Información:  " + e);
            }
            
            // Oculta la vista
            this.visInfoDado.setVisible(false);
            
            this.contPrin.getContVisPrin().getVisPrin().repaint();
        }
    }
}
