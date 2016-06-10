/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.Accion;
import Modelos.Criatura;
import Modelos.Dado;
import Modelos.ElementoEnCampo;
import Modelos.JefeDeTerreno;
import Modelos.Jugador;
import Modelos.Posicion;
import Modelos.Tablero;
import Modelos.Terreno;
import Modelos.Trampa;
import Otros.BotonCheckImagen;
import Otros.BotonImagen;
import Vistas.SubVistaCambioTurno;
import Vistas.SubVistaCuadroDialogo;
import Vistas.SubVistaInfoElemento;
import Vistas.SubVistaInfoJugadorBatalla;
import Vistas.SubVistaLanzamientoDados;
import Vistas.SubVistaPosicion;
import Vistas.SubVistaTablero;
import Vistas.VistaBatalla;
import Vistas.SubVistaMenuPausa;
import Vistas.SubVistaSeleccionCriatura;
import Vistas.SubVistaSeleccionDados;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author mam28
 */
public class ControladorBatalla {
    private final ControladorPrincipal contPrin;
    private final VistaBatalla visBat;
    private final SubVistaMenuPausa visPausBat;
    private SubVistaCuadroDialogo visMen;
    private final Tablero tablero;
    private final Accion accion;
    private final ArrayList<Terreno> terrenos;
    private final int[][] posJugTab = {{7, 0}, {7, 14}, {0, 7}, {14, 7}};
    private SubVistaInfoElemento visInfoEl;
    private Timer timerVisInfoEl;
    
    public ControladorBatalla(
            ControladorPrincipal contPrin,
            ArrayList<Jugador> jugadores){
        
        this.contPrin = contPrin;
        
        // Instancia y agrega la vista de batalla
        this.visBat = new VistaBatalla(this.contPrin.getFuente());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visBat);
        // Instancia y agrega el tablero a la vista batalla
        this.tablero = new Tablero();
        this.terrenos = new ArrayList();
        this.accion = new Accion();
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
            this.terrenos.add(new Terreno());
            this.tablero.getJugador(i).setTerreno(this.terrenos.get(i));
            this.visBat.agregarJugador(jugPartida.get(i));
            this.tablero.asignarCasilla(posJugTab[i], i + 1);
            this.tablero.getPosicion(posJugTab[i][0], posJugTab[i][1])
                    .setElemento(jugPartida.get(i).getJefeDeTerreno());
            this.asignarCasilla(posJugTab[i], i, jugPartida.get(i).getJefeDeTerreno());
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
        
