/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Otros.BotonImagen;
import Vistas.CompPosicion;
import Vistas.CompSelDesp;
import Vistas.CompTablero;
import Vistas.VistaBatalla;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author mam28
 */
public class ControladorBatalla {
    private final ControladorPrincipal contPrin;
    private VistaBatalla visBat;
    private int accion = 0;
    private int direccion = 0;
    private int despliegue = 0;
    private int turnoActual = 1;
    
    public ControladorBatalla(ControladorPrincipal contPrin){
        this.contPrin = contPrin;
        
        this.visBat = new VistaBatalla(this.contPrin.getFuentePersonalizada());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visBat);
        this.agregarTablero();
        this.agregarListenersVistaBatalla();
        
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visBat.getVisSelDesp());
        this.agregarListenersVistaSeleccionarDespliegue();
    }
    
    public void agregarTablero(){
        CompPosicion[][] posiciones = new CompPosicion[15][15];
        CompTablero tablero = new CompTablero();
        
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                posiciones[i][j] = new CompPosicion(i, j);
                tablero.add(posiciones[i][j]);
                this.agregarListenersPosicion(posiciones[i][j]);
            }
        }
        
        tablero.setPosiciones(posiciones);
        this.visBat.setTablero(tablero);
        this.visBat.add(this.visBat.getTablero(), 0);
        this.visBat.getTablero().setLocation(150, 50);
    }
    
    public void agregarListenersPosicion(CompPosicion posicion){
        posicion.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e){
                switch(accion){
                    case 0: break;
                    case 1: visBat.getTablero().reiniciarCasillas();
                            mostrarDespliegue(
                                    despliegue,
                                    (CompPosicion) e.getComponent(),
                                    direccion,
                                    turnoActual);
                            break;
                }
            }
            
            @Override
            public void mouseClicked(MouseEvent e){
                switch(accion){
                    case 0: break;
                    case 1: asignarCasillas(getDespliegue(
                            despliegue,
                            (CompPosicion) e.getComponent()),
                            turnoActual);
                }
            }
        });
        
        posicion.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                switch(e.getKeyCode()){
                    case KeyEvent.VK_W: direccion = 0;
                                        break;
                    case KeyEvent.VK_A: direccion = 1;
                                        break;
                    case KeyEvent.VK_S: direccion = 2;
                                        break;
                    case KeyEvent.VK_D: direccion = 3;
                                        break;
                    case KeyEvent.VK_1: turnoActual = 1;
                                        break;
                    case KeyEvent.VK_2: turnoActual = 2;
                                        break;
                    case KeyEvent.VK_3: turnoActual = 3;
                                        break;
                    case KeyEvent.VK_4: turnoActual = 4;
                                        break;
                }
            }
        });
    }
    
    public void agregarListenersVistaBatalla(){
        this.visBat.getInvocacion().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                mostrarVistaSeleccionarDespliegue();
            }
        });
    }
    
    public void agregarListenersVistaSeleccionarDespliegue(){
        ArrayList<BotonImagen> botonesDespliegue = this.visBat.getVisSelDesp().getBotonesDespliegue();
        
        for(BotonImagen botonDespliegue: botonesDespliegue){
            botonDespliegue.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e){
                    cambiarDespliegue(Integer.parseInt(e.getComponent().getName()));
                    ocultarVistaSeleccionarDespliegue();
                }
            });
        }
    }
    
    public void mostrarVistaBatalla(){
        this.visBat.setVisible(true);
    }
    
    public void mostrarVistaSeleccionarDespliegue(){
        this.visBat.getVisSelDesp().setVisible(true);
    }
    
    public void ocultarVistaSeleccionarDespliegue(){
        this.visBat.getVisSelDesp().setVisible(false);
    }
    
    public void cambiarDespliegue(int numDespliegue){
        this.accion = 1;
        this.despliegue = numDespliegue;
        this.visBat.getTablero().getPosiciones()[0][0].requestFocus();
    }
    
    public ArrayList<CompPosicion> getDespliegue(int numDespliegue, CompPosicion botonActual){
        switch(numDespliegue){
            case 0: return this.visBat.getTablero().getDespliegueCruz(botonActual, direccion);
            case 1: return this.visBat.getTablero().getDespliegueEscalera(botonActual, direccion);
            case 2: return this.visBat.getTablero().getDespliegueT(botonActual, direccion);
            case 3: return this.visBat.getTablero().getDespliegueS(botonActual, direccion);
            case 4: return this.visBat.getTablero().getDespliegue4(botonActual, direccion);
            case 5: return this.visBat.getTablero().getDespliegueR(botonActual, direccion);
        }
        return null;
    }
    
    public void mostrarDespliegue(int numDespliegue, CompPosicion botonActual, int direccion, int jugador){
        ArrayList<CompPosicion> despliegue = getDespliegue(numDespliegue, botonActual);
        if(despliegue != null){
            // Comprobar
            for(CompPosicion casilla: despliegue){
                if(casilla.getDueno() == 0){
                    casilla.setImagenSobre("/Imagenes/Botones/casilla_j" + jugador + ".png");
                }else{
                    casilla.setImagenSobre("/Imagenes/Botones/casilla_error.png");
                }
            }
            
            // Pintar
            for(CompPosicion casilla: despliegue){
                casilla.setImagenActual(1);
            }
        }else{
            this.visBat.setMensaje("Fuera de los límites del tablero.");
        }
    }
    
    public void asignarCasillas(ArrayList<CompPosicion> casillas, int jugador){
        // Comprobar
        if(casillas != null){
            boolean sePuedeAsignar = true;
            for(CompPosicion casilla: casillas){
                if(casilla.getDueno() != 0){
                    this.visBat.setMensaje("Casilla ocupada por jugador " + casilla.getDueno());
                    sePuedeAsignar = false;
                    break;
                }
            }

            // Asignar
            if(sePuedeAsignar){
                for(CompPosicion casilla: casillas){
                    casilla.setImagenFuera("/Imagenes/Botones/casilla_j" + jugador + ".png");
                    casilla.setDueno(jugador);
                }
            }
        }else{
            this.visBat.setMensaje("No se pudo invocar. Elige una posición válida.");
        }
    }
}
