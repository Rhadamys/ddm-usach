/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.Jugador;
import Modelos.Usuario;
import Vistas.CompInfoJug;
import Vistas.CompSelEquipos;
import Vistas.VistaNuevaPartida;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 *
 * @author mam28
 */
public class ControladorNuevaPartida {
    private final ControladorPrincipal contPrin;
    private VistaNuevaPartida visNuePar;
    private ArrayList<Jugador> jugadores;
    private String[] prueba = {"mario", "metodos4", "metodos2", "usuario3"};
    
    ControladorNuevaPartida(ControladorPrincipal contPrin) {
        this.contPrin = contPrin;
               
        this.visNuePar = new VistaNuevaPartida(this.contPrin.getFuente());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visNuePar);
        this.agregarListenersVistaNuevaPartida();
        
        this.jugadores = new ArrayList();
        
        this.agregarJugador(Usuario.getUsuario(prueba[0]));
        this.agregarJugador(Usuario.getUsuario(prueba[1]));
    }
    
    public void mostrarVistaNuevaPartida(){
        this.visNuePar.setVisible(true);
    }
    
    public void agregarListenersVistaNuevaPartida(){
        this.visNuePar.getAgregar().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                agregarJugador(Usuario.getUsuario(prueba[visNuePar.getVistasInfoJug().size()]));
            }
        });
        
        this.visNuePar.getEnEquipos().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                enEquipos();
            }
        });
        
        this.visNuePar.getVolver().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                volver();
            }
        });
        
        this.visNuePar.getComenzar().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                comenzarPartida();
            }
        });
    }
    
    public void agregarListenersVistaInfoJugador(CompInfoJug visInfoJug){
        visInfoJug.getEliminar().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                eliminarJugador(visNuePar.getVistasInfoJug().indexOf((CompInfoJug)
                        e.getComponent().getParent()));
            }
        });
    }
    
    public void agregarJugador(Jugador jug){
        this.jugadores.add(jug);
        this.visNuePar.agregarVistaInfoJugador(jug);
        this.agregarListenersVistaInfoJugador(this.visNuePar.getVistasInfoJug()
                .get(this.visNuePar.getVistasInfoJug().size() - 1));
        
        try{
            this.visNuePar.getVisSelEq().agregarJugador(jug);
            this.agregarListenersVistaSeleccionEquipos(
                    this.visNuePar.getVisSelEq().getIconosJugadores().size() - 1);
        }catch(Exception e){
            // Nada
        }
    }
    
    public void eliminarJugador(int i){
        if(this.visNuePar.getVistasInfoJug().size() > 2){
            this.jugadores.remove(i);
            this.visNuePar.eliminarVisInfoJug(i);
            
            try{
                this.visNuePar.getVisSelEq().eliminarJugador(i);
            }catch(Exception e){
                // Nada
            }
        }else{
            this.visNuePar.setMensaje("Mínimo 2 jugadores.");
        }
    }
    
    public void crearVistaSeleccionEquipos(){
        this.visNuePar.setVisSelEq(new CompSelEquipos());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visNuePar.getVisSelEq());
        this.visNuePar.add(this.visNuePar.getVisSelEq(), 0);
        this.visNuePar.getVisSelEq().setLocation(280, 475);
        this.visNuePar.getVisSelEq().setVisible(true);
        
        for(int i = 0; i < this.jugadores.size(); i++){
            this.visNuePar.getVisSelEq().agregarJugador(this.jugadores.get(i));
            this.agregarListenersVistaSeleccionEquipos(i);
        }
    }
    
    public void agregarListenersVistaSeleccionEquipos(int i){
        this.visNuePar.getVisSelEq().getIconosJugadores().get(i).addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                visNuePar.getVisSelEq().cambiarEquipo(e.getComponent());
            }
        });
    }

    public void volver(){
        this.visNuePar.dispose();
        contPrin.crearControladorMenuPrincipal();
        contPrin.getContMenuPrin().mostrarVistaMenuPrincipal();
    }
    
    public void enEquipos(){
        try{
            this.visNuePar.getVisSelEq().dispose();
            this.visNuePar.setVisSelEq(null);
            
            for(Jugador jug: this.jugadores){
                jug.setEquipo(0);
            }
        }catch(Exception e){
            this.crearVistaSeleccionEquipos();
        }
    }
    
    public void comenzarPartida(){
        this.contPrin.crearControladorBatalla(this.jugadores);
        this.contPrin.getContBat().mostrarVistaBatalla();
        
        visNuePar.dispose();
    }
}
