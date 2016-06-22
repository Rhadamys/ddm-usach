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
import Vistas.SubVistaCuadroDialogo;
import Vistas.VistaModificarPuzzle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author mam28
 */
public final class ControladorModificarPuzzle {
    private final ControladorPrincipal contPrin;
    private final VistaModificarPuzzle visModPuzz;
    private final Usuario usuario;
    
    public ControladorModificarPuzzle(ControladorPrincipal contPrin, Usuario usuario){
        this.contPrin = contPrin;
        
        this.visModPuzz = new VistaModificarPuzzle(usuario.getDados());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visModPuzz);
        
        this.usuario = usuario;
        
        this.agregarListenersPanelesDadosPuzzle();
        this.agregarListenersPanelesDadosNoEnPuzzle();
        this.agregarListenersVistaModificarPuzzle();
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
}