        this.visBat.getAtaque().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getComponent().isEnabled()){
                    if(sePuedeAtacar()){
                        
                    }else{
                        mostrarMensajeNoSePuedeAtacar();
                    }
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e){
                if(e.getComponent().isEnabled()){
                    visBat.setMensaje("Realizar un ataque");
                }
            }
        });
        
        this.visBat.getInvocacion().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getComponent().isEnabled()){
                    if(tablero.getNumAccion() == 1){
                        mostrarVistaSeleccionarDespliegue();
                    }else{
                        mostrarMensaje("No estás invocando una criatura actualmente.");
                    }
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e){
                if(e.getComponent().isEnabled()){
                    visBat.setMensaje("Cambiar despliegue invocación");
                }
            }
        });
        
        this.visBat.getMagia().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getComponent().isEnabled()){
                    
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e){
                if(e.getComponent().isEnabled()){
                    visBat.setMensaje("Activar una magia");
                }
            }
        });
        
        this.visBat.getMovimiento().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getComponent().isEnabled()){
                    if(tablero.getNumAccion() == 0){
                        if(sePuedeMover()){
                            solicitarSeleccionarCriatura();
                        }else{
                            mostrarMensajeNoSePuedeMover();
                        }
                    }else{
                        moverCriatura();
                    }
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e){
                if(e.getComponent().isEnabled()){
                    visBat.setMensaje("Mover una criatura");
                }
            }
        });
        
        this.visBat.getTrampa().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getComponent().isEnabled()){
                    
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e){
                if(e.getComponent().isEnabled()){
                    visBat.setMensaje("Colocar una trampa");
                }
            }
        });
        
        this.visBat.getPausa().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getComponent().isEnabled()){
                    visPausBat.setVisible(true);
                }
            }
        });
        
        this.visBat.getTerminarTurno().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getComponent().isEnabled()){
                    cambiarTurno();
                }
            }
        });
    }
    
    public void mostrarVistaBatalla(){
        this.visBat.setVisible(true);
    }

    public VistaBatalla getVisBat() {
        return visBat;
    }
    
    public void actualizarVistaJugador(int i){
        SubVistaInfoJugadorBatalla visInfoJug = this.visBat.getVistaJugador(i);
        visInfoJug.setPuntosAtaque(String.valueOf(this.tablero.getJugador(i).getTurno().getPuntosAtaque()));
        visInfoJug.setPuntosMagia(String.valueOf(this.tablero.getJugador(i).getTurno().getPuntosMagia()));
        visInfoJug.setPuntosMovimiento(String.valueOf(this.tablero.getJugador(i).getTurno().getPuntosMovimiento()));
        visInfoJug.setPuntosTrampa(String.valueOf(this.tablero.getJugador(i).getTurno().getPuntosTrampa()));
        visInfoJug.setVidaJugador(this.tablero.getJugador(i).getJefeDeTerreno().getVida());
    }
    
    public void iniciarJuego(){
        this.tablero.setTurnoActual(-1);
        this.cambiarTurno();
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
        this.visBat.getTablero().getCasilla(fila, columna).addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e){
                visBat.setMensaje("");
                switch(tablero.getNumAccion()){
                    case 0: mostrarVistaInfoElemento((SubVistaPosicion) e.getComponent());
                            posicionarVistaInfoElemento(e.getComponent().getX() + 150);
                            break;
                    case 1: visBat.getTablero().setBotonActual((SubVistaPosicion) e.getComponent());
                            mostrarDespliegue(
                                    tablero.getNumDespliegue(),
                                    visBat.getTablero().getBotonActual(),
                                    tablero.getDireccion(),
                                    tablero.getTurnoActual());
                            break;
                    case 10:    comprobarCasillaSeleccionCriatura((SubVistaPosicion) e.getComponent());
                                break;
                    case 11:    comprobarCasillaMovimiento((SubVistaPosicion) e.getComponent());
                                break;
                }
            }
            
            @Override
            public void mouseClicked(MouseEvent e){
                switch(tablero.getNumAccion()){
                    case 0: break;
                    case 1: visBat.getTablero().reiniciarCasillas();
                            invocarCriatura((SubVistaPosicion) e.getComponent());
                            break;
                    case 10:    setCriaturaAMover((SubVistaPosicion) e.getComponent());
                                break;
                    case 11:    cambiarEstadoCasillaCamino((SubVistaPosicion) e.getComponent());
                                break;
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e){
                ocultarVistaInfoElemento();
            }
        });
        
        this.visBat.getTablero().getCasilla(fila, columna).addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                switch(tablero.getNumAccion()){
                    case 0: break;
                    case 1: switch(e.getKeyCode()){
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
                            break;
                }
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
        for(int[] coord: this.tablero.getDespliegue(botonActual.getFila(), botonActual.getColumna())){
        Posicion posicionActual = this.tablero.getPosicion(coord[0], coord[1]);
            try{
                if(posicionActual.getDueno() == 0){
                    this.visBat.getTablero().getCasilla(coord[0], coord[1]).setImagenSobre(
                            "/Imagenes/Botones/casilla_j" + (turno + 1) + ".png");
                }else{
                    this.visBat.getTablero().getCasilla(coord[0], coord[1]).setImagenSobre(
                            "/Imagenes/Botones/casilla_error.png");
                }
                this.visBat.getTablero().getCasilla(coord[0], coord[1]).setImagenActual(1);
            }catch(Exception e){
                this.visBat.setMensaje("Fuera de los límites del tablero.");
            }
        }
    }
    
    /**
     * Marca las casillas del despliegue como propiedad del jugador del turno actual.
     * @param idxCasillas Índices (Coordenadas) de las casillas que forman el despliegue.
     * @param turno Turno actual.
     */
    public void asignarCasillas(int[][] idxCasillas, int turno){
        for(int[] coord: idxCasillas){
            this.asignarCasilla(coord, turno, this.tablero.getPosicion(coord[0], coord[1]).getElemento());
        }
    }
    
    /**
     * Asigna una casilla a un jugador y guarda el elemento en campo señalado en dicha
     * posición.
     * @param coord Índice (coordenada) de la casilla.
     * @param turno Turno actual.
     * @param elemento Elemento que se guardará en la casilla.
     */
    public void asignarCasilla(int[] coord, int turno, ElementoEnCampo elemento){
        this.tablero.asignarCasilla(coord, (turno + 1));
        this.visBat.getTablero().marcarCasilla(coord, (turno + 1),                            
            "/Imagenes/" + (elemento == null ? "vacio.png" :
                    (elemento instanceof Criatura ? "Criaturas/" : 
                    elemento instanceof JefeDeTerreno ? "Jefes/" : "") + 
                    elemento.getNomArchivoImagen() + ".png"));
    }
    
    public void mostrarVistaInfoElemento(SubVistaPosicion posicion){
        Posicion posicionActual = this.tablero.getPosicion(posicion.getFila(), posicion.getColumna());
        if(posicionActual.getElemento() != null){
            this.visInfoEl = new SubVistaInfoElemento(
                    posicionActual.getElemento(),
                    this.contPrin.getFuente());
            this.visBat.add(this.visInfoEl, 0);
            
            this.timerVisInfoEl = new Timer();
            this.timerVisInfoEl.schedule(new TimerTask(){
                @Override
                public void run(){
                    visInfoEl.setVisible(true);
                    timerVisInfoEl.cancel();
                }
            }, 1500, 10);
        }
    }
    
    public void posicionarVistaInfoElemento(int x){
        if(this.visInfoEl != null){
            this.visInfoEl.setLocation(x > 400 ? 10: 540, 100);
        }
    }
    
    public void ocultarVistaInfoElemento(){
        if(this.visInfoEl != null){
            try{
                this.timerVisInfoEl.cancel();
            }catch(Exception e){
                // Nada
            }
            this.visInfoEl.setVisible(false);
            this.visBat.repaint();
        }
    }
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con la vista de selección de dados">  
    
    public void crearVistaSeleccionDados(Jugador jugador){
        this.visBat.setVisSelDados(new SubVistaSeleccionDados(this.contPrin.getFuente(), jugador.getDados()));
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visBat.getVisSelDados());
        this.visBat.getVisSelDados().setVisible(true);
        
        for(int i = 0; i < this.visBat.getVisSelDados().getPanelesDados().size(); i++){
            this.agregarListenersPanelDado(i);
        }
        
        this.agregarListenersVistaSeleccionDados();
    }
    
    public void agregarListenersPanelDado(int i){
        this.visBat.getVisSelDados().getPanelesDados().get(i).addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                comprobarCantidadDadosSeleccionados((BotonCheckImagen) e.getComponent());
            }
            
            @Override
            public void mouseEntered(MouseEvent e){
                mostrarVistaInfoCriatura((BotonCheckImagen) e.getComponent());
                posicionarVistaInfoCriatura(e.getComponent().getX() + 101);
            }
            
            @Override
            public void mouseExited(MouseEvent e){
                ocultarVistaInfoCriatura();
            }
        });
    }
    
    public void agregarListenersVistaSeleccionDados(){
        this.visBat.getVisSelDados().getLanzarDados().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                lanzarDados();
            }
        });
    }
    
    public void comprobarCantidadDadosSeleccionados(BotonCheckImagen panelDado){
        if(panelDado.isSelected() && this.visBat.getVisSelDados().getSeleccionados() > 4){
                SubVistaCuadroDialogo visMen = new SubVistaCuadroDialogo("Máximo 4 dados.", "Aceptar", this.contPrin.getFuente());
                this.contPrin.getContVisPrin().getVisPrin().agregarVista(visMen);
                visMen.setVisible(true);
                panelDado.setSelected(false);
        }
    }
    
    public void lanzarDados(){        
        this.tablero.getJugadorActual().getTurno().lanzarDados(this.visBat.getVisSelDados().getDadosSeleccionados());
        this.visBat.getVisSelDados().dispose();
        
        this.visBat.setVisLanDados(new SubVistaLanzamientoDados(
                this.tablero.getJugadorActual().getTurno().getResultadoLanzamiento(),
                this.tablero.getJugadorActual().getTurno().getDadosLanzados(), this.contPrin.getFuente()));
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visBat.getVisLanDados());
        this.visBat.getVisLanDados().setVisible(true);
        this.agregarListenersVistaLanzamientoDados();
        
    }
    
    public void mostrarVistaInfoCriatura(BotonCheckImagen panelDado){
        this.visBat.getVisSelDados().setVisInfo(new SubVistaInfoElemento(
                this.visBat.getVisSelDados().getDado(panelDado).getCriatura(),
                this.contPrin.getFuente()));
        this.visBat.getVisSelDados().add(this.visBat.getVisSelDados().getVisInfo(), 0);
        
        this.timerVisInfoEl = new Timer();
        this.timerVisInfoEl.schedule(new TimerTask(){
            @Override
            public void run(){
                visBat.getVisSelDados().getVisInfo().setVisible(true);
                timerVisInfoEl.cancel();
            }
        }, 1500, 10);
    }
    
    public void posicionarVistaInfoCriatura(int x){
        this.visBat.getVisSelDados().getVisInfo().setLocation(x > 400 ? 10: 540, 100);
        this.visBat.getVisSelDados().repaint();
    }
    
    public void ocultarVistaInfoCriatura(){
        try{
            this.timerVisInfoEl.cancel();
        }catch(Exception e){
            // Nada
        }
        
        try{
            this.visBat.getVisSelDados().getVisInfo().setVisible(false);
            this.visBat.getVisSelDados().repaint();
        }catch(Exception e){
            // Nada
        }
    }
