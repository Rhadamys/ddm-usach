/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.Accion;
import Modelos.Dado;
import Modelos.Jugador;
import Modelos.Tablero;
import Otros.BotonCheckImagen;
import Otros.BotonImagen;
import Otros.PanelImagen;
import Vistas.SubVistaCuadroDialogo;
import Vistas.SubVistaInfoElemento;
import Vistas.SubVistaLanzamientoDados;
import Vistas.SubVistaPosicion;
import Vistas.SubVistaTablero;
import Vistas.VistaBatalla;
import Vistas.SubVistaMenuPausa;
import Vistas.SubVistaSeleccionDados;
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
    private final SubVistaMenuPausa visPausBat;
    private final SubVistaCuadroDialogo visVolMenuPrin;
    private final Tablero tablero;
    private final int[][] posJugTab = {{7, 0}, {7, 14}, {0, 7}, {14, 7}};
    
    public ControladorBatalla(
            ControladorPrincipal contPrin,
            ArrayList<Jugador> jugadores){
        
        this.contPrin = contPrin;
        
        // Instancia y agrega la vista de batalla
        this.visBat = new VistaBatalla(this.contPrin.getFuente());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visBat);
        // Instancia y agrega el tablero a la vista batalla
        this.tablero = new Tablero();
        this.crearVistaTablero();
        // Agrega los listeners a los componentes de la vista batalla
        this.agregarListenersVistaBatalla();
        
        // Agregar la vista para seleccionar el despliegue de dado
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visBat.getVisSelDesp());
        this.agregarListenersVistaSeleccionarDespliegue();
        
        this.tablero.setJugadores(jugadores);
        this.agregarJugadoresPartida(jugadores);
        
        this.visPausBat = new SubVistaMenuPausa(this.contPrin.getFuente());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visPausBat);
        this.agregarListenersVistaPausaBatalla();
        
        this.visVolMenuPrin = new SubVistaCuadroDialogo(
                "<html><center>¿Deseas volver al menú principal?<br>"
              + "Se perderá el progreso de la partida.</center></html>",
                "Si", "No", this.contPrin.getFuente(), this.contPrin, 2);
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visVolMenuPrin);
    }
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con vista batalla">   
    
    /**
     * Agrega las vistas de resumen de información de jugador para los jugadores
     * que conforman esta partida.
     * @param jugPartida Jugadores de la partida para los cuales se crearán las
     * vistas.
     */
    public void agregarJugadoresPartida(ArrayList<Jugador> jugPartida){
        for(int i = 0; i < jugPartida.size(); i++){
            this.visBat.agregarJugador(jugPartida.get(i));
            this.visBat.getTablero().marcarCasilla(posJugTab[i], i + 1);
            this.tablero.asignarCasilla(posJugTab[i], i + 1);
        }
        
        this.visBat.getTablero().actualizarCasillas();
    }
       
    /**
     * Agrega los listeners a los componentes de la vista de batalla.
     */
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
        
        this.visBat.getAtaque().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                crearVistaSeleccionDados(tablero.getJugadores().get(tablero.getTurnoActual()));
            }
            
            @Override
            public void mouseEntered(MouseEvent e){
                visBat.setMensaje("Realizar un ataque");
            }
        });
        
        this.visBat.getPausa().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                cambiarVisibilidadVistaPausaBatalla();
            }
        });
    }
    
    public void mostrarVistaBatalla(){
        this.visBat.setVisible(true);
    }

    public VistaBatalla getVisBat() {
        return visBat;
    }
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con la vista tablero">    
    
    /**
     * Instancia una nueva "vista" de tablero y la agrega a la vista de batalla
     * instanciada en este controlador. Además, crea una nueva instancia del modelo
     * Tablero y la almacena en este controlador.
     */
    public void crearVistaTablero(){
        this.visBat.setTablero(new SubVistaTablero());
        
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                this.visBat.getTablero().getPosiciones()[i][j] = new SubVistaPosicion(i, j);
                this.visBat.getTablero().add(this.visBat.getTablero().getPosiciones()[i][j]);
                this.agregarListenersPosicion(i, j);
            }
        }
        
        this.visBat.add(this.visBat.getTablero(), 0);
        this.visBat.getTablero().setLocation(150, 50);
    }
    
    /**
     * Agrega los listeners a una de las posiciones dentro de la "vista" tablero.
     * @param fila Fila de la posición dentro del tablero.
     * @param columna Columna de la posición dentro del tablero.
     */
    public void agregarListenersPosicion(int fila, int columna){
        this.visBat.getTablero().getPosiciones()[fila][columna].addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e){
                visBat.setMensaje("");
                switch(tablero.getAccion()){
                    case 0: break;
                    case 1: visBat.getTablero().setBotonActual((SubVistaPosicion) e.getComponent());
                            mostrarDespliegue(
                                    tablero.getNumDespliegue(),
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
                    case 1: asignarCasillas(
                            tablero.getDespliegue(visBat.getTablero().getBotonActual()),
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
                
                visBat.getTablero().actualizarCasillas();
                mostrarDespliegue(
                        tablero.getNumDespliegue(),
                        visBat.getTablero().getBotonActual(),
                        tablero.getDireccion(),
                        tablero.getTurnoActual());
            }
        });
    }
    
    /**
     * Pinta los botones que conforman el despliegue de dados en la posición indicada
     * por el botón actual sobre el que se encuentra el mouse.
     * @param numDespliegue Número del despliegue a mostrar.
     * @param botonActual Botón actual sobre el que se encuentra el mouse.
     * @param direccion Dirección del despliegue.
     * @param turno Turno actual.
     */
    public void mostrarDespliegue(int numDespliegue, SubVistaPosicion botonActual, int direccion, int turno){
        this.visBat.getTablero().actualizarCasillas();
        for(int[] coord: this.tablero.getDespliegue(botonActual)){
            try{
                if(this.tablero.getPosiciones()[coord[0]][coord[1]].getDueno() == 0){
                    this.visBat.getTablero().getPosiciones()[coord[0]][coord[1]].setImagenSobre(
                            "/Imagenes/Botones/casilla_j" + (turno + 1) + ".png");
                }else{
                    this.visBat.getTablero().getPosiciones()[coord[0]][coord[1]].setImagenSobre(
                            "/Imagenes/Botones/casilla_error.png");
                }
                this.visBat.getTablero().getPosiciones()[coord[0]][coord[1]].setImagenActual(1);
            }catch(Exception e){
                this.visBat.setMensaje("Fuera de los límites del tablero.");
            }
        }
    }
    
    /**
     * Marca las casillas del despliegue como propiedad del jugador del turno actual.
     * @param idxCasillas Índices de las posiciones en el tablero que conforman el despliegue.
     * @param turno Turno actual.
     */
    public void asignarCasillas(int[][] idxCasillas, int turno){
        // Comprobar si las casillas están disponibles
        if(this.tablero.estaDisponible(idxCasillas)){
            // Comprobar que el despliegue esté conectado al terreno del jugador
            if(this.tablero.estaConectadoAlTerreno(idxCasillas, (turno + 1))){
                // Asignar casillas al jugador
                for(int[] coord: idxCasillas){
                    this.tablero.asignarCasilla(coord, (turno + 1));
                    this.visBat.getTablero().marcarCasilla(coord, (turno + 1));
                }
            }else{
                this.visBat.setMensaje("No está conectado a tu terreno.");
            }
        }else{
            this.visBat.setMensaje("No es posible invocar en esta posición.");
        }
    }
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con la vista de selección de dados">  
    
    public void crearVistaSeleccionDados(Jugador jugador){
        this.visBat.setVisSelDados(new SubVistaSeleccionDados(this.contPrin.getFuente(), jugador.getDados()));
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visBat.getVisSelDados());
        this.visBat.getVisSelDados().setVisible(true);
        
        for(int i = 0; i < this.visBat.getVisSelDados().getPanelesDados().size(); i++){
            this.agregarListenersVistaSeleccionDados(i);
        }
    }
    
    public void agregarListenersVistaSeleccionDados(int i){
        this.visBat.getVisSelDados().getPanelesDados().get(i).addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                cambiarEstadoPanelDado((BotonCheckImagen) e.getComponent());
            }
            
            @Override
            public void mouseEntered(MouseEvent e){
                mostrarVistaInfoCriatura((BotonCheckImagen) e.getComponent());
            }
            
            @Override
            public void mouseExited(MouseEvent e){
                ocultarVistaInfoCriatura();
            }
        });
        
        this.visBat.getVisSelDados().getPanelesDados().get(i).addMouseMotionListener(new MouseMotionAdapter(){
            @Override
            public void mouseMoved(MouseEvent e){
                cambiarPosicionVistaInfoCriatura(e.getComponent().getX() + 101);
            }
        });
        
        this.visBat.getVisSelDados().getLanzarDados().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                lanzarDados();
            }
        });
    }
    
    public void cambiarEstadoPanelDado(BotonCheckImagen panelDado){
        if(panelDado.isSelected()){
            if(this.visBat.getVisSelDados().getSeleccionados().size() < 4){
                this.visBat.getVisSelDados().getSeleccionados().add(panelDado);
            }else{
                System.out.println("Entro aquí");
                panelDado.setSelected(false);
            }
        }else{
            this.visBat.getVisSelDados().getSeleccionados().remove(panelDado);
        }
    }
    
    public void lanzarDados(){
        this.visBat.getVisSelDados().setVisible(false);
        
        ArrayList<Dado> dados = new ArrayList();
        
        for(BotonCheckImagen panelDado: this.visBat.getVisSelDados().getSeleccionados()){
            dados.add(Dado.getDado(this.visBat.getVisSelDados().getDados().get(
                    this.visBat.getVisSelDados().getPanelesDados().indexOf(panelDado)).getClave()));
        }
        
        this.visBat.setVisLanDados(new SubVistaLanzamientoDados(Accion.lanzarDados(dados), dados));
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visBat.getVisLanDados());
        this.visBat.getVisLanDados().setVisible(true);
    }
    
    public void mostrarVistaInfoCriatura(BotonCheckImagen panelDado){
        this.visBat.getVisSelDados().setVisInfo(new SubVistaInfoElemento(
                this.visBat.getVisSelDados().getDados().get(this.visBat.getVisSelDados().getPanelesDados().indexOf(panelDado)).getCriatura(),
                this.contPrin.getFuente()));
        this.visBat.getVisSelDados().add(this.visBat.getVisSelDados().getVisInfo(), 0);  
        this.visBat.getVisSelDados().getVisInfo().setVisible(true);
    }
    
    public void cambiarPosicionVistaInfoCriatura(int x){
        this.visBat.getVisSelDados().getVisInfo().setLocation(x > 400 ? 10: 540, 100);
        this.visBat.getVisSelDados().repaint();
    }
    
    public void ocultarVistaInfoCriatura(){
        this.visBat.getVisSelDados().getVisInfo().setVisible(false);
        this.visBat.getVisSelDados().remove(this.visBat.getVisSelDados().getVisInfo());
    }
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con la vista de selección de despliegue"> 
    
    /**
     * Agrega los listeners a la "vista" de selección de despliegue de dados.
     */
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
        this.tablero.setNumDespliegue(numDespliegue);
        this.visBat.getTablero().getPosiciones()[0][0].requestFocus();
    }
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con la vista pausa batalla">
    
    public void agregarListenersVistaPausaBatalla(){
        this.visPausBat.getContinuarPartida().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                cambiarVisibilidadVistaPausaBatalla();
            }
        });
        
        this.visPausBat.getVolverMenuPrincipal().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                volverMenuPrincipal();
            }
        });
        
        this.visPausBat.getSalirAplicacion().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                contPrin.getContVisPrin().salir();
            }
        });
    }
    
    public void cambiarVisibilidadVistaPausaBatalla(){
        this.visPausBat.setVisible(!this.visPausBat.isVisible());
    }

    public SubVistaMenuPausa getVisPausBat() {
        return visPausBat;
    }
    
    public void volverMenuPrincipal(){
        this.visVolMenuPrin.setVisible(true);
    }
    
// </editor-fold>
    
}
