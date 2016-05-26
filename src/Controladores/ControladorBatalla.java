/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.Tablero;
import Otros.BotonImagen;
import Vistas.CompPosicion;
import Vistas.CompTablero;
import Vistas.VistaBatalla;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

/**
 *
 * @author mam28
 */
public class ControladorBatalla {
    private final ControladorPrincipal contPrin;
    private final VistaBatalla visBat;
    private Tablero tablero;
    
    public ControladorBatalla(ControladorPrincipal contPrin){
        this.contPrin = contPrin;
        
        // Instancia y agrega la vista de batalla
        this.visBat = new VistaBatalla(this.contPrin.getFuente());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visBat);
        // Instancia y agrega el tablero a la vista batalla
        this.agregarTablero();
        // Agrega los listeners a los componentes de la vista batalla
        this.agregarListenersVistaBatalla();
        
        // Agregar la vista para seleccionar el despliegue de dado
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visBat.getVisSelDesp());
        this.agregarListenersVistaSeleccionarDespliegue();
    }
    
    public void agregarTablero(){
        this.tablero = new Tablero();
        this.visBat.setTablero(new CompTablero());
        
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                this.visBat.getTablero().getPosiciones()[i][j] = new CompPosicion(i, j);
                this.visBat.getTablero().add(this.visBat.getTablero().getPosiciones()[i][j]);
                this.agregarListenersPosicion(i, j);
            }
        }
        
        this.visBat.add(this.visBat.getTablero(), 0);
        this.visBat.getTablero().setLocation(150, 50);
        
        this.visBat.getTablero().getPosiciones()[0][7].setDueno(1);
    }
    
    public void agregarListenersPosicion(int fila, int columna){
        this.visBat.getTablero().getPosiciones()[fila][columna].addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e){
                visBat.setMensaje("");
                switch(tablero.getAccion()){
                    case 0: break;
                    case 1: visBat.getTablero().reiniciarCasillas();
                            visBat.getTablero().setBotonActual((CompPosicion) e.getComponent());
                            mostrarDespliegue(
                                    tablero.getDespliegue(),
                                    visBat.getTablero().getBotonActual(),
                                    tablero.getDireccion(),
                                    tablero.getTurnoActual());
                            break;
                }
            }
            
            @Override
            public void mouseClicked(MouseEvent e){
                switch(tablero.getAccion()){
                    case 0: break;
                    case 1: asignarCasillas(getDespliegue(
                            tablero.getDespliegue(),
                            visBat.getTablero().getBotonActual()),
                            tablero.getTurnoActual());
                }
            }
        });
        
        this.visBat.getTablero().getPosiciones()[fila][columna].addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                switch(e.getKeyCode()){
                    case KeyEvent.VK_W: tablero.setDireccion(0);
                                        break;
                    case KeyEvent.VK_A: tablero.setDireccion(1);
                                        break;
                    case KeyEvent.VK_S: tablero.setDireccion(2);
                                        break;
                    case KeyEvent.VK_D: tablero.setDireccion(3);
                                        break;
                }
                
                visBat.getTablero().reiniciarCasillas();
                mostrarDespliegue(
                        tablero.getDespliegue(),
                        visBat.getTablero().getBotonActual(),
                        tablero.getDireccion(),
                        tablero.getTurnoActual());
            }
        });
    }
    
    public void agregarListenersVistaBatalla(){
        this.visBat.addMouseMotionListener(new MouseMotionAdapter(){
            @Override
            public void mouseMoved(MouseEvent e){
                visBat.setMensaje("");
            }
        });
        
        this.visBat.getInvocacion().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                mostrarVistaSeleccionarDespliegue();
            }
            
            @Override
            public void mouseEntered(MouseEvent e){
                visBat.setMensaje("Invocar criatura");
            }
        });
    }
    
    public void agregarListenersVistaSeleccionarDespliegue(){        
        for(BotonImagen botonDespliegue: this.visBat.getVisSelDesp().getBotonesDespliegue()){
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
    
    /**
     * Cambia el valor del despliegue a mostrar en el tablero.
     * @param numDespliegue Número de despliegue a mostrar.
     */
    public void cambiarDespliegue(int numDespliegue){
        this.tablero.setAccion(1);
        this.tablero.setDespliegue(numDespliegue);
        this.visBat.getTablero().getPosiciones()[0][0].requestFocus();
    }
    
    /**
     * Devuelve la forma del despliegue indicado por "numDespliegue".
     * @param numDespliegue Número del despliegue a obtener.
     * @param botonActual Botón actual sobre el que se encuentra el mouse.
     * @return 
     */
    public ArrayList<CompPosicion> getDespliegue(int numDespliegue, CompPosicion botonActual){
        switch(numDespliegue){
            case 0: return this.visBat.getTablero().getDespliegueCruz(botonActual, this.tablero.getDireccion());
            case 1: return this.visBat.getTablero().getDespliegueEscalera(botonActual, this.tablero.getDireccion());
            case 2: return this.visBat.getTablero().getDespliegueT(botonActual, this.tablero.getDireccion());
            case 3: return this.visBat.getTablero().getDespliegueS(botonActual, this.tablero.getDireccion());
            case 4: return this.visBat.getTablero().getDespliegue4(botonActual, this.tablero.getDireccion());
            case 5: return this.visBat.getTablero().getDespliegueR(botonActual, this.tablero.getDireccion());
        }
        return null;
    }
    
    /**
     * Pinta los botones que conforman el despliegue de dados en la posición indicada
     * por el botón actual sobre el que se encuentra el mouse.
     * @param numDespliegue Número del despliegue a mostrar.
     * @param botonActual Botón actual sobre el que se encuentra el mouse.
     * @param direccion Dirección del despliegue.
     * @param turno Turno actual.
     */
    public void mostrarDespliegue(int numDespliegue, CompPosicion botonActual, int direccion, int turno){
        if(getDespliegue(numDespliegue, botonActual) != null){
            // Comprobar
            for(CompPosicion casilla: getDespliegue(numDespliegue, botonActual)){
                if(casilla.getDueno() == 0){
                    casilla.setImagenSobre("/Imagenes/Botones/casilla_j" + (turno + 1) + ".png");
                }else{
                    casilla.setImagenSobre("/Imagenes/Botones/casilla_error.png");
                }
            }
            
            // Pintar
            for(CompPosicion casilla: getDespliegue(numDespliegue, botonActual)){
                casilla.setImagenActual(1);
            }
        }else{
            this.visBat.setMensaje("Fuera de los límites del tablero.");
        }
    }
    
    /**
     * Marca las casillas del despliegue como propiedad del jugador del turno actual.
     * @param casillas Casillas que conforman el despliegue.
     * @param turno Turno actual.
     */
    public void asignarCasillas(ArrayList<CompPosicion> casillas, int turno){
        int jugador = ++turno;
        
        // Comprobar
        if(casillas != null){

            // Asignar
            if(this.visBat.getTablero().estaDisponible(casillas)){
                if(this.visBat.getTablero().estaConectadoAlTerreno(casillas, jugador)){
                    for(CompPosicion casilla: casillas){
                        casilla.setDueno(jugador);
                    }
                }else{
                    this.visBat.setMensaje("No está conectado a tu terreno.");
                }
            }else{
                this.visBat.setMensaje("Casilla ocupada por otro jugador.");
            }
        }else{
            this.visBat.setMensaje("No se pudo invocar. Elige una posición válida.");
        }
    }
    
}