// </editor-fold>
  
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con la vista de lanzamiento de dados">  
    
    public void agregarListenersVistaLanzamientoDados(){
        this.visBat.getVisLanDados().getAculumarPuntos().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(cantidadCarasInvocacion() != 0){
                    mostrarMensajeCarasInvocacion();
                }else{
                    acumularPuntos();
                    cambiarTurno();
                }
            }
        });
        
        this.visBat.getVisLanDados().getRealizarAcciones().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                acumularPuntos();
                if(cantidadCarasInvocacion() != 0){
                    mostrarVistaSeleccionCriatura();
                }else{
                    realizarAcciones();
                }
            }
        });
    }
    
    public int cantidadCarasInvocacion(){
        int cantidadInvocacion = 0;
        for(int cara: this.tablero.getJugadorActual().getTurno().getResultadoLanzamiento()){
            if(cara == 2){
                cantidadInvocacion++;
            }
        }
        
        return cantidadInvocacion;
    }
    
    public void acumularPuntos(){
        this.tablero.getJugadorActual().getTurno().acumularPuntos();
        this.visBat.getVisLanDados().dispose();
        this.actualizarVistaJugador(this.tablero.getTurnoActual());
    }
    
    public void mostrarMensajeCarasInvocacion(){
        this.visMen = new SubVistaCuadroDialogo("<html><center>Atención: Las caras de invocación no se acumulan. ¿Continuar de todos modos?</center></html>",
                "Si", "No", this.contPrin.getFuente());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visMen);
        this.visMen.setVisible(true);
        this.visMen.setName("puntos_invocacion");
        this.agregarListenersVistaMensaje();
    }
    
    public void realizarAcciones(){
        this.visBat.getVisLanDados().dispose();
        this.habilitarBotones();
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
        ocultarVistaSeleccionarDespliegue();
        this.tablero.setNumAccion(1);
        this.tablero.setNumDespliegue(numDespliegue);
        this.visBat.getTablero().getCasilla(0, 0).requestFocus();
        this.visBat.getInvocacion().setEnabled(true);
    }
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con la vista pausa batalla">
    
    public void agregarListenersVistaPausaBatalla(){
        this.visPausBat.getContinuarPartida().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                visPausBat.setVisible(false);
            }
        });
        
        this.visPausBat.getVolverMenuPrincipal().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                mostrarMensajeVolverMenuPrincipal();
            }
        });
        
        this.visPausBat.getSalirAplicacion().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                contPrin.getContVisPrin().mostrarMensajeSalir();
            }
        });
    }

    public SubVistaMenuPausa getVisPausBat() {
        return visPausBat;
    }
    
    public void mostrarMensajeVolverMenuPrincipal(){
        this.visMen = new SubVistaCuadroDialogo(
                "<html><center>¿Deseas volver al menú principal? Se perderá el progreso de la partida.</center></html>",
                "Si", "No", this.contPrin.getFuente());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visMen);
        this.visMen.setVisible(true);
        this.visMen.setName("volver_menu");
        this.agregarListenersVistaMensaje();
    }
    
    public void volverMenuPrincipal(){
        this.contPrin.getContBat().getVisBat().dispose();
        this.contPrin.getContBat().getVisPausBat().dispose();
        this.contPrin.crearControladorMenuPrincipal();
        this.contPrin.getContMenuPrin().mostrarVistaMenuPrincipal();
        this.visMen.dispose();
    }
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con la vista de cuadro de diálogo">  
    public void agregarListenersVistaMensaje() {
        this.visMen.getBoton1().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                switch(visMen.getName()){
                    case "volver_menu": volverMenuPrincipal();
                                        break;
                    case "puntos_invocacion":   acumularPuntos();
                                                break;
                }
                visMen.dispose();
            }
        });
        
        this.visMen.getBoton2().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                visMen.dispose();
            }
        });
    }
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con el ataque de criatura">  
    
    public boolean sePuedeAtacar(){
        if(this.tablero.getJugadorActual().getTurno().getPuntosAtaque() != 0){
            int cantidadJugadoresTienenCriaturas = 0;
            for(Terreno terreno: terrenos){
                if(terreno.cantidadCriaturasInvocadas() != 0){
                    cantidadJugadoresTienenCriaturas++;
                }
            }

            if(cantidadJugadoresTienenCriaturas > 1){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    
    public boolean casillaContieneCriatura(SubVistaPosicion posicion){
        return this.tablero.getPosicion(posicion.getFila(), posicion.getColumna()).getElemento() instanceof Criatura;
    }
    
    public void mostrarMensajeNoSePuedeAtacar(){
        this.mostrarMensaje("No se puede atacar porque no se cumple con el mínimo de criaturas requeridas en el tablero.");
    }
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con la invocación de criaturas">  
    
    public void agregarListenersVistaSeleccionCriatura(int i){
        this.visBat.getVisSelCri().getPanelCriatura(i).addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                seleccionarCriaturaAInvocar((BotonImagen) e.getComponent());
            }
        });
    }
        
    public void mostrarVistaSeleccionCriatura(){
        this.visBat.getVisLanDados().dispose();
        
        this.visBat.setVisSelCri(new SubVistaSeleccionCriatura(this.criaturasQueSePuedenInvocar(
                this.tablero.getJugadorActual().getTurno().getDadosLanzados()), this.contPrin.getFuente()));
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visBat.getVisSelCri());
        this.visBat.getVisSelCri().setVisible(true);
        
        for(int i = 0; i < this.visBat.getVisSelCri().getCantidadCriaturas(); i++){
            this.agregarListenersVistaSeleccionCriatura(i);
        }
    }
    
    public ArrayList<Criatura> criaturasQueSePuedenInvocar(ArrayList<Dado> dados){
        ArrayList<Criatura> criaturas = new ArrayList();
        for(Dado dado: dados){
            int nivel = dado.getCriatura().getNivel();
            if(nivel == 1){
                criaturas.add(dado.getCriatura());
            }else if(this.cantidadCarasInvocacion() >= 2){
                if(nivel == 2){
                    criaturas.add(dado.getCriatura());
                }else{
                    boolean terminar = false;
                    for(int i = 0; i < 15; i++){
                        if(!terminar){
                            for(int j = 0; j < 15; j++){
                                if(this.tablero.getPosicion(i, j).getElemento() instanceof Criatura &&
                                   ((Criatura) this.tablero.getPosicion(i, j).getElemento()).getNivel() == (nivel - 1)){
                                    criaturas.add(dado.getCriatura());
                                    terminar = true;
                                    break;
                                }
                            }
                        }else{
                            break;
                        }
                    }
                }
            }
        }
        return criaturas;
    }
    
    public void seleccionarCriaturaAInvocar(BotonImagen panelCriatura){
        this.visBat.getVisSelCri().dispose();
        accion.setCriaturaAInvocar(this.visBat.getVisSelCri().getCriatura(panelCriatura));
        this.mostrarVistaSeleccionarDespliegue();
    }
    
    public boolean sePuedeInvocar(int[][] idxCasillas, int turno){
        // Comprobar si las casillas están disponibles
        if(this.tablero.estaDisponible(idxCasillas)){
            // Comprobar que el despliegue esté conectado al terreno del jugador
            if(this.tablero.estaConectadoAlTerreno(idxCasillas, (turno + 1))){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    
    public void invocarCriatura(SubVistaPosicion posicion)
{        int[][] despliegue = tablero.getDespliegue(posicion.getFila(), posicion.getColumna());
        
        if(this.sePuedeInvocar(despliegue, tablero.getTurnoActual())){
            this.accion.invocarCriatura(this.tablero.getPosicion(posicion.getFila(), posicion.getColumna()));
            asignarCasillas(despliegue, tablero.getTurnoActual());
            this.visBat.getTablero().actualizarCasillas();
            this.habilitarBotones();
            this.visBat.getInvocacion().setEnabled(false);
            this.tablero.setNumAccion(0);
        }else{
            this.visBat.setMensaje("No se puede invocar en la posición actual.");
        }
    }
    
// </editor-fold>  
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con la activación de magias">  
    
// </editor-fold>  

// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con el movimiento de criaturas"> 
    
    public boolean sePuedeMover(){
        if(this.tablero.getJugadorActual().getTurno().getPuntosMovimiento() != 0 &&
           terrenos.get(this.tablero.getTurnoActual()).cantidadCriaturasInvocadas() != 0){
            return true;
        }else{
            return false;
        }
    }
    
    public void solicitarSeleccionarCriatura(){
        this.visBat.setMensaje("Selecciona una criatura.");
        this.tablero.setNumAccion(10);
        this.deshabilitarBotones();
    }
    
    public boolean posicionTieneCriaturaDelJugador(Posicion posicionActual){
        if(this.terrenos.get(this.tablero.getTurnoActual()).contienePosicion(posicionActual) &&
                posicionActual.getElemento() instanceof Criatura){
            return true;
        }else{
            return false;
        }
    }
    
    public void setCriaturaAMover(SubVistaPosicion posicion){
        Posicion posicionActual = this.tablero.getPosicion(posicion.getFila(), posicion.getColumna());
        if(this.posicionTieneCriaturaDelJugador(posicionActual)){
            this.accion.setCriaturaAMover((Criatura) posicionActual.getElemento());
            this.cambiarEstadoCasillaCamino(posicion);
            this.visBat.setMensaje("Marca las casillas del camino.");
            this.tablero.setNumAccion(11);
        }else{
            this.visBat.setMensaje("Aquí no hay una criatura o no te pertenece.");
            this.visBat.getTablero().reiniciarCasillas();
        }
    }
    
    public void cambiarEstadoCasillaCamino(SubVistaPosicion casilla){
        Posicion posicionActual = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        if(this.accion.caminoContienePosicion(posicionActual)){
            System.out.println("Está");
            if(this.sePuedeEliminarCasilla(posicionActual)){
                System.out.println("Se eliminó");
                this.accion.eliminarPosicionDelCamino(posicionActual);
            }else{                
                casilla.setSelected(true);
            }
        }else{
            System.out.println("No está");
            if(this.accion.getLargoDelCamino() == 0 ||
               this.sePuedeAgregarCasilla(
               this.accion.obtenerUltimaPosicionAgregada().getFila(),
               this.accion.obtenerUltimaPosicionAgregada().getColumna(),
               posicionActual)){   
                System.out.println("Se agregó");
                this.accion.agregarPosicionAlCamino(posicionActual);
            }else{                
                casilla.setSelected(false);
            } 
        }
        
        if(this.accion.getLargoDelCamino() > 1){
            this.visBat.getMovimiento().setEnabled(true);
        }else{
            this.visBat.getMovimiento().setEnabled(false);
        }
    }
    
    public boolean sePuedeAgregarCasilla(int fila, int columna, Posicion posicion){
        if((this.accion.getLargoDelCamino() - 1) < this.tablero.getJugadorActual().getTurno().getPuntosMovimiento() &&
          (posicion.getElemento() == null || posicion.getElemento() instanceof Trampa)){
            int[][] vecinos = {{fila - 1, columna},
                               {fila + 1, columna},
                               {fila, columna - 1},
                               {fila, columna + 1}};

            for(int[] coord: vecinos){
                try{                    
                    if(this.tablero.getPosicion(coord[0], coord[1]).equals(posicion)){
                        return true;
                    }
                }catch(Exception e){
                    // Nada
                }
            }
        }else{
            return false;
        }
        
        return false;
    }
    
    public boolean sePuedeEliminarCasilla(Posicion posicion){
        return this.accion.obtenerUltimaPosicionAgregada().equals(posicion);
    }
    
    public void moverCriatura(){
        this.tablero.setNumAccion(0);
        this.visBat.getTablero().actualizarCasillas();
        
        Timer timerMovimiento = new Timer();
        timerMovimiento.schedule(new TimerTask(){
            int pasos = accion.getLargoDelCamino() - 1;
            int tic = 0;
            
            @Override
            public void run(){
                if(tic != pasos){
                    System.out.println("TIC: " + tic + " PASOS: " + pasos);
                    Posicion posAnt = accion.obtenerPosicionCamino(0);
                    Posicion posAct = accion.obtenerPosicionCamino(1);

                    ElementoEnCampo elemento = accion.moverCriaturaSiguientePosicion();
                    tablero.getJugadorActual().getTurno().descontarPuntoMovimiento();

                    visBat.getTablero().getCasilla(posAnt.getFila(), posAnt.getColumna()).setImagenIconoElemento(
                            "/Imagenes/vacio.png");
                    visBat.getTablero().getCasilla(posAnt.getFila(), posAnt.getColumna()).setImagenIconoElemento(
                            "/Imagenes/Criaturas/" + accion.getCriaturaAMover().getNomArchivoImagen() + ".png");

                    if(elemento instanceof Trampa){
                        ((Trampa) elemento).activarTrampa();
                        mostrarMensaje("Se ha activado una trampa.");
                        finalizarMovimiento();
                    }

                    tic++;
                }else{
                    finalizarMovimiento();        
                }
            }
            
            public void finalizarMovimiento(){
                actualizarVistaJugador(tablero.getTurnoActual());
                visBat.getTablero().reiniciarCasillas();
                accion.finalizarMovimiento();
                habilitarBotones();
                
                timerMovimiento.cancel();
            }
        }, 0, 500);
    }
    
    public void comprobarCasillaSeleccionCriatura(SubVistaPosicion casilla){
        this.visBat.getTablero().actualizarCasillas();
        if(this.tablero.getPosicion(casilla.getFila(), casilla.getColumna()).getDueno() == (this.tablero.getTurnoActual() + 1) &&
           this.tablero.getPosicion(casilla.getFila(), casilla.getColumna()).getElemento() instanceof Criatura){
            casilla.setImagenSobre("/Imagenes/Botones/casilla_correcta.png");
        }else{
            casilla.setImagenSobre("/Imagenes/Botones/casilla_error.png");
        }
        casilla.setImagenActual(1);
    }
    
    public void comprobarCasillaMovimiento(SubVistaPosicion casilla){
        Posicion posicionActual = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        this.visBat.getTablero().actualizarCasillas();
        if(!this.accion.caminoContienePosicion(posicionActual) && posicionActual.getDueno() != 0 && 
          (posicionActual.getElemento() == null || posicionActual.getElemento() instanceof Trampa)){
            casilla.setImagenSobre("/Imagenes/Botones/casilla_correcta.png");
        }else{
            casilla.setImagenSobre("/Imagenes/Botones/casilla_error.png");
        }
        casilla.setImagenActual(1);
    }
    
    public void mostrarMensajeNoSePuedeMover(){
        this.mostrarMensaje("No tienes criaturas para mover o no tienes puntos de movimiento.");
    }
    
// </editor-fold>   
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con colocar trampas">  
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con el turno">  
    
    public void habilitarBotones(){
        this.visBat.getAtaque().setEnabled(true);
        this.visBat.getInvocacion().setEnabled(true);
        this.visBat.getMagia().setEnabled(true);
        this.visBat.getMovimiento().setEnabled(true);
        this.visBat.getTrampa().setEnabled(true);
        this.visBat.getTerminarTurno().setEnabled(true);
    }
    
    public void deshabilitarBotones(){
        this.visBat.getAtaque().setEnabled(false);
        this.visBat.getInvocacion().setEnabled(false);
        this.visBat.getMagia().setEnabled(false);
        this.visBat.getMovimiento().setEnabled(false);
        this.visBat.getTrampa().setEnabled(false);
        this.visBat.getTerminarTurno().setEnabled(false);
    }
    
    public void cambiarTurno(){
        this.deshabilitarBotones();
        this.visBat.getTablero().reiniciarCasillas();
        this.tablero.cambiarTurno();
        
        Timer timerAnimacion = new Timer();
        timerAnimacion.schedule(new TimerTask(){
            SubVistaCambioTurno visCamTur = new SubVistaCambioTurno(tablero.getTurnoActual());
            int tic = 0;
            
            @Override
            public void run(){
                if(tic == 0){
                    contPrin.getContVisPrin().getVisPrin().agregarVista(visCamTur);
                    visCamTur.setVisible(true);
                }else{
                    visCamTur.dispose();
                    crearVistaSeleccionDados(tablero.getJugadorActual());
                    timerAnimacion.cancel();
                }
                tic++;
            }
        }, 0, 3500);
    }
    
// </editor-fold>  
    
    public void mostrarMensaje(String mensaje){
        this.visMen = new SubVistaCuadroDialogo("<html><center>" + mensaje + "</center></html>",
                "Aceptar", this.contPrin.getFuente());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visMen);
        this.visMen.setVisible(true);
    }
}