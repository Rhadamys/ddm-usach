/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Vistas.CompPosicion;
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
    private int accion = 1;
    private int direccion = 0;
    private int despliegue = 0;
    private int turnoActual = 1;
    
    public ControladorBatalla(ControladorPrincipal contPrin){
        this.contPrin = contPrin;
        
        this.visBat = new VistaBatalla(this.contPrin.getFuentePersonalizada());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visBat);
        this.agregarTablero();
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
        this.visBat.add(this.visBat.getTablero());
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
                    case KeyEvent.VK_5: despliegue = 0;
                                        break;
                    case KeyEvent.VK_6: despliegue = 1;
                                        break;
                    case KeyEvent.VK_7: despliegue = 2;
                                        break;
                    case KeyEvent.VK_8: despliegue = 3;
                                        break;
                    case KeyEvent.VK_9: despliegue = 4;
                                        break;
                    case KeyEvent.VK_0: despliegue = 5;
                                        break;
                }
            }
        });
    }
    
    public void mostrarVistaBatalla(){
        this.visBat.setVisible(true);
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
            for(CompPosicion casilla: despliegue){
                casilla.setImagenSobre("/Imagenes/Botones/casilla_j" + jugador + ".png");
                casilla.setImagenActual(1);
            }
        }else{
            this.visBat.setMensaje("Fuera de los límites del tablero.");
        }
    }
    
    public void asignarCasillas(ArrayList<CompPosicion> casillas, int jugador){
        for(CompPosicion casilla: casillas){
            casilla.setImagenFuera("/Imagenes/Botones/casilla_j" + jugador + ".png");
        }
    }
}
